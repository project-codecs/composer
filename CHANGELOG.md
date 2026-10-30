# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [5.1.1] - 2026-10-30

### Fixed
- A bug which made it so only the base got registered when using the DeferredItemRegistry to register a whole family by @zn0n_

## [5.1] - 2026-10-30

### Added
- Composed block families for registering entire families automatically by @zn0n_
- Composer block tag provider for the block family system by @zn0n_
- Dynamic registry registration by @zn0n_
- Dynamic registry utilities by @zn0n_

## [5.0.2] - 2026-10-28

### Changed
- Updated links in documentation to reflect on [zn0n_](https://github.com/zn0n3)'s username change by [@zn0n3](https://github.com/zn0n3)
- Re-publish on cloudsmith with new semver due to failed build by [@zn0n3](https://github.com/zn0n3)

## [5.0.1] - 2026-10-25

### Fixed
- Server side crash caused by the item tooltip api being client-only

## [5.0] - 2026-10-24

### Added
- Multiblock extensions for things like input and output hatches by [@lilbrocodes](https://github.com/zn0n3)
- Vanilla-based config screen on 1.21.5 and lower to replace Composer's client config by [@lilbrocodes](https://github.com/zn0n3)

### Changed
- Rewrote (most of) the multiblock system to not use CCA, be more efficient and add extensions by [@lilbrocodes](https://github.com/zn0n3)
- Synced up all the languages to have the same translation keys by [@lilbrocodes](https://github.com/zn0n3)
- Made all previously 'developer only' commands be available always by [@lilbrocodes](https://github.com/zn0n3)
- Updated links in documentation & made proper documentation for multiblocks and the language generator changes by [@lilbrocodes](https://github.com/zn0n3)

### Removed
- Dependency on CCA by [@lilbrocodes](https://github.com/zn0n3)
- Dependency on Fzzy Config by [@lilbrocodes](https://github.com/zn0n3)
- Dynamic Tooltips by [@lilbrocodes](https://github.com/zn0n3)

## [4.3.2] - 2026-10-01

### Fixed
- Fixed all server-to-client packets in 1.20.6+ by [@lilbrocodes](https://github.com/zn0n3)

## [4.3.1] - 2026-09-27

### Added
- Manual type registrar to DeferredParticleRegistry by [@lilbrocodes](https://github.com/zn0n3)

### Changed
- Made plushies squishy on all versions! by [@lilbrocodes](https://github.com/zn0n3)

## [4.3] - 2026-09-06

### Added
- A registerReference to the deferred sound registry by [@lilbrocodes](https://github.com/zn0n3)

### Removed
- The SemiLangProvider abstraction as well as the old ComposerSemiLanguageProvider in favor of SemiLanguageProvider by [@lilbrocodes](https://github.com/zn0n3)

## [4.2] - 2026-06-30

### Added

- Inline prefixes to ComposerLanguageProvider by [@lilbrocodes](https://github.com/zn0n3)
- SemiLanguageProvider with all the same utilities as ComposerLanguageProvider by [@lilbrocodes](https://github.com/zn0n3)
- Better entity registration utilities for 1.21.3+ by [@lilbrocodes](https://github.com/zn0n3)

### Changes

- Multiblocks now use a custom data structure called a "cube matrix" to prevent the raw use of stacked lists by [@lilbrocodes](https://github.com/zn0n3)

### Deprecated

- ComposerSemiLanguageProvider in favour of SemiLanguageProvider by [@lilbrocodes](https://github.com/zn0n3)
- DeferredEntityRegistry#register(FabricEntityTypeBuilder<T>) is now only deprecated on 1.21.3+ by [@lilbrocodes](https://github.com/zn0n3)

## [4.1] - 2026-05-28

### Removed

- Removed multiple classes from api.utils in favor of the ones in [ambarella](https://github.com/project-codecs/ambarella) by [@lilbrocodes](https://github.com/zn0n3).

## [4.0.1] - 2026-05-17

### Added

- Composer post-initialization entrypoint for registering to composer's registries by [@lilbrocodes](https://github.com/zn0n3)

## [4.0] - 2026-05-8

### Added

- Wiki! View it on the [Sinytra Modded MC Wiki](https://moddedmc.wiki/en/project/composer) by [@lilbrocodes](https://github.com/zn0n3)
- Support for 1.21.5 - 1.21.11, except for 1.21.9 because of broken CCA by [@lilbrocodes](https://github.com/zn0n3)
- Alternatives for ComposerCompound in 1.21.6 and above by [@lilbrocodes](https://github.com/zn0n3)
- Typed tag builders for 1.20.4+ by [@lilbrocodes](https://github.com/zn0n3)
- ServerRecipeManager Events by [@lilbrocodes](https://github.com/zn0n3)
- Deferred recipe registry by [@lilbrocodes](https://github.com/zn0n3)
- ListBuilder from Constructive to not depend by [@lilbrocodes](https://github.com/zn0n3)
- KeyBiding press/hold "events" to BindManager by [@lilbrocodes](https://github.com/zn0n3)

### Changed

- PlushieBlockEntity -> AbstractPlushieBlockEntity by [@lilbrocodes](https://github.com/zn0n3)
- v1.nbt -> v1.data by [@lilbrocodes](https://github.com/zn0n3)
- events.impl.ItemFilterScrollEvent -> events.ClientScrollEvents.ClientItemFilterScrollEvent by [@lilbrocodes](https://github.com/zn0n3)
- Dynamic tooltips to use Texts instead of raw Strings by [@lilbrocodes](https://github.com/zn0n3)
- RainbowColor to be more accessible to third-party mods by [@lilbrocodes](https://github.com/zn0n3)

### Removed

- Composer data fixers by [@lilbrocodes](https://github.com/zn0n3)
- Composer features by [@lilbrocodes](https://github.com/zn0n3)
- Targeting utilities by [@lilbrocodes](https://github.com/zn0n3)
- Easytags / Automata by [@lilbrocodes](https://github.com/zn0n3)
- SerializableBoolean by [@lilbrocodes](https://github.com/zn0n3)
- Constructive from dependencies by [@lilbrocodes](https://github.com/zn0n3)
- SerializableIdentifier by [@lilbrocodes](https://github.com/zn0n3)
- Composite Events by [@lilbrocodes](https://github.com/zn0n3)

## [3.4] - 2026-04-26

### Added

- Multiblock system by [@lilbrocodes](https://github.com/zn0n3)
- Ability to generate item models that parent block models from ComposerModelProvider by [@lilbrocodes](https://github.com/zn0n3)
- Locking to AbstractPseudoRegistries by [@lilbrocodes](https://github.com/zn0n3)

### Changed

- Moved BindManager to util.misc by [@lilbrocodes](https://github.com/zn0n3)
- Moved ComposerCommand to util.misc by [@lilbrocodes](https://github.com/zn0n3)
- Moved CubicInterpolation to util.math by [@lilbrocodes](https://github.com/zn0n3)
- Moved @Optional to util.builder by [@lilbrocodes](https://github.com/zn0n3)
- Moved dev mode from a feature to a config value as feature values don't get loaded until resource load by [@lilbrocodes](https://github.com/zn0n3)
- TargetingContext no longer requires a player instance as it was never used and prevented caching by [@lilbrocodes](https://github.com/zn0n3)

### Deprecated

- Entirety of Easytags by [@lilbrocodes](https://github.com/zn0n3)

### Removed

- Toasts in favor of overlays as they can do the same and more by [@lilbrocodes](https://github.com/zn0n3)

## [3.3.3] - 2026-04-25

### Changed

- Plushie block is now abstracted & implemented internally for expandability by [@lilbrocodes](https://github.com/zn0n3)


## [3.3.2] - 2026-04-11

### Added

- Re-added grantAdvancement utility from old composer by [@lilbrocodes](https://github.com/zn0n3)
- KeyBinding event-registration methods by [@lilbrocodes](https://github.com/zn0n3)


## [3.3.1] - 2026-04-01

### Added

- Utilities to generate language files across many languages by [@lilbrocodes](https://github.com/zn0n3)


## [3.2.1] - 2026-03-12

### Added

- Option to use display model with custom model and upload by [@lilbrocodes](https://github.com/zn0n3)

### Deprecated

- Item group modification system in favor of fabric one by [@lilbrocodes](https://github.com/zn0n3)


## [3.2] - 2026-03-11

### Added

- Prefab types to DeferredDataComponentTypeRegistry by [@lilbrocodes](https://github.com/zn0n3)
- An utility for generating item models with a display property by [@lilbrocodes](https://github.com/zn0n3)
- Item group modifications by [@lilbrocodes](https://github.com/zn0n3)

### Removed

- Check for old feature system by [@lilbrocodes](https://github.com/zn0n3)


## [3.1.3] - 2026-02-21

### Changed

- Made DynamicTooltip.Location.AFTER_SEARCH_TAGS and AFTER_ITEM_GROUPS with AFTER_CREATIVE_TOOLTIP by [@lilbrocodes](https://github.com/zn0n3)

### Fixed

- Composer crashing with PuzzlesLib installed due to some weird stuff happening with @Local captures by [@lilbrocodes](https://github.com/zn0n3)


## [3.1.2] - 2026-01-31

### Added

- A \"relevancy\" check before appending tooltips to make it so not all items have useless keybinds in their tooltips. by [@lilbrocodes](https://github.com/zn0n3)

### Fixed

- A Gradle misconfiguration which caused all inheriting mods to need Gradle 9.2.0 and loom 1.14-SNAPSHOT by [@lilbrocodes](https://github.com/zn0n3)

## [3.1.1] - 2026-01-23

### Added

- Methods in DeferredItemRegistry for registering items while not adding them to the item group by [@lilbrocodes](https://github.com/zn0n3)

## [3.1] - 2026-01-23

### Added

- An option for the overlay animations to neither fade, nor slide. by [@lilbrocodes](https://github.com/zn0n3)

### Changed

- Structure of DeferredItemRegistry for less-verbose registration by [@lilbrocodes](https://github.com/zn0n3)

### Deprecated

- Provider<T> class. Use Supplier<T> instead. by [@lilbrocodes](https://github.com/zn0n3)

### Removed

- Provider<T> class, use Supplier<T> by [@lilbrocodes](https://github.com/zn0n3)

### Fixed

- ColorArgumentType allowing you to use RGBA syntax on an RGB color input by [@lilbrocodes](https://github.com/zn0n3)
- TexturedOverlay crashing the client on invalid texture by [@lilbrocodes](https://github.com/zn0n3)
