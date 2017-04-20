/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package io.vavr;

/**
 * This exception is temporarily used during development in order to indicate that an implementation is missing.
 * <p>
 * The idiomatic way is to use one of {@link API#TODO()} and {@link API#TODO(String)}.
 */
public class NotImplementedError extends Error {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a {@code NotImplementedError} containing the message "an implementation is missing".
     */
    public NotImplementedError() {
        super("An implementation is missing.");
    }

    /**
     * Creates a {@code NotImplementedError} containing the given {@code message}.
     *
     * @param message A text that describes the error
     */
    public NotImplementedError(String message) {
        super(message);
    }
}
