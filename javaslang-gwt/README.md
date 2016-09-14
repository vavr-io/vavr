# GWT support for Javaslang

Ensure that your `~/.m2/settings.xml` contains the following:

```xml
<repository>
  <id>snapshots-google</id>
  <url>https://oss.sonatype.org/content/repositories/google-snapshots</url>
  <releases>
    <enabled>false</enabled>
  </releases>
  <snapshots>
    <enabled>true</enabled>
  </snapshots>
</repository>
```

### Run tests

```
mvn -pl \!javaslang-benchmark -Pgwt test
```

### Start codeserver

```
mvn -pl \!javaslang-benchmark -DskipTests -Pgwt gwt:codeserver
```