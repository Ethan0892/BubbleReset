package com.bubblecraft.bubblereset;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TeleportToggleTest {

    private ServerMock server;
    private BubbleReset plugin;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        server.addSimpleWorld("world");
        plugin = MockBukkit.load(BubbleReset.class);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void teleportCommandBlockedWhenNetherTeleportDisabled() {
        PlayerMock player = server.addPlayer();
        player.addAttachment(plugin, "rw.tp", true);

        plugin.getPluginConfig().set("nether.teleport_enabled", false);

        boolean ok = server.dispatchCommand(player, "resource tp nether");
        assertTrue(ok);

        String msg = player.nextMessage();
        assertNotNull(msg);
        assertTrue(msg.contains("Teleport to this world is currently disabled"), msg);
        assertEquals("world", player.getWorld().getName());
    }

    @Test
    void menuClickBlockedWhenNetherTeleportDisabled() {
        PlayerMock player = server.addPlayer();
        player.addAttachment(plugin, "rw.menu", true);
        player.addAttachment(plugin, "rw.tp", true);

        plugin.getPluginConfig().set("nether.teleport_enabled", false);

        boolean ok = server.dispatchCommand(player, "resource");
        assertTrue(ok);
        assertNotNull(player.getOpenInventory());

        ItemStack netherItem = player.getOpenInventory().getTopInventory().getItem(13);
        assertNotNull(netherItem);
        assertNotNull(netherItem.getItemMeta());
        assertNotNull(netherItem.getItemMeta().getLore());

        // Nether item is configured at slot 13 with action "nether" in default config.yml
        InventoryClickEvent click = new InventoryClickEvent(
            player.getOpenInventory(),
            InventoryType.SlotType.CONTAINER,
            13,
            ClickType.LEFT,
            InventoryAction.PICKUP_ALL
        );
        click.setCurrentItem(netherItem);
        server.getPluginManager().callEvent(click);

        assertTrue(click.isCancelled(), "Expected menu click to be cancelled");

        String msg = player.nextMessage();
        assertNotNull(msg);
        assertTrue(msg.contains("Teleport to this world is currently disabled"), msg);
        assertEquals("world", player.getWorld().getName());
    }
}
