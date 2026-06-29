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

/**
 * Generator of Functions
 */
def genFunctions(targetMain: String): Unit = {

  (0 to N).foreach(i => {

    genVavrFile("io.vavr", s"CheckedFunction$i", targetMain)(genFunction("CheckedFunction", checked = true))
    genVavrFile("io.vavr", s"Function$i", targetMain)(genFunction("Function", checked = false))

    def genFunction(name: String, checked: Boolean)(im: ImportManager, packageName: String, className: String): String = {

      val a = Arity(i)
      import a.{generics, fullGenerics, fullWideGenerics, genericsTuple, genericsFunction, genericsReversedFunction, paramsDecl, params, paramsReversed, tupled}
      val genericsOptionReturnType = s"<$genericsFunction${im.getType("io.vavr.control.Option")}<R>>"
      val genericsTryReturnType = s"<$genericsFunction${im.getType("io.vavr.control.Try")}<R>>"
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

      // Type-variable curried form e.g. Function1<T1, Function1<T2, R>>
      // Cannot share with FunctionTestGenerator: that version uses Object, not T1..Ti
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
        case _ => s"$count arguments"
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
