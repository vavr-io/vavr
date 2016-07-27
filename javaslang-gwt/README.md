# GWT support for Javaslang

Highly Experimental, works and tested in IntelliJ Idea only

## Building GWT artifacts

Tested with 6dadf9b431dde5cde1d86d2023ba33c047d19e7f

```
git checkout 6dadf9b4
ant clean elemental dist-dev
mvn install:install-file -Dfile=build/lib/gwt-user.jar -DgroupId=com.google.gwt -DartifactId=gwt-user -Dversion=2.8.0-6dadf9b4 -Dpackaging=jar 
mvn install:install-file -Dfile=build/lib/gwt-dev.jar -DgroupId=com.google.gwt -DartifactId=gwt-dev -Dversion=2.8.0-6dadf9b4 -Dpackaging=jar 
```