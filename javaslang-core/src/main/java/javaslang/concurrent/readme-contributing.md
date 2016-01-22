These are the ideas behind the implementation of Future/Promise:

The design follows separation of concerns in two manners:

- public interfaces vs. internal implementations (Promise/PromiseImpl, Future/FutureImpl)
- Future = read-only, Promise = write-once

### Interfaces vs. internal classes

Concurrent programming is all about synchronized state. To keep this as simple as possible, it is a good idea to
separate readable and writable state. The internal implementations encapsulate the state. Their number of methods
drill down to those which access instance variables. These classes are typically very short. They represent a
clean and easy to maintain, thread-safe core.

The interfaces define, beside abstract methods, static factory methods, static extension methods and default methods.
The default implementations are built on top of the tread-safe core mentioned above. Typically, no additional
synchronization takes place here.

### Read-only vs. write-once

Separating the end-user API into the Future interface, providing the read-only API, and the Promise interface,
providing the write-once API, solves a different concurrency problem than state synchronization. By separating
these concerns, we define a specific _programming model_ that allows us to easily deal with common tasks in
concurrent programming.

```java
final Promise<String> promise = Promise.make();
final Future<String> future = promise.future();

// producer
Future.run(() -> {
    promise.success(produceSomething());
    continueDoingSomethingUnrelated();
});

// consumer
Future.run(() -> {
    startDoingSomething();
    future.onSuccess(doSomethingWithResult);
});
```
