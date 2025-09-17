package com.bubblecraft.bubblereset;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIHook extends PlaceholderExpansion {
    
    private final BubbleReset plugin;
    
    public PlaceholderAPIHook(BubbleReset plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public @NotNull String getIdentifier() {
        return "rw";
    }
    
    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }
    
    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }
    
    @Override
    public boolean persist() {
        return true;
    }
    
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        
        switch (params.toLowerCase()) {
            case "world":
                return getWorldInfo("overworld");
            case "nether":
                return getWorldInfo("nether");
            case "end":
                return getWorldInfo("end");
            case "world_players":
                return getWorldPlayerCount("overworld");
            case "nether_players":
                return getWorldPlayerCount("nether");
            case "end_players":
                return getWorldPlayerCount("end");
            default:
                return null;
        }
    }
    
    private String getWorldInfo(String worldType) {
        String worldName = plugin.getPluginConfig().getString("worlds." + worldType);
        World world = Bukkit.getWorld(worldName);
        
        if (world == null) {
            return "World not found";
        }
        
        // Calculate time until next reset (if auto-reset is enabled)
        if (plugin.getPluginConfig().getBoolean("auto-reset.enabled")) {
            long interval = plugin.getPluginConfig().getLong("auto-reset.interval");
            long currentTime = System.currentTimeMillis() / 1000;
            
            // This is a simplified calculation - in a real implementation you'd want to track actual reset times
            long nextReset = interval - (currentTime % interval);
            return formatDuration(nextReset);
        }
        
        return "Auto-reset disabled";
    }
    
    private String getWorldPlayerCount(String worldType) {
        String worldName = plugin.getPluginConfig().getString("worlds." + worldType);
        World world = Bukkit.getWorld(worldName);
        
        if (world == null) {
            return "0";
        }
        
        return String.valueOf(world.getPlayers().size());
    }
    
    private String formatDuration(long seconds) {
        long days = seconds / 86400;
        long hours = (seconds % 86400) / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        
        StringBuilder sb = new StringBuilder();
        
        if (days > 0) {
            sb.append(days).append("d ");
        }
        if (hours > 0) {
            sb.append(hours).append("h ");
        }
        if (minutes > 0) {
            sb.append(minutes).append("m ");
        }
        if (secs > 0 || sb.length() == 0) {
            sb.append(secs).append("s");
        }
        
        return sb.toString().trim();
    }
}
