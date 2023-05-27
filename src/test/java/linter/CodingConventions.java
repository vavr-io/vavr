package linter;

import io.vavr.CheckedFunction1;
import io.vavr.Function0;
import io.vavr.collection.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.File;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

public class CodingConventions {

    @BeforeAll
    public static void beforeClass() {
        System.out.println("[LINTER] Start");
    }

    @AfterAll
    public static void afterClass() {
        System.out.println("[LINTER] Finished");
    }

    static class TraceUnitExtension implements AfterEachCallback, BeforeEachCallback {

        @Override
        public void beforeEach(ExtensionContext context) {
            printInfo("[LINTER] Checking rule", context);
        }

        @Override
        public void afterEach(ExtensionContext context) {
            printInfo("[LINTER] Finished rule", context);
        }

        private void printInfo(String prefix, ExtensionContext context) {
            System.out.println(prefix + " " + context.getDisplayName());
        }

    }

    @Test
    public void shouldHaveTransformMethodWhenIterable() {
        final int errors = vavrTypes.get()
            .filter(type -> !type.isInterface() && Iterable.class.isAssignableFrom(type))
            .filter(type -> {
                if (type.isAnnotationPresent(Deprecated.class)) {
                    skip(type, "deprecated");
                    return false;
                } else {
                    try {
                        type.getMethod("transform", Function.class);
                        System.out.println("✅ " + type + ".transform(Function)");
                        return false;
                    } catch(NoSuchMethodException x) {
                        System.out.println("⛔ transform method missing in " + type);
                        return true;
                    }
                }
        }).length();
        assertThat(errors).isZero();
    }

    private void skip(Class<?> type, String reason) {
        System.out.println("⏭ skipping " + reason + " " + type.getName());
    }

    private static final Supplier<List<Class<?>>> vavrTypes = Function0.of(() -> getClasses(
            "src/main/java", "src-gen/main/java"
    )).memoized();

    private static List<Class<?>> getClasses(String... startDirs) {
        return List.of(startDirs)
                .flatMap(CheckedFunction1.<String, List<String>> of(startDir -> {
                    final Path startPath = Paths.get(startDir);
                    final String fileExtension = ".java";
                    return Files.find(startPath, Integer.MAX_VALUE, (path, basicFileAttributes) ->
                            basicFileAttributes.isRegularFile() &&
                                    path.getName(path.getNameCount() - 1).toString().endsWith(fileExtension)
                    )
                            .map(Object::toString)
                            .map(path -> path.substring(startDir.length() + 1, path.length() - fileExtension.length()))
                            .filter(path -> !path.endsWith(File.separator + "package-info"))
                            .map(path -> path.replace(File.separator, "."))
                            .collect(List.collector());
                }).unchecked())
                .sorted()
                .map(CheckedFunction1.<String, Class<?>> of(Class::forName).unchecked())
                .filter(type -> Modifier.isPublic(type.getModifiers()));
    }
}
