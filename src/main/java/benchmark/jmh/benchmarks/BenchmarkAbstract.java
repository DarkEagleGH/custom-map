package benchmark.jmh.benchmarks;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javamex.classmexer.MemoryUtil;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

import static benchmark.jmh.AppStarter.props;
import static benchmark.jmh.Utils.sizeFormatter;

@State(Scope.Benchmark)
abstract class BenchmarkAbstract {
    long i;
    String invokedMethod;
    LinkedHashMap<String, Long> memoryResults;

    void evaluateSizeAfterIteration(Object target, String invokedMethod) {
        if (props.isUseAgent()) {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference ref = new TypeReference<LinkedHashMap<String, Long>>() {};
            try {
                memoryResults = mapper.readValue(new File(props.getResultsTempFile()), ref);
            } catch (IOException e) {
                memoryResults = new LinkedHashMap<>();
            }
            long objectSize = MemoryUtil.deepMemoryUsageOf(target);
            try {
                memoryResults.put(invokedMethod, memoryResults.getOrDefault(invokedMethod, 0L) + objectSize);
                mapper.writeValue(new File(props.getResultsTempFile()), memoryResults);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.printf("\nMemory: %s B. ", sizeFormatter(objectSize));
        }
    }

    void setInvokedMethod(String invokedMethod) {
        this.invokedMethod = getClass().getSimpleName().replaceAll("_\\w+$", ".") + invokedMethod;
    }
}
