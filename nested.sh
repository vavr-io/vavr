#!/bin/zsh
set -euo pipefail

find . -name "*Test.java" -type f -exec sed -E -i '' \
    's|// -- ([a-z])([a-zA-Z0-9_]*)|}\n\n    @Nested\n    class \U\1\E\2 {|' {} +

exit 0;
