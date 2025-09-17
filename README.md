# BubbleReset - Resource World Plugin

A comprehensive replacement for the popular Resource World plugin, designed for Minecraft 1.21.8 and compatible with Spigot/Paper servers.

## Features

✅ **Multiple World Support**: Overworld, Nether, and End resource worlds  
✅ **Automatic Scheduled Resets**: Configurable intervals with advance warnings  
✅ **Interactive Menu System**: Easy-to-use GUI for world teleportation  
✅ **PlaceholderAPI Integration**: Display reset timers and player counts  
✅ **Portal Override**: Redirect vanilla portals to resource worlds  
✅ **Random Teleportation**: Safe random teleports within world borders  
✅ **Configurable Game Rules**: Customize world behavior  
✅ **Permission System**: Fine-grained access control  
✅ **World Border Support**: Automatic world border setup  
✅ **Customizable Messages**: Fully customizable plugin messages

## Commands

- `/resource tp [world]` - Teleport to resource world (overworld/nether/end)
- `/resource menu` - Open the resource world menu
- `/resource reset [world]` - Reset a specific resource world (admin only)
- `/resource reload` - Reload plugin configuration (admin only)

**Aliases**: `/rw`, `/resourceworld`

## Permissions

- `rw.tp` - Access to teleport to resource overworld (default: true)
- `rw.tp.nether` - Access to teleport to resource nether (default: false)
- `rw.tp.end` - Access to teleport to resource end (default: false)
- `rw.admin` - Access to admin commands (default: op)
- `rw.menu` - Access to resource world menu (default: true)

## PlaceholderAPI Placeholders

- `%rw_world%` - Shows time until overworld reset
- `%rw_nether%` - Shows time until nether reset  
- `%rw_end%` - Shows time until end reset
- `%rw_world_players%` - Number of players in resource overworld
- `%rw_nether_players%` - Number of players in resource nether
- `%rw_end_players%` - Number of players in resource end

## Installation

1. Download the latest BubbleReset.jar file
2. Place it in your server's `plugins` folder
3. Start/restart your server
4. Configure the plugin in `plugins/BubbleReset/config.yml`
5. Use `/resource reload` to apply changes

## Building from Source

### Prerequisites
- Java 21 or higher
- Maven 3.6 or higher

### Build Steps
1. Clone this repository
2. Navigate to the project directory
3. Run: `mvn clean package`
4. The compiled JAR will be in the `target` directory

## Configuration

The plugin creates a `config.yml` file with extensive customization options:

- **World Settings**: Configure each world type, environment, seeds, and borders
- **Auto-Reset**: Set intervals and warning times for automatic resets
- **Portal Overrides**: Redirect vanilla portals to resource worlds
- **Game Rules**: Set custom game rules for resource worlds
- **Messages**: Customize all plugin messages
- **Menu**: Configure the GUI menu appearance

## Compatibility

- **Minecraft Versions**: 1.21.8 (should work with 1.20.x and 1.21.x)
- **Server Software**: Spigot, Paper, and forks
- **Java**: Requires Java 21 or higher
- **Dependencies**: None (PlaceholderAPI optional)

## Support

If you encounter any issues or need help:

1. Check the console for error messages
2. Ensure you have the correct permissions set
3. Verify your configuration file syntax
4. Make sure your server has enough memory for multiple worlds

## License

This project is licensed under the MIT License. Feel free to modify and distribute as needed.

## Credits

Inspired by the original Resource World plugin by NikV2. This is a complete rewrite with enhanced features and improved performance for modern Minecraft versions.
