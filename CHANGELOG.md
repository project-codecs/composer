# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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


