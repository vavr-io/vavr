/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */

var generate = function(package, _class) { return <<CODE
${package.isDefined().gen("package ${package};")}

public final class ${_class} {

    // hidden
    private ${_class}() {
    }

}
CODE}

// TODO:

    // Tuple0 Nil(List.Nil<?> nil)
    // if (arity == 0) {
    // static Pattern0 ${model.name} = new Pattern0() {
    //     @Override
    //     public Option<Void> apply(Object o) {
    //         return (o instanceof model.paramType) ? Option.nothing() : Option.none();
    //     }
    // };

// <T> Tuple2<T, List<T>> Cons(List.Cons<T> cons)

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

// @Unapply static <T> Tuple2<T, List<T>> Cons(List.Cons<T> cons)
// @Unapply static     Tuple0    Nil(List.Nil<?> nil)

// static Pattern0 Nil = new Pattern0() {
//     @Override
//     public Option<Void> apply(Object o) {
//         return (o instanceof List.Nil) ? Option.nothing() : Option.none();
//     }
// };

// static                                Pattern0                      List(Pattern0 p1, Pattern0 p2)
// static                                Pattern1                      List(Pattern0 p1, Pattern1<? extends List<U>> p2)
// static                                Pattern1                      List(Pattern0 p1, InversePattern<? extends List<U>> p2)
// static <T extends List<U>, U, T1>     Pattern1<List<U>, T1>         List(Pattern1<? extends U, T1> p1, Pattern0 p2)
// static <T extends List<U>, U, T1, T2> Pattern2<List<U>, T1, T2>     List(Pattern1<? extends U, T1> p1, Pattern1<? extends List<U>, T2> p2)
// static <T extends List<U>, U>         Pattern1<List<U>, U>          List(InversePattern<? extends U> head, Pattern0 tail) {
// static <T extends List<U>, U>         Pattern2<List<U>, U, List<U>> List(InversePattern<? extends U> head, Pattern1<? extends List<U>> tail)
// static <T extends List<U>, U>         Pattern2<List<U>, U, List<U>> List(InversePattern<? extends U> head, InversePattern<List<U>> tail)
// ...

// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -