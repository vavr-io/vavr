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
import JavaGenerator.ImportManager
import Config._

def genAPIShortcuts(im: ImportManager): String = {

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
     * <pre>{@code 
     * public HttpResponse getResponse(HttpRequest request) {
     *     return TODO();
     * }
     *
     * final HttpResponse response = getHttpResponse(TODO());
     * }</pre>
     *
     * @param <T> The result type of the missing implementation.
     * @return Nothing - this method always throws.
     * @throws NotImplementedError when this method is called
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
     * <pre>{@code 
     * public HttpResponse getResponse(HttpRequest request) {
     *     return TODO("fake response");
     * }
     *
     * final HttpResponse response = getHttpResponse(TODO("fake request"));
     * }</pre>
     *
     * @param msg An error message
     * @param <T> The result type of the missing implementation.
     * @return Nothing - this method always throws.
     * @throws NotImplementedError when this method is called
     * @see NotImplementedError#NotImplementedError(String)
     */
    public static <T> T TODO(String msg) {
        throw new NotImplementedError(msg);
    }

    /**
     * Shortcut for {@code System.out.print(obj)}. See {@link $PrintStreamType#print(Object)}.
     *
     * @param obj The {@code>Object} to be printed
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
    public static void printf(String format, Object... args) {
        System.out.printf(format, args);
    }

    /**
     * Shortcut for {@code System.out.println(obj)}. See {@link $PrintStreamType#println(Object)}.
     *
     * @param obj The {@code Object} to be printed
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

def genAPIAliases(im: ImportManager): String = {

  val NonNullType = im.getType("org.jspecify.annotations.NonNull")
  val OptionType = im.getType("io.vavr.control.Option")
  val EitherType = im.getType("io.vavr.control.Either")
  val FutureType = im.getType("io.vavr.concurrent.Future")
  val CheckedFunction0Type = im.getType("io.vavr.CheckedFunction0")
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
  val JavaComparatorType = im.getType("java.util.Comparator")
  val JavaMapType = im.getType("java.util.Map")
  val ExecutorType = im.getType("java.util.concurrent.Executor")
  val SupplierType = im.getType("java.util.function.Supplier")

  def genTraversableAliases(traversableType: String, returnType: String, name: String, sorted: Boolean = false) = {
    val bound = if (sorted) " extends Comparable<? super T>" else ""
    val ofLinkParam = if (sorted) "Comparable" else "Object"
    val emptyReturn = if (sorted) s"A new {@link $traversableType} empty instance" else s"A singleton instance of empty {@link $traversableType}"
    xs"""
    // -- $name

    /$javadoc
     * Alias for {@link $traversableType#empty()}
     *
     * @param <T> Component type of element.
     * @return $emptyReturn
     */
    public static <T$bound> $returnType<T> $name() {
        return $traversableType.empty();
    }

    ${if (sorted) xs"""
    /$javadoc
     * Alias for {@link $traversableType#empty($JavaComparatorType)}
     *
     * @param <T>        Component type of element.
     * @param comparator The comparator used to sort the elements
     * @return A new {@link $traversableType} empty instance
     */
    public static <T$bound> $returnType<T> $name(@NonNull $JavaComparatorType<? super T> comparator) {
        return $traversableType.empty(comparator);
    }
    """ else ""}

    /$javadoc
     * Alias for {@link $traversableType#of($ofLinkParam)}
     *
     * @param <T>     Component type of element.
     * @param element An element.
     * @return A new {@link $traversableType} instance containing the given element
     */
    public static <T$bound> $returnType<T> $name(T element) {
        return $traversableType.of(element);
    }

    ${if (sorted) xs"""
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
    """ else ""}

    ${if (sorted) xs"""
    /$javadoc
     * Alias for {@link $traversableType#of($ofLinkParam...)}
     *
     * @param <T>      Component type of element.
     * @param elements Zero or more elements.
     * @return A new {@link $traversableType} instance containing the given elements
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <T$bound> $returnType<T> $name(T @NonNull ... elements) {
        return $traversableType.of(elements);
    }
    """ else xs"""
    /$javadoc
     * Alias for {@link $traversableType#of($ofLinkParam...)}
     *
     * @param <T>      Component type of elements.
     * @param elements Zero or more elements.
     * @return A new {@link $traversableType} instance containing the given elements
     * @throws NullPointerException if {@code elements} is null
     */
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <T> $returnType<T> $name(T @NonNull ... elements) {
        return $traversableType.of(elements);
    }
    """}

    ${if (sorted) xs"""
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
    public static <T> $returnType<T> $name(@NonNull $JavaComparatorType<? super T> comparator, T @NonNull ... elements) {
        return $traversableType.of(comparator, elements);
    }
    """ else ""}
  """
  }

  def genMapAliases(mapType: String, returnType: String, name: String, sorted: Boolean = false) = {
    val keyBound = if (sorted) " extends Comparable<? super K>" else ""
    val ofLinkParam = if (sorted) "Comparable" else "Object"
    xs"""
    // -- $name

    /$javadoc
     * Alias for {@link $mapType#empty()}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @return ${if (sorted) s"A new empty {@link $mapType} instance" else s"A singleton instance of empty {@link $mapType}"}
     */
    public static <K$keyBound, V> $returnType<K, V> $name() {
        return $mapType.empty();
    }

    ${if (sorted) xs"""
    /$javadoc
     * Alias for {@link $mapType#empty($JavaComparatorType)}
     *
     * @param <K>           The key type.
     * @param <V>           The value type.
     * @param keyComparator The comparator used to sort the entries by their key
     * @return A new empty {@link $mapType} instance
     */
    public static <K, V> $returnType<K, V> $name(@NonNull $JavaComparatorType<? super K> keyComparator) {
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
    public static <K, V> $returnType<K, V> $name(@NonNull Comparator<? super K> keyComparator, K key, V value) {
        return $mapType.of(keyComparator, key, value);
    }
    """ else ""}

    /$javadoc
     * Alias for {@link $mapType#ofEntries(Tuple2...)}
     *
     * @param <K>     The key type.
     * @param <V>     The value type.
     * @param entries Map entries.
     * @return A new {@link $mapType} instance containing the given entries
     * @deprecated Will be removed in a future version.
     */
    @Deprecated
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <K$keyBound, V> $returnType<K, V> $name(Tuple2<? extends K, ? extends V> @NonNull ... entries) {
        return $mapType.ofEntries(entries);
    }

    ${if (sorted) xs"""
    /$javadoc
     * Alias for {@link $mapType#ofEntries($JavaComparatorType, Tuple2...)}
     *
     * @param <K>           The key type.
     * @param <V>           The value type.
     * @param keyComparator The comparator used to sort the entries by their key
     * @param entries       Map entries.
     * @return A new {@link $mapType} instance containing the given entry
     * @deprecated Will be removed in a future version.
     */
    @Deprecated
    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <K, V> $returnType<K, V> $name(@NonNull $JavaComparatorType<? super K> keyComparator, Tuple2<? extends K, ? extends V> @NonNull ... entries) {
        return $mapType.ofEntries(keyComparator, entries);
    }

    /$javadoc
     * Alias for {@link $mapType#ofAll($JavaMapType)}
     *
     * @param <K> The key type.
     * @param <V> The value type.
     * @param map A map entry.
     * @return A new {@link $mapType} instance containing the given map
     * @deprecated Will be removed in a future version.
     */
    @Deprecated
    public static <K$keyBound, V> $returnType<K, V> $name($JavaMapType<? extends K, ? extends V> map) {
        return $mapType.ofAll(map);
    }
    """ else ""}

    ${(1 to VARARGS).gen(i => {
      xs"""
        /$javadoc
         * Alias for {@link $mapType#of(${(1 to i).gen(j => s"$ofLinkParam, Object")(using ", ")})}
         *
         * @param <K> The key type.
         * @param <V> The value type.
         ${(1 to i).gen(j => s"* @param k$j  The key${ if (i > 1) s" of the ${j.ordinal} pair" else ""}\n* @param v$j  The value${ if (i > 1) s" of the ${j.ordinal} pair" else ""}\n")}
         * @return A new {@link $mapType} instance containing the given entries
         */
        public static <K$keyBound, V> $returnType<K, V> $name(${(1 to i).gen(j => xs"K k$j, V v$j")(using ", ")}) {
            return $mapType.of(${(1 to i).gen(j => xs"k$j, v$j")(using ", ")});
        }
      """
    })(using "\n\n")}
  """
  }

  xs"""
    //
    // Aliases for static factories
    //

    // -- Function

    ${(0 to N).gen(i => {
      val a = Arity(i)
      xs"""
        /$javadoc
         * Alias for {@link Function$i#of(Function$i)}
         *
         ${(0 to i).gen(j => if (j == 0) "* @param <R>             return type" else s"* @param <T$j>${if (j < 10) " " else ""}           type of the ${j.ordinal} argument")(using "\n")}
         * @param methodReference A method reference
         * @return A {@link Function$i}
         */
        public static ${a.fullGenerics} Function$i${a.fullGenerics} Function(Function$i${a.fullGenerics} methodReference) {
            return Function$i.of(methodReference);
        }
      """
    })(using "\n\n")}

    // -- CheckedFunction

    ${(0 to N).gen(i => {
      val a = Arity(i)
      xs"""
        /$javadoc
         * Alias for {@link CheckedFunction$i#of(CheckedFunction$i)}
         *
         ${(0 to i).gen(j => if (j == 0) "* @param <R>             return type" else s"* @param <T$j>            type of the ${j.ordinal} argument")(using "\n")}
         * @param methodReference A method reference
         * @return A {@link CheckedFunction$i}
         */
        public static ${a.fullGenerics} CheckedFunction$i${a.fullGenerics} CheckedFunction(CheckedFunction$i${a.fullGenerics} methodReference) {
            return CheckedFunction$i.of(methodReference);
        }
      """
    })(using "\n\n")}

    // -- unchecked

    ${(0 to N).gen(i => {
      val a = Arity(i)
      xs"""
        /$javadoc
         * Alias for {@link CheckedFunction$i#unchecked}
         *
         ${(0 to i).gen(j => if (j == 0) "* @param <R>  return type" else s"* @param <T$j> type of the ${j.ordinal} argument")(using "\n")}
         * @param f    A method reference
         * @return An unchecked wrapper of supplied {@link CheckedFunction$i}
         */
        public static ${a.fullGenerics} Function$i${a.fullGenerics} unchecked(CheckedFunction$i${a.fullGenerics} f) {
            return f.unchecked();
        }
      """
    })(using "\n\n")}

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
      val a = Arity(i)
      val links = (1 to i).gen(j => s"Object")(using ", ")
      xs"""
        /$javadoc
         * Alias for {@link Tuple#of($links)}
         *
         * Creates a tuple of ${i.numerus("element")}.
         *
         ${(1 to i).gen(j => s"* @param <T$j> type of the ${j.ordinal} element")(using "\n")}
         ${(1 to i).gen(j => s"* @param t$j   the ${j.ordinal} element")(using "\n")}
         * @return a tuple of ${i.numerus("element")}.
         */
        public static <${a.generics}> Tuple$i<${a.generics}> Tuple(${a.paramsDecl}) {
            return Tuple.of(${a.params});
        }
      """
    })(using "\n\n")}

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
     * Alias for {@link $FutureType#of($ExecutorType, $CheckedFunction0Type)}
     *
     * @param <T>             Type of the computation result.
     * @param executorService An executor service.
     * @param computation     A computation.
     * @return A new {@link $FutureType} instance.
     * @throws NullPointerException if one of executorService or computation is null.
     */
    public static <T> $FutureType<T> Future($ExecutorType executorService, $CheckedFunction0Type<? extends T> computation) {
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
     * Alias for {@link $FutureType#successful($ExecutorType, Object)}
     *
     * @param <T>             The value type of a successful result.
     * @param executorService An {@code ExecutorService}.
     * @param result          The result.
     * @return A succeeded {@link $FutureType}.
     * @throws NullPointerException if executorService is null
     */
    public static <T> $FutureType<T> Future($ExecutorType executorService, T result) {
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
    public static <T> Lazy<T> Lazy(@NonNull $SupplierType<? extends T> supplier) {
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

    ${genTraversableAliases(PriorityQueueType, PriorityQueueType, "PriorityQueue", sorted = true)}

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
    ${genTraversableAliases(TreeSetType, SortedSetType, "SortedSet", sorted = true)}

    // -- MAPS

    ${genMapAliases(HashMapType, MapType, "Map")}
    ${genMapAliases(LinkedHashMapType, MapType, "LinkedMap")}
    ${genMapAliases(TreeMapType, SortedMapType, "SortedMap", sorted = true)}

  """
}

def genAPIJavaTypeTweaks(im: ImportManager): String = {
  xs"""
    //
    // Java type tweaks
    //

    /**
     * Runs a {@code unit} of work and returns {@code Void}. This is helpful when a return value is expected,
     * e.g. by {@code Match}:
     *
     * <pre>{@code Match(i).of(
     *     Case($$(is(0)), i -> run(() -> System.out.println("zero"))),
     *     Case($$(is(1)), i -> run(() -> System.out.println("one"))),
     *     Case($$(), o -> run(() -> System.out.println("many")))
     * )}</pre>
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

def genAPIForComprehensions(im: ImportManager, isLazy: Boolean): String = {

  val OptionType = im.getType("io.vavr.control.Option")
  val IteratorType = im.getType("io.vavr.collection.Iterator")
  val EitherType = im.getType("io.vavr.control.Either")
  val FutureType = im.getType("io.vavr.concurrent.Future")
  val TryType = im.getType("io.vavr.control.Try")
  val ListType = im.getType("io.vavr.collection.List")
  val ValidationType = im.getType("io.vavr.control.Validation")
  val Objects = im.getType("java.util.Objects")
  im.getType("java.util.function.BiFunction")
  im.getType("org.jspecify.annotations.NonNull")

  val monadicTypesFor = List("Iterable", OptionType, FutureType, TryType, ListType, EitherType, ValidationType)
  val monadicTypesThatNeedParameter = List(EitherType, ValidationType)

  val types = if (isLazy) monadicTypesFor.filterNot(_ == "Iterable") else monadicTypesFor
  val arityRange: Range = if (isLazy) 2 to N else 1 to N

  def forClassName(mtype: String, i: Int): String =
    if (!isLazy && mtype == "Iterable") s"For$i" else s"${if (isLazy) "ForLazy" else "For"}$i$mtype"

  /** Returns the type of the j-th For parameter/field (function-wrapped in lazy mode for j > 1). */
  def tsType(mtype: String, parameterInset: String, j: Int): String =
    if (isLazy && j > 1) {
      val inputTypes = (1 until j).gen(k => s"? super T$k")(using ", ")
      s"Function${j - 1}<$inputTypes, $mtype<${parameterInset}T$j>>"
    } else s"$mtype<${parameterInset}T$j>"

  val factories = types.gen(mtype => arityRange.gen(i => {
    val isComplex = monadicTypesThatNeedParameter.contains(mtype)
    val parameterInset = if (isComplex) "L, " else ""
    val generics = parameterInset + Arity(i).generics
    val fcn = forClassName(mtype, i)
    val params = (1 to i).gen(j => s"@NonNull ${tsType(mtype, parameterInset, j)} ts$j")(using ", ")
    val ctorArgs = (1 to i).gen(j => s"ts$j")(using ", ")

    if (isLazy) {
      xs"""
        /$javadoc
         * Creates a lazy {@code For}-comprehension over ${i.numerus(mtype)}.
         *
         * <p>The first argument ({@code ts1}) is the initial ${mtype}. Each subsequent
         * argument ({@code ts2} .. {@code ts$i}) is a function that receives all values
         * bound so far and returns the next ${mtype}. This method only constructs the
         * lazy comprehension; underlying effects are evaluated when {@code yield(...)}
         * is invoked.</p>
         *
         ${(0 to i).gen(j => if (j == 0) "*" else s"* @param ts$j the ${j.ordinal} ${mtype}")(using "\n")}
         ${if (isComplex) s"* @param <L> the common left-hand type of all ${mtype}s\n" else ""}
         ${(1 to i).gen(j => s"* @param <T$j> the component type of the ${j.ordinal} ${mtype}")(using "\n")}
         * @return a new {@code $fcn} builder of arity $i
         * @throws NullPointerException if any argument is {@code null}
         */
        public static <$generics> $fcn<$generics> For($params) {
            ${(1 to i).gen(j => xs"""$Objects.requireNonNull(ts$j, "ts$j is null");""")(using "\n")}
            return new $fcn<>($ctorArgs);
        }
      """
    } else {
      xs"""
        /$javadoc
         * Creates a {@code For}-comprehension of ${i.numerus(mtype)}.
         ${(0 to i).gen(j => if (j == 0) "*" else s"* @param ts$j the ${j.ordinal} $mtype")(using "\n")}
         ${if (isComplex) s"* @param <L> left-hand type of all ${mtype}s\n" else ""}
         ${(1 to i).gen(j => s"* @param <T$j> component type of the ${j.ordinal} $mtype")(using "\n")}
         * @return a new {@code For}-comprehension of arity $i
         */
        public static <$generics> $fcn<$generics> For($params) {
            ${(1 to i).gen(j => xs"""$Objects.requireNonNull(ts$j, "ts$j is null");""")(using "\n")}
            return new $fcn<>($ctorArgs);
        }
      """
    }
  })(using "\n\n"))(using "\n\n")

  val classes = types.gen(mtype => arityRange.gen(i => {
    val rtype = if (!isLazy && mtype == "Iterable") IteratorType else mtype
    val cons: String => String = if (!isLazy && mtype == "Iterable") { m => s"$IteratorType.ofAll($m)" } else { m => m }
    val fcn = forClassName(mtype, i)
    val parameterInset = if (monadicTypesThatNeedParameter.contains(mtype)) "L, " else ""
    val generics = parameterInset + Arity(i).generics

    val fields = (1 to i).gen(j => s"private final ${tsType(mtype, parameterInset, j)} ts$j;")(using "\n")
    val ctorParams = (1 to i).gen(j => s"${tsType(mtype, parameterInset, j)} ts$j")(using ", ")
    val assignments = (1 to i).gen(j => s"this.ts$j = ts$j;")(using "\n")

    if (isLazy) {
      val yieldBody = {
        def nestedLambda(j: Int): String = {
          val base = " " * 11
          val indent = "  " * (j + 1)
          if (j == i) {
            val argsList = (1 to i).map(k => s"t$k").mkString(", ")
            val inputArgs = (1 until i).map(k => s"t$k").mkString(", ")
            s"ts$i.apply($inputArgs).map(t$i -> f.apply($argsList))"
          } else if (j == 1) {
            s"ts1.flatMap(t1 -> {\n" +
              s"${base}${indent}  return ${nestedLambda(j + 1)};\n" +
              s"${base}${indent}})"
          } else {
            val inputArgs = (1 until j).map(k => s"t$k").mkString(", ")
            s"ts$j.apply($inputArgs).flatMap(t$j -> {\n" +
              s"${base}${indent}  return ${nestedLambda(j + 1)};\n" +
              s"${base}${indent}})"
          }
        }
        nestedLambda(1)
      }

      xs"""
        /$javadoc
         * A lazily evaluated {@code For}-comprehension with ${i.numerus(mtype)}.
         *
         * <p>Constructed via {@code For(...)} and evaluated by calling {@code yield(...)}.
         * Construction is side-effect free; underlying ${i.plural(mtype)} are traversed
         * only when {@code yield(...)} is invoked.</p>
         *
         ${if (monadicTypesThatNeedParameter.contains(mtype)) s"* @param <L> the common left-hand type of all ${mtype}s\n" else ""}
         ${(1 to i).gen(j => s"* @param <T$j> the component type of the ${j.ordinal} ${mtype}")(using "\n")}
         */
        public static class $fcn<$generics> {

            $fields

            private $fcn($ctorParams) {
                $assignments
            }

            /$javadoc
             * Produces results by mapping the Cartesian product of all bound values.
             *
             * <p>Evaluation is lazy and delegated to the underlying ${i.plural(mtype)} by
             * composing {@code flatMap} and {@code map} chains.</p>
             *
             * @param f a function mapping a tuple of bound values to a result
             * @param <R> the element type of the resulting {@code $rtype}
             * @return an {@code $rtype} containing mapped results
             * @throws NullPointerException if {@code f} is {@code null}
             */
            public <R> $rtype<${parameterInset}R> yield(${
          if (i == 2)
            s"@NonNull BiFunction<? super T1, ? super T2, ? extends R>"
          else
            s"@NonNull Function$i<${Arity(i).wideGenerics}, ? extends R>"
        } f) {
                $Objects.requireNonNull(f, "f is null");
                return $yieldBody;
            }
        }
      """
    } else {
      val functionType = javaFunctionType(i, im)
      val parameterDoc = (if (monadicTypesThatNeedParameter.contains(mtype)) {
        s"\n* @param <L> The left-hand type of all {@link $mtype}s"
      } else { "" })
      val typeDocs = (1 to i).gen(j => s"* @param <T$j> component type of {@link $mtype} number $j\n")
      val args = Arity(i).wideGenerics
      xs"""
        /$javadoc
         * For-comprehension with ${i.numerus(mtype)}.
         $parameterDoc
         $typeDocs
         */
        public static class $fcn<$generics> {

            ${(1 to i).gen(j => xs"""private final $mtype<${parameterInset}T$j> ts$j;""")(using "\n")}

            private $fcn(${(1 to i).gen(j => s"$mtype<${parameterInset}T$j> ts$j")(using ", ")}) {
                ${(1 to i).gen(j => xs"""this.ts$j = ts$j;""")(using "\n")}
            }

            /$javadoc
             * Yields a result for elements of the cross-product of the underlying ${i.plural(mtype)}.
             *
             * @param f a function that maps an element of the cross-product to a result
             * @param <R> type of the resulting {@code $rtype} elements
             * @return an {@code $rtype} of mapped results
             */
            public <R> $rtype<${parameterInset}R> yield(@NonNull $functionType<$args, ? extends R> f) {
                $Objects.requireNonNull(f, "f is null");
                ${if (i == 1) xs"""
                  return ${cons("ts1")}.map(f);
                """ else xs"""
                  return
                      ${(1 until i).gen(j => s"${cons(s"ts$j")}.flatMap(t$j ->")(using "\n")}
                      ${cons(s"ts$i")}.map(t$i -> f.apply(${(1 to i).gen(j => s"t$j")(using ", ")}))${")" * (i - 1)};
                """}
            }

            ${(i == 1).gen(xs"""
              /$javadoc
               * A shortcut for {@code yield(Function.identity())}.
               *
               * @return an {@code Iterator} of mapped results
               */
              public $rtype<${parameterInset}T1> yield() {
                  return this.yield(Function.identity());
              }
            """)}
        }
      """
    }
  })(using "\n\n"))(using "\n\n")

  if (isLazy) {
    xs"""
        $factories

        $classes
    """
  } else {
    xs"""
        //
        // For-Comprehension
        //

        /**
         * A shortcut for {@code Iterator.ofAll(ts).flatMap(f)} which allows us to write real for-comprehensions using
         * {@code For(...).yield(...)}.
         * <p>
         * Example:
         * <pre>{@code
         * For(getPersons(), person ->
         *     For(person.getTweets(), tweet ->
         *         For(tweet.getReplies())
         *             .yield(reply -> person + ", " + tweet + ", " + reply)));
         * }</pre>
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

        $factories

         $classes
    """
  }
}

def genAPIMatch(im: ImportManager): String = {

  im.getStatic("io.vavr.API.Match.*")

  val Objects = im.getType("java.util.Objects")
  val FunctionType = im.getType("java.util.function.Function")
  val SupplierType = im.getType("java.util.function.Supplier")
  val PartialFunctionType = im.getType("io.vavr.PartialFunction")
  val OptionType = im.getType("io.vavr.control.Option")
  val PredicateType = im.getType("java.util.function.Predicate")
  im.getType("org.jspecify.annotations.NonNull")

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
    public static <T> Match<T> Match(T value) {
        return new Match<>(value);
    }

    // -- static Case API

    // - Pattern0

    /**
     * Returns a {@link Case0} instance for a specific {@link Pattern0} and {@link $FunctionType}
     *
     * @param <T>     Type of the value being matched
     * @param <R>     Return value type
     * @param pattern Pattern to match
     * @param f       Matched value consumer
     * @return new Case0
     */
    public static <T, R> Case<T, R> Case(@NonNull Pattern0<T> pattern, @NonNull $FunctionType<? super T, ? extends R> f) {
        $Objects.requireNonNull(pattern, "pattern is null");
        $Objects.requireNonNull(f, "f is null");
        return new Case0<>(pattern, f);
    }

    /**
     * Returns a {@link Case0} instance for a specific {@link Pattern0} and {@link $SupplierType}
     *
     * @param <T>      Type of the value being matched
     * @param <R>      Return value type
     * @param pattern  Pattern to match
     * @param supplier Matched value supplier
     * @return new Case0
     */
    public static <T, R> Case<T, R> Case(@NonNull Pattern0<T> pattern, @NonNull $SupplierType<? extends R> supplier) {
        $Objects.requireNonNull(pattern, "pattern is null");
        $Objects.requireNonNull(supplier, "supplier is null");
        return new Case0<>(pattern, ignored -> supplier.get());
    }

    /**
     * Returns a {@link Case0} instance for a specific {@link Pattern0} and a constant value
     *
     * @param <T>     Type of the value being matched
     * @param <R>     Return value type
     * @param pattern Pattern to match
     * @param retVal  Constant value to return
     * @return new Case0
     */
    public static <T, R> Case<T, R> Case(@NonNull Pattern0<T> pattern, R retVal) {
        $Objects.requireNonNull(pattern, "pattern is null");
        return new Case0<>(pattern, ignored -> retVal);
    }

    ${(1 to N).gen(i => {
      val a = Arity(i)
      val argTypes = a.wideGenerics
      val generics = a.generics
      val params = (i > 1).gen("(") + a.underscoreParams + (i > 1).gen(")")
      val functionType = javaFunctionType(i, im)
      val typeDocs = (1 to i).gen(j => s"* @param <T$j>     Intermediate type $j for the pattern\n")

      xs"""
        // - Pattern$i

        /$javadoc
         * Returns a {@link Case$i} instance for a specific {@link Pattern$i} and {@link $functionType}
         *
         * @param <T>      Type of the value being matched
         $typeDocs
         * @param <R>      Return value type
         * @param pattern  Pattern to match
         * @param f        Matched value consumer
         * @return new Case$i
         */
        public static <T, $generics, R> Case<T, R> Case(@NonNull Pattern$i<T, $generics> pattern, @NonNull $functionType<$argTypes, ? extends R> f) {
            $Objects.requireNonNull(pattern, "pattern is null");
            $Objects.requireNonNull(f, "f is null");
            return new Case$i<>(pattern, f);
        }

        /$javadoc
         * Returns a {@link Case$i} instance for a specific {@link Pattern$i} and {@link $SupplierType}
         *
         * @param <T>      Type of the value being matched
         $typeDocs
         * @param <R>      Return value type
         * @param pattern  Pattern to match
         * @param supplier Matched value supplier
         * @return new Case$i
         */
        public static <T, $generics, R> Case<T, R> Case(@NonNull Pattern$i<T, $generics> pattern, @NonNull $SupplierType<? extends R> supplier) {
            $Objects.requireNonNull(pattern, "pattern is null");
            $Objects.requireNonNull(supplier, "supplier is null");
            return new Case$i<>(pattern, $params -> supplier.get());
        }

        /$javadoc
         * Returns a {@link Case$i} instance for a specific {@link Pattern$i} and a constant value
         *
         * @param <T>      Type of the value being matched
         $typeDocs
         * @param <R>      Return value type
         * @param pattern  Pattern to match
         * @param retVal   Constant value to return
         * @return new Case$i
         */
        public static <T, $generics, R> Case<T, R> Case(@NonNull Pattern$i<T, $generics> pattern, R retVal) {
            $Objects.requireNonNull(pattern, "pattern is null");
            return new Case$i<>(pattern, $params -> retVal);
        }
      """
    })(using "\n\n")}

    // PRE-DEFINED PATTERNS

    // 1) Atomic patterns $$(), $$(value), $$(predicate)

    /**
     * Wildcard pattern, matches any value.
     *
     * @param <T> injected type of the underlying value
     * @return a new {@code Pattern0} instance
     */
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
    public static <T> Pattern0<T> $$(T prototype) {
        return new Pattern0<T>() {

            private static final long serialVersionUID = 1L;

            @Override
            public T apply(T obj) {
                return obj;
            }

            @Override
            public boolean isDefinedAt(T obj) {
                if (obj == prototype) {
                    return true;
                } else if (prototype != null && prototype.getClass().isInstance(obj)) {
                    return $Objects.equals(obj, prototype);
                } else {
                    return false;
                }
            }
        };
    }

    /**
     * Guard pattern, checks if a predicate is satisfied.
     * <p>
     * This method is intended to be used with lambdas and method references, for example:
     *
     * <pre>{@code 
     * String evenOrOdd(int num) {
     *     return Match(num).of(
     *             Case($$(i -> i % 2 == 0), "even"),
     *             Case($$(this::isOdd), "odd")
     *     );
     * }
     *
     * boolean isOdd(int i) {
     *     return i % 2 == 1;
     * }
     * }</pre>
     *
     * It is also valid to pass {@code Predicate} instances:
     *
     * <pre>{@code 
     * Predicate<Integer> isOdd = i -> i % 2 == 1;
     *
     * Match(num).of(
     *         Case($$(i -> i % 2 == 0), "even"),
     *         Case($$(isOdd), "odd")
     * );
     * }</pre>
     *
     * <strong>Note:</strong> Please take care when matching {@code Predicate} instances. In general,
     * <a href="http://cstheory.stackexchange.com/a/14152" target="_blank">function equality</a>
     * is an undecidable problem in computer science. In Vavr we are only able to check,
     * if two functions are the same instance.
     * <p>
     * However, this code will fail:
     *
     * <pre>{@code 
     * Predicate<Integer> p = i -> true;
     * Match(p).of(
     *     Case($$(p), 1) // WRONG! It calls $$(Predicate)
     * );
     * }</pre>
     *
     * Instead we have to use {@link Predicates#is(Object)}:
     *
     * <pre>{@code 
     * Predicate<Integer> p = i -> true;
     * Match(p).of(
     *     Case($$(is(p)), 1) // CORRECT! It calls $$(T)
     * );
     * }</pre>
     *
     * @param <T>       type of the prototype
     * @param predicate the predicate that tests a given value
     * @return a new {@code Pattern0} instance
     */
    public static <T> Pattern0<T> $$(@NonNull $PredicateType<? super T> predicate) {
        $Objects.requireNonNull(predicate, "predicate is null");
        return new Pattern0<T>() {

            private static final long serialVersionUID = 1L;

            @Override
            public T apply(T obj) {
                return obj;
            }

            @Override
            public boolean isDefinedAt(T obj) {
                try {
                    return predicate.test(obj);
                } catch (ClassCastException x) {
                    return false;
                }
            }
        };
    }

    /**
     * Scala-like structural pattern matching for Java. Instances are obtained via {@link API#Match(Object)}.
     * @param <T> type of the object that is matched
     */
    public static final class Match<T> {

        private final T value;

        private Match(T value) {
            this.value = value;
        }

        /$javadoc
         * Executes the match, created by the factory function {@link API#Match(Object)}. Throws exceptions
         * when the list of {@link Case}s is incomplete.
         *
         * @param cases list of cases we execute the match against
         * @param <R>   return value type
         * @return The matched value
         * @throws MatchError if the list of cases was not defined for all possible values of T
         */
        @SuppressWarnings({ "unchecked", "varargs" })
        @SafeVarargs
        public final <R> R of(@NonNull Case<? extends T, ? extends R> @NonNull ... cases) {
            Objects.requireNonNull(cases, "cases is null");
            for (Case<? extends T, ? extends R> _case : cases) {
                final Case<T, R> __case = (Case<T, R>) _case;
                if (__case.isDefinedAt(value)) {
                    return __case.apply(value);
                }
            }
            throw new MatchError(value);
        }

        /$javadoc
        * Executes the match, created by the factory function {@link API#Match(Object)}. Returns $OptionType.some(...)
        * if the value was matched and $OptionType.none() otherwise.
        *
        * @param cases list of cases we execute the match against
        * @param <R>   return value type
        * @return $OptionType containing the matched value, or none
        */
        @SuppressWarnings({ "unchecked", "varargs" })
        @SafeVarargs
        public final <R> $OptionType<R> option(@NonNull Case<? extends T, ? extends R> @NonNull ... cases) {
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

        /$javadoc
         * Base interface for all the Cases
         *
         * @param <T> Type of the value being matched
         * @param <R> Return value type
         */
        public interface Case<T, R> extends $PartialFunctionType<T, R> {

            /**
             * The serial version UID for serialization.
             */
            long serialVersionUID = 1L;
        }

        /$javadoc
         * {@link Case} implementation for simplest case
         *
         * @param <T> Type of the value being matched
         * @param <R> Return value type
         */
        public static final class Case0<T, R> implements Case<T, R> {

            /$javadoc
             * The serial version UID for serialization.
             */
            private static final long serialVersionUID = 1L;

            private final Pattern0<T> pattern;
            private transient final $FunctionType<? super T, ? extends R> f;

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
          val a = Arity(i)
          val argTypes = a.wideGenerics
          val generics = a.generics
          val functionType = javaFunctionType(i, im)
          val accessModifier = i match {
            case 1 => "transient final"
            case 2 => "transient final"
            case _ => "final"
          }
          val typeDocs = (1 to i).gen(j => s"* @param <T$j> Intermediate type $j\n")
          xs"""
            /$javadoc
             * {@link Case} implementation for a case with $i intermediate type${if (i>1) "s" else ""}
             *
             * @param <T>  Type of the value being matched
             $typeDocs
             * @param <R>  Return value type
             */
            public static final class Case$i<T, $generics, R> implements Case<T, R> {

                /$javadoc
                 * The serial version UID for serialization.
                 */
                private static final long serialVersionUID = 1L;

                private final Pattern$i<T, $generics> pattern;
                private $accessModifier $functionType<$argTypes, ? extends R> f;

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
        })(using "\n\n")}

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

        /$javadoc
         * A {@link Pattern} implementation for the simplest pattern
         *
         * @param <T>  Class type that is matched by this pattern
         */
        public static abstract class Pattern0<T> implements Pattern<T, T> {

            /$javadoc
             * The serial version UID for serialization.
             */
            private static final long serialVersionUID = 1L;

            private static final Pattern0<Object> ANY = new Pattern0<Object>() {

                private static final long serialVersionUID = 1L;

                @Override
                public Object apply(Object obj) {
                    return obj;
                }

                @Override
                public boolean isDefinedAt(Object obj) {
                    return true;
                }
            };

            /**
             * The greediest match, a catch-all
             *
             * @param <T> Class type that is matched by this pattern
             * @return Pattern0
             */
            @SuppressWarnings("unchecked")
            public static <T> Pattern0<T> any() {
                return (Pattern0<T>) ANY;
            }

            ${// DEV-NOTE: We need the lower bound `Class<? super T>` instead of the more appropriate `Class<T>`
            //           because it allows us to create patterns for generic types, which would otherwise not be
            //           possible: `Pattern0<Some<String>> p = Pattern0.of(Some.class);`
            ""
            }
            /$javadoc
             * Static factory for a {@link Pattern0} based on a {@link Class}
             *
             * @param type {@link Class} to build the pattern from
             * @param <T>  Class type matched by this pattern
             * @return new Pattern0
             */
            public static <T> Pattern0<T> of(@NonNull Class<? super T> type) {
                return new Pattern0<T>() {

                    /$javadoc
                     * The serial version UID for serialization.
                     */
                    private static final long serialVersionUID = 1L;

                    @Override
                    public T apply(T obj) {
                        return obj;
                    }

                    @Override
                    public boolean isDefinedAt(T obj) {
                        return type.isInstance(obj);
                    }
                };
            }

            /$javadoc
             * Creates a new {@code Pattern0}.
             *
             * <p>This constructor is protected because {@code Pattern0} is abstract and
             * intended to be subclassed rather than instantiated directly.</p>
             */
            protected Pattern0() {
                // default constructor
            }
        }

        ${(1 to N).gen(i => {
          val a = Arity(i)
          val declaredGenerics = (1 to i).gen(j => s"T$j extends U$j, U$j")(using ", ")
          val resultGenerics = a.generics
          val resultType = if (i == 1) resultGenerics else s"Tuple$i<$resultGenerics>"
          val unapplyGenerics = (1 to i).gen(j => s"U$j")(using ", ")
          val unapplyTupleType = s"Tuple$i<$unapplyGenerics>"
          val args = (1 to i).gen(j => s"@NonNull Pattern<T$j, ?> p$j")(using ", ")
          val typeDocs = (1 to i).gen(j => s"* @param <T$j> Member type $j of the composite part this pattern decomposes\n")
          val staticOfTypeDocs = (1 to i).gen(j =>
            xs"""
                * @param <T$j>    Member type $j of the composite part this pattern decomposes
                * @param <U$j>    Member type $j of the Tuple the composite part of this pattern decomposes to\n
            """
          )
          val staticOfParamDocs = (1 to i).gen(j => s"* @param p$j      {@link Pattern} matching the intermediate type $j\n")
          xs"""
            /$javadoc
             * A {@link Pattern} implementation for the pattern with $i intermediate type${if (i>1) "s" else ""}
             *
             * @param <T>  Class type that is matched by this pattern
             $typeDocs
             */
            public static abstract class Pattern$i<T, $resultGenerics> implements Pattern<T, $resultType> {

                /$javadoc
                 * The serial version UID for serialization.
                 */
                private static final long serialVersionUID = 1L;

                /**
                 * Static factory for a {@link Pattern$i} based on a {@link Class}, ${if (i >1) s"$i {@link Pattern}s" else "{@link Pattern}"} to decompose
                 * it to and a mapper to aggregate result back into a {@link Tuple$i}
                 *
                 * @param type    {@link Class} to build the pattern from
                 $staticOfParamDocs
                 * @param unapply Mapper function from T to a {@link Tuple$i}
                 * @param <T>     Class type matched by this pattern
                 $staticOfTypeDocs
                 * @return new Pattern$i
                 */
                public static <T, $declaredGenerics> Pattern$i<T, $resultGenerics> of(@NonNull Class<? super T> type, $args, @NonNull Function<T, $unapplyTupleType> unapply) {
                    return new Pattern$i<T, $resultGenerics>() {

                        /$javadoc
                         * The serial version UID for serialization.
                         */
                        private static final long serialVersionUID = 1L;

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
                            if (type.isInstance(obj)) {
                                final $unapplyTupleType u = unapply.apply(obj);
                                return
                                        ${(1 to i).gen(j => s"((Pattern<U$j, ?>) p$j).isDefinedAt(u._$j)")(using " &&\n")};
                            } else {
                                return false;
                            }
                        }
                    };
                }

                /$javadoc
                 * Creates a new {@code Pattern$i}.
                 *
                 * <p>This constructor is protected because {@code Pattern$i} is abstract and
                 * intended to be subclassed rather than instantiated directly.</p>
                 */
                 protected Pattern$i() {
                     // default constructor
                 }
            }
          """
        })(using "\n\n")}
    }
  """
}
