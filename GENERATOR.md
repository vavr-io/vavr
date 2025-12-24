# Vavr Code Generator Documentation

## Table of Contents

1. [Overview](#overview)
2. [Architecture](#architecture)
3. [Quick Start](#quick-start)
4. [Configuration](#configuration)
5. [Core Components](#core-components)
6. [Generated Code Categories](#generated-code-categories)
7. [Generator Framework](#generator-framework)
8. [String Interpolation System](#string-interpolation-system)
9. [Import Management](#import-management)
10. [Extension System](#extension-system)
11. [How to Run](#how-to-run)
12. [How to Extend](#how-to-extend)
13. [Best Practices](#best-practices)

---

## Overview

### Purpose

The Vavr code generator is a sophisticated Scala-based metaprogramming system that automatically generates hundreds of Java source files for the Vavr library. This approach ensures consistency, reduces human error, and makes it practical to maintain multiple arities of similar constructs (like Function0 through Function8, Tuple1 through Tuple8, etc.).

### Why Code Generation?

Vavr needs to support multiple arities for several functional programming constructs:
- **Functions**: `Function0` through `Function8` (and their checked variants)
- **Tuples**: `Tuple0` through `Tuple8`
- **Pattern matching**: `Pattern0` through `Pattern8`
- **For-comprehensions**: `For1` through `For8` (for multiple monadic types)

Writing and maintaining these by hand would be:
- **Error-prone**: Easy to introduce inconsistencies
- **Time-consuming**: Changes must be replicated across many similar files
- **Difficult to maintain**: Keeping patterns consistent is challenging

The generator solves these problems by defining templates once and generating all variants automatically.

### Key Benefits

1. **Consistency**: All generated code follows the same patterns
2. **Maintainability**: Changes to templates propagate automatically
3. **Correctness**: Reduces human error in repetitive code
4. **Scalability**: Easy to add new arities or constructs
5. **Documentation**: Generated code includes comprehensive Javadoc

---

## Architecture

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Generator.scala (Main Script)             │
│                                                               │
│  ┌───────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │  Entry Point  │→ │ generateMain │→ │ generateTest │     │
│  │    run()      │  │   Classes    │  │   Classes    │     │
│  └───────────────┘  └──────────────┘  └──────────────┘     │
└─────────────────────────────────────────────────────────────┘
                              │
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                    Code Generation Logic                     │
│                                                               │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐         │
│  │   genAPI()  │  │genFunctions │  │ genTuples() │         │
│  └─────────────┘  └─────────────┘  └─────────────┘         │
│                                                               │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐         │
│  │genArrayTypes│  │  genTests() │  │  genMatch() │         │
│  └─────────────┘  └─────────────┘  └─────────────┘         │
└─────────────────────────────────────────────────────────────┘
                              │
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                    Generator Framework                       │
│                                                               │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │ImportManager │  │StringContext │  │RangeExtensions│     │
│  └──────────────┘  │  Extensions  │  └──────────────┘      │
│                    └──────────────┘                          │
│                                                               │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   genFile()  │  │genJavaFile() │  │genVavrFile() │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
                              │
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                      Generated Output                        │
│                                                               │
│  vavr/src-gen/main/java/io/vavr/                            │
│    ├── Function0.java ... Function8.java                    │
│    ├── CheckedFunction0.java ... CheckedFunction8.java      │
│    ├── Tuple0.java ... Tuple8.java                          │
│    ├── API.java (with Match, Case, For comprehensions)      │
│    └── ... (and many more)                                   │
│                                                               │
│  vavr/src-gen/test/java/io/vavr/                            │
│    ├── Function0Test.java ... Function8Test.java            │
│    ├── Tuple0Test.java ... Tuple8Test.java                  │
│    └── APITest.java                                          │
└─────────────────────────────────────────────────────────────┘
```

### Execution Flow

1. **Maven Build Phase**: During the `generate-sources` phase, Maven invokes the Scala Maven Plugin
2. **Script Execution**: The plugin executes `Generator.scala` as a Scala script
3. **Code Generation**: The script calls various generator functions to produce Java files
4. **File Writing**: Generated files are written to `src-gen/main/java` and `src-gen/test/java`
5. **Compilation**: Maven then compiles the generated Java files along with hand-written sources

---

## Quick Start

### Running the Generator

```bash
# Generate all code (main and test)
mvn generate-sources

# Clean and regenerate
mvn clean generate-sources

# Full build (includes generation)
mvn clean install
```

### Location of Generated Files

- **Main sources**: `vavr/src-gen/main/java/`
- **Test sources**: `vavr/src-gen/test/java/`
- **Generator script**: `vavr/generator/Generator.scala`

### Modifying the Generator

1. Edit `vavr/generator/Generator.scala`
2. Run `mvn clean generate-sources` to regenerate
3. Check the generated files in `vavr/src-gen/`
4. Test your changes with `mvn test`

---

## Configuration

### Global Constants

The generator uses several configuration constants defined at the top of `Generator.scala`:

```scala
val N = 8                    // Maximum arity for functions, tuples, etc.
val VARARGS = 10            // Maximum arity for varargs methods (e.g., Map.of)
val TARGET_MAIN = s"${project.getBasedir()}/src-gen/main/java"
val TARGET_TEST = s"${project.getBasedir()}/src-gen/test/java"
val CHARSET = java.nio.charset.StandardCharsets.UTF_8
val comment = "//"          // Line comment marker
val javadoc = "**"          // Javadoc marker (used as /**)
```

### Key Configuration Values

- **`N = 8`**: Defines the maximum arity for most generated constructs
  - Functions: `Function0` through `Function8` (9 total)
  - Tuples: `Tuple0` through `Tuple8` (9 total)
  - Pattern matching: `Pattern0` through `Pattern8`
  
- **`VARARGS = 10`**: Some methods (like `Map.of()`) support higher arities via varargs
  - Example: `Map.of(k1, v1, k2, v2, ..., k10, v10)`

- **`TARGET_MAIN` and `TARGET_TEST`**: Output directories for generated code
  - Uses Maven's `project.getBasedir()` for portability

### Arity Design Rationale

The choice of `N = 8` balances:
- **Practical utility**: Most real-world use cases need ≤8 parameters
- **Java limitations**: Very high arities become unwieldy
- **Code size**: Each arity multiplies the codebase size
- **Community convention**: Matches Scala's standard library (Function0-Function22, but Vavr is more conservative)

---

## Core Components

### Entry Point: `run()`

The `run()` function is the main entry point called by Maven:

```scala
def run(): Unit = {
  generateMainClasses()
  generateTestClasses()
}
```

### Main Class Generation: `generateMainClasses()`

This function orchestrates the generation of all main source files:

```scala
def generateMainClasses(): Unit = {
  genAPI()          // Generate API.java with Match, Case, For comprehensions
  genFunctions()    // Generate Function0-Function8 and CheckedFunction0-CheckedFunction8
  genTuples()       // Generate Tuple0-Tuple8
  genArrayTypes()   // Generate specialized array collection types
}
```

### Test Generation: `generateTestClasses()`

Generates comprehensive test suites for generated code:

```scala
def generateTestClasses(): Unit = {
  genAPITest()              // Tests for API.java (Match, For comprehensions)
  genFunctionTests()        // Tests for all Function/CheckedFunction variants
  genTupleTests()           // Tests for all Tuple variants
  genMapOfEntriesTests()    // Tests for Map construction
}
```

### Individual Generators

Each generator function follows a pattern:

1. **Outer function**: Orchestrates file generation
2. **Inner function**: Defines the actual Java code template
3. **Template function**: Uses string interpolation to generate code
4. **File writing**: Uses `genVavrFile()` or `genJavaFile()` to write to disk

---

## Generated Code Categories

### 1. Functions (`genFunctions()`)

Generates two families of function interfaces:

#### Function0 through Function8

Standard function interfaces that don't throw checked exceptions:

```java
@FunctionalInterface
public interface Function3<T1, T2, T3, R> {
    R apply(T1 t1, T2 t2, T3 t3);
    // ... methods like curried(), tupled(), memoized(), compose, andThen
}
```

Features:
- **Currying**: `Function3<A,B,C,R>` → `Function1<A, Function1<B, Function1<C,R>>>`
- **Tupling**: `Function3<A,B,C,R>` → `Function1<Tuple3<A,B,C>, R>`
- **Memoization**: Caches results for pure functions
- **Composition**: `compose()` and `andThen()`
- **Partial application**: For arities > 1

#### CheckedFunction0 through CheckedFunction8

Function interfaces that can throw checked exceptions:

```java
@FunctionalInterface
public interface CheckedFunction3<T1, T2, T3, R> {
    R apply(T1 t1, T2 t2, T3 t3) throws Throwable;
    // ... includes unchecked(), recover(), liftTry()
}
```

Additional features:
- **`unchecked()`**: Converts to regular Function (throws unchecked)
- **`recover()`**: Handles exceptions and provides fallback
- **`liftTry()`**: Returns `Try<R>` instead of throwing

### 2. Tuples (`genTuples()`)

Generates immutable tuple classes for 0 to 8 elements:

```java
public final class Tuple3<T1, T2, T3> implements Tuple, Comparable<Tuple3<T1, T2, T3>> {
    public final T1 _1;
    public final T2 _2;
    public final T3 _3;
    
    // Factory methods
    public static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 t1, T2 t2, T3 t3);
    
    // Transformations
    public <U1> Tuple3<U1, T2, T3> map1(Function<? super T1, ? extends U1> mapper);
    public <U> Tuple3<U, U, U> map(Function3<? super T1, ? super T2, ? super T3, ? extends U> mapper);
    
    // Utility methods
    public Tuple3<T1, T2, T3> update1(T1 value);
    public <T4> Tuple4<T1, T2, T3, T4> append(T4 value);
    public <T4, T5> Tuple5<T1, T2, T3, T4, T5> concat(Tuple2<T4, T5> tuple);
    
    // ... equals, hashCode, toString, compareTo
}
```

Special cases:
- **Tuple0**: Singleton pattern (no fields)
- **Tuple2**: Additional `swap()` method and `toEntry()` for Map compatibility

### 3. API (`genAPI()`)

The `API.java` file is the most complex generated file, containing:

#### Pattern Matching (`genMatch()`)

```java
public static <T> Match<T> Match(T value) { ... }

// Case patterns for different arities
public static <T, R> Case<T, R> Case(Pattern0<T> pattern, Function<? super T, ? extends R> f);
public static <T, T1, R> Case<T, R> Case(Pattern1<T, T1> pattern, Function<? super T1, ? extends R> f);
// ... up to Pattern8
```

#### For-Comprehensions (`genFor()`)

For-comprehensions for Iterable and monadic types (Option, Try, Future, Either, Validation):

```java
// Eager for-comprehensions
public static <T1, T2> For2<T1, T2> For(Iterable<T1> ts1, Iterable<T2> ts2);

// Lazy for-comprehensions
public static <T1, T2> ForLazy2Option<T1, T2> For(Option<T1> ts1, 
    Function<? super T1, Option<T2>> ts2);
```

#### Type Aliases

Convenient factory methods for common types:

```java
// Options
public static <T> Option<T> Option(T value);
public static <T> Option.Some<T> Some(T value);
public static <T> Option.None<T> None();

// Try
public static <T> Try<T> Try(CheckedFunction0<? extends T> supplier);
public static <T> Try.Success<T> Success(T value);
public static <T> Try.Failure<T> Failure(Throwable exception);

// Collections
public static <T> List<T> List(T... elements);
public static <K, V> Map<K, V> Map(K k1, V v1, K k2, V v2, ...);
// ... and many more
```

### 4. Array Types (`genArrayTypes()`)

Generates specialized array collection types with optimized implementations.

---

## Generator Framework

The generator framework consists of three layers:

### Layer 1: Core Generator (`Generator` object)

Located at the bottom of `Generator.scala`, this object provides:

#### File Generation

```scala
def genFile(baseDir: String, dirName: String, fileName: String,
           createOption: StandardOpenOption = StandardOpenOption.CREATE_NEW)
          (contents: => String)
          (implicit charset: Charset = StandardCharsets.UTF_8): Unit
```

Creates directories as needed and writes string content to a file.

#### Extension Methods

Provides implicit classes to enhance Scala types:

```scala
// Integers
(1 to 3).gen(i => s"x$i")(using ", ")  // "x1, x2, x3"
3.ordinal                               // "3rd"
2.numerus("name")                       // "two names"

// Booleans
true.gen("yes")                         // "yes"
false.gen("yes")                        // ""

// Ranges
(1 to 3).gen(i => s"T$i")(using ", ")  // "T1, T2, T3"
```

### Layer 2: Java Generator (`JavaGenerator` object)

Adds Java-specific functionality:

#### Java File Generation

```scala
def genJavaFile(baseDir: String, packageName: String, className: String)
               (classHeader: String)
               (gen: (ImportManager, String, String) => String,
                knownSimpleClassNames: List[String] = List())
               (implicit charset: Charset = StandardCharsets.UTF_8): Unit
```

Features:
- Generates package declaration
- Manages imports via `ImportManager`
- Adds standard header comments
- Marks files as "GENERATOR CRAFTED"

#### Import Management (`ImportManager`)

Intelligently manages Java imports:

```scala
class ImportManager(packageNameOfClass: String, 
                   knownSimpleClassNames: List[String],
                   wildcardThreshold: Int = 5)
```

Features:
- **Automatic short names**: `im.getType("java.util.List")` → `"List"`
- **Conflict resolution**: Uses fully qualified names when conflicts occur
- **Wildcard optimization**: Converts multiple imports to `package.*` when threshold is exceeded
- **Static imports**: Via `im.getStatic("fully.qualified.Name.*")`

Example usage:

```scala
def genAPI(im: ImportManager, packageName: String, className: String): String = {
  val ListType = im.getType("io.vavr.collection.List")
  val assertThat = im.getStatic("org.assertj.core.api.Assertions.assertThat")
  
  xs"""
    public static <T> $ListType<T> emptyList() {
        $assertThat(true).isTrue();
        return $ListType.empty();
    }
  """
}
```

### Layer 3: Vavr Generator

#### Vavr File Generation

```scala
def genVavrFile(packageName: String, className: String, 
               baseDir: String = TARGET_MAIN)
              (gen: (ImportManager, String, String) => String,
               knownSimpleClassNames: List[String] = List())
```

Wraps `genJavaFile()` with Vavr-specific header and license.

---

## String Interpolation System

The generator uses Scala's string interpolation extensively, with custom enhancements.

### Standard Scala Interpolation

```scala
val name = "World"
s"Hello, $name!"           // "Hello, World!"
s"1 + 1 = ${1 + 1}"       // "1 + 1 = 2"
```

### Custom `xs` Interpolator

The `xs` (for "extended string") interpolator provides:

1. **Automatic indentation alignment**
2. **Multi-line string support**
3. **Embedded expression indentation**

Example:

```scala
val code = xs"""
  public class Example {
      ${(1 to 3).gen(i => s"private int field$i;")(using "\n")}
      
      public void method() {
          ${someComplexExpression}
      }
  }
"""
```

Output:
```java
public class Example {
    private int field1;
    private int field2;
    private int field3;
    
    public void method() {
        // complex expression properly indented
    }
}
```

### Custom `xraw` Interpolator

Similar to `xs`, but doesn't escape special characters (like `\n`, `\t`).

### Template Patterns

Common patterns used in templates:

#### Conditional Generation

```scala
${(arity > 0).gen(s"// Only for non-zero arity")}
${(arity == 2).gen(xs"""
  public Tuple2<T2, T1> swap() {
      return Tuple.of(_2, _1);
  }
""")}
```

#### Range-Based Generation

```scala
${(1 to arity).gen(i => s"private final T$i _$i;")(using "\n")}
// Generates:
// private final T1 _1;
// private final T2 _2;
// private final T3 _3;
```

#### List-Based Generation

```scala
${List("Option", "Try", "Future").gen(type => xs"""
  public static <T> $type<T> empty$type() {
      return $type.empty();
  }
""")(using "\n\n")}
```

---

## Import Management

The `ImportManager` class is crucial for generating clean, compilable Java code.

### How It Works

1. **Type Registration**: When you call `im.getType("fully.qualified.Name")`, it returns a short name
2. **Conflict Detection**: If the short name is already used, it returns the fully qualified name
3. **Import Collection**: Builds a list of needed imports
4. **Optimization**: Groups related imports and uses wildcards when beneficial

### Example

```scala
def genExample(im: ImportManager, pkg: String, cls: String): String = {
  // Register types - returns short names
  val JavaList = im.getType("java.util.List")           // "List"
  val VavrList = im.getType("io.vavr.collection.List")  // "io.vavr.collection.List" (conflict!)
  val Option = im.getType("io.vavr.control.Option")     // "Option"
  
  xs"""
    public class Example {
        private $JavaList<String> javaList;
        private $VavrList<String> vavrList;
        private $Option<String> option;
    }
  """
}
```

Generated imports:
```java
import io.vavr.control.Option;
import java.util.List;

public class Example {
    private List<String> javaList;
    private io.vavr.collection.List<String> vavrList;
    private Option<String> option;
}
```

### Wildcard Imports

When more than 5 types from the same package are used:

```scala
val Type1 = im.getType("com.example.Type1")
val Type2 = im.getType("com.example.Type2")
// ... (6 types total)
val Type6 = im.getType("com.example.Type6")
```

Generated imports:
```java
import com.example.*;
```

### Static Imports

```scala
val assertThat = im.getStatic("org.assertj.core.api.Assertions.assertThat")
im.getStatic("io.vavr.API.Match.*")  // Import all static members
```

Generated imports:
```java
import static org.assertj.core.api.Assertions.assertThat;
import static io.vavr.API.Match.*;
```

---

## Extension System

The generator extensively uses Scala's implicit classes to add domain-specific methods to built-in types.

### Integer Extensions

```scala
implicit class IntExtensions(i: Int) {
  def ordinal: String        // 1 → "1st", 2 → "2nd", 3 → "3rd", 4 → "4th"
  def numerus(noun: String): String  // 2.numerus("name") → "two names"
  def plural(noun: String): String   // 2.plural("name") → "names"
}
```

Usage in templates:
```scala
xs"""
  /**
   * Creates a tuple of ${arity.numerus("element")}.
   * @param t1 the ${1.ordinal} element
   */
"""
// For arity=3: "Creates a tuple of three elements."
```

### Range Extensions

```scala
implicit class RangeExtensions(range: Range) {
  def gen(f: Int => String = String.valueOf)
         (implicit delimiter: String = ""): String
}
```

Usage:
```scala
(1 to 3).gen(i => s"T$i")(using ", ")              // "T1, T2, T3"
(1 to 3).gen(i => s"private int x$i;")(using "\n") // Multiple lines
```

### Boolean Extensions

```scala
implicit class BooleanExtensions(condition: Boolean) {
  def gen(s: => String): String = if (condition) s else ""
}
```

Usage:
```scala
${(arity > 0).gen("// Has parameters")}
${(arity == 0).gen(xs"""
  public R get() {
      return apply();
  }
""")}
```

### Tuple Extensions

Scala tuples can be used for grouped generation:

```scala
implicit class Tuple3Extensions(tuple: (Any, Any, Any)) {
  def gen(f: String => String = identity)
         (implicit delimiter: String = ""): String
}
```

Usage:
```scala
("Int", "String", "Boolean").gen(t => s"public $t field;")(using "\n")
// Generates three field declarations
```

---

## How to Run

### Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Scala is downloaded automatically by Maven

### Running the Generator

#### Generate All Code

```bash
cd vavr
mvn generate-sources
```

This will:
1. Execute the Scala script
2. Generate files in `src-gen/main/java/` and `src-gen/test/java/`
3. Not compile or test (just generation)

#### Clean and Regenerate

```bash
mvn clean generate-sources
```

Deletes all generated files and regenerates them.

#### Full Build

```bash
mvn clean install
```

Generates, compiles, tests, and packages the library.

#### Watch Mode (Development)

For rapid iteration during development:

```bash
# In one terminal, watch for changes
watch -n 2 'mvn generate-sources'

# In another terminal, make changes to Generator.scala
```

### Maven Integration

The generator is integrated via the `scala-maven-plugin`:

```xml
<plugin>
    <groupId>net.alchim31.maven</groupId>
    <artifactId>scala-maven-plugin</artifactId>
    <executions>
        <execution>
            <phase>generate-sources</phase>
            <goals>
                <goal>script</goal>
            </goals>
            <configuration>
                <scriptFile>${project.basedir}/generator/Generator.scala</scriptFile>
            </configuration>
        </execution>
    </executions>
</plugin>
```

### Troubleshooting

#### "File already exists" Error

If you get errors about files already existing:

```bash
mvn clean  # Removes src-gen/ directory
mvn generate-sources
```

#### Scala Version Issues

The generator requires Scala 2.13.x. If you encounter Scala version issues, check:

```bash
mvn dependency:tree | grep scala
```

#### Generator Script Errors

If the Scala script fails to compile:

1. Check the error message in Maven output
2. Fix the syntax error in `Generator.scala`
3. Run `mvn clean generate-sources` again

---

## How to Extend

### Adding a New Generator

#### 1. Define the Generator Function

```scala
def genMyNewType(): Unit = {
  (1 to N).foreach(arity => {
    genVavrFile("io.vavr", s"MyType$arity")(genMyTypeImpl)
  })
  
  def genMyTypeImpl(im: ImportManager, packageName: String, className: String): String = {
    val arity = className.stripPrefix("MyType").toInt
    
    xs"""
      public class $className<${(1 to arity).gen(i => s"T$i")(using ", ")}> {
          ${(1 to arity).gen(i => s"private final T$i field$i;")(using "\n")}
          
          public $className(${(1 to arity).gen(i => s"T$i field$i")(using ", ")}) {
              ${(1 to arity).gen(i => s"this.field$i = field$i;")(using "\n")}
          }
      }
    """
  }
}
```

#### 2. Register in `generateMainClasses()`

```scala
def generateMainClasses(): Unit = {
  genAPI()
  genFunctions()
  genTuples()
  genArrayTypes()
  genMyNewType()  // Add your generator here
}
```

#### 3. Generate and Test

```bash
mvn clean generate-sources
# Check output in vavr/src-gen/main/java/io/vavr/MyType*.java
```

### Adding a New Method to Existing Type

To add a new method to all Function variants:

#### 1. Locate the Generator

Find `genFunctions()` in `Generator.scala`.

#### 2. Add Method Template

Inside the `genFunction()` inner function, add:

```scala
xs"""
  /**
   * My new method that does something useful.
   */
  public ${returnType} myNewMethod(${parameters}) {
      ${implementation}
  }
"""
```

#### 3. Use Conditional Generation

If the method only applies to certain arities:

```scala
${(arity > 0).gen(xs"""
  public void onlyForNonZeroArity() {
      // implementation
  }
""")}
```

### Changing Arity Limits

To support more than 8 parameters:

#### 1. Update Constants

```scala
val N = 12  // Was 8, now supports up to 12
```

#### 2. Regenerate

```bash
mvn clean generate-sources
```

#### 3. Check Output Size

Be aware that increasing `N`:
- Multiplies the number of generated files
- Increases codebase size significantly
- May impact compilation time

### Adding New Type Aliases

To add new factory methods to `API.java`:

#### 1. Locate `genAliases()`

Inside `genAPI()`, find the `genAliases()` function.

#### 2. Add Alias Template

```scala
xs"""
  /**
   * Alias for {@link MyType#create(Object)}.
   */
  public static <T> MyType<T> MyTypeAlias(T value) {
      return MyType.create(value);
  }
"""
```

---

## Best Practices

### Template Design

#### 1. Use Descriptive Variable Names

```scala
// Good
val generics = (1 to arity).gen(i => s"T$i")(using ", ")
val params = (1 to arity).gen(i => s"T$i t$i")(using ", ")

// Avoid
val g = (1 to arity).gen(i => s"T$i")(using ", ")
val p = (1 to arity).gen(i => s"T$i t$i")(using ", ")
```

#### 2. Factor Out Common Patterns

```scala
// Define once
def genTypeParameters(count: Int) = (1 to count).gen(i => s"T$i")(using ", ")

// Use many times
val generics = genTypeParameters(arity)
```

#### 3. Document Complex Logic

```scala
// Complex nested generation
${(1 to arity).gen(i => {
  // For each type parameter, generate a getter method
  // that returns the value at position i
  xs"""
    public T$i _$i() {
        return this._$i;
    }
  """
})(using "\n\n")}
```

### Testing Generated Code

#### 1. Always Generate Tests

For every generated main class, generate corresponding tests:

```scala
def generateTestClasses(): Unit = {
  genMyNewTypeTests()  // Don't forget this!
}
```

#### 2. Test Edge Cases

Ensure your generator handles:
- Zero arity (`arity == 0`)
- Single arity (`arity == 1`)
- Maximum arity (`arity == N`)

#### 3. Verify Compilation

```bash
mvn clean install
```

Make sure all generated code compiles without warnings.

### Performance Considerations

#### 1. Lazy Evaluation

Use call-by-name parameters for expensive computations:

```scala
def genExpensive(contents: => String): Unit = {
  // contents is only evaluated if needed
}
```

#### 2. Avoid Redundant Generation

Cache results when generating the same code multiple times:

```scala
lazy val commonHeader = xs"""
  // This is only generated once
"""
```

### Maintainability

#### 1. Keep Generators Focused

Each generator function should have a single responsibility:
- `genFunctions()` → Function interfaces only
- `genTuples()` → Tuple classes only
- Don't mix concerns

#### 2. Use Helper Functions

Extract complex logic into helper functions:

```scala
def genFieldDeclarations(arity: Int, typePrefix: String = "T") = {
  (1 to arity).gen(i => s"private final $typePrefix$i field$i;")(using "\n")
}
```

#### 3. Comment Non-Obvious Code

```scala
// We reverse the order for composition to match mathematical notation:
// (f compose g)(x) = f(g(x))
val composedFunction = ...
```

### Version Control

#### 1. Commit Generator Changes Separately

```bash
# Commit 1: Generator changes
git add vavr/generator/Generator.scala
git commit -m "Add new method to Function generator"

# Commit 2: Regenerated code
mvn clean generate-sources
git add vavr/src-gen/
git commit -m "Regenerate code with new Function method"
```

#### 2. Review Generated Diffs

Before committing, review the generated changes:

```bash
git diff vavr/src-gen/
```

Look for unexpected changes that might indicate a bug.

---

## Summary

The Vavr code generator is a powerful metaprogramming system that:

1. **Generates** hundreds of Java files from concise Scala templates
2. **Ensures** consistency across all generated code
3. **Reduces** human error and maintenance burden
4. **Enables** rapid iteration and experimentation
5. **Documents** itself through comprehensive Javadoc

Key takeaways:

- The generator uses Scala's advanced features (implicits, string interpolation) to create a DSL for code generation
- All generated code follows consistent patterns defined once in templates
- The `ImportManager` automatically handles Java imports to keep code clean
- Extension methods (`gen`, `ordinal`, `numerus`) make templates readable
- Adding new generators or extending existing ones is straightforward

For questions or contributions, see [CONTRIBUTING.md](./CONTRIBUTING.md).

---

*This documentation is maintained alongside the generator. Last updated: 2025.*
