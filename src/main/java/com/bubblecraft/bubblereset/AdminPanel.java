package com.bubblecraft.bubblereset;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class AdminPanel implements Listener {
    private final BubbleReset plugin;
    private final NamespacedKey key;

    public AdminPanel(BubbleReset plugin) {
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, "rw_admin_action");
    }

    public void open(Player player) {
        if (!player.hasPermission("rw.admin")) {
            player.sendMessage(pref(plugin, "no_perm"));
            return;
        }
        Inventory inv = Bukkit.createInventory(null, 27, ColorUtil.colorize("<#ffae00>Resource World Admin"));
        inv.setItem(10, button(Material.TNT, "&cReset Overworld", "reset:overworld"));
        inv.setItem(12, button(Material.TNT, "&cReset Nether", "reset:nether"));
        inv.setItem(14, button(Material.TNT, "&cReset End", "reset:end"));
        inv.setItem(16, button(Material.EMERALD, "&aRun Chunky (All)", "chunky:all"));
        player.openInventory(inv);
    }

    private ItemStack button(Material mat, String name, String action) {
        ItemStack is = new ItemStack(mat);
        ItemMeta meta = is.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ColorUtil.colorize(name));
            meta.setLore(Arrays.asList(ColorUtil.colorize("&7Action: &f" + action)));
            meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, action);
            is.setItemMeta(meta);
        }
        return is;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        String title = ColorUtil.colorize("<#ffae00>Resource World Admin");
        if (!e.getView().getTitle().equals(title)) return;
        e.setCancelled(true);
        ItemStack item = e.getCurrentItem();
        if (item == null || !item.hasItemMeta()) return;
        String action = item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
        if (action == null) return;
        Player p = (Player) e.getWhoClicked();
        if (!p.hasPermission("rw.admin")) {
            p.sendMessage(ColorUtil.colorize("&cNo permission."));
            return;
        }
        if (action.startsWith("reset:")) {
            String type = action.substring("reset:".length());
            String worldName = plugin.getPluginConfig().getString("worlds." + type);
            if (worldName != null) {
                // Let resetWorld handle broadcasting
                plugin.resetWorld(worldName, type);
            }
        } else if (action.equals("chunky:all")) {
            if (Bukkit.getPluginManager().getPlugin("Chunky") == null) {
                p.sendMessage(pref(plugin, "teleport_error"));
                return;
            }
            for (String type : new String[]{"overworld", "nether", "end"}) {
                String worldName = plugin.getPluginConfig().getString("worlds." + type);
                if (worldName != null) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chunky world " + worldName);
                    int radius = Math.max(128, plugin.getPluginConfig().getInt("chunky.radius", 2000));
                    String center = plugin.getPluginConfig().getString("chunky.center", "0 0");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chunky center " + center);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chunky radius " + radius);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chunky start");
                }
            }
            p.sendMessage(ColorUtil.colorize("&aStarted Chunky pregeneration for all resource worlds."));
        }
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
