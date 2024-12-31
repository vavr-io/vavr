#!/bin/zsh
set -euo pipefail

find . -name "*Test.java" -type f -exec sed -E -i '' \
    's|// -- ([a-zA-Z])([a-zA-Z0-9_]*)(.*)|}\n\n    @Nested\n    @DisplayName("\1\2\3")\n    class \CAPS\1\2Test {|' {} +

find . -type f -name '*Test.java' -exec perl -i -pe 's/\bCAPS([a-zA-Z])([a-zA-Z]*)/\u\1\2/g' {} +

./gradlew test