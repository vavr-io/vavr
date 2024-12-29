package linter;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class PackageDepthTest {
    @Test
    void maxTwo() {
        classes()
                .should(havePackageDepthLessThanOrEqualTo(1 + 2))
                .check(new ClassFileImporter()
                        .withImportOption(new ImportOption.DoNotIncludeTests())
                        .importPackages("io.vavr"));
    }

    private static ArchCondition<JavaClass> havePackageDepthLessThanOrEqualTo(int maxDepth) {
        return new ArchCondition<JavaClass>("have a package depth of " + maxDepth + " or less") {
            @Override
            public void check(JavaClass item, ConditionEvents events) {
                int depth = item.getPackageName().split("\\.").length;
                if (depth > maxDepth) {
                    String message = String.format(
                            "Class %s has a package depth of %d, which exceeds the allowed maximum of %d",
                            item.getName(), depth, maxDepth
                    );
                    events.add(SimpleConditionEvent.violated(item, message));
                }
            }
        };
    }
}
