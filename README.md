# Vavr

[![Build Status](https://github.com/vavr-io/vavr/actions/workflows/ci.yml/badge.svg)](https://github.com/vavr-io/vavr/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=flat-square)](https://opensource.org/licenses/MIT)
[![Maven Central Version](https://img.shields.io/maven-central/v/io.vavr/vavr?versionPrefix=0)](https://central.sonatype.com/artifact/io.vavr/vavr/versions)
[![Code Coverage](https://codecov.io/gh/vavr-io/vavr/branch/master/graph/badge.svg)](https://codecov.io/gh/vavr-io/vavr)

```text
 ____  ______________  ________________________  __________
 \   \/   /      \   \/   /   __/   /      \   \/   /      \
  \______/___/\___\______/___/_____/___/\___\______/___/\___\
```

Vavr is an **object-functional extension for Java that makes defensive programming easy by leveraging immutability and functional control structures**

Vavr seamlessly combines object-oriented programming with the elegance and robustness of functional programming. 

It provides:
* persistent collections
* functional abstractions for error handling, concurrent programming
* pattern matching
* ...and more

Since **Vavr has no dependencies** beyond the JVM, you can easily add it as a standalone .jar to your classpath.

Led and maintained by [@pivovarit](http://github.com/pivovarit)

### Stargazers over time
[![Stargazers over time](https://starchart.cc/vavr-io/vavr.svg?variant=adaptive)](https://starchart.cc/vavr-io/vavr)

### Maven Dependency

    <dependency>
        <groupId>io.vavr</groupId>
        <artifactId>vavr</artifactId>
        <version>0.11.0</version>
    </dependency>

### Gradle Dependency

    implementation 'io.vavr:vavr:0.11.0'

## Using Vavr

See [User Guide](http://docs.vavr.io) and/or [Javadoc](http://www.javadoc.io/doc/io.vavr/vavr).

### Useful Maven Goals

* Executing tests: `mvn clean test`
* Executing doclint: `mvn javadoc:javadoc`
* Executing code coverage report: `mvn -P ci clean test jacoco:report`
* Create -javadoc.jar: `mvn javadoc:jar`
* Create -source.jar: `mvn source:jar`

### Contributing

Currently, there are two significant branches:
- `main` (represents a stream of work leading to the release of a new major version)
- `version/1.x` (historical work that went into `1.0.0-alpha-3`, treat it as read-only - will be kept around for cherry-picking)

A small number of users have reported problems building Vavr. Read our [contribution guide](./CONTRIBUTING.md) for details.
