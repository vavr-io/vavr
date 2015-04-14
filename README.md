[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.javaslang/javaslang/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.javaslang/javaslang)
[![Build Status](https://travis-ci.org/javaslang/javaslang.png)](https://travis-ci.org/javaslang/javaslang)
[![Coverage Status](https://img.shields.io/coveralls/javaslang/javaslang.svg)](https://coveralls.io/r/javaslang/javaslang)

# Javaslang

Feature-rich & self-contained functional programming in Java&trade; 8 and above.

## Using Javaslang

Projects that include Javaslang need to target Java 1.8 at minimum.

The .jar is available at [Maven Central](http://search.maven.org/#search|ga|1|a:"javaslang").

### Gradle

```gradle
compile "com.javaslang:javaslang:1.2.0"
```

### Maven

```xml
<dependency>
    <groupId>com.javaslang</groupId>
    <artifactId>javaslang</artifactId>
    <version>1.2.0</version>
</dependency>
```

### Standalone

Because Javaslang does _not_ depend on any libraries (other than the JVM) you can easily add it as standalone .jar to your classpath.

## Using Developer Versions

Developer versions can be found [here](https://oss.sonatype.org/content/repositories/snapshots/com/javaslang/javaslang).

Ensure that your `~/.m2/settings.xml` contains the following:

```xml
<profiles>
    <profile>
        <id>allow-snapshots</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <repositories>
            <repository>
                <id>snapshots-repo</id>
                <url>https://oss.sonatype.org/content/repositories/snapshots</url>
                <releases>
                    <enabled>false</enabled>
                </releases>
                <snapshots>
                    <enabled>true</enabled>
                </snapshots>
            </repository>
        </repositories>
    </profile>
</profiles>
```

## Howto Contribute

[Fork](https://help.github.com/articles/fork-a-repo) the GitHub repo and send a [pull request](https://help.github.com/articles/using-pull-requests).

Javaslang needs jdk 1.8.0_40+ to compile. Prior jdks will not work due to type inference bugs. However, in order to use Javaslang as maven dependency, any build of jdk 1.8.0 is sufficient. 

```shell
git remote add upstream https://github.com/javaslang/javaslang.git
git fetch upstream
```

### Useful Maven Goals

* Executing tests: `mvn clean test`
* Executing doclint: `mvn javadoc:javadoc`
* Executing code coverage report: `mvn -P ci clean test jacoco:report`
* Create -javadoc.jar: `mvn javadoc:jar`
* Create -source.jar: `mvn source:jar`
* Update version properties: `mvn versions:update-properties` 
* Check for new plugin version: `mvn versions:display-plugin-updates`

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
