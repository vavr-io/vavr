## Coding Conventions

### Style guidelines

* The file `eclipse_formatter.xml` has to be used with Eclipse IDE.
* It is recommended to automatically organize imports and format code on save action.

### Javadoc

* Public API needs javadoc, e.g. public classes and public methods.
* Non-trivial private methods need javadoc, too.
* A package, which is part of the public API, contains a `package-info.java`.
* Unit tests contain no javadoc at all (because they introduce no new API and contain no business logic).
* Running `mvn javadoc:javadoc` results in no javadoc errors.
* All classes start with the following copyright notice, which contains the list of core developers:
```java
/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
```

### Packages

* There is only one first-level package: javaslang.
* The maximum package depth is two.
* Package names are denoted in singular.
* Packages are sliced by domain.
* Util packages do not exist.
* Cyclic dependencies between packages do not exist.
* To avoid cycles, coherent classes are grouped in packages. E.g. Arrays is located in javaslang.lang instead of javaslang.collection.
* Package private classes are welcome in order to hide non-public API.
* Inner classes are preferred over package private classes in case of one-to-one dependencies.

### Exceptions

* Unchecked exceptions, i.e. RuntimeExceptions, which are not expected to be handled explicitly by the User of an API, are not declared in the method signature. Instead, unchecked exceptions are declared in the javadoc to document the API.

### Extension Methods

* Classes containing extensions methods have the name of the extended class and end with an 's'. E.g. `java.util.List` is extended by `javaslang.collection.Lists`.
* Classes containing extensions methods are final and have a private constructor.
* Extension methods are static.
* The private constructor of class Xxx is:
```java
/**
 * This class is not intended to be instantiated.
 */
private Xxx() {
	requireNotInstantiable();
}
```
* The extended type is the first parameter of an extension method. E.g. `Lists.lastElement(list)`.
* Use `Iterable` instead of `Collection` to support also `javaslang.collection.*`. See `javaslang.Streamz.*` for streaming Iterables. 
* Use `CharSequence` instead of `String` to widen the API (`charSequence.toString()` will provide the String).

### toString
* The String representation of an object o should be `o.getClass().getSimpleName() + "(" + string + ")"`.

### Class instantiation

* Classes are instantiated via `new`.
* Interface may provide static factory methods. E.g. javaslang.option.Option.of(Object obj).
* Extension methods may instantiate classes, too. 

### Unit tests

* Public API is tested.
* High-level functionality is tested in first place.
* Corner cases are tested.
* Trivial methods are not tested, e.g. getters, setters.
* The test method name documents the test, i.e. 'shouldFooWhenBarGivenBaz'
* In most cases it makes sense to run one assertion per @Test.

### 3rd party libraries

* Javaslang has no dependencies other than Java.
* Unit tests depend solely on junit and assertj.

## SCM

* Commits are coarsely granular grouped by feature/change.
* Commits do not mix change sets of different domains/purpose.
* Commit messages provide enough detail to extract a changelog for a new release.

## IDE

Currently it is recommended to use IntelliJ IDEA because of performance issues with Eclipse regarding type inference. Please note that these issues only occur when developing Javaslang - for using Javaslang, Eclipse is fine.

Using IntelliJ IDEA, the Community Edition works out-of-the-box. The idea-settings.jar can be found in the repo.

Using Eclipse, the minimal requirements are

* Eclipse 'Platform Runtime Binary' (http://download.eclipse.org/eclipse/downloads/)
* jdt (Eclipse Java Development Tools)
* m2e (Maven Integration for Eclipse)
* git (Eclipse Git Team Provider)
* Code Recommenders for Java Developers
