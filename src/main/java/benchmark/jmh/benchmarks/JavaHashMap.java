package benchmark.jmh.benchmarks;

import java.util.HashMap;
import java.util.Random;

import benchmark.jmh.DummyObject;
import org.openjdk.jmh.annotations.*;

import static benchmark.jmh.AppStarter.props;

@State(Scope.Benchmark)
public class JavaHashMap extends BenchmarkAbstract {
    HashMap testedMap;

    @Setup(Level.Iteration)
    public void setup() {
        testedMap = new HashMap();
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
    public Object getFromMap(JavaHashMap javaHashMap) {
        for (i = 1; i <= props.getMapItemsCount(); i += 2) {
            javaHashMap.testedMap.put(i, new DummyObject());
        }
        setInvokedMethod("getFromMap");
        return testedMap;
    }

    @Benchmark
    public Object removeFromMap(JavaHashMap javaHashMap) {
        for (i = 1; i <= (props.getMapItemsCount() / 2); i++) {
            javaHashMap.testedMap.remove(new Random().nextInt(testedMap.size()));
        }
        setInvokedMethod("removeFromMap");
        return testedMap;
    }

    @Benchmark
    public Object containsKey(JavaHashMap javaHashMap) {
        boolean res = false;
        for (i = 1; i <= (props.getMapItemsCount() / 2); i++) {
            res = javaHashMap.testedMap.containsKey(new Random().nextInt(testedMap.size() * 2));
        }
        setInvokedMethod("containsKey");
        return res;
    }

    @Benchmark
    public Object containsValue(JavaHashMap javaHashMap) {
        boolean res = false;
        for (i = 1; i <= (props.getMapItemsCount() / 2); i++) {
            res = javaHashMap.testedMap.containsValue(new DummyObject());
        }
        setInvokedMethod("containsValue");
        return res;
    }

    @TearDown(Level.Iteration)
    public void getResults() {
        evaluateSizeAfterIteration(testedMap, invokedMethod);
    }
}
