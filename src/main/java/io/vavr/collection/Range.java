/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2021 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.collection;
import java.util.NoSuchElementException;

/**
 * A {@code Range} represents a finite sequence of elements.
 * <p>
 * Example: Use a Range in a for-loop
 *
 * <pre>{@code
 * // prints '0' to '10' on the console
 * for (int i : Range.inclusive(0, 10)) {
 *     System.out.println(i);
 * }
 * }</pre>
 *
 * Example: Performing an action for each element, with index
 *
 * <pre>{@code
 * // prints '0: 10' to '9: 1' on the console
 * Range.exclusive(10, 0).forEachWithIndex((element, index) -> {
 *     System.out.println(index + ": " + element);
 * });
 * }</pre>
 *
 * Example: Generic conversion
 *
 * <pre>{@code
 * // = List(102, 100, 98)
 * List<Integer> intList = Range.exclusiveBy('f', 'a', -2).to(List::ofAll);
 *
 * // = List(f, d, b)
 * List<Character> charList = list.map(i -> (char) i.shortValue());
 * }</pre>
 *
 * @param <T> element type
 */
public interface Range<T> extends Iterable<T> {
	static Range<Integer> inclusive(Integer from, Integer toInclusive) {
		return Range.inclusiveBy(from, toInclusive, from <= toInclusive ? 1 : -1);
	}

	static Range<Integer> inclusiveBy(int from, int toInclusive, int step) {
		if (step == 0) {
			throw new IllegalArgumentException("step cannot be 0");
		} else if (from == toInclusive) {
			return () -> Iterator.of(from);
		}

		if (step > 0) {
			if (from > toInclusive) {
				return () -> Iterator.empty();
			} else {
				return () -> new IntegerForwardIterator(from, toInclusive, step, true);
			}
		} else {
			if (from < toInclusive) {
				return () -> Iterator.empty();
			} else {
				return () -> new IntegerBackwardIterator(from, toInclusive, step, true);
			}
		}
	}

	static Range<Integer> exclusive(int from, int toExclusive) {
		return Range.exclusiveBy(from, toExclusive, from <= toExclusive ? 1 : -1);
	}

	static Range<Integer> exclusiveBy(int from, int toExclusive, int step) {
		int signum = Integer.signum(step);
		int toInclusive = toExclusive - signum;
		if (Integer.signum(toInclusive) != Integer.signum(toExclusive)) {
			// because of abs(signum) <= abs(step) and overflow detection, toExclusive will not be included
			return Range.inclusiveBy(from, toExclusive, step);
		} else {
			return Range.inclusiveBy(from, toInclusive, step);
		}
	}

	// TODO: Byte
	// TODO: Short
	// Long
	static Range<Long> exclusiveBy(long from, long toExclusive, long step) {
		if (step == 0) {
			throw new IllegalArgumentException("step cannot be 0");
		}

		return step > 0
				? () -> new LongForwardIterator(from, toExclusive, step, false)
				: () -> new LongBackwardIterator(from, toExclusive, step, false);
	}

	static Range<Long> exclusive(long from, long toExclusive) {
		return exclusiveBy(from, toExclusive, 1L);
	}

	static Range<Long> inclusiveBy(Long from, Long toInclusive, long step) {
		if (step == 0) {
			throw new IllegalArgumentException("step cannot be 0");
		}

		return step > 0
				? () -> new LongForwardIterator(from, toInclusive, step, true)
				: () -> new LongBackwardIterator(from, toInclusive, step, true);
	}

	static Range<Long> inclusive(Long from, Long toInclusive) {
		return inclusiveBy(from, toInclusive, 1L);
	}

	// Character
	static Range<Character> exclusiveBy(char from, char toExclusive, int step) {
		if (step == 0) {
			throw new IllegalArgumentException("step cannot be 0");
		}

		return step > 0
				? () -> new CharacterForwardIterator(from, toExclusive, step, false)
				: () -> new CharacterBackwardIterator(from, toExclusive, step, false);
	}

	static Range<Character> exclusive(char from, char toExclusive) {
		return exclusiveBy(from, toExclusive, 1);
	}

	static Range<Character> inclusiveBy(char from, char toInclusive, int step) {
		if (step == 0) {
			throw new IllegalArgumentException("step cannot be 0");
		}

		return step > 0
				? () -> new CharacterForwardIterator(from, toInclusive, step, true)
				: () -> new CharacterBackwardIterator(from, toInclusive, step, true);
	}

	static Range<Character> inclusive(char from, char toInclusive) {
		return inclusiveBy(from, toInclusive, 1);
	}

	// TODO: Float
	// TODO: Double
	// TODO: BigDecimal
}

final class IntegerForwardIterator implements Iterator<Integer> {
	private final int start;
	private final int end;
	private final int step;
	private final boolean inclusive;
	private int next;
	private boolean overflow;

	IntegerForwardIterator(int start, int end, int step, boolean inclusive) {
		this.start = start;
		this.next = start;
		this.end = end;
		this.step = step;
		this.inclusive = inclusive;
	}

	@Override
	public boolean hasNext() {
		if (start > end) {
			return false;
		}
		if (inclusive) {
			return !overflow && next <= end;
		} else {
			return !overflow && next < end;
		}
	}

	@Override
	public Integer next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		int curr = next;
		int r = curr + step;
		overflow = ((curr ^ r) & (step ^ r)) < 0;
		next = r;
		return curr;
	}
}

final class IntegerBackwardIterator implements Iterator<Integer> {
	private final int start;
	private final int end;
	private final int step;
	private final boolean inclusive;
	private int next;
	private boolean underflow;

	IntegerBackwardIterator(int start, int end, int step, boolean inclusive) {
		this.start = start;
		this.next = start;
		this.end = end;
		this.step = step;
		this.inclusive = inclusive;
	}

	@Override
	public boolean hasNext() {
		if (start < end) {
			return false;
		}
		if (inclusive) {
			return !underflow && next >= end;
		} else {
			return !underflow && next > end;
		}
	}

	@Override
	public Integer next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		int curr = next;
		int r = curr + step;
		underflow = ((curr ^ r) & (step ^ r)) < 0;
		next = r;
		return curr;
	}
}

final class LongForwardIterator implements Iterator<Long> {
	private final long start;
	private final long end;
	private final long step;
	private final boolean inclusive;
	private long next;
	private boolean overflow;

	LongForwardIterator(long start, long end, long step, boolean inclusive) {
		this.start = start;
		this.next = start;
		this.end = end;
		this.step = step;
		this.inclusive = inclusive;
	}

	@Override
	public boolean hasNext() {
		if (start > end) {
			return false;
		}
		if (inclusive) {
			return !overflow && next <= end;
		} else {
			return !overflow && next < end;
		}
	}

	@Override
	public Long next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		long curr = next;
		long r = curr + step;
		overflow = ((curr ^ r) & (step ^ r)) < 0;
		next = r;
		return curr;
	}
}

final class LongBackwardIterator implements Iterator<Long> {
	private final long start;
	private final long end;
	private final long step;
	private final boolean inclusive;
	private long next;
	private boolean underflow;

	LongBackwardIterator(long start, long end, long step, boolean inclusive) {
		this.start = start;
		this.next = start;
		this.end = end;
		this.step = step;
		this.inclusive = inclusive;
	}

	@Override
	public boolean hasNext() {
		if (start < end) {
			return false;
		}
		if (inclusive) {
			return !underflow && next >= end;
		} else {
			return !underflow && next > end;
		}
	}

	@Override
	public Long next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		long curr = next;
		long r = curr + step;
		underflow = ((curr ^ r) & (step ^ r)) < 0;
		next = r;
		return curr;
	}
}

final class CharacterForwardIterator implements Iterator<Character> {
	private final char start;
	private final char end;
	private final int step;
	private final boolean inclusive;
	private char next;
	private boolean overflow;

	CharacterForwardIterator(char start, char end, int step, boolean inclusive) {
		this.start = start;
		this.next = start;
		this.end = end;
		this.step = step;
		this.inclusive = inclusive;
	}

	@Override
	public boolean hasNext() {
		if (start > end) {
			return false;
		}
		if (inclusive) {
			return !overflow && next <= end;
		} else {
			return !overflow && next < end;
		}
	}

	@Override
	public Character next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		char curr = next;
		char r = (char)(curr + step);
		overflow = ((curr ^ r) & (step ^ r)) < 0;
		next = r;
		return curr;
	}
}

final class CharacterBackwardIterator implements Iterator<Character> {
	private final char start;
	private final char end;
	private final int step;
	private final boolean inclusive;
	private char next;
	private boolean underflow;

	CharacterBackwardIterator(char start, char end, int step, boolean inclusive) {
		this.start = start;
		this.next = start;
		this.end = end;
		this.step = step;
		this.inclusive = inclusive;
	}

	@Override
	public boolean hasNext() {
		if (start < end) {
			return false;
		}
		if (inclusive) {
			return !underflow && next >= end;
		} else {
			return !underflow && next > end;
		}
	}

	@Override
	public Character next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		char curr = next;
		char r = (char)(curr + step);
		underflow = ((curr ^ r) & (step ^ r)) < 0;
		next = r;
		return curr;
	}
}