/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package io.vavr.control;

import io.vavr.AbstractValueTest;
import io.vavr.collection.Seq;
import io.vavr.collection.List;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;

import static io.vavr.API.Left;
import static io.vavr.API.Right;

public class EitherTest extends AbstractValueTest {

    // -- AbstractValueTest

    @Override
    protected <T> Either<?, T> empty() {
        return Either.<T, T> left(null);
    }

    @Override
    protected <T> Either<?, T> of(T element) {
        return Either.<T, T> right(element);
    }

    @SafeVarargs
    @Override
    protected final <T> Either<?, T> of(T... elements) {
        return of(elements[0]);
    }

    @Override
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return true;
    }

    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    // -- Either

    @Test
    public void shouldBimapLeft() {
        final Either<Integer, String> actual = Either.<Integer, String> left(1).bimap(i -> i + 1, s -> s + "1");
        final Either<Integer, String> expected = Either.left(2);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldBimapLeftProjection() {
        final Either.LeftProjection<Integer, String> actual = Either.<Integer, String> left(1).left().bimap(i -> i + 1, s -> s + "1");
        final Either<Integer, String> expected = Either.left(2);
        assertThat(actual.get()).isEqualTo(expected.getLeft());
    }

    @Test
    public void shouldBimapRight() {
        final Either<Integer, String> actual = Either.<Integer, String> right("1").bimap(i -> i + 1, s -> s + "1");
        final Either<Integer, String> expected = Either.right("11");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldBimapRightProjection() {
        final Either.RightProjection<Integer, String> actual = Either.<Integer, String> right("1").right().bimap(i -> i + 1, s -> s + "1");
        final Either<Integer, String> expected = Either.right("11");
        assertThat(actual.get()).isEqualTo(expected.get());
    }

    @Test
    public void shouldFoldLeft() {
        final String value = Either.left("L").fold(l -> l + "+", r -> r + "-");
        assertThat(value).isEqualTo("L+");
    }

    @Test
    public void shouldFoldRight() {
        final String value = Either.right("R").fold(l -> l + "-", r -> r + "+");
        assertThat(value).isEqualTo("R+");
    }

    // -- sequence

    @Test
    public void shouldConvertListOfRightToEitherOfList() {
        List<Either<String, String>> tries = List.of(Either.right("a"), Either.right("b"), Either.right("c"));
        Either<String, Seq<String>> reducedEither = Either.sequence(tries);
        assertThat(reducedEither instanceof Either.Right).isTrue();
        assertThat(reducedEither.get().size()).isEqualTo(3);
        assertThat(reducedEither.get().mkString()).isEqualTo("abc");
    }

    @Test
    public void shouldConvertListOfLeftToEitherOfList() {
        List<Either<Integer, String>> tries = List.of(Either.left(1), Either.left(2), Either.left(3));
        Either<Integer, Seq<String>> reducedEither = Either.sequence(tries);
        assertThat(reducedEither).isEqualTo(Either.left(1));
    }

    @Test
    public void shouldConvertListOfMixedEitherToEitherOfList() {
        List<Either<Integer,String>> tries = List.of(Either.right("a"), Either.left(1), Either.right("c"));
        Either<Integer, Seq<String>> reducedEither = Either.sequence(tries);
        assertThat(reducedEither).isEqualTo(Either.left(1));
    }

    @Test
    public void shouldReturnSameWhenCallingMapOnLeft() {
        final Either<Integer, Object> actual = Left(1);
        assertThat(actual.map(v -> { throw new IllegalStateException(); })).isSameAs(actual);
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowIfRightGetLeft() {
        Right(1).getLeft();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowIfLeftGet() {
        Left(1).get();
    }

    @Test
    public void shouldSwapLeft() {
        assertThat(Either.left(1).swap()).isEqualTo(Either.right(1));
    }

    @Test
    public void shouldSwapRight() {
        assertThat(Either.right(1).swap()).isEqualTo(Either.left(1));
    }

    // -- Either.narrow

    @Test
    public void shouldNarrowRightEither() {
        Either<String, Integer> either = Either.right(42);
        Either<CharSequence, Number> narrow = Either.narrow(either);
        assertThat(narrow.get()).isEqualTo(42);
    }

    @Test
    public void shouldNarrowLeftEither() {
        Either<String, Integer> either = Either.left("vavr");
        Either<CharSequence, Number> narrow = Either.narrow(either);
        assertThat(narrow.getLeft()).isEqualTo("vavr");
    }

    // orElse

    @Test
    public void shouldEitherOrElseEither() {
        assertThat(Either.right(1).orElse(Either.right(2)).get()).isEqualTo(1);
        assertThat(Either.left(1).orElse(Either.right(2)).get()).isEqualTo(2);
    }

    @Test
    public void shouldEitherOrElseSupplier() {
        assertThat(Either.right(1).orElse(() -> Either.right(2)).get()).isEqualTo(1);
        assertThat(Either.left(1).orElse(() -> Either.right(2)).get()).isEqualTo(2);
    }

    // -- Left

    @Test
    public void shouldReturnTrueWhenCallingIsLeftOnLeft() {
        assertThat(Either.left(1).isLeft()).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenCallingIsRightOnLeft() {
        assertThat(Either.left(1).isRight()).isFalse();
    }

    // -- filter

    @Test
    public void shouldFilterRight() {
        Either<String, Integer> either = Either.right(42);
        assertThat(either.filter(i -> true).get()).isSameAs(either);
        assertThat(either.filter(i -> false)).isSameAs(Option.none());
    }

    @Test
    public void shouldFilterLeft() {
        Either<String, Integer> either = Either.left("vavr");
        assertThat(either.filter(i -> true).get()).isSameAs(either);
        assertThat(either.filter(i -> false).get()).isSameAs(either);
    }

    // -- flatMap

    @Test
    public void shouldFlatMapRight() {
        Either<String, Integer> either = Either.right(42);
        assertThat(either.flatMap(v -> Either.right("ok")).get()).isEqualTo("ok");
    }

    @Test
    public void shouldFlatMapLeft() {
        Either<String, Integer> either = Either.left("vavr");
        assertThat(either.flatMap(v -> Either.right("ok"))).isSameAs(either);
    }

    // -- peekLeft

    @Test
    public void shouldPeekLeftNil() {
        assertThat(empty().peekLeft(t -> {})).isEqualTo(empty());
    }

    @Test
    public void shouldPeekLeftForLeft() {
        final int[] effect = { 0 };
        final Either<Integer, ?> actual = Either.left(1).peekLeft(i -> effect[0] = i);
        assertThat(actual).isEqualTo(Either.left(1));
        assertThat(effect[0]).isEqualTo(1);
    }

    @Test
    public void shouldNotPeekLeftForRight() {
        Either.right(1).peekLeft(i -> { throw new IllegalStateException(); });
    }

    // equals

    @Test
    public void shouldEqualLeftIfObjectIsSame() {
        final Either<Integer, ?> left = Either.left(1);
        assertThat(left.equals(left)).isTrue();
    }

    @Test
    public void shouldNotEqualLeftIfObjectIsNull() {
        assertThat(Either.left(1).equals(null)).isFalse();
    }

    @Test
    public void shouldNotEqualLeftIfObjectIsOfDifferentType() {
        assertThat(Either.left(1).equals(new Object())).isFalse();
    }

    @Test
    public void shouldEqualLeft() {
        assertThat(Either.left(1)).isEqualTo(Either.left(1));
    }

    // hashCode

    @Test
    public void shouldHashLeft() {
        assertThat(Either.left(1).hashCode()).isEqualTo(Objects.hashCode(1));
    }

    // toString

    @Test
    public void shouldConvertLeftToString() {
        assertThat(Either.left(1).toString()).isEqualTo("Left(1)");
    }

    // -- Right

    @Test
    public void shouldReturnTrueWhenCallingIsRightOnRight() {
        assertThat(Either.right(1).isRight()).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenCallingIsLeftOnRight() {
        assertThat(Either.right(1).isLeft()).isFalse();
    }

    // equals

    @Test
    public void shouldEqualRightIfObjectIsSame() {
        final Either<?, ?> right = Either.right(1);
        assertThat(right.equals(right)).isTrue();
    }

    @Test
    public void shouldNotEqualRightIfObjectIsNull() {
        assertThat(Either.right(1).equals(null)).isFalse();
    }

    @Test
    public void shouldNotEqualRightIfObjectIsOfDifferentType() {
        assertThat(Either.right(1).equals(new Object())).isFalse();
    }

    @Test
    public void shouldEqualRight() {
        assertThat(Either.right(1)).isEqualTo(Either.right(1));
    }

    // -- toValidation

    @Test
    public void shouldConvertToValidValidation() {
        final Validation<?, Integer> validation = Either.right(42).toValidation();
        assertThat(validation.isValid()).isTrue();
        assertThat(validation.get()).isEqualTo(42);
    }

    @Test
    public void shouldConvertToInvalidValidation() {
        final Validation<String, ?> validation = Either.left("vavr").toValidation();
        assertThat(validation.isInvalid()).isTrue();
        assertThat(validation.getError()).isEqualTo("vavr");
    }

    // hashCode

    @Test
    public void shouldHashRight() {
        assertThat(Either.right(1).hashCode()).isEqualTo(Objects.hashCode(1));
    }

    // toString

    @Test
    public void shouldConvertRightToString() {
        assertThat(Either.right(1).toString()).isEqualTo("Right(1)");
    }


    // -- spliterator

    @Test
    public void shouldHaveSizedSpliterator() {
        assertThat(of(1).spliterator().hasCharacteristics(Spliterator.SIZED | Spliterator.SUBSIZED)).isTrue();
    }

    @Test
    public void shouldHaveOrderedSpliterator() {
        assertThat(of(1).spliterator().hasCharacteristics(Spliterator.ORDERED)).isTrue();
    }

    @Test
    public void shouldReturnSizeWhenSpliterator() {
        assertThat(of(1).spliterator().getExactSizeIfKnown()).isEqualTo(1);
    }
}
