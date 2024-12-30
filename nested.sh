#!/bin/zsh
set -euo pipefail

find . -name "*.java" -type f -exec sed -i \
    -e 's|// -- \(.*\)|}\n\n@Nested\nclass \1 {|' {} +

exit 0;
