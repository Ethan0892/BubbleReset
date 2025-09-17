package com.bubblecraft.bubblereset;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

public final class TeleportUtil {
    private TeleportUtil() {}

    public static Location randomSafeLocation(World world) {
        var border = world.getWorldBorder();
        double centerX = border.getCenter().getX();
        double centerZ = border.getCenter().getZ();
        double radius = Math.max(32.0, border.getSize() / 2 - 100); // keep margin and min radius

        for (int tries = 0; tries < 20; tries++) {
            double x = centerX + (ThreadLocalRandom.current().nextDouble() * 2 - 1) * radius;
            double z = centerZ + (ThreadLocalRandom.current().nextDouble() * 2 - 1) * radius;
            int bx = (int) Math.round(x);
            int bz = (int) Math.round(z);

            int y = world.getHighestBlockYAt(bx, bz);
            if (y <= world.getMinHeight()) continue;

            Location loc = new Location(world, x + 0.5, y + 1.0, z + 0.5);
            if (isLocationSafe(loc)) {
                return loc;
            }
        }
        // Fallback to spawn if no safe spot found
        return world.getSpawnLocation().add(new Vector(0.5, 1.0, 0.5));
    }

    /**
     * Finds a safe location in the Nether, scanning downward from just below the bedrock roof
     * and ensuring a two-block-tall air space with solid ground, avoiding lava, fire, magma, etc.
     */
    public static Location randomSafeLocationInNether(World world) {
        var border = world.getWorldBorder();
        double centerX = border.getCenter().getX();
        double centerZ = border.getCenter().getZ();
        double radius = Math.max(16.0, border.getSize() / 2 - 64);

        for (int tries = 0; tries < 30; tries++) {
            double x = centerX + (ThreadLocalRandom.current().nextDouble() * 2 - 1) * radius;
            double z = centerZ + (ThreadLocalRandom.current().nextDouble() * 2 - 1) * radius;
            int startY = Math.min(123, world.getMaxHeight() - 5);
            for (int yy = startY; yy > world.getMinHeight() + 10; yy--) {
                Location loc = new Location(world, x + 0.5, yy, z + 0.5);
                if (isLocationSafe(loc)) {
                    return loc;
                }
            }
        }
        return world.getSpawnLocation();
    }

    private static boolean isLocationSafe(Location loc) {
        World world = loc.getWorld();
        if (world == null) return false;
        Block feet = world.getBlockAt(loc);
        Block head = feet.getRelative(0, 1, 0);
        Block ground = feet.getRelative(0, -1, 0);
        return isPassable(feet) && isPassable(head) && isSolid(ground.getType());
    }

    private static boolean isPassable(Block block) {
        return block.isPassable() && block.getType() != Material.LAVA && block.getType() != Material.FIRE;
    }

    private static boolean isSolid(Material mat) {
        return mat.isSolid() && mat != Material.CACTUS && mat != Material.MAGMA_BLOCK;
    }
}
