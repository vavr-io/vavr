## Javaslang [![Build Status](https://travis-ci.org/rocketscience-projects/javaslang.png)](https://travis-ci.org/rocketscience-projects/javaslang)

**Javaslang** is a functional library for Java&trade; 8 and above.

## Avoid use of null

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

    // if list is null or first element >= 2, result is "nothing"
    // else the first list element is returned as string

}
```

## Fluent code, less exception boilerplate

## Match because if/return is like goto 
