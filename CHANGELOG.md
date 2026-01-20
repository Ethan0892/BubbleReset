# BubbleReset Changelog

## [1.1.3] - 2026-01-20

### Maintenance
- Version bumped to 1.1.3.
- Reduced cyclomatic complexity in key hotspots (refactor only; no functional changes).

### Code Quality
- Addressed high-complexity methods identified by static analysis:
  - BubbleReset.java [289-319] — scheduler tick logic extracted into helper methods.
  - ResourceWorldMenu.java [239-301] — click handling simplified with action-extraction helpers.
  - ResourceWorldMenu.java [122-185] — skull texture application split into small helpers.

---

## [1.1.2-1] - 2026-01-07

### Added
- **New `/rwadmin reload` command** - Dedicated reload command with enhanced feedback
  - Displays reload time in milliseconds
  - Reports number of reset timers cleared
  - Shows cached worlds cleared count
  - Confirms config cache rebuild status
  
- **New `/rwadmin test <feature>` command** - Comprehensive testing system for diagnostics
  - `cache` - View world and config cache contents with entry counts
  - `timer` - Display next reset times for all worlds (shows remaining hours/minutes)
  - `performance` - Show operation performance metrics (teleport, reset timings)
  - `tps` - Check current server TPS with color-coded health indicators (green/yellow/red)
  - `config` - Verify configuration loading for all worlds (displays name, auto-reset status, interval/time_of_day mode)
  
- **Enhanced tab completion** - Added full tab completion support for `/rwadmin` subcommands and test features

### Improved
- **Reload command feedback** - More detailed output showing exactly what was cleared and recalculated
- **World cache management** - Reload now properly clears world cache in addition to config cache and reset timers
- **Admin panel access** - `/rwadmin` with no arguments opens admin panel, or use `/rwadmin panel` explicitly
- **Console compatibility** - Reload and test commands now work from console, not just in-game

### Fixed
- Continued fix from v1.1.1: Reset timers properly clear on reload, ensuring `time_of_day` changes take effect immediately

### Technical
- Refactored command handler to support subcommands (panel, reload, test)
- Added dedicated test methods for each diagnostic feature
- Updated ResourceTabCompleter to handle both `/resource` and `/rwadmin` commands
- Improved error handling and permission checks for admin commands

---

## [1.1.1] - 2026-01-07

### Fixed
- **Critical reload bug** - `/resource reload` now properly clears reset timers map
  - `time_of_day` configuration changes now take effect immediately on reload
  - Previously cached reset times are recalculated based on new config values
  - No longer requires server restart for `time_of_day` changes

### Improved
- **Reload functionality** - Enhanced `handleReload()` method to clear all caches and restart scheduler

---

## [1.1.0] - 2026-01-07

### Added
- **Performance improvements**
  - World caching using `ConcurrentHashMap<String, World>` for faster lookups
  - Config value caching to reduce file I/O operations
  - Async chunk pre-loading system with configurable radius
  - `CompletableFuture` async operations for world deletion
  - Performance monitoring and statistics logging
  - TPS-aware reset queue processor

- **Version compatibility expansion**
  - Extended support from Minecraft 1.21.8 to full 1.19-1.21.x range
  - Updated Paper API to 1.20.1-R0.1-SNAPSHOT for broader compatibility

- **New configuration options**
  - `performance.log-statistics` - Enable/disable statistics logging
  - `performance.log-reset-times` - Log reset operation timings
  - `performance.preload-chunk-radius` - Set chunk pre-loading radius
  - `performance.slow-operation-threshold-ms` - Threshold for slow operation warnings

### Improved
- Optimized teleport performance with world caching
- Enhanced reset operation with async processing
- Better memory management with proper cache invalidation

---

## [1.0.0] - 2026-01-07

### Initial Release
- Resource world management for Overworld, Nether, and End dimensions
- Automated world resets with two modes:
  - Interval-based resets (configurable in hours)
  - Time-of-day resets (HH:mm format)
- GUI menu system for world selection
- Admin panel for world management
- PlaceholderAPI integration for dynamic placeholders
- Safe teleportation with location validation
- Configurable world names and settings
- Permission system for access control
- Command aliases: `/rw`, `/resource`, `/rwadmin`, `/rwa`
- Tab completion for all commands
- Full color code support in messages
- Multi-world support with separate configurations
