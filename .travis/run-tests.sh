#!/bin/bash
set -ev
mvn test -B

# Break CI build on PRs where committed differ from generated sources
if [ -n "$(git status -su vavr/src/generated*)" ]; then
  exit 1
fi

if [ -n "$(git status -su vavr-test/src/generated*)" ]; then
  exit 1
fi
