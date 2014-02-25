## Javaslang

[Javaslang](http://javaslang.com) is a [rocketscience.io](https://github.com/rocketscience-projects) project.

[![Build Status](https://travis-ci.org/rocketscience-projects/javaslang.png)](https://travis-ci.org/rocketscience-projects/javaslang)

## Change is the only Constant

**Javaslang** _/ˈdʒɑːvə/ /slæŋ/_ is a library for the Java&trade; 8 language. Why _~slang_?

> "Slang consists of a lexicon of non-standard words and phrases in a given language."

> Michael Adams remarks that "Slang is on the edge." 

> "It is often difficult to differentiate slang from (...) standard language, because slang generally becomes accepted into the standard lexicon over time."

> "Still, while a great deal of slang takes off, even becoming accepted into the standard lexicon, much slang dies out (...)"

_\- taken from http://en.wikipedia.org/wiki/Slang_

The purpose of Javaslang is to embrace the new concepts which come with Java 8 and make the features more accessible. There are many resources on the internet about [lambdas](http://cr.openjdk.java.net/~briangoetz/lambda/lambda-state-final.html), the new [streaming API](http://cr.openjdk.java.net/~briangoetz/lambda/collections-overview.html) and [default methods](http://cr.openjdk.java.net/~briangoetz/lambda/Defender%20Methods%20v4.pdf). How can we leverage these features in our daily programming?

## The need for First Class Functions

The object oriented programmer tends to think object oriented ;-) Our classes are logically connected by design patterns. They form the structure of our programs. Systems are built top down - we think of the abstract whole, analyze the details and restructure (read: refactor) our system to meet new requirements. It is all about structure.

But wait - what about the business logic? We have no structure for business logic, right? It's behavior. Yes, the GOF book tells us, there are structural *and* behavioral patterns but [behavioral patterns](http://en.wikipedia.org/wiki/Behavioral_pattern) are structure for communication between objects.

Functions for the rescue! Objects are data structures with functions, closures/lambdas are functions with data. Business logic is all about computation, we transform data into something new and valuable.

Naturally, in contrast to object structures, computations are built bottom up. We use functions to compute values. The results are input of new computations and so on.

Java 8 offers the new streaming API to chain operations. This is an instant transformation of data, which can be easily performed in parallel.

## What Does Javaslang Offer?

Java 8 allows us to express things in different ways - object oriented and functional. The `Javaslang` component library gives us the tools to

* Prevent NullPointerExceptions
* Handle exceptions without try/catch
* Focus on computations without technical interruption and boilerplate

This is accomplished by introducing the new types

* Option (a replacement for java.util.Optional)
* Try
* Either

Having these types, we need new components which make use of them. For example, the [apache-commons](http://commons.apache.org) library and [google-guava](http://code.google.com/p/guava-libraries) were not designed to help us coding the functional way. They are intended to be used with previous versions of Java.

The Javaslang library was designed from the ground up to be used with all the awesomeness Java 8 offers.

## The need for Container types

`Optional` is an example for a container type. It can hold a non-null value and has a specific representation (empty) for null. It encourages developers to check the state of an Optional before performing specific operations because it is obvious that an Optional may be empty. This is the big difference to null values. Often it is not clear if a value may be null or not which leaves us in NPE hell.

There are other container types which serve different purposes. Lets take a look at `Option`, `Try` and `Either` in the following.

### Motivation

How would we get the first element of a given list? There are three general cases we want to take a look at.

**1) Rely on integrity**

```java
Object getFirstElement1(List<?> list) {
    return list.get(0);
}
```

This will throw an `IndexOutOfBoundsException` Exception if the given list is null or empty. It is generally no good idea to rely on integrity.

**2) Use _null_ as placeholder for _nothing_.**

```java
Object getFirstElement2(List<?> list) {
    if (list == null || list.isEmpty()) {
        return null;
    } else {
        return list.get(0);
    }
}
```

We are lost in an ambiguous situation because it is not clear how to interpret `null`. The `null` value is returned, if the list is `null` or empty or if the first element of the list is `null`.

**3) Throw an exception back to the caller if there is no first element.**

```java
Object getFirstElement3(List<?> list) throws Exception {
   if (list == null || list.isEmpty()) {
      throw new Exception("no such element");
   } else {
      return list.get(0);
   }
}
```

This is the Java <8 way to circumvent an undefined state. But it introduces new problems.

### What's wrong with java.util.Optional?

Java 8 introduces the container type `java.util.Optional`, which holds one value or is empty if the value is null. We have to make a decision what `Optional.empty()` should represent.

#### Map empty list to Optional.empty()

`Optional.empty()` represents the empty list (null or empty). `Optional.of(elem)` throws if `elem` is null, so we have to use `Optional.ofNullable(elem)`.

```java
Optional<?> getFirstElement2(List<?> list) {
    if (list == null || list.isEmpty()) {
        return Optional.empty();
    } else {
        return Optional.ofNullable(list.get(0));
    }
}
```

This means that effectively we get an `Optional.empty()` if the list is empty or the element is `null`. We leave the caller of the method in an ambivalent situation.

#### Map empty list to null

A second solution is to represent the empty list by `null`. If the first element of the list is `null` we have `Optional.empty()` and otherwise `Optional.of(elem)`.

```java
Optional<?> getFirstElement2(List<?> list) {
    if (list == null || list.isEmpty()) {
        return null;
    } else {
        return Optional.ofNullable(list.get(0));
    }
}
```

The resulting values are not ambiguous any more. But the caller has to perform `null` checks.

#### We can do better: javaslang.util.Option

`Option` is roughly equivalent to `Optional`. The difference is, that it allows us to store `null` values. `Option.of(elem)` has the same semantics as `Optional.of(elem)`. But additionally it offers specific types `Some` (has a value) and `None` (is empty).

```java
Option<?> getFirstElement2(List<?> list) {
    if (list == null || list.isEmpty) {
        return None.instance();
    } else {
        return new Some(list.get(0));
    }
}
```

Yay! Now the result is always an `Option` and we can distinguish between an empty list and a `null` value.

## javaslang.util.Try

_\#tobedone_

## javaslang.util.Either

_\#tobedone_

# _Work in progress..._
