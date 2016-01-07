/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.Function2;
import javaslang.Function3;
import javaslang.Function4;

public class ValidationBuilder<E,T1,T2> {

    private Validation<E,T1> v1;
    private Validation<E,T2> v2;

    public ValidationBuilder(Validation<E,T1> v1, Validation<E,T2> v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public <R> Validation<E,R> ap(Function2<T1,T2,R> f) {
        return v2.ap(v1.ap(Validation.success(f.curried())));
    }

    public <T3> ValidationBuilder3<E,T1,T2,T3> combine(Validation<E,T3> v3) {
        return new ValidationBuilder3<>(v1, v2, v3);
    }


    public static class ValidationBuilder3<E,T1,T2,T3> {

        private Validation<E,T1> v1;
        private Validation<E,T2> v2;
        private Validation<E,T3> v3;

        public ValidationBuilder3(Validation<E,T1> v1, Validation<E,T2> v2, Validation<E,T3> v3) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
        }

        public <R> Validation<E,R> ap(Function3<T1,T2,T3,R> f) {
            return v3.ap(v2.ap(v1.ap(Validation.success(f.curried()))));
        }

        public <T4> ValidationBuilder4<E,T1,T2,T3,T4> combine(Validation<E,T4> v4) {
            return new ValidationBuilder4<>(v1, v2, v3, v4);
        }


        public static class ValidationBuilder4<E,T1,T2,T3,T4> {

            private Validation<E,T1> v1;
            private Validation<E,T2> v2;
            private Validation<E,T3> v3;
            private Validation<E,T4> v4;

            public ValidationBuilder4(Validation<E,T1> v1, Validation<E,T2> v2, Validation<E,T3> v3, Validation<E,T4> v4) {
                this.v1 = v1;
                this.v2 = v2;
                this.v3 = v3;
                this.v4 = v4;
            }

            public <R> Validation<E,R> ap(Function4<T1,T2,T3,T4,R> f) {
                return v4.ap(v3.ap(v2.ap(v1.ap(Validation.success(f.curried())))));
            }

        }

    }

}
