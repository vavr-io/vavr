package io.vavr.collection.champ;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        MutableChampMapGuavaTests.class,
        MutableChampSetGuavaTests.class,
        MutableSequencedChampMapGuavaTests.class,
        MutableSequencedChampSetGuavaTests.class
})
public class ChampGuavaTestSuite {
}
