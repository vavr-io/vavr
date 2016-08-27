# GWT support for Javaslang

Tested with 7da4bf36a105faf5811235692bf00e86c958f3de

```
git checkout 7da4bf
ant clean elemental dist-dev
mvn install:install-file -Dfile=build/lib/gwt-user.jar -DgroupId=com.google.gwt -DartifactId=gwt-user -Dversion=2.8.0-7da4bf -Dpackaging=jar 
mvn install:install-file -Dfile=build/lib/gwt-dev.jar -DgroupId=com.google.gwt -DartifactId=gwt-dev -Dversion=2.8.0-7da4bf -Dpackaging=jar 
```
