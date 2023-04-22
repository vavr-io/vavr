package io.vavr.collection.champ;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        MutableHashMapGuavaTests.class,
        MutableHashSetGuavaTests.class,
        MutableLinkedHashMapGuavaTests.class,
        MutableLinkedHashSetGuavaTests.class
})
public class GuavaTestSuite {
}
