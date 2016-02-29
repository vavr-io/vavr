/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.List;
import javaslang.control.Option;
import javaslang.match.Unapply;

@javaslang.match.Patterns
class $ {

    // Option
    @Unapply
    static <T> Tuple1<T> Some(Option.Some<T> some) { return Tuple.of(some.get()); }

    @Unapply
    static Tuple0 None(Option.None<?> none) { return Tuple.empty(); }

    // List
    @Unapply
    static <T> Tuple2<T, List<T>> Cons(List.Cons<T> cons) { return Tuple.of(cons.head(), cons.tail()); }

    @Unapply
    static Tuple0 Nil(List.Nil<?> nil) { return Tuple.empty(); }

}
