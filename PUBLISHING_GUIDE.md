# Quick Publishing Guide - BubbleReset

## 📦 Files Ready for Distribution

**Main Plugin:** `target/bubblereset-1.0.0.jar` (Ready to upload)

**Documentation:**
- `SPIGOT_DESCRIPTION.md` - For Spigot resource page
- `MODRINTH_DESCRIPTION.md` - For Modrinth listing
- `README.md` - For GitHub repository
- `BUILD_SUMMARY.md` - Build and feature reference

---

## 🚀 SpigotMC Publishing

### URL: https://www.spigotmc.org/resources/

### Resource Information:
- **Title:** BubbleReset - Modern Resource World Plugin
- **Tag Line:** Advanced resource world management with automated resets, GUI menus, and PlaceholderAPI support
- **Category:** Admin Tools › World Management
- **Price:** Free (or Premium - your choice)

### Resource Fields:
- **Version:** 1.0.0
- **Tested Versions:** 1.20, 1.21
- **Contributors:** eirvi
- **Native Version:** 1.21
- **License:** MIT License

### Description:
Copy content from `SPIGOT_DESCRIPTION.md`

### Icon:
Recommended size: 256x256 PNG
Suggestion: Create an icon with:
- Green/blue gradient background
- World/globe symbol
- "BubbleReset" text

### Screenshots to Add:
1. GUI menu showing three resource worlds
2. Admin panel interface
3. In-game chat showing reset warnings
4. Config file example
5. PlaceholderAPI scoreboard integration

---

## 🎮 Modrinth Publishing

### URL: https://modrinth.com/

### Project Information:
- **Title:** BubbleReset
- **Summary:** Modern resource world management with automated resets and GUI menus
- **Project Type:** Plugin
- **Client Side:** Unsupported
- **Server Side:** Required
- **License:** MIT

### Categories:
- Utility
- Management
- World Generation

### Loaders:
- Spigot
- Paper
- Purpur

### Game Versions:
- 1.20.1 - 1.20.6
- 1.21 - 1.21.3
- 1.21.4 - 1.21.8

### Description:
Copy content from `MODRINTH_DESCRIPTION.md`

### External Links:
- **Source Code:** [Your GitHub URL]
- **Issues:** [Your GitHub Issues URL]
- **Discord:** [Your Discord Server] (optional)
- **Wiki:** [Your Wiki URL] (optional)

### Gallery:
Same screenshots as SpigotMC

---

## 📝 GitHub Repository Setup

### Repository Name: `BubbleReset`

### Files to Include:
- All source files from `src/`
- `pom.xml`
- `README.md` (already exists)
- `LICENSE` (MIT - already exists)
- `.gitignore`:
```
target/
.idea/
*.iml
.vscode/
*.class
dependency-reduced-pom.xml
```

### README Sections:
Use existing `README.md` or copy from `MODRINTH_DESCRIPTION.md`

### Releases:
1. Create a new release: v1.0.0
2. Tag: `v1.0.0`
3. Title: `BubbleReset 1.0.0 - Initial Release`
4. Attach: `bubblereset-1.0.0.jar`
5. Description: Copy changelog from descriptions

---

## 📋 Pre-Release Checklist

### Testing
- [ ] Test on Paper 1.21.8 server
- [ ] Verify all commands work
- [ ] Test GUI menu opens correctly
- [ ] Test world resets (overworld, nether, end)
- [ ] Test with PlaceholderAPI installed
- [ ] Test without PlaceholderAPI installed
- [ ] Verify portal overrides work
- [ ] Test permission nodes
- [ ] Check console for errors
- [ ] Test `/resource reload` command
- [ ] Test admin panel functionality

### Documentation
- [ ] Proofread all descriptions
- [ ] Check all links work
- [ ] Verify version numbers match
- [ ] Add screenshots
- [ ] Create demo video (optional)

### Code Quality
- [x] No compilation errors
- [x] No warnings (except deprecation notice)
- [ ] Code comments present
- [ ] No debug print statements

### Legal
- [x] License file present (MIT)
- [ ] Credits to original plugin mentioned
- [ ] No copyrighted content used

---

## 🎨 Branding Suggestions

### Color Scheme:
- Primary: Green (#67E25F) - Growth, renewal
- Secondary: Blue (#3C9EE5) - Water, resources
- Accent: Gold (#F0C040) - Premium, quality

### Logo Ideas:
1. **Globe with Reset Arrow** - World symbol with circular arrow
2. **Layered Worlds** - Three stacked world icons (overworld/nether/end)
3. **Resource Cube** - Minecraft block with refresh symbol
4. **Portal Gateway** - Nether portal frame with world inside

### Taglines:
- "Renewable Resources, Endless Possibilities"
- "Fresh Worlds, Fresh Adventures"
- "Smart Resource Management for Modern Servers"
- "Automated Resets, Seamless Experience"

---

## 📞 Support Setup

### Discord Server (Recommended):
Create channels:
- #announcements - Updates and releases
- #support - Help requests
- #suggestions - Feature requests
- #showcase - User screenshots

### GitHub Issues:
Enable issues with labels:
- `bug` - Something isn't working
- `enhancement` - New feature request
- `question` - Help wanted
- `documentation` - Docs improvements

### Support Response Template:
```
Thank you for using BubbleReset!

To help you better, please provide:
1. Server version (e.g., Paper 1.21.8)
2. BubbleReset version
3. Full error message from console (if any)
4. Steps to reproduce the issue
5. Your config.yml (remove sensitive info)

Quick checks:
- Are you using Java 21 or higher?
- Have you tried `/resource reload`?
- Any errors in console on startup?
```

---

## 📈 Marketing Tips

### SpigotMC Forums:
- Post in "Server Administration" forum
- Create showcase thread with screenshots
- Respond to user questions promptly
- Update regularly with changelogs

### Reddit:
- r/admincraft - Server admin discussions
- r/minecraft - General Minecraft community
- Post as text with description, not just link

### YouTube:
- Create tutorial video
- Record setup guide
- Show features in action
- Include download link in description

### Discord:
- Join Minecraft admin communities
- Share in appropriate channels
- Don't spam - follow server rules

---

## 🔄 Update Strategy

### Version Numbering:
- **Major (X.0.0)** - Breaking changes, major features
- **Minor (1.X.0)** - New features, backwards compatible
- **Patch (1.0.X)** - Bug fixes, minor improvements

### Planned Features for 1.1.0:
- Economy integration (Vault)
- Custom world generators support
- Multi-language support
- Database storage for reset times
- Web dashboard
- World backups before reset
- More PlaceholderAPI placeholders
- Per-player teleport cooldowns
- Warp points within resource worlds

---

## ✅ Ready to Publish!

You now have everything you need to publish BubbleReset:
- ✅ Compiled plugin JAR
- ✅ Spigot description
- ✅ Modrinth description
- ✅ Documentation
- ✅ License file
- ✅ README

**Next Step:** Test the plugin thoroughly, then upload to your chosen platforms!

Good luck with your release! 🚀
