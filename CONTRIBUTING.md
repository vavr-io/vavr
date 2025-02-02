# How to Contribute

Vavr needs to be compiled with **jdk 1.8.0_40** at a minimum, which fixes many type inference bugs of the java compiler.

[Fork](https://help.github.com/articles/fork-a-repo) the GitHub, send a [pull request](https://help.github.com/articles/using-pull-requests) and keep your fork in [sync](https://help.github.com/articles/syncing-a-fork/) with the upstream repository.

## Building

Some people have reported problems building Vavr on their platforms. You should be able to build Vavr with Java 8 or above, but on some platforms (operating systems or distributions), you might need to choose from a narrower range of versions of Java, Maven, and the Scala plugin for Maven. See [this issue](https://github.com/vavr-io/vavr/issues/2321) for details.

## IDE

We use recent IDE version to develop Vavr. IntelliJ IDEA is preferred over Eclipse.

Using IntelliJ IDEA, the Community Edition works out-of-the-box. The idea-settings.jar can be found in the repo.

The IDEA settings include:

* Code Style
* File templates
* Inspection profiles

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
* All classes start with the following copyright notice in order to apply the Apache-2.0 license:

```java
/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2025 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
```

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
* Benchmarks are done with JMH

## Build

### Useful Maven Goals

* Executing tests: `mvn clean test`
* Executing doclint: `mvn javadoc:javadoc`
* Executing code coverage report: `mvn -P ci clean test jacoco:report`
* Create -javadoc.jar: `mvn javadoc:jar`
* Create -source.jar: `mvn source:jar`
* Update version properties: `mvn versions:update-properties`
* Check for new plugin version: `mvn versions:display-plugin-updates`

### Benchmarks

If you have dedicated hardware (i.e. no virtual machines) and are interested in how Vavr compares to other alternatives,
you can run all benchmarks from the `vavr-benchmark` module via `io.vavr.JmhRunner.main` or running the following Maven command:

```bash
mvn clean test -P benchmark -pl vavr-benchmark 
```

Note: running all the tests will require several hours, during which there should be no other activity done on the given machine.

### Releasing

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

Note: Detailed information about performing a release can be found in the SCM section.

## SCM

* Commits are coarsely granular grouped by feature/change.
* Commits do not mix change sets of different domains/purpose.
* Commit messages provide enough detail to extract a changelog for a new release.


### Branching Model

We following a simple git workflow/branching model:

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

We currently check for API changes (which may affect the binary compatibility) using the maven-bundle-plugin:

```bash
mvn package org.apache.felix:maven-bundle-plugin:baseline -DcomparisonVersion=2.0.1 -DskipTests
```

In the example above we check API changes between the current branch and the _2.0.1_ tag. In most cases the tag should be the latest official release. 

### Major release

#### Performing a release

Performing a release requires admin-rights.

1. get a fresh copy of the repo `git clone https://github.com/vavr-io/vavr.git`
2. run `mvn clean test` and(!) `mvn javadoc:javadoc` to ensure all is working fine
3. perform the release

    ```bash
    mvn release:clean
    mvn release:prepare
    mvn release:perform
    ```

4. Go to `http://oss.sonatype.org` and stage the release.

#### Post-Release steps (e.g. for release v2.1.0)

1. [CAUTION] Delete the old maintenance branch (e.g. v2.0.x)

    ```bash
    git push origin :v2.0.x
    ```

2. Create the new _maintenance branch_ (e.g. v2.1.x) based on the new release tag (e.g. v2.1.0)

    ```bash
    git checkout origin/master
    git fetch origin
    git branch v2.1.x v2.1.0
    git checkout v2.1.x
    git push origin v2.1.x
    ```

3. Update the version of the _maintenance branch_

    ```bash
    mvn versions:set -DnewVersion=2.1.1-SNAPSHOT
    ```

When a maintenance release is performed, we increase the last digit of the new development version of the maintenance branch (e.g. 2.1.2-SNAPSHOT).

#### Merging specific commits into the maintenance branch

Pull requests are merged into master. Only specific commits are merged from master into the maintenance branch.

```bash
git checkout v2.1.x
git log --date-order --date=iso --graph --full-history --all --pretty=format:'%x08%x09%C(red)%h %C(cyan)%ad%x08%x08%x08%x08%x08%x08%x08%x08%x08%x08%x08%x08%x08%x08%x08 %C(bold green)%aN%C(reset)%C(bold yellow)%d %C(reset)%s'
# pick one or more commits from the log, e.g. a741cf1.
git cherry-pick a741cf1
```

### Bugfix release

#### Steps to bugfix and perform a release

Given a release 1.2.2, we create a bugfix release as follows.

First, we clone the repository. We work on origin instead of a fork (this requires admin rights).

```bash
git clone https://github.com/vavr-io/vavr.git vavr-1.2.3
```

We checkout the release tag and create a new (local) branch.

```bash
git checkout v1.2.2
git checkout -b bugfix-release-1.2.3
```

Then we create the new snapshot version.

```bash
mvn versions:set -DnewVersion=1.2.3-SNAPSHOT
```

Now the changes can be performed to the repository. After that, we test the changes.

```bash
mvn clean test
mvn javadoc:javadoc
```

Then the new files can be added and the changes can be committed.

```bash
git add <files>
git commit -a -m "fixes #<issue>"
```

Then we perform the release as usual:

```bash
mvn release:clean
mvn release:prepare
mvn release:perform
```

Goto oss.sonatype.org and release to Maven Central.

#### Housekeeping

Delete the branch which was pushed by the maven release plugin to origin:

```bash
git checkout master
git branch -D bugfix-release-1.2.3
git push origin :bugfix-release-1.2.3
```

### Release notes

For major, minor and bugfix releases we create release notes on Github.

#### Useful commands

The number of lines changed by author since a specific date:

```bash
git ls-files -z | xargs -0n1 git blame -w --since="3/18/2016" --line-porcelain | grep -a "^author " | sort -f | uniq -c | sort -n -r
```
