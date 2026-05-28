# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [4.1] - 2026-05-28

### Removed

- Removed multiple classes from api.utils in favor of the ones in [ambarella](https://github.com/project-codecs/ambarella) by @lilbrocodes.

## [4.0.1] - 2026-05-17

### Added

- Composer post-initialization entrypoint for registering to composer's registries by @lilbrocodes

## [4.0] - 2026-05-8

### Added

- Wiki! View it on https://moddedmc.wiki/ (slug undecided) by @lilbrocodes
- Support for 1.21.5 - 1.21.11, except for 1.21.9 because of broken CCA b by @lilbrocodes
- Alternatives for ComposerCompound in 1.21.5 and above by @lilbrocodes
- Typed tag builders for 1.20.4+ by @lilbrocodes
- ServerRecipeManager Events by @lilbrocodes
- Deferred recipe registry by @lilbrocodes
- ListBuilder from Constructive to not depend by @lilbrocodes
- KeyBiding press/hold "events" to BindManager by @lilbrocodes

### Changed

- PlushieBlockEntity -> AbstractPlushieBlockEntity by @lilbrocodes
- v1.nbt -> v1.data by @lilbrocodes
- events.impl.ItemFilterScrollEvent -> events.ClientScrollEvents.ClientItemFilterScrollEvent by @lilbrocodes
- Dynamic tooltips to use Texts instead of raw Strings by @lilbrocodes
- RainbowColor to be more accessible to third-party mods by @lilbrocodes

### Removed

- Composer data fixers by @lilbrocodes
- Composer features by @lilbrocodes
- Targeting utilities by @lilbrocodes
- Easytags / Automata by @lilbrocodes
- SerializableBoolean by @lilbrocodes
- Constructive from dependencies by @lilbrocodes
- SerializableIdentifier by @lilbrocodes
- Composite Events by @lilbrocodes

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

## [3.3.3] - 2026-04-25

### Changed

- Plushie block is now abstracted & implemented internally for expandability by @lilbrocodes


## [3.3.2] - 2026-04-11

### Added

- Re-added grantAdvancement utility from old composer by @lilbrocodes
- KeyBinding event-registration methods by @lilbrocodes


## [3.3.1] - 2026-04-01

### Added

- Utilities to generate language files across many languages by @lilbrocodes


## [3.2.1] - 2026-03-12

### Added

- Option to use display model with custom model and upload by @lilbrocodes

### Deprecated

- Item group modification system in favor of fabric one by @lilbrocodes


## [3.2] - 2026-03-11

### Added

- Prefab types to DeferredDataComponentTypeRegistry by @lilbrocodes
- An utility for generating item models with a display property by @lilbrocodes
- Item group modifications by @lilbrocodes

### Removed

- Check for old feature system by @lilbrocodes


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
