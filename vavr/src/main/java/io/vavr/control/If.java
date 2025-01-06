package io.vavr.control;

import io.vavr.NonePredicate;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Tool to simplify "if" code blocks.
 * <pre>{@code
 * String planet = getPlanet();
 * if (planet == null) {
 *     planet = "Earth";
 * }
 * }</pre>
 * <p>
 * To simplify this code, you can try using a ternary expression
 * <pre>{@code
 * String planet = null==getPlanet()?"Earth":"getPlanet()";
 * }</pre>
 * Obviously, calling the 'getPlanet' method twice is inappropriate.
 * <br/>
 * But you can simplify this code by using {@link #ifNew(Object, Predicate, Object)}  as follows
 * <pre>{@code
 *  String planet =  If.ifNew(getPlanet(),Objects::isNull,"Earth");
 * }</pre>
 *
 **/
@SuppressWarnings("unused")
public abstract class If implements Serializable {


    private static final long serialVersionUID = 1L;

    // sealed
    private If() {
    }


    /**
     * For a target, if the predicate is true, return ifNewT, otherwise return the target
     *
     * @param target    target
     * @param predicate predicate
     * @param ifNewT    ifNewT
     * @param <T>       value
     * @return target or ifNewT
     */
    public static <T> T ifNew(T target, Predicate<? super T> predicate, T ifNewT) {
        Objects.requireNonNull(predicate);
        if (predicate.test(target)) {
            return ifNewT;
        }
        return target;
    }


    public static <T> T ifNew(T target, Predicate<? super T> predicate, Supplier<T> supplier) {
        Objects.requireNonNull(supplier);
        return ifNew(target, predicate, supplier.get());
    }


    public static <T> T ifMap(T target, Predicate<? super T> predicate, Function<T, T> ifMapper) {
        Objects.requireNonNull(predicate);
        if (predicate.test(target)) {
            return ifMapper.apply(target);
        }
        return target;
    }

    public static <T, R> R ifNewElNew(T target, Predicate<? super T> predicate, R ifNewR, R elseNewR) {
        Objects.requireNonNull(predicate);
        if (predicate.test(target)) {
            return ifNewR;
        }
        return elseNewR;
    }


    public static <T, R> R ifNewElMap(T target, Predicate<? super T> predicate, R ifNewR, Function<T, R> elseMapper) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(elseMapper);
        if (predicate.test(target)) {
            return ifNewR;
        }
        return elseMapper.apply(target);
    }


    // Ternary Expression
    public static <R> R ifNewElNew(boolean predicate, R ifNewR, R elseNewR) {
        if (predicate) {
            return ifNewR;
        }
        return elseNewR;
    }

    public static <R> R ifNewElNew(NonePredicate predicate, R ifNewR, R elseNewR) {
        Objects.requireNonNull(predicate);
        return ifNewElNew(predicate.test(), ifNewR, elseNewR);
    }


    public static <T, R> R ifMapElMap(T target, Predicate<? super T> predicate, Function<T, R> ifMapper, Function<T, R> elseMapper) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(ifMapper);
        Objects.requireNonNull(elseMapper);
        if (predicate.test(target)) {
            return ifMapper.apply(target);
        }
        return elseMapper.apply(target);
    }


    public static <T, R> R ifMapElNew(T target, Predicate<? super T> predicate, Function<T, R> ifMapper, R elseNewR) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(ifMapper);
        if (predicate.test(target)) {
            return ifMapper.apply(target);
        }
        return elseNewR;
    }

    public static <T> T ifSelfElNew(T target, Predicate<? super T> predicate, T elseNew) {
        return ifMapElNew(target, predicate, t -> t, elseNew);
    }


    public static <T> void ifDo(T target, Predicate<? super T> predicate, Consumer<T> ifConsumer) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(ifConsumer);
        if (predicate.test(target)) {
            ifConsumer.accept(target);
        }
    }


    public static <T> void ifDo(T target, Predicate<? super T> predicate, Runnable runnable) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(runnable);
        if (predicate.test(target)) {
            runnable.run();
        }
    }


    public static void ifDo(boolean boo, Runnable runnable) {
        Objects.requireNonNull(runnable);
        if (boo) {
            runnable.run();
        }
    }


    public static <T> void ifDoElseDo(T target, Predicate<? super T> predicate, Consumer<T> ifConsumer, Consumer<T> elseConsumer) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(ifConsumer);
        Objects.requireNonNull(elseConsumer);
        if (predicate.test(target)) {
            ifConsumer.accept(target);
        } else {
            elseConsumer.accept(target);
        }
    }

    public static <T> void ifDoElseDo(T target, Predicate<? super T> predicate, Runnable ifRun, Runnable elseRun) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(ifRun);
        Objects.requireNonNull(elseRun);
        if (predicate.test(target)) {
            ifRun.run();
        } else {
            elseRun.run();
        }
    }

    public static <T> void ifDoElseDo(boolean flag, Runnable ifRun, Runnable elseRun) {
        Objects.requireNonNull(ifRun);
        Objects.requireNonNull(elseRun);
        if (flag) {
            ifRun.run();
        } else {
            elseRun.run();
        }
    }

    public static <T> void ifDoElseDo(T target, Predicate<? super T> predicate, Consumer<T> ifConsumer, Runnable elseRun) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(ifConsumer);
        Objects.requireNonNull(elseRun);
        if (predicate.test(target)) {
            ifConsumer.accept(target);
        } else {
            elseRun.run();
        }
    }

    public static <T> void ifDoElseDo(T target, Predicate<? super T> predicate, Runnable ifRun, Consumer<T> elseConsumer) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(ifRun);
        Objects.requireNonNull(elseConsumer);
        if (predicate.test(target)) {
            ifRun.run();
        } else {
            elseConsumer.accept(target);
        }
    }


}
