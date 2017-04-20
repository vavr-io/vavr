Please ensure that all major types but Functions, currently Tuples, Values and Traversables, have a `transform` method.

Example (`TYPE` is replaced by the actual type):

```java
/**
 * Transforms this {@code TYPE}.
 *
 * @param f   A transformation
 * @param <U> Type of transformation result
 * @return An instance of type {@code U}
 * @throws NullPointerException if {@code f} is null
 */
default <U> U transform(Function<? super TYPE<? super T>, ? extends U> f) {
    Objects.requireNonNull(f, "f is null");
    return f.apply(this);
}
```

See also [#716](https://github.com/vavr-io/vavr/issues/716#issuecomment-163399633).
