# Contributing Guidelines

(work in progess)

## Writing Extensions

```java
public final class SomeExtension {
	
    private SomeExtension() {
        throw new AssertionError(SomeExtension.class.getName() + " cannot be instantiated.");
    }

    public static X extensionMethod() {
        ...
    }

}
```
