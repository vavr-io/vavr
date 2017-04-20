/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package io.vavr.match.model;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;

/**
 * Representation of a method parameter.
 *
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public class ParameterModel {

    private final Elements elementUtils;
    private final VariableElement variableElement;

    public ParameterModel(Elements elementUtils, VariableElement variableElement) {
        this.elementUtils = elementUtils;
        this.variableElement = variableElement;
    }

    public ClassModel getType() {
        return new ClassModel(elementUtils, (DeclaredType) variableElement.asType());
    }

    @Override
    public boolean equals(Object o) {
        return (o == this) || (o instanceof ParameterModel && toString().equals(o.toString()));
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return variableElement.toString();
    }
}
