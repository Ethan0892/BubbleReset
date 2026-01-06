# BubbleReset - Release Notes

## BubbleReset v1.1.2

**Release Date:** January 6, 2026  
**Plugin Version:** 1.1.2  
**Minecraft Versions:** 1.19 - 1.21.x  
**File:** bubblereset-1.1.2.jar

### Highlights
- Time-of-day automated resets (`automated_resets.time_of_day`, format `HH:mm`)
- Datapacks support for custom structures/loot tables in resource worlds
- Per-world teleport toggle (`teleport_enabled`) for overworld/nether/end
- Custom `/resource` menu heads via Base64 textures (fixes heads showing Steve)

---

## BubbleReset v1.0.0

**Release Date:** December 15, 2025  
**Plugin Version:** 1.0.0  
**Minecraft Version:** 1.21.8 (Compatible with 1.20.x - 1.21.x)  
**File:** bubblereset-1.0.0.jar (40 KB)

---

## 🎉 Initial Release

BubbleReset is a modern, feature-rich resource world management plugin built from the ground up for Minecraft 1.21.8 and Paper servers. This is the first stable release ready for production use.

---

## ✨ Features Included

### Core Functionality
- **Multi-World Support** - Manage Overworld, Nether, and End resource worlds independently
- **Automated Resets** - Schedule automatic world resets with configurable intervals per world
- **TPS-Aware System** - Intelligent queue system that pauses during server lag
- **Player Safety** - Automatic player evacuation to spawn before resets
- **Advance Warnings** - Customizable announcements before world resets

### User Interface
- **Interactive GUI Menu** - Beautiful custom menu with player head icons and gradient titles
- **Admin Control Panel** - `/rwadmin` command for server administrators
- **Tab Completion** - Smart tab completion for all commands
- **Custom Messages** - Fully customizable messages with color code support

### Integration
- **PlaceholderAPI Support** - 6 placeholders for scoreboards and holograms
  - Reset timers for each world
  - Player counts in each dimension
- **Portal Override** - Redirect vanilla portals to resource worlds
- **Chunky Integration** - Optional world pre-generation after resets

### Teleportation
- **Smart Random Teleports** - Safe random locations within world borders
- **Nether-Aware Algorithm** - Special logic for safe nether spawns
- **Cooldown System** - Configurable teleport cooldowns
- **Teleport Delay** - Configurable delay before teleport
- **Potion Effects** - Optional effects on teleport

### Performance
- **Async Operations** - World deletion happens asynchronously
- **Staggered Resets** - Automatic delays between world resets
- **View Distance Control** - Reduce view distance during operations
- **Retry Logic** - Automatic retries for file operations

### Configuration
- **Per-World Settings** - Individual configuration for each dimension
- **Custom Seeds** - Set custom seeds for each world
- **World Types** - Support for NORMAL, FLAT, LARGE_BIOMES, AMPLIFIED
- **World Borders** - Configurable borders with damage and warnings
- **Game Rules** - Per-world game rule customization
- **Post-Reset Commands** - Execute commands after world resets

---

## 📋 Commands

- `/resource` - Open resource world menu
- `/resource tp [world]` - Teleport to resource world (overworld/nether/end)
- `/resource menu` - Open the GUI menu
- `/resource reset [world]` - Manually reset a world (admin)
- `/resource reload` - Reload configuration (admin)
- `/rwadmin` - Open admin control panel

**Aliases:** `/rw`, `/resourceworld`, `/rwa`

---

## 🔐 Permissions

- `rw.tp` - Access to teleport to resource worlds (default: true)
- `rw.menu` - Access to resource world menu (default: true)
- `rw.admin` - Access to admin commands and panel (default: op)

*Note: Legacy permissions (`rw.tp.nether`, `rw.tp.end`) maintained for compatibility*

---

## ⚙️ Requirements

- **Minecraft:** 1.21.8 recommended (1.20.x - 1.21.x compatible)
- **Server:** Spigot, Paper, or Paper forks (Paper recommended)
- **Java:** Java 21 or higher **REQUIRED**
- **Dependencies:** None (PlaceholderAPI optional)

---

## 📥 Installation

1. Download `bubblereset-1.0.0.jar`
2. Place in your server's `plugins/` folder
3. **(Optional)** Install PlaceholderAPI for placeholder support
4. Start or restart your server
5. The plugin will generate `plugins/BubbleReset/config.yml`
6. Configure your world settings
7. Run `/resource reload` to apply changes
8. Test with `/resource menu`

---

## 🆕 What's New in 1.0.0

This is the initial release, featuring:

- Complete multi-world resource management system
- Intelligent TPS-aware reset queue
- Beautiful interactive GUI menus
- Full PlaceholderAPI integration
- Portal override functionality
- Smart teleportation system
- Admin control panel
- Performance optimizations
- Extensive configuration options
- Backward compatibility with legacy configs

---

## 🐛 Known Issues

No known issues at this time. Please report any bugs on the issue tracker.

---

## 🔜 Planned Features (v1.1.0+)

- Vault economy integration
- Multi-language support (i18n)
- Database storage for reset timers
- World backup system before resets
- Custom world generator support
- Web-based dashboard
- Per-player teleport cooldowns
- Warp points within resource worlds
- Reset history and statistics
- Discord webhook notifications

---

## 📝 Configuration Example

```yaml
# Per-World Configuration
world:
  enabled: true
  world_name: resource_world
  world_type: NORMAL
  environment: NORMAL
  world_border:
    enabled: true
    size: 4500
  automated_resets:
    enabled: true
    interval: 168  # hours (7 days)

# Performance Settings
performance:
  tps-threshold: 18.0
  reset-gap-per-world-ticks: 600

# Teleportation
teleport_settings:
  cooldown: 300  # seconds
  delay: 3
  effects:
    enabled: true
    effect: ABSORPTION
```

---

## 🔧 Compatibility

✅ **Fully Tested:**
- Paper 1.21.8
- Spigot 1.21.8
- PlaceholderAPI 2.11.6

✅ **Should Work:**
- Paper 1.20.1 - 1.21.8
- Spigot 1.20.1 - 1.21.8
- Purpur 1.20.1 - 1.21.8
- Chunky world pre-generation

❓ **Not Tested:**
- Folia (multi-threaded)
- Vault economy integration (planned for v1.1.0)

---

## 💡 Upgrade Path

This is the first release, so no upgrades are necessary. Future versions will include:
- Automatic config migration
- Backwards compatibility
- Clear upgrade instructions

---

## 🆘 Support

Need help? Here's how to get support:

1. **Documentation:** Check the README.md and config.yml comments
2. **Issues:** Report bugs on GitHub Issues
3. **Questions:** Ask in the discussion forum
4. **Discord:** Join our Discord server (link in resource description)

**Before Reporting Issues:**
- Ensure you're using Java 21 or higher
- Check console for error messages
- Try `/resource reload`
- Verify your config.yml syntax

---

## 📄 License

BubbleReset is licensed under the MIT License. You are free to:
- Use commercially
- Modify
- Distribute
- Use privately

See LICENSE file for full terms.

---

## 🙏 Credits

- **Original Inspiration:** Resource World plugin by NikV2
- **Author:** eirvi
- **Libraries:** Spigot/Paper API, PlaceholderAPI

Special thanks to the Spigot and Paper development teams for their excellent APIs and documentation.

---

## 📊 Statistics

- **Lines of Code:** ~750
- **Java Files:** 7
- **Commands:** 5 main commands
- **Permissions:** 3 main nodes
- **Placeholders:** 6
- **Configuration Options:** 50+
- **Supported Worlds:** 3 (Overworld, Nether, End)

---

## ⚠️ Important Notes

1. **Java 21 Required:** This plugin will NOT work with Java 8, 11, or 17
2. **Paper Recommended:** While Spigot is supported, Paper provides better performance
3. **Config Backup:** Always backup your config before updating
4. **World Backups:** Consider backing up worlds before first reset
5. **Test First:** Test on a development server before production use

---

## 📞 Contact

- **SpigotMC:** [Your Resource Page]
- **Modrinth:** [Your Modrinth Page]
- **GitHub:** [Your GitHub Repository]
- **Discord:** [Your Discord Server]

---

## 🎯 Next Steps After Installation

1. Edit `config.yml` to set your world names
2. Configure reset intervals (in hours)
3. Customize messages and colors
4. Set up permissions for players
5. Test the `/resource menu` command
6. (Optional) Install PlaceholderAPI
7. (Optional) Set up portal overrides
8. (Optional) Configure Chunky integration

---

**Thank you for using BubbleReset!**

We hope this plugin enhances your server's resource world management. If you enjoy it, please consider leaving a review and sharing with others!

---

*BubbleReset v1.0.0 - Built with ❤️ for the Minecraft community*
