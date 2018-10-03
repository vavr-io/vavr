[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.vavr/vavr/badge.png)](https://maven-badges.herokuapp.com/maven-central/io.vavr/vavr)
[![Build Status](https://travis-ci.org/vavr-io/vavr.png)](https://travis-ci.org/vavr-io/vavr)
[![codecov](https://codecov.io/gh/vavr-io/vavr/branch/master/graph/badge.svg)](https://codecov.io/gh/vavr-io/vavr)
[![Gitter Chat](https://badges.gitter.im/Join%20Chat.png)](https://gitter.im/vavr-io/vavr)

# [Vavr](http://vavr.io/)

Vavr is an object-functional language extension to Java 11, which aims to increase code quality and readability.
It provides persistent collections, functional abstractions for error handling, concurrent programming, pattern matching and much more.

To stay up to date please follow the [blog](http://blog.vavr.io).

---

## Prerequisites:

* Vavr 1.x runs with Java 11 and is completely modularized (JPMS conform). It runs also on the classpath.
* Java 8 users are able to fall back to Vavr 0.9.

## Using Vavr

Starting with v1, Vavr is available on JCenter and Maven Central. 
Vavr v0.9 is available on Maven Central.

### Gradle

```groovy
dependencies {
    compile "io.vavr:vavr-control:1.0.0-alpha-1"
}
```

### Maven

```xml
<dependencies>
    <dependency>
        <groupId>io.vavr</groupId>
        <artifactId>vavr-control</artifactId>
        <version>1.0.0-alpha-1</version>
    </dependency>
</dependencies>
```

---

Developers, please note that the previous version Vavr 0.9 was moved to the branch [v0.9.x](https://github.com/vavr-io/vavr/tree/v0.9.x) and master was replaced with v1.0, which is a complete rewrite. 
