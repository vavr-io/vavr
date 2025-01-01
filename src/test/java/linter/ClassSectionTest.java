package linter;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.domain.JavaParameter;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class ClassSectionTest {

    public static final String STATIC_API = "static API";
    public static final String ADJUSTED_RETURN_TYPES = "adjusted return types";
    public static final String NON_STATIC_API = "non-static API";

    @Test
    public void methodsAreAlphabeticallyOrderedInSections() {
        classes()
                .should(haveOrderedMethodSections()).check(new ClassFileImporter()
                        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                        .importPackages("io.vavr"));
    }

    private ArchCondition<JavaClass> haveOrderedMethodSections() {
        return new ArchCondition<JavaClass>("have methods alphabetically ordered in sections") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                Map<String, List<JavaMethod>> sections = methodSections(javaClass.getMethods());

                for (Map.Entry<String, List<JavaMethod>> section : sections.entrySet()) {
                    List<JavaMethod> methods = section.getValue();

                    List<String> methodNames = methods.stream()
                            .map(JavaMethod::getName)
                            .sorted()
                            .collect(Collectors.toList());

                    List<String> methodOrder = methods.stream()
                            .sorted(Comparator.comparing(m -> m.getSourceCodeLocation().getLineNumber()))
                            .map(JavaMethod::getName)
                            .collect(Collectors.toList());

                    if (!methodOrder.equals(methodNames)) {
                        String message = String.format("Methods in section '%s' of class '%s' are not alphabetically ordered: %s",
                                section.getKey(), javaClass.getName(), methodOrder);
                        events.add(SimpleConditionEvent.violated(javaClass, message));
                    }
                }
            }
        };
    }

    private Map<String, List<JavaMethod>> methodSections(Set<JavaMethod> methods) {
        Map<String, List<JavaMethod>> sections = new HashMap<>();
        sections.put(STATIC_API, new ArrayList<>());
        sections.put(ADJUSTED_RETURN_TYPES, new ArrayList<>());
        sections.put(NON_STATIC_API, new ArrayList<>());

        for (JavaMethod method : methods) {
            if (method.getModifiers().contains(JavaModifier.STATIC)) {
                sections.get(STATIC_API).add(method);
            } else if (isOverridden(method)) {
                sections.get(ADJUSTED_RETURN_TYPES).add(method);
            }
            else {
                sections.get(NON_STATIC_API).add(method);
            }
        }
        return sections;
    }

    private boolean isOverridden(JavaMethod method) {
        return method.getOwner().getAllRawInterfaces().stream()
                .anyMatch(c -> c.tryGetMethod(method.getName(), getTypeNames(method.getParameters())).isPresent());
    }

    private String[] getTypeNames(List<JavaParameter> parameters) {
        return parameters.stream().map(p -> p.getRawType().getName()).toArray(String[]::new);
    }
}
