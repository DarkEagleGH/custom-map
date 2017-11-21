package benchmark.jmh.configs;

public enum DefaultProps {
    MAP_ITEMS_COUNT("100000000"),
    USE_AGENT("true"),
    AGENT_NAME("classmexer-1.0.jar"),
    HEAP_START_SIZE("6g"),
    HEAP_MAX_SIZE("6g"),
    WARMUP_ITERATIONS("4"),
    ITERATIONS("4"),
    FORKS("1"),
    RESULTS_TEMP_FILE("memoryResults.tmp");

    private String propValue;

    DefaultProps(String propValue) {
        this.propValue = propValue;
    }

    public String value() {
        return propValue;
    }
}
