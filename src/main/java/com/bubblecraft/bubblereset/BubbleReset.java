package com.bubblecraft.bubblereset;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Deque;
import java.util.ArrayDeque;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
    private BukkitTask resetQueueTask;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.GameRule;
import org.bukkit.WorldBorder;
import org.bukkit.Location;
    private final Deque<String> resetQueue = new ArrayDeque<>();
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.command.PluginCommand;
import org.bukkit.BanList;
    // Start TPS-aware reset queue processor
    startResetQueueProcessor();

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
        if (resetQueueTask != null) {
            resetQueueTask.cancel();
            resetQueueTask = null;
        }
import java.util.Set;
import java.util.Collections;

                    if (elapsed >= intervalSec) {
                        String worldName = getWorldName(type);
                        if (worldName != null && Bukkit.getWorld(worldName) != null) {
                            enqueueReset(type);
                        }
                        resetTimers.put(type, now);
                    }
        // Save default config
        saveDefaultConfig();
        config = getConfig();
        

    private void startResetQueueProcessor() {
        if (resetQueueTask != null) {
            resetQueueTask.cancel();
        }
        resetQueueTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!resetting.isEmpty()) return; // already resetting a world
                String next;
                synchronized (resetQueue) {
                    next = resetQueue.peekFirst();
                }
                if (next == null) return;
                // Only run when TPS is healthy
                double threshold = config.getDouble("performance.tps-threshold", 18.0);
                double currentTps = getCurrentTps();
                if (currentTps < threshold) return;
                long gapTicks = Math.max(0L, config.getLong("performance.reset-gap-per-world-ticks", 20L * 30L));
                String worldType = next;
                synchronized (resetQueue) {
                    resetQueue.pollFirst();
                }
                String worldName = getWorldName(worldType);
                if (worldName == null) return;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        resetWorld(worldName, worldType);
                    }
                }.runTaskLater(BubbleReset.this, gapTicks);
            }
        }.runTaskTimer(this, 40L, 40L); // check ~2x/sec
    }

    private double getCurrentTps() {
        try {
            double[] tps = Bukkit.getServer().getTPS();
            if (tps != null && tps.length > 0) return Math.min(20.0, tps[0]);
        } catch (Throwable ignored) {}
        return 20.0; // assume good if unavailable
    }
        // Initialize menu
        enqueueReset(worldType);
        sender.sendMessage(prefixed("&aQueued reset for " + worldType + "."));
    // Register events
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(menu, this);
    getServer().getPluginManager().registerEvents(adminPanel, this);

        // Register tab-completion for /resource
        if (getCommand("resource") != null) {
            getCommand("resource").setTabCompleter(new ResourceTabCompleter());
        }
        
        // Setup PlaceholderAPI if available
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholderHook = new PlaceholderAPIHook(this);

    public void enqueueReset(String worldType) {
        String type = worldType.toLowerCase();
        if (resetting.contains(type)) return;
        synchronized (resetQueue) {
            if (!resetQueue.contains(type)) {
                resetQueue.addLast(type);
            }
        }
    }
            placeholderHook.register();
        }
        
        // Create resource worlds
        createResourceWorlds();
        
    // Start auto-reset scheduler
    startAutoResetScheduler();
        
    getLogger().info("BubbleReset enabled successfully!");
    }
    
    @Override
    public void onDisable() {
        if (placeholderHook != null) {
            placeholderHook.unregister();
        }
        if (autoResetTask != null) {
            autoResetTask.cancel();
            autoResetTask = null;
        }
        getLogger().info("BubbleReset disabled.");
    }
    
    private void createResourceWorlds() {
        String[] worldTypes = {"overworld", "nether", "end"};
        
        for (String type : worldTypes) {
            if (!isWorldEnabled(type)) continue;
            
            String worldName = getWorldName(type);
            if (Bukkit.getWorld(worldName) == null) {
                WorldCreator wc = new WorldCreator(worldName);
                wc.type(WorldType.valueOf(getWorldType(type)));
                wc.environment(World.Environment.valueOf(getEnvironment(type)));
                
                String seed = getSeed(type);
                if (seed != null && !seed.isEmpty()) {
                    try {
                        wc.seed(Long.parseLong(seed));
                    } catch (NumberFormatException e) {
                        wc.seed(seed.hashCode());
                    }
                }
                
                wc.generateStructures(getGenerateStructures(type));
                
                World world = Bukkit.createWorld(wc);
                if (world != null) {
                    setupWorld(world, type);
                    getLogger().info("Created resource world: " + worldName);
                }
            } else {
                setupWorld(Bukkit.getWorld(worldName), type);
            }
        }
    }
    
    private void setupWorld(World world, String type) {
        // Set spawn location
        int x = config.getInt("world-settings." + type + ".spawn-x", 0);
        int y = config.getInt("world-settings." + type + ".spawn-y", "nether".equalsIgnoreCase(type) ? 64 : 100);
        int z = config.getInt("world-settings." + type + ".spawn-z", 0);
    world.setSpawnLocation(x, y, z);
        
        // Set world border
        int borderSize = getBorderSize(type);
        WorldBorder border = world.getWorldBorder();
    border.setCenter(x + 0.5, z + 0.5);
    // Treat configured border size as diameter
    border.setSize(Math.max(128, borderSize));
    border.setWarningDistance(10);
    border.setDamageAmount(0.2);
        
    // Set game rules
    if (config.getConfigurationSection("gamerules") != null) {
    for (String rule : config.getConfigurationSection("gamerules").getKeys(false)) {
            Object value = config.get("gamerules." + rule);
            try {
                GameRule<?> gameRule = GameRule.getByName(rule);
                if (gameRule != null) {
                    if (value instanceof Boolean && gameRule.getType() == Boolean.class) {
                        @SuppressWarnings("unchecked")
                        GameRule<Boolean> boolRule = (GameRule<Boolean>) gameRule;
                        world.setGameRule(boolRule, (Boolean) value);
                    } else if (value instanceof Integer && gameRule.getType() == Integer.class) {
                        @SuppressWarnings("unchecked")
                        GameRule<Integer> intRule = (GameRule<Integer>) gameRule;
                        world.setGameRule(intRule, (Integer) value);
                    }
                }
            } catch (Exception e) {
                getLogger().warning("Could not set gamerule " + rule + ": " + e.getMessage());
            }
    }
    }
    }
    
    private void startAutoResetScheduler() {
        // Prefer per-world automated_resets if defined; else fall back to global auto-reset
        long globalIntervalSec = config.getLong("auto-reset.interval", 0L);
        long announceBeforeSec = config.getLong("auto-reset.announce-before", 300L);
        boolean anyEnabled = config.getBoolean("auto-reset.enabled", false)
            || config.getBoolean("world.automated_resets.enabled", false)
            || config.getBoolean("nether.automated_resets.enabled", false)
            || config.getBoolean("end.automated_resets.enabled", false);
        if (!anyEnabled && globalIntervalSec <= 0) {
            return; // scheduler disabled
        }
        if (autoResetTask != null) {
            autoResetTask.cancel();
        }
        // Single coordinator task ticks every minute to check per-world timers
        autoResetTask = new BukkitRunnable() {
            @Override
            public void run() {
                String[] types = new String[]{"overworld", "nether", "end"};
                long now = System.currentTimeMillis() / 1000L;
                for (int i = 0; i < types.length; i++) {
                    String type = types[i];
                    if (!isWorldEnabled(type)) continue;
                    // Determine interval (seconds)
                    long intervalSec = getResetIntervalSeconds(type);
                    if (intervalSec <= 0) intervalSec = globalIntervalSec;
                    if (intervalSec <= 0) continue; // no scheduler
                    long last = resetTimers.getOrDefault(type, 0L);
                    if (last == 0L) {
                        // Initialize on first pass and skip actions to avoid instant reset after startup
                        resetTimers.put(type, now);
                        continue;
                    }
                    long elapsed = now - last;
                    long warnAt = Math.max(0L, intervalSec - announceBeforeSec);
                    if (elapsed >= warnAt && elapsed < intervalSec) {
                        String warn = config.getString("messages.reset-warning", "&eResource world will reset in %time%!")
                            .replace("%time%", formatTime(intervalSec - elapsed));
                        Bukkit.broadcastMessage(prefixed(warn));
                    }
                    if (elapsed >= intervalSec) {
                        String worldName = getWorldName(type);
                        if (worldName != null && Bukkit.getWorld(worldName) != null) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    resetWorld(worldName, type);
                                }
                            }.runTaskLater(BubbleReset.this, i * 200L); // stagger by 10s per world
                        }
                        resetTimers.put(type, now);
                    }
                }
            }
        }.runTaskTimer(this, 1200L, 1200L);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("rwadmin")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(msg("console_message"));
                return true;
            }
            Player p = (Player) sender;
            if (!p.hasPermission("rw.admin")) {
                p.sendMessage(msg("no_perm"));
                return true;
            }
            adminPanel.open(p);
            return true;
        }
        if (!command.getName().equalsIgnoreCase("resource")) return false;
        
        // If no arguments provided, open the menu for players
        if (args.length == 0) {
            if (sender instanceof Player) {
                return handleMenu(sender);
            } else {
                sender.sendMessage(prefixed(ColorUtil.colorize("&e/resource <tp|menu|reset|reload> [world]")));
            }
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "tp":
                return handleTeleport(sender, args);
            case "menu":
                return handleMenu(sender);
            case "reset":
                return handleReset(sender, args);
            case "reload":
                return handleReload(sender);
            default:
                sender.sendMessage(prefixed(ColorUtil.colorize("&e/resource <tp|menu|reset|reload> [world]")));
                return true;
        }
    }
    
    private boolean handleTeleport(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(msg("console_message"));
            return true;
        }
        
        Player player = (Player) sender;
    String worldType = args.length > 1 ? args[1].toLowerCase() : "overworld";
        
    // Single permission for all resource world teleports
    if (!player.hasPermission("rw.tp")) {
            player.sendMessage(msg("no_perm"));
            return true;
        }
        
    String worldName = getWorldName(worldType);
        if (worldName == null) {
        player.sendMessage(msg("not_exist"));
            return true;
        }
        
    World world = getOrCreateWorld(worldType);
        if (world == null) {
        player.sendMessage(msg("not_exist"));
            return true;
        }
        
    Location safe;
    if ("nether".equalsIgnoreCase(worldType)) {
        safe = TeleportUtil.randomSafeLocationInNether(world);
    } else {
        safe = TeleportUtil.randomSafeLocation(world);
    }
    player.teleport(safe);
    player.sendMessage(msgOrDefault("teleported", "&aTeleported to the Resource World!"));
        
        return true;
    }
    
    private boolean handleMenu(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(msg("console_message"));
            return true;
        }
        
        Player player = (Player) sender;
        if (!player.hasPermission("rw.menu")) {
            player.sendMessage(msg("no_perm"));
            return true;
        }
        
        menu.openMenu(player);
        return true;
    }
    
    private boolean handleReset(CommandSender sender, String[] args) {
        if (!sender.hasPermission("rw.admin")) {
            sender.sendMessage(msg("no_perm"));
            return true;
        }
        
    String worldType = args.length > 1 ? args[1].toLowerCase() : "overworld";
    String worldName = getWorldName(worldType);
        
        if (worldName == null) {
            sender.sendMessage(msg("not_exist"));
            return true;
        }
        
        resetWorld(worldName, worldType);
    // Send a brief confirmation to the command sender; broadcasts are handled inside resetWorld
    sender.sendMessage(prefixed("&aReset started for " + worldType + "."));
        return true;
    }
    
    private boolean handleReload(CommandSender sender) {
        if (!sender.hasPermission("rw.admin")) {
            sender.sendMessage(msg("no_perm"));
            return true;
        }
        
        reloadConfig();
        config = getConfig();
    sender.sendMessage(msgOrDefault("reloaded", "&fYou have successfully reloaded the plugin!"));
        return true;
    }
    
    public void resetWorld(String worldName, String worldType) {
        // Prevent overlapping resets for the same world type
        if (!resetting.add(worldType.toLowerCase())) {
            return;
        }
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            // Broadcast start message
            String startKey = getResettingKey(worldType);
            Bukkit.broadcastMessage(msgOrDefault(startKey, defaultStart(worldType)));
            // Teleport all players out of the world
            Location mainWorldSpawn = getMainSpawn();
            int moved = 0;
            for (Player player : world.getPlayers()) {
                player.teleport(mainWorldSpawn);
                player.sendMessage(msg("teleported_message"));
                moved++;
            }
            if (moved > 0) Bukkit.broadcastMessage(msgOrDefault("teleported_players", "&aTeleported all the players back to spawn!"));
            // Unload the world (no save) to release files
            try { world.setAutoSave(false); } catch (Throwable ignored) {}
            Bukkit.unloadWorld(world, false);
            // Delete the world folder asynchronously with a short delay and retries, then recreate on main thread
            File toDelete = world.getWorldFolder();
            Bukkit.getScheduler().runTaskLaterAsynchronously(this, () -> {
                for (int i = 0; i < 3; i++) {
                    deleteWorldFolder(toDelete);
                    if (!toDelete.exists()) break;
                    try { Thread.sleep(500); } catch (InterruptedException ignored) {}
                }
                Bukkit.getScheduler().runTask(this, () -> recreateWorld(worldName, worldType));
            }, 100L); // ~5s
            return;
        }
        // If world was already absent, just create it
        recreateWorld(worldName, worldType);
    }

    private void recreateWorld(String worldName, String worldType) {
    WorldCreator wc = new WorldCreator(worldName);
    wc.type(WorldType.valueOf(getWorldType(worldType)));
    wc.environment(World.Environment.valueOf(getEnvironment(worldType)));

    String seed = getSeed(worldType);
        if (seed != null && !seed.isEmpty()) {
            try {
                wc.seed(Long.parseLong(seed));
            } catch (NumberFormatException e) {
                wc.seed(seed.hashCode());
            }
        }

    wc.generateStructures(getGenerateStructures(worldType));

        World newWorld = Bukkit.createWorld(wc);
    if (newWorld != null) {
            setupWorld(newWorld, worldType);
            // Execute commands_after_reset if configured (new schema)
            for (String cmd : getPostResetCommands(worldType)) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
            }
            // Optionally run Chunky to pre-generate after reset for performance
            if (getServer().getPluginManager().getPlugin("Chunky") != null && config.getBoolean("chunky.enabled", false)) {
                boolean syncToBorder = config.getBoolean("chunky.sync-to-border", true);
                int radius = syncToBorder ? Math.max(64, getBorderSize(worldType) / 2) : Math.max(128, config.getInt("chunky.radius", Math.max(64, getBorderSize(worldType) / 2)));
                String center = config.getString("chunky.center", "0 0");
                String dim = newWorld.getName();
                long chunkyDelay = config.getLong("performance.schedule-chunky-delay-ticks", 20L * 60L);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chunky world " + dim);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chunky center " + center);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chunky radius " + radius);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "chunky start");
                    }
                }.runTaskLater(this, Math.max(0L, chunkyDelay));
            }
            // Broadcast finish message
            String doneKey = getFinishedKey(worldType);
            Bukkit.broadcastMessage(msgOrDefault(doneKey, defaultFinish(worldType)));
        }
        // Clear resetting flag at the very end of recreation
        resetting.remove(worldType.toLowerCase());
    }

    public World getOrCreateWorld(String worldType) {
    String worldName = getWorldName(worldType);
        World world = Bukkit.getWorld(worldName);
        if (world != null) return world;
        // Create if missing based on config
    WorldCreator wc = new WorldCreator(worldName);
    wc.type(WorldType.valueOf(getWorldType(worldType)));
    wc.environment(World.Environment.valueOf(getEnvironment(worldType)));
    String seed = getSeed(worldType);
        if (seed != null && !seed.isEmpty()) {
            try {
                wc.seed(Long.parseLong(seed));
            } catch (NumberFormatException e) {
                wc.seed(seed.hashCode());
            }
        }
    wc.generateStructures(getGenerateStructures(worldType));
        World created = Bukkit.createWorld(wc);
        if (created != null) {
            setupWorld(created, worldType);
        }
        return created;
    }
    
    private void deleteWorldFolder(File folder) {
        if (folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                deleteWorldFolder(file);
            }
        }
        folder.delete();
    }
    
    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        boolean overrideNether = config.getBoolean("portals.override-nether-portals") ||
            config.getBoolean("nether.portals.override", false);
        boolean overrideEnd = config.getBoolean("portals.override-end-portals") ||
            config.getBoolean("end.portals.override", false);
        if (!overrideNether && !overrideEnd) {
            return;
        }
        
        Player player = event.getPlayer();
        
        // Override portal destinations if configured
        switch (event.getCause()) {
            case NETHER_PORTAL:
                if (overrideNether) {
                    String netherWorld = getWorldName("nether");
                    World nether = Bukkit.getWorld(netherWorld);
                    if (nether != null && player.hasPermission("rw.tp")) {
                        event.setTo(nether.getSpawnLocation());
                    }
                }
                break;
            case END_PORTAL:
                if (overrideEnd) {
                    String endWorld = getWorldName("end");
                    World end = Bukkit.getWorld(endWorld);
                    if (end != null && player.hasPermission("rw.tp")) {
                        event.setTo(end.getSpawnLocation());
                    }
                }
                break;
        }
    }
    
    private String formatTime(long seconds) {
        long minutes = seconds / 60;
        long hours = minutes / 60;
        
        if (hours > 0) {
            return hours + " hour" + (hours != 1 ? "s" : "");
        } else if (minutes > 0) {
            return minutes + " minute" + (minutes != 1 ? "s" : "");
        } else {
            return seconds + " second" + (seconds != 1 ? "s" : "");
        }
    }
    
    public FileConfiguration getPluginConfig() {
        return config;
    }
    
    public Map<String, Long> getResetTimers() {
        return resetTimers;
    }

    private String prefixed(String message) {
        String prefix = getConfig().getString("messages.prefix", "");
        String raw = message == null ? "" : message;
        if (prefix != null && !prefix.isEmpty() && raw.contains(prefix)) {
            return ColorUtil.colorize(raw);
        }
        return ColorUtil.colorize((prefix != null ? prefix : "") + raw);
    }

    private String msg(String key) {
        String raw = getConfig().getString("messages." + key, "");
        return prefixed(raw);
    }

    private String msgOrDefault(String key, String def) {
        String raw = getConfig().getString("messages." + key);
        if (raw == null || raw.trim().isEmpty()) raw = def;
        return prefixed(raw);
    }

    private String defaultStart(String type) {
        switch (type.toLowerCase()) {
            case "nether": return "&fCleaning up the Nether World, This may cause Lag!";
            case "end": return "&fCleaning up the End World, This may cause Lag!";
            default: return "&fCleaning up the Resource World, This may cause Lag!";
        }
    }

    private String defaultFinish(String type) {
        switch (type.toLowerCase()) {
            case "nether": return "&fThe Nether World has been Reset!";
            case "end": return "&fThe End World has been Reset!";
            default: return "&fThe Resource World has been Reset!";
        }
    }

    private Location randomSafeLocationInNether(World world) {
        var border = world.getWorldBorder();
        double centerX = border.getCenter().getX();
        double centerZ = border.getCenter().getZ();
        double radius = Math.max(16.0, border.getSize() / 2 - 64);
        for (int tries = 0; tries < 30; tries++) {
            double x = centerX + (Math.random() * 2 - 1) * radius;
            double z = centerZ + (Math.random() * 2 - 1) * radius;
            int bx = (int) Math.round(x);
            int bz = (int) Math.round(z);
            // target Y just under roof bedrock (bedrock roof at 127)
            int y = 123;
            // scan downward to find air space with solid ground
            for (int yy = y; yy > world.getMinHeight() + 10; yy--) {
                Location feet = new Location(world, x + 0.5, yy, z + 0.5);
                var blockFeet = world.getBlockAt(feet);
                var blockHead = blockFeet.getRelative(0, 1, 0);
                var ground = blockFeet.getRelative(0, -1, 0);
                if (blockFeet.isPassable() && blockHead.isPassable() && ground.getType().isSolid()) {
                    return new Location(world, x + 0.5, yy, z + 0.5);
                }
            }
        }
        return world.getSpawnLocation();
    }

    private String getResettingKey(String type) {
        switch (type.toLowerCase()) {
            case "nether":
                return "resetting_the_nether";
            case "end":
                return "resetting_the_end";
            default:
                return "resetting_the_world";
        }
    }

    private String getFinishedKey(String type) {
        switch (type.toLowerCase()) {
            case "nether":
                return "nether_has_been_reset";
            case "end":
                return "end_has_been_reset";
            default:
                return "world_has_been_reset";
        }
    }

    // =========================
    // Config compatibility helpers
    // =========================
    private String sectionFor(String type) {
        return "overworld".equalsIgnoreCase(type) ? "world" : type.toLowerCase();
    }

    private boolean isWorldEnabled(String type) {
        // New path: world.enabled / nether.enabled / end.enabled
        String section = sectionFor(type);
        if (config.isSet(section + ".enabled")) {
            return config.getBoolean(section + ".enabled", true);
        }
        return config.getBoolean("world-settings." + type + ".enabled", true);
    }

    private String getWorldName(String type) {
        String legacy = config.getString("worlds." + type);
        if (legacy != null) return legacy;
        String section = sectionFor(type);
        return config.getString(section + ".world_name");
    }

    private String getWorldType(String type) {
        String legacy = config.getString("world-settings." + type + ".world-type");
        if (legacy != null) return legacy;
        return config.getString(sectionFor(type) + ".world_type", "NORMAL");
    }

    private String getEnvironment(String type) {
        String legacy = config.getString("world-settings." + type + ".environment");
        if (legacy != null) return legacy;
        return config.getString(sectionFor(type) + ".environment", "NORMAL");
    }

    private boolean getGenerateStructures(String type) {
        if (config.isSet("world-settings." + type + ".generate-structures"))
            return config.getBoolean("world-settings." + type + ".generate-structures");
        return config.getBoolean(sectionFor(type) + ".generate_structures", true);
    }

    private String getSeed(String type) {
        String legacy = config.getString("world-settings." + type + ".seed");
        if (legacy != null && !legacy.isEmpty()) return legacy;
        String base = sectionFor(type) + ".custom_seed";
        if (config.getBoolean(base + ".enabled", false)) {
            return String.valueOf(config.get("" + base + ".seed"));
        }
        return "";
    }

    private int getBorderSize(String type) {
        if (config.isSet("world-settings." + type + ".border-size"))
            return config.getInt("world-settings." + type + ".border-size", 1000);
        String base = sectionFor(type) + ".world_border";
        if (config.getBoolean(base + ".enabled", true)) {
            return config.getInt(base + ".size", 1000);
        }
        return 1000;
    }

    private long getResetIntervalSeconds(String type) {
        String base = sectionFor(type) + ".automated_resets";
        if (config.getBoolean(base + ".enabled", false)) {
            // interval in hours
            long hours = config.getLong(base + ".interval", 0L);
            return hours > 0 ? hours * 3600L : 0L;
        }
        return 0L;
    }

    private java.util.List<String> getPostResetCommands(String type) {
        String base = sectionFor(type) + ".commands_after_reset";
        if (config.getBoolean(base + ".enabled", false)) {
            return config.getStringList(base + ".commands");
        }
        return java.util.Collections.emptyList();
    }

    private Location getMainSpawn() {
        String worldName = config.getString("settings.main_spawn_world");
        if (worldName != null) {
            World w = Bukkit.getWorld(worldName);
            if (w != null) return w.getSpawnLocation();
        }
        return Bukkit.getWorlds().get(0).getSpawnLocation();
    }
}
