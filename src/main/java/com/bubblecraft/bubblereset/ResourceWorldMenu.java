package com.bubblecraft.bubblereset;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.URL;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResourceWorldMenu implements Listener {
    
    private final BubbleReset plugin;
    private static final Pattern HEX_PATTERN = Pattern.compile("<#([A-Fa-f0-9]{6})>");
    
    public ResourceWorldMenu(BubbleReset plugin) {
        this.plugin = plugin;
    }
    
    public void openMenu(Player player) {
    String title = ColorUtil.colorize(plugin.getPluginConfig().getString("menu.title"));
        int rows = plugin.getPluginConfig().getInt("menu.rows", 3);
        int size = rows * 9;
        
        Inventory menu = Bukkit.createInventory(null, size, title);
        
        // Get menu items from config
        if (plugin.getPluginConfig().contains("menu.items")) {
            for (String slotKey : plugin.getPluginConfig().getConfigurationSection("menu.items").getKeys(false)) {
                try {
                    int slot = Integer.parseInt(slotKey);
                    if (slot >= 0 && slot < size) {
                        ItemStack item = createCustomMenuItem(slotKey);
                        if (item != null) {
                            menu.setItem(slot, item);
                        }
                    }
                } catch (NumberFormatException e) {
                    plugin.getLogger().warning("Invalid slot number in menu config: " + slotKey);
                }
            }
        }
        
        player.openInventory(menu);
    }
    
    private ItemStack createCustomMenuItem(String slotKey) {
        String basePath = "menu.items." + slotKey;
        String material = plugin.getPluginConfig().getString(basePath + ".material");
        String name = plugin.getPluginConfig().getString(basePath + ".name");
        String texture = plugin.getPluginConfig().getString(basePath + ".texture");
        String action = plugin.getPluginConfig().getString(basePath + ".action");
        
        if (material == null || name == null) {
            return null;
        }
        
        ItemStack item;
        if ("PLAYER_HEAD".equals(material) && texture != null) {
            item = createCustomSkull(texture);
        } else {
            try {
                item = new ItemStack(Material.valueOf(material));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid material: " + material);
                return null;
            }
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ColorUtil.colorize(name));
            
            // Add lore based on action
            List<String> lore = Arrays.asList(
                "§7Click to teleport to the",
                "§7" + action + " resource world!",
                "",
                "§eAction: §f" + action
            );
            meta.setLore(lore);

            // Store action in PDC (more reliable than hash)
            NamespacedKey key = new NamespacedKey(plugin, "rw_action");
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, action);
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    private ItemStack createCustomSkull(String textureBase64) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        
        if (meta != null) {
            try {
                // Decode the base64 texture
                String decoded = new String(Base64.getDecoder().decode(textureBase64));
                
                // Extract URL from the decoded JSON
                Pattern urlPattern = Pattern.compile("\"url\":\"([^\"]+)\"");
                Matcher matcher = urlPattern.matcher(decoded);
                
                if (matcher.find()) {
                    String textureUrl = matcher.group(1);
                    
                    // Create player profile with custom texture
                    PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID(), "CustomHead");
                    PlayerTextures textures = profile.getTextures();
                    textures.setSkin(new URL(textureUrl));
                    profile.setTextures(textures);
                    
                    meta.setOwnerProfile(profile);
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to set custom skull texture: " + e.getMessage());
            }
            
            skull.setItemMeta(meta);
        }
        
        return skull;
    }
    
    private String translateHexColorCodes(String message) {
        if (message == null) return "";
        
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer();
        
        while (matcher.find()) {
            String hex = matcher.group(1);
            matcher.appendReplacement(buffer, "§x§" + hex.charAt(0) + "§" + hex.charAt(1) + 
                                              "§" + hex.charAt(2) + "§" + hex.charAt(3) + 
                                              "§" + hex.charAt(4) + "§" + hex.charAt(5));
        }
        matcher.appendTail(buffer);
        
        return buffer.toString();
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
    String title = ColorUtil.colorize(plugin.getPluginConfig().getString("menu.title"));
        
        if (!event.getView().getTitle().equals(title)) return;
        
        event.setCancelled(true);
        
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;
        
    ItemMeta meta = clicked.getItemMeta();
    if (meta == null) return;
        
    // Get action from PersistentDataContainer
    NamespacedKey key = new NamespacedKey(plugin, "rw_action");
    PersistentDataContainer pdc = meta.getPersistentDataContainer();
    String action = pdc.get(key, PersistentDataType.STRING);
        
        if (action == null) return;
        
        // Check permissions and teleport
        String permission = "rw.tp";
        if (!player.hasPermission(permission)) {
            player.sendMessage(pref(plugin, "no_perm"));
            return;
        }
        
        teleportToWorld(player, action);
    }
    
    private void teleportToWorld(Player player, String worldType) {
        String worldName = plugin.getPluginConfig().getString("worlds." + worldType);
        World world = plugin.getServer().getWorld(worldName);
        if (world == null && plugin instanceof BubbleReset) {
            world = ((BubbleReset) plugin).getOrCreateWorld(worldType);
        }
        
        if (world == null) {
            player.sendMessage(pref(plugin, "not_exist"));
            return;
        }
        
        player.closeInventory();
        
        org.bukkit.Location safe = "nether".equalsIgnoreCase(worldType)
            ? TeleportUtil.randomSafeLocationInNether(world)
            : TeleportUtil.randomSafeLocation(world);
        player.teleport(safe);
        player.sendMessage(pref(plugin, "teleported"));
    }

    private String pref(BubbleReset plugin, String key) {
        String prefix = plugin.getConfig().getString("messages.prefix", "");
        String raw = plugin.getConfig().getString("messages." + key);
        if (raw == null || raw.trim().isEmpty()) raw = "&f" + key;
        if (prefix != null && !prefix.isEmpty() && raw.contains(prefix)) {
            return ColorUtil.colorize(raw);
        }
        return ColorUtil.colorize((prefix != null ? prefix : "") + raw);
    }
}
