package custom.map;

import java.util.Arrays;
import java.util.Objects;

/**
 * Simple Map implementation based on sorted arrays and binary search algorithm.
 * Much more memory efficient than HashMap, but has huge performance degradation on
 * large items count.
 */
public class LongMapImpl<V> implements LongMap<V> {
    private final int DEFAULT_CAPACITY = 16;
    private final int MAX_BUFFER_SIZE = 1000;
    private int itemsCount = 0;

    private long[] keys = new long[DEFAULT_CAPACITY];
    private Object[] values = new Object[DEFAULT_CAPACITY];

    private void resize() {
        if (itemsCount <= DEFAULT_CAPACITY - 1) {
            return;
        }
        if (keys.length == itemsCount) {
            int buffer = Math.round(itemsCount * 0.1f);
            if (buffer > MAX_BUFFER_SIZE) {
                buffer = MAX_BUFFER_SIZE;
            }
            keys = Arrays.copyOf(keys, itemsCount + buffer);
            values = Arrays.copyOf(values, itemsCount + buffer);
            return;
        }
        if (keys.length > Math.round(itemsCount * 1.2f)) {
            int buffer = Math.round(itemsCount * 0.1f);
            if (buffer > MAX_BUFFER_SIZE) {
                buffer = MAX_BUFFER_SIZE;
            }
            keys = Arrays.copyOf(keys, itemsCount + buffer);
            values = Arrays.copyOf(values, itemsCount + buffer);
        }
    }

    public V put(long key, V value) {
        if (itemsCount == 0) {
            keys[0] = key;
            values[0] = value;
            itemsCount++;
            return null;
        }
        int pos = Arrays.binarySearch(keys, 0, itemsCount, key);
        if (pos < 0) {
            pos = Math.abs(pos) - 1;
            System.arraycopy(keys, pos, keys, pos + 1, keys.length - pos - 1);
            System.arraycopy(values, pos, values, pos + 1, values.length - pos - 1);
            keys[pos] = key;
            values[pos] = value;
        } else if (pos == itemsCount) {
            keys[pos] = key;
            values[pos] = value;
        } else {
            Object oldValue = values[pos];
            values[pos] = value;
            return (V) oldValue;
        }
        itemsCount++;
        resize();
        return null;
    }

    public V get(long key) {
        int pos = Arrays.binarySearch(keys, 0, itemsCount, key);
        if (pos < 0 || pos == itemsCount) {
            return null;
        } else {
            return (V) values[pos];
        }
    }

    public V remove(long key) {
        int pos = Arrays.binarySearch(keys, 0, itemsCount, key);
        if (pos < 0 || pos == itemsCount) {
            return null;
        } else {
            Object oldValue = values[pos];
            System.arraycopy(keys, pos + 1, keys, pos, itemsCount - pos - 1);
            System.arraycopy(values, pos + 1, values, pos, itemsCount - pos - 1);
            return (V) oldValue;
        }
    }

    public boolean isEmpty() {
        return itemsCount == 0;
    }

    public boolean containsKey(long key) {
        int pos = Arrays.binarySearch(keys, 0, itemsCount, key);
        return pos >= 0 && pos != itemsCount;
    }

    public boolean containsValue(V value) {
        for (int i = 0; i < itemsCount; i++) {
            if (Objects.deepEquals(values[i], (value))) {
                return true;
            }
        }
        return false;
    }

    public long[] keys() {
        return keys;
    }

    public V[] values() {
        return (V[]) values;
    }

    public long size() {
        return itemsCount;
    }

    public void clear() {
        keys = new long[DEFAULT_CAPACITY];
        values = new Object[DEFAULT_CAPACITY];
        itemsCount = 0;
    }
}
