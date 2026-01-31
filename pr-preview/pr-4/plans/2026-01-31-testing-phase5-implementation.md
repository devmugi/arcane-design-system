# Phase 5: CI Integration Implementation

**Date:** 2026-01-31
**Branch:** testing
**Goal:** Full test pipeline with PR blocking

## Overview

Create GitHub Actions workflows to run all tests automatically on every PR, blocking merge on failures.

## Jobs Structure

```
┌─────────────────┐
│   unit-tests    │
└────────┬────────┘
         │
    ┌────┴────┬────────────┬────────────┐
    │         │            │            │
    ▼         ▼            ▼            ▼
┌──────┐  ┌──────────┐  ┌──────────┐  ┌─────────┐
│  ui  │  │ android  │  │ desktop  │  │   ios   │
│ tests│  │screenshots│ │screenshots│ │screenshots│
└──────┘  └──────────┘  └──────────┘  └─────────┘
```

## Test Commands

### Unit Tests
```bash
./gradlew :arcane-foundation-tests:allTests
./gradlew :arcane-components-tests:allTests
./gradlew :arcane-chat-tests:allTests
```

### UI Tests
```bash
./gradlew :arcane-components-ui-tests:jvmTest
./gradlew :arcane-chat-ui-tests:jvmTest
```

### Android Screenshots (Roborazzi)
```bash
# Verify mode - compare against golden images
./gradlew :arcane-components-ui-screenshots:verifyRoborazziDebug
./gradlew :arcane-chat-ui-screenshots:verifyRoborazziDebug

# Record mode - update golden images
./gradlew :arcane-components-ui-screenshots:recordRoborazziDebug
./gradlew :arcane-chat-ui-screenshots:recordRoborazziDebug
```

### Desktop Screenshots (Skiko)
```bash
./gradlew :arcane-components-ui-screenshots:desktopTest
./gradlew :arcane-chat-ui-screenshots:desktopTest
```

### iOS Screenshots
```bash
# Future: iOS screenshot testing
./gradlew :arcane-components-ui-screenshots:iosSimulatorArm64Test
./gradlew :arcane-chat-ui-screenshots:iosSimulatorArm64Test
```

## Workflow Files

### `.github/workflows/tests.yml`
Main test workflow with parallel jobs:
- `unit-tests` - Runs first, blocks all other jobs
- `ui-tests` - Compose UI interaction tests
- `screenshots-android` - Roborazzi verification
- `screenshots-desktop` - Skiko-based desktop tests

### Platform-Specific Runners
| Job | Runner | Reason |
|-----|--------|--------|
| unit-tests | ubuntu-latest | Standard, fast |
| ui-tests | ubuntu-latest | JVM tests |
| screenshots-android | ubuntu-latest | Roborazzi JVM-based |
| screenshots-desktop | ubuntu-latest | Headless Skiko |
| screenshots-ios | macos-latest | Requires Xcode |

## Git LFS Configuration

Git LFS is required for golden images. The workflow must checkout with LFS:
```yaml
- uses: actions/checkout@v4
  with:
    lfs: true
```

## Artifact Upload on Failure

When screenshot verification fails, upload diffs for review:
```yaml
- uses: actions/upload-artifact@v4
  if: failure()
  with:
    name: screenshot-diffs
    path: '**/build/outputs/roborazzi/'
```

## Merge Blocking

PRs cannot merge if any test job fails. This is configured via GitHub branch protection rules (manual setup required after workflow creation).

## Implementation Tasks

1. ✅ Create implementation plan
2. Create `.github/workflows/tests.yml`
3. Add unit test job
4. Add UI test job
5. Add Android screenshot job with artifact upload
6. Add Desktop screenshot job
7. Add iOS screenshot job (optional, macos runner)
8. Test workflow with PR
9. Configure branch protection (manual)
