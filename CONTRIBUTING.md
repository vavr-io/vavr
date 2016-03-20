# How to Contribute

Javaslang needs to be compiled with **jdk 1.8.0_40** at minimum, which fixes many type inference bugs of the java compiler.

https://help.github.com/articles/fork-a-repo[Fork] the GitHub, send a https://help.github.com/articles/using-pull-requests[pull request] and keep your fork in https://help.github.com/articles/syncing-a-fork/[sync] with the upstream repository.

## IDE

We use recent IDE version to develop Javaslang. IntelliJ IDEA is preferred over Eclipse.

Using IntelliJ IDEA, the Community Edition works out-of-the-box. The idea-settings.jar can be found in the repo.

The IDEA settings include:

* Code Style
* File templates
* Inspection profiles

## Coding Conventions

Just a few notes here. In general it is good to look at existing code to get a clear picture.

### Javadoc

* Public API needs javadoc, e.g. public classes and public methods.
* Non-trivial private methods need javadoc, too.
* A package, which is part of the public API, contains a `package-info.java`.
* Unit tests contain no javadoc at all (because they introduce no new API and contain no business logic).
* Running `mvn javadoc:javadoc` results in no javadoc errors.
* All classes start with the following copyright notice, which contains the list of core developers:
```java
/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
```

### Packages

* There is only one first-level package: javaslang.
* The maximum package depth is two.
* Package names are denoted in singular.
* Packages are sliced by domain (no util or tool packages).
* Package private classes are used in order to hide non-public API.
* Inner classes are preferred over package private classes in case of one-to-one dependencies.

### Unit tests

* Public API is tested.
* High-level functionality is tested in first place.
* Corner cases are tested.
* Trivial methods are not _directly_ tested, e.g. getters, setters.
* The test method name documents the test, i.e. 'shouldFooWhenBarGivenBaz'
* In most cases it makes sense to run one assertion per @Test.

### 3rd party libraries

* Javaslang has no dependencies other than Java.
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

mvn clean test -Pbenchmark

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

```java
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

### Major release

#### Performing a release

Performing a release requires admin-rights.

1. get a fresh copy of the repo `git clone https://github.com/javaslang/javaslang.git`
2. perform the release

    ```bash
    mvn release:clean
    mvn release:prepare
    mvn release:perform
    ```

3. Go to `http://oss.sonatype.org` and stage the release.

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
git log --pretty=oneline --abbrev-commit
# pick a commit from the log, e.g. a741cf1
git cherry-pick a741cf1
```
