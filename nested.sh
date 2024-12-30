#!/bin/zsh
set -euo pipefail

find . -name "*Test.java" -type f -exec sed -E -i '' \
    's|// -- ([a-z])([a-zA-Z0-9_]*)(.*)|}\n\n    @Nested\n    @DisplayName(\1\2\3)\n    class \CAPS\1\2 {|' {} +

exit 0;
