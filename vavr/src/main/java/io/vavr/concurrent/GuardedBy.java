/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package io.vavr.concurrent;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <strong>INTERNAL API - Used for documentation purpose only.</strong>
 * <p>
 * An annotated field or method must only be accessed when holding the lock specified in {@code value()}.
 * <p>
 * This is an annotation known from <a href="http://jcip.net">Java Concurrency in Practice</a>.
 * See also <a href="https://jcp.org/en/jsr/detail?id=305">JSR 305</a>
 *
 * @author Daniel Dietrich
 * @since 2.1.0
 */
@Documented
@Target(value = { FIELD, METHOD })
@Retention(RUNTIME)
@interface GuardedBy {

    /**
     * Specifies the lock that guards the annotated field or method.
     *
     * @return a valid lock that guards the annotated field or method
     */
    String value();
}
