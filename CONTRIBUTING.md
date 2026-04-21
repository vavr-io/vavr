# How to Contribute

[Fork](https://help.github.com/articles/fork-a-repo) the GitHub, send a [pull request](https://help.github.com/articles/using-pull-requests) and keep your fork in [sync](https://help.github.com/articles/syncing-a-fork/) with the upstream repository.

Vavr requires JDK 21+ to build. Maven 3.9.9+ is enforced by the build.

## AI-assisted contributions

Using AI tools to help write code is fine - please just make sure you understand and stand behind every line you submit. PRs that are easier to recreate from scratch than to review are unlikely to be merged.

To avoid disappointment, open a GitHub issue or discussion before starting work on anything non-trivial (especially AI-assisted changes) so we can align on the approach first.

## Coding Conventions

We follow _Rob Pike's 5 Rules of Programming_:

> * **Rule 1. You can't tell where a program is going to spend its time.** Bottlenecks occur in surprising places, so don't try to second guess and put in a speed hack until you've proven that's where the bottleneck is.
> * **Rule 2. Measure.** Don't tune for speed until you've measured, and even then don't unless one part of the code overwhelms the rest.
> * **Rule 3. Fancy algorithms are slow when n is small, and n is usually small.** Fancy algorithms have big constants. Until you know that n is frequently going to be big, don't get fancy. (Even if n does get big, use Rule 2 first.)
> * **Rule 4. Fancy algorithms are buggier than simple ones, and they're much harder to implement.** Use simple algorithms as well as simple data structures.
> * **Rule 5. Data dominates.** If you've chosen the right data structures and organized things well, the algorithms will almost always be self-evident. Data structures, not algorithms, are central to programming.
>
> Pike's rules 1 and 2 restate Tony Hoare's famous maxim, "Premature optimization is the root of all evil." Ken Thompson rephrased Pike's rules 3 and 4 as "When in doubt, use brute force.". Rules 3 and 4 are instances of the design philosophy KISS. Rule 5 was previously stated by Fred Brooks in The Mythical Man-Month. Rule 5 is often shortened to "write stupid code that uses smart objects".

_Source: http://users.ece.utexas.edu/~adnan/pike.html_

### Javadoc

* Public API needs Javadoc, e.g., public classes and public methods.
* Non-trivial private methods need Javadoc, too.
* A package, which is part of the public API, contains a `package-info.java`.
* Unit tests contain no Javadoc at all (because they introduce no new API and contain no business logic).
* Running `mvn javadoc:javadoc` results in no javadoc errors.
* The Apache-2.0 license header, unused imports, import order, and trailing newlines are enforced by the Spotless plugin (`mvn spotless:apply`).

### Packages

* There is only one first-level package: io.vavr.
* The maximum package depth is two.
* Package names are denoted in the singular.
* Packages are sliced by domain (no util or tool packages).
* Package private classes are used in order to hide non-public API.
* Inner classes are preferred over package-private classes in case of one-to-one dependencies.

### File structure

We organize our classes and interfaces in the following way:

* The Javadoc of the type contains an overview of the new (i.e. not overridden) API declared in the actual type.
* The type consists of three sections:
   1. static API
   2. non-static API
   3. adjusted return types

```java
/**
 * Description of this class.
 * 
 * <ul>
 * <li>{@link #containsKey(Object)}}</li>
 * <li>{@link ...}</li>
 * </ul>
 * 
 * @author ...
 */
public interface Map<K, V> extends Traversable<Tuple2<K, V>> {
    
    // -- static API
    
    static <K, V> Tuple2<K, V> entry(K key, V value) { ... }
    
    ...
    
    // -- non-static API

    @Override
    default boolean contains(Tuple2<K, V> element) { ... }
    
    boolean containsKey(K key);
    
    ...
    
    // -- Adjusted return types

    @Override
    Map<K, V> distinct();
    
    ...
    
}
```

### Unit tests

* Public API is tested.
* High-level functionality is tested in first place.
* Corner cases are tested.
* Trivial methods are not _directly_ tested, e.g. getters, setters.
* The test method name documents the test, i.e. 'shouldFooWhenBarGivenBaz'
* In most cases it makes sense to run one assertion per @Test.

### 3rd party libraries

* Vavr has no dependencies other than Java.
* Unit tests depend solely on junit and assertj.

## Build

### Useful Maven Goals

* Executing tests: `mvn clean test`
* Executing doclint: `mvn javadoc:javadoc`
* Create -javadoc.jar: `mvn javadoc:jar`
* Create -source.jar: `mvn source:jar`
* Update version properties: `mvn versions:update-properties`
* Check for new plugin version: `mvn versions:display-plugin-updates`

### Releasing

Publishing to Maven Central is handled by the `release` GitHub Actions workflow. Locally, a maintainer only needs to tag the release:

```bash
mvn release:prepare
```

The workflow is then dispatched with the resulting tag to publish the artifacts.

## SCM

* Commits are coarsely granular grouped by feature/change.
* Commits do not mix change sets of different domains/purpose.
* Commit messages provide enough detail to extract a changelog for a new release.


### Branching Model

We follow a simple git workflow/branching model:

```
                         master
                           |
                           |     v2.0.x
release v2.0.0 - - - - - - + - - - + 2.0.1-SNAPSHOT
                           |       |
                  bugfix1  |       |
                     |     |       |
                  PR x---->|<------+ cherry-picking bugfix1
                           |       |
                  featureA |       |
                     |     |       |
                  PR x---->|       |
                           |       |
release v2.0.1 - - - - - - | - - - + 2.0.2-SNAPSHOT
                           |       |
                           |       |     v2.1.x
release v2.1.0 - - - - - - + - - - X - - - + 2.1.1-SNAPSHOT
                           |               |
                           |               |
                  featureB |               |
                     |     |               |
                  PR x---->|               |
                          ...             ...
```

## Versioning

We follow the [Semantic Versioning](http://semver.org) scheme.

### Backward compatibility

We distinguish between 3 kinds of (backward-)compatibilty:

1. **Source** - Source compatibility concerns translating Java source code into class files.
2. **Binary** - Binary compatibility is [defined](http://java.sun.com/docs/books/jls/third_edition/html/binaryComp.html#13.2) in The Java Language Specification as preserving the ability to link without error.
3. **Behavioral** - Behavioral compatibility includes the semantics of the code that is executed at runtime.

_Source: [OpenJDK Developers Guide v0.777, Kinds of Compatibility](http://cr.openjdk.java.net/~darcy/OpenJdkDevGuide/OpenJdkDevelopersGuide.v0.777.html#compatibility)_

Given a version number `<major>.<minor>.<path>` Vavr

* may affect **behavioral** compatibility in **all kind of releases**, especially bug fix/patch releases. For example we might decide to release a more effective hashing algorithm in the next minor release that reduces the probability of collisions.
* may affect **source** compatibility in **patch** releases. For example this may be the case when generic type bounds of method signatures need to be fixed.
* retains **binary** backwards compatibility (drop in replacement jar) within the same **minor** version (this includes **patch** versions)
* is not **binary** backward compatible when the **major** version changes

Summing up, drop-in replacements of Vavr can be made for **minor** and **patch** releases.

### Tool Support

We check for API changes (which may affect binary compatibility) using the maven-bundle-plugin:

```bash
mvn package org.apache.felix:maven-bundle-plugin:baseline -DcomparisonVersion=<latest-release> -DskipTests
```
