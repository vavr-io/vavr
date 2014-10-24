/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.stream;

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
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javaslang.collection.Iterators;

/**
 * A sequential {@linkplain java.util.stream.Stream} implementation.
 */
// DEV-NOTE: Currently I see no benefit creating an interface for SStream
public final class SStream<T> implements Stream<T>, Iterable<T> {

	private final Stream<T> stream;

	private SStream(Stream<T> stream) {
		this.stream = stream;
	}

	// -- SStream API

	// TODO

	public SStream<T> limitUntil(Predicate<? super T> predicate) {
		return SStream.of(Iterators.of(iterator(), predicate.negate()));
	}

	// -- Stream impl (High-Level Stream API)

	@Override
	public SStream<T> filter(Predicate<? super T> predicate) {
		return SStream.of(stream.filter(predicate));
	}

	@Override
	public <R> SStream<R> map(Function<? super T, ? extends R> mapper) {
		return SStream.of(stream.map(mapper));
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
	public <R> SStream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
		return SStream.of(stream.flatMap(mapper));
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
	public SStream<T> distinct() {
		return SStream.of(stream.distinct());
	}

	@Override
	public SStream<T> sorted() {
		return SStream.of(stream.sorted());
	}

	@Override
	public SStream<T> sorted(Comparator<? super T> comparator) {
		return SStream.of(stream.sorted(comparator));
	}

	@Override
	public SStream<T> peek(Consumer<? super T> action) {
		return SStream.of(stream.peek(action));
	}

	@Override
	public SStream<T> limit(long maxSize) {
		return SStream.of(stream.limit(maxSize));
	}

	@Override
	public SStream<T> skip(long n) {
		return SStream.of(stream.skip(n));
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
	public SStream<T> onClose(Runnable closeHandler) {
		return SStream.of(stream.onClose(closeHandler));
	}

	/**
	 * Returns this, i.e. has no effect.
	 * 
	 * @return this
	 * @see java.util.stream.BaseStream#parallel()
	 */
	@Override
	public SStream<T> parallel() {
		return this;
	}

	/**
	 * Returns this, i.e. has no effect.
	 * 
	 * @return this
	 * @see java.util.stream.BaseStream#sequential()
	 */
	@Override
	public SStream<T> sequential() {
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
	public SStream<T> unordered() {
		return this;
	}

	// -- factory methods

	public static <T> SStream<T> of(Stream<T> stream) {
		requireNonNull(stream, "stream is null");
		return new SStream<>(stream);
	}

	public static <T> SStream<T> of(Spliterator<T> spliterator) {
		requireNonNull(spliterator, "spliterator is null");
		return SStream.of(StreamSupport.stream(spliterator, false));
	}

	public static <T> SStream<T> of(Iterable<T> iterable) {
		requireNonNull(iterable, "iterable is null");
		return SStream.of(iterable.spliterator());
	}

	public static <T> SStream<T> of(Iterator<T> iterator) {
		requireNonNull(iterator, "iterator is null");
		return SStream.of(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED));
	}

	// TODO: more factory methods (see java.util.stream.Stream)
}
