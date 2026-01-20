# BubbleReset v1.1.3 — 2026-01-20

This is a maintenance release focused on versioning and tracking code quality hotspots. No functional behavior has changed.

## Changes
- Bump project version to 1.1.3.
- Document complex methods and proposed refactors to improve maintainability.

## Issues
- Complex Method (cyclomatic complexity = 19)
  - File: src/main/java/com/bubblecraft/bubblereset/BubbleReset.java:289-319
  - Context: Scheduler tick `run()` inside `startAutoResetScheduler()`

- Complex Method (cyclomatic complexity = 16)
  - File: src/main/java/com/bubblecraft/bubblereset/ResourceWorldMenu.java:239-301
  - Context: `onInventoryClick(InventoryClickEvent event)`

- Complex Method (cyclomatic complexity = 16)
  - File: src/main/java/com/bubblecraft/bubblereset/ResourceWorldMenu.java:122-185
  - Context: `createCustomSkull(String textureBase64)`

## Actions
- BubbleReset.java (scheduler `run()`)
  - Extract fixed-time vs interval scheduling into separate private methods.
  - Replace nested conditionals with guard clauses (early returns).
  - Cache repeated configuration reads into local variables.
  - Extract broadcast message and reset scheduling into helper methods to reduce branching.

- ResourceWorldMenu.java (`onInventoryClick`)
  - Use a switch on slot/button IDs and extract per-action handlers.
  - Centralize permission checks and common messaging.
  - Prefer early returns to minimize nesting.

- ResourceWorldMenu.java (`createCustomSkull`)
  - Extract profile/texture creation into a helper.
  - Consolidate null/meta validation path.
  - Isolate texture application and return immutable `ItemStack` copy if appropriate.

## Notes
- These refactors are planned; they will be implemented in a subsequent release with tests to ensure no behavioral regressions.
