[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.javaslang/javaslang/badge.png)](https://maven-badges.herokuapp.com/maven-central/io.javaslang/javaslang)
[![Build Status](https://travis-ci.org/javaslang/javaslang.png)](https://travis-ci.org/javaslang/javaslang)
[![Coverage Status](https://codecov.io/github/javaslang/javaslang/coverage.png?branch=master)](https://codecov.io/github/javaslang/javaslang?branch=master)
[![Gitter Chat](https://badges.gitter.im/Join%20Chat.png)](https://gitter.im/javaslang/javaslang)

# [Javaslang](http://javaslang.io/)

Javaslang is an object-functional language extension to Java 8, which aims to reduce the lines of code and increase code quality.
It provides persistent collections, functional abstractions for error handling, concurrent programming, pattern matching and much more.

Javaslang fuses the power of object-oriented programming with the elegance and robustness of functional programming.
The most interesting part is a feature-rich, persistent collection library that smoothly integrates with Java's standard collections.

Because Javaslang does not depend on any libraries (other than the JVM) you can easily add it as standalone .jar to your classpath.

To stay up to date please follow the [blog](http://blog.javaslang.io).

## Using Javaslang

See [User Guide](http://docs.javaslang.io)

### Useful Maven Goals

* Executing tests: `mvn clean test`
* Executing doclint: `mvn javadoc:javadoc`
* Executing code coverage report: `mvn -P ci clean test jacoco:report`
* Create -javadoc.jar: `mvn javadoc:jar`
* Create -source.jar: `mvn source:jar`
* Update version properties: `mvn versions:update-properties`
* Check for new plugin version: `mvn versions:display-plugin-updates`

### Benchmarks

Currently only basic microbenchmarks are available. To run

```bash
mvn clean test -P benchmark -pl javaslang-benchmark
```
