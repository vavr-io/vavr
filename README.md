## Javaslang [![Build Status](https://travis-ci.org/rocketscience-projects/javaslang.png)](https://travis-ci.org/rocketscience-projects/javaslang)

**Javaslang** _/ˈdʒɑːvə/ /slæŋ/_ is a standard library extension for the Java&trade; 8 language. Why _~slang_?

> "Slang consists of a lexicon of non-standard words and phrases in a given language."

> Michael Adams remarks that "Slang is on the edge." 

> "It is often difficult to differentiate slang from (...) standard language, because slang generally becomes accepted into the standard lexicon over time."

_\- taken from http://en.wikipedia.org/wiki/Slang_

The purpose of Javaslang is to embrace the new concepts which come with Java 8 and make the features more accessible. There are many resources on the internet about [lambdas](http://cr.openjdk.java.net/~briangoetz/lambda/lambda-state-final.html), the new [streaming API](http://cr.openjdk.java.net/~briangoetz/lambda/collections-overview.html) and [default methods](http://cr.openjdk.java.net/~briangoetz/lambda/Defender%20Methods%20v4.pdf). How can we leverage these features in our daily programming?

## The need for First Class Functions

The object oriented programmer tends to think object oriented ;-) Our classes are logically connected by design patterns. They form the structure of our programs. Systems are built top down - we think of the abstract whole, analyze the details and restructure (read: refactor) our system to meet new requirements. It is all about structure.

But wait - what about the business logic? We have no structure for business logic, right? It's behavior. Yes, the GOF book tells us, there are structural *and* behavioral patterns but [behavioral patterns](http://en.wikipedia.org/wiki/Behavioral_pattern) are structure for communication between objects.

Functions for the rescue! Objects are data structures with functions, closures/lambdas are functions with data. Business logic is all about computation, we transform data into something new and valuable.

Naturally, in contrast to object structures, computations are built bottom up. We use functions to compute values. The results are input of new computations and so on.

Java 8 offers the new streaming API to chain/pipe operations. This is an instant transformation of data, which can be easily performed in parallel. Along the pipeline of operations we have to deal with common problems: undefined states, null values, (non-fatal) exceptions, etc. We don't want to clutter our code base with technical stuff like null-checks and try-catch blocks. This is the part where functional patterns help us to create better programs.

## What does Javaslang offer?

Java 8 allows us to express things in different ways - object oriented and functional. Popular component libraries, like the [apache-commons](http://commons.apache.org) library and the [google-guava](http://code.google.com/p/guava-libraries) library, were not designed to help us coding the functional way. There is the need for new (versions of) Java libraries, which embrace the concepts of functional programming.

The Javaslang component library gives us the tools to

* Prevent NullPointerExceptions
* Handle exceptions without try/catch
* Focus on computations without technical interruption and boilerplate

This is accomplished by bringing [Option](https://github.com/rocketscience-projects/javaslang/blob/master/src/main/java/javaslang/util/Option.java), [Try](https://github.com/rocketscience-projects/javaslang/blob/master/src/main/java/javaslang/util/Try.java) and [Either](https://github.com/rocketscience-projects/javaslang/blob/master/src/main/java/javaslang/util/Either.java) into play.

Having these types, we need new components which make use of them. For example,  They are intended to be used with previous versions of Java.

The Javaslang library was designed from the ground up to be used with all the awesomeness Java 8 offers.

_Work in progress..._
