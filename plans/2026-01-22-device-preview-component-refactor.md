# Device Preview Component Refactor Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Consolidate device preview functionality into a single reusable ComponentPreview component that properly handles safe area insets to prevent content overlap with device chrome (status bar, notch/dynamic island, navigation bar).

**Architecture:** The existing `ComponentPreview` already wraps `DevicePreview` with safe area padding logic. We'll standardize on ComponentPreview, fix the safe area insets calculation, update all screens to use it consistently, and make DevicePreview internal.

**Tech Stack:** Kotlin Compose Multiplatform, Arcane Design System

---

## Task 1: Analyze Current Safe Area Insets

**Files:**
- Read: `catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/components/DeviceChrome.kt:27-39`
- Read: `catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/components/DevicePreview.kt:22-80`

**Step 1: Verify current safe area insets values**

Check `DeviceChrome.kt:27-39`:
```kotlin
fun DeviceType.getSafeAreaInsets(): SafeAreaInsets {
    return when (this) {
        DeviceType.None -> SafeAreaInsets(top = 0.dp, bottom = 0.dp)
        DeviceType.Pixel8 -> SafeAreaInsets(
            top = 48.dp,  // Status bar + notch clearance
            bottom = 16.dp // Gesture bar
        )
        DeviceType.iPhone16 -> SafeAreaInsets(
            top = 59.dp,  // Status bar + Dynamic Island clearance
            bottom = 34.dp // Home indicator
        )
    }
}
```

**Step 2: Calculate correct insets based on device specs**

From `DevicePreview.kt`:

Pixel 8:
- Status bar height: 24.dp (line 49 in DeviceChrome.kt)
- Punch hole: y = 24.dp, radius = 6.dp (line 63)
- Total top clearance needed: 24 (status) + 24 (punch hole y) + 12 (punch hole diameter) = 60.dp
- Navigation bar: 16.dp (line 100 in DeviceChrome.kt)
- Current: top = 48.dp ❌ (should be 60.dp)
- Current: bottom = 16.dp ✅

iPhone 16:
- Status bar height: 24.dp (line 49 in DeviceChrome.kt)
- Dynamic Island: y = 20.dp, height = 37.dp (line 73)
- Total top clearance needed: 24 (status) + 20 (island y) + 37 (island height) = 81.dp
- Navigation bar: 34.dp (line 100 in DeviceChrome.kt)
- Current: top = 59.dp ❌ (should be 81.dp)
- Current: bottom = 34.dp ✅

**Step 3: Document the issue**

Write findings:
- Pixel 8 top inset should be 60.dp (currently 48.dp - 12.dp short)
- iPhone 16 top inset should be 81.dp (currently 59.dp - 22.dp short)
- This causes notch/Dynamic Island to overlap with content

---

## Task 2: Fix Safe Area Insets

**Files:**
- Modify: `catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/components/DeviceChrome.kt:27-39`

**Step 1: Update getSafeAreaInsets() with correct values**

```kotlin
fun DeviceType.getSafeAreaInsets(): SafeAreaInsets {
    return when (this) {
        DeviceType.None -> SafeAreaInsets(top = 0.dp, bottom = 0.dp)
        DeviceType.Pixel8 -> SafeAreaInsets(
            top = 60.dp,  // Status bar (24) + punch hole position (24) + punch hole diameter (12)
            bottom = 16.dp // Gesture bar
        )
        DeviceType.iPhone16 -> SafeAreaInsets(
            top = 81.dp,  // Status bar (24) + Dynamic Island position (20) + island height (37)
            bottom = 34.dp // Home indicator
        )
    }
}
```

**Step 2: Run the catalog app to verify**

Run: `./gradlew :catalog-chat:composeApp:run`
Expected: Content should not overlap with notch/Dynamic Island on device previews

**Step 3: Commit the fix**

```bash
git add catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/components/DeviceChrome.kt
git commit -m "fix(catalog-chat): correct safe area insets for device chrome clearance

- Update Pixel 8 top inset to 60.dp (was 48.dp)
- Update iPhone 16 top inset to 81.dp (was 59.dp)
- Prevents content overlap with status bar and notch/Dynamic Island"
```

---

## Task 3: Update ChatScreen to Use ComponentPreview

**Files:**
- Modify: `catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/screens/ChatScreen.kt:1-70`

**Step 1: Change import from DevicePreview to ComponentPreview**

Old line 10:
```kotlin
import io.github.devmugi.arcane.catalog.chat.components.DevicePreview
```

New line 10:
```kotlin
import io.github.devmugi.arcane.catalog.chat.components.ComponentPreview
```

**Step 2: Update ChatScreen composable to use ComponentPreview**

Old lines 25-68:
```kotlin
DevicePreview(deviceType = deviceType) {
    ArcaneChatScreenScaffold(
        isEmpty = messages.isEmpty(),
        emptyState = {
            // ... empty state content
        }
    ) {
        ArcaneChatMessageList(
            // ... message list
        )
    }
}
```

New lines 25-68:
```kotlin
ComponentPreview(deviceType = deviceType) {
    ArcaneChatScreenScaffold(
        isEmpty = messages.isEmpty(),
        emptyState = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(ArcaneSpacing.Small)
            ) {
                Text(
                    text = "No messages yet",
                    style = ArcaneTheme.typography.bodyLarge,
                    color = ArcaneTheme.colors.textSecondary
                )
                Text(
                    text = "Start a conversation",
                    style = ArcaneTheme.typography.labelMedium,
                    color = ArcaneTheme.colors.textDisabled
                )
            }
        }
    ) {
        ArcaneChatMessageList(
            messages = messages,
            messageKey = { it.id }
        ) { message ->
            when (message) {
                is ChatMessage.User -> ArcaneUserMessageBlock(
                    text = message.text,
                    timestamp = message.timestamp
                )
                is ChatMessage.Assistant -> ArcaneAssistantMessageBlock(
                    title = message.title,
                    isLoading = message.isLoading
                ) {
                    Text(
                        text = message.content,
                        style = ArcaneTheme.typography.bodyMedium,
                        color = ArcaneTheme.colors.text
                    )
                }
            }
        }
    }
}
```

**Step 3: Run the catalog app to verify ChatScreen**

Run: `./gradlew :catalog-chat:composeApp:run`
Expected:
- ChatScreen now shows device chrome (status bar, navigation bar)
- Content has proper safe area padding
- No overlap with notch/Dynamic Island

**Step 4: Commit the change**

```bash
git add catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/screens/ChatScreen.kt
git commit -m "refactor(catalog-chat): use ComponentPreview in ChatScreen for consistent chrome

- Replace DevicePreview with ComponentPreview
- Ensures chat screen has device chrome and safe area padding
- Matches behavior of other screens (ChatInput, MessageBlocks)"
```

---

## Task 4: Make DevicePreview Internal

**Files:**
- Modify: `catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/components/DevicePreview.kt:50-80`

**Step 1: Add internal visibility to DevicePreview composable**

Old line 50-56:
```kotlin
@Composable
fun DevicePreview(
    deviceType: DeviceType,
    modifier: Modifier = Modifier,
    showChrome: Boolean = false,
    content: @Composable () -> Unit
) {
```

New line 50-56:
```kotlin
@Composable
internal fun DevicePreview(
    deviceType: DeviceType,
    modifier: Modifier = Modifier,
    showChrome: Boolean = false,
    content: @Composable () -> Unit
) {
```

**Step 2: Verify DevicePreview is only used by ComponentPreview**

Run: `grep -r "DevicePreview" catalog-chat/composeApp/src/commonMain/kotlin`
Expected output:
- DevicePreview.kt (definition)
- ComponentPreview.kt (usage)
- No other files should import/use DevicePreview

**Step 3: Build the module to verify no compilation errors**

Run: `./gradlew :catalog-chat:composeApp:compileKotlinJvm`
Expected: Build succeeds with no errors

**Step 4: Commit the change**

```bash
git add catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/components/DevicePreview.kt
git commit -m "refactor(catalog-chat): make DevicePreview internal

- ComponentPreview is now the public API for device previews
- DevicePreview is an internal implementation detail"
```

---

## Task 5: Add Documentation to ComponentPreview

**Files:**
- Modify: `catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/components/ComponentPreview.kt:1-42`

**Step 1: Add KDoc to ComponentPreview**

Old lines 9-14:
```kotlin
@Composable
fun ComponentPreview(
    deviceType: DeviceType,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
```

New lines 9-26:
```kotlin
/**
 * Standard component preview wrapper for catalog-chat screens.
 *
 * Wraps content in a device frame (when deviceType != None) with:
 * - Device bezel and border
 * - Status bar with system icons
 * - Navigation bar with home indicator
 * - Notch or Dynamic Island overlay
 * - Safe area padding to prevent content overlap with chrome
 *
 * @param deviceType The device frame to display (None, Pixel8, iPhone16)
 * @param modifier Modifier for the preview container
 * @param content The component content to display inside the device frame
 */
@Composable
fun ComponentPreview(
    deviceType: DeviceType,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
```

**Step 2: Run build to verify**

Run: `./gradlew :catalog-chat:composeApp:compileKotlinJvm`
Expected: Build succeeds

**Step 3: Commit documentation**

```bash
git add catalog-chat/composeApp/src/commonMain/kotlin/io/github/devmugi/arcane/catalog/chat/components/ComponentPreview.kt
git commit -m "docs(catalog-chat): add KDoc to ComponentPreview

- Document purpose and features of ComponentPreview
- Clarify usage as standard wrapper for catalog screens"
```

---

## Task 6: Verification and Testing

**Files:**
- Run: Catalog app on Desktop, Android (if available)
- Verify: All three screens (Chat, MessageBlocks, ChatInput)

**Step 1: Run Desktop app and test all device types**

Run: `./gradlew :catalog-chat:composeApp:run`

Test matrix:
1. Select "No Preview" device type
   - Expected: Content renders without device frame
2. Select "Pixel 8" device type
   - Expected: Device frame with punch hole notch visible
   - Expected: Content has 60.dp top padding (no overlap with notch)
   - Expected: Status bar and navigation bar visible
3. Select "iPhone 16" device type
   - Expected: Device frame with Dynamic Island visible
   - Expected: Content has 81.dp top padding (no overlap with island)
   - Expected: Status bar and navigation bar visible

**Step 2: Test all three tabs**

For each device type (None, Pixel8, iPhone16):
1. Navigate to "Chat" tab
   - Expected: Chat messages render correctly with safe area padding
2. Navigate to "Message Blocks" tab
   - Expected: Message block previews render correctly
3. Navigate to "Chat Input" tab
   - Expected: Input field previews render correctly

**Step 3: Verify no visual regressions**

Check that:
- Content is not cut off at top or bottom
- Status bar is visible and not overlapped
- Notch/Dynamic Island does not cover content
- Navigation bar home indicator is visible
- Device frame bezel is properly styled

**Step 4: Document test results**

Create a test summary:
```
✅ All three device types render correctly
✅ Safe area padding prevents content overlap
✅ Device chrome (status bar, nav bar) renders correctly
✅ All three catalog tabs work with device preview
✅ No visual regressions
```

**Step 5: Final commit if any issues found**

If issues found and fixed:
```bash
git add .
git commit -m "fix(catalog-chat): address device preview issues from testing

[List specific issues fixed]"
```

---

## Summary

**Files Modified:**
1. `DeviceChrome.kt` - Fixed safe area inset calculations
2. `ChatScreen.kt` - Migrated to ComponentPreview
3. `DevicePreview.kt` - Made internal visibility
4. `ComponentPreview.kt` - Added documentation

**Key Changes:**
- Pixel 8 top inset: 48.dp → 60.dp
- iPhone 16 top inset: 59.dp → 81.dp
- ChatScreen now uses ComponentPreview consistently with other screens
- DevicePreview is now an internal implementation detail

**Result:**
- All catalog-chat screens use ComponentPreview consistently
- Content no longer overlaps with device chrome (status bar, notch, navigation bar)
- Single source of truth for device preview functionality
