# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.3.2] - 2025-01-25

### Added
- `agent2Dark()` and `agent2Light()` theme variants with purple (#7C3AED) accent

### Changed
- Renamed `mtg()` theme to `cvAgentDark()`
- Added `cvAgentLight()` light mode variant for cvAgent theme

## [0.3.1] - 2025-01-25

### Added
- Interactive chat catalog with JS bridge for browser automation
- Adaptive navigation in catalog-chat (NavigationSuiteScaffold)
- Responsive device preview with chrome simulator

## [0.3.0] - 2025-01-24

### Added
- P2D/P2L theme variants (Perplexity-inspired teal themes)
- ClaudeD/ClaudeL theme variants (warm coral accent)
- Theme tab in catalog with comprehensive token showcase
- FlowRow responsive layouts in catalog

### Changed
- Improved Material 3 theme compliance
- Differentiated M3 secondary colors across themes

### Fixed
- FlowRow for responsive theme selector in catalog

## [0.2.1] - 2025-01-20

### Fixed
- Binary incompatibility crash in chat module (removed onPreviewKeyEvent)

## [0.1.3] - 2025-01-18

### Added
- Claude Code skill for creating new modules
- WASM/JS support with publishWasmJsToDocs task
- GitHub Pages deployment with landing page
- PR preview system with auto-deployment

## [0.1.1] - 2025-01-15

### Added
- arcane-chat module with message blocks and input components
- Chat screen scaffold and message list

## [0.1.0] - 2025-01-10

### Added
- Initial release
- arcane-foundation: Design tokens, theme, colors, typography
- arcane-components: 30+ UI components
  - Controls: Button, TextField, Checkbox, RadioButton, Switch, Slider
  - Navigation: Tabs, Breadcrumbs, Pagination, Stepper
  - Display: Card, ListItem, Badge, Avatar, Tooltip, Table
  - Feedback: Spinner, Progress, Skeleton, Toast, Modal, AlertBanner
- Multi-platform support: Android, iOS, Desktop, WASM
- Catalog app for component showcase

[unreleased]: https://github.com/devmugi/arcane-design-system/compare/v0.3.2...HEAD
[0.3.2]: https://github.com/devmugi/arcane-design-system/compare/v0.3.1...v0.3.2
[0.3.1]: https://github.com/devmugi/arcane-design-system/compare/v0.3.0...v0.3.1
[0.3.0]: https://github.com/devmugi/arcane-design-system/compare/v0.2.1...v0.3.0
[0.2.1]: https://github.com/devmugi/arcane-design-system/compare/v0.1.3...v0.2.1
[0.1.3]: https://github.com/devmugi/arcane-design-system/compare/v0.1.1...v0.1.3
[0.1.1]: https://github.com/devmugi/arcane-design-system/compare/v0.1.0...v0.1.1
[0.1.0]: https://github.com/devmugi/arcane-design-system/releases/tag/v0.1.0
