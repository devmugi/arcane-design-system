#!/bin/bash
# detect-pr-changes.sh
# Analyzes git diff and outputs pr-changes.json manifest

set -e

INPUT_FILE="${1:-}"
PR_NUMBER="${2:-0}"
BASE_BRANCH="${3:-main}"

# If no input file, read from stdin
if [ -z "$INPUT_FILE" ]; then
    CHANGED_FILES=$(cat)
elif [ -f "$INPUT_FILE" ]; then
    CHANGED_FILES=$(cat "$INPUT_FILE")
else
    CHANGED_FILES=""
fi

# Initialize arrays
declare -a COMPONENTS=()
declare -a SCREENS=()

# Map directory to screen name
get_screen() {
    local path="$1"
    case "$path" in
        *"/components/controls/"*) echo "Controls" ;;
        *"/components/navigation/"*) echo "Navigation" ;;
        *"/components/display/"*) echo "Data Display" ;;
        *"/components/feedback/"*) echo "Feedback" ;;
        *"arcane-foundation/"*) echo "Design Spec" ;;
        *) echo "" ;;
    esac
}

# Get change type (added, modified, deleted)
get_change_type() {
    local file="$1"
    if git ls-files --error-unmatch "$file" &>/dev/null 2>&1; then
        echo "modified"
    else
        echo "added"
    fi
}

# Process each changed file
while IFS= read -r file; do
    # Skip empty lines
    [ -z "$file" ] && continue

    # Only process .kt files in component directories
    if [[ "$file" == *.kt ]] && [[ "$file" == *"/components/"* || "$file" == *"arcane-foundation/"* ]]; then
        # Extract component name from filename
        filename=$(basename "$file" .kt)
        screen=$(get_screen "$file")

        if [ -n "$screen" ]; then
            # Add to components array (JSON format)
            change_type=$(get_change_type "$file")
            COMPONENTS+=("{\"name\":\"$filename\",\"screen\":\"$screen\",\"changeType\":\"$change_type\",\"filePath\":\"$file\"}")

            # Track unique screens
            if [[ ! " ${SCREENS[*]} " =~ " ${screen} " ]]; then
                SCREENS+=("$screen")
            fi
        fi
    fi
done <<< "$CHANGED_FILES"

# Build JSON output
TIMESTAMP=$(date -u +"%Y-%m-%dT%H:%M:%SZ")

# Convert arrays to JSON
COMPONENTS_JSON=$(printf '%s\n' "${COMPONENTS[@]}" | paste -sd ',' -)
SCREENS_JSON=$(printf '"%s"\n' "${SCREENS[@]}" | paste -sd ',' -)

# Handle empty arrays
[ -z "$COMPONENTS_JSON" ] && COMPONENTS_JSON=""
[ -z "$SCREENS_JSON" ] && SCREENS_JSON=""

cat << EOF
{
  "prNumber": $PR_NUMBER,
  "baseBranch": "$BASE_BRANCH",
  "changedComponents": [$COMPONENTS_JSON],
  "affectedScreens": [$SCREENS_JSON],
  "generatedAt": "$TIMESTAMP"
}
EOF
