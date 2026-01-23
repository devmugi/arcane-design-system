#!/bin/bash
# Install ArcaneDesignSystem skill globally for Claude Code
#
# Usage: ./scripts/install-skill.sh
#
# This copies the skill to ~/.claude/skills/arcane/ so it's available
# in any project when using Claude Code.

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
SKILL_SOURCE="$PROJECT_ROOT/.claude/skills/new-module"
SKILL_TARGET="$HOME/.claude/skills/arcane"

# Check if source exists
if [ ! -d "$SKILL_SOURCE" ]; then
    echo "Error: Skill source not found at $SKILL_SOURCE"
    exit 1
fi

# Create target directory
mkdir -p "$SKILL_TARGET"

# Copy skill files
echo "Installing ArcaneDesignSystem skill..."
cp -r "$SKILL_SOURCE"/* "$SKILL_TARGET/"

# Make sure templates directory exists
mkdir -p "$SKILL_TARGET/templates"

echo ""
echo "Installed to: $SKILL_TARGET"
echo ""
echo "Files installed:"
ls -la "$SKILL_TARGET"
echo ""
echo "To use in Claude Code, say:"
echo "  'create new module for <feature>'"
echo "  'start app with Arcane'"
echo "  'add new screen using ArcaneDesignSystem'"
echo ""
echo "Done!"
