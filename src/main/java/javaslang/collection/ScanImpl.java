package javaslang.collection;

import java.util.Objects;
import java.util.function.BiFunction;

class ScanImpl {

    static <A, B, X extends Traversable<A>> List<B> scanLeft(B zero, BiFunction<? super B, ? super A, ? extends B> operation, X thiz){
        Objects.requireNonNull(zero, "zero is null");
        Objects.requireNonNull(operation, "operation is null");
        List<B> builder = List.empty();
        B acc = zero;
        builder = builder.prepend(acc);
        for(A a: thiz) {
            acc = operation.apply(acc, a);
            builder = builder.prepend(acc);
        }
        return builder.reverse();
    }
    
    static <A, B, X extends Traversable<A>> List<B> scanRight(B zero, BiFunction<? super A, ? super B, ? extends B> operation, X thiz){
        Objects.requireNonNull(zero, "zero is null");
        Objects.requireNonNull(operation, "operation is null");
        List<B> scanned = List.of(zero);
        B acc = zero;
        for(A a: reverse(thiz)) {
            acc = operation.apply(a, acc);
            scanned = scanned.prepend(acc);
        }
        List<B> builder = List.empty();
        for(B elem: scanned) {
            builder = builder.prepend(elem);
        }
        return builder.reverse();
    }
    
    private static <A> Traversable<A> reverse(Traversable<A> t) {
        if(t instanceof Seq){
            return ((Seq<A>)t).reverse();
        } else {
            List<A> list = List.empty();
            for(A a: t) {
                list = list.prepend(a);
            }
            return list;
        }
    }
    
}
