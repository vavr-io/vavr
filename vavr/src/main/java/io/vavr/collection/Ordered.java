/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package io.vavr.collection;

import java.util.Comparator;

/**
 * An ordered collection interface.
 *
 * @param <T> Component type
 * @author Ruslan Sennov, Daniel Dietrich
 * @since 2.1.0
 */
public interface Ordered<T> {

    /**
     * Returns the comparator which defines the order of the elements contained in this collection.
     *
     * @return The comparator that defines the order of this collection's elements.
     */
    Comparator<T> comparator();
}
