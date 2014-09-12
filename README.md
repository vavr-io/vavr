## Why Javaslang?
[![Build Status](https://travis-ci.org/rocketscience-projects/javaslang.png)](https://travis-ci.org/rocketscience-projects/javaslang)
[![Coverage Status](https://img.shields.io/coveralls/rocketscience-projects/javaslang.svg)](https://coveralls.io/r/rocketscience-projects/javaslang)

**Javaslang** is a non-profit functional component library for Java&trade; 8 and above.

## Using Javaslang

Projects that include Javaslang need to target Java 1.8 at minimum.

### Maven dependency

The .jar is available at [Maven Central](http://search.maven.org/#search|ga|1|a:"javaslang").

```xml
<dependency>
    <groupId>com.javaslang</groupId>
    <artifactId>javaslang</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Standalone

Because Javaslang has _no_ dependencies to other libraries you can easily add it as standalone .jar to your classpath.

Javaslang can be downloaded [here](http://search.maven.org/#search|ga|1|a:"javaslang").

## Developing Javaslang

### Some Maven Goals

* Executing tests: `mvn clean test`
* Executing doclint: `mvn javadoc:javadoc`
* Executing code coverage report: `mvn -P ci clean test jacoco:report`
* Create -javadoc.jar: `mvn javadoc:jar`
* Create -source.jar: `mvn source:jar`

### Release Management

See https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide


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
