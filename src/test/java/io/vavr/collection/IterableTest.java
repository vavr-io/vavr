package io.vavr.collection;

class IterableTest {
}

@SuppressWarnings("unused")
final class ShouldJustCompile {

    private ShouldJustCompile() {
        throw new Error("Should just compile.");
    }

    <T> LinearSeq<T> toLinearSeq(java.lang.Iterable<? extends T> iterable) {
        return null;
    }

    void shouldConvertIndexedSeqToIterable(IndexedSeq<String> indexedSeq) {
        { // Iterable
            Iterable<String> iterable1 = indexedSeq.to(this::toLinearSeq);
            Iterable<CharSequence> iterable2 = indexedSeq.to(this::toLinearSeq);
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
