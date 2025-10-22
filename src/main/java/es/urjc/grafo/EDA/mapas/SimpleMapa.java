package es.urjc.grafo.EDA.mapas;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

/**
 * Este mapa implementa una tabla hash sin resolver colisiones. Además, tiene un tamaño fijo y no se redimensiona.
 *
 * @param <K> las claves
 * @param <V> los valores
 */
public class SimpleMapa<K, V> extends AbstractMapa<K, V> {

    private final MapEntry<K, V>[] table;

    public SimpleMapa() {
        super();
        table = new MapEntry[CAPACITY];
    }

    public SimpleMapa(int capacity) {
        super(capacity);
        table = new MapEntry[capacity];
    }

    public V get(K key) {
        int j = hashValue(key);
        if (table[j] == null) return null;
        return table[j].getValue();
    }

    /**
     * Associates the specified value with the specified key in this map
     * (optional operation).  If the map previously contained a mapping for
     * the key, the old value is replaced by the specified value.  (A map
     * {@code m} is said to contain a mapping for a key {@code k} if and only
     * if {@link #containsKey(Object) m.containsKey(k)} would return
     * {@code true}.)
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with {@code key}, or
     * {@code null} if there was no mapping for {@code key}.
     * (A {@code null} return can also indicate that the map
     * previously associated {@code null} with {@code key},
     * if the implementation supports {@code null} values.)
     */
    public V put(K key, V value) {
        int j = hashValue(key);
        V oldValue = null;
        if (table[j] != null) {
            oldValue = table[j].getValue();
        }
        this.table[j] = new MapEntry<>(key, value);
        size++;
        return oldValue;
    }

    /**
     * Hash function applying MAD method to default hash code.
     *
     * @param key Key
     * @return
     */
    protected int hashValue(K key) {
        return Math.abs(((a * key.hashCode() + b) % prime) % CAPACITY);
    }

    public V remove(K key) {
        int j = hashValue(key);
        if (table[j] == null) return null;
        V oldValue = table[j].getValue();
        table[j] = null;
        size--;
        return oldValue;
    }

    public Iterable<Entry<K, V>> entries() {
        return new EntryIterable();
    }

    /**
     * Returns {@code true} if this map contains a mapping for the specified
     * key.  More formally, returns {@code true} if and only if
     * this map contains a mapping for a key {@code k} such that
     * {@code Objects.equals(key, k)}.  (There can be
     * at most one such mapping.)
     *
     * @param key key whose presence in this map is to be tested
     * @return {@code true} if this map contains a mapping for the specified
     * key
     */
    public boolean containsKey(K key) {
        int j = hashValue(key);
        if (table[j] == null) return false;
        return table[j].getKey().equals(key);
    }

    public boolean containsValue(V value) {
        for (V v : this.values()) {
            if (v.equals(value)) return true;
        }
        return false;
    }

    private class EntryIterator implements Iterator<Entry<K, V>> {

        private int currentIndex = 0;
        private int nextIndex = 0;

        public EntryIterator() {
            advanceToNext();
            currentIndex = nextIndex;
            nextIndex++;
            advanceToNext();
        }

        private void advanceToNext() {
            while (nextIndex < table.length && table[nextIndex] == null) {
                nextIndex++;
            }
        }

        public boolean hasNext() {
            return nextIndex < table.length;
        }

        public Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Entry<K, V> entry = table[currentIndex];
            currentIndex = nextIndex;
            nextIndex++;
            advanceToNext();
            return entry;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private class EntryIterable implements Iterable<Entry<K, V>> {

        public Iterator<Entry<K, V>> iterator() {
            return new EntryIterator();
        }

    }
}
