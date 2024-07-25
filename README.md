# Vavr

[![Gitpod ready-to-code](https://img.shields.io/badge/Gitpod-ready--to--code-blue?logo=gitpod&style=flat-square)](https://gitpod.io/#https://github.com/vavr-io/vavr)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=flat-square)](https://opensource.org/licenses/MIT)
[![GitHub Release](https://img.shields.io/github/release/vavr-io/vavr.svg?style=flat-square)](https://github.com/vavr-io/vavr/releases)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.vavr/vavr/badge.svg?style=flat-square)](http://search.maven.org/#search|gav|1|g:"io.vavr"%20AND%20a:"vavr")
[![Build Status](https://github.com/vavr-io/vavr/actions/workflows/build.yml/badge.svg)](https://github.com/vavr-io/vavr/actions/workflows/build.yml)
[![Code Coverage](https://codecov.io/gh/vavr-io/vavr/branch/master/graph/badge.svg)](https://codecov.io/gh/vavr-io/vavr)
[![Gitter Chat](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/vavr-io/vavr)
[![donate](https://img.shields.io/badge/Donate-PayPal-blue.svg?logo=paypal&style=flat-square)](https://paypal.me/danieldietrich13)

[![vavr-logo](https://user-images.githubusercontent.com/743833/62367542-486f0500-b52a-11e9-815e-e9788d4c8c8d.png)](http://vavr.io/)

Vavr is an object-functional language extension to Java 8 that aims to reduce the number of lines of code and increase code quality.
It provides persistent collections, functional abstractions for error handling, concurrent programming, pattern matching, and much more.

Vavr fuses the power of object-oriented programming with the elegance and robustness of functional programming.
The most interesting part is a feature-rich, persistent collection library that smoothly integrates with Java's standard collections.

Because Vavr does not depend on any libraries (other than the JVM), you can easily add it as a standalone .jar to your classpath.

To stay up to date please follow the [blog](http://blog.vavr.io).

### Stargazers over time
[![Stargazers over time](https://starchart.cc/vavr-io/vavr.svg?variant=adaptive)](https://starchart.cc/vavr-io/vavr)


## Using Vavr

See [User Guide](http://docs.vavr.io) and/or [Javadoc](http://www.javadoc.io/doc/io.vavr/vavr).

### Gradle tasks:

* Build: `./gradlew check`
  * test reports: `./build/reports/tests/test/index.html`
  * coverage reports: `./build/reports/jacoco/test/html/index.html`
* Javadoc (linting): `./gradlew javadoc`

### Contributing

A small number of users have reported problems building Vavr. Read our [contribution guide](./CONTRIBUTING.md) for details.
