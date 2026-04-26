## [3.4] - 2026-04-26

### Added

- Multiblock system by @lilbrocodes
- Ability to generate item models that parent block models from ComposerModelProvider by @lilbrocodes
- Locking to AbstractPseudoRegistries by @lilbrocodes

### Changed

- Moved BindManager to util.misc by @lilbrocodes
- Moved ComposerCommand to util.misc by @lilbrocodes
- Moved CubicInterpolation to util.math by @lilbrocodes
- Moved @Optional to util.builder by @lilbrocodes
- Moved dev mode from a feature to a config value as feature values don't get loaded until resource load by @lilbrocodes
- TargetingContext no longer requires a player instance as it was never used and prevented caching by @lilbrocodes

### Deprecated

- Entirety of Easytags by @lilbrocodes

### Removed

- Toasts in favor of overlays as they can do the same and more by @lilbrocodes