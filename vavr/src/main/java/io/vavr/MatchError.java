/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package io.vavr;

import java.util.NoSuchElementException;

/**
 * A {@link API.Match} throws a MatchError if no case matches the applied object.
 *
 * @author Daniel Dietrich
 * @since 1.0.0
 */
public class MatchError extends NoSuchElementException {

    private static final long serialVersionUID = 1L;

    private final Object obj;

    /**
     * Internally called by {@link API.Match}.
     *
     * @param obj The object which could not be matched.
     */
    MatchError(Object obj) {
        super((obj == null) ? "null" : "type: " + obj.getClass().getName() + ", value: " + obj);
        this.obj = obj;
    }

    /**
     * Returns the object which could not be matched.
     *
     * @return An Object.
     */
    public Object getObject() {
        return obj;
    }

}
