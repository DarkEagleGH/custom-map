package benchmark.jmh;

import java.util.Random;

public class DummyObject {
    private String msg = "I am dummy object ";
    private int internalValue;

    public DummyObject() {
        internalValue = new Random().nextInt(1000);
    }

    @Override
    public String toString() {
        return msg.concat(String.valueOf(internalValue));
    }
}
