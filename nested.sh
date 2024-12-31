#!/bin/zsh
set -euo pipefail

find . -name "*Test.java" -type f -exec sed -E -i '' \
    's|// -- [^a-zA-Z0-9 ]|}\n\n    @Nested\n    @DisplayName("\1")\n    class \CAPS\1Test {|' {} +

find . -type f -name '*Test.java' -exec perl -i -pe 's/\bCAPS([a-zA-Z])([a-zA-Z]*)/\u\1\2/g' {} +

./gradlew test