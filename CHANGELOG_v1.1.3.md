# BubbleReset v1.1.3 — 2026-01-20

This is a maintenance release focused on versioning and improving maintainability. No functional behavior has changed.

## Changes
- Bump project version to 1.1.3.
- Refactor complex methods to reduce cyclomatic complexity.

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

## Actions (Implemented)
- BubbleReset.java (scheduler `run()`)
  - Extracted fixed-time vs interval scheduling into separate private methods.
  - Reduced nesting with guard clauses.
  - Extracted warning broadcast + reset scheduling helpers.

- ResourceWorldMenu.java (`onInventoryClick`)
  - Centralized action extraction (PDC first, lore fallback).
  - Simplified click handler using helpers + early returns.

- ResourceWorldMenu.java (`createCustomSkull`)
  - Extracted texture normalization + profile application helpers.
  - Isolated base64/url parsing + skin application.

## Notes
- Unit tests passed after the refactor.
