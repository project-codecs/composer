# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [3.1.3] - 2026-02-21

### Changed

- Made DynamicTooltip.Location.AFTER_SEARCH_TAGS and AFTER_ITEM_GROUPS with AFTER_CREATIVE_TOOLTIP by @lilbrocodes

### Fixed

- Composer crashing with PuzzlesLib installed due to some weird stuff happening with @Local captures by @lilbrocodes


## [3.1.2] - 2026-01-31

### Added

- A \"relevancy\" check before appending tooltips to make it so not all items have useless keybinds in their tooltips. by @lilbrocodes

### Fixed

- A Gradle misconfiguration which caused all inheriting mods to need Gradle 9.2.0 and loom 1.14-SNAPSHOT by @lilbrocodes

## [3.1.1] - 2026-01-23

### Added

- Methods in DeferredItemRegistry for registering items while not adding them to the item group by @lilbrocodes

## [3.1] - 2026-01-23

### Added

- An option for the overlay animations to neither fade, nor slide. by @lilbrocodes

### Changed

- Structure of DeferredItemRegistry for less-verbose registration by @lilbrocodes

### Deprecated

- Provider<T> class. Use Supplier<T> instead. by @lilbrocodes

### Removed

- Provider<T> class, use Supplier<T> by @lilbrocodes

### Fixed

- ColorArgumentType allowing you to use RGBA syntax on an RGB color input by @lilbrocodes
- TexturedOverlay crashing the client on invalid texture by @lilbrocodes


