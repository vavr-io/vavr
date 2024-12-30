#!/bin/zsh
set -euo pipefail

find . -name "*.java" -type f -exec sed -i '' \
    's|// -- \(.*\)|}\n\n   @Nested\n   class \1 {|' {} +

exit 0;
