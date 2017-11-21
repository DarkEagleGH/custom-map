package benchmark.jmh.configs;

import java.util.Properties;

import static benchmark.jmh.configs.DefaultProps.*;

public final class Props {
    private long mapItemsCount;
    private boolean useAgent;
    private String agentName;
    private String heapStartSize;
    private String heapMaxSize;
    private int warmupIterations;
    private int iterations;
    private int forks;
    private String resultsTempFile;

    public Props(Properties properties) {
        this.mapItemsCount = Long.parseLong(properties.getProperty("mapItemsCount", MAP_ITEMS_COUNT.value()));
        this.useAgent = Boolean.parseBoolean(properties.getProperty("useAgent", USE_AGENT.value()));
        this.agentName = properties.getProperty("agentName", AGENT_NAME.value());
        this.heapStartSize = properties.getProperty("heapStartSize", HEAP_START_SIZE.value());
        this.heapMaxSize = properties.getProperty("heapMaxSize", HEAP_MAX_SIZE.value());
        this.warmupIterations = Integer.parseInt(properties.getProperty("warmupIterations", WARMUP_ITERATIONS.value()));
        this.iterations = Integer.parseInt(properties.getProperty("iterations", ITERATIONS.value()));
        this.forks = Integer.parseInt(properties.getProperty("forks", FORKS.value()));
        this.resultsTempFile = properties.getProperty("resultsTempFile", RESULTS_TEMP_FILE.value());
    }

    public long getMapItemsCount() {
        return mapItemsCount;
    }

    public boolean isUseAgent() {
        return useAgent;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getHeapStartSize() {
        return heapStartSize;
    }

    public String getHeapMaxSize() {
        return heapMaxSize;
    }

    public int getWarmupIterations() {
        return warmupIterations;
    }

    public int getIterations() {
        return iterations;
    }

    public int getForks() {
        return forks;
    }

    public String getResultsTempFile() {
        return resultsTempFile;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("mapItemsCount: ").append(mapItemsCount);
        sb.append("\nuseAgent: ").append(useAgent);
        sb.append("\nagentName: ").append(agentName);
        sb.append("\nheapStartSize: ").append(heapStartSize);
        sb.append("\nheapMaxSize: ").append(heapMaxSize);
        sb.append("\nwarmupIterations: ").append(warmupIterations);
        sb.append("\niterations: ").append(iterations);
        sb.append("\nforks: ").append(forks);
        return sb.toString();
    }
}
