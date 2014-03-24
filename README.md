## Why Javaslang? [![Build Status](https://travis-ci.org/rocketscience-projects/javaslang.png)](https://travis-ci.org/rocketscience-projects/javaslang)

**Javaslang** is a non-profit functional library for Java&trade; 8 and above. With the release of Java 8 we face a new programming paradigm - Java goes functional. Objects are not obsolete - the opposite is true. We've learned from other JVM languages like Scala, that it is a good practice to take the best from both worlds, objects and functions.

Most libraries, also the popular ones, like spring, apache-commons and google-guava, can be considered as outdated from the perspective of Java 8. Even if new functionality targeting Java 8 is added to these libraries, they still will carry all the burden of the past. Javaslang is a fresh and lightweight start into the second age of Java. It is no re-implementation of existing APIs in a new fashion. Javaslang is simple and focused.

Javaslang makes your code more concise by reducing boilerplate. In particular

* it closes the gap between primitive data types and objects by providing extensions methods, e.g. to uniform the streaming API. Did you notice for example, that IntStream and Stream have no common super type which provides the methods map, flatMap and filter?
* it introduces new types to [avoid using null](http://blog.rocketscience.io/java-8-the-happy-path/) (also read [this](http://blog.rocketscience.io/your-codebase-looks-like-this/)) and defers exception handling. This can be subsumed under the term [monadic](http://blog.rocketscience.io/trying-to-explain-monads).

The functionality described above is the basis to create simple and powerful high-level APIs based on Java 8. This is demonstrated with the javaslang.io package for resource reading and conversion. Future releases of Javaslang will contain additional high-level functionality for text parsing, a jdbc layer based on Lambdas, etc.

## Howto integrate Javaslang in your Project

The .jars are available at [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Cjavaslang).

```xml
<dependency>
    <groupId>com.javaslang</groupId>
    <artifactId>javaslang</artifactId>
    <version>1.0.0</version>
</dependency>
```

Please ensure that the maven .pom targets jdk 1.8.

## Content

1. Packages and their Dependencies
2. Language
    1. Lang - assertions and a better println
    2. Arrays - type conversion and bulk operations
    3. Strings - string operations
    4. Runtimes - definite jvm termination
    5. Timers - syntactic sugar for Timer
3. Monads and Matching
    1. Option - null avoidance
    2. Match - type and value matching
    3. Try - deferred exception handling
    4. Either - Variety of results
4. Collections
    1. Collections - missing collection functions
    2. Sets - set operations (math.)
5. Input/Output
    1. IO - resource loading and encoding
6. Parsers
7. (_scheduled_) Jdbc - a functional jdbc layer
8. (_scheduled_) Json - another json api
9. (_scheduled_) Xml - missing xml functions


## Packages and their Dependencies

```
|             io            |          text          |
| collection | either | exception | option | matcher |
|                         lang                       |
 - - - - - - - - - - - - - - - - - - - - - - - - - - -
|                        (java 8)                    |
```

\- - - - - - - - - - - - -_work in progress below this line_- - - - - - - - - - - - -

## Option - Avoid use of null

Use Option to represent value which may be undefined. Some represents the a value (which may be null), and None is the placeholder for nothing. Both types implement Option, so that no more NullPointerExceptions should occur.

```java
<T> Option<T> head(List<T> list) {
    if (list == null || list.isEmpty()) {
        return None.instance();
    } else {
        return new Some<>(list.get(0));
    }
}

void test() {

    List<Integer> list = Arrays.asList(3, 2, 1);

    String result = head(list)
        .filter(i -> i < 2)
        .map(el -> el.toString())
        .orElse("nothing");    

}
```

If list is null or first element >= 2, result is "nothing" else the first list element is returned as string.

## Use match expression instead of if/return statements 

Instead of

```java
static Stream<?> getStream(Object object) {
    if (o == null) {
        throw new IllegalArgumentException("object is null");
    }
    final Class<?> type = o.getClass().getComponentType();
    if (type.isPrimitive()) {
        if (boolean.class.isAssignableFrom(type)) {
            return Arrays.stream((boolean[]) o);
        } else if (byte.class.isAssignableFrom(type)) {
            return Arrays.stream((byte[]) o);
        } else if (char.class.isAssignableFrom(type)) {
            return Arrays.stream((char[]) o);
        } else if (double.class.isAssignableFrom(type)) {
            return Arrays.stream((double[]) o);
        } else if (float.class.isAssignableFrom(type)) {
            return Arrays.stream((float[]) o);
        } else if (int.class.isAssignableFrom(type)) {
            return Arrays.stream((int[]) o);
        } else if (long.class.isAssignableFrom(type)) {
            return Arrays.stream((long[]) o);
        } else if (short.class.isAssignableFrom(type)) {
            return Arrays.stream((short[]) o);
        } else {
            throw new IllegalStateException("Unknown primitive type: " + o.getClass());
        }
    } else {
        return Arrays.stream((Object[]) o);
    }
}
```

the match API allows us to write

```java
    static final Match<Stream<?>> ARRAY_TO_STREAM_MATCHER = new Match<Stream<?>>()
            .caze((boolean[] a) -> Arrays.stream(a))
            .caze((byte[] a) -> Arrays.stream(a))
            .caze((char[] a) -> Arrays.stream(a))
            .caze((double[] a) -> Arrays.stream(a))
            .caze((float[] a) -> Arrays.stream(a))
            .caze((int[] a) -> Arrays.stream(a))
            .caze((long[] a) -> Arrays.stream(a))
            .caze((short[] a) -> Arrays.stream(a))
            .caze((Object[] a) -> Arrays.stream(a));
    
    static Stream<?> getStream(Object object) {
        return ARRAY_TO_STREAM_MATCHER.apply(object);
    }
```

It is also possible to match values instead of types by passing prototype objects to the caze function:

```java
Match matcher = Matchs.caze("Moin", s -> s + " Kiel!");

// and then...
String s = matcher.apply("Moin"); // = "Moin Kiel!"
```

## Write fluent code and reduce exception handling boilerplate

Exception handling adds additional technical boilerplate to our source code. Also Java does not distinguish between Fatal (non-recoverable) and NonFatal (recoverable) exceptions.

Use Try to handle exceptions in a clean way.

```java
Try<byte[]> bytes = IO.loadResource("some/system/resource.txt");

String s = Matchs
    .caze((Success<byte[]> s) -> new String(s.get()))
    .caze((Failure f) -> f.toString())
    .apply(bytes);
```

No try/catch needed here.
