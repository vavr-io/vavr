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

We are following a simple git workflow/branching model:

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

Given a version number `<major>.<minor>.<path>`, Vavr

* may affect **behavioral** compatibility in **all kind of releases**, especially bug fix/patch releases. For example we might decide to release a more effective hashing algorithm in the next minor release that reduces the probability of collisions.
* may affect **source** compatibility in **patch** releases. For example this may be the case when generic type bounds of method signatures need to be fixed.
* retains **binary** backwards compatibility (drop in replacement jar) within the same **minor** version (this includes **patch** versions)
* is not **binary** backward compatible when the **major** version changes

Summing up, drop-in replacements of Vavr can be made for **minor** and **patch** releases.

### Major release

#### Performing a release

Performing a release requires admin-rights.

1. get a fresh copy of the repo `git clone https://github.com/vavr-io/vavr.git`
2. run `./gradlew clean check` to ensure all is working fine
3. perform the release `./gradlew release --info`
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

3. Update the version of the _maintenance branch_ in `gradle.properties` (e.g. 2.1.1-SNAPSHOT)

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

Then we create the new snapshot version by editing `gradle.properties` (e.g. 1.2.3-SNAPSHOT). Now the changes can be performed to the repository. After that, we test the changes.

```bash
./gradlew clean check
```

Then the new files can be added and the changes can be committed.

```bash
git add <files>
git commit -a -m "fixes #<issue>"
```

Then we perform the release as usual:

```bash
./gradlew release --info
```

Go to https://oss.sonatype.org and release to Maven Central.

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
