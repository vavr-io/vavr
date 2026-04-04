/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2026 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.gen

import Generator._
import JavaGenerator._
import Config._
import collection.immutable.ListMap

def generateMainClasses(targetMain: String): Unit = {

  genAPI()
  genFunctions()
  genTuples()
  genArrayTypes()

  /**
   * Generator of Match
   */
  def genAPI(): Unit = {

    genVavrFile("io.vavr", "API", targetMain)(genAPI)

    def genAPI(im: ImportManager, packageName: String, className: String): String = {

      xs"""
        /**
         * The most basic Vavr functionality is accessed through this API class.
         *
         * <pre>{@code 
         * import static io.vavr.API.*;
         * }</pre>
         *
         * <h2>For-comprehension</h2>
         * <p>
         * The {@code For}-comprehension is syntactic sugar for nested for-loops. We write
         *
         * <pre>{@code 
         * // lazily evaluated
         * Iterator<R> result = For(iterable1, iterable2, ..., iterableN).yield(f);
         * }</pre>
         *
         * or
         *
         * <pre>{@code 
         * Iterator<R> result =
         *     For(iterable1, v1 ->
         *         For(iterable2, v2 ->
         *             ...
         *             For(iterableN).yield(vN -> f.apply(v1, v2, ..., vN))
         *         )
         *     );
         * }</pre>
         *
         * instead of
         *
         * <pre>{@code 
         * for(T1 v1 : iterable1) {
         *     for (T2 v2 : iterable2) {
         *         ...
         *         for (TN vN : iterableN) {
         *             R result = f.apply(v1, v2, ..., VN);
         *             //
         *             // We are forced to perform side effects to do s.th. meaningful with the result.
         *             //
         *         }
         *     }
         * }
         * }</pre>
         *
         * Please note that values like Option, Try, Future, etc. are also iterable.
         * <p>
         * Given a suitable function
         * f: {@code (v1, v2, ..., vN) -> ...} and {@code 1 <= N <= 8} iterables, the result is a Stream of the
         * mapped cross product elements.
         *
         * <pre>{@code 
         * { f(v1, v2, ..., vN) | v1 ∈ iterable1, ... vN ∈ iterableN }
         * }</pre>
         *
         * As with all Vavr Values, the result of a For-comprehension can be converted
         * to standard Java library and Vavr types.
         * @author Daniel Dietrich, Grzegorz Piwowarek
         */
        public final class API {

            private API() {
            }

            ${genAPIShortcuts(im)}

            ${genAPIAliases(im)}

            ${genAPIJavaTypeTweaks(im)}

            ${genAPIForComprehensions(im, isLazy = false)}

            ${genAPIForComprehensions(im, isLazy = true)}

            ${genAPIMatch(im)}
        }
      """
    }
  }

  /**
   * Generator of Functions
   */
  def genFunctions(): Unit = {

    (0 to N).foreach(i => {

      genVavrFile("io.vavr", s"CheckedFunction$i", targetMain)(genFunction("CheckedFunction", checked = true))
      genVavrFile("io.vavr", s"Function$i", targetMain)(genFunction("Function", checked = false))

      def genFunction(name: String, checked: Boolean)(im: ImportManager, packageName: String, className: String): String = {

        val a = Arity(i)
        import a.{generics, fullGenerics, wideGenerics, fullWideGenerics, genericsReversed, genericsTuple, genericsFunction, genericsReversedFunction, paramsDecl, params, paramsReversed, tupled}
        val genericsOptionReturnType = s"<${genericsFunction}${im.getType("io.vavr.control.Option")}<R>>"
        val genericsTryReturnType = s"<${genericsFunction}${im.getType("io.vavr.control.Try")}<R>>"
        val curried = if (i == 0) "v" else (1 to i).gen(j => s"t$j")(using " -> ")
        val compositionType = if(checked) "CheckedFunction1" else im.getType("java.util.function.Function")

        // imports

        val NonNullType = im.getType("org.jspecify.annotations.NonNull")
        val Objects = im.getType("java.util.Objects")
        val Try = if (checked) im.getType("io.vavr.control.Try") else ""
        val Serializable = im.getType("java.io.Serializable")
        val additionalExtends = (checked, i) match {
          case (false, 0) => ", " + im.getType("java.util.function.Supplier") + "<R>"
          case (false, 1) => ", " + im.getType("java.util.function.Function") + "<T1, R>"
          case (false, 2) => ", " + im.getType("java.util.function.BiFunction") + "<T1, T2, R>"
          case _ => ""
        }
        def fullGenericsTypeF(checked: Boolean, i: Int): String = (checked, i) match {
          case (true, _) => im.getType(s"io.vavr.CheckedFunction$i") + fullWideGenerics
          case (false, 0) => im.getType("java.util.function.Supplier") + "<? extends R>"
          case (false, 1) => im.getType("java.util.function.Function") + "<? super T1, ? extends R>"
          case (false, 2) => im.getType("java.util.function.BiFunction") + "<? super T1, ? super T2, ? extends R>"
          case (false, _) => im.getType(s"io.vavr.Function$i") + fullWideGenerics
        }
        val fullGenericsType = fullGenericsTypeF(checked, i)
        val refApply = i match {
          case 0 => "get"
          case _ => "apply"
        }
        val callApply = s"$refApply($params)"

        def curriedType(max: Int, function: String, idx: Int = 1): String = max match {
          case 0 => s"$className<R>"
          case 1 => s"${function}1<T$idx, R>"
          case _ => s"Function1<T$idx, ${curriedType(max - 1, function, idx + 1)}>"
        }

        def arguments(count: Int): String = count match {
          case 0 => "no arguments"
          case 1 => "one argument"
          case 2 => "two arguments"
          case 3 => "three arguments"
          case _ => s"$i arguments"
        }

        if (checked) {
          im.getStatic(s"io.vavr.${className}Module.sneakyThrow")
        }

        xs"""
          /**
           * Represents a function with ${arguments(i)}.
           ${(0 to i).gen(j => if (j == 0) "*" else s"* @param <T$j> argument $j of the function")(using "\n")}
           * @param <R> return type of the function
           * @author Daniel Dietrich
           */
          @FunctionalInterface
          public interface $className$fullGenerics extends $Serializable$additionalExtends {

              /$javadoc
               * The serial version UID for serialization.
               */
              long serialVersionUID = 1L;

              /$javadoc
               * Returns a function that always returns the constant
               * value that you give in parameter.
               *
               ${(1 to i).gen(j => s"* @param <T$j> generic parameter type $j of the resulting function")(using "\n")}
               * @param <R> the result type
               * @param value the value to be returned
               * @return a function always returning the given value
               */
              static $fullGenerics $className$fullGenerics constant(R value) {
                  return ($params) -> value;
              }

              /$javadoc
               * Creates a {@code $className} based on
               * <ul>
               * <li><a href="https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html">method reference</a></li>
               * <li><a href="https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html#syntax">lambda expression</a></li>
               * </ul>
               *
               * Examples (w.l.o.g. referring to Function1):
               * <pre>{@code // using a lambda expression
               * Function1<Integer, Integer> add1 = Function1.of(i -> i + 1);
               *
               * // using a method reference (, e.g. Integer method(Integer i) { return i + 1; })
               * Function1<Integer, Integer> add2 = Function1.of(this::method);
               *
               * // using a lambda reference
               * Function1<Integer, Integer> add3 = Function1.of(add1::apply);
               * }</pre>
               * <p>
               * <strong>Caution:</strong> Reflection loses type information of lambda references.
               * <pre>{@code // type of a lambda expression
               * Type<?, ?> type1 = add1.getType(); // (Integer) -> Integer
               *
               * // type of a method reference
               * Type<?, ?> type2 = add2.getType(); // (Integer) -> Integer
               *
               * // type of a lambda reference
               * Type<?, ?> type3 = add3.getType(); // (Object) -> Object
               * }</pre>
               *
               * @param methodReference (typically) a method reference, e.g. {@code Type::method}
               ${(0 to i).gen(j => if (j == 0) "* @param <R> return type" else s"* @param <T$j> ${j.ordinal} argument")(using "\n")}
               * @return a {@code $className}
               */
              static $fullGenerics $className$fullGenerics of(@NonNull $className$fullGenerics methodReference) {
                  return methodReference;
              }

              /$javadoc
               * Lifts the given {@code partialFunction} into a total function that returns an {@code Option} result.
               *
               * @param partialFunction a function that is not defined for all values of the domain (e.g. by throwing)
               ${(0 to i).gen(j => if (j == 0) "* @param <R> return type" else s"* @param <T$j> ${j.ordinal} argument")(using "\n")}
               * @return a function that applies arguments to the given {@code partialFunction} and returns {@code Some(result)}
               *         if the function is defined for the given arguments, and {@code None} otherwise.
               */
              static $fullGenerics ${im.getType(s"io.vavr.Function$i")}$genericsOptionReturnType lift(@NonNull $fullGenericsType partialFunction) {
                  ${
                    val func = "partialFunction"
                    val supplier = if (!checked && i == 0) s"$func::get" else if (checked && i == 0) s"$func::apply" else s"() -> $func.apply($params)"
                    val lambdaArgs = if (i == 1) params else s"($params)"
                    xs"""
                      return $lambdaArgs -> ${im.getType("io.vavr.control.Try")}.<R>of($supplier).toOption();
                    """
                  }
              }

              /$javadoc
               * Lifts the given {@code partialFunction} into a total function that returns an {@code Try} result.
               *
               * @param partialFunction a function that is not defined for all values of the domain (e.g. by throwing)
               ${(0 to i).gen(j => if (j == 0) "* @param <R> return type" else s"* @param <T$j> ${j.ordinal} argument")(using "\n")}
               * @return a function that applies arguments to the given {@code partialFunction} and returns {@code Success(result)}
               *         if the function is defined for the given arguments, and {@code Failure(throwable)} otherwise.
               */
              static $fullGenerics ${im.getType(s"io.vavr.Function$i")}$genericsTryReturnType liftTry(@NonNull $fullGenericsType partialFunction) {
                  ${
                    val supplier = if (!checked && i == 0) "partialFunction::get" else if (checked && i == 0) "partialFunction::apply" else s"() -> partialFunction.apply($params)"
                    val lambdaArgs = if (i == 1) params else s"($params)"
                    xs"""
                      return $lambdaArgs -> ${im.getType("io.vavr.control.Try")}.of($supplier);
                    """
                  }
              }

              /$javadoc
               * Narrows the given {@code $className$fullWideGenerics} to {@code $className$fullGenerics}
               *
               * @param f A {@code $className}
               ${(0 to i).gen(j => if (j == 0) "* @param <R> return type" else s"* @param <T$j> ${j.ordinal} argument")(using "\n")}
               * @return the given {@code f} instance as narrowed type {@code $className$fullGenerics}
               */
              @SuppressWarnings("unchecked")
              static $fullGenerics $className$fullGenerics narrow($className$fullWideGenerics f) {
                  return ($className$fullGenerics) f;
              }

              ${(i == 1).gen(xs"""
                /$javadoc
                 * Returns the identity $className, i.e. the function that returns its input.
                 *
                 * @param <T> argument type (and return type) of the identity function
                 * @return the identity $className
                 */
                static <T> ${name}1<T, T> identity() {
                    return t -> t;
                }
              """)}

              /$javadoc
               * Applies this function to ${arguments(i)} and returns the result.
               ${(0 to i).gen(j => if (j == 0) "*" else s"* @param t$j argument $j")(using "\n")}
               * @return the result of function application
               * ${checked.gen("@throws Throwable if something goes wrong applying this function to the given arguments")}
               */
              R apply($paramsDecl)${checked.gen(" throws Throwable")};

              ${(1 until i).gen(j => {
                val partialApplicationArgs = (1 to j).gen(k => s"T$k t$k")(using ", ")
                val resultFunctionGenerics = (j+1 to i).gen(k => s"T$k")(using ", ")
                val resultFunctionArgs = (j+1 to i).gen(k => s"T$k t$k")(using ", ")
                val fixedApplyArgs = (1 to j).gen(k => s"t$k")(using ", ")
                val variableApplyArgs = (j+1 to i).gen(k => s"t$k")(using ", ")
                xs"""
                  /$javadoc
                   * Applies this function partially to ${j.numerus("argument")}.
                   *
                   ${(1 to j).gen(k => s"* @param t$k argument $k")(using "\n")}
                   * @return a partial application of this function
                   */
                  default $name${i - j}<$resultFunctionGenerics, R> apply($partialApplicationArgs) {
                      return ($resultFunctionArgs) -> apply($fixedApplyArgs, $variableApplyArgs);
                  }
                """
              })(using "\n\n")}

              ${(i == 0 && !checked).gen(
                xs"""
                  /$javadoc
                   * Implementation of {@linkplain java.util.function.Supplier#get()}, just calls {@linkplain #apply()}.
                   *
                   * @return the result of {@code apply()}
                   */
                  @Override
                  default R get() {
                      return apply();
                  }
                """
              )}

              /**
               * Returns the number of function arguments.
               * @return an int value &gt;= 0
               * @see <a href="http://en.wikipedia.org/wiki/Arity">Arity</a>
               */
              default int arity() {
                  return $i;
              }

              /**
               * Returns a curried version of this function.
               *
               * @return a curried function equivalent to this.
               */
              default ${curriedType(i, name)} curried() {
                  return ${if (i < 2) "this" else s"$curried -> apply($params)"};
              }

              /**
               * Returns a tupled version of this function.
               *
               * @return a tupled function equivalent to this.
               */
              default ${name}1<Tuple$i$genericsTuple, R> tupled() {
                  return t -> apply($tupled);
              }

              /**
               * Returns a reversed version of this function. This may be useful in a recursive context.
               *
               * @return a reversed function equivalent to this.
               */
              default $className<${genericsReversedFunction}R> reversed() {
                  return ${if (i < 2) "this" else s"($paramsReversed) -> apply($params)"};
              }

              /**
               * Returns a memoizing version of this function, which computes the return value for given arguments only one time.
               * On subsequent calls given the same arguments the memoized value is returned.
               * <p>
               * Please note that memoizing functions do not permit {@code null} as single argument or return value.
               *
               * @return a memoizing function equivalent to this.
               */
              default $className$fullGenerics memoized() {
                  if (isMemoized()) {
                      return this;
                  } else {
                      ${if (i == 0) xs"""
	                        ${if (checked) xs"""
	                            final Lazy<R> lazy = Lazy.of(() -> {
	                                try {
	                                    return apply();
	                                } catch (Throwable x) {
                                        throw new RuntimeException(x);
	                                }
	                            });
	                            return (CheckedFunction0<R> & Memoized) () -> {
	                                try {
	                                    return lazy.get();
	                                } catch(RuntimeException x) {
	                                    throw x.getCause();
	                                }
	                            };
	                        """ else xs"""
                              return ($className$fullGenerics & Memoized) Lazy.of(this)::get;
	                        """}
                      """ else if (i == 1) xs"""
                        final ${im.getType("java.util.Map")}<$generics, R> cache = new ${im.getType("java.util.HashMap")}<>();
                        final ${im.getType("java.util.concurrent.locks.ReentrantLock")} lock = new ${im.getType("java.util.concurrent.locks.ReentrantLock")}();
                        return ($className$fullGenerics & Memoized) ($params) -> {
                            lock.lock();
                            try {
                                if (cache.containsKey($params)) {
                                    return cache.get($params);
                                } else {
                                    final R value = apply($params);
                                    cache.put($params, value);
                                    return value;
                                }
                            } finally {
                                lock.unlock();
                            }
                        };
                      """ else xs"""
                        final ${im.getType("java.util.Map")}<Tuple$i<$generics>, R> cache = new ${im.getType("java.util.HashMap")}<>();
                        final ${im.getType("java.util.concurrent.locks.ReentrantLock")} lock = new ${im.getType("java.util.concurrent.locks.ReentrantLock")}();
                        return ($className$fullGenerics & Memoized) ($params) -> {
                            final Tuple$i$genericsTuple key = Tuple.of($params);
                            lock.lock();
                            try {
                                if (cache.containsKey(key)) {
                                    return cache.get(key);
                                } else {
                                    final R value = tupled().apply(key);
                                    cache.put(key, value);
                                    return value;
                                }
                            } finally {
                                lock.unlock();
                            }
                        };
                      """}
                  }
              }

              /**
               * Checks if this function is memoizing (= caching) computed values.
               *
               * @return true, if this function is memoizing, false otherwise
               */
              default boolean isMemoized() {
                  return this instanceof Memoized;
              }

              ${(i == 1 && !checked).gen(xs"""
                /$javadoc
                 * Converts this {@code Function1} to a {@link PartialFunction} by adding an {@code isDefinedAt} condition.
                 *
                 * @param isDefinedAt a predicate that states if an element is in the domain of the returned {@code PartialFunction}.
                 * @return a new {@code PartialFunction} that has the same behavior like this function but is defined only for those elements that make it through the given {@code Predicate}
                 * @throws NullPointerException if {@code isDefinedAt} is null
                 */
                default PartialFunction<T1, R> partial(@NonNull ${im.getType("java.util.function.Predicate")}<? super T1> isDefinedAt) {
                    Objects.requireNonNull(isDefinedAt, "isDefinedAt is null");
                    final Function1<T1, R> self = this;
                    return new PartialFunction<T1, R>() {

                        private static final long serialVersionUID = 1L;

                        @Override
                        public boolean isDefinedAt(T1 t1) {
                            return isDefinedAt.test(t1);
                        }

                        @Override
                        public R apply(T1 t1) {
                          return self.apply(t1);
                        }
                    };
                }
              """)}

              ${checked.gen(xs"""
                /$javadoc
                 * Return a composed function that first applies this $className to the given arguments and in case of throwable
                 * try to get value from {@code recover} function with same arguments and throwable information.
                 *
                 * @param recover the function applied in case of throwable
                 * @return a function composed of this and recover
                 * @throws NullPointerException if recover is null
                 */
                default Function$i$fullGenerics recover(@NonNull ${im.getType("java.util.function.Function")}<? super Throwable, ? extends ${fullGenericsTypeF(checked = false, i)}> recover) {
                    Objects.requireNonNull(recover, "recover is null");
                    return ($params) -> {
                        try {
                            return this.apply($params);
                        } catch (Throwable throwable) {
                            final ${fullGenericsTypeF(checked = false, i)} func = recover.apply(throwable);
                            Objects.requireNonNull(func, () -> "recover return null for " + throwable.getClass() + ": " + throwable.getMessage());
                            return func.$callApply;
                        }
                    };
                }

                /$javadoc
                 * Returns an unchecked function that will <em>sneaky throw</em> if an exceptions occurs when applying the function.
                 *
                 * @return a new Function$i that throws a {@code Throwable}.
                 */
                default Function$i$fullGenerics unchecked() {
                    return ($params) -> {
                        try {
                            return apply($params);
                        } catch(Throwable t) {
                            return sneakyThrow(t);
                        }
                    };
                }
              """)}

              /$javadoc
               * Returns a composed function that first applies this $className to the given argument and then applies
               * {@linkplain $compositionType} {@code after} to the result.
               *
               * @param <V> return type of after
               * @param after the function applied after this
               * @return a function composed of this and after
               * @throws NullPointerException if after is null
               */
              default <V> $className<${genericsFunction}V> andThen(@NonNull $compositionType<? super R, ? extends V> after) {
                  $Objects.requireNonNull(after, "after is null");
                  return ($params) -> after.apply(apply($params));
              }

              ${(i == 1).gen(xs"""
                /$javadoc
                 * Returns a composed function that first applies the {@linkplain $compositionType} {@code before} the
                 * given argument and then applies this $className to the result.
                 *
                 * @param <V> argument type of before
                 * @param before the function applied before this
                 * @return a function composed of before and this
                 * @throws NullPointerException if before is null
                 */
                default <V> ${name}1<V, R> compose(@NonNull $compositionType<? super V, ? extends T1> before) {
                    $Objects.requireNonNull(before, "before is null");
                    return v -> apply(before.apply(v));
                }
              """)}

              ${(1 to i).gen(j => {
                val fName = s"before"
                val fGeneric = "S"
                val applicationArgs = (1 to i).gen(k => if (k == j) s"$fGeneric ${fGeneric.toLowerCase}" else s"T$k t$k")(using ", ")
                val generics = (1 to i).gen(k => if (k == j) "S" else s"T$k")(using ", ")
                val resultFunctionArgs = (j+1 to i).gen(k => s"T$k t$k")(using ", ")
                val applyArgs = (1 to i).gen(k => if (k ==j) s"$fName.apply(${fGeneric.toLowerCase})" else s"t$k")(using ", ")
                val variableApplyArgs = (j+1 to i).gen(k => s"t$k")(using ", ")
                val docAdd = i match
                  case 1 => ""
                  case 2 => " and the other argument"
                  case _ => " and the other arguments"
                xs"""
                  /$javadoc
                   * Returns a composed function that first applies the {@linkplain Function} {@code $fName} to the
                   * ${j.ordinal} argument and then applies this $className to the result$docAdd.
                   *
                   * @param <$fGeneric> argument type of $fName
                   * @param $fName the function applied before this
                   * @return a function composed of $fName and this
                   * @throws NullPointerException if $fName is null
                   */
                  default <S> $className<$generics, R> compose$j(@NonNull Function1<? super $fGeneric, ? extends T$j> $fName) {
                      Objects.requireNonNull($fName, "$fName is null");
                      return ($applicationArgs) -> apply($applyArgs);
                  }
                """
              })(using "\n\n")}
          }

          ${checked.gen(xs"""
            interface ${className}Module {

                // DEV-NOTE: we do not plan to expose this as public API
                @SuppressWarnings("unchecked")
                static <T extends Throwable, R> R sneakyThrow(Throwable t) throws T {
                    throw (T) t;
                }
            }
          """)}
        """
      }
    })
  }

  /**
   * Generator of io.vavr.Tuple*
   */
  def genTuples(): Unit = {

    genVavrFile("io.vavr", "Tuple", targetMain)(genBaseTuple)

    (0 to N).foreach { i =>
      genVavrFile("io.vavr", s"Tuple$i", targetMain)(genTuple(i))
    }

    /*
     * Generates Tuple1..N
     */
    def genTuple(i: Int)(im: ImportManager, packageName: String, className: String): String = {
      val a = Arity(i)
      val generics = a.genericsTuple
      val paramsDecl = a.paramsDecl
      val params = a.underscoreParams
      val paramTypes = a.wideGenerics
      val resultGenerics = if (i == 0) "" else s"<${(1 to i).gen(j => s"U$j")(using ", ")}>"
      val mapResult = i match {
        case 0 => ""
        case 1 => "? extends U1"
        case _ => s"Tuple$i<${(1 to i).gen(j => s"U$j")(using ", ")}>"
      }
      val comparableGenerics = if (i == 0) "" else s"<${(1 to i).gen(j => s"U$j extends Comparable<? super U$j>")(using ", ")}>"
      val untyped = if (i == 0) "" else s"<${(1 to i).gen(j => "?")(using ", ")}>"
      val functionType = javaFunctionType(i, im)
      val NonNullType = im.getType("org.jspecify.annotations.NonNull")
      val Comparator = im.getType("java.util.Comparator")
      val Objects = im.getType("java.util.Objects")
      val Seq = im.getType("io.vavr.collection.Seq")
      val List = im.getType("io.vavr.collection.List")
      if(i==2){
        im.getType("java.util.Map")
        im.getType("java.util.AbstractMap")
      }

      xs"""
        /**
         * A tuple of ${i.numerus("element")} which can be seen as cartesian product of ${i.numerus("component")}.
         ${(0 to i).gen(j => if (j == 0) "*" else s"* @param <T$j> type of the ${j.ordinal} element")(using "\n")}
         * @author Daniel Dietrich
         */
        public final class $className$generics implements Tuple, Comparable<$className$generics>, ${im.getType("java.io.Serializable")} {

            private static final long serialVersionUID = 1L;

            ${(1 to i).gen(j => xs"""
              /$javadoc
               * The ${j.ordinal} element of this tuple.
               */
              @SuppressWarnings("serial") // Conditionally serializable
              public final T$j _$j;
            """)(using "\n\n")}

            ${if (i == 0) xs"""
              /$javadoc
               * The singleton instance of Tuple0.
               */
              private static final Tuple0 INSTANCE = new Tuple0 ();

              /$javadoc
               * The singleton Tuple0 comparator.
               */
              private static final Comparator<Tuple0> COMPARATOR = (Comparator<Tuple0> & Serializable) (t1, t2) -> 0;

              // hidden constructor, internally called
              private Tuple0 () {
              }

              /$javadoc
               * Returns the singleton instance of Tuple0.
               *
               * @return The singleton instance of Tuple0.
               */
              public static Tuple0 instance() {
                  return INSTANCE;
              }
            """ else xs"""
              /$javadoc
               * Constructs a tuple of ${i.numerus("element")}.
               ${(0 to i).gen(j => if (j == 0) "*" else s"* @param t$j the ${j.ordinal} element")(using "\n")}
               */
              public $className($paramsDecl) {
                  ${(1 to i).gen(j => s"this._$j = t$j;")(using "\n")}
              }
            """}

            public static $generics $Comparator<$className$generics> comparator(${(1 to i).gen(j => s"$Comparator<? super T$j> t${j}Comp")(using ", ")}) {
                ${if (i == 0) xs"""
                  return COMPARATOR;
                """ else xs"""
                  return (Comparator<$className$generics> & Serializable) (t1, t2) -> {
                      ${(1 to i).gen(j => xs"""
                        final int check$j = t${j}Comp.compare(t1._$j, t2._$j);
                        if (check$j != 0) {
                            return check$j;
                        }
                      """)(using "\n\n")}

                      // all components are equal
                      return 0;
                  };
                """}
            }

            ${(i > 0).gen(xs"""
              @SuppressWarnings("unchecked")
              private static $comparableGenerics int compareTo($className$untyped o1, $className$untyped o2) {
                  final $className$resultGenerics t1 = ($className$resultGenerics) o1;
                  final $className$resultGenerics t2 = ($className$resultGenerics) o2;

                  ${(1 to i).gen(j => xs"""
                    final int check$j = t1._$j.compareTo(t2._$j);
                    if (check$j != 0) {
                        return check$j;
                    }
                  """)(using "\n\n")}

                  // all components are equal
                  return 0;
              }
            """)}

            @Override
            public int arity() {
                return $i;
            }

            @Override
            public int compareTo($className$generics that) {
                ${if (i == 0) xs"""
                  return 0;
                """ else xs"""
                  return $className.compareTo(this, that);
                """}
            }

            ${(1 to i).gen(j => xs"""
              /$javadoc
               * Getter of the ${j.ordinal} element of this tuple.
               *
               * @return the ${j.ordinal} element of this Tuple.
               */
              public T$j _$j() {
                  return _$j;
              }

              /$javadoc
               * Sets the ${j.ordinal} element of this tuple to the given {@code value}.
               *
               * @param value the new value
               * @return a copy of this tuple with a new value for the ${j.ordinal} element of this Tuple.
               */
              public $className$generics update$j(T$j value) {
                  return new $className<>(${(1 until j).gen(k => s"_$k")(using ", ")}${(j > 1).gen(", ")}value${(j < i).gen(", ")}${((j + 1) to i).gen(k => s"_$k")(using ", ")});
              }
            """)(using "\n\n")}

            ${(i == 2).gen(xs"""
              /$javadoc
               * Swaps the elements of this {@code Tuple}.
               *
               * @return A new Tuple where the first element is the second element of this Tuple
               *   and the second element is the first element of this Tuple.
               */
              public Tuple2<T2, T1> swap() {
                  return Tuple.of(_2, _1);
              }

              /$javadoc
               * Converts the tuple to java.util.Map.Entry {@code Tuple}.
               *
               * @return A  java.util.Map.Entry where the first element is the key and the second
               * element is the value.
               */
              public Map.Entry$generics toEntry() {
                  return new AbstractMap.SimpleEntry<>(_1, _2);
              }

            """)}

            ${(i > 0).gen(xs"""
              /$javadoc
               * Maps the components of this tuple using a mapper function.
               *
               * @param mapper the mapper function
               ${(1 to i).gen(j => s"* @param <U$j> new type of the ${j.ordinal} component")(using "\n")}
               * @return A new Tuple of same arity.
               * @throws NullPointerException if {@code mapper} is null
               */
              public $resultGenerics $className$resultGenerics map(@NonNull $functionType<$paramTypes, $mapResult> mapper) {
                  Objects.requireNonNull(mapper, "mapper is null");
                  ${if (i == 1)
                    "return Tuple.of(mapper.apply(_1));"
                  else
                    s"return mapper.apply($params);"
                  }
              }
            """)}

            ${(i > 1).gen(xs"""
              /$javadoc
               * Maps the components of this tuple using a mapper function for each component.
               ${(0 to i).gen(j => if (j == 0) "*" else s"* @param f$j the mapper function of the ${j.ordinal} component")(using "\n")}
               ${(1 to i).gen(j => s"* @param <U$j> new type of the ${j.ordinal} component")(using "\n")}
               * @return A new Tuple of same arity.
               * @throws NullPointerException if one of the arguments is null
               */
              public $resultGenerics $className$resultGenerics map(${(1 to i).gen(j => s"@NonNull ${im.getType("java.util.function.Function")}<? super T$j, ? extends U$j> f$j")(using ", ")}) {
                  ${(1 to i).gen(j => s"""Objects.requireNonNull(f$j, "f$j is null");""")(using "\n")}
                  return ${im.getType("io.vavr.Tuple")}.of(${(1 to i).gen(j => s"f$j.apply(_$j)")(using ", ")});
              }
            """)}

            ${(i > 1) `gen` (1 to i).gen(j => xs"""
              /$javadoc
               * Maps the ${j.ordinal} component of this tuple to a new value.
               *
               * @param <U> new type of the ${j.ordinal} component
               * @param mapper A mapping function
               * @return a new tuple based on this tuple and substituted ${j.ordinal} component
               */
              public <U> $className<${(1 to i).gen(k => if (j == k) "U" else s"T$k")(using ", ")}> map$j(${im.getType("java.util.function.Function")}<? super T$j, ? extends U> mapper) {
                  Objects.requireNonNull(mapper, "mapper is null");
                  final U u = mapper.apply(_$j);
                  return Tuple.of(${(1 to i).gen(k => if (j == k) "u" else s"_$k")(using ", ")});
              }
            """)(using "\n\n")}

            /**
             * Transforms this tuple to an object of type U.
             *
             * @param f Transformation which creates a new object of type U based on this tuple's contents.
             * @param <U> type of the transformation result
             * @return An object of type U
             * @throws NullPointerException if {@code f} is null
             */
            ${if (i == 0) xs"""
              public <U> U apply(@NonNull $functionType<? extends U> f) {
                  $Objects.requireNonNull(f, "f is null");
                  return f.get();
              }
            """ else xs"""
              public <U> U apply(@NonNull $functionType<$paramTypes, ? extends U> f) {
                  $Objects.requireNonNull(f, "f is null");
                  return f.apply($params);
              }
            """}

            @Override
            public $Seq<?> toSeq() {
                ${if (i == 0) xs"""
                  return $List.empty();
                """ else xs"""
                  return $List.of($params);
                """}
            }

            ${(i < N).gen(xs"""
              /$javadoc
               * Append a value to this tuple.
               *
               * @param <T${i+1}> type of the value to append
               * @param t${i+1} the value to append
               * @return a new Tuple with the value appended
               */
              public <T${i+1}> Tuple${i+1}<${(1 to i+1).gen(j => s"T$j")(using ", ")}> append(T${i+1} t${i+1}) {
                  return ${im.getType("io.vavr.Tuple")}.of(${(1 to i).gen(k => s"_$k")(using ", ")}${(i > 0).gen(", ")}t${i+1});
              }
            """)}

            ${(i < N) `gen` (1 to N-i).gen(j => xs"""
              /$javadoc
               * Concat a tuple's values to this tuple.
               *
               ${(i+1 to i+j).gen(k => s"* @param <T$k> the type of the ${k.ordinal} value in the tuple")(using "\n")}
               * @param tuple the tuple to concat
               * @return a new Tuple with the tuple values appended
               * @throws NullPointerException if {@code tuple} is null
               */
              public <${(i+1 to i+j).gen(k => s"T$k")(using ", ")}> Tuple${i+j}<${(1 to i+j).gen(k => s"T$k")(using ", ")}> concat(@NonNull Tuple$j<${(i+1 to i+j).gen(k => s"T$k")(using ", ")}> tuple) {
                  Objects.requireNonNull(tuple, "tuple is null");
                  return ${im.getType("io.vavr.Tuple")}.of(${(1 to i).gen(k => s"_$k")(using ", ")}${(i > 0).gen(", ")}${(1 to j).gen(k => s"tuple._$k")(using ", ")});
              }
            """)(using "\n\n")}

            // -- Object

            @Override
            public boolean equals(Object o) {
                ${if (i == 0) xs"""
                  return o == this;
                """ else xs"""
                  if (o == this) {
                      return true;
                  } else if (!(o instanceof $className)) {
                      return false;
                  } else {
                      final $className$untyped that = ($className$untyped) o;
                      return ${(1 to i).gen(j => s"${im.getType("java.util.Objects")}.equals(this._$j, that._$j)")(using "\n                           && ")};
                  }"""
                }
            }

            @Override
            public int hashCode() {
                return ${if (i == 0) "1" else s"""Tuple.hash(${(1 to i).gen(j => s"_$j")(using ", ")})"""};
            }

            @Override
            public String toString() {
                return ${if (i == 0) "\"()\"" else s""""(" + ${(1 to i).gen(j => s"_$j")(using " + \", \" + ")} + ")""""};
            }

            ${(i == 0).gen(xs"""
              // -- Serializable implementation

              /$javadoc
               * Instance control for object serialization.
               *
               * @return The singleton instance of Tuple0.
               * @see java.io.Serializable
               */
              private Object readResolve() {
                  return INSTANCE;
              }
            """)}

        }
      """
    }

    /*
     * Generates Tuple
     */
    def genBaseTuple(im: ImportManager, packageName: String, className: String): String = {
      val NonNullType = im.getType("org.jspecify.annotations.NonNull")

      val Map = im.getType("java.util.Map")
      val Objects = im.getType("java.util.Objects")
      val Seq = im.getType("io.vavr.collection.Seq")

      def genFactoryMethod(i: Int) = {
        val a = Arity(i)
        import a.{generics, paramsDecl, params, covariantGenerics => wideGenerics}
        xs"""
          /**
           * Creates a tuple of ${i.numerus("element")}.
           ${(0 to i).gen(j => if (j == 0) "*" else s"* @param <T$j> type of the ${j.ordinal} element")(using "\n")}
           ${(1 to i).gen(j => s"* @param t$j the ${j.ordinal} element")(using "\n")}
           * @return a tuple of ${i.numerus("element")}.
           */
          static <$generics> Tuple$i<$generics> of($paramsDecl) {
              return new Tuple$i<>($params);
          }
        """
      }

      def genHashMethod(i: Int) = {
        val paramsDecl = (1 to i).gen(j => s"Object o$j")(using ", ")
        xs"""
          /**
           * Return the order-dependent hash of the ${i.numerus("given value")}.
           ${(0 to i).gen(j => if (j == 0) "*" else s"* @param o$j the ${j.ordinal} value to hash")(using "\n")}
           * @return the same result as {@link $Objects#${if (i == 1) "hashCode(Object)" else "hash(Object...)"}}
           */
          static int hash($paramsDecl) {
              ${if (i == 1) {
                s"return $Objects.hashCode(o1);"
              } else {
                xs"""
                  int result = 1;
                  ${(1 to i).gen(j => s"result = 31 * result + hash(o$j);")(using "\n")}
                  return result;
                """
              }}
          }
        """
      }

      def genNarrowMethod(i: Int) = {
        val a = Arity(i)
        import a.{generics, covariantGenerics => wideGenerics}
        xs"""
          /**
           * Narrows a widened {@code Tuple$i<$wideGenerics>} to {@code Tuple$i<$generics>}.
           * This is eligible because immutable/read-only tuples are covariant.
           * @param t A {@code Tuple$i}.
           ${(1 to i).gen(j => s"* @param <T$j> the ${j.ordinal} component type")(using "\n")}
           * @return the given {@code t} instance as narrowed type {@code Tuple$i<$generics>}.
           */
          @SuppressWarnings("unchecked")
          static <$generics> Tuple$i<$generics> narrow(Tuple$i<$wideGenerics> t) {
              return (Tuple$i<$generics>) t;
          }
        """
      }

      def genSeqMethod(i: Int) = {
        val a = Arity(i)
        import a.generics
        val seqs = (1 to i).gen(j => s"Seq<T$j>")(using ", ")
        val Stream = im.getType("io.vavr.collection.Stream")
        val widenedGenerics = a.covariantGenerics
        xs"""
            /**
             * Turns a sequence of {@code Tuple$i} into a Tuple$i of {@code Seq}${(i > 1).gen("s")}.
             *
             ${(1 to i).gen(j => s"* @param <T$j> ${j.ordinal} component type")(using "\n")}
             * @param tuples an {@code Iterable} of tuples
             * @return a tuple of ${i.numerus(s"{@link $Seq}")}.
             */
            static <$generics> Tuple$i<$seqs> sequence$i(@NonNull Iterable<? extends Tuple$i<$widenedGenerics>> tuples) {
                $Objects.requireNonNull(tuples, "tuples is null");
                final Stream<Tuple$i<$widenedGenerics>> s = $Stream.ofAll(tuples);
                return new Tuple$i<>(${(1 to i).gen(j => s"s.map(Tuple$i::_$j)")(using s", ")});
            }
        """
      }

      xs"""
        /**
         * The base interface of all tuples.
         *
         * @author Daniel Dietrich
         */
        public interface Tuple extends ${im.getType("java.io.Serializable")} {

            long serialVersionUID = 1L;

            /**
             * The maximum arity of an Tuple.
             * <p>
             * Note: This value might be changed in a future version of Vavr.
             * So it is recommended to use this constant instead of hardcoding the current maximum arity.
             */
            int MAX_ARITY = $N;

            /**
             * Returns the number of elements of this tuple.
             *
             * @return the number of elements.
             */
            int arity();

            /**
             * Converts this tuple to a sequence.
             *
             * @return A new {@code Seq}.
             */
            $Seq<?> toSeq();

            // -- factory methods

            /$javadoc
             * Creates the empty tuple.
             *
             * @return the empty tuple.
             */
            static Tuple0 empty() {
                return Tuple0.instance();
            }

            /**
             * Creates a {@code Tuple2} from a {@link $Map.Entry}.
             *
             * @param <T1> Type of first component (entry key)
             * @param <T2> Type of second component (entry value)
             * @param      entry A {@link java.util.Map.Entry}
             * @return a new {@code Tuple2} containing key and value of the given {@code entry}
             */
            static <T1, T2> Tuple2<T1, T2> fromEntry($Map.@NonNull Entry<? extends T1, ? extends T2> entry) {
                $Objects.requireNonNull(entry, "entry is null");
                return new Tuple2<>(entry.getKey(), entry.getValue());
            }

            ${(1 to N).gen(genFactoryMethod)(using "\n\n")}

            ${(1 to N).gen(genHashMethod)(using "\n\n")}

            ${(1 to N).gen(genNarrowMethod)(using "\n\n")}

            ${(1 to N).gen(genSeqMethod)(using "\n\n")}

        }
      """
    }
  }

  /**
   * Generator of io.vavr.collection.*ArrayType
   */
  def genArrayTypes(): Unit = {

    val types = ListMap(
      "boolean" -> "Boolean",
      "byte" -> "Byte",
      "char" -> "Character",
      "double" -> "Double",
      "float" -> "Float",
      "int" -> "Integer",
      "long" -> "Long",
      "short" -> "Short",
      "Object" -> "Object" // fallback
    ) // note: there is no void[] in Java

    genVavrFile("io.vavr.collection", "ArrayType", targetMain)((im: ImportManager, packageName: String, className: String) => xs"""
      import java.util.Collection;

      /**
       * Helper to replace reflective array access.
       *
       * @author Pap Lőrinc
       */
      interface ArrayType<T> extends Serializable {

          long serialVersionUID = 1L;

          @SuppressWarnings("unchecked")
          static <T> ArrayType<T> obj() { return (ArrayType<T>) ObjectArrayType.INSTANCE; }

          Class<T> type();
          int lengthOf(Object array);
          T getAt(Object array, int index);

          Object empty();
          void setAt(Object array, int index, T value) throws ClassCastException;
          Object copy(Object array, int arraySize, int sourceFrom, int destinationFrom, int size);

          @SuppressWarnings("unchecked")
          static <T> ArrayType<T> of(Object array)  { return of((Class<T>) array.getClass().getComponentType()); }
          static <T> ArrayType<T> of(Class<T> type) { return !type.isPrimitive() ? obj() : ofPrimitive(type); }
          @SuppressWarnings("unchecked")
          static <T> ArrayType<T> ofPrimitive(Class<T> type) {
              if (boolean.class == type) {
                  return (ArrayType<T>) BooleanArrayType.INSTANCE;
              } else if (byte.class == type) {
                  return (ArrayType<T>) ByteArrayType.INSTANCE;
              } else if (char.class == type) {
                  return (ArrayType<T>) CharArrayType.INSTANCE;
              } else if (double.class == type) {
                  return (ArrayType<T>) DoubleArrayType.INSTANCE;
              } else if (float.class == type) {
                  return (ArrayType<T>) FloatArrayType.INSTANCE;
              } else if (int.class == type) {
                  return (ArrayType<T>) IntArrayType.INSTANCE;
              } else if (long.class == type) {
                  return (ArrayType<T>) LongArrayType.INSTANCE;
              } else if (short.class == type) {
                  return (ArrayType<T>) ShortArrayType.INSTANCE;
              } else {
                  throw new IllegalArgumentException(String.valueOf(type));
              }
          }

          default Object newInstance(int length) { return copy(empty(), length); }

          /** System.arrayCopy with same source and destination */
          default Object copyRange(Object array, int from, int to) {
              final int length = to - from;
              return copy(array, length, from, 0, length);
          }

          /** Repeatedly group an array into equal sized sub-trees */
          default Object grouped(Object array, int groupSize) {
              final int arrayLength = lengthOf(array);
              final Object results = obj().newInstance(1 + ((arrayLength - 1) / groupSize));
              obj().setAt(results, 0, copyRange(array, 0, groupSize));

              for (int start = groupSize, i = 1; start < arrayLength; i++) {
                  final int nextLength = Math.min(groupSize, arrayLength - (i * groupSize));
                  obj().setAt(results, i, copyRange(array, start, start + nextLength));
                  start += nextLength;
              }

              return results;
          }

          /** clone the source and set the value at the given position */
          default Object copyUpdate(Object array, int index, T element) {
              final Object copy = copy(array, index + 1);
              setAt(copy, index, element);
              return copy;
          }

          default Object copy(Object array, int minLength) {
              final int arrayLength = lengthOf(array);
              final int length = Math.max(arrayLength, minLength);
              return copy(array, length, 0, 0, arrayLength);
          }

          /** clone the source and keep everything after the index (pre-padding the values with null) */
          default Object copyDrop(Object array, int index) {
              final int length = lengthOf(array);
              return copy(array, length, index, index, length - index);
          }

          /** clone the source and keep everything before and including the index */
          default Object copyTake(Object array, int lastIndex) {
              return copyRange(array, 0, lastIndex + 1);
          }

          /** Create a single element array */
          default Object asArray(T element) {
              final Object result = newInstance(1);
              setAt(result, 0, element);
              return result;
          }

          /** Store the content of an iterable in an array */
          static Object[] asArray(java.util.Iterator<?> it, int length) {
              final Object[] array = new Object[length];
              for (int i = 0; i < length; i++) {
                  array[i] = it.next();
              }
              return array;
          }

          @SuppressWarnings("unchecked")
          static <T> T asPrimitives(Class<?> primitiveClass, Iterable<?> values) {
              final Object[] array = Array.ofAll(values).toJavaArray();
              final ArrayType<T> type = of((Class<T>) primitiveClass);
              final Object results = type.newInstance(array.length);
              for (int i = 0; i < array.length; i++) {
                  type.setAt(results, i, (T) array[i]);
              }
              return (T) results;
          }

          ${types.keys.toSeq.gen(arrayType =>
            genArrayType(arrayType)(im, packageName, arrayType.capitalize + className)
          )(using "\n\n")}
      }
    """)

    def genArrayType(arrayType: String)(im: ImportManager, packageName: String, className: String): String = {
      val wrapperType = types(arrayType)
      val isPrimitive = arrayType != "Object"

      xs"""
        final class $className implements ArrayType<$wrapperType>, ${im.getType("java.io.Serializable")} {
            private static final long serialVersionUID = 1L;
            static final $className INSTANCE = new $className();
            static final $arrayType[] EMPTY = new $arrayType[0];

            private static $arrayType[] cast(Object array) { return ($arrayType[]) array; }

            @Override
            public Class<$wrapperType> type() { return $arrayType.class; }

            @Override
            public $arrayType[] empty() { return EMPTY; }

            @Override
            public int lengthOf(Object array) { return (array != null) ? cast(array).length : 0; }

            @Override
            public $wrapperType getAt(Object array, int index) { return cast(array)[index]; }

            @Override
            public void setAt(Object array, int index, $wrapperType value) ${if (isPrimitive) "throws ClassCastException " else ""}{
                ${if (isPrimitive)
                """if (value != null) {
                  |    cast(array)[index] = value;
                  |} else {
                  |    throw new ClassCastException();
                  |}""".stripMargin
              else "cast(array)[index] = value;" }
            }

            @Override
            public Object copy(Object array, int arraySize, int sourceFrom, int destinationFrom, int size) {
                return (size > 0)
                        ? copyNonEmpty(array, arraySize, sourceFrom, destinationFrom, size)
                        : new $arrayType[arraySize];
            }
            private static Object copyNonEmpty(Object array, int arraySize, int sourceFrom, int destinationFrom, int size) {
                final $arrayType[] result = new $arrayType[arraySize];
                System.arraycopy(array, sourceFrom, result, destinationFrom, size); /* has to be near the object allocation to avoid zeroing out the array */
                return result;
            }
        }
      """
    }
  }
}
