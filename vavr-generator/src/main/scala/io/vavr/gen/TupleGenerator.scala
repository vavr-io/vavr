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
 * Generator of io.vavr.Tuple*
 */
def genTuples(targetMain: String): Unit = {

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
                    return ${(1 to i).gen(j => s"${im.getType("java.util.Objects")}.equals(this._$j, that._$j)")(using "\n                         && ")};
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
