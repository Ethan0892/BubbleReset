# BubbleReset v1.1.2 - Scheduled Resets & Custom Heads

**Release Date:** January 6, 2026  
**Plugin Version:** 1.1.2  
**Minecraft Versions:** 1.19 - 1.21.x  
**File:** bubblereset-1.1.2.jar

---

## ✨ New Features

### Time-of-day automated resets
- Added `automated_resets.time_of_day` (format `HH:mm`) under each dimension.
- When set, the auto-reset schedule runs at that local server time instead of using the interval timer.
- Existing interval-based scheduling still works as a fallback when `time_of_day` is blank.

---

## 🛠️ Fixes

### Custom menu heads (Base64 textures)
- Fixed `/resource` menu custom head textures showing as Steve.
- Head textures are now applied using the Base64 `textures` value reliably (preferred) with URL/JSON fallback.
- Config now documents that `menu.items.<slot>.texture` supports Base64 (recommended) or a textures URL.

### Test log noise (MockBukkit)
- Suppressed repeated `Failed to set custom skull texture: Not implemented` warnings when running under MockBukkit.
- Real texture failures still log normally on live servers.

---

## ⚙️ Configuration Highlights

```yaml
world:
  automated_resets:
    # If set, overrides interval-based resets (server-local time)
    time_of_day: "02:00"

menu:
  items:
    11:
      material: "PLAYER_HEAD"
      # Base64 textures value (recommended) or textures.minecraft.net URL
      texture: "<base64>"
```
