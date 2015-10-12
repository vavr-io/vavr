#!/bin/bash

if [ "$TRAVIS_REPO_SLUG" == "javaslang/javaslang" ] && [ "$TRAVIS_JDK_VERSION" == "oraclejdk8" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then

  echo -e "Publishing javadoc...\n"

  cp -R target/site/apidocs $HOME/javadoc-snapshot

  cd $HOME
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "travis-ci"
  git clone https://${GH_TOKEN}@github.com/javaslang/javaslang.github.io

  cd javaslang.github.io
  git rm -rf ./javadoc/snapshot
  cp -Rf $HOME/javadoc-snapshot ./javadoc/snapshot
  cd javadoc/snapshot
  zip -r javaslang-snapshot-javadoc.zip *
  cd -
  git add -f .
  git commit -m "Snapshot javadoc on successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"
  git push origin master

  echo -e "Published Javadoc to gh-pages.\n"
  
fi
