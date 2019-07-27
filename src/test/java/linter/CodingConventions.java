package linter;

import io.vavr.CheckedFunction1;
import io.vavr.Function0;
import io.vavr.Iterable;
import io.vavr.collection.List;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.io.File;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

public class CodingConventions {

    @BeforeClass
    public static void beforeClass() {
        System.out.println("[LINTER] Start");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("[LINTER] Finished");
    }

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description desc) {
            printInfo("[LINTER] Checking rule", desc);
        }

        @Override
        protected void finished(Description desc) {
            printInfo("[LINTER] Finished rule", desc);
        }

        private void printInfo(String prefix, Description desc) {
            System.out.println(prefix + " " + desc.getDisplayName());
        }
    };

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
