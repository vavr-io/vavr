package io.vavr.jmh;

import io.vavr.collection.Map;
import io.vavr.collection.champ.ChampMap;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * # JMH version: 1.28
 * # VM version: JDK 17, OpenJDK 64-Bit Server VM, 17+35-2724
 * # Intel(R) Core(TM) i7-8700B CPU @ 3.20GHz
 *
 * Benchmark         (size)  Mode  Cnt    _     Score        Error  Units
 * ContainsFound     1000000  avgt    4       179.705 ±       5.735  ns/op
 * ContainsNotFound  1000000  avgt    4       178.312 ±       8.082  ns/op
 * Iterate           1000000  avgt    4  48892070.205 ± 4267871.730  ns/op
 * Put               1000000  avgt    4       334.626 ±      17.592  ns/op
 * Head              1000000  avgt    4        38.292 ±       2.783  ns/op
 * RemoveThenAdd     1000000  avgt    4       530.084 ±      13.140  ns/op
 * -----
 * Benchmark                                       (size)  Mode  Cnt          Score   Error  Units
 * JavaUtilHashMapJmh.mContainsFound              1000000  avgt             109.722          ns/op
 * JavaUtilHashMapJmh.mContainsNotFound           1000000  avgt             106.555          ns/op
 * JavaUtilHashMapJmh.mHead                       1000000  avgt               3.290          ns/op
 * JavaUtilHashMapJmh.mIterate                    1000000  avgt        50320063.437          ns/op
 * JavaUtilHashMapJmh.mPut                        1000000  avgt             312.280          ns/op
 * JavaUtilHashMapJmh.mRemoveThenAdd              1000000  avgt             283.116          ns/op
 * JavaUtilHashSetJmh.mContainsFound              1000000  avgt             102.401          ns/op
 * JavaUtilHashSetJmh.mContainsNotFound           1000000  avgt             101.596          ns/op
 * JavaUtilHashSetJmh.mIterate                    1000000  avgt        50493893.291          ns/op
 * JavaUtilHashSetJmh.mRemoveThenAdd              1000000  avgt             282.793          ns/op
 * KotlinxPersistentHashMapJmh.mContainsFound     1000000  avgt             349.783          ns/op
 * KotlinxPersistentHashMapJmh.mContainsNotFound  1000000  avgt             354.807          ns/op
 * KotlinxPersistentHashMapJmh.mHead              1000000  avgt              47.308          ns/op
 * KotlinxPersistentHashMapJmh.mIterate           1000000  avgt        71063621.433          ns/op
 * KotlinxPersistentHashMapJmh.mPut               1000000  avgt             495.998          ns/op
 * KotlinxPersistentHashMapJmh.mRemoveThenAdd     1000000  avgt             752.391          ns/op
 * KotlinxPersistentHashMapJmh.mTail              1000000  avgt             159.420          ns/op
 * KotlinxPersistentHashSetJmh.mContainsFound     1000000  avgt             312.897          ns/op
 * KotlinxPersistentHashSetJmh.mContainsNotFound  1000000  avgt             309.661          ns/op
 * KotlinxPersistentHashSetJmh.mHead              1000000  avgt             113.518          ns/op
 * KotlinxPersistentHashSetJmh.mIterate           1000000  avgt       106309262.032          ns/op
 * KotlinxPersistentHashSetJmh.mRemoveThenAdd     1000000  avgt             704.238          ns/op
 * KotlinxPersistentHashSetJmh.mTail              1000000  avgt             227.621          ns/op
 * ScalaHashMapJmh.mContainsFound                 1000000  avgt             413.227          ns/op
 * ScalaHashMapJmh.mContainsNotFound              1000000  avgt             420.548          ns/op
 * ScalaHashMapJmh.mHead                          1000000  avgt              28.428          ns/op
 * ScalaHashMapJmh.mIterate                       1000000  avgt        56581123.232          ns/op
 * ScalaHashMapJmh.mPut                           1000000  avgt             669.935          ns/op
 * ScalaHashMapJmh.mRemoveThenAdd                 1000000  avgt            1038.223          ns/op
 * ScalaHashMapJmh.mTail                          1000000  avgt             141.395          ns/op
 * ScalaHashSetJmh.mContainsFound                 1000000  avgt             361.863          ns/op
 * ScalaHashSetJmh.mContainsNotFound              1000000  avgt             364.637          ns/op
 * ScalaHashSetJmh.mHead                          1000000  avgt              28.018          ns/op
 * ScalaHashSetJmh.mIterate                       1000000  avgt        55998433.156          ns/op
 * ScalaHashSetJmh.mRemoveThenAdd                 1000000  avgt             971.067          ns/op
 * ScalaHashSetJmh.mTail                          1000000  avgt             142.584          ns/op
 * ScalaListMapJmh.mContainsFound                     100  avgt              94.836          ns/op
 * ScalaListMapJmh.mContainsNotFound                  100  avgt              94.411          ns/op
 * ScalaListMapJmh.mHead                              100  avgt             771.887          ns/op
 * ScalaListMapJmh.mIterate                           100  avgt            1092.943          ns/op
 * ScalaListMapJmh.mPut                               100  avgt             404.491          ns/op
 * ScalaListMapJmh.mRemoveThenAdd                     100  avgt             698.171          ns/op
 * ScalaTreeSeqMapJmh.mContainsFound              1000000  avgt             459.557          ns/op
 * ScalaTreeSeqMapJmh.mContainsNotFound           1000000  avgt             453.985          ns/op
 * ScalaTreeSeqMapJmh.mCopyOf                     1000000  avgt       824561770.231          ns/op
 * ScalaTreeSeqMapJmh.mHead                       1000000  avgt              56.851          ns/op
 * ScalaTreeSeqMapJmh.mIterate                    1000000  avgt        48915187.015          ns/op
 * ScalaTreeSeqMapJmh.mPut                        1000000  avgt            1768.932          ns/op
 * ScalaTreeSeqMapJmh.mRemoveThenAdd              1000000  avgt            2341.271          ns/op
 * ScalaTreeSeqMapJmh.mTail                       1000000  avgt             361.624          ns/op
 * ScalaVectorMapJmh.mContainsFound               1000000  avgt             427.417          ns/op
 * ScalaVectorMapJmh.mContainsNotFound            1000000  avgt             432.271          ns/op
 * ScalaVectorMapJmh.mHead                        1000000  avgt              27.265          ns/op
 * ScalaVectorMapJmh.mIterate                     1000000  avgt       534587534.632          ns/op
 * ScalaVectorMapJmh.mPut                         1000000  avgt             827.507          ns/op
 * ScalaVectorMapJmh.mRemoveThenAdd               1000000  avgt            1898.554          ns/op
 * VavrChampMapJmh.mContainsFound                 1000000  avgt             295.212          ns/op
 * VavrChampMapJmh.mContainsNotFound              1000000  avgt             295.953          ns/op
 * VavrChampMapJmh.mHead                          1000000  avgt              39.773          ns/op
 * VavrChampMapJmh.mIterate                       1000000  avgt        88809860.133          ns/op
 * VavrChampMapJmh.mPut                           1000000  avgt             520.899          ns/op
 * VavrChampMapJmh.mRemoveThenAdd                 1000000  avgt             789.359          ns/op
 * VavrChampMapJmh.mTail                          1000000  avgt             157.809          ns/op
 * VavrChampSetJmh.mContainsFound                 1000000  avgt             309.100          ns/op
 * VavrChampSetJmh.mContainsNotFound              1000000  avgt             321.234          ns/op
 * VavrChampSetJmh.mHead                          1000000  avgt              27.516          ns/op
 * VavrChampSetJmh.mIterate                       1000000  avgt        44732276.268          ns/op
 * VavrChampSetJmh.mRemoveThenAdd                 1000000  avgt             767.298          ns/op
 * VavrChampSetJmh.mTail                          1000000  avgt             133.767          ns/op
 * VavrHashMapJmh.mContainsFound                  1000000  avgt             306.214          ns/op
 * VavrHashMapJmh.mContainsNotFound               1000000  avgt             302.352          ns/op
 * VavrHashMapJmh.mHead                           1000000  avgt              30.477          ns/op
 * VavrHashMapJmh.mIterate                        1000000  avgt       124738628.198          ns/op
 * VavrHashMapJmh.mPut                            1000000  avgt             555.883          ns/op
 * VavrHashMapJmh.mRemoveThenAdd                  1000000  avgt             722.073          ns/op
 * VavrHashSetJmh.mContainsFound                  1000000  avgt             306.366          ns/op
 * VavrHashSetJmh.mContainsNotFound               1000000  avgt             316.097          ns/op
 * VavrHashSetJmh.mHead                           1000000  avgt              30.622          ns/op
 * VavrHashSetJmh.mIterate                        1000000  avgt       123808551.827          ns/op
 * VavrHashSetJmh.mRemoveThenAdd                  1000000  avgt             761.223          ns/op
 * VavrHashSetJmh.mTail                           1000000  avgt             162.027          ns/op
 * VavrLinkedHashMapJmh.mContainsFound            1000000  avgt             298.668          ns/op
 * VavrLinkedHashMapJmh.mContainsNotFound         1000000  avgt             313.466          ns/op
 * VavrLinkedHashMapJmh.mHead                     1000000  avgt               1.722          ns/op
 * VavrLinkedHashMapJmh.mIterate                  1000000  avgt       118420084.741          ns/op
 * VavrLinkedHashMapJmh.mPut                      1000000  avgt        34379122.236          ns/op
 * VavrLinkedHashMapJmh.mRemoveThenAdd            1000000  avgt        77564084.546          ns/op
 * VavrLinkedHashSetJmh.mContainsFound            1000000  avgt             309.449          ns/op
 * VavrLinkedHashSetJmh.mContainsNotFound         1000000  avgt             320.921          ns/op
 * VavrLinkedHashSetJmh.mHead                     1000000  avgt               2.530          ns/op
 * VavrLinkedHashSetJmh.mIterate                  1000000  avgt       118213734.188          ns/op
 * VavrLinkedHashSetJmh.mRemoveThenAdd            1000000  avgt        76093131.803          ns/op
 * VavrLinkedHashSetJmh.mTail                     1000000  avgt         8440840.840          ns/op
 * VavrSequencedChampMapJmh.mContainsFound        1000000  avgt             303.078          ns/op
 * VavrSequencedChampMapJmh.mContainsNotFound     1000000  avgt             314.134          ns/op
 * VavrSequencedChampMapJmh.mHead                 1000000  avgt             106.266          ns/op
 * VavrSequencedChampMapJmh.mIterate              1000000  avgt        95913550.200          ns/op
 * VavrSequencedChampMapJmh.mPut                  1000000  avgt            1187.377          ns/op
 * VavrSequencedChampMapJmh.mRemoveThenAdd        1000000  avgt            1556.704          ns/op
 * VavrSequencedChampMapJmh.mTail                 1000000  avgt             425.287          ns/op
 * VavrSequencedChampSetJmh.mContainsFound        1000000  avgt             329.597          ns/op
 * VavrSequencedChampSetJmh.mContainsNotFound     1000000  avgt             338.803          ns/op
 * VavrSequencedChampSetJmh.mHead                 1000000  avgt              11.655          ns/op
 * VavrSequencedChampSetJmh.mIterate              1000000  avgt       109824913.250          ns/op
 * VavrSequencedChampSetJmh.mRemoveThenAdd        1000000  avgt            1517.139          ns/op
 * VavrSequencedChampSetJmh.mTail                 1000000  avgt             294.813          ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 1)
@Warmup(iterations = 1)
@Fork(value = 1)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class VavrChampMapJmh {
    @Param({"1000000"})
    private int size;

    private final int mask = ~64;

    private BenchmarkData data;
    private ChampMap<Key, Boolean> mapA;


    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
         mapA =  ChampMap.empty();
        for (Key key : data.setA) {
            mapA=mapA.put(key,Boolean.TRUE);
        }
    }

    @Benchmark
    public int mIterate() {
        int sum = 0;
        for (Key k : mapA.keysIterator()) {
            sum += k.value;
        }
        return sum;
    }

    @Benchmark
    public void mRemoveThenAdd() {
        Key key =data.nextKeyInA();
        mapA.remove(key).put(key,Boolean.TRUE);
    }

    @Benchmark
    public void mPut() {
        Key key =data.nextKeyInA();
        mapA.put(key,Boolean.FALSE);
    }

    @Benchmark
    public boolean mContainsFound() {
        Key key = data.nextKeyInA();
        return mapA.containsKey(key);
    }

    @Benchmark
    public boolean mContainsNotFound() {
        Key key = data.nextKeyInB();
        return mapA.containsKey(key);
    }

    @Benchmark
    public Key mHead() {
        return mapA.head()._1;
    }

    @Benchmark
    public Map<Key, Boolean> mTail() {
        return mapA.tail();
    }

}
