/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package io.vavr;

import org.assertj.core.api.Assertions;

import java.lang.reflect.Constructor;

public final class AssertionsExtensions {

    private AssertionsExtensions() {
    }

    public static ClassAssert assertThat(Class<?> clazz) {
        return new ClassAssert(clazz);
    }

    public static class ClassAssert {

        final Class<?> clazz;

        ClassAssert(Class<?> clazz) {
            this.clazz = clazz;
        }

        @SuppressWarnings("deprecation")
        public void isNotInstantiable() {
            final Constructor<?> cons;
            try {
                cons = clazz.getDeclaredConstructor();
                Assertions.assertThat(cons.isAccessible()).isFalse();
            } catch (NoSuchMethodException e) {
                throw new AssertionError("no default constructor found");
            }
        }
    }
}
