/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.MatchTest.Developer;
import javaslang.collection.List;
import javaslang.control.Option;

@Patterns
class My {

    // Option
    @Unapply static <T> Tuple1<T> Some(Option.Some<T> some) { return Tuple.of(some.get()); }
    @Unapply static Tuple0 None(Option.None<?> none) { return Tuple.empty(); }

    // List
    @Unapply static <T> Tuple2<T, List<T>> Cons(List.Cons<T> cons) { return Tuple.of(cons.head(), cons.tail()); }
    @Unapply static Tuple0 Nil(List.Nil<?> nil) { return Tuple.empty(); }

    // Developer
    @Unapply static Tuple2<String, Boolean> Developer(Developer dev) { return Tuple.of(dev.getName(), dev.isCaffeinated()); }

    // TEST!
    void non_static_method() {}

    Tuple2<String, Boolean> no_annotation(Developer dev) { return Tuple.of(dev.getName(), dev.isCaffeinated()); }

}
