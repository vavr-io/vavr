#!/bin/bash

repo=$1

if [ -z $repo ]; then
  echo "Please specify a javaslang repo name."
  echo "Example: trigger-build.sh javaslang-gwt"
else
  body='{
  "request": {
    "branch":"master"
  }}'

  curl -s -X POST \
    -H "Content-Type: application/json" \
    -H "Accept: application/json" \
    -H "Travis-API-Version: 3" \
    -H "Authorization: token $TRAVIS_ACCESS_TOKEN" \
    -d "$body" \
    https://api.travis-ci.org/repo/javaslang%2F$repo/requests
fi
