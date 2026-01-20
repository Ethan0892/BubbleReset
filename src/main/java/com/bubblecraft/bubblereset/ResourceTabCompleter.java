package com.bubblecraft.bubblereset;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ResourceTabCompleter implements TabCompleter {
    private static final List<String> SUBS = Arrays.asList("tp", "menu", "reset", "reload");
    private static final List<String> WORLDS = Arrays.asList("overworld", "nether", "end");
    private static final List<String> ADMIN_SUBS = Arrays.asList("panel", "reload", "test");
    private static final List<String> TEST_FEATURES = Arrays.asList("cache", "timer", "performance", "tps", "config");

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("rwadmin")) {
            if (args.length == 1) {
                return prefix(ADMIN_SUBS, args[0]);
            }
            if (args.length == 2 && "test".equalsIgnoreCase(args[0])) {
                return prefix(TEST_FEATURES, args[1]);
            }
            return new ArrayList<>();
        }
        
        if (!command.getName().equalsIgnoreCase("resource")) return null;
        if (args.length == 1) {
            return prefix(SUBS, args[0]);
        }
        if (args.length == 2 && ("tp".equalsIgnoreCase(args[0]) || "reset".equalsIgnoreCase(args[0]))) {
            return prefix(WORLDS, args[1]);
        }
        return new ArrayList<>();
    }

    private List<String> prefix(List<String> pool, String start) {
        String s = start == null ? "" : start.toLowerCase(Locale.ROOT);
        return pool.stream().filter(it -> it.startsWith(s)).collect(Collectors.toList());
    }
}
