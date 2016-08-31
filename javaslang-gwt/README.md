# GWT support for Javaslang

Tested with 535f2fa973995f18d190a689127acbe134a42a15

```
git checkout 535f2fa9
ant clean elemental dist-dev
mvn install:install-file -Dfile=build/lib/gwt-user.jar -DgroupId=com.google.gwt -DartifactId=gwt-user -Dversion=2.8.0-535f2fa9 -Dpackaging=jar 
mvn install:install-file -Dfile=build/lib/gwt-dev.jar -DgroupId=com.google.gwt -DartifactId=gwt-dev -Dversion=2.8.0-535f2fa9 -Dpackaging=jar 
```
