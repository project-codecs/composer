# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [3.2.2] - 2026-01-31

### Added

- A \"relevancy\" check before appending tooltips to make it so not all items have useless keybinds in their tooltips. by @lilbrocodes
- Methods in DeferredItemRegistry for registering items while not adding them to the item group by @lilbrocodes
- IdentifierMap by @lilbrocodes
- DeferredHudLayerRegistry by @lilbrocodes
- An option for the overlay animations to neither fade, nor slide. by @lilbrocodes

### Changed

- Structure of DeferredItemRegistry for less-verbose registration by @lilbrocodes

### Deprecated

- Provider<T> class. Use Supplier<T> instead. by @lilbrocodes

### Removed

- Provider<T> class, use Supplier<T> by @lilbrocodes

### Fixed

- A gradle misconfiguration which caused all depending mods to need gradle 9.2.0 and loom 1.14-SNAPSHOT by @lilbrocodes
- ColorArgumentType allowing you to use RGBA syntax on an RGB color input by @lilbrocodes
- TexturedOverlay crashing the client on invalid texture by @lilbrocodes


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


