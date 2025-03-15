package io.vavr;

import io.vavr.control.Option;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Optional-like structural pattern matching for Java。
 * <pre>
 * <code>
 *      MatchV2.of(matchString, "def")
 *          .caseNull("is null")
 *          .caseValue("null", "null")
 *          .caseValue(String::isEmpty, "is empty")
 *          .caseValue(StringUtils::isBlank, ()->"is blank");
 * </code>
 * </pre>
 *
 * @param <T> The type of the match value of an MatchV2.
 * @param <R> The type of the return/def value of an MatchV2.
 * @author tanglt
 */
public class MatchV2<T,R> {


    /**
     * match value
     */
    private final T matchValue;

    /**
     * return value
     */
    private final R returnValue;

    /**
     * matches the condition is true
     */
    private final boolean hit;

    /**
     * default return value
     */
    private final R defValue;

    private MatchV2(final T matchValue, final R defValue) {
        this.matchValue = matchValue;
        this.returnValue = null;
        this.defValue = defValue;
        this.hit = false;
    }


    private MatchV2(final T matchValue, final R returnValue, final R defValue, boolean hit) {
        this.matchValue = matchValue;
        this.returnValue = returnValue;
        this.defValue = defValue;
        this.hit = hit;
    }

    /**
     * 一个空的{@code Match}
     */
//    private static final MatchV2<?,?> EMPTY = new MatchV2<>(null,null);

    /**
     * 返回一个空的{@code Match}
     *
     * @return Match
     */
//    public static <T,R> MatchV2<T,R> empty() {
//        @SuppressWarnings("unchecked")
//        final MatchV2<T,R> t = (MatchV2<T,R>) EMPTY;
//        return t;
//    }


    /**
     * Creates a new {@code MatchV2} of a match value.
     *
     * @param matchValue A match value
     * @param defValue A default value
     * @param <T>   type of the value
     * @return {@code MatchV2}
     */
    public static <T,R> MatchV2<T,R> of(final T matchValue, final R defValue) {
        return new MatchV2<>(matchValue,defValue);
    }

    /**
     * Creates a new {@code MatchV2} of a match value. not set default
     *
     * @param matchValue A match value
     * @param <T>   type of the value
     * @return {@code MatchV2}
     */
    public static <T,R> MatchV2<T,R> of(final T matchValue) {
        return new MatchV2<>(matchValue,null);
    }

    /**
     * Maps the value to a new {@code MatchV2}.Two match statements can be concatenated.
     * where if the first match condition is met, the second match becomes invalid.
     * Otherwise, the second match takes effect.
     *
     * @param mapper A mapper
     * @param <M>    a new matchValue type
     * @return a new {@code Option}
     */
    public <M> MatchV2<M,R> flatMap(final BiFunction<? super T, ? super R, ? extends MatchV2<M,R>> mapper) {
        Objects.requireNonNull(mapper);
        MatchV2<M, R> mrMatchV2 = mapper.apply(this.matchValue ,isPresent()?this.returnValue:this.defValue);
        if (isPresent()) {
            return new MatchV2<>(mrMatchV2.matchValue,this.returnValue, mrMatchV2.defValue,true);
        } else {
            return mrMatchV2;
        }
    }

    /**
     * Maps the matchValue to a new {@code MatchV2}.
     * @param matchMapper A mapper
     * @param <U>    a new matchValue type
     * @return a new {@code MatchV2}
     */
    public <U> MatchV2<U,R> mapMatch(final Function<? super T, ? extends U> matchMapper) {
        Objects.requireNonNull(matchMapper);
        return new MatchV2<>(matchMapper.apply(this.matchValue),this.returnValue,this.defValue,this.hit);
    }

    /**
     * Maps the returnValue to a new {@code MatchV2}.
     * @param returnMapper A mapper
     * @param <F>    a new returnValue type
     * @return a new {@code MatchV2}
     */
    public <F> MatchV2<T,F> map(final Function<? super R, ? extends F> returnMapper) {
        Objects.requireNonNull(returnMapper);
        if(isPresent()){
            F returnValue = returnMapper.apply(this.returnValue);
            return new MatchV2<>(this.matchValue,returnValue,returnValue,this.hit);
        }else {
            F defValue = returnMapper.apply(this.defValue);
            return new MatchV2<>(this.matchValue,null,defValue,this.hit);
        }
    }

    /**
     * Evaluate the condition and specify the return value.
     * @param caseValue A case value
     * @param returnValue  a return value
     * @return a new {@code MatchV2}
     */
    public MatchV2<T,R> caseValue(final T caseValue, final R returnValue) {
        Objects.requireNonNull(caseValue);
        if(isPresent()||!Objects.equals(this.matchValue,caseValue)){
            return this;
        }
        return new MatchV2<>(this.matchValue,returnValue,this.defValue,true);
    }

    /**
     * Evaluate the condition and specify the return value.
     * @param predicate A Predicate Function
     * @param returnValue  a return value
     * @return a new {@code MatchV2}
     */
    public MatchV2<T,R> caseValue(final Predicate<? super T> predicate, final R returnValue) {
        Objects.requireNonNull(predicate);
        if(isPresent()||!predicate.test(this.matchValue)){
            return this;
        }
        return new MatchV2<>(this.matchValue,returnValue,this.defValue,true);
    }

    /**
     * Evaluate the condition and specify the return value.
     * @param predicate A Predicate Function
     * @param supplier  a return supplier
     * @return a new {@code MatchV2}
     */
    public MatchV2<T,R> caseValue(final Predicate<? super T> predicate, final Supplier<R> supplier) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(supplier);
        if(isPresent()||!predicate.test(this.matchValue)){
            return this;
        }
        return new MatchV2<>(this.matchValue,supplier.get(),this.defValue,true);
    }

    /**
     * Check null and specify the return value.
     * @param returnValue  a return value
     * @return a new {@code MatchV2}
     */
    public MatchV2<T,R> caseNull(final R returnValue) {
        if(isPresent()||!Objects.isNull(this.matchValue)){
            return this;
        }
        return new MatchV2<>(null,returnValue,this.defValue,true);
    }

    /**
     * Check null and specify the return value.
     * @param supplier  a return supplier
     * @return a new {@code MatchV2}
     */
    public MatchV2<T,R> caseNull(final Supplier<R> supplier) {
        Objects.requireNonNull(supplier);
        if(isPresent()||!Objects.isNull(this.matchValue)){
            return this;
        }
        return new MatchV2<>(null,supplier.get(),this.defValue,true);
    }

    /**
     * get matches the condition value or default value.
     * @return <R>  the value that matches the condition or default value
     */
    public R get() {
        return isPresent() ? this.returnValue : this.defValue;
    }


    /**
     * Get the value that meets the condition. If there is none, use the other value.
     * @param other  a other value
     * @return <R>  the value that matches the condition or default value
     */
    public R orElse(final R other) {
        return isPresent() ? this.returnValue : other;
    }

    /**
     * Get the value that meets the condition. If there is none, use the other value.
     * @param supplier  a supplier Function
     * @return <R>  the value that matches the condition or default value
     */
    public R orElseGet(final Supplier<? extends R> supplier) {
        return isPresent() ? this.returnValue : supplier.get();
    }

    /**
     * Get the value that meets the condition. If there is none,  raise an exception.
     * @param exceptionSupplier  a  Throwable supplier Function
     * @return <R>  the value that matches the condition or default value
     */
    public <X extends Throwable> R orElseThrow(final Supplier<? extends X> exceptionSupplier) throws X{
        if (isPresent()) {
            return this.returnValue;
        }
        throw exceptionSupplier.get();
    }

    /**
     * Check if the matching condition is satisfied.
     * @return boolean
     */
    public boolean isPresent() {
        return this.hit;
    }


    @Override
    public boolean equals(Object o) {
        return o == this || ( o instanceof Option.Some && Objects.equals(o.toString(),this.toString()));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.toString());
    }

    @Override
    public String toString() {
        return "matchValue("
                +this.matchValue+
                "),returnValue("
                +this.returnValue+
                "),hit("
                +this.hit+
                "),defValue("
                +this.defValue+")";
    }
}
