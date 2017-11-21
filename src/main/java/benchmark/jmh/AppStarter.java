package benchmark.jmh;

import benchmark.jmh.configs.Props;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static benchmark.jmh.Utils.sizeFormatter;
import static benchmark.jmh.configs.DefaultProps.*;

public class AppStarter {

    public static final Props props = initProperties();
    private static String jarPath;

    public static void main(String[] args) throws RunnerException, URISyntaxException, IOException {
        LinkedHashMap<String, Long> memoryResults = new LinkedHashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(props.getResultsTempFile()), memoryResults);

        ChainedOptionsBuilder chain = new OptionsBuilder()
                .include("benchmark.jmh.benchmarks.*")
                .mode(Mode.Throughput)
                .warmupIterations(props.getWarmupIterations())
                .measurementIterations(props.getIterations())
                .forks(props.getForks())
                .timeout(new TimeValue(1, TimeUnit.HOURS))
                .detectJvmArgs()
                .jvmArgsAppend("-Xms".concat(props.getHeapStartSize()), "-Xmx".concat(props.getHeapMaxSize()));
        if (props.isUseAgent()) {
            chain.jvmArgsPrepend("-javaagent:"
                    .concat(jarPath)
                    .concat(File.separator)
                    .concat(props.getAgentName()));
        }
        new Runner(chain.build()).run();

        if (props.isUseAgent()) {
            try {
                TypeReference ref = new TypeReference<LinkedHashMap<String, Long>>() {};
                memoryResults = mapper.readValue(new File(props.getResultsTempFile()), ref);
                printResults(memoryResults);
            } catch (IOException e) {
                System.out.println("Can't read data from tmp file");
            }
        }
    }

    private static Props initProperties() {
        Properties properties = new Properties();
        try {
            jarPath = new File(AppStarter.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
                    .getParentFile().getAbsolutePath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        try (FileInputStream propertiesInputStream = new FileInputStream(jarPath
                .concat(File.separator)
                .concat("benchmark.properties"))) {
            properties.load(propertiesInputStream);
        } catch (IOException e) {
            properties.clear();
            properties.setProperty("mapItemCount", MAP_ITEMS_COUNT.value());
            properties.setProperty("useAgent", USE_AGENT.value());
            properties.setProperty("heapStartSize", HEAP_START_SIZE.value());
            properties.setProperty("heapMaxSize", HEAP_MAX_SIZE.value());
            properties.setProperty("warmupIterations", WARMUP_ITERATIONS.value());
            properties.setProperty("iterations", ITERATIONS.value());
            properties.setProperty("forks", FORKS.value());
        }
        return new Props(properties);
    }

    private static void printResults(LinkedHashMap<String, Long> memoryResults) {
        System.out.printf("\nAverage memory results (%s items):\n", sizeFormatter(props.getMapItemsCount()));
        long totalIterationPerBenchmark = props.getForks() * (props.getWarmupIterations() + props.getIterations());
        memoryResults.forEach((s, aLong) -> System.out.printf("%-35s %s B\n",
                s, sizeFormatter((long) Math.floor(aLong / totalIterationPerBenchmark))));
    }
}