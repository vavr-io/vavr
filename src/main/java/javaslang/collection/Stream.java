/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static javaslang.Requirements.requireNonNull;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.StreamSupport;

import javaslang.collection.Tuple.Tuple2;

/**
 * IStream is an Iterable Stream implementation providing methods which require a guaranteed sequential order of
 * elements. E.g. in general it is not possible to zipWithIndex the elements of a parallel Stream because their order is
 * unpredictable.
 * 
 * @see java.lang.Iterable
 * @see java.util.stream.Stream
 */
// DEV-NOTE: Currently I see no benefit creating an interface for SStream
public final class Stream<T> implements Iterable<T>, java.util.stream.Stream<T> {

	private final java.util.stream.Stream<T> stream;

	private Stream(java.util.stream.Stream<T> stream) {
		this.stream = stream;
	}

	// -- new API

	public <U> Stream<Tuple2<T, U>> zip(Stream<U> other) {
		return zip(other, (t1, t2) -> Tuple.of(t1, t2));
	}

	public <U, R> Stream<R> zip(Stream<U> other, BiFunction<T, U, R> zipper) {
		final Iterator<T> left = iterator();
		final Iterator<U> right = other.iterator();
		final Iterator<R> zipped = Iterators.of(() -> left.hasNext() && right.hasNext(),
				() -> zipper.apply(left.next(), right.next()));
		return Stream.of(zipped);
	}

	// TODO: test joining null values
	public String join() {
		return stream.map(s -> (s == null) ? null : s.toString()).collect(Collectors.joining());
	}

	public String join(CharSequence delimiter) {
		return stream.map(s -> (s == null) ? null : s.toString()).collect(Collectors.joining(delimiter));
	}

	public String join(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
		return stream
				.map(s -> (s == null) ? null : s.toString())
				.collect(Collectors.joining(delimiter, prefix, suffix));
	}

	public List<T> toList() {
		return stream.collect(List.collector());
	}

	// -- Stream impl (High-Level Stream API)

	@Override
	public Stream<T> filter(Predicate<? super T> predicate) {
		return Stream.of(stream.filter(predicate));
	}

	@Override
	public <R> Stream<R> map(Function<? super T, ? extends R> mapper) {
		return Stream.of(stream.map(mapper));
	}

	@Override
	public IntStream mapToInt(ToIntFunction<? super T> mapper) {
		return stream.mapToInt(mapper);
	}

	@Override
	public LongStream mapToLong(ToLongFunction<? super T> mapper) {
		return stream.mapToLong(mapper);
	}

	@Override
	public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
		return stream.mapToDouble(mapper);
	}

	@Override
	public <R> Stream<R> flatMap(Function<? super T, ? extends java.util.stream.Stream<? extends R>> mapper) {
		return Stream.of(stream.flatMap(mapper));
	}

	@Override
	public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
		return stream.flatMapToInt(mapper);
	}

	@Override
	public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
		return stream.flatMapToLong(mapper);
	}

	@Override
	public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
		return stream.flatMapToDouble(mapper);
	}

	@Override
	public Stream<T> distinct() {
		return Stream.of(stream.distinct());
	}

	@Override
	public Stream<T> sorted() {
		return Stream.of(stream.sorted());
	}

	@Override
	public Stream<T> sorted(Comparator<? super T> comparator) {
		return Stream.of(stream.sorted(comparator));
	}

	@Override
	public Stream<T> peek(Consumer<? super T> action) {
		return Stream.of(stream.peek(action));
	}

	@Override
	public Stream<T> limit(long maxSize) {
		return Stream.of(stream.limit(maxSize));
	}

	@Override
	public Stream<T> skip(long n) {
		return Stream.of(stream.skip(n));
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		stream.forEach(action);
	}

	@Override
	public void forEachOrdered(Consumer<? super T> action) {
		stream.forEachOrdered(action);
	}

	@Override
	@SuppressWarnings("unchecked")
	public T[] toArray() {
		return (T[]) stream.toArray();
	}

	@Override
	public <A> A[] toArray(IntFunction<A[]> generator) {
		return stream.toArray(generator);
	}

	@Override
	public T reduce(T identity, BinaryOperator<T> accumulator) {
		return stream.reduce(identity, accumulator);
	}

	@Override
	public Optional<T> reduce(BinaryOperator<T> accumulator) {
		return stream.reduce(accumulator);
	}

	@Override
	public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
		return stream.reduce(identity, accumulator, combiner);
	}

	@Override
	public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
		return stream.collect(supplier, accumulator, combiner);
	}

	@Override
	public <R, A> R collect(Collector<? super T, A, R> collector) {
		return stream.collect(collector);
	}

	@Override
	public Optional<T> min(Comparator<? super T> comparator) {
		return stream.min(comparator);
	}

	@Override
	public Optional<T> max(Comparator<? super T> comparator) {
		return stream.max(comparator);
	}

	@Override
	public long count() {
		return stream.count();
	}

	@Override
	public boolean anyMatch(Predicate<? super T> predicate) {
		return stream.anyMatch(predicate);
	}

	@Override
	public boolean allMatch(Predicate<? super T> predicate) {
		return stream.allMatch(predicate);
	}

	@Override
	public boolean noneMatch(Predicate<? super T> predicate) {
		return stream.noneMatch(predicate);
	}

	@Override
	public Optional<T> findFirst() {
		return stream.findFirst();
	}

	@Override
	public Optional<T> findAny() {
		return stream.findAny();
	}

	// -- BaseStream impl (Low-Level Stream API)

	/**
	 * Closes this SStream.
	 * 
	 * @see java.util.stream.BaseStream#close()
	 */
	@Override
	public void close() {
		stream.close();
	}

	/**
	 * Returns false.
	 * 
	 * @return false
	 * @see java.util.stream.BaseStream#isParallel()
	 */
	@Override
	public boolean isParallel() {
		return false;
	}

	/**
	 * Returns an Iterator.
	 * 
	 * @return a new Iterator
	 * @see java.util.stream.BaseStream#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return stream.iterator();
	}

	/**
	 * Adds a close handler to this SStream.
	 * 
	 * @param closeHandler A close handler
	 * @return a new instance of SStream
	 * @see java.util.stream.BaseStream#onClose(Runnable)
	 */
	@Override
	public Stream<T> onClose(Runnable closeHandler) {
		return Stream.of(stream.onClose(closeHandler));
	}

	/**
	 * Returns this, i.e. has no effect.
	 * 
	 * @return this
	 * @see java.util.stream.BaseStream#parallel()
	 */
	@Override
	public Stream<T> parallel() {
		return this;
	}

	/**
	 * Returns this, i.e. has no effect.
	 * 
	 * @return this
	 * @see java.util.stream.BaseStream#sequential()
	 */
	@Override
	public Stream<T> sequential() {
		return this;
	}

	/**
	 * Returns a Spliterator of this SStream.
	 * 
	 * @return a new Spliterator
	 * @see java.util.stream.BaseStream#spliterator()
	 */
	@Override
	public Spliterator<T> spliterator() {
		return stream.spliterator();
	}

	/**
	 * Returns this, i.e. has no effect.
	 * 
	 * @return this
	 * @see java.util.stream.BaseStream#unordered()
	 */
	@Override
	public Stream<T> unordered() {
		return this;
	}

	// -- factory methods

	// TODO: provide unboxed version DoubleStream
	public static Stream<Double> of(DoubleStream stream) {
		requireNonNull(stream, "stream is null");
		return new Stream<>(stream.boxed());
	}

	// TODO: provide unboxed version IntStream
	public static Stream<Integer> of(IntStream stream) {
		requireNonNull(stream, "stream is null");
		return new Stream<>(stream.boxed());
	}

	// TODO: provide unboxed version LongStream
	public static Stream<Long> of(LongStream stream) {
		requireNonNull(stream, "stream is null");
		return new Stream<>(stream.boxed());
	}

	// TODO: provide unboxed version CharStream
	public static IntStream of(String s) {
		requireNonNull(s, "string is null");
		return s.chars();
	}

	public static <T> Stream<T> of(java.util.stream.Stream<T> stream) {
		requireNonNull(stream, "stream is null");
		return new Stream<>(stream);
	}

	public static <T> Stream<T> of(Spliterator<T> spliterator) {
		requireNonNull(spliterator, "spliterator is null");
		return Stream.of(StreamSupport.stream(spliterator, false));
	}

	public static <T> Stream<T> of(Iterable<T> iterable) {
		requireNonNull(iterable, "iterable is null");
		return Stream.of(iterable.spliterator());
	}

	public static <T> Stream<T> of(Iterator<T> iterator) {
		requireNonNull(iterator, "iterator is null");
		return Stream.of(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED));
	}

	public static <T> Stream<T> empty() {
		return Stream.of(java.util.stream.Stream.empty());
	}

	public static <T> Stream<T> of(T t) {
		return Stream.of(java.util.stream.Stream.of(t));
	}

	@SafeVarargs
	public static <T> Stream<T> of(T... values) {
		requireNonNull(values, "values is null");
		return Stream.of(java.util.stream.Stream.of(values));
	}

	public static <T> Stream<T> iterate(final T seed, final UnaryOperator<T> f) {
		requireNonNull(f, "f is null");
		return Stream.of(java.util.stream.Stream.iterate(seed, f));
	}

	public static <T> Stream<T> generate(Supplier<T> supplier) {
		requireNonNull(supplier, "supplier is null");
		return Stream.of(java.util.stream.Stream.generate(supplier));
	}

	public static <T> Stream<T> concat(java.util.stream.Stream<? extends T> a, java.util.stream.Stream<? extends T> b) {
		requireNonNull(a, "stream a is null");
		requireNonNull(b, "stream b is null");
		return Stream.of(java.util.stream.Stream.concat(a, b));
	}
}
