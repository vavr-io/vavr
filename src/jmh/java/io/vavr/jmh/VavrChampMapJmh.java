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
 *
 * Benchmark                                       (size)  Mode  Cnt          Score   Error  Units
 * JavaUtilHashMapJmh.mContainsFound                   10  avgt               5.337          ns/op
 * JavaUtilHashMapJmh.mContainsFound              1000000  avgt              87.837          ns/op
 * JavaUtilHashMapJmh.mContainsNotFound                10  avgt               5.867          ns/op
 * JavaUtilHashMapJmh.mContainsNotFound           1000000  avgt              89.524          ns/op
 * JavaUtilHashMapJmh.mHead                            10  avgt               3.451          ns/op
 * JavaUtilHashMapJmh.mHead                       1000000  avgt               3.849          ns/op
 * JavaUtilHashMapJmh.mIterate                         10  avgt              74.716          ns/op
 * JavaUtilHashMapJmh.mIterate                    1000000  avgt        45972390.862          ns/op
 * JavaUtilHashMapJmh.mPut                             10  avgt              13.585          ns/op
 * JavaUtilHashMapJmh.mPut                        1000000  avgt             239.954          ns/op
 * JavaUtilHashMapJmh.mRemoveThenAdd                   10  avgt              21.504          ns/op
 * JavaUtilHashMapJmh.mRemoveThenAdd              1000000  avgt             201.168          ns/op
 * JavaUtilHashSetJmh.mContainsFound                   10  avgt               4.392          ns/op
 * JavaUtilHashSetJmh.mContainsFound              1000000  avgt              75.398          ns/op
 * JavaUtilHashSetJmh.mContainsNotFound                10  avgt               4.408          ns/op
 * JavaUtilHashSetJmh.mContainsNotFound           1000000  avgt              74.959          ns/op
 * JavaUtilHashSetJmh.mIterate                         10  avgt              61.102          ns/op
 * JavaUtilHashSetJmh.mIterate                    1000000  avgt        37404920.101          ns/op
 * JavaUtilHashSetJmh.mRemoveThenAdd                   10  avgt              18.339          ns/op
 * JavaUtilHashSetJmh.mRemoveThenAdd              1000000  avgt             204.424          ns/op
 * KotlinxPersistentHashMapJmh.mContainsFound          10  avgt               4.481          ns/op
 * KotlinxPersistentHashMapJmh.mContainsFound     1000000  avgt             223.844          ns/op
 * KotlinxPersistentHashMapJmh.mContainsNotFound       10  avgt               5.124          ns/op
 * KotlinxPersistentHashMapJmh.mContainsNotFound  1000000  avgt             217.791          ns/op
 * KotlinxPersistentHashMapJmh.mHead                   10  avgt              26.998          ns/op
 * KotlinxPersistentHashMapJmh.mHead              1000000  avgt              40.497          ns/op
 * KotlinxPersistentHashMapJmh.mIterate                10  avgt              44.236          ns/op
 * KotlinxPersistentHashMapJmh.mIterate           1000000  avgt        47243646.311          ns/op
 * KotlinxPersistentHashMapJmh.mPut                    10  avgt              20.266          ns/op
 * KotlinxPersistentHashMapJmh.mPut               1000000  avgt             351.040          ns/op
 * KotlinxPersistentHashMapJmh.mRemoveThenAdd          10  avgt              63.294          ns/op
 * KotlinxPersistentHashMapJmh.mRemoveThenAdd     1000000  avgt             536.243          ns/op
 * KotlinxPersistentHashMapJmh.mTail                   10  avgt              45.442          ns/op
 * KotlinxPersistentHashMapJmh.mTail              1000000  avgt             117.912          ns/op
 * KotlinxPersistentHashSetJmh.mContainsFound          10  avgt               4.763          ns/op
 * KotlinxPersistentHashSetJmh.mContainsFound     1000000  avgt             170.489          ns/op
 * KotlinxPersistentHashSetJmh.mContainsNotFound       10  avgt               4.764          ns/op
 * KotlinxPersistentHashSetJmh.mContainsNotFound  1000000  avgt             169.976          ns/op
 * KotlinxPersistentHashSetJmh.mHead                   10  avgt              15.265          ns/op
 * KotlinxPersistentHashSetJmh.mHead              1000000  avgt             114.349          ns/op
 * KotlinxPersistentHashSetJmh.mIterate                10  avgt             108.717          ns/op
 * KotlinxPersistentHashSetJmh.mIterate           1000000  avgt        71895818.100          ns/op
 * KotlinxPersistentHashSetJmh.mRemoveThenAdd          10  avgt              58.418          ns/op
 * KotlinxPersistentHashSetJmh.mRemoveThenAdd     1000000  avgt             465.049          ns/op
 * KotlinxPersistentHashSetJmh.mTail                   10  avgt              36.783          ns/op
 * KotlinxPersistentHashSetJmh.mTail              1000000  avgt             206.459          ns/op
 * ScalaHashMapJmh.mContainsFound                      10  avgt               8.857          ns/op
 * ScalaHashMapJmh.mContainsFound                 1000000  avgt             234.180          ns/op
 * ScalaHashMapJmh.mContainsNotFound                   10  avgt               7.099          ns/op
 * ScalaHashMapJmh.mContainsNotFound              1000000  avgt             242.381          ns/op
 * ScalaHashMapJmh.mHead                               10  avgt               1.668          ns/op
 * ScalaHashMapJmh.mHead                          1000000  avgt              25.695          ns/op
 * ScalaHashMapJmh.mIterate                            10  avgt               9.571          ns/op
 * ScalaHashMapJmh.mIterate                       1000000  avgt        36057440.773          ns/op
 * ScalaHashMapJmh.mPut                                10  avgt              15.468          ns/op
 * ScalaHashMapJmh.mPut                           1000000  avgt             401.539          ns/op
 * ScalaHashMapJmh.mRemoveThenAdd                      10  avgt              81.192          ns/op
 * ScalaHashMapJmh.mRemoveThenAdd                 1000000  avgt             685.426          ns/op
 * ScalaHashMapJmh.mTail                               10  avgt              36.870          ns/op
 * ScalaHashMapJmh.mTail                          1000000  avgt             114.338          ns/op
 * ScalaHashSetJmh.mContainsFound                      10  avgt               6.394          ns/op
 * ScalaHashSetJmh.mContainsFound                 1000000  avgt             211.333          ns/op
 * ScalaHashSetJmh.mContainsNotFound                   10  avgt               6.594          ns/op
 * ScalaHashSetJmh.mContainsNotFound              1000000  avgt             211.221          ns/op
 * ScalaHashSetJmh.mHead                               10  avgt               1.708          ns/op
 * ScalaHashSetJmh.mHead                          1000000  avgt              24.852          ns/op
 * ScalaHashSetJmh.mIterate                            10  avgt               9.237          ns/op
 * ScalaHashSetJmh.mIterate                       1000000  avgt        37197441.216          ns/op
 * ScalaHashSetJmh.mRemoveThenAdd                      10  avgt              78.368          ns/op
 * ScalaHashSetJmh.mRemoveThenAdd                 1000000  avgt             635.750          ns/op
 * ScalaHashSetJmh.mTail                               10  avgt              36.796          ns/op
 * ScalaHashSetJmh.mTail                          1000000  avgt             115.349          ns/op
 * ScalaListMapJmh.mContainsFound                     100  avgt              90.916          ns/op
 * ScalaListMapJmh.mContainsNotFound                  100  avgt              92.102          ns/op
 * ScalaListMapJmh.mHead                              100  avgt             591.860          ns/op
 * ScalaListMapJmh.mIterate                           100  avgt             900.463          ns/op
 * ScalaListMapJmh.mPut                               100  avgt             359.019          ns/op
 * ScalaListMapJmh.mRemoveThenAdd                     100  avgt             613.173          ns/op
 * ScalaTreeSeqMapJmh.mContainsFound                   10  avgt               6.663          ns/op
 * ScalaTreeSeqMapJmh.mContainsFound              1000000  avgt             243.142          ns/op
 * ScalaTreeSeqMapJmh.mContainsNotFound                10  avgt               6.632          ns/op
 * ScalaTreeSeqMapJmh.mContainsNotFound           1000000  avgt             242.669          ns/op
 * ScalaTreeSeqMapJmh.mCopyOf                          10  avgt             881.246          ns/op
 * ScalaTreeSeqMapJmh.mCopyOf                     1000000  avgt       499947401.714          ns/op
 * ScalaTreeSeqMapJmh.mHead                            10  avgt              10.266          ns/op
 * ScalaTreeSeqMapJmh.mHead                       1000000  avgt              53.381          ns/op
 * ScalaTreeSeqMapJmh.mIterate                         10  avgt              66.743          ns/op
 * ScalaTreeSeqMapJmh.mIterate                    1000000  avgt        30681238.288          ns/op
 * ScalaTreeSeqMapJmh.mPut                             10  avgt              59.923          ns/op
 * ScalaTreeSeqMapJmh.mPut                        1000000  avgt             994.342          ns/op
 * ScalaTreeSeqMapJmh.mRemoveThenAdd                   10  avgt             148.966          ns/op
 * ScalaTreeSeqMapJmh.mRemoveThenAdd              1000000  avgt            1383.396          ns/op
 * ScalaTreeSeqMapJmh.mTail                            10  avgt              83.148          ns/op
 * ScalaTreeSeqMapJmh.mTail                       1000000  avgt             291.186          ns/op
 * ScalaVectorMapJmh.mContainsFound                    10  avgt               6.629          ns/op
 * ScalaVectorMapJmh.mContainsFound               1000000  avgt             252.086          ns/op
 * ScalaVectorMapJmh.mContainsNotFound                 10  avgt               6.626          ns/op
 * ScalaVectorMapJmh.mContainsNotFound            1000000  avgt             250.581          ns/op
 * ScalaVectorMapJmh.mHead                             10  avgt               7.118          ns/op
 * ScalaVectorMapJmh.mHead                        1000000  avgt              27.016          ns/op
 * ScalaVectorMapJmh.mIterate                          10  avgt              89.465          ns/op
 * ScalaVectorMapJmh.mIterate                     1000000  avgt       308377875.515          ns/op
 * ScalaVectorMapJmh.mPut                              10  avgt              30.457          ns/op
 * ScalaVectorMapJmh.mPut                         1000000  avgt             493.239          ns/op
 * ScalaVectorMapJmh.mRemoveThenAdd                    10  avgt             140.110          ns/op
 * ScalaVectorMapJmh.mRemoveThenAdd               1000000  avgt            1214.649          ns/op
 * VavrChampMapJmh.mContainsFound                      10  avgt               5.228          ns/op
 * VavrChampMapJmh.mContainsFound                 1000000  avgt             180.860          ns/op
 * VavrChampMapJmh.mContainsNotFound                   10  avgt               4.810          ns/op
 * VavrChampMapJmh.mContainsNotFound              1000000  avgt             182.962          ns/op
 * VavrChampMapJmh.mHead                               10  avgt              14.497          ns/op
 * VavrChampMapJmh.mHead                          1000000  avgt              34.982          ns/op
 * VavrChampMapJmh.mIterate                            10  avgt              63.279          ns/op
 * VavrChampMapJmh.mIterate                       1000000  avgt        54610745.207          ns/op
 * VavrChampMapJmh.mPut                                10  avgt              23.779          ns/op
 * VavrChampMapJmh.mPut                           1000000  avgt             339.750          ns/op
 * VavrChampMapJmh.mRemoveThenAdd                      10  avgt              65.039          ns/op
 * VavrChampMapJmh.mRemoveThenAdd                 1000000  avgt             535.499          ns/op
 * VavrChampMapJmh.mTail                               10  avgt              38.912          ns/op
 * VavrChampMapJmh.mTail                          1000000  avgt             118.332          ns/op
 * VavrChampSetJmh.mContainsFound                      10  avgt               4.720          ns/op
 * VavrChampSetJmh.mContainsFound                 1000000  avgt             208.266          ns/op
 * VavrChampSetJmh.mContainsNotFound                   10  avgt               4.397          ns/op
 * VavrChampSetJmh.mContainsNotFound              1000000  avgt             208.751          ns/op
 * VavrChampSetJmh.mHead                               10  avgt              10.912          ns/op
 * VavrChampSetJmh.mHead                          1000000  avgt              25.173          ns/op
 * VavrChampSetJmh.mIterate                            10  avgt              15.869          ns/op
 * VavrChampSetJmh.mIterate                       1000000  avgt        39349325.941          ns/op
 * VavrChampSetJmh.mRemoveThenAdd                      10  avgt              58.045          ns/op
 * VavrChampSetJmh.mRemoveThenAdd                 1000000  avgt             614.303          ns/op
 * VavrChampSetJmh.mTail                               10  avgt              36.092          ns/op
 * VavrChampSetJmh.mTail                          1000000  avgt             114.222          ns/op
 * VavrHashMapJmh.mContainsFound                       10  avgt               5.314          ns/op
 * VavrHashMapJmh.mContainsFound                  1000000  avgt             185.863          ns/op
 * VavrHashMapJmh.mContainsNotFound                    10  avgt               5.305          ns/op
 * VavrHashMapJmh.mContainsNotFound               1000000  avgt             187.200          ns/op
 * VavrHashMapJmh.mHead                                10  avgt              15.275          ns/op
 * VavrHashMapJmh.mHead                           1000000  avgt              27.608          ns/op
 * VavrHashMapJmh.mIterate                             10  avgt             113.337          ns/op
 * VavrHashMapJmh.mIterate                        1000000  avgt        76943358.646          ns/op
 * VavrHashMapJmh.mPut                                 10  avgt              18.400          ns/op
 * VavrHashMapJmh.mPut                            1000000  avgt             378.292          ns/op
 * VavrHashMapJmh.mRemoveThenAdd                       10  avgt              58.646          ns/op
 * VavrHashMapJmh.mRemoveThenAdd                  1000000  avgt             508.140          ns/op
 * VavrHashSetJmh.mContainsFound                       10  avgt               5.332          ns/op
 * VavrHashSetJmh.mContainsFound                  1000000  avgt             219.572          ns/op
 * VavrHashSetJmh.mContainsNotFound                    10  avgt               5.245          ns/op
 * VavrHashSetJmh.mContainsNotFound               1000000  avgt             218.752          ns/op
 * VavrHashSetJmh.mHead                                10  avgt              16.080          ns/op
 * VavrHashSetJmh.mHead                           1000000  avgt              28.728          ns/op
 * VavrHashSetJmh.mIterate                             10  avgt             108.675          ns/op
 * VavrHashSetJmh.mIterate                        1000000  avgt        88617533.248          ns/op
 * VavrHashSetJmh.mRemoveThenAdd                       10  avgt              60.133          ns/op
 * VavrHashSetJmh.mRemoveThenAdd                  1000000  avgt             584.563          ns/op
 * VavrHashSetJmh.mTail                                10  avgt              41.577          ns/op
 * VavrHashSetJmh.mTail                           1000000  avgt             140.873          ns/op
 * VavrLinkedHashMapJmh.mContainsFound                 10  avgt               6.188          ns/op
 * VavrLinkedHashMapJmh.mContainsFound            1000000  avgt             207.582          ns/op
 * VavrLinkedHashMapJmh.mContainsNotFound              10  avgt               6.116          ns/op
 * VavrLinkedHashMapJmh.mContainsNotFound         1000000  avgt             227.305          ns/op
 * VavrLinkedHashMapJmh.mHead                          10  avgt               1.703          ns/op
 * VavrLinkedHashMapJmh.mHead                     1000000  avgt               1.700          ns/op
 * VavrLinkedHashMapJmh.mIterate                       10  avgt             290.365          ns/op
 * VavrLinkedHashMapJmh.mIterate                  1000000  avgt        82143446.320          ns/op
 * VavrLinkedHashMapJmh.mPut                           10  avgt             103.274          ns/op
 * VavrLinkedHashMapJmh.mPut                      1000000  avgt        22639241.620          ns/op
 * VavrLinkedHashMapJmh.mRemoveThenAdd                 10  avgt             638.327          ns/op
 * VavrLinkedHashMapJmh.mRemoveThenAdd            1000000  avgt        61101342.665          ns/op
 * VavrLinkedHashSetJmh.mContainsFound                 10  avgt               5.483          ns/op
 * VavrLinkedHashSetJmh.mContainsFound            1000000  avgt             217.186          ns/op
 * VavrLinkedHashSetJmh.mContainsNotFound              10  avgt               5.499          ns/op
 * VavrLinkedHashSetJmh.mContainsNotFound         1000000  avgt             222.599          ns/op
 * VavrLinkedHashSetJmh.mHead                          10  avgt               2.498          ns/op
 * VavrLinkedHashSetJmh.mHead                     1000000  avgt               2.520          ns/op
 * VavrLinkedHashSetJmh.mIterate                       10  avgt             284.097          ns/op
 * VavrLinkedHashSetJmh.mIterate                  1000000  avgt        81268916.597          ns/op
 * VavrLinkedHashSetJmh.mRemoveThenAdd                 10  avgt             836.239          ns/op
 * VavrLinkedHashSetJmh.mRemoveThenAdd            1000000  avgt        63933909.115          ns/op
 * VavrLinkedHashSetJmh.mTail                          10  avgt              58.188          ns/op
 * VavrLinkedHashSetJmh.mTail                     1000000  avgt         5629253.535          ns/op
 * VavrSequencedChampMapJmh.mContainsFound             10  avgt               4.824          ns/op
 * VavrSequencedChampMapJmh.mContainsFound        1000000  avgt             203.568          ns/op
 * VavrSequencedChampMapJmh.mContainsNotFound          10  avgt               4.928          ns/op
 * VavrSequencedChampMapJmh.mContainsNotFound     1000000  avgt             218.794          ns/op
 * VavrSequencedChampMapJmh.mHead                      10  avgt              95.301          ns/op
 * VavrSequencedChampMapJmh.mHead                 1000000  avgt              96.318          ns/op
 * VavrSequencedChampMapJmh.mIterate                   10  avgt             222.893          ns/op
 * VavrSequencedChampMapJmh.mIterate              1000000  avgt        73357417.161          ns/op
 * VavrSequencedChampMapJmh.mPut                       10  avgt             223.565          ns/op
 * VavrSequencedChampMapJmh.mPut                  1000000  avgt             846.578          ns/op
 * VavrSequencedChampMapJmh.mRemoveThenAdd             10  avgt             365.420          ns/op
 * VavrSequencedChampMapJmh.mRemoveThenAdd        1000000  avgt            1166.389          ns/op
 * VavrSequencedChampMapJmh.mTail                      10  avgt             248.558          ns/op
 * VavrSequencedChampMapJmh.mTail                 1000000  avgt             373.650          ns/op
 * VavrSequencedChampSetJmh.mContainsFound             10  avgt               5.045          ns/op
 * VavrSequencedChampSetJmh.mContainsFound        1000000  avgt             219.930          ns/op
 * VavrSequencedChampSetJmh.mContainsNotFound          10  avgt               5.033          ns/op
 * VavrSequencedChampSetJmh.mContainsNotFound     1000000  avgt             219.717          ns/op
 * VavrSequencedChampSetJmh.mHead                      10  avgt               1.799          ns/op
 * VavrSequencedChampSetJmh.mHead                 1000000  avgt              11.397          ns/op
 * VavrSequencedChampSetJmh.mIterate                   10  avgt             197.473          ns/op
 * VavrSequencedChampSetJmh.mIterate              1000000  avgt        75740944.368          ns/op
 * VavrSequencedChampSetJmh.mRemoveThenAdd             10  avgt             364.559          ns/op
 * VavrSequencedChampSetJmh.mRemoveThenAdd        1000000  avgt            1146.828          ns/op
 * VavrSequencedChampSetJmh.mTail                      10  avgt             145.895          ns/op
 * VavrSequencedChampSetJmh.mTail                 1000000  avgt             251.419          ns/op
 * </pre>
 */
@State(Scope.Benchmark)
@Measurement(iterations = 0)
@Warmup(iterations = 0)
@Fork(value = 0)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class VavrChampMapJmh {
    @Param({"10", "1000000"})
    private int size;

    private final int mask = ~64;

    private BenchmarkData data;
    private ChampMap<Key, Boolean> mapA;


    @Setup
    public void setup() {
        data = new BenchmarkData(size, mask);
        mapA = ChampMap.empty();
        for (Key key : data.setA) {
            mapA = mapA.put(key, Boolean.TRUE);
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
        Key key = data.nextKeyInA();
        mapA.remove(key).put(key, Boolean.TRUE);
    }

    @Benchmark
    public void mPut() {
        Key key = data.nextKeyInA();
        mapA.put(key, Boolean.FALSE);
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
