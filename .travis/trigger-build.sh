#!/bin/bash

org="vavr-io"
repo=$1
branch=$2

if [ -z $repo ]; then

  echo "Usage: trigger-build.sh <github-repo> [<branch>]"

else

  if [ -z $branch ]; then
    branch="master"
  fi

  echo "Triggering travis-ci build of $org/$repo [$branch]..."

  body=`cat <<EOF
{ "request": {
    "branch": "$branch"
}}
EOF
`

  curl -s -X POST \
    -H "Content-Type: application/json" \
    -H "Accept: application/json" \
    -H "Travis-API-Version: 3" \
    -H "Authorization: token $TRAVIS_ACCESS_TOKEN" \
    -d "$body" \
    https://api.travis-ci.org/repo/$org%2F$repo/requests

fi
