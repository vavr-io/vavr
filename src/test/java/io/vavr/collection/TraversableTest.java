package io.vavr.collection;

class TraversableTest {
}

@SuppressWarnings("unused")
final class ShouldJustCompile {

    private ShouldJustCompile() {
        throw new Error("Should just compile.");
    }

    <T> LinearSeq<T> toLinearSeq(Iterable<? extends T> iterable) {
        return null;
    }

    void shouldConvertIndexedSeqToIterable(IndexedSeq<String> indexedSeq) {
        { // Traversable
            Traversable<String> traversable1 = indexedSeq.to(this::toLinearSeq);
            Traversable<CharSequence> traversable2 = indexedSeq.to(this::toLinearSeq);
        }
        { // Seq
            Seq<String> seq1 = indexedSeq.to(this::toLinearSeq);
            Seq<CharSequence> seq2 = indexedSeq.to(this::toLinearSeq);
        }
        { // LinearSeq
            LinearSeq<String> linearSeq = indexedSeq.to(this::toLinearSeq);
            LinearSeq<CharSequence> seq = indexedSeq.to(this::toLinearSeq);
        }
    }

}
