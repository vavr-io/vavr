/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */

// temporarily needed to circumvent https://issues.scala-lang.org/browse/SI-3772 (see case class Generics)
import Generator._
import JavaGenerator._

import collection.immutable.ListMap
import scala.language.implicitConversions

val N = 8
val VARARGS = 10
val TARGET_MAIN = "vavr/src-gen/main/java"
val TARGET_TEST = "vavr/src-gen/test/java"
val CHARSET = java.nio.charset.StandardCharsets.UTF_8

/**
 * ENTRY POINT
 */
def run(): Unit = {
  generateMainClasses()
  generateTestClasses()
}

/**
 * Generate Vavr src-gen/main/java classes
 */
def generateMainClasses(): Unit = {

  // Workaround: Use /$javadoc instead of /** in a StringContext when IntelliJ IDEA otherwise shows up errors in the editor
  val javadoc = "**"

  genAPI()
  genFunctions()
  genTuples()
  genArrayTypes()

  /**
   * Generator of Match
   */
  def genAPI(): Unit = {

    genVavrFile("io.vavr", "API")(genAPI)

    def genAPI(im: ImportManager, packageName: String, className: String): String = {

      val OptionType = im.getType("io.vavr.control.Option")
      val IteratorType = im.getType("io.vavr.collection.Iterator")
      val EitherType = im.getType("io.vavr.control.Either")
      val FutureType = im.getType("io.vavr.concurrent.Future")
      val CheckedFunction0Type = im.getType("io.vavr.CheckedFunction0")
      val PartialFunctionType = im.getType("io.vavr.PartialFunction")
      val TryType = im.getType("io.vavr.control.Try")
      val ValidationType = im.getType("io.vavr.control.Validation")
      val CharSeqType = im.getType("io.vavr.collection.CharSeq")
      val ArrayType = im.getType("io.vavr.collection.Array")
      val VectorType = im.getType("io.vavr.collection.Vector")
      val ListType = im.getType("io.vavr.collection.List")
      val StreamType = im.getType("io.vavr.collection.Stream")
      val QueueType = im.getType("io.vavr.collection.Queue")
      val LinkedHashSetType = im.getType("io.vavr.collection.LinkedHashSet")
      val HashSetType = im.getType("io.vavr.collection.HashSet")
      val TreeSetType = im.getType("io.vavr.collection.TreeSet")
      val PriorityQueueType = im.getType("io.vavr.collection.PriorityQueue")
      val LinkedHashMapType = im.getType("io.vavr.collection.LinkedHashMap")
      val HashMapType = im.getType("io.vavr.collection.HashMap")
      val TreeMapType = im.getType("io.vavr.collection.TreeMap")
      val IndexedSeqType = im.getType("io.vavr.collection.IndexedSeq")
      val MapType = im.getType("io.vavr.collection.Map")
      val SeqType = im.getType("io.vavr.collection.Seq")
      val SetType = im.getType("io.vavr.collection.Set")
      val SortedMapType = im.getType("io.vavr.collection.SortedMap")
      val SortedSetType = im.getType("io.vavr.collection.SortedSet")

      // Note: import the Java stuff last in order to force full qualified names on import clashes

      val Objects = im.getType("java.util.Objects")
      val JavaComparatorType = im.getType("java.util.Comparator")
      val JavaMapType = im.getType("java.util.Map")
      val ExecutorServiceType = im.getType("java.util.concurrent.ExecutorService")
      val FunctionType = im.getType("java.util.function.Function")
      val BiFunctionType = im.getType("java.util.function.BiFunction")
      val PredicateType = im.getType("java.util.function.Predicate")
      val SupplierType = im.getType("java.util.function.Supplier")

      def genTraversableAliases(traversableType: String, returnType: String, name: String) = xs"""
        // -- $name

        /$javadoc
         * Alias for {@link $traversableType#empty()}
         *
         * @param <T> Component type of element.
         * @return A singleton instance of empty {@link $traversableType}
         */
        public static <T> $returnType<T> $name() {
            return $traversableType.empty();
        }

        /$javadoc
         * Alias for {@link $traversableType#of(Object)}
         *
         * @param <T>     Component type of element.
         * @param element An element.
         * @return A new {@link $traversableType} instance containing the given element
         */
        public static <T> $returnType<T> $name(T element) {
            return $traversableType.of(element);
        }

        /$javadoc
         * Alias for {@link $traversableType#of(Object...)}
         *
         * @param <T>      Component type of elements.
         * @param elements Zero or more elements.
         * @return A new {@link $traversableType} instance containing the given elements
         * @throws NullPointerException if {@code elements} is null
         */
        @SuppressWarnings("varargs")
        @SafeVarargs
        public static <T> $returnType<T> $name(T... elements) {
            return $traversableType.of(elements);
        }
      """

      def genSortedTraversableAliases(traversableType: String, returnType: String, name: String) = xs"""
        // -- $name

        /$javadoc
         * Alias for {@link $traversableType#empty()}
         *
         * @param <T> Component type of element.
         * @return A new {@link $traversableType} empty instance
         */
        public static <T extends Comparable<? super T>> $returnType<T> $name() {
            return $traversableType.empty();
        }

        /$javadoc
         * Alias for {@link $traversableType#empty($JavaComparatorType)}
         *
         * @param <T>        Component type of element.
         * @param comparator The comparator used to sort the elements
         * @return A new {@link $traversableType} empty instance
         */
        public static <T extends Comparable<? super T>> $returnType<T> $name($JavaComparatorType<? super T> comparator) {
            return $traversableType.empty(comparator);
        }

        /$javadoc
         * Alias for {@link $traversableType#of(Comparable)}
         *
         * @param <T>     Component type of element.
         * @param element An element.
         * @return A new {@link $traversableType} instance containing the given element
         */
        public static <T extends Comparable<? super T>> $returnType<T> $name(T element) {
            return $traversableType.of(element);
        }

        /$javadoc
         * Alias for {@link $traversableType#of($JavaComparatorType, Object)}
         *
         * @param <T>        Component type of element.
         * @param comparator The comparator used to sort the elements
         * @param element    An element.
         * @return A new {@link $traversableType} instance containing the given element
         */
        public static <T> $returnType<T> $name($JavaComparatorType<? super T> comparator, T element) {
            return $traversableType.of(comparator, element);
        }

        /$javadoc
         * Alias for {@link $traversableType#of(Comparable...)}
         *
         * @param <T>      Component type of element.
         * @param elements Zero or more elements.
         * @return A new {@link $traversableType} instance containing the given elements
         */
        @SuppressWarnings("varargs")
        @SafeVarargs
        public static <T extends Comparable<? super T>> $returnType<T> $name(T... elements) {
            return $traversableType.of(elements);
        }

        /$javadoc
         * Alias for {@link $traversableType#of($JavaComparatorType, Object...)}
         *
         * @param <T>        Component type of element.
         * @param comparator The comparator used to sort the elements
         * @param elements   Zero or more elements.
         * @return A new {@link $traversableType} instance containing the given elements
         */
        @SuppressWarnings("varargs")
        @SafeVarargs
        public static <T> $returnType<T> $name($JavaComparatorType<? super T> comparator, T... elements) {
            return $traversableType.of(comparator, elements);
        }
      """

      def genMapAliases(mapType: String, returnType: String, name: String) = xs"""
        // -- $name

        /$javadoc
         * Alias for {@link $mapType#empty()}
         *
         * @param <K> The key type.
         * @param <V> The value type.
         * @return A singleton instance of empty {@link $mapType}
         */
        public static <K, V> $returnType<K, V> $name() {
            return $mapType.empty();
        }

        /$javadoc
         * Alias for {@link $mapType#ofEntries(Tuple2...)}
         *
         * @param <K>     The key type.
         * @param <V>     The value type.
         * @param entries Map entries.
         * @return A new {@link $mapType} instance containing the given entries
         */
        @SuppressWarnings("varargs")
        @SafeVarargs
        public static <K, V> $returnType<K, V> $name(Tuple2<? extends K, ? extends V>... entries) {
            return $mapType.ofEntries(entries);
        }

        ${(1 to VARARGS).gen(i => {
          xs"""
            /$javadoc
             * Alias for {@link $mapType#of(${(1 to i).gen(j => "Object, Object")(", ")})}
             *
             * @param <K> The key type.
             * @param <V> The value type.
             ${(1 to i).gen(j => s"* @param k$j  The key${ if (i > 1) s" of the ${j.ordinal} pair" else ""}\n* @param v$j  The value${ if (i > 1) s" of the ${j.ordinal} pair" else ""}\n")}
             * @return A new {@link $mapType} instance containing the given entries
             */
            public static <K, V> $returnType<K, V> $name(${(1 to i).gen(j => xs"K k$j, V v$j")(", ")}) {
                return $mapType.of(${(1 to i).gen(j => xs"k$j, v$j")(", ")});
            }
          """
        })("\n\n")}
      """

      def genSortedMapAliases(mapType: String, returnType: String, name: String) = xs"""
        /$javadoc
         * Alias for {@link $mapType#empty()}
         *
         * @param <K> The key type.
         * @param <V> The value type.
         * @return A new empty {@link $mapType} instance
         */
        public static <K extends Comparable<? super K>, V> $returnType<K, V> $name() {
            return $mapType.empty();
        }

        /$javadoc
         * Alias for {@link $mapType#empty($JavaComparatorType)}
         *
         * @param <K>           The key type.
         * @param <V>           The value type.
         * @param keyComparator The comparator used to sort the entries by their key
         * @return A new empty {@link $mapType} instance
         */
        public static <K, V> $returnType<K, V> $name($JavaComparatorType<? super K> keyComparator) {
            return $mapType.empty(keyComparator);
        }

        /$javadoc
         * Alias for {@link $mapType#of(Comparator, Object, Object)}
         *
         * @param <K>           The key type.
         * @param <V>           The value type.
         * @param keyComparator The comparator used to sort the entries by their key
         * @param key           A singleton map key.
         * @param value         A singleton map value.
         * @return A new {@link $mapType} instance containing the given entry
         */
        public static <K, V> $returnType<K, V> $name(Comparator<? super K> keyComparator, K key, V value) {
            return $mapType.of(keyComparator, key, value);
        }

        /$javadoc
         * Alias for {@link $mapType#ofEntries(Tuple2...)}
         *
         * @param <K>     The key type.
         * @param <V>     The value type.
         * @param entries Map entries.
         * @return A new {@link $mapType} instance containing the given entries
         */
        @SuppressWarnings("varargs")
        @SafeVarargs
        public static <K extends Comparable<? super K>, V> $returnType<K, V> $name(Tuple2<? extends K, ? extends V>... entries) {
            return $mapType.ofEntries(entries);
        }

        /$javadoc
         * Alias for {@link $mapType#ofEntries($JavaComparatorType, Tuple2...)}
         *
         * @param <K>           The key type.
         * @param <V>           The value type.
         * @param keyComparator The comparator used to sort the entries by their key
         * @param entries       Map entries.
         * @return A new {@link $mapType} instance containing the given entry
         */
        @SuppressWarnings("varargs")
        @SafeVarargs
        public static <K, V> $returnType<K, V> $name($JavaComparatorType<? super K> keyComparator, Tuple2<? extends K, ? extends V>... entries) {
            return $mapType.ofEntries(keyComparator, entries);
        }

        /$javadoc
         * Alias for {@link $mapType#ofAll($JavaMapType)}
         *
         * @param <K> The key type.
         * @param <V> The value type.
         * @param map A map entry.
         * @return A new {@link $mapType} instance containing the given map
         */
        public static <K extends Comparable<? super K>, V> $returnType<K, V> $name($JavaMapType<? extends K, ? extends V> map) {
            return $mapType.ofAll(map);
        }

        ${(1 to VARARGS).gen(i => xs"""
          /$javadoc
           * Alias for {@link $mapType#of(${(1 to i).gen(j => s"${if (mapType.equals("TreeMap")) s"Comparable" else s"Object"}, Object")(", ")})}
           *
           * @param <K> The key type.
           * @param <V> The value type.
           ${(1 to i).gen(j => s"* @param k$j  The key${if (i > 1) s" of the ${j.ordinal} pair" else ""}\n* @param v$j  The value${if (i > 1) s" of the ${j.ordinal} pair" else ""}\n")}
           * @return A new {@link $mapType} instance containing the given entries
           */
          public static <K extends Comparable<? super K>, V> $returnType<K, V> $name(${(1 to i).gen(j => xs"K k$j, V v$j")(", ")}) {
              return $mapType.of(${(1 to i).gen(j => xs"k$j, v$j")(", ")});
          }
        """)("\n\n")}
      """

      def genAliases(im: ImportManager, packageName: String, className: String): String = {
        xs"""
          //
          // Aliases for static factories
          //

          // -- Function

          ${(0 to N).gen(i => {
            val generics = (1 to i).gen(j => s"T$j")(", ")
            val fullGenerics = s"<${(i > 0).gen(s"$generics, ")}R>"
            xs"""
              /$javadoc
               * Alias for {@link Function$i#of(Function$i)}
               *
               ${(0 to i).gen(j => if (j == 0) "* @param <R>             return type" else s"* @param <T$j>${if (j < 10) " " else ""}           type of the ${j.ordinal} argument")("\n")}
               * @param methodReference A method reference
               * @return A {@link Function$i}
               */
              public static $fullGenerics Function$i$fullGenerics Function(Function$i$fullGenerics methodReference) {
                  return Function$i.of(methodReference);
              }
            """
          })("\n\n")}

          // -- CheckedFunction

          ${(0 to N).gen(i => {
            val generics = (1 to i).gen(j => s"T$j")(", ")
            val fullGenerics = s"<${(i > 0).gen(s"$generics, ")}R>"
            xs"""
              /$javadoc
               * Alias for {@link CheckedFunction$i#of(CheckedFunction$i)}
               *
               ${(0 to i).gen(j => if (j == 0) "* @param <R>             return type" else s"* @param <T$j>            type of the ${j.ordinal} argument")("\n")}
               * @param methodReference A method reference
               * @return A {@link CheckedFunction$i}
               */
              public static $fullGenerics CheckedFunction$i$fullGenerics CheckedFunction(CheckedFunction$i$fullGenerics methodReference) {
                  return CheckedFunction$i.of(methodReference);
              }
            """
          })("\n\n")}

          // -- unchecked

          ${(0 to N).gen(i => {
            val generics = (1 to i).gen(j => s"T$j")(", ")
            val fullGenerics = s"<${(i > 0).gen(s"$generics, ")}R>"
            xs"""
              /$javadoc
               * Alias for {@link CheckedFunction$i#unchecked}
               *
               ${(0 to i).gen(j => if (j == 0) "* @param <R>  return type" else s"* @param <T$j> type of the ${j.ordinal} argument")("\n")}
               * @param f    A method reference
               * @return An unchecked wrapper of supplied {@link CheckedFunction$i}
               */
              public static $fullGenerics Function$i$fullGenerics unchecked(CheckedFunction$i$fullGenerics f) {
                  return f.unchecked();
              }
            """
          })("\n\n")}

          // -- Tuple

          /$javadoc
           * Alias for {@link Tuple#empty()}
           *
           * @return the empty tuple.
           */
          public static Tuple0 Tuple() {
              return Tuple.empty();
          }

          ${(1 to N).gen(i => {
            val generics = (1 to i).gen(j => s"T$j")(", ")
            val links = (1 to i).gen(j => s"Object")(", ")
            val params = (1 to i).gen(j => s"T$j t$j")(", ")
            val args = (1 to i).gen(j => s"t$j")(", ")
            xs"""
              /$javadoc
               * Alias for {@link Tuple#of($links)}
               *
               * Creates a tuple of ${i.numerus("element")}.
               *
               ${(1 to i).gen(j => s"* @param <T$j> type of the ${j.ordinal} element")("\n")}
               ${(1 to i).gen(j => s"* @param t$j   the ${j.ordinal} element")("\n")}
               * @return a tuple of ${i.numerus("element")}.
               */
              public static <$generics> Tuple$i<$generics> Tuple($params) {
                  return Tuple.of($args);
              }
            """
          })("\n\n")}

          // -- Either

          /$javadoc
           * Alias for {@link $EitherType#right(Object)}
           *
           * @param <L>   Type of left value.
           * @param <R>   Type of right value.
           * @param right The value.
           * @return A new {@link $EitherType.Right} instance.
           */
          @SuppressWarnings("unchecked")
          public static <L, R> $EitherType.Right<L, R> Right(R right) {
              return ($EitherType.Right<L, R>) $EitherType.right(right);
          }

          /$javadoc
           * Alias for {@link $EitherType#left(Object)}
           *
           * @param <L>  Type of left value.
           * @param <R>  Type of right value.
           * @param left The value.
           * @return A new {@link $EitherType.Left} instance.
           */
          @SuppressWarnings("unchecked")
          public static <L, R> $EitherType.Left<L, R> Left(L left) {
              return ($EitherType.Left<L, R>) $EitherType.left(left);
          }

          // -- Future

          /$javadoc
           * Alias for {@link $FutureType#of($CheckedFunction0Type)}
           *
           * @param <T>         Type of the computation result.
           * @param computation A computation.
           * @return A new {@link $FutureType} instance.
           * @throws NullPointerException if computation is null.
           */
          public static <T> $FutureType<T> Future($CheckedFunction0Type<? extends T> computation) {
              return $FutureType.of(computation);
          }

          /$javadoc
           * Alias for {@link $FutureType#of($ExecutorServiceType, $CheckedFunction0Type)}
           *
           * @param <T>             Type of the computation result.
           * @param executorService An executor service.
           * @param computation     A computation.
           * @return A new {@link $FutureType} instance.
           * @throws NullPointerException if one of executorService or computation is null.
           */
          public static <T> $FutureType<T> Future($ExecutorServiceType executorService, $CheckedFunction0Type<? extends T> computation) {
              return $FutureType.of(executorService, computation);
          }

          /$javadoc
           * Alias for {@link $FutureType#successful(Object)}
           *
           * @param <T>    The value type of a successful result.
           * @param result The result.
           * @return A succeeded {@link $FutureType}.
           */
          public static <T> $FutureType<T> Future(T result) {
              return $FutureType.successful(result);
          }

          /$javadoc
           * Alias for {@link $FutureType#successful($ExecutorServiceType, Object)}
           *
           * @param <T>             The value type of a successful result.
           * @param executorService An {@code ExecutorService}.
           * @param result          The result.
           * @return A succeeded {@link $FutureType}.
           * @throws NullPointerException if executorService is null
           */
          public static <T> $FutureType<T> Future($ExecutorServiceType executorService, T result) {
              return $FutureType.successful(executorService, result);
          }

          // -- Lazy

          /$javadoc
           * Alias for {@link Lazy#of($SupplierType)}
           *
           * @param <T>      type of the lazy value
           * @param supplier A supplier
           * @return A new instance of {@link Lazy}
           */
          public static <T> Lazy<T> Lazy($SupplierType<? extends T> supplier) {
              return Lazy.of(supplier);
          }

          // -- Option

          /$javadoc
           * Alias for {@link $OptionType#of(Object)}
           *
           * @param <T>   type of the value
           * @param value A value
           * @return {@link $OptionType.Some} if value is not {@code null}, {@link $OptionType.None} otherwise
           */
          public static <T> $OptionType<T> Option(T value) {
              return $OptionType.of(value);
          }

          /$javadoc
           * Alias for {@link $OptionType#some(Object)}
           *
           * @param <T>   type of the value
           * @param value A value
           * @return {@link $OptionType.Some}
           */
          @SuppressWarnings("unchecked")
          public static <T> $OptionType.Some<T> Some(T value) {
              return ($OptionType.Some<T>) $OptionType.some(value);
          }

          /$javadoc
           * Alias for {@link $OptionType#none()}
           *
           * @param <T> component type
           * @return the singleton instance of {@link $OptionType.None}
           */
          @SuppressWarnings("unchecked")
          public static <T> $OptionType.None<T> None() {
              return ($OptionType.None<T>) $OptionType.none();
          }

          // -- Try

          /$javadoc
           * Alias for {@link $TryType#of($CheckedFunction0Type)}
           *
           * @param <T>      Component type
           * @param supplier A checked supplier
           * @return {@link $TryType.Success} if no exception occurs, otherwise {@link $TryType.Failure} if an
           * exception occurs calling {@code supplier.get()}.
           */
          public static <T> $TryType<T> Try($CheckedFunction0Type<? extends T> supplier) {
              return $TryType.of(supplier);
          }

          /$javadoc
           * Alias for {@link $TryType#success(Object)}
           *
           * @param <T>   Type of the given {@code value}.
           * @param value A value.
           * @return A new {@link $TryType.Success}.
           */
          @SuppressWarnings("unchecked")
          public static <T> $TryType.Success<T> Success(T value) {
              return ($TryType.Success<T>) $TryType.success(value);
          }

          /$javadoc
           * Alias for {@link $TryType#failure(Throwable)}
           *
           * @param <T>       Component type of the {@code Try}.
           * @param exception An exception.
           * @return A new {@link $TryType.Failure}.
           */
          @SuppressWarnings("unchecked")
          public static <T> $TryType.Failure<T> Failure(Throwable exception) {
              return ($TryType.Failure<T>) $TryType.failure(exception);
          }

          // -- Validation

          /$javadoc
           * Alias for {@link $ValidationType#valid(Object)}
           *
           * @param <E>   type of the error
           * @param <T>   type of the given {@code value}
           * @param value A value
           * @return {@link $ValidationType.Valid}
           * @throws NullPointerException if value is null
           */
          @SuppressWarnings("unchecked")
          public static <E, T> $ValidationType.Valid<E, T> Valid(T value) {
              return ($ValidationType.Valid<E, T>) $ValidationType.valid(value);
          }

          /$javadoc
           * Alias for {@link $ValidationType#invalid(Object)}
           *
           * @param <E>   type of the given {@code error}
           * @param <T>   type of the value
           * @param error An error
           * @return {@link $ValidationType.Invalid}
           * @throws NullPointerException if error is null
           */
          @SuppressWarnings("unchecked")
          public static <E, T> $ValidationType.Invalid<E, T> Invalid(E error) {
              return ($ValidationType.Invalid<E, T>) $ValidationType.invalid(error);
          }

          // -- CharSeq

          /$javadoc
           * Alias for {@link $CharSeqType#of(char)}
           *
           * @param character A character.
           * @return A new {@link $CharSeqType} instance containing the given element
           */
          public static $CharSeqType CharSeq(char character) {
              return $CharSeqType.of(character);
          }

          /$javadoc
           * Alias for {@link $CharSeqType#of(char...)}
           *
           * @param characters Zero or more characters.
           * @return A new {@link $CharSeqType} instance containing the given characters in the same order.
           * @throws NullPointerException if {@code elements} is null
           */
          public static $CharSeqType CharSeq(char... characters) {
              return $CharSeqType.of(characters);
          }

          /$javadoc
           * Alias for {@link $CharSeqType#of(CharSequence)}
           *
           * @param sequence {@code CharSequence} instance.
           * @return A new {@link $CharSeqType} instance
           */
          public static $CharSeqType CharSeq(CharSequence sequence) {
              return $CharSeqType.of(sequence);
          }

          // -- TRAVERSABLES

          ${genSortedTraversableAliases(PriorityQueueType, PriorityQueueType, "PriorityQueue")}

          // -- SEQUENCES

          ${genTraversableAliases(ListType, SeqType, "Seq")}
          ${genTraversableAliases(VectorType, IndexedSeqType, "IndexedSeq")}
          ${genTraversableAliases(ArrayType, ArrayType, "Array")}
          ${genTraversableAliases(ListType, ListType, "List")}
          ${genTraversableAliases(QueueType, QueueType, "Queue")}
          ${genTraversableAliases(StreamType, StreamType, "Stream")}
          ${genTraversableAliases(VectorType, VectorType, "Vector")}

          // -- SETS

          ${genTraversableAliases(HashSetType, SetType, "Set")}
          ${genTraversableAliases(LinkedHashSetType, SetType, "LinkedSet")}
          ${genSortedTraversableAliases(TreeSetType, SortedSetType, "SortedSet")}

          // -- MAPS

          ${genMapAliases(HashMapType, MapType, "Map")}
          ${genMapAliases(LinkedHashMapType, MapType, "LinkedMap")}
          ${genSortedMapAliases(TreeMapType, SortedMapType, "SortedMap")}

        """
      }

      im.getStatic("io.vavr.API.Match.*")

      def genJavaTypeTweaks(im: ImportManager, packageName: String, className: String): String = {
        xs"""
          //
          // Java type tweaks
          //

          /**
           * Runs a {@code unit} of work and returns {@code Void}. This is helpful when a return value is expected,
           * e.g. by {@code Match}:
           *
           * <pre><code>Match(i).of(
           *     Case($$(is(0)), i -&gt; run(() -&gt; System.out.println("zero"))),
           *     Case($$(is(1)), i -&gt; run(() -&gt; System.out.println("one"))),
           *     Case($$(), o -&gt; run(() -&gt; System.out.println("many")))
           * )</code></pre>
           *
           * @param unit A block of code to be run.
           * @return the single instance of {@code Void}, namely {@code null}
           */
          public static Void run(Runnable unit) {
              unit.run();
              return null;
          }
        """
      }

      def genFor(im: ImportManager, packageName: String, className: String): String = {
        xs"""
          //
          // For-Comprehension
          //

          /**
           * A shortcut for {@code Iterator.ofAll(ts).flatMap(f)} which allows us to write real for-comprehensions using
           * {@code For(...).yield(...)}.
           * <p>
           * Example:
           * <pre><code>
           * For(getPersons(), person -&gt;
           *     For(person.getTweets(), tweet -&gt;
           *         For(tweet.getReplies())
           *             .yield(reply -&gt; person + ", " + tweet + ", " + reply)));
           * </code></pre>
           *
           * @param ts An iterable
           * @param f A function {@code T -> Iterable<U>}
           * @param <T> element type of {@code ts}
           * @param <U> component type of the resulting {@code Iterator}
           * @return A new Iterator
           */
          public static <T, U> $IteratorType<U> For(Iterable<T> ts, Function<? super T, ? extends Iterable<U>> f) {
              return $IteratorType.ofAll(ts).flatMap(f);
          }

          ${(1 to N).gen(i => {
            val generics = (1 to i).gen(j => s"T$j")(", ")
            val params = (1 to i).gen(j => s"Iterable<T$j> ts$j")(", ")
            xs"""
              /$javadoc
               * Creates a {@code For}-comprehension of ${i.numerus("Iterable")}.
               ${(0 to i).gen(j => if (j == 0) "*" else s"* @param ts$j the ${j.ordinal} Iterable")("\n")}
               ${(1 to i).gen(j => s"* @param <T$j> component type of the ${j.ordinal} Iterable")("\n")}
               * @return a new {@code For}-comprehension of arity $i
               */
              public static <$generics> For$i<$generics> For($params) {
                  ${(1 to i).gen(j => xs"""$Objects.requireNonNull(ts$j, "ts$j is null");""")("\n")}
                  return new For$i<>(${(1 to i).gen(j => s"ts$j")(", ")});
              }
            """
          })("\n\n")}

          ${(1 to N).gen(i => {
            val generics = (1 to i).gen(j => s"T$j")(", ")
            val functionType = i match {
              case 1 => FunctionType
              case 2 => BiFunctionType
              case _ => s"Function$i"
            }
            val args = (1 to i).gen(j => s"? super T$j")(", ")
            xs"""
              /$javadoc
               * For-comprehension with ${i.numerus("Iterable")}.
               */
              public static class For$i<$generics> {

                  ${(1 to i).gen(j => xs"""private final Iterable<T$j> ts$j;""")("\n")}

                  private For$i(${(1 to i).gen(j => s"Iterable<T$j> ts$j")(", ")}) {
                      ${(1 to i).gen(j => xs"""this.ts$j = ts$j;""")("\n")}
                  }

                  /$javadoc
                   * Yields a result for elements of the cross product of the underlying Iterables.
                   *
                   * @param f a function that maps an element of the cross product to a result
                   * @param <R> type of the resulting {@code Iterator} elements
                   * @return an {@code Iterator} of mapped results
                   */
                  public <R> $IteratorType<R> yield($functionType<$args, ? extends R> f) {
                      $Objects.requireNonNull(f, "f is null");
                      ${if (i == 1) xs"""
                        return $IteratorType.ofAll(ts1).map(f);
                      """ else xs"""
                        return
                            ${(1 until i).gen(j => s"$IteratorType.ofAll(ts$j).flatMap(t$j ->")("\n")}
                            $IteratorType.ofAll(ts$i).map(t$i -> f.apply(${(1 to i).gen(j => s"t$j")(", ")}))${")" * (i - 1)};
                      """}
                  }

                  ${(i == 1).gen(xs"""
                    /$javadoc
                     * A shortcut for {@code yield(Function.identity())}.
                     *
                     * @return an {@code Iterator} of mapped results
                     */
                    public $IteratorType<T1> yield() {
                        return yield(Function.identity());
                    }
                  """)}
              }
            """
          })("\n\n")}
        """
      }

      def genMatch(im: ImportManager, packageName: String, className: String): String = {
        xs"""
          //
          // Structural Pattern Matching
          //

          // -- static Match API

          /**
           * Entry point of the match API.
           *
           * @param value a value to be matched
           * @param <T> type of the value
           * @return a new {@code Match} instance
           */
          @GwtIncompatible
          public static <T> Match<T> Match(T value) {
              return new Match<>(value);
          }

          // -- static Case API

          // - Pattern0

          @GwtIncompatible
          public static <T, R> Case<T, R> Case(Pattern0<T> pattern, $FunctionType<? super T, ? extends R> f) {
              $Objects.requireNonNull(pattern, "pattern is null");
              $Objects.requireNonNull(f, "f is null");
              return new Case0<>(pattern, f);
          }

          @GwtIncompatible
          public static <T, R> Case<T, R> Case(Pattern0<T> pattern, $SupplierType<? extends R> supplier) {
              $Objects.requireNonNull(pattern, "pattern is null");
              $Objects.requireNonNull(supplier, "supplier is null");
              return new Case0<>(pattern, ignored -> supplier.get());
          }

          @GwtIncompatible
          public static <T, R> Case<T, R> Case(Pattern0<T> pattern, R retVal) {
              $Objects.requireNonNull(pattern, "pattern is null");
              return new Case0<>(pattern, ignored -> retVal);
          }

          ${(1 to N).gen(i => {
            val argTypes = (1 to i).gen(j => s"? super T$j")(", ")
            val generics = (1 to i).gen(j => s"T$j")(", ")
            val params = (i > 1).gen("(") + (1 to i).gen(j => s"_$j")(", ") + (i > 1).gen(")")
            val functionType = i match {
              case 1 => FunctionType
              case 2 => BiFunctionType
              case _ => s"Function$i"
            }
            xs"""
              // - Pattern$i

              @GwtIncompatible
              public static <T, $generics, R> Case<T, R> Case(Pattern$i<T, $generics> pattern, $functionType<$argTypes, ? extends R> f) {
                  $Objects.requireNonNull(pattern, "pattern is null");
                  $Objects.requireNonNull(f, "f is null");
                  return new Case$i<>(pattern, f);
              }

              @GwtIncompatible
              public static <T, $generics, R> Case<T, R> Case(Pattern$i<T, $generics> pattern, $SupplierType<? extends R> supplier) {
                  $Objects.requireNonNull(pattern, "pattern is null");
                  $Objects.requireNonNull(supplier, "supplier is null");
                  return new Case$i<>(pattern, $params -> supplier.get());
              }

              @GwtIncompatible
              public static <T, $generics, R> Case<T, R> Case(Pattern$i<T, $generics> pattern, R retVal) {
                  $Objects.requireNonNull(pattern, "pattern is null");
                  return new Case$i<>(pattern, $params -> retVal);
              }
            """
          })("\n\n")}

          // PRE-DEFINED PATTERNS

          // 1) Atomic patterns $$(), $$(value), $$(predicate)

          /**
           * Wildcard pattern, matches any value.
           *
           * @param <T> injected type of the underlying value
           * @return a new {@code Pattern0} instance
           */
          @GwtIncompatible
          public static <T> Pattern0<T> $$() {
              return Pattern0.any();
          }

          /**
           * Value pattern, checks for equality.
           *
           * @param <T>       type of the prototype
           * @param prototype the value that should be equal to the underlying object
           * @return a new {@code Pattern0} instance
           */
          @GwtIncompatible
          public static <T> Pattern0<T> $$(T prototype) {
              return new Pattern0<T>() {
                  @Override
                  public T apply(T obj) {
                      return obj;
                  }

                  @Override
                  public boolean isDefinedAt(T obj) {
                      return $Objects.equals(obj, prototype);
                  }
              };
          }

          /**
           * Guard pattern, checks if a predicate is satisfied.
           * <p>
           * This method is intended to be used with lambdas and method references, for example:
           *
           * <pre><code>
           * String evenOrOdd(int num) {
           *     return Match(num).of(
           *             Case($$(i -&gt; i % 2 == 0), "even"),
           *             Case($$(this::isOdd), "odd")
           *     );
           * }
           *
           * boolean isOdd(int i) {
           *     return i % 2 == 1;
           * }
           * </code></pre>
           *
           * It is also valid to pass {@code Predicate} instances:
           *
           * <pre><code>
           * Predicate&lt;Integer&gt; isOdd = i -&gt; i % 2 == 1;
           *
           * Match(num).of(
           *         Case($$(i -&gt; i % 2 == 0), "even"),
           *         Case($$(isOdd), "odd")
           * );
           * </code></pre>
           *
           * <strong>Note:</strong> Please take care when matching {@code Predicate} instances. In general,
           * <a href="http://cstheory.stackexchange.com/a/14152" target="_blank">function equality</a>
           * is an undecidable problem in computer science. In Vavr we are only able to check,
           * if two functions are the same instance.
           * <p>
           * However, this code will fail:
           *
           * <pre><code>
           * Predicate&lt;Integer&gt; p = i -&gt; true;
           * Match(p).of(
           *     Case($$(p), 1) // WRONG! It calls $$(Predicate)
           * );
           * </code></pre>
           *
           * Instead we have to use {@link Predicates#is(Object)}:
           *
           * <pre><code>
           * Predicate&lt;Integer&gt; p = i -&gt; true;
           * Match(p).of(
           *     Case($$(is(p)), 1) // CORRECT! It calls $$(T)
           * );
           * </code></pre>
           *
           * @param <T>       type of the prototype
           * @param predicate the predicate that tests a given value
           * @return a new {@code Pattern0} instance
           */
          @GwtIncompatible
          public static <T> Pattern0<T> $$($PredicateType<? super T> predicate) {
              $Objects.requireNonNull(predicate, "predicate is null");
              return new Pattern0<T>() {
                  @Override
                  public T apply(T obj) {
                      return obj;
                  }

                  @Override
                  public boolean isDefinedAt(T obj) {
                      return predicate.test(obj);
                  }
              };
          }

          /**
           * Scala-like structural pattern matching for Java. Instances are obtained via {@link API#Match(Object)}.
           * @param <T> type of the object that is matched
           */
          @GwtIncompatible
          public static final class Match<T> {

              private final T value;

              private Match(T value) {
                  this.value = value;
              }

              @SuppressWarnings({ "unchecked", "varargs" })
              @SafeVarargs
              public final <R> R of(Case<? extends T, ? extends R>... cases) {
                  Objects.requireNonNull(cases, "cases is null");
                  for (Case<? extends T, ? extends R> _case : cases) {
                      final Case<T, R> __case = (Case<T, R>) _case;
                      if (__case.isDefinedAt(value)) {
                          return __case.apply(value);
                      }
                  }
                  throw new MatchError(value);
              }

              @SuppressWarnings({ "unchecked", "varargs" })
              @SafeVarargs
              public final <R> $OptionType<R> option(Case<? extends T, ? extends R>... cases) {
                  Objects.requireNonNull(cases, "cases is null");
                  for (Case<? extends T, ? extends R> _case : cases) {
                      final Case<T, R> __case = (Case<T, R>) _case;
                      if (__case.isDefinedAt(value)) {
                          return $OptionType.some(__case.apply(value));
                      }
                  }
                  return $OptionType.none();
              }

              // -- CASES

              // javac needs fqn's here
              public interface Case<T, R> extends $PartialFunctionType<T, R> {
              }

              public static final class Case0<T, R> implements Case<T, R> {

                  private final Pattern0<T> pattern;
                  private final $FunctionType<? super T, ? extends R> f;

                  private Case0(Pattern0<T> pattern, $FunctionType<? super T, ? extends R> f) {
                      this.pattern = pattern;
                      this.f = f;
                  }

                  @Override
                  public R apply(T obj) {
                      return f.apply(pattern.apply(obj));
                  }

                  @Override
                  public boolean isDefinedAt(T obj) {
                      return pattern.isDefinedAt(obj);
                  }
              }

              ${(1 to N).gen(i => {
                val argTypes = (1 to i).gen(j => s"? super T$j")(", ")
                val generics = (1 to i).gen(j => s"T$j")(", ")
                val functionType = i match {
                  case 1 => FunctionType
                  case 2 => BiFunctionType
                  case _ => s"Function$i"
                }
                xs"""
                  public static final class Case$i<T, $generics, R> implements Case<T, R> {

                      private final Pattern$i<T, $generics> pattern;
                      private final $functionType<$argTypes, ? extends R> f;

                      private Case$i(Pattern$i<T, $generics> pattern, $functionType<$argTypes, ? extends R> f) {
                          this.pattern = pattern;
                          this.f = f;
                      }

                      @Override
                      public R apply(T obj) {
                          ${if (i == 1) xs"""
                             return f.apply(pattern.apply(obj));
                          """ else xs"""
                            return pattern.apply(obj).apply(f);
                          """}
                      }

                      @Override
                      public boolean isDefinedAt(T obj) {
                          return pattern.isDefinedAt(obj);
                      }
                  }
                """
              })("\n\n")}

              // -- PATTERNS

              /**
               * A Pattern is a partial {@link $FunctionType} in the sense that a function applications returns an
               * optional result of type {@code Option<R>}.
               *
               * @param <T> Class type that is matched by this pattern
               * @param <R> Type of the single or composite part this pattern decomposes
               */
              // javac needs fqn's here
              public interface Pattern<T, R> extends $PartialFunctionType<T, R> {
              }

              // These can't be @FunctionalInterfaces because of ambiguities.
              // For benchmarks lambda vs. abstract class see http://www.oracle.com/technetwork/java/jvmls2013kuksen-2014088.pdf

              public static abstract class Pattern0<T> implements Pattern<T, T> {

                  private static final Pattern0<Object> ANY = new Pattern0<Object>() {
                      @Override
                      public Object apply(Object obj) {
                          return obj;
                      }

                      @Override
                      public boolean isDefinedAt(Object obj) {
                          return true;
                      }
                  };

                  @SuppressWarnings("unchecked")
                  public static <T> Pattern0<T> any() {
                      return (Pattern0<T>) ANY;
                  }

                  // DEV-NOTE: We need the lower bound `Class<? super T>` instead of the more appropriate `Class<T>`
                  //           because it allows us to create patterns for generic types, which would otherwise not be
                  //           possible: `Pattern0<Some<String>> p = Pattern0.of(Some.class);`
                  public static <T> Pattern0<T> of(Class<? super T> type) {
                      return new Pattern0<T>() {
                          @Override
                          public T apply(T obj) {
                              return obj;
                          }

                          @Override
                          public boolean isDefinedAt(T obj) {
                              return obj != null && type.isAssignableFrom(obj.getClass());
                          }
                      };
                  }

                  private Pattern0() {
                  }
              }

              ${(1 to N).gen(i => {
                val declaredGenerics = (1 to i).gen(j => s"T$j extends U$j, U$j")(", ")
                val resultGenerics = (1 to i).gen(j => s"T$j")(", ")
                val resultType = if (i == 1) resultGenerics else s"Tuple$i<$resultGenerics>"
                val unapplyGenerics = (1 to i).gen(j => s"U$j")(", ")
                val unapplyTupleType = s"Tuple$i<$unapplyGenerics>"
                val args = (1 to i).gen(j => s"Pattern<T$j, ?> p$j")(", ")
                xs"""
                  public static abstract class Pattern$i<T, $resultGenerics> implements Pattern<T, $resultType> {

                      public static <T, $declaredGenerics> Pattern$i<T, $resultGenerics> of(Class<? super T> type, $args, Function<T, $unapplyTupleType> unapply) {
                          return new Pattern$i<T, $resultGenerics>() {
                              @SuppressWarnings("unchecked")
                              @Override
                              public $resultType apply(T obj) {
                                  ${if (i == 1) xs"""
                                    return (T1) unapply.apply(obj)._1;
                                  """ else xs"""
                                    return ($resultType) unapply.apply(obj);
                                  """}
                              }

                              @SuppressWarnings("unchecked")
                              @Override
                              public boolean isDefinedAt(T obj) {
                                  if (obj == null || !type.isAssignableFrom(obj.getClass())) {
                                      return false;
                                  } else {
                                      final $unapplyTupleType u = unapply.apply(obj);
                                      return
                                              ${(1 to i).gen(j => s"((Pattern<U$j, ?>) p$j).isDefinedAt(u._$j)")(" &&\n")};
                                  }
                              }
                          };
                      }
                  }
                """
              })("\n\n")}
          }
        """
      }

      def genShortcuts(im: ImportManager, packageName: String, className: String): String = {

        val FormatterType = im.getType("java.util.Formatter")
        val PrintStreamType = im.getType("java.io.PrintStream")

        xs"""
          //
          // Shortcuts
          //

          /**
           * A temporary replacement for an implementations used during prototyping.
           * <p>
           * Example:
           *
           * <pre><code>
           * public HttpResponse getResponse(HttpRequest request) {
           *     return TODO();
           * }
           *
           * final HttpResponse response = getHttpResponse(TODO());
           * </code></pre>
           *
           * @param <T> The result type of the missing implementation.
           * @return Nothing - this methods always throws.
           * @throws NotImplementedError when this methods is called
           * @see NotImplementedError#NotImplementedError()
           */
          public static <T> T TODO() {
              throw new NotImplementedError();
          }

          /**
           * A temporary replacement for an implementations used during prototyping.
           * <p>
           * Example:
           *
           * <pre><code>
           * public HttpResponse getResponse(HttpRequest request) {
           *     return TODO("fake response");
           * }
           *
           * final HttpResponse response = getHttpResponse(TODO("fake request"));
           * </code></pre>
           *
           * @param msg An error message
           * @param <T> The result type of the missing implementation.
           * @return Nothing - this methods always throws.
           * @throws NotImplementedError when this methods is called
           * @see NotImplementedError#NotImplementedError(String)
           */
          public static <T> T TODO(String msg) {
              throw new NotImplementedError(msg);
          }

          /**
           * Shortcut for {@code System.out.print(obj)}. See {@link $PrintStreamType#print(Object)}.
           *
           * @param obj The <code>Object</code> to be printed
           */
          public static void print(Object obj) {
              System.out.print(obj);
          }

          /**
           * Shortcut for {@code System.out.printf(format, args)}. See {@link $PrintStreamType#printf(String, Object...)}.
           *
           * @param format A format string as described in {@link $FormatterType}.
           * @param args   Arguments referenced by the format specifiers
           */
          @GwtIncompatible
          public static void printf(String format, Object... args) {
              System.out.printf(format, args);
          }

          /**
           * Shortcut for {@code System.out.println(obj)}. See {@link $PrintStreamType#println(Object)}.
           *
           * @param obj The <code>Object</code> to be printed
           */
          public static void println(Object obj) {
              System.out.println(obj);
          }

          /**
           * Shortcut for {@code System.out.println()}. See {@link $PrintStreamType#println()}.
           */
          public static void println() {
              System.out.println();
          }
        """
      }

      xs"""
        /**
         * The most basic Vavr functionality is accessed through this API class.
         *
         * <pre><code>
         * import static io.vavr.API.*;
         * </code></pre>
         *
         * <h3>For-comprehension</h3>
         * <p>
         * The {@code For}-comprehension is syntactic sugar for nested for-loops. We write
         *
         * <pre><code>
         * // lazily evaluated
         * Iterator&lt;R&gt; result = For(iterable1, iterable2, ..., iterableN).yield(f);
         * </code></pre>
         *
         * or
         *
         * <pre><code>
         * Iterator&lt;R&gt; result =
         *     For(iterable1, v1 -&gt;
         *         For(iterable2, v2 -&gt;
         *             ...
         *             For(iterableN).yield(vN -&gt; f.apply(v1, v2, ..., vN))
         *         )
         *     );
         * </code></pre>
         *
         * instead of
         *
         * <pre><code>
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
         * </code></pre>
         *
         * Please note that values like Option, Try, Future, etc. are also iterable.
         * <p>
         * Given a suitable function
         * f: {@code (v1, v2, ..., vN) -> ...} and 1 &lt;= N &lt;= 8 iterables, the result is a Stream of the
         * mapped cross product elements.
         *
         * <pre><code>
         * { f(v1, v2, ..., vN) | v1 &isin; iterable1, ... vN &isin; iterableN }
         * </code></pre>
         *
         * As with all Vavr Values, the result of a For-comprehension can be converted
         * to standard Java library and Vavr types.
         * @author Daniel Dietrich
         */
        public final class API {

            private API() {
            }

            ${genShortcuts(im, packageName, className)}

            ${genAliases(im, packageName, className)}

            ${genJavaTypeTweaks(im, packageName, className)}

            ${genFor(im, packageName, className)}

            ${genMatch(im, packageName, className)}
        }
      """
    }
  }

  /**
   * Generator of Functions
   */
  def genFunctions(): Unit = {

    (0 to N).foreach(i => {

      genVavrFile("io.vavr", s"CheckedFunction$i")(genFunction("CheckedFunction", checked = true))
      genVavrFile("io.vavr", s"Function$i")(genFunction("Function", checked = false))

      def genFunction(name: String, checked: Boolean)(im: ImportManager, packageName: String, className: String): String = {

        val generics = (1 to i).gen(j => s"T$j")(", ")
        val fullGenerics = s"<${(i > 0).gen(s"$generics, ")}R>"
        val wideGenerics = (1 to i).gen(j => s"? super T$j")(", ")
        val fullWideGenerics = s"<${(i > 0).gen(s"$wideGenerics, ")}? extends R>"
        val genericsReversed = (1 to i).reverse.gen(j => s"T$j")(", ")
        val genericsTuple = if (i > 0) s"<$generics>" else ""
        val genericsFunction = if (i > 0) s"$generics, " else ""
        val genericsReversedFunction = if (i > 0) s"$genericsReversed, " else ""
        val genericsOptionReturnType = s"<${(i > 0).gen(s"$generics, ")}${im.getType("io.vavr.control.Option")}<R>>"
        val genericsTryReturnType = s"<${(i > 0).gen(s"$generics, ")}${im.getType("io.vavr.control.Try")}<R>>"
        val curried = if (i == 0) "v" else (1 to i).gen(j => s"t$j")(" -> ")
        val paramsDecl = (1 to i).gen(j => s"T$j t$j")(", ")
        val params = (1 to i).gen(j => s"t$j")(", ")
        val paramsReversed = (1 to i).reverse.gen(j => s"t$j")(", ")
        val tupled = (1 to i).gen(j => s"t._$j")(", ")
        val compositionType = if(checked) "CheckedFunction1" else im.getType("java.util.function.Function")

        // imports

        val Objects = im.getType("java.util.Objects")
        val Try = if (checked) im.getType("io.vavr.control.Try") else ""
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

        im.getStatic(s"io.vavr.${className}Module.sneakyThrow")

        xs"""
          /**
           * Represents a function with ${arguments(i)}.
           ${(0 to i).gen(j => if (j == 0) "*" else s"* @param <T$j> argument $j of the function")("\n")}
           * @param <R> return type of the function
           * @author Daniel Dietrich
           */
          @FunctionalInterface
          public interface $className$fullGenerics extends Lambda<R>$additionalExtends {

              /$javadoc
               * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
               */
              long serialVersionUID = 1L;

              /$javadoc
               * Creates a {@code $className} based on
               * <ul>
               * <li><a href="https://docs.oracle.com/javase/tutorial/java/javaOO/methodreferences.html">method reference</a></li>
               * <li><a href="https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html#syntax">lambda expression</a></li>
               * </ul>
               *
               * Examples (w.l.o.g. referring to Function1):
               * <pre><code>// using a lambda expression
               * Function1&lt;Integer, Integer&gt; add1 = Function1.of(i -&gt; i + 1);
               *
               * // using a method reference (, e.g. Integer method(Integer i) { return i + 1; })
               * Function1&lt;Integer, Integer&gt; add2 = Function1.of(this::method);
               *
               * // using a lambda reference
               * Function1&lt;Integer, Integer&gt; add3 = Function1.of(add1::apply);
               * </code></pre>
               * <p>
               * <strong>Caution:</strong> Reflection loses type information of lambda references.
               * <pre><code>// type of a lambda expression
               * Type&lt;?, ?&gt; type1 = add1.getType(); // (Integer) -&gt; Integer
               *
               * // type of a method reference
               * Type&lt;?, ?&gt; type2 = add2.getType(); // (Integer) -&gt; Integer
               *
               * // type of a lambda reference
               * Type&lt;?, ?&gt; type3 = add3.getType(); // (Object) -&gt; Object
               * </code></pre>
               *
               * @param methodReference (typically) a method reference, e.g. {@code Type::method}
               ${(0 to i).gen(j => if (j == 0) "* @param <R> return type" else s"* @param <T$j> ${j.ordinal} argument")("\n")}
               * @return a {@code $className}
               */
              static $fullGenerics $className$fullGenerics of($className$fullGenerics methodReference) {
                  return methodReference;
              }

              /$javadoc
               * Lifts the given {@code partialFunction} into a total function that returns an {@code Option} result.
               *
               * @param partialFunction a function that is not defined for all values of the domain (e.g. by throwing)
               ${(0 to i).gen(j => if (j == 0) "* @param <R> return type" else s"* @param <T$j> ${j.ordinal} argument")("\n")}
               * @return a function that applies arguments to the given {@code partialFunction} and returns {@code Some(result)}
               *         if the function is defined for the given arguments, and {@code None} otherwise.
               */
              @SuppressWarnings("RedundantTypeArguments")
              static $fullGenerics ${im.getType(s"io.vavr.Function$i")}$genericsOptionReturnType lift($fullGenericsType partialFunction) {
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
               ${(0 to i).gen(j => if (j == 0) "* @param <R> return type" else s"* @param <T$j> ${j.ordinal} argument")("\n")}
               * @return a function that applies arguments to the given {@code partialFunction} and returns {@code Success(result)}
               *         if the function is defined for the given arguments, and {@code Failure(throwable)} otherwise.
               */
              static $fullGenerics ${im.getType(s"io.vavr.Function$i")}$genericsTryReturnType liftTry($fullGenericsType partialFunction) {
                  ${
                    val supplier = if (!checked && i == 0) "partialFunction::get" else if (checked && i == 0) "partialFunction::apply" else s"() -> partialFunction.apply($params)"
                    val lambdaArgs = if (i == 1) params else s"($params)"
                    xs"""
                      return $lambdaArgs -> ${im.getType("io.vavr.control.Try")}.of($supplier);
                    """
                  }
              }

              /$javadoc
               * Narrows the given {@code $fullGenericsType} to {@code $className$fullGenerics}
               *
               * @param f A {@code $className}
               ${(0 to i).gen(j => if (j == 0) "* @param <R> return type" else s"* @param <T$j> ${j.ordinal} argument")("\n")}
               * @return the given {@code f} instance as narrowed type {@code $className$fullGenerics}
               */
              @SuppressWarnings("unchecked")
              static $fullGenerics $className$fullGenerics narrow($fullGenericsType f) {
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
               ${(0 to i).gen(j => if (j == 0) "*" else s"* @param t$j argument $j")("\n")}
               * @return the result of function application
               * ${checked.gen("@throws Throwable if something goes wrong applying this function to the given arguments")}
               */
              R apply($paramsDecl)${checked.gen(" throws Throwable")};

              ${(1 until i).gen(j => {
                val partialApplicationArgs = (1 to j).gen(k => s"T$k t$k")(", ")
                val resultFunctionGenerics = (j+1 to i).gen(k => s"T$k")(", ")
                val resultFunctionArgs = (j+1 to i).gen(k => s"T$k t$k")(", ")
                val fixedApplyArgs = (1 to j).gen(k => s"t$k")(", ")
                val variableApplyArgs = (j+1 to i).gen(k => s"t$k")(", ")
                xs"""
                  /$javadoc
                   * Applies this function partially to ${j.numerus("argument")}.
                   *
                   ${(1 to j).gen(k => s"* @param t$k argument $k")("\n")}
                   * @return a partial application of this function
                   */
                  default $name${i - j}<$resultFunctionGenerics, R> apply($partialApplicationArgs) {
                      return ($resultFunctionArgs) -> apply($fixedApplyArgs, $variableApplyArgs);
                  }
                """
              })("\n\n")}

              ${(!checked && i == 0).gen(xs"""
                /$javadoc
                 * Implementation of {@linkplain java.util.function.Supplier#get()}, just calls {@linkplain #apply()}.
                 *
                 * @return the result of {@code apply()}
                 */
                @Override
                default R get() {
                    return apply();
                }
              """)}

              @Override
              default int arity() {
                  return $i;
              }

              /**
               * Returns a function that always returns the constant
               * value that you give in parameter.
               *
               ${(1 to i).gen(j => s"* @param <T$j> generic parameter type $j of the resulting function")("\n")}
               * @param <R> the result type
               * @param value the value to be returned
               * @return a function always returning the given value
               */
              static $fullGenerics $className$fullGenerics constant(R value) {
                  return ($params) -> value;
              }

              @Override
              default ${curriedType(i, name)} curried() {
                  return ${if (i < 2) "this" else s"$curried -> apply($params)"};
              }

              @Override
              default ${name}1<Tuple$i$genericsTuple, R> tupled() {
                  return t -> apply($tupled);
              }

              @Override
              default $className<${genericsReversedFunction}R> reversed() {
                  return ${if (i < 2) "this" else s"($paramsReversed) -> apply($params)"};
              }

              @Override
              default $className$fullGenerics memoized() {
                  if (isMemoized()) {
                      return this;
                  } else {
                      ${val mappingFunction = (checked, i) match {
                          case (true, 0) => s"() -> $Try.of(this::apply).get()"
                          case (true, _) => s"t -> $Try.of(() -> apply($params)).get()"
                          case (false, 0) => s"this::apply"
                          case (false, _) => s"tupled()"
                        }
                        if (i == 0) xs"""
                          return ($className$fullGenerics & Memoized) Lazy.of($mappingFunction)::get;
                        """ else xs"""
                          final ${im.getType("java.util.Map")}<Tuple$i<$generics>, R> cache = new ${im.getType("java.util.HashMap")}<>();
                          return ($className$fullGenerics & Memoized) ($params)
                                  -> Memoized.of(cache, Tuple.of($params), $mappingFunction);
                        """
                      }
                  }
              }

              ${(i == 1 && !checked).gen(xs"""
                /$javadoc
                 * Converts this {@code Function1} to a {@link PartialFunction} by adding an {@code isDefinedAt} condition.
                 * <p>
                 * @param isDefinedAt a predicate that states if an element is in the domain of the returned {@code PartialFunction}.
                 * @return a new {@code PartialFunction} that has the same behavior like this function but is defined only for those elements that make it through the given {@code Predicate}
                 * @throws NullPointerException if {@code isDefinedAt} is null
                 */
                default PartialFunction<T1, R> partial(${im.getType("java.util.function.Predicate")}<? super T1> isDefinedAt) {
                    Objects.requireNonNull(isDefinedAt, "isDefinedAt is null");
                    final Function1<T1, R> self = this;
                    return new PartialFunction<T1, R>() {
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
                default Function$i$fullGenerics recover(${im.getType("java.util.function.Function")}<? super Throwable, ? extends ${fullGenericsTypeF(checked = false, i)}> recover) {
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
              default <V> $className<${genericsFunction}V> andThen($compositionType<? super R, ? extends V> after) {
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
                default <V> ${name}1<V, R> compose($compositionType<? super V, ? extends T1> before) {
                    $Objects.requireNonNull(before, "before is null");
                    return v -> apply(before.apply(v));
                }
              """)}
          }

          interface ${className}Module {

              // DEV-NOTE: we do not plan to expose this as public API
              @SuppressWarnings("unchecked")
              static <T extends Throwable, R> R sneakyThrow(Throwable t) throws T {
                  throw (T) t;
              }
          }
        """
      }
    })
  }

  /**
   * Generator of io.vavr.Tuple*
   */
  def genTuples(): Unit = {

    genVavrFile("io.vavr", "Tuple")(genBaseTuple)

    (0 to N).foreach { i =>
      genVavrFile("io.vavr", s"Tuple$i")(genTuple(i))
    }

    /*
     * Generates Tuple1..N
     */
    def genTuple(i: Int)(im: ImportManager, packageName: String, className: String): String = {
      val generics = if (i == 0) "" else s"<${(1 to i).gen(j => s"T$j")(", ")}>"
      val paramsDecl = (1 to i).gen(j => s"T$j t$j")(", ")
      val params = (1 to i).gen(j => s"_$j")(", ")
      val paramTypes = (1 to i).gen(j => s"? super T$j")(", ")
      val resultGenerics = if (i == 0) "" else s"<${(1 to i).gen(j => s"U$j")(", ")}>"
      val mapResult = i match {
        case 0 => ""
        case 1 => "? extends U1"
        case _ => s"Tuple$i<${(1 to i).gen(j => s"U$j")(", ")}>"
      }
      val comparableGenerics = if (i == 0) "" else s"<${(1 to i).gen(j => s"U$j extends Comparable<? super U$j>")(", ")}>"
      val untyped = if (i == 0) "" else s"<${(1 to i).gen(j => "?")(", ")}>"
      val functionType = i match {
        case 0 => im.getType("java.util.function.Supplier")
        case 1 => im.getType("java.util.function.Function")
        case 2 => im.getType("java.util.function.BiFunction")
        case _ => s"Function$i"
      }
      val Comparator = im.getType("java.util.Comparator")
      val Objects = im.getType("java.util.Objects")
      val Seq = im.getType("io.vavr.collection.Seq")
      val List = im.getType("io.vavr.collection.List")
      val Iterator = im.getType("io.vavr.collection.Iterator")
      if(i==2){
        im.getType("java.util.Map")
        im.getType("java.util.AbstractMap")
      }

      xs"""
        /**
         * A tuple of ${i.numerus("element")} which can be seen as cartesian product of ${i.numerus("component")}.
         ${(0 to i).gen(j => if (j == 0) "*" else s"* @param <T$j> type of the ${j.ordinal} element")("\n")}
         * @author Daniel Dietrich
         */
        public final class $className$generics implements Tuple, Comparable<$className$generics>, ${im.getType("java.io.Serializable")} {

            private static final long serialVersionUID = 1L;

            ${(1 to i).gen(j => xs"""
              /$javadoc
               * The ${j.ordinal} element of this tuple.
               */
              public final T$j _$j;
            """)("\n\n")}

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
               ${(0 to i).gen(j => if (j == 0) "*" else s"* @param t$j the ${j.ordinal} element")("\n")}
               */
              public $className($paramsDecl) {
                  ${(1 to i).gen(j => s"this._$j = t$j;")("\n")}
              }
            """}

            public static $generics $Comparator<$className$generics> comparator(${(1 to i).gen(j => s"$Comparator<? super T$j> t${j}Comp")(", ")}) {
                ${if (i == 0) xs"""
                  return COMPARATOR;
                """ else xs"""
                  return (Comparator<$className$generics> & Serializable) (t1, t2) -> {
                      ${(1 to i).gen(j => xs"""
                        final int check$j = t${j}Comp.compare(t1._$j, t2._$j);
                        if (check$j != 0) {
                            return check$j;
                        }
                      """)("\n\n")}

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
                  """)("\n\n")}

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
                  return new $className<>(${(1 until j).gen(k => s"_$k")(", ")}${(j > 1).gen(", ")}value${(j < i).gen(", ")}${((j + 1) to i).gen(k => s"_$k")(", ")});
              }
            """)("\n\n")}

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
               ${(1 to i).gen(j => s"* @param <U$j> new type of the ${j.ordinal} component")("\n")}
               * @return A new Tuple of same arity.
               * @throws NullPointerException if {@code mapper} is null
               */
              public $resultGenerics $className$resultGenerics map($functionType<$paramTypes, $mapResult> mapper) {
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
               ${(0 to i).gen(j => if (j == 0) "*" else s"* @param f$j the mapper function of the ${j.ordinal} component")("\n")}
               ${(1 to i).gen(j => s"* @param <U$j> new type of the ${j.ordinal} component")("\n")}
               * @return A new Tuple of same arity.
               * @throws NullPointerException if one of the arguments is null
               */
              public $resultGenerics $className$resultGenerics map(${(1 to i).gen(j => s"${im.getType("java.util.function.Function")}<? super T$j, ? extends U$j> f$j")(", ")}) {
                  ${(1 to i).gen(j => s"""Objects.requireNonNull(f$j, "f$j is null");""")("\n")}
                  return ${im.getType("io.vavr.Tuple")}.of(${(1 to i).gen(j => s"f$j.apply(_$j)")(", ")});
              }
            """)}

            ${(i > 1) gen (1 to i).gen(j => xs"""
              /$javadoc
               * Maps the ${j.ordinal} component of this tuple to a new value.
               *
               * @param <U> new type of the ${j.ordinal} component
               * @param mapper A mapping function
               * @return a new tuple based on this tuple and substituted ${j.ordinal} component
               */
              public <U> $className<${(1 to i).gen(k => if (j == k) "U" else s"T$k")(", ")}> map$j(${im.getType("java.util.function.Function")}<? super T$j, ? extends U> mapper) {
                  Objects.requireNonNull(mapper, "mapper is null");
                  final U u = mapper.apply(_$j);
                  return Tuple.of(${(1 to i).gen(k => if (j == k) "u" else s"_$k")(", ")});
              }
            """)("\n\n")}

            /**
             * Transforms this tuple to an object of type U.
             *
             * @param f Transformation which creates a new object of type U based on this tuple's contents.
             * @param <U> type of the transformation result
             * @return An object of type U
             * @throws NullPointerException if {@code f} is null
             */
            ${if (i == 0) xs"""
              public <U> U apply($functionType<? extends U> f) {
                  $Objects.requireNonNull(f, "f is null");
                  return f.get();
              }
            """ else xs"""
              public <U> U apply($functionType<$paramTypes, ? extends U> f) {
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
                      return ${(1 to i).gen(j => s"${im.getType("java.util.Objects")}.equals(this._$j, that._$j)")("\n                             && ")};
                  }"""
                }
            }

            ${(i == 1).gen("// if _1 == null, hashCode() returns Objects.hash(new T1[] { null }) = 31 instead of 0 = Objects.hash(null)")}
            @Override
            public int hashCode() {
                return ${if (i == 0) "1" else s"""${im.getType("java.util.Objects")}.hash(${(1 to i).gen(j => s"_$j")(", ")})"""};
            }

            @Override
            public String toString() {
                return ${if (i == 0) "\"()\"" else s""""(" + ${(1 to i).gen(j => s"_$j")(" + \", \" + ")} + ")""""};
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

      val Map = im.getType("java.util.Map")
      val Objects = im.getType("java.util.Objects")
      val Seq = im.getType("io.vavr.collection.Seq")

      def genFactoryMethod(i: Int) = {
        val generics = (1 to i).gen(j => s"T$j")(", ")
        val paramsDecl = (1 to i).gen(j => s"T$j t$j")(", ")
        val params = (1 to i).gen(j => s"t$j")(", ")
        val wideGenerics = (1 to i).gen(j => s"? extends T$j")(", ")
        xs"""
          /**
           * Creates a tuple of ${i.numerus("element")}.
           ${(0 to i).gen(j => if (j == 0) "*" else s"* @param <T$j> type of the ${j.ordinal} element")("\n")}
           ${(1 to i).gen(j => s"* @param t$j the ${j.ordinal} element")("\n")}
           * @return a tuple of ${i.numerus("element")}.
           */
          static <$generics> Tuple$i<$generics> of($paramsDecl) {
              return new Tuple$i<>($params);
          }
        """
      }

      def genSeqMethod(i: Int) = {
        val generics = (1 to i).gen(j => s"T$j")(", ")
        val seqs = (1 to i).gen(j => s"Seq<T$j>")(", ")
        val Stream = im.getType("io.vavr.collection.Stream")
        val widenedGenerics = (1 to i).gen(j => s"? extends T$j")(", ")
        xs"""
            /**
             * Turns a sequence of {@code Tuple$i} into a Tuple$i of {@code Seq}${(i > 1).gen("s")}.
             *
             ${(1 to i).gen(j => s"* @param <T$j> ${j.ordinal} component type")("\n")}
             * @param tuples an {@code Iterable} of tuples
             * @return a tuple of ${i.numerus(s"{@link $Seq}")}.
             */
            static <$generics> Tuple$i<$seqs> sequence$i(Iterable<? extends Tuple$i<$widenedGenerics>> tuples) {
                $Objects.requireNonNull(tuples, "tuples is null");
                final Stream<Tuple$i<$widenedGenerics>> s = $Stream.ofAll(tuples);
                return new Tuple$i<>(${(1 to i).gen(j => s"s.map(Tuple$i::_$j)")(s", ")});
            }
        """
      }

      def genNarrowMethod(i: Int) = {
        val generics = (1 to i).gen(j => s"T$j")(", ")
        val wideGenerics = (1 to i).gen(j => s"? extends T$j")(", ")
        xs"""
          /**
           * Narrows a widened {@code Tuple$i<$wideGenerics>} to {@code Tuple$i<$generics>}.
           * This is eligible because immutable/read-only tuples are covariant.
           * @param t A {@code Tuple$i}.
           ${(1 to i).gen(j => s"* @param <T$j> the ${j.ordinal} component type")("\n")}
           * @return the given {@code t} instance as narrowed type {@code Tuple$i<$generics>}.
           */
          @SuppressWarnings("unchecked")
          static <$generics> Tuple$i<$generics> narrow(Tuple$i<$wideGenerics> t) {
              return (Tuple$i<$generics>) t;
          }
        """
      }

      xs"""
        /**
         * The base interface of all tuples.
         *
         * @author Daniel Dietrich
         */
        public interface Tuple {

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
            static <T1, T2> Tuple2<T1, T2> fromEntry($Map.Entry<? extends T1, ? extends T2> entry) {
                $Objects.requireNonNull(entry, "entry is null");
                return new Tuple2<>(entry.getKey(), entry.getValue());
            }

            ${(1 to N).gen(genFactoryMethod)("\n\n")}

            ${(1 to N).gen(genSeqMethod)("\n\n")}

            ${(1 to N).gen(genNarrowMethod)("\n\n")}


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

    genVavrFile("io.vavr.collection", "ArrayType")((im: ImportManager, packageName: String, className: String) => xs"""
      import java.util.Collection;

      /**
       * Helper to replace reflective array access.
       *
       * @author Pap Lőrinc
       */
      interface ArrayType<T> {
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
          )("\n\n")}
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

/**
 * Generate Vavr src-gen/test/java classes
 */
def generateTestClasses(): Unit = {

  genAPITests()
  genFunctionTests()
  genTupleTests()

  /**
   * Generator of Function tests
   */
  def genAPITests(): Unit = {

    genVavrFile("io.vavr", s"APITest", baseDir = TARGET_TEST)((im: ImportManager, packageName, className) => {

      val assertThat = im.getStatic("org.assertj.core.api.Assertions.assertThat")
      val test = im.getType("org.junit.Test")

      val API = im.getType("io.vavr.API")
      val AssertionsExtensions = im.getType("io.vavr.AssertionsExtensions")
      val ListType = im.getType("io.vavr.collection.List")
      val MapType = im.getType("io.vavr.collection.Map")
      val OptionType = im.getType("io.vavr.control.Option")
      val FutureType = im.getType("io.vavr.concurrent.Future")
      val ExecutorServiceType = im.getType("java.util.concurrent.Executors")
      val ExecutorService = s"$ExecutorServiceType.newSingleThreadExecutor()"
      val TryType = im.getType("io.vavr.control.Try")
      val JavaComparatorType = im.getType("java.util.Comparator")

      val d = "$"

      im.getStatic("io.vavr.API.*")

      def genFutureTests(name: String, value: String, success: Boolean): String = {
        val check = if (success) "isSuccess" else "isFailure"
        xs"""
          @$test
          public void shouldFutureWith${name}ReturnNotNull() {
              final $FutureType<?> future = Future($value).await();
              assertThat(future).isNotNull();
              assertThat(future.$check()).isTrue();
          }

          @$test
          public void shouldFutureWithinExecutorWith${name}ReturnNotNull() {
              final $FutureType<?> future = Future($ExecutorService, $value).await();
              assertThat(future).isNotNull();
              assertThat(future.$check()).isTrue();
          }
        """
      }

      def genExtAliasTest(name: String, func: String, value: String, check: String): String = {
        xs"""
          @$test
          public void should$name() {
              assertThat($func($value)).$check;
          }
        """
      }

      def genMediumAliasTest(name: String, func: String, value: String): String = genExtAliasTest(s"${name}ReturnNotNull", func, value, "isNotNull()")

      def genSimpleAliasTest(name: String, value: String): String = genMediumAliasTest(name, name, value)

      def genTraversableTests(func: String): String = {
        xs"""
          ${genMediumAliasTest(s"Empty$func", func, "")}

          ${genMediumAliasTest(s"${func}WithSingle", func, "'1'")}

          ${genMediumAliasTest(s"${func}WithVarArg", func, "'1', '2', '3'")}

        """
      }

      def genSortedTraversableTests(func: String): String = {
        xs"""
          ${genMediumAliasTest(s"Empty$func", func, "")}

          ${genMediumAliasTest(s"Empty${func}WithComparator", func, s"($JavaComparatorType<Character>) Character::compareTo")}

          ${genMediumAliasTest(s"${func}WithSingle", func, "'1'")}

          ${genMediumAliasTest(s"${func}WithSingleAndComparator", func, "Character::compareTo, '1'")}

          ${genMediumAliasTest(s"${func}WithVarArg", func, "'1', '2', '3'")}

          ${genMediumAliasTest(s"${func}WithVarArgAndComparator", func, s"($JavaComparatorType<Character>) Character::compareTo, '1', '2', '3'")}

        """
      }

      def genMapTests(func: String): String = {
        xs"""
          ${genMediumAliasTest(s"Empty$func", func, "")}

          ${genMediumAliasTest(s"${func}FromSingle", func, "1, '1'")}

          ${genMediumAliasTest(s"${func}FromTuples", func, "Tuple(1, '1'), Tuple(2, '2'), Tuple(3, '3')")}

          ${genMediumAliasTest(s"${func}FromPairs", func, "1, '1', 2, '2', 3, '3'")}

          ${(1 to VARARGS).gen(i => {
            xs"""
              @$test
              public void shouldCreate${func}From${i}Pairs() {
                $MapType<Integer, Integer> map = $func(${(1 to i).gen(j => s"$j, ${j*2}")(", ")});
                ${(1 to i).gen(j => s"assertThat(map.apply($j)).isEqualTo(${j*2});")("\n")}
              }
            """
          })("\n\n")}

        """
      }

      def genTryTests(func: String, value: String, success: Boolean): String = {
        val check = if (success) "isSuccess" else "isFailure"
        xs"""
          @$test
          public void should${func.firstUpper}ReturnNotNull() {
              final $TryType<?> t = $func($value);
              assertThat(t).isNotNull();
              assertThat(t.$check()).isTrue();
          }
        """
      }

      def genAliasesTests(im: ImportManager, packageName: String, className: String): String = {
        xs"""
          ${(0 to N).gen(i => {
            val params = (1 to i).gen(j => s"v$j")(", ")
            xs"""
              @$test
              public void shouldFunction${i}ReturnNotNull() {
                  assertThat(Function(($params) -> null)).isNotNull();
              }

              @$test
              public void shouldCheckedFunction${i}ReturnNotNull() {
                  assertThat(CheckedFunction(($params) -> null)).isNotNull();
              }

            """
          })("\n\n")}

          ${(0 to N).gen(i => {
            val params = (1 to i).gen(j => s"v$j")(", ")
            genExtAliasTest(s"Unchecked${i}ReturnNonCheckedFunction", "unchecked", s"($params) -> null", s"isInstanceOf(Function$i.class)")
          })("\n\n")}

          ${(0 to N).gen(i => {
            val params = (1 to i).gen(j => s"$j")(", ")
            xs"""
              @$test
              public void shouldTuple${i}ReturnNotNull() {
                  assertThat(Tuple($params)).isNotNull();
              }

            """
          })("\n\n")}

          ${Seq("Right", "Left").gen(name => {
            xs"""
              @$test
              public void should${name}ReturnNotNull() {
                  assertThat($name(null)).isNotNull();
              }

            """
          })("\n\n")}

          ${genFutureTests("Supplier", "() -> 1", success = true)}

          ${genFutureTests("Value", "1", success = true)}

          ${genSimpleAliasTest("Lazy", "() -> 1")}

          ${genSimpleAliasTest("Option", "1")}

          ${genSimpleAliasTest("Some", "1")}

          ${genSimpleAliasTest("None", "")}

          ${genTryTests("Try", "() -> 1", success = true)}

          ${genTryTests("Success", "1", success = true)}

          ${genTryTests("Failure", "new Error()", success = false)}

          ${genSimpleAliasTest("Valid", "1")}

          ${genSimpleAliasTest("Invalid", "new Error()")}

          ${genMediumAliasTest("Char", "(Iterable<Character>) CharSeq", "'1'")}

          ${genMediumAliasTest("CharArray", "(Iterable<Character>) CharSeq", "'1', '2', '3'")}

          ${genMediumAliasTest("CharSeq", "(Iterable<Character>) CharSeq", "\"123\"")}

          ${genTraversableTests("Array")}
          ${genTraversableTests("Vector")}
          ${genTraversableTests("List")}
          ${genTraversableTests("Stream")}
          ${genTraversableTests("Queue")}
          ${genTraversableTests("LinkedSet")}
          ${genTraversableTests("Set")}
          ${genTraversableTests("Seq")}
          ${genTraversableTests("IndexedSeq")}

          ${genSortedTraversableTests("SortedSet")}
          ${genSortedTraversableTests("PriorityQueue")}

          ${genMapTests("LinkedMap")}
          ${genMapTests("Map")}
          ${genMapTests("SortedMap")}
          ${genMediumAliasTest("EmptySortedMapFromComparator", "SortedMap", "Integer::compareTo")}

          ${genMediumAliasTest("SortedMapFromSingleAndComparator", "SortedMap", s"($JavaComparatorType<Integer>)Integer::compareTo, 1, '1'")}

          ${genMediumAliasTest("SortedMapFromTuplesAndComparator", "SortedMap", s"($JavaComparatorType<Integer>)Integer::compareTo, Tuple(1, '1'), Tuple(2, '2'), Tuple(3, '3')")}
        """
      }

      def genShortcutsTests(im: ImportManager, packageName: String, className: String): String = {

        val fail = im.getStatic("org.junit.Assert.fail")

        xs"""
          @$test
          public void shouldCompileTODOAndThrowDefaultMessageAtRuntime() {
              try {
                  final String s = TODO();
                  $fail("TODO() should throw. s: " + s);
              } catch(NotImplementedError err) {
                  assertThat(err.getMessage()).isEqualTo("An implementation is missing.");
              }
          }

          @$test
          public void shouldCompileTODOAndThrowGivenMessageAtRuntime() {
              final String msg = "Don't try this in production!";
              try {
                  final String s = TODO(msg);
                  $fail("TODO(String) should throw. s: " + s);
              } catch(NotImplementedError err) {
                  assertThat(err.getMessage()).isEqualTo(msg);
              }
          }

          @$test
          public void shouldCallprint_Object() {
              print("ok");
          }

          @$test
          public void shouldCallprintf() {
              printf("%s", "ok");
          }

          @$test
          public void shouldCallprintln_Object() {
              println("ok");
          }

          @$test
          public void shouldCallprintln() {
              println();
          }
        """
      }

      xs"""
        public class $className {

            @$test
            public void shouldNotBeInstantiable() {
                $AssertionsExtensions.assertThat($API.class).isNotInstantiable();
            }

            // -- shortcuts

            ${genShortcutsTests(im, packageName, className)}

            //
            // Alias should return not null.
            // More specific test for each aliased class implemented in separate test class
            //

            ${genAliasesTests(im, packageName, className)}

            // -- run

            @$test
            public void shouldRunUnitAndReturnVoid() {
                int[] i = { 0 };
                @SuppressWarnings("unused")
                Void nothing = run(() -> i[0]++);
                $assertThat(i[0]).isEqualTo(1);
            }

            // -- For

            @$test
            public void shouldIterateFor1UsingSimpleYield() {
                final $ListType<Integer> list = List.of(1, 2, 3);
                final $ListType<Integer> actual = For(list).yield().toList();
                $assertThat(actual).isEqualTo(list);
            }

            ${(1 to N).gen(i => xs"""
              @$test
              public void shouldIterateFor$i() {
                  final $ListType<Integer> result = For(
                      ${(1 to i).gen(j => s"$ListType.of(1, 2, 3)")(",\n")}
                  ).yield(${(i > 1).gen("(")}${(1 to i).gen(j => s"i$j")(", ")}${(i > 1).gen(")")} -> ${(1 to i).gen(j => s"i$j")(" + ")}).toList();
                  $assertThat(result.length()).isEqualTo((int) Math.pow(3, $i));
                  $assertThat(result.head()).isEqualTo($i);
                  $assertThat(result.last()).isEqualTo(3 * $i);
              }
            """)("\n\n")}

            @$test
            public void shouldIterateNestedFor() {
                final $ListType<String> result =
                        For(${im.getType("java.util.Arrays")}.asList(1, 2), i ->
                                For(${im.getType("io.vavr.collection.CharSeq")}.of('a', 'b')).yield(c -> i + ":" + c)).toList();
                assertThat(result).isEqualTo($ListType.of("1:a", "1:b", "2:a", "2:b"));
            }

            // -- Match

            @$test
            public void shouldReturnSomeWhenApplyingCaseGivenPredicateAndSupplier() {
                final Match.Case<Object, Integer> _case = Case($$(ignored -> true), ignored -> 1);
                assertThat(_case.isDefinedAt(null)).isTrue();
                assertThat(_case.apply(null)).isEqualTo(1);
            }

            @$test
            public void shouldReturnNoneWhenApplyingCaseGivenPredicateAndSupplier() {
                assertThat(Case($$(ignored -> false), ignored -> 1).isDefinedAt(null)).isFalse();
            }

            @$test
            public void shouldReturnSomeWhenApplyingCaseGivenPredicateAndValue() {
                final Match.Case<Object, Integer> _case = Case($$(ignored -> true), 1);
                assertThat(_case.isDefinedAt(null)).isTrue();
                assertThat(_case.apply(null)).isEqualTo(1);
            }

            @$test
            public void shouldReturnNoneWhenApplyingCaseGivenPredicateAndValue() {
                assertThat(Case($$(ignored -> false), 1).isDefinedAt(null)).isFalse();
            }

            // -- Match patterns

            static class ClzMatch {}
            static class ClzMatch1 extends ClzMatch {}
            static class ClzMatch2 extends ClzMatch {}

            ${(1 to N).gen(i => {

              im.getStatic("io.vavr.API.*")
              im.getStatic("io.vavr.Patterns.*")

              xs"""
                @$test
                public void shouldMatchPattern$i() {
                    final Tuple$i<${(1 to i).gen(j => s"Integer")(", ")}> tuple = Tuple.of(${(1 to i).gen(j => s"1")(", ")});
                    final String func = Match(tuple).of(
                            Case($$Tuple$i($d(0)${(2 to i).gen(j => s", $d()")}), (${(1 to i).gen(j => s"m$j")(", ")}) -> "fail"),
                            Case($$Tuple$i(${(1 to i).gen(j => s"$d()")(", ")}), (${(1 to i).gen(j => s"m$j")(", ")}) -> "okFunc")
                    );
                    assertThat(func).isEqualTo("okFunc");
                    final String supp = Match(tuple).of(
                            Case($$Tuple$i($d(0)${(2 to i).gen(j => s", $d()")}), () -> "fail"),
                            Case($$Tuple$i(${(1 to i).gen(j => s"$d()")(", ")}), () -> "okSupp")
                    );
                    assertThat(supp).isEqualTo("okSupp");
                    final String val = Match(tuple).of(
                            Case($$Tuple$i($d(0)${(2 to i).gen(j => s", $d()")}), "fail"),
                            Case($$Tuple$i(${(1 to i).gen(j => s"$d()")(", ")}), "okVal")
                    );
                    assertThat(val).isEqualTo("okVal");

                    final ClzMatch c = new ClzMatch2();
                    final String match = Match(c).of(
                            Case(Match.Pattern$i.of(ClzMatch1.class, ${(1 to i).gen(j => s"$d()")(", ")}, t -> Tuple.of(${(1 to i).gen(j => s"null")(", ")})), "fail"),
                            Case(Match.Pattern$i.of(ClzMatch2.class, ${(1 to i).gen(j => s"$d()")(", ")}, t -> Tuple.of(${(1 to i).gen(j => s"null")(", ")})), "okMatch")
                    );
                    assertThat(match).isEqualTo("okMatch");
                }
              """
            })("\n\n")}
        }
      """
    })
  }

  /**
   * Generator of Function tests
   */
  def genFunctionTests(): Unit = {

    (0 to N).foreach(i => {

      genVavrFile("io.vavr", s"CheckedFunction${i}Test", baseDir = TARGET_TEST)(genFunctionTest("CheckedFunction", checked = true))
      genVavrFile("io.vavr", s"Function${i}Test", baseDir = TARGET_TEST)(genFunctionTest("Function", checked = false))

      def genFunctionTest(name: String, checked: Boolean)(im: ImportManager, packageName: String, className: String): String = {

        val AtomicInteger = im.getType("java.util.concurrent.atomic.AtomicInteger")

        val functionArgsDecl = (1 to i).gen(j => s"Object o$j")(", ")
        val functionArgs = (1 to i).gen(j => s"o$j")(", ")
        val generics = (1 to i + 1).gen(j => "Object")(", ")

        val test = im.getType("org.junit.Test")
        val assertThat = im.getStatic("org.assertj.core.api.Assertions.assertThat")
        val recFuncF1 = if (i == 0) "11;" else s"i1 <= 0 ? i1 : $className.recurrent2.apply(${(1 to i).gen(j => s"i$j" + (j == 1).gen(s" - 1"))(", ")}) + 1;"

        def curriedType(max: Int, function: String): String = max match {
          case 0 => s"${function}0<Object>"
          case 1 => s"${function}1<Object, Object>"
          case _ => s"Function1<Object, ${curriedType(max - 1, function)}>"
        }

        val wideGenericArgs = (1 to i).gen(j => "Number")(", ")
        val wideGenericResult = "String"
        val wideFunctionPattern = (1 to i).gen(j => "%s")(", ")
        val narrowGenericArgs = (1 to i).gen(j => "Integer")(", ")
        val narrowGenericResult = im.getType("java.lang.CharSequence")
        val narrowArgs = (1 to i).gen(j => j.toString)(", ")

        xs"""
          public class $className {

              @$test
              public void shouldCreateFromMethodReference() {
                  class Type {
                      Object methodReference($functionArgsDecl) {
                          return null;
                      }
                  }
                  final Type type = new Type();
                  assertThat($name$i.of(type::methodReference)).isNotNull();
              }

              @$test
              public void shouldLiftPartialFunction() {
                  assertThat($name$i.lift(($functionArgs) -> { while(true); })).isNotNull();
              }

              ${(i == 1).gen(xs"""
                @$test
                public void shouldCreateIdentityFunction()${checked.gen(" throws Throwable")} {
                    final $name$i<String, String> identity = $name$i.identity();
                    final String s = "test";
                    assertThat(identity.apply(s)).isEqualTo(s);
                }
              """)}

              ${(i == 0 && !checked).gen(xs"""
                @$test
                public void shouldGetValue()${checked.gen(" throws Throwable")} {
                    final String s = "test";
                    final ${name}0<String> supplier = () -> s;
                    assertThat(supplier.get()).isEqualTo(s);
                }
              """)}

              ${(i > 1).gen(xs"""
                @$test
                public void shouldPartiallyApply()${checked.gen(" throws Throwable")} {
                    final $name$i<$generics> f = ($functionArgs) -> null;
                    ${(1 until i).gen(j => {
                      val partialArgs = (1 to j).gen(k => "null")(", ")
                      s"$assertThat(f.apply($partialArgs)).isNotNull();"
                    })("\n")}
                }
              """)}

              @$test
              public void shouldGetArity() {
                  final $name$i<$generics> f = ($functionArgs) -> null;
                  $assertThat(f.arity()).isEqualTo($i);
              }

              @$test
              public void shouldConstant()${checked.gen(" throws Throwable")} {
                  final $name$i<$generics> f = $name$i.constant(6);
                  $assertThat(f.apply(${(1 to i).gen(j => s"$j")(", ")})).isEqualTo(6);
              }

              @$test
              public void shouldCurry() {
                  final $name$i<$generics> f = ($functionArgs) -> null;
                  final ${curriedType(i, name)} curried = f.curried();
                  $assertThat(curried).isNotNull();
              }

              @$test
              public void shouldTuple() {
                  final $name$i<$generics> f = ($functionArgs) -> null;
                  final ${name}1<Tuple$i${(i > 0).gen(s"<${(1 to i).gen(j => "Object")(", ")}>")}, Object> tupled = f.tupled();
                  $assertThat(tupled).isNotNull();
              }

              @$test
              public void shouldReverse() {
                  final $name$i<$generics> f = ($functionArgs) -> null;
                  $assertThat(f.reversed()).isNotNull();
              }

              @$test
              public void shouldMemoize()${checked.gen(" throws Throwable")} {
                  final $AtomicInteger integer = new $AtomicInteger();
                  final $name$i<${(1 to i + 1).gen(j => "Integer")(", ")}> f = (${(1 to i).gen(j => s"i$j")(", ")}) -> ${(1 to i).gen(j => s"i$j")(" + ")}${(i > 0).gen(" + ")}integer.getAndIncrement();
                  final $name$i<${(1 to i + 1).gen(j => "Integer")(", ")}> memo = f.memoized();
                  // should apply f on first apply()
                  final int expected = memo.apply(${(1 to i).gen(j => s"$j")(", ")});
                  // should return memoized value of second apply()
                  $assertThat(memo.apply(${(1 to i).gen(j => s"$j")(", ")})).isEqualTo(expected);
                  ${(i > 0).gen(xs"""
                    // should calculate new values when called subsequently with different parameters
                    $assertThat(memo.apply(${(1 to i).gen(j => s"${j + 1} ")(", ")})).isEqualTo(${(1 to i).gen(j => s"${j + 1} ")(" + ")} + 1);
                    // should return memoized value of second apply() (for new value)
                    $assertThat(memo.apply(${(1 to i).gen(j => s"${j + 1} ")(", ")})).isEqualTo(${(1 to i).gen(j => s"${j + 1} ")(" + ")} + 1);
                  """)}
              }

              @$test
              public void shouldNotMemoizeAlreadyMemoizedFunction()${checked.gen(" throws Throwable")} {
                  final $name$i<${(1 to i + 1).gen(j => "Integer")(", ")}> f = (${(1 to i).gen(j => s"i$j")(", ")}) -> null;
                  final $name$i<${(1 to i + 1).gen(j => "Integer")(", ")}> memo = f.memoized();
                  $assertThat(memo.memoized() == memo).isTrue();
              }

              ${(i > 0).gen(xs"""
                @$test
                public void shouldMemoizeValueGivenNullArguments()${checked.gen(" throws Throwable")} {
                    final $name$i<${(1 to i + 1).gen(j => "Integer")(", ")}> f = (${(1 to i).gen(j => s"i$j")(", ")}) -> null;
                    final $name$i<${(1 to i + 1).gen(j => "Integer")(", ")}> memo = f.memoized();
                    $assertThat(memo.apply(${(1 to i).gen(j => "null")(", ")})).isNull();
                }
              """)}

              @$test
              public void shouldRecognizeMemoizedFunctions() {
                  final $name$i<${(1 to i + 1).gen(j => "Integer")(", ")}> f = (${(1 to i).gen(j => s"i$j")(", ")}) -> null;
                  final $name$i<${(1 to i + 1).gen(j => "Integer")(", ")}> memo = f.memoized();
                  $assertThat(f.isMemoized()).isFalse();
                  $assertThat(memo.isMemoized()).isTrue();
              }

              ${(i == 1 && !checked).gen({
                val assertThatThrownBy = im.getStatic("org.assertj.core.api.Assertions.assertThatThrownBy")
                xs"""
                  @$test
                  public void shouldThrowOnPartialWithNullPredicate() {
                      final Function1<Integer, String> f = String::valueOf;
                      $assertThatThrownBy(() -> f.partial(null))
                              .isInstanceOf(NullPointerException.class)
                              .hasMessage("isDefinedAt is null");
                  }

                  @$test
                  public void shouldCreatePartialFunction() {
                      final Function1<Integer, String> f = String::valueOf;
                      final PartialFunction<Integer, String> pf = f.partial(i -> i % 2 == 0);
                      assertThat(pf.isDefinedAt(0)).isTrue();
                      assertThat(pf.isDefinedAt(1)).isFalse();
                      assertThat(pf.apply(0)).isEqualTo("0");
                      assertThat(pf.apply(1)).isEqualTo("1"); // it is valid to return a value, even if isDefinedAt returns false
                  }
                """})}

              ${(!checked).gen(xs"""
                @$test
                public void shouldLiftTryPartialFunction() {
                    $AtomicInteger integer = new $AtomicInteger();
                    $name$i<${(1 to i + 1).gen(j => "Integer")(", ")}> divByZero = (${(1 to i).gen(j => s"i$j")(", ")}) -> 10 / integer.get();
                    $name$i<${(1 to i).gen(j => "Integer, ")("")}Try<Integer>> divByZeroTry = $name$i.liftTry(divByZero);

                    ${im.getType("io.vavr.control.Try")}<Integer> res = divByZeroTry.apply(${(1 to i).gen(j => s"0")(", ")});
                    assertThat(res.isFailure()).isTrue();
                    assertThat(res.getCause()).isNotNull();
                    assertThat(res.getCause().getMessage()).isEqualToIgnoringCase("/ by zero");

                    integer.incrementAndGet();
                    res = divByZeroTry.apply(${(1 to i).mkString(", ")});
                    assertThat(res.isSuccess()).isTrue();
                    assertThat(res.get()).isEqualTo(10);
                }
              """)}

              ${checked.gen(xs"""
                ${(i == 0).gen(xs"""
                  @$test
                  public void shouldRecover() {
                      final $AtomicInteger integer = new $AtomicInteger();
                      $name$i<MessageDigest> digest = () -> ${im.getType("java.security.MessageDigest")}.getInstance(integer.get() == 0 ? "MD5" : "Unknown");
                      Function$i<MessageDigest> recover = digest.recover(throwable -> () -> null);
                      MessageDigest md5 = recover.apply();
                      assertThat(md5).isNotNull();
                      assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
                      assertThat(md5.getDigestLength()).isEqualTo(16);
                      integer.incrementAndGet();
                      assertThat(recover.apply()).isNull();
                  }

                  @$test
                  public void shouldRecoverNonNull() {
                      final $AtomicInteger integer = new $AtomicInteger();
                      $name$i<MessageDigest> digest = () -> ${im.getType("java.security.MessageDigest")}.getInstance(integer.get() == 0 ? "MD5" : "Unknown");
                      Function$i<MessageDigest> recover = digest.recover(throwable -> null);

                      MessageDigest md5 = recover.apply();
                      assertThat(md5).isNotNull();
                      assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
                      assertThat(md5.getDigestLength()).isEqualTo(16);

                      integer.incrementAndGet();
                      ${im.getType("io.vavr.control.Try")}<MessageDigest> unknown = Function$i.liftTry(recover).apply();
                      assertThat(unknown).isNotNull();
                      assertThat(unknown.isFailure()).isTrue();
                      assertThat(unknown.getCause()).isNotNull().isInstanceOf(NullPointerException.class);
                      assertThat(unknown.getCause().getMessage()).isNotEmpty().isEqualToIgnoringCase("recover return null for class java.security.NoSuchAlgorithmException: Unknown MessageDigest not available");
                  }

                  @$test
                  public void shouldUncheckedWork() {
                      $name$i<MessageDigest> digest = () -> ${im.getType("java.security.MessageDigest")}.getInstance("MD5");
                      Function$i<MessageDigest> unchecked = digest.unchecked();
                      MessageDigest md5 = unchecked.apply();
                      assertThat(md5).isNotNull();
                      assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
                      assertThat(md5.getDigestLength()).isEqualTo(16);
                  }

                  @$test(expected = ${im.getType("java.security.NoSuchAlgorithmException")}.class)
                  public void shouldThrowCheckedExceptionWhenUnchecked() {
                      $name$i<MessageDigest> digest = () -> ${im.getType("java.security.MessageDigest")}.getInstance("Unknown");
                      Function$i<MessageDigest> unchecked = digest.unchecked();
                      unchecked.apply(); // Look ma, we throw an undeclared checked exception!
                  }

                  @$test
                  public void shouldLiftTryPartialFunction() {
                      final $AtomicInteger integer = new $AtomicInteger();
                      $name$i<MessageDigest> digest = () -> ${im.getType("java.security.MessageDigest")}.getInstance(integer.get() == 0 ? "MD5" : "Unknown");
                      Function$i<Try<MessageDigest>> liftTry = $name$i.liftTry(digest);
                      ${im.getType("io.vavr.control.Try")}<MessageDigest> md5 = liftTry.apply();
                      assertThat(md5.isSuccess()).isTrue();
                      assertThat(md5.get()).isNotNull();
                      assertThat(md5.get().getAlgorithm()).isEqualToIgnoringCase("MD5");
                      assertThat(md5.get().getDigestLength()).isEqualTo(16);

                      integer.incrementAndGet();
                      ${im.getType("io.vavr.control.Try")}<MessageDigest> unknown = liftTry.apply();
                      assertThat(unknown.isFailure()).isTrue();
                      assertThat(unknown.getCause()).isNotNull();
                      assertThat(unknown.getCause().getMessage()).isEqualToIgnoringCase("Unknown MessageDigest not available");
                  }
                """)}
                ${(i > 0).gen(xs"""
                  ${
                    val types = s"<${(1 to i).gen(j => "String")(", ")}, MessageDigest>"
                    def toArgList (s: String) = s.split("", i).mkString("\"", "\", \"", "\"") + (s.length + 2 to i).gen(j => ", \"\"")
                    xs"""

                      private static final $name$i$types digest = (${(1 to i).gen(j => s"s$j")(", ")}) -> ${im.getType("java.security.MessageDigest")}.getInstance(${(1 to i).gen(j => s"s$j")(" + ")});

                      @$test
                      public void shouldRecover() {
                          final Function$i<${(1 to i).gen(j => "String")(", ")}, MessageDigest> recover = digest.recover(throwable -> (${(1 to i).gen(j => s"s$j")(", ")}) -> null);
                          final MessageDigest md5 = recover.apply(${toArgList("MD5")});
                          assertThat(md5).isNotNull();
                          assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
                          assertThat(md5.getDigestLength()).isEqualTo(16);
                          assertThat(recover.apply(${toArgList("Unknown")})).isNull();
                      }

                      @$test
                      public void shouldRecoverNonNull() {
                          final Function$i<${(1 to i).gen(j => "String")(", ")}, MessageDigest> recover = digest.recover(throwable -> null);
                          final MessageDigest md5 = recover.apply(${toArgList("MD5")});
                          assertThat(md5).isNotNull();
                          assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
                          assertThat(md5.getDigestLength()).isEqualTo(16);
                          final ${im.getType("io.vavr.control.Try")}<MessageDigest> unknown = Function$i.liftTry(recover).apply(${toArgList("Unknown")});
                          assertThat(unknown).isNotNull();
                          assertThat(unknown.isFailure()).isTrue();
                          assertThat(unknown.getCause()).isNotNull().isInstanceOf(NullPointerException.class);
                          assertThat(unknown.getCause().getMessage()).isNotEmpty().isEqualToIgnoringCase("recover return null for class java.security.NoSuchAlgorithmException: Unknown MessageDigest not available");
                      }

                      @$test
                      public void shouldUncheckedWork() {
                          final Function$i<${(1 to i).gen(j => "String")(", ")}, MessageDigest> unchecked = digest.unchecked();
                          final MessageDigest md5 = unchecked.apply(${toArgList("MD5")});
                          assertThat(md5).isNotNull();
                          assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
                          assertThat(md5.getDigestLength()).isEqualTo(16);
                      }

                      @$test(expected = ${im.getType("java.security.NoSuchAlgorithmException")}.class)
                      public void shouldUncheckedThrowIllegalState() {
                          final Function$i<${(1 to i).gen(j => "String")(", ")}, MessageDigest> unchecked = digest.unchecked();
                          unchecked.apply(${toArgList("Unknown")}); // Look ma, we throw an undeclared checked exception!
                      }

                      @$test
                      public void shouldLiftTryPartialFunction() {
                          final Function$i<${(1 to i).gen(j => "String")(", ")}, Try<MessageDigest>> liftTry = $name$i.liftTry(digest);
                          final ${im.getType("io.vavr.control.Try")}<MessageDigest> md5 = liftTry.apply(${toArgList("MD5")});
                          assertThat(md5.isSuccess()).isTrue();
                          assertThat(md5.get()).isNotNull();
                          assertThat(md5.get().getAlgorithm()).isEqualToIgnoringCase("MD5");
                          assertThat(md5.get().getDigestLength()).isEqualTo(16);
                          final ${im.getType("io.vavr.control.Try")}<MessageDigest> unknown = liftTry.apply(${toArgList("Unknown")});
                          assertThat(unknown.isFailure()).isTrue();
                          assertThat(unknown.getCause()).isNotNull();
                          assertThat(unknown.getCause().getMessage()).isEqualToIgnoringCase("Unknown MessageDigest not available");
                      }
                    """
                  }
                """)}
              """)}

              private static final $name$i<${(1 to i + 1).gen(j => "Integer")(", ")}> recurrent1 = (${(1 to i).gen(j => s"i$j")(", ")}) -> $recFuncF1
              ${(i > 0).gen(xs"""
                private static final $name$i<${(1 to i + 1).gen(j => "Integer")(", ")}> recurrent2 = $className.recurrent1.memoized();
              """)}

              @$test
              public void shouldCalculatedRecursively()${checked.gen(" throws Throwable")} {
                  assertThat(recurrent1.apply(${(1 to i).gen(j => "11")(", ")})).isEqualTo(11);
                  ${(i > 0).gen(s"assertThat(recurrent1.apply(${(1 to i).gen(j => "22")(", ")})).isEqualTo(22);")}
              }

              @$test
              public void shouldComposeWithAndThen() {
                  final $name$i<$generics> f = ($functionArgs) -> null;
                  final ${name}1<Object, Object> after = o -> null;
                  final $name$i<$generics> composed = f.andThen(after);
                  $assertThat(composed).isNotNull();
              }

              ${(i == 1).gen(xs"""
                @$test
                public void shouldComposeWithCompose() {
                    final $name$i<$generics> f = ($functionArgs) -> null;
                    final ${name}1<Object, Object> before = o -> null;
                    final $name$i<$generics> composed = f.compose(before);
                    $assertThat(composed).isNotNull();
                }
              """)}

              ${(i == 0).gen(xs"""
              @$test
              public void shouldNarrow()${checked.gen(" throws Throwable")}{
                  final $name$i<$wideGenericResult> wideFunction = () -> "Zero args";
                  final $name$i<$narrowGenericResult> narrowFunction = $name$i.narrow(wideFunction);

                  $assertThat(narrowFunction.apply()).isEqualTo("Zero args");
              }
              """)}

              ${(i > 0).gen(xs"""
              @$test
              public void shouldNarrow()${checked.gen(" throws Throwable")}{
                  final $name$i<$wideGenericArgs, $wideGenericResult> wideFunction = ($functionArgs) -> String.format("Numbers are: $wideFunctionPattern", $functionArgs);
                  final $name$i<$narrowGenericArgs, $narrowGenericResult> narrowFunction = $name$i.narrow(wideFunction);

                  $assertThat(narrowFunction.apply($narrowArgs)).isEqualTo("Numbers are: $narrowArgs");
              }
              """)}
          }
        """
      }
    })
  }

  /**
   * Generator of Tuple tests
   */
  def genTupleTests(): Unit = {

    def genArgsForComparing(digits: Int, p: Int): String = {
      (1 to digits).gen(i => if(i == p) "1" else "0")(", ")
    }

    (0 to N).foreach(i => {

      genVavrFile("io.vavr", s"Tuple${i}Test", baseDir = TARGET_TEST)((im: ImportManager, packageName, className) => {

        val test = im.getType("org.junit.Test")
        val seq = im.getType("io.vavr.collection.Seq")
        val list = im.getType("io.vavr.collection.List")
        val stream = if (i == 0) "" else im.getType("io.vavr.collection.Stream")
        val comparator = im.getType("java.util.Comparator")
        val assertThat = im.getStatic("org.assertj.core.api.Assertions.assertThat")
        val generics = if (i == 0) "" else s"<${(1 to i).gen(j => s"Object")(", ")}>"
        val intGenerics = if (i == 0) "" else s"<${(1 to i).gen(j => s"Integer")(", ")}>"
        val functionArgs = if (i == 0) "()" else s"${(i > 1).gen("(") + (1 to i).gen(j => s"o$j")(", ") + (i > 1).gen(")")}"
        val nullArgs = (1 to i).gen(j => "null")(", ")
        if(i==2){
          im.getType("java.util.AbstractMap")
          im.getType("java.util.Map")
        }


        xs"""
          public class Tuple${i}Test {

              @$test
              public void shouldCreateTuple() {
                  final Tuple$i$generics tuple = createTuple();
                  $assertThat(tuple).isNotNull();
              }

              @$test
              public void shouldGetArity() {
                  final Tuple$i$generics tuple = createTuple();
                  $assertThat(tuple.arity()).isEqualTo($i);
              }

              ${(i > 0).gen(xs"""
                @$test
                public void shouldReturnElements() {
                    final Tuple$i$intGenerics tuple = createIntTuple(${(1 to i).gen(j => s"$j")(", ")});
                    ${(1 to i).gen(j => s"$assertThat(tuple._$j).isEqualTo($j);\n")}
                }
              """)}

              ${(1 to i).gen(j =>
                xs"""
                  @$test
                  public void shouldUpdate$j() {
                    final Tuple$i$intGenerics tuple = createIntTuple(${(1 to i).gen(j => s"$j")(", ")}).update$j(42);
                    ${(1 to i).gen(k => s"$assertThat(tuple._$k).isEqualTo(${if (j == k) 42 else k});\n")}
                  }
                """)("\n\n")}

              @$test
              public void shouldConvertToSeq() {
                  final $seq<?> actual = createIntTuple(${genArgsForComparing(i, 1)}).toSeq();
                  $assertThat(actual).isEqualTo($list.of(${genArgsForComparing(i, 1)}));
              }

              @$test
              public void shouldCompareEqual() {
                  final Tuple$i$intGenerics t0 = createIntTuple(${genArgsForComparing(i, 0)});
                  $assertThat(t0.compareTo(t0)).isZero();
                  $assertThat(intTupleComparator.compare(t0, t0)).isZero();
              }

              ${(1 to i).gen(j => xs"""
                @$test
                public void shouldCompare${j.ordinal}Arg() {
                    final Tuple$i$intGenerics t0 = createIntTuple(${genArgsForComparing(i, 0)});
                    final Tuple$i$intGenerics t$j = createIntTuple(${genArgsForComparing(i, j)});
                    $assertThat(t0.compareTo(t$j)).isNegative();
                    $assertThat(t$j.compareTo(t0)).isPositive();
                    $assertThat(intTupleComparator.compare(t0, t$j)).isNegative();
                    $assertThat(intTupleComparator.compare(t$j, t0)).isPositive();
                }
              """)("\n\n")}

              ${(i == 2).gen(xs"""
                @$test
                public void shouldSwap() {
                    $assertThat(createIntTuple(1, 2).swap()).isEqualTo(createIntTuple(2, 1));
                }

                @$test
                public void shouldConvertToEntry() {
                    Tuple$i$intGenerics tuple = createIntTuple(1,2);
                    Map.Entry$intGenerics entry = new AbstractMap.SimpleEntry<>(1, 2);
                    assertThat(tuple.toEntry().equals(entry));
                }

              """)}

              ${(i > 0).gen(xs"""
                @$test
                public void shouldMap() {
                    final Tuple$i$generics tuple = createTuple();
                    ${if (i == 1) xs"""
                      final Tuple$i$generics actual = tuple.map(o -> o);
                      $assertThat(actual).isEqualTo(tuple);
                    """ else xs"""
                      final Tuple$i$generics actual = tuple.map($functionArgs -> tuple);
                      $assertThat(actual).isEqualTo(tuple);
                    """}
                }

                @$test
                public void shouldMapComponents() {
                  final Tuple$i$generics tuple = createTuple();
                  ${(1 to i).gen(j => xs"""final Function1<Object, Object> f$j = Function1.identity();""")("\n")}
                  final Tuple$i$generics actual = tuple.map(${(1 to i).gen(j => s"f$j")(", ")});
                  $assertThat(actual).isEqualTo(tuple);
                }

                @$test
                public void shouldReturnTuple${i}OfSequence$i() {
                  final $seq<Tuple$i<${(1 to i).gen(j => xs"Integer")(", ")}>> iterable = $list.of(${(1 to i).gen(j => xs"Tuple.of(${(1 to i).gen(k => xs"${k+2*j-1}")(", ")})")(", ")});
                  final Tuple$i<${(1 to i).gen(j => xs"$seq<Integer>")(", ")}> expected = Tuple.of(${(1 to i).gen(j => xs"$stream.of(${(1 to i).gen(k => xs"${2*k+j-1}")(", ")})")(", ")});
                  $assertThat(Tuple.sequence$i(iterable)).isEqualTo(expected);
                }
              """)}

              ${(i > 1).gen(xs"""
                @$test
                public void shouldReturnTuple${i}OfSequence1() {
                  final $seq<Tuple$i<${(1 to i).gen(j => xs"Integer")(", ")}>> iterable = $list.of(Tuple.of(${(1 to i).gen(k => xs"$k")(", ")}));
                  final Tuple$i<${(1 to i).gen(j => xs"$seq<Integer>")(", ")}> expected = Tuple.of(${(1 to i).gen(j => xs"$stream.of($j)")(", ")});
                  $assertThat(Tuple.sequence$i(iterable)).isEqualTo(expected);
                }
              """)}

              ${(i > 1) gen (1 to i).gen(j => {
                val substitutedResultTypes = if (i == 0) "" else s"<${(1 to i).gen(k => if (k == j) "String" else "Integer")(", ")}>"
                val ones = (1 to i).gen(_ => "1")(", ")
                val result = (1 to i).gen(k => if (k == j) "\"X\"" else "1")(", ")
                xs"""
                  @$test
                  public void shouldMap${j.ordinal}Component() {
                    final Tuple$i$substitutedResultTypes actual = Tuple.of($ones).map$j(i -> "X");
                    final Tuple$i$substitutedResultTypes expected = Tuple.of($result);
                    assertThat(actual).isEqualTo(expected);
                  }
                """
              })("\n\n")}

              @$test
              public void shouldApplyTuple() {
                  final Tuple$i$generics tuple = createTuple();
                  final Tuple0 actual = tuple.apply($functionArgs -> Tuple0.instance());
                  assertThat(actual).isEqualTo(Tuple0.instance());
              }

              @$test
              public void shouldRecognizeEquality() {
                  final Tuple$i$generics tuple1 = createTuple();
                  final Tuple$i$generics tuple2 = createTuple();
                  $assertThat((Object) tuple1).isEqualTo(tuple2);
              }

              @$test
              public void shouldRecognizeNonEquality() {
                  final Tuple$i$generics tuple = createTuple();
                  final Object other = new Object();
                  $assertThat(tuple).isNotEqualTo(other);
              }

              ${(i > 0).gen(xs"""
                @$test
                public void shouldRecognizeNonEqualityPerComponent() {
                    final Tuple$i<${(1 to i).gen(_ => "String")(", ")}> tuple = Tuple.of(${(1 to i).gen(j => "\"" + j + "\"")(", ")});
                    ${(1 to i).gen(j => {
                      val that = "Tuple.of(" + (1 to i).gen(k => if (j == k) "\"X\"" else "\"" + k + "\"")(", ") + ")"
                      s"$assertThat(tuple.equals($that)).isFalse();"
                    })("\n")}
                }
              """)}

              @$test
              public void shouldComputeCorrectHashCode() {
                  final int actual = createTuple().hashCode();
                  final int expected = ${im.getType("java.util.Objects")}.hash(${if (i == 1) "new Object[] { null }" else nullArgs});
                  $assertThat(actual).isEqualTo(expected);
              }

              @$test
              public void shouldImplementToString() {
                  final String actual = createTuple().toString();
                  final String expected = "($nullArgs)";
                  $assertThat(actual).isEqualTo(expected);
              }

              private $comparator<Tuple$i$intGenerics> intTupleComparator = Tuple$i.comparator(${(1 to i).gen($j => s"Integer::compare")(", ")});

              private Tuple$i$generics createTuple() {
                  return ${if (i == 0) "Tuple0.instance()" else s"new Tuple$i<>($nullArgs)"};
              }

              private Tuple$i$intGenerics createIntTuple(${(1 to i).gen(j => s"Integer i$j")(", ")}) {
                  return ${if (i == 0) "Tuple0.instance()" else s"new Tuple$i<>(${(1 to i).gen(j => s"i$j")(", ")})"};
              }
          }
        """
      })
    })
  }
}

/**
 * Adds the Vavr header to generated classes.
 *
 * @param packageName Java package name
 * @param className Simple java class name
 * @param gen A generator which produces a String.
 */
def genVavrFile(packageName: String, className: String, baseDir: String = TARGET_MAIN)(gen: (ImportManager, String, String) => String, knownSimpleClassNames: List[String] = List()) =
  genJavaFile(baseDir, packageName, className)(xraw"""
    /*                        __    __  __  __    __  ___
     *                       \  \  /  /    \  \  /  /  __/
     *                        \  \/  /  /\  \  \/  /  /
     *                         \____/__/  \__\____/__/.ɪᴏ
     * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
     */
  """)(gen)(CHARSET)

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
     J A V A   G E N E R A T O R   F R A M E W O R K
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

object JavaGenerator {

  import java.nio.charset.{Charset, StandardCharsets}

  import Generator._

  /**
   * Generates a Java file.
   *
   * @param packageName Java package name
   * @param className Simple java class name
   * @param classHeader A class file header
   * @param gen A generator which produces a String.
   */
  def genJavaFile(baseDir: String, packageName: String, className: String)(classHeader: String)(gen: (ImportManager, String, String) => String, knownSimpleClassNames: List[String] = List())(implicit charset: Charset = StandardCharsets.UTF_8): Unit = {

    // DEV-NOTE: using File.separator instead of "/" does *not* work on windows!
    val dirName = packageName.replaceAll("[.]", "/")
    val fileName = className + ".java"
    val importManager = new ImportManager(packageName, knownSimpleClassNames)
    val classBody = gen.apply(importManager, packageName, className)

    genFile(baseDir, dirName, fileName)(xraw"""
      $classHeader
      package $packageName;

      /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
         G E N E R A T O R   C R A F T E D
      \*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

      ${importManager.getImports}

      $classBody
    """)
  }

  /**
   * A <em>stateful</em> ImportManager which generates an import section of a Java class file.
   *
   * @param packageNameOfClass package name of the generated class
   * @param knownSimpleClassNames a list of class names which may not be imported from other packages
   */
  class ImportManager(packageNameOfClass: String, knownSimpleClassNames: List[String], wildcardThreshold: Int = 5) {

    import scala.collection.mutable

    val nonStaticImports = new mutable.HashMap[String, String]
    val staticImports = new mutable.HashMap[String, String]

    def getType(fullQualifiedName: String): String = simplify(fullQualifiedName, nonStaticImports)

    def getStatic(fullQualifiedName: String): String = simplify(fullQualifiedName, staticImports)

    def getImports: String = {

      def optimizeImports(imports: Seq[String], static: Boolean): String = {
        val counts = imports.map(getPackageName).groupBy(s => s).map { case (s, list) => s -> list.length }
        val directImports = imports.filter(s => counts(getPackageName(s)) <= wildcardThreshold)
        val wildcardImports = counts.filter { case (_, count) => count > wildcardThreshold }.keySet.toIndexedSeq.map(s => s"$s.*")
        (directImports ++ wildcardImports).sorted.map(fqn => s"import ${static.gen("static ")}$fqn;").mkString("\n")
      }

      val staticImportSection = optimizeImports(staticImports.keySet.toIndexedSeq, static = true)
      val nonStaticImportSection = optimizeImports(nonStaticImports.keySet.toIndexedSeq, static = false)
      Seq(staticImportSection, nonStaticImportSection).mkString("\n\n")
    }

    private def simplify(fullQualifiedName: String, imports: mutable.HashMap[String, String]): String = {
      val simpleName = getSimpleName(fullQualifiedName)
      val packageName = getPackageName(fullQualifiedName)
      if (packageName.isEmpty && !packageNameOfClass.isEmpty) {
        throw new IllegalStateException(s"Can't import class '$simpleName' located in default package")
      } else if (packageName == packageNameOfClass) {
        simpleName
      } else if (imports.contains(fullQualifiedName)) {
        imports(fullQualifiedName)
      } else if (simpleName != "*" && (knownSimpleClassNames.contains(simpleName) || imports.values.exists(simpleName.equals(_)))) {
        fullQualifiedName
      } else {
        imports += fullQualifiedName -> simpleName
        simpleName
      }
    }

    private def getPackageName(fqn: String): String = fqn.substring(0, Math.max(fqn.lastIndexOf("."), 0))
    private def getSimpleName(fqn: String): String = fqn.substring(fqn.lastIndexOf(".") + 1)
  }
}

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
     C O R E   G E N E R A T O R   F R A M E W O R K
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

/**
 * Core generator API
 */
object Generator {

  import java.nio.charset.{Charset, StandardCharsets}
  import java.nio.file.{Files, Paths, StandardOpenOption}

  /**
   * Generates a file by writing string contents to the file system.
   *
   * @param baseDir The base directory, e.g. src-gen
   * @param dirName The directory relative to baseDir, e.g. main/java
   * @param fileName The file name within baseDir/dirName
   * @param createOption One of java.nio.file.{StandardOpenOption.CREATE_NEW, StandardOpenOption.CREATE}, default: CREATE_NEW
   * @param contents The string contents of the file
   * @param charset The charset, by default UTF-8
   */
  def genFile(baseDir: String, dirName: String, fileName: String, createOption: StandardOpenOption = StandardOpenOption.CREATE_NEW)(contents: => String)(implicit charset: Charset = StandardCharsets.UTF_8): Unit = {

    // println(s"Generating $dirName${File.separator}$fileName")

    Files.write(
      Files.createDirectories(Paths.get(baseDir, dirName)).resolve(fileName),
      contents.getBytes(charset),
      createOption, StandardOpenOption.WRITE)
  }

  implicit class IntExtensions(i: Int) {

    // returns i as ordinal, i.e. 1st, 2nd, 3rd, 4th, ...
    def ordinal: String =
      s"$i" + (if (i >= 4 && i <= 20) {
        "th"
      } else {
        i % 10 match {
          case 1 => "st"
          case 2 => "nd"
          case 3 => "rd"
          case _ => "th"
        }
      })

    // returns the grammatical number of a string, i.e. `i.numerus("name")` is
    // 0: "no name", 1: "one name", 2: "two names", 3: "three names", 4: "4 names", ...
    def numerus(noun: String): String = Math.abs(i) match {
      case 0 => s"no ${noun}s"
      case 1 => s"one $noun"
      case 2 => s"two ${noun}s"
      case 3 => s"three ${noun}s"
      case _ => s"$i ${noun}s"
    }

    // returns the a pluralized noun, e.g. 0: "names", 1: "name", -1: "name", 2: "names"
    def plural(noun: String): String = noun + (i != 1).gen("s")

  }

  implicit class StringExtensions(s: String) {

    // gets first char of s as string. throws if string is empty
    def first: String = s.substring(0, 1)

    // converts first char of s to upper case. throws if string is empty
    def firstUpper: String = s(0).toUpper + s.substring(1)

    // converts first char of s to lower case. throws if string is empty
    def firstLower: String = s(0).toLower + s.substring(1)
  }

  implicit class BooleanExtensions(condition: Boolean) {
    def gen(s: => String): String =  if (condition) s else ""
  }

  implicit class OptionExtensions(option: Option[Any]) {
    def gen(f: String => String): String =  option.map(any => f.apply(any.toString)).getOrElse("")
    def gen: String = option.map(any => any.toString).getOrElse("")
  }

  /**
   * Generates a String based on ints within a specific range.
   * {{{
   * (1 to 3).gen(i => s"x$i")(", ") // x1, x2, x3
   * (1 to 3).reverse.gen(i -> s"x$i")(", ") // x3, x2, x1
   * }}}
   *
   * @param range A Range
   */
  implicit class RangeExtensions(range: Range) {
    def gen(f: Int => String = String.valueOf)(implicit delimiter: String = ""): String =
      range map f mkString delimiter
  }

  /**
   * Generates a String based on an Iterable of objects. Objects are converted to Strings via toString.
   * {{{
   * // val a = "A"
   * // val b = "B"
   * // val c = "C"
   * Seq("a", "b", "c").gen(s => raw"""val $s = "${s.toUpperCase}"""")("\n")
   * }}}
   *
   * @param iterable An Interable
   */
  implicit class IterableExtensions(iterable: Iterable[Any]) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      iterable.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple1Extensions(tuple: Tuple1[Any]) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      f.apply(tuple._1.toString) mkString delimiter
  }

  implicit class Tuple2Extensions(tuple: (Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  /**
   * Generates a String based on a tuple of objects. Objects are converted to Strings via toString.
   * {{{
   * // val seq = Seq("a", "1", "true")
   * s"val seq = Seq(${("a", 1, true).gen(s => s""""$s"""")(", ")})"
   * }}}
   *
   * @param tuple A Tuple
   */
  implicit class Tuple3Extensions(tuple: (Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple4Extensions(tuple: (Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple5Extensions(tuple: (Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple6Extensions(tuple: (Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple7Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple8Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple9Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple10Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple11Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple12Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple13Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple14Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple15Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple16Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple17Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple18Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple19Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple20Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple21Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  implicit class Tuple22Extensions(tuple: (Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any)) {
    def gen(f: String => String = identity)(implicit delimiter: String = ""): String =
      tuple.productIterator.toList.map(x => f.apply(x.toString)) mkString delimiter
  }

  /**
   * Provides StringContext extensions, e.g. indentation of cascaded rich strings.
   *
   * @param sc Current StringContext
   * @see <a href="https://gist.github.com/danieldietrich/5174348">this gist</a>
   */
  implicit class StringContextExtensions(sc: StringContext) {

    import scala.util.Properties.lineSeparator

    /**
     * Formats escaped strings.
     *
     * @param args StringContext parts
     * @return An aligned String
     */
    def xs(args: Any*): String = align(sc.s, args)

    /**
     * Formats raw/unescaped strings.
     *
     * @param args StringContext parts
     * @return An aligned String
     */
    def xraw(args: Any*): String = align(sc.raw, args)

    /**
     * Indenting a rich string, removing first and last newline.
     * A rich string consists of arguments surrounded by text parts.
     */
    private def align(interpolator: Seq[Any] => String, args: Seq[Any]): String = {

      // indent embedded strings, invariant: parts.length = args.length + 1
      val indentedArgs = for {
        (part, arg) <- sc.parts zip args.map(s => if (s == null) "" else s.toString)
      } yield {
        // get the leading space of last line of current part
        val space = """([ \t]*)[^\s]*$""".r.findFirstMatchIn(part).map(_.group(1)).getOrElse("")
        // add this leading space to each line (except the first) of current arg
        arg.split("\r?\n") match {
          case lines: Array[String] if lines.nonEmpty => lines reduce (_ + lineSeparator + space + _)
          case whitespace => whitespace mkString ""
        }
      }

      // remove first and last newline and split string into separate lines
      // adding termination symbol \u0000 in order to preserve empty strings between last newlines when splitting
      val split = (interpolator(indentedArgs).replaceAll( """(^[ \t]*\r?\n)|(\r?\n[ \t]*$)""", "") + '\u0000').split("\r?\n")

      // find smallest indentation
      val prefix = split filter (!_.trim().isEmpty) map { s =>
        """^\s+""".r.findFirstIn(s).getOrElse("")
      } match {
        case prefixes: Array[String] if prefixes.length > 0 => prefixes reduce { (s1, s2) =>
          if (s1.length <= s2.length) s1 else s2
        }
        case _ => ""
      }

      // align all lines
      val aligned = split map { s =>
        if (s.startsWith(prefix)) s.substring(prefix.length) else s
      } mkString lineSeparator dropRight 1 // dropping termination character \u0000

      // combine multiple newlines to two
      aligned.replaceAll("""[ \t]*\r?\n ([ \t]*\r?\n)+""", lineSeparator * 2)
    }
  }
}
