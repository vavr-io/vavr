[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.javaslang/javaslang/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.javaslang/javaslang)
[![Build Status](https://travis-ci.org/javaslang/javaslang.png)](https://travis-ci.org/javaslang/javaslang)
[![Coverage Status](https://codecov.io/github/javaslang/javaslang/coverage.svg?branch=master)](https://codecov.io/github/javaslang/javaslang?branch=master)
[![Gitter Chat](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/javaslang/javaslang)

# [Javaslang](http://javaslang.com/)

Feature-rich & self-contained functional programming in Java&trade; 8 and above.
Javaslang is a functional library for Java 8+ that provides persistent data types and functional control structures. Because Javaslang does not depend on any libraries (other than the JVM) you can easily add it as standalone .jar to your classpath.

There's also a [blog](http://blog.javaslang.com).

## Using Javaslang

See [User Guide](http://javaslang.github.io/javaslang-docs/2.0.0-RC2)

## Release Management

See http://central.sonatype.org/pages/ossrh-guide.html

Sonatype-Nexus specific maven configuration: `~/.m2/settings.xml`

```xml
<settings>
  <servers>
    <server>
      <id>sonatype-nexus-snapshots</id>
      <username>your-jira-id</username>
      <password>your-jira-pwd</password>
    </server>
    <server>
      <id>sonatype-nexus-staging</id>
      <username>your-jira-id</username>
      <password>your-jira-pwd</password>
    </server>
  </servers>
</settings>
```

### Deploy a snapshot

```
mvn clean deploy
```

The snaphot is deployed to https://oss.sonatype.org/content/repositories/snapshots/

### Prepare a release

```
mvn release:clean
mvn release:prepare
```

### Stage a release

```
mvn release:perform
```
