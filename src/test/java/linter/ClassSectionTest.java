package linter;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaModifier;
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
        sections.put("static API", new ArrayList<>());
        sections.put("non-static API", new ArrayList<>());

        for (JavaMethod method : methods) {
            if (method.getModifiers().contains(JavaModifier.STATIC)) {
                sections.get("static API").add(method);
            } else {
                sections.get("non-static API").add(method);
            }
        }
        return sections;
    }
}
