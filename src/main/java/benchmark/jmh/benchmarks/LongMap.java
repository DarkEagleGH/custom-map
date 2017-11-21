package benchmark.jmh.benchmarks;

import benchmark.jmh.DummyObject;
import custom.map.LongMapImpl;
import org.openjdk.jmh.annotations.*;

import java.util.Random;

import static benchmark.jmh.AppStarter.props;

@State(Scope.Benchmark)
public class LongMap extends BenchmarkAbstract {
    LongMapImpl testedMap;

    @Setup(Level.Iteration)
    public void setup() {
        testedMap = new LongMapImpl();
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
    public Object getFromMap(LongMap longMap) {
        for (i = 1; i <= props.getMapItemsCount(); i += 2) {
            longMap.testedMap.put(i, new DummyObject());
        }
        setInvokedMethod("getFromMap");
        return testedMap;
    }

    @Benchmark
    public Object removeFromMap(LongMap longMap) {
        for (i = 1; i <= (props.getMapItemsCount() / 2); i++) {
            longMap.testedMap.remove(new Random().nextInt((int) testedMap.size()));
        }
        setInvokedMethod("removeFromMap");
        return testedMap;
    }

    @Benchmark
    public Object containsKey(LongMap longMap) {
        boolean res = false;
        for (i = 1; i <= (props.getMapItemsCount() / 2); i++) {
            res = longMap.testedMap.containsKey(new Random().nextInt((int) testedMap.size() * 2));
        }
        setInvokedMethod("containsKey");
        return res;
    }

    @Benchmark
    public Object containsValue(LongMap longMap) {
        boolean res = false;
        for (i = 1; i <= (props.getMapItemsCount() / 2); i++) {
            res = longMap.testedMap.containsValue(new DummyObject());
        }
        setInvokedMethod("containsValue");
        return res;
    }

    @TearDown(Level.Iteration)
    public void getResults() {
        evaluateSizeAfterIteration(testedMap, invokedMethod);
    }
}
