## Why Javaslang?
[![Build Status](https://travis-ci.org/rocketscience-projects/javaslang.png)](https://travis-ci.org/rocketscience-projects/javaslang)
[![Coverage Status](https://img.shields.io/coveralls/rocketscience-projects/javaslang.svg)](https://coveralls.io/r/rocketscience-projects/javaslang)

Feature-rich & self-contained functional programming in Java&trade; 8 and above.

## Using Javaslang

Projects that include Javaslang need to target Java 1.8 at minimum.

### Maven dependency

The .jar is available at [Maven Central](http://search.maven.org/#search|ga|1|a:"javaslang").

The actual release is

```xml
<dependency>
    <groupId>com.javaslang</groupId>
    <artifactId>javaslang</artifactId>
    <version>1.0.0</version>
</dependency>
```

On every push to github a new snapshot will be deployed to [oss.sonatype.org](https://oss.sonatype.org/content/repositories/snapshots/com/javaslang/javaslang) via [travis-ci.org](https://travis-ci.org/rocketscience-projects/javaslang):

```xml
<dependency>
    <groupId>com.javaslang</groupId>
    <artifactId>javaslang</artifactId>
    <version>1.1.0-SNAPSHOT</version>
</dependency>
```

### Standalone

Because Javaslang does _not_ depend on any libraries (other than the JVM) you can easily add it as standalone .jar to your classpath.

Javaslang can be downloaded [here](http://search.maven.org/#search|ga|1|a:"javaslang").

## Developing Javaslang

### Some Maven Goals

* Executing tests: `mvn clean test`
* Executing doclint: `mvn javadoc:javadoc`
* Executing code coverage report: `mvn -P ci clean test jacoco:report`
* Create -javadoc.jar: `mvn javadoc:jar`
* Create -source.jar: `mvn source:jar`
* Update version properties: `mvn versions:update-properties` 
* Check for new plugin version: `mvn versions:display-plugin-updates`

### Release Management

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

#### Deploy a snapshot

```
mvn clean deploy
```

The snaphot is deployed to https://oss.sonatype.org/content/repositories/snapshots/

#### Prepare a release

```
mvn release:clean
mvn release:prepare
```

#### Stage a release

```
mvn release:perform
```
