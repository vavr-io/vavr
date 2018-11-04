[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.vavr/vavr/badge.png)](https://maven-badges.herokuapp.com/maven-central/io.vavr/vavr)
[![Code Scene](https://img.shields.io/badge/codescene-analyzed-brightgreen.svg)](https://codescene.io/projects/981/jobs/latest-successful/results)
[![Build Status](https://travis-ci.org/vavr-io/vavr.png)](https://travis-ci.org/vavr-io/vavr)
[![codecov](https://codecov.io/gh/vavr-io/vavr/branch/master/graph/badge.svg)](https://codecov.io/gh/vavr-io/vavr)
[![Sputnik](https://sputnik.ci/conf/badge)](https://sputnik.ci/app#/builds/vavr-io/vavr)
[![Gitter Chat](https://badges.gitter.im/Join%20Chat.png)](https://gitter.im/vavr-io/vavr)

# [Vavr](http://vavr.io/)

Vavr is an object-functional language extension to Java 8, which aims to reduce the lines of code and increase code quality.
It provides persistent collections, functional abstractions for error handling, concurrent programming, pattern matching and much more.

Vavr fuses the power of object-oriented programming with the elegance and robustness of functional programming.
The most interesting part is a feature-rich, persistent collection library that smoothly integrates with Java's standard collections.

Because Vavr does not depend on any libraries (other than the JVM) you can easily add it as standalone .jar to your classpath.

To stay up to date please follow the [blog](http://blog.vavr.io).

## Using Vavr

See [User Guide](http://docs.vavr.io) and/or [Javadoc](http://www.javadoc.io/doc/io.vavr/vavr).

### Useful Maven Goals

* Executing tests: `mvn clean test`
* Executing doclint: `mvn javadoc:javadoc`
* Executing code coverage report: `mvn -P ci clean test jacoco:report`
* Create -javadoc.jar: `mvn javadoc:jar`
* Create -source.jar: `mvn source:jar`
* Update version properties: `mvn versions:update-properties`
* Check for new plugin version: `mvn versions:display-plugin-updates`

### Benchmarks

Currently, only basic microbenchmarks are available. To run:

```bash
mvn clean test -P benchmark -pl vavr-benchmark
```

### Contributing

A small number of users have reported problems building Vavr. Read "How to Contribute" (CONTRIBUTING.md) for details.

---

If you like Vavr you can support us by donating.

<a href="https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=8ZR8YCWB9K5WA">
<img src="https://cloud.githubusercontent.com/assets/743833/23549988/02d66ccc-000f-11e7-8764-a257b21377bd.gif">
</a>
