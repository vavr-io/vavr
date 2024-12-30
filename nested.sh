#!/bin/zsh
set -euo pipefail

find . -name "*Test.java" -type f -exec sed -i '' \
    's|// -- \([a-z][a-zA-Z0-9_]*\).*|}\n\n    @Nested\n    class \u\1 {|' {} +

exit 0;
