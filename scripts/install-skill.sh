#!/bin/bash
# Install ArcaneDesignSystem skills globally for Claude Code
#
# Usage: ./scripts/install-skill.sh
#
# This copies skills to ~/.claude/skills/arcane/ so they're available
# in any project when using Claude Code.

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
TARGET_BASE="$HOME/.claude/skills/arcane"

# Create target directory
mkdir -p "$TARGET_BASE"

# Install new-module skill
SKILL_SOURCE="$PROJECT_ROOT/.claude/skills/new-module"
if [ -d "$SKILL_SOURCE" ]; then
    echo "Installing new-module skill..."
    cp -r "$SKILL_SOURCE"/* "$TARGET_BASE/"
    mkdir -p "$TARGET_BASE/templates"
else
    echo "Warning: new-module skill not found at $SKILL_SOURCE"
fi

# Install compose-adaptive-layouts skill
ADAPTIVE_SOURCE="$PROJECT_ROOT/.claude/skills/compose-adaptive-layouts"
ADAPTIVE_TARGET="$HOME/.claude/skills/compose-adaptive-layouts"
if [ -d "$ADAPTIVE_SOURCE" ]; then
    echo "Installing compose-adaptive-layouts skill..."
    mkdir -p "$ADAPTIVE_TARGET"
    cp -r "$ADAPTIVE_SOURCE"/* "$ADAPTIVE_TARGET/"
else
    echo "Warning: compose-adaptive-layouts skill not found at $ADAPTIVE_SOURCE"
fi

echo ""
echo "Skills installed:"
echo ""
echo "1. Arcane New Module ($TARGET_BASE)"
ls -la "$TARGET_BASE" 2>/dev/null || echo "   (not installed)"
echo ""
echo "2. Compose Adaptive Layouts ($ADAPTIVE_TARGET)"
ls -la "$ADAPTIVE_TARGET" 2>/dev/null || echo "   (not installed)"
echo ""
echo "Trigger phrases:"
echo ""
echo "  New Module Skill:"
echo "    'create new module for <feature>'"
echo "    'start app with Arcane'"
echo ""
echo "  Adaptive Layouts Skill:"
echo "    'add adaptive layout to <module>'"
echo "    'make module responsive'"
echo "    'support different screen sizes'"
echo ""
echo "Done!"
