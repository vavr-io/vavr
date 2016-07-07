# GWT support for Javaslang

Highly Experimental, works and tested in IntelliJ Idea only

## Building GWT artifacts

Tested with 398030dfb188a8676e57ac3c95d07dd60172a1b4

```
git checkout 398030df
ant clean elemental dist-dev
mvn install:install-file -Dfile=build/lib/gwt-user.jar -DgroupId=com.google.gwt -DartifactId=gwt-user -Dversion=2.8.0-398030df -Dpackaging=jar 
mvn install:install-file -Dfile=build/lib/gwt-dev.jar -DgroupId=com.google.gwt -DartifactId=gwt-dev -Dversion=2.8.0-398030df -Dpackaging=jar 
```