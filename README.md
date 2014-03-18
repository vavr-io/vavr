## Javaslang [![Build Status](https://travis-ci.org/rocketscience-projects/javaslang.png)](https://travis-ci.org/rocketscience-projects/javaslang)

**Javaslang** is a functional library for Java&trade; 8 and above.

## Avoid use of null

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
    static final Matcher<Stream<?>> ARRAY_TO_STREAM_MATCHER = Matcher.<Stream<?>>create()
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
Matcher matcher = Matchers.caze("Moin", s -> s + " Kiel!");

// and then...
String s = matcher.apply("Moin"); // = "Moin Kiel!"
```

## Write fluent code and reduce exception handling boilerplate

Exception handling adds additional technical boilerplate to our source code. Also Java does not distinguish between Fatal (non-recoverable) and NonFatal (recoverable) exceptions.

Use Try to handle exceptions in a clean way.

```java
Try<byte[]> bytes = IO.loadResource("some/system/resource.txt");

String s = Matchers.
    .caze((Success<byte[]> s) -> new String(s))
    .caze((Failure f) -> f.toString())
    .apply(bytes);
```

No try/catch needed here.
