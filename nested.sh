#!/bin/zsh
set -euo pipefail

find ./src -name "*Test.java" -type f -exec sed -E -i '' \
    's|// -- ([a-zA-Z0-9]+)$|}\n\n    @Nested\n    @DisplayName("\1")\n    class \CAPS\1Test {|' {} +

find ./src -type f -name '*Test.java' -exec perl -i -pe 's/\bCAPS([a-zA-Z])([a-zA-Z]*)/\u\1\2/g' {} +

git restore **/LazyTest.java \
  **/PredicatesTest.java \
  **/PartialFunctionTest.java \
  **/MatchErrorTest.java \
  **/ValueTest.java \
  **/BitSetTest.java \
  **/LinkedHashMultimapTest.java \
  **/LinkedHashMultimapOfEntriesTest.java

#./gradlew test