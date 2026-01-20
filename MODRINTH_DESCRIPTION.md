# BubbleReset

**Modern Resource World Management for Minecraft Servers**

A comprehensive and high-performance resource world plugin designed for Minecraft 1.21.8+. BubbleReset provides automated resets, intuitive GUI menus, PlaceholderAPI integration, and intelligent performance optimization.

---

## Features

### 🌍 Multi-World Resource System
Manage separate resource worlds for Overworld, Nether, and End dimensions with individual configurations, custom seeds, and world borders.

### ⏰ Intelligent Automated Resets
- Scheduled resets with configurable intervals per world
- TPS-aware queue system that pauses during server lag
- Advance warning announcements before resets
- Staggered resets to minimize server impact
- Safe player evacuation to spawn

### 🎨 Beautiful Interactive GUI
- Custom menu with gradient titles and player head icons
- One-click teleportation to resource worlds
- Fully customizable layout and textures
- Permission-based access control

### 📊 PlaceholderAPI Integration
Display real-time information in scoreboards and chat:
- Reset timers for each world
- Player counts in each dimension
- Fully compatible with any PAPI-supporting plugin

### 🚪 Portal Override
Redirect vanilla Nether and End portals to your resource worlds, seamlessly integrating them into normal gameplay.

### 📍 Smart Teleportation
- Random safe spawn locations within world borders
- Nether-aware algorithm (avoids lava, finds solid ground)
- Configurable cooldowns, delays, and costs
- Optional potion effects on teleport

### ⚡ Performance Optimized
- TPS monitoring prevents heavy operations during lag
- Async world deletion with retry logic
- Queue-based system prevents overlapping resets
- Chunky integration for world pre-generation
- Configurable view distance reduction during resets

### 🛠️ Admin Control Panel
- `/rwadmin` - Interactive GUI for server administrators
- Manual world reset controls
- Live configuration reload
- Queue management and monitoring

---

## Commands

- `/resource` - Open resource world menu (aliases: `/rw`, `/resourceworld`)
- `/resource tp [world]` - Teleport to a resource world
- `/resource menu` - Open the GUI menu
- `/resource reset [world]` - Manually reset a world (admin)
- `/resource reload` - Reload configuration (admin)
- `/rwadmin` - Open admin panel (alias: `/rwa`)

---

## Permissions

- `rw.tp` - Teleport to resource worlds (default: true)
- `rw.menu` - Access GUI menu (default: true)
- `rw.admin` - Admin commands and panel (default: op)

---

## Requirements

- **Minecraft:** 1.21.8 (compatible with 1.20.x - 1.21.x)
- **Server:** Spigot, Paper, or Paper forks
- **Java:** 21 or higher
- **Dependencies:** None (PlaceholderAPI optional)

---

## Installation

1. Download the plugin JAR file
2. Place in your `plugins/` folder
3. (Optional) Install PlaceholderAPI for placeholder support
4. Start/restart your server
5. Configure in `plugins/BubbleReset/config.yml`
6. Use `/resource reload` to apply changes

---

## Configuration Example

```yaml
world:
  enabled: true
  world_name: resource_world
  world_type: NORMAL  # NORMAL, FLAT, LARGE_BIOMES, AMPLIFIED
  environment: NORMAL
  world_border:
    enabled: true
    size: 4500
  automated_resets:
    enabled: true
    interval: 168  # hours
  commands_after_reset:
    enabled: false
    commands:
      - chunky radius 2250
      - chunky start resource_world

performance:
  tps-threshold: 18.0
  reset-gap-per-world-ticks: 600

teleport_settings:
  cooldown: 300
  delay: 3
  effects:
    enabled: true
    effect: ABSORPTION
```

---

## PlaceholderAPI Placeholders

- `%rw_world%` - Time until overworld reset
- `%rw_nether%` - Time until nether reset
- `%rw_end%` - Time until end reset
- `%rw_world_players%` - Players in resource overworld
- `%rw_nether_players%` - Players in resource nether
- `%rw_end_players%` - Players in resource end

---

## Why Choose BubbleReset?

✅ **Modern Architecture** - Built for current Minecraft versions  
✅ **Performance First** - TPS-aware systems prevent lag  
✅ **Highly Configurable** - Customize every detail  
✅ **User Friendly** - Intuitive commands and GUIs  
✅ **Active Development** - Regular updates and improvements  
✅ **Clean Codebase** - Easy to understand and modify

---

## Compatibility

- ✅ Spigot
- ✅ Paper (recommended)
- ✅ Purpur
- ✅ PlaceholderAPI (optional)
- ✅ Chunky (optional)

---

## Support

For bug reports, feature requests, or support, please visit our [GitHub repository](https://github.com/yourusername/BubbleReset).

---

## License

Licensed under the MIT License. Free to use, modify, and distribute.

---

## Credits

Inspired by the original Resource World plugin. Complete rewrite with modern features and optimizations.

**Author:** eirvi  
**Version:** 1.0.0  
**MC Version:** 1.21.8
