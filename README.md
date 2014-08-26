## Why Javaslang?
[![Build Status](https://travis-ci.org/rocketscience-projects/javaslang.png)](https://travis-ci.org/rocketscience-projects/javaslang)
[![Coverage Status](https://img.shields.io/coveralls/rocketscience-projects/javaslang.svg)](https://coveralls.io/r/rocketscience-projects/javaslang)

**Javaslang** is a non-profit functional component library for Java&trade; 8 and above. Javaslang gives you the best from both worlds, functional and object oriented.

## Howto use Javaslang with Maven

The .jar is available at [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Cjavaslang).

```xml
<dependency>
    <groupId>com.javaslang</groupId>
    <artifactId>javaslang</artifactId>
    <version>1.0.0</version>
</dependency>
```

Please ensure that the maven .pom targets Java 8 at minimum.

## Developing Javaslang
### Maven Goals

* Executing tests: `mvn clean test`
* Executing doclint: `mvn javadoc:javadoc`
* Executing code coverage report: `mvn -P ci clean test jacoco:report`

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

Deploy a snapshot to https://oss.sonatype.org/content/repositories/snapshots/

```
mvn clean deploy
```

Create -javadoc.jar and -source.jar

```
mvn javadoc:jar
mvn source:jar
```

Prepare a release

```
mvn release:clean
mvn release:prepare
```

Stage a release

```
mvn release:perform
```
