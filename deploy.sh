#!/bin/sh

repo=releases
#repo=snapshots

mvn -DaltDeploymentRepository=${repo}::default::file:../rocketscience-mvn-repo/${repo} clean deploy
