package outside;

import javaslang.control.Try;
import org.junit.Test;

import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class IllegalAccessRegressionTest {

    @Test(/* regression test, see #1366 */)
    public void shouldCombineOptionWithTry() {
        final Function<Try<String>, Optional<String>> methodRef = Try::toJavaOptional;
        final Optional<String> actual = methodRef.apply(Try.of(() -> "hi"));
        assertThat(actual).isEqualTo(Optional.of("hi"));
    }
}
