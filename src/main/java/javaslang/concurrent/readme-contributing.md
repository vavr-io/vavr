These are the ideas behind the implementation of Future/Promise:

The design follows separation of concerns in two manners:

- public interfaces vs. internal implementations (Promise/PromiseImpl, Future/FutureImpl)
- Future = read-only, Promise = write-once

### Interfaces vs. internal classes

The internal implementations encapsulate the state. Their number of methods drill down to these which access instance
variables. These classes are typically very short. They represent a clean and easy to maintain, thread-safe core.

The interfaces define, beside abstract methods, static factory methods, static extension methods and default methods.
The default implementations are built on top of the tread-safe core mentioned above. Typically, no additional
synchronization takes place here.

### Read-only vs. write-once

Concurrent programming is all about synchronized state. To keep this as simple as possible, it is a good idea to
separate readable and writable state. The Future interface provides the read-only API, the Promise interface the
write-once API.

This split allows us to easily perform concurrent tasks:

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
