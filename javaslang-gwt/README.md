# GWT support for Javaslang

### Using Javaslang in GWT maven projects

* Add the following maven dependency to your project:

```
<dependency>
    <groupId>io.javaslang</groupId>
    <artifactId>javaslang-gwt</artifactId>
    <version>{javaslang-current-version}</version>
</dependency>
```

* Inherit the `Javaslang` module in your GWT module's descriptor file:

```
<module>
    <!-- ... -->
    <inherits name="Javaslang"/>
    <!-- ... -->
</module>
```

* Use the Javaslang APIs in your code.