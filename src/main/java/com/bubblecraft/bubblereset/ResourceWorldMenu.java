package com.bubblecraft.bubblereset;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.ChatColor;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ResourceWorldMenu implements Listener {
    
    private final BubbleReset plugin;
    private static final Pattern HEX_PATTERN = Pattern.compile("<#([A-Fa-f0-9]{6})>");
    
    public ResourceWorldMenu(BubbleReset plugin) {
        this.plugin = plugin;
    }

    private static final class MenuHolder implements InventoryHolder {
        @Override
        public Inventory getInventory() {
            return null;
        }
    }
    
    public void openMenu(Player player) {
    String title = ColorUtil.colorize(plugin.getPluginConfig().getString("menu.title"));
        int rows = plugin.getPluginConfig().getInt("menu.rows", 3);
        int size = rows * 9;
        
        Inventory menu = Bukkit.createInventory(new MenuHolder(), size, title);
        
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

        if (meta == null) return skull;

        try {
            String texture = normalizeTexture(textureBase64);
            if (!texture.isEmpty()) {
                PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID(), "CustomHead");
                applyTextureToProfile(profile, texture);
                meta.setOwnerProfile(profile);
            }
        } catch (Exception e) {
            // MockBukkit doesn't implement parts of the profile/skull API.
            // Avoid spamming warnings during unit tests while still logging real server failures.
            String msg = e.getMessage();
            if (!isMockBukkit() && (msg == null || !msg.equalsIgnoreCase("Not implemented"))) {
                plugin.getLogger().warning("Failed to set custom skull texture: " + msg);
            }
        }

        skull.setItemMeta(meta);
        
        return skull;
    }

    private String normalizeTexture(String textureBase64) {
        return textureBase64 == null ? "" : textureBase64.trim();
    }

    private void applyTextureToProfile(PlayerProfile profile, String texture) throws Exception {
        if (texture.startsWith("http://") || texture.startsWith("https://")) {
            applySkinUrl(profile, normalizeTexturesUrl(texture));
            return;
        }

        String base64 = ensureBase64Textures(texture);

        // Prefer setting the raw base64 "textures" property (most reliable; no external fetch).
        // The required API type isn't exposed on Bukkit in all versions, so we use reflection.
        boolean applied = applyTexturesPropertyReflectively(profile, base64);
        if (applied) return;

        // Fallback: decode -> URL -> setSkin (may still work if property API isn't present)
        String decoded = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
        String url = extractUrlFromTexturesPayload(decoded);
        if (url != null && !url.isEmpty()) {
            applySkinUrl(profile, normalizeTexturesUrl(url));
        }
    }

    private void applySkinUrl(PlayerProfile profile, String url) throws Exception {
        PlayerTextures textures = profile.getTextures();
        textures.setSkin(new URL(url));
        profile.setTextures(textures);
    }

    private String normalizeTexturesUrl(String url) {
        if (url.startsWith("http://textures.minecraft.net/")) {
            return "https://" + url.substring("http://".length());
        }
        return url;
    }

    private String ensureBase64Textures(String texture) {
        if (texture.startsWith("{")) {
            return Base64.getEncoder().encodeToString(texture.getBytes(StandardCharsets.UTF_8));
        }

        // Validate base64 early so we can log clearly if it's invalid
        Base64.getDecoder().decode(texture);
        return texture;
    }

    private String extractUrlFromTexturesPayload(String decodedJsonOrPayload) {
        Pattern urlPattern = Pattern.compile("\\\"url\\\":\\\"([^\\\"]+)\\\"");
        Matcher matcher = urlPattern.matcher(decodedJsonOrPayload);
        return matcher.find() ? matcher.group(1) : null;
    }

    private boolean isMockBukkit() {
        try {
            if (Bukkit.getServer() == null) return false;
            String name = Bukkit.getServer().getName();
            return name != null && name.toLowerCase().contains("mockbukkit");
        } catch (Throwable ignored) {
            return false;
        }
    }

    private boolean applyTexturesPropertyReflectively(PlayerProfile profile, String base64Textures) {
        String[] candidatePropertyClasses = new String[] {
            "com.destroystokyo.paper.profile.ProfileProperty",
            "io.papermc.paper.profile.ProfileProperty"
        };

        for (String className : candidatePropertyClasses) {
            try {
                Class<?> propertyClass = Class.forName(className);
                Constructor<?> ctor = propertyClass.getConstructor(String.class, String.class);
                Object prop = ctor.newInstance("textures", base64Textures);

                // Common method name on Paper profile implementations
                Method m = profile.getClass().getMethod("setProperty", propertyClass);
                m.invoke(profile, prop);
                return true;
            } catch (NoSuchMethodException ignored) {
                // Try next class name
            } catch (Throwable ignored) {
                // Try next class name
            }
        }

        return false;
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
        Player player = asPlayer(event);
        if (player == null) return;

        if (!isResourceWorldMenu(event)) return;
        event.setCancelled(true);

        String action = extractAction(event.getCurrentItem());
        if (action == null) return;

        if (!player.hasPermission("rw.tp")) {
            player.sendMessage(pref(plugin, "no_perm"));
            return;
        }

        if (!plugin.isTeleportEnabled(action)) {
            player.sendMessage(pref(plugin, "teleport_disabled"));
            return;
        }

        teleportToWorld(player, action);
    }

    private Player asPlayer(InventoryClickEvent event) {
        return (event.getWhoClicked() instanceof Player) ? (Player) event.getWhoClicked() : null;
    }

    private boolean isResourceWorldMenu(InventoryClickEvent event) {
        Inventory top = event.getView() != null ? event.getView().getTopInventory() : null;
        return top != null && (top.getHolder() instanceof MenuHolder);
    }

    private String extractAction(ItemStack clicked) {
        if (clicked == null || clicked.getType() == Material.AIR) return null;
        ItemMeta meta = clicked.getItemMeta();
        if (meta == null) return null;

        String action = extractActionFromPdc(meta);
        if (action != null) return action;

        return extractActionFromLore(meta.getLore());
    }

    private String extractActionFromPdc(ItemMeta meta) {
        NamespacedKey key = new NamespacedKey(plugin, "rw_action");
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String action = pdc.get(key, PersistentDataType.STRING);
        return (action == null || action.trim().isEmpty()) ? null : action.trim().toLowerCase();
    }

    private String extractActionFromLore(List<String> lore) {
        if (lore == null) return null;

        for (String line : lore) {
            String stripped = ChatColor.stripColor(line);
            if (stripped == null) continue;
            stripped = stripped.trim();
            int idx = stripped.toLowerCase().indexOf("action:");
            if (idx < 0) continue;

            String after = stripped.substring(idx + "action:".length()).trim();
            if (!after.isEmpty()) return after.toLowerCase();
        }

        return null;
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
