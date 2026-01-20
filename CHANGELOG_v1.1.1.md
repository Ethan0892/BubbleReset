# BubbleReset v1.1.1 - Datapacks & Teleport Controls

**Release Date:** January 6, 2026  
**Plugin Version:** 1.1.1  
**Minecraft Versions:** 1.19 - 1.21.x  
**File:** bubblereset-1.1.1.jar

---

## ✨ New Features

### Datapacks support (custom structures / loot tables)
- New `datapacks` config section to automatically copy datapacks into freshly created resource worlds.
- Optional `/minecraft:reload` after copying so datapacks apply without a full restart.
- Configurable source (`world:<name>` or `folder:<path>`) and per-dimension apply toggles.

### Per-world teleport toggle
- New `teleport_enabled` option per dimension:
  - `world.teleport_enabled`
  - `nether.teleport_enabled`
  - `end.teleport_enabled`
- Blocks teleports from both `/resource tp <world>` and the `/resource` GUI when disabled.

---

## 🛠️ Improvements
- Added clear notes in `config.yml` explaining default `/resource` GUI behavior and permissions.
- Improved GUI click handling to identify the BubbleReset menu reliably (uses a dedicated inventory holder).
- Added a fallback to read the menu action from item lore when PDC is unavailable.

---

## 🧪 Testing
- Added MockBukkit + JUnit 5 tests validating per-world teleport blocking.

---

## ⚙️ Configuration Highlights

```yaml
# Disable Nether teleports but keep Nether reset logic enabled
nether:
  enabled: true
  teleport_enabled: false

# Enable datapacks copying from your main world
# (copies <server>/world/datapacks into each newly created resource world)
datapacks:
  enabled: true
  source: "world:world"
  run_minecraft_reload: true
```

---

## 📌 Notes
- `enabled: false` under `world`/`nether`/`end` only disables reset/auto-create logic for that dimension.
- Use `teleport_enabled: false` to hard-disable teleports per world.
