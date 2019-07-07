// @formatter:off
// CHECKSTYLE:OFF
package io.vavr;

import io.vavr.API.Match.Pattern;
import io.vavr.API.Match.Pattern3;
import io.vavr.control.Option;

/**
 * @deprecated Will be removed in the next major version, along with VAVR's pattern matching, in favor of Java's native pattern matching.
 */
@Deprecated
public final class MatchTest_DeveloperPatterns {

    private MatchTest_DeveloperPatterns() {
    }

    public static <_1 extends String, _2 extends Boolean, _3 extends Option<Number>> Pattern3<MatchTest.Developer, _1, _2, _3> $Developer(Pattern<_1, ?> p1, Pattern<_2, ?> p2, Pattern<_3, ?> p3) {
        return Pattern3.of(MatchTest.Developer.class, p1, p2, p3, io.vavr.MatchTest.Developer.$::Developer);
    }

}
// CHECKSTYLE:ON
// @formatter:on