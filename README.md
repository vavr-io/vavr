# vavr-champ

vavr-champ is binary compatible with the vavr library, but uses CHAMP-based collections internally.

For more details see the [JavaDoc](https://www.randelshofer.ch/vavr/javadoc/):

- [HashMap](https://www.randelshofer.ch/vavr/javadoc/io/vavr/collection/HashMap.html)

- [HashSet](https://www.randelshofer.ch/vavr/javadoc/io/vavr/collection/HashSet.html
  )

- [LinkedHashMap](https://www.randelshofer.ch/vavr/javadoc/io/vavr/collection/LinkedHashMap.html)

- [LinkedHashSet](https://www.randelshofer.ch/vavr/javadoc/io/vavr/collection/LinkedHashSet.html)

To use it instead of the original `vavr` library, you can specify `vavr-champ` as your dependency.

Maven:

```
<dependency>
    <groupId>ch.randelshofer</groupId>
    <artifactId>vavr-champ</artifactId>
    <version>0.10.5</version>
</dependency>
```

Gradle:

```
implementation group: 'ch.randelshofer', name: 'vavr-champ', version: '0.10.5'
```