# architecture Analysis

**Date:** 2026-01-31
**Branch:** main

## Overview

Arcane Design System is a presentation-focused Compose Multiplatform UI component library that follows stateless composable patterns with full state hoisting to parent components. The architecture emphasizes reusable, composable components with callback-based event handling rather than advanced patterns like MVI/MVVM or state management frameworks.

## Applicability

**Highly Applicable** - This project is a UI component library, which naturally requires different architectural patterns than full applications. The architecture is well-suited to the library's purpose of providing reusable, testable components that delegate state management to consumers.

## Findings

### Following Best Practices

| File | Line | Pattern | Notes |
|------|------|---------|-------|
| `arcane-components/controls/Button.kt` | 53-59 | Sealed class style variants | Uses sealed class hierarchy for type-safe button styles (Filled, Tonal, Outlined, Elevated, Text) |
| `arcane-components/controls/Button.kt` | 217-227 | Callback-based onClick | Single responsibility: receives onClick callback, fully stateless |
| `arcane-components/controls/TextField.kt` | 32-49 | Value + callback pattern | Implements standard Compose unidirectional data flow: `value: String, onValueChange: (String) -> Unit` |
| `arcane-components/controls/Checkbox.kt` | 33-40 | State hoisting | Checkbox is fully controlled: `checked: Boolean, onCheckedChange: (Boolean) -> Unit` |
| `arcane-components/controls/Switch.kt` | 35-42 | State hoisting | Fully stateless switch with hoisted boolean state |
| `arcane-chat/messages/ArcaneChatMessageList.kt` | 40-50 | Generic list composable | Uses generics and lambda content slots: `<T>(...messageContent: @Composable (T) -> Unit)` |
| `arcane-chat/input/ArcaneAgentChatInput.kt` | 74-91 | Rich callback interface | Provides multiple optional callbacks for different input modes (voice, audio, send) |
| `catalog/screens/ControlsScreen.kt` | 44-58 | Local state in showcase | Uses `remember { mutableStateOf() }` for demo state, appropriate for showcase screens |
| `catalog/App.kt` | 55-87 | Sealed class navigation | Uses sealed class for type-safe screen navigation with companion object helpers |
| `catalog/App.kt` | 265 | Stateful app root | App root manages theme selection state with `mutableStateOf`, appropriate for top-level UI |
| `arcane-components/controls/Button.kt` | 240-259 | LaunchedEffect for animations | Properly uses `LaunchedEffect` for side effects (glow pulse animation) |
| `arcane-chat/input/ArcaneAgentChatInput.kt` | 98-107 | Focus state derived from interaction | Uses `MutableInteractionSource` to collect focus state, following Compose idioms |

### Issues Found

| File | Line | Issue | Recommendation |
|------|------|-------|----------------|
| `arcane-components/controls/Checkbox.kt` | 46 | Deprecated `surfaceInset` property | Update to use `surfaceContainerLowest` per v0.2.0 migration - color reference is deprecated and will be removed in v0.3.0 |
| `arcane-components/controls/Switch.kt` | 59, 61 | Deprecated `surfaceInset` property | Replace with `surfaceContainerLowest` to align with Material 3 surface system |
| `arcane-components/controls/RadioButton.kt` | (not read) | Likely deprecated `surfaceInset` | Should verify and update RadioButton component for consistency |
| `arcane-chat/input/ArcaneAgentChatInput.kt` | 98 | No error handling in LaunchedEffect | Consider adding try-catch if future state updates might throw exceptions |

### Not Evaluated

- **ViewModel/StateFlow patterns**: No ViewModels found in library code (expected, as this is a component library)
- **Complex business logic**: Not applicable to a UI component library
- **Data persistence**: No database or persistence layer (appropriate for library scope)
- **Network integration**: No API calls (appropriate for library scope)
- **Dependency injection framework**: Not needed for stateless components with callback-based design

## Rating

**architecture:** ⚠️ Partial

**Justification:**

The architecture is excellent for a UI component library and follows Compose best practices consistently:

**Strengths:**
- All components are stateless and properly hoist state via callbacks
- Uses Kotlin sealed classes for type-safe style/navigation variants
- Implements unidirectional data flow (value + callback pattern)
- Proper use of `LaunchedEffect`, `remember`, and composition locals
- Generic components with flexible content slots enable composition
- Clear separation between library components (stateless) and showcase app (stateful)

**Partial deduction reason:**
- Two components still reference deprecated `surfaceInset` property (Checkbox, Switch)
- These are breaking changes requiring updates before v0.3.0

The "partial" rating reflects the deprecation issues rather than architectural concerns. The pattern itself is exemplary for a Compose library.

## Recommendations

1. **Update deprecated surface colors**: In Checkbox and Switch components, replace `colors.surfaceInset` with `colors.surfaceContainerLowest` to align with Material 3 tone-based surface system and prevent breakage in v0.3.0

2. **Audit all components for deprecated properties**: Verify RadioButton, and other components haven't missed the v0.2.0 surface color migration

3. **Document state hoisting pattern**: Add architecture guide to `.claude/CLAUDE.md` or project docs explaining the callback-based pattern for library consumers

4. **Consider state management helpers** (optional): For complex showcase screens in catalog, could introduce a simple composable state holder pattern to reduce boilerplate:
   ```kotlin
   @Composable
   fun rememberControlsScreenState() = remember {
       mutableStateOf(ControlsScreenState())
   }
   ```

5. **Add architecture ADR**: Create an Architecture Decision Record documenting why the library uses stateless components + callbacks instead of ViewModels/StateFlow (improves clarity for future maintainers)
