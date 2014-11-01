/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static javaslang.Requirements.requireNonNull;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import javaslang.Requirements;
import javaslang.Tuples;
import javaslang.Tuples.Tuple2;

/**
 * Seq is an Iterable Stream implementation providing methods which require a guaranteed sequential order of elements.
 * E.g. in general it is not possible to zipWithIndex the elements of a parallel Stream because their order is
 * unpredictable.
 * 
 * @see java.lang.Iterable
 * @see java.util.stream.Stream
 */
interface Seq<T> {

	// -- Low-level API

	<U> Seq<U> cons(Iterator<U> iterator);

	<U> Seq<U> cons(U element);

	@SuppressWarnings("unchecked")
	<U> Seq<U> cons(U... elements);

	Iterator<T> iterator();

	// -- High-level API

	Seq<T> limitUntil(Predicate<? super T> predicate);

	// TODO
	default Seq<Tuple2<T, Integer>> zipWithIndex() {
		return zip(iterate(0, i -> i + 1), (T t1, Integer t2) -> Tuples.of(t1, t2));
	}

	default <U> Seq<Tuple2<T, U>> zip(Seq<U> other) {
		return zip(other, (t1, t2) -> Tuples.of(t1, t2));
	}

	//	// TODO: LazySeq has to provide an intermediate version instead
	default <U, R> Seq<R> zip(Seq<U> other, BiFunction<T, U, R> zipper) {
		final Iterator<T> left = iterator();
		final Iterator<U> right = other.iterator();
		final Iterator<R> zipped = Iterators.of(() -> left.hasNext() && right.hasNext(),
				() -> zipper.apply(left.next(), right.next()));
		return cons(zipped);
	}

	// -- Stream impl (High-Level Stream API)

	Seq<T> filter(Predicate<? super T> predicate);

	<R> Seq<R> map(Function<? super T, ? extends R> mapper);

	//	public <R> Seq<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
	//
	//	public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
	//
	//	public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
	//
	//	public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
	//
	//	public T distinct() {
	//
	//	public T sorted() {
	//
	//	public T sorted(Comparator<? super T> comparator) {
	//
	//	public T peek(Consumer<? super T> action) {
	//
	//	public T limit(long maxSize) {
	//
	//	public T skip(long n) {
	//
	//	public void forEach(Consumer<? super T> action) {
	//
	//	public void forEachOrdered(Consumer<? super T> action) {
	//
	//	public T[] toArray() {
	//
	//	public <A> A[] toArray(IntFunction<A[]> generator) {
	//
	//	public T reduce(T identity, BinaryOperator<T> accumulator) {
	//
	//	public Optional<T> reduce(BinaryOperator<T> accumulator) {
	//
	//	public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
	//
	//	public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
	//
	//	public <R, A> R collect(Collector<? super T, A, R> collector) {
	//
	//	public Optional<T> min(Comparator<? super T> comparator) {
	//
	//	public Optional<T> max(Comparator<? super T> comparator) {
	//
	//	public long count() {
	//
	//	public boolean anyMatch(Predicate<? super T> predicate) {
	//
	//	public boolean allMatch(Predicate<? super T> predicate) {
	//
	//	public boolean noneMatch(Predicate<? super T> predicate) {
	//
	//  TODO: head vs first vs ...
	//	public Optional<T> findFirst() {
	//	public Optional<T> findAny() {

	// -- factory methods

	public static <T> Seq<T> of(Spliterator<T> spliterator) {
		requireNonNull(spliterator, "spliterator is null");
		return null; // TODO: Seq.of(StreamSupport.stream(spliterator, false));
	}

	public static <T> Seq<T> of(Iterable<T> iterable) {
		requireNonNull(iterable, "iterable is null");
		return Seq.of(iterable.spliterator());
	}

	public static <T> Seq<T> of(Iterator<T> iterator) {
		requireNonNull(iterator, "iterator is null");
		return Seq.of(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED));
	}

	public static <T> Seq<T> empty() {
		return null; // TODO: Seq.of(Stream.empty());
	}

	public static <T> Seq<T> of(T t) {
		return null; // TODO: Seq.of(Stream.of(t));
	}

	@SafeVarargs
	public static <T> Seq<T> of(T... values) {
		requireNonNull(values, "values is null");
		return null; // TODO: Seq.of(Stream.of(values));
	}

	static <T> Seq<T> iterate(final T seed, final UnaryOperator<T> f) {
		Requirements.requireNonNull(f, "f is null");
		return Seq.of(Stream.iterate(seed, f).iterator());
	}

	//
	//	public static <T> T generate(Supplier<T> supplier) {
	//		requireNonNull(supplier, "supplier is null");
	//		return Seq.of(Stream.generate(supplier));
	//	}
	//
	//	public static <T> T concat(Stream<? extends T> a, Stream<? extends T> b) {
	//		requireNonNull(a, "stream a is null");
	//		requireNonNull(b, "stream b is null");
	//		return Seq.of(Stream.concat(a, b));
	//	}
}
