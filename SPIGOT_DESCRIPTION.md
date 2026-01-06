# BubbleReset - Modern Resource World Plugin

🌍 **A comprehensive and feature-rich resource world management plugin for Minecraft 1.19 - 1.21.x**

BubbleReset is a complete replacement for traditional resource world plugins, designed from the ground up for modern Spigot/Paper servers. It provides automatic world resets, intuitive GUI menus, PlaceholderAPI integration, and advanced performance optimization.

---

## ✨ Key Features

### 🌐 Multi-World Support
- **Overworld, Nether, and End** resource worlds
- Individual configuration for each dimension
- Custom seeds and world types (Normal, Flat, Large Biomes, Amplified)
- Configurable world borders with damage and warnings
- Per-world game rule customization

### ⚡ Intelligent Reset System
- **Automated scheduled resets** with configurable intervals or fixed time-of-day
- **TPS-aware reset queue** - resets pause during server lag
- **Advance warnings** before resets with customizable announcements
- **Staggered resets** to minimize server impact
- **Safe player evacuation** - teleports players to spawn before reset
- **Post-reset commands** - run Chunky or custom commands after resets

### 🎨 Interactive GUI Menu
- **Beautiful custom menu** with player head icons
- **Gradient color titles** using HEX color codes
- **One-click teleportation** to any resource world
- **Customizable layout** - change items, positions, and textures (supports Base64 head textures)
- Simple permission-based access control

### 🔌 PlaceholderAPI Integration
Display real-time information in scoreboards, holograms, and chat:
- `%rw_world%` - Time until overworld reset
- `%rw_nether%` - Time until nether reset
- `%rw_end%` - Time until end reset
- `%rw_world_players%` - Players in resource overworld
- `%rw_nether_players%` - Players in resource nether
- `%rw_end_players%` - Players in resource end

### 🚪 Portal Override System
- **Redirect vanilla portals** to resource worlds
- **Configurable per dimension** - override nether and/or end portals
- Seamlessly integrate resource worlds into normal gameplay

### 📍 Smart Teleportation
- **Random safe locations** within world borders
- **Nether-aware algorithm** - avoids lava and finds solid ground
- **Configurable cooldowns** and delays
- **Cost system** (optional economy integration)
- **Potion effects** on teleport for added flair

### ⚙️ Performance Optimized
- **TPS monitoring** - pauses heavy operations during lag
- **Async world deletion** with automatic retries
- **Queue-based reset system** prevents overlapping operations
- **Configurable delays** between world resets
- **View distance reduction** during resets
- **Chunky integration** for world pre-generation

### 🛠️ Admin Tools
- **/rwadmin** - Interactive admin panel with GUI
- **Manual world resets** - reset any world on demand
- **Config reload** without server restart
- **Detailed logging** of all operations
- **Queue management** - view and control reset queue

---

## 📋 Commands

| Command | Description | Permission | Aliases |
|---------|-------------|------------|---------|
| `/resource` | Open resource world menu | `rw.menu` | `/rw`, `/resourceworld` |
| `/resource tp [world]` | Teleport to resource world | `rw.tp` | - |
| `/resource menu` | Open the GUI menu | `rw.menu` | - |
| `/resource reset [world]` | Manually reset a world | `rw.admin` | - |
| `/resource reload` | Reload configuration | `rw.admin` | - |
| `/rwadmin` | Open admin control panel | `rw.admin` | `/rwa` |

---

## 🔐 Permissions

- `rw.tp` - Access to teleport to resource worlds (default: **true**)
- `rw.menu` - Access to resource world menu (default: **true**)
- `rw.admin` - Access to admin commands and panel (default: **op**)

*Note: Legacy permissions (`rw.tp.nether`, `rw.tp.end`) are maintained for backwards compatibility but are no longer required.*

---

## ⚙️ Configuration Highlights

```yaml
# Per-World Settings
world:
  enabled: true
  teleport_enabled: true
  world_name: resource_world
  world_type: NORMAL  # NORMAL, FLAT, LARGE_BIOMES, AMPLIFIED
  environment: NORMAL  # NORMAL, NETHER, THE_END
  world_border:
    enabled: true
    size: 4500
  automated_resets:
    enabled: true
    interval: 168  # hours
    # Optional: run resets at a fixed server-local time instead of by interval
    # Format: HH:mm (example: 02:00)
    time_of_day: ""

# Performance Tuning
performance:
  tps-threshold: 18.0  # Minimum TPS to process resets
  reset-gap-per-world-ticks: 600  # Delay between resets

# Teleport Settings
teleport_settings:
  cooldown: 300  # seconds
  delay: 3  # seconds
  effects:
    enabled: true
    effect: ABSORPTION
```

---

## 📦 Installation

1. Download **bubblereset-1.1.2.jar**
2. Place it in your server's `plugins/` folder
3. **(Optional)** Install [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) for placeholder support
4. Start/restart your server
5. Configure worlds in `plugins/BubbleReset/config.yml`
6. Run `/resource reload` to apply changes

---

## 🎯 Requirements

- **Minecraft Version:** 1.19 - 1.21.x
- **Server Software:** Spigot, Paper, or any Paper fork
- **Java Version:** Java 21 or higher
- **Dependencies:** None required (PlaceholderAPI optional)

---

## 🔧 Compatibility

✅ **Spigot** - Full support  
✅ **Paper** - Full support with enhanced features  
✅ **Purpur** - Full support  
✅ **Folia** - Not yet tested  
✅ **PlaceholderAPI** - Optional integration  
✅ **Chunky** - Optional pre-generation support  
✅ **Vault** - Optional economy support (planned)

---

## 💡 Why BubbleReset?

- **Modern Design** - Built specifically for modern Minecraft versions
- **Performance First** - Intelligent systems prevent server lag
- **Highly Configurable** - Customize every aspect to your needs
- **User Friendly** - Intuitive commands and beautiful GUIs
- **Active Development** - Regular updates and bug fixes
- **Clean Code** - Well-structured for easy customization

---

## 📝 Changelog

### Version 1.1.2
- Time-of-day automated resets (`automated_resets.time_of_day`)
- Datapacks copying support for resource worlds
- Per-world teleport toggle (`teleport_enabled`)
- Custom menu heads via Base64 textures

### Version 1.0.0
- Initial release
- Multi-world resource system (Overworld, Nether, End)
- Automated reset scheduling with TPS awareness
- Interactive GUI menu with custom heads
- PlaceholderAPI integration
- Portal override system
- Safe random teleportation
- Admin control panel
- Performance optimizations
- Configurable game rules per world
- Post-reset command execution
- World border support

---

## 🐛 Bug Reports & Feature Requests

Found a bug or have a feature request? Please report it on our [GitHub Issues](https://github.com/yourusername/BubbleReset/issues) page.

---

## 📄 License

This plugin is licensed under the **MIT License**. You are free to use, modify, and distribute this plugin.

---

## ❤️ Support

If you enjoy this plugin, please leave a ⭐ review and consider supporting development!

**Credits:** Inspired by the original Resource World plugin. This is a complete rewrite with modern features and optimizations.

---

## 📸 Screenshots

[Add screenshots of the GUI menu, admin panel, and world borders here]

---

**Download BubbleReset today and give your players a fresh, renewable resource experience!**
