package benchmark.jmh.benchmarks;

import benchmark.jmh.DummyObject;
import gnu.trove.map.hash.TLongObjectHashMap;
import org.openjdk.jmh.annotations.*;

import java.util.Random;

import static benchmark.jmh.AppStarter.props;

@State(Scope.Benchmark)
public class TroveLongMap extends BenchmarkAbstract {
    TLongObjectHashMap testedMap;

    @Setup(Level.Iteration)
    public void setup() {
        testedMap = new TLongObjectHashMap();
        for (i = 1; i <= props.getMapItemsCount(); i++) {
            testedMap.put(i, new DummyObject());
        }
    }

    @Benchmark
    public Object sequentialPutToMap() {
        testedMap.clear();
        for (i = 1; i <= props.getMapItemsCount(); i++) {
            testedMap.put(i, new DummyObject());
        }
        setInvokedMethod("sequentialPutToMap");
        return testedMap;
    }

    @SuppressWarnings("Duplicates")
    @Benchmark
    public Object randomPutToMap() {
        testedMap.clear();
        for (i = 1; i <= props.getMapItemsCount(); i += 2) {
            testedMap.put(i, new DummyObject());
        }
        for (i = 2; i <= props.getMapItemsCount(); i += 2) {
            testedMap.put(i, new DummyObject());
        }
        setInvokedMethod("randomPutToMap");
        return testedMap;
    }

    @Benchmark
    public Object getFromMap(TroveLongMap troveLongMap) {
        for (i = 1; i <= props.getMapItemsCount(); i += 2) {
            troveLongMap.testedMap.put(i, new DummyObject());
        }
        setInvokedMethod("getFromMap");
        return testedMap;
    }

    @Benchmark
    public Object removeFromMap(TroveLongMap troveLongMap) {
        for (i = 1; i <= (props.getMapItemsCount() / 2); i++) {
            troveLongMap.testedMap.remove(new Random().nextInt(testedMap.size()));
        }
        setInvokedMethod("removeFromMap");
        return testedMap;
    }

    @Benchmark
    public Object containsKey(TroveLongMap troveLongMap) {
        boolean res = false;
        for (i = 1; i <= (props.getMapItemsCount() / 2); i++) {
            res = troveLongMap.testedMap.containsKey(new Random().nextInt(testedMap.size() * 2));
        }
        setInvokedMethod("containsKey");
        return res;
    }

    @Benchmark
    public Object containsValue(TroveLongMap troveLongMap) {
        boolean res = false;
        for (i = 1; i <= (props.getMapItemsCount() / 2); i++) {
            res = troveLongMap.testedMap.containsValue(new DummyObject());
        }
        setInvokedMethod("containsValue");
        return res;
    }

    @TearDown(Level.Iteration)
    public void getResults() {
        evaluateSizeAfterIteration(testedMap, invokedMethod);
    }
}
