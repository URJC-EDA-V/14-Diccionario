package es.urjc.grafo.EDA.mapas;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;


/**
 * Separate chaining table implementation of hash tables.
 */
public class MapaEncadenamientoSeparado<K, V> extends AbstractMapa<K, V> {

    protected ArrayList<MapEntry<K, V>>[] table;

    /**
     * Creates a hash table
     */
    public MapaEncadenamientoSeparado() {
        this(999983, CAPACITY);
    }

    /**
     * Creates a hash table.
     *
     * @param cap initial capacity
     */
    public MapaEncadenamientoSeparado(int cap) {

        this(999983, cap);
    }

    /**
     * Creates a hash table with the given prime factor and capacity.
     *
     * @param p   prime number
     * @param cap initial capacity
     */
    public MapaEncadenamientoSeparado(int p, int cap) {
        super(p, cap);
        table = new ArrayList[cap];
    }

    /**
     * Hash function applying MAD method to default hash code.
     *
     * @param key Key
     * @return
     */
    protected int hashValue(K key) {
        return key == null ? 0 : Math.abs(((a * key.hashCode() + b) % prime) % this.table.length);
    }

    @Override
    public Iterable<Map.Entry<K, V>> entries() {
        return new EntryIterable();
    }

    /**
     * Returns the value associated with a key.
     *
     * @param key
     * @return value
     */
    @Override
    public V get(K key) {
        ArrayList<MapEntry<K, V>> bucket = table[hashValue(key)];
        if (bucket != null) {
            int index = find(bucket, key);
            if (index != -1) {
                return bucket.get(index).getValue();
            }
        }
        return null;
    }

    protected int find(ArrayList<MapEntry<K, V>> bucket, K key) {
        int index = 0;
        for (MapEntry<K, V> hashEntry : bucket) {
            if (hashEntry.getKey().equals(key)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * Put a key-value pair in the map, replacing previous one if it exists.
     *
     * @param key
     * @param value
     * @return value
     */
    @Override
    public V put(K key, V value) {
        ArrayList<MapEntry<K, V>> bucket = this.table[hashValue(key)];
        V valueToReturn = null;
        if (bucket == null) {
            bucket = new ArrayList<>();
            this.table[hashValue(key)] = bucket;
        } else {
            valueToReturn = remove(key);
        }
        size++;
        bucket.add(new MapEntry<>(key, value));
        if (this.needToResize()) {
            this.rehash(this.table.length * 2);
        }
        return valueToReturn;
    }

    protected boolean needToResize() {
        return size >= this.table.length / 2;
    }

    /**
     * Removes the key-value pair with a specified key.
     *
     * @param key
     * @return
     */
    @Override
    public V remove(K key) {
        ArrayList<MapEntry<K, V>> bucket = this.table[hashValue(key)];
        V toReturn = null;
        if (bucket != null) {
            int index = find(bucket, key);
            if (index != -1) {
                toReturn = bucket.remove(index).getValue();
                size--;
            }
        }

        return toReturn;
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
        ArrayList<MapEntry<K, V>> bucket = table[hashValue(key)];
        if (bucket != null) {
            int index = find(bucket, key);
            return index != -1;
        }
        return false;
    }

    public boolean containsValue(V value) {
        for (V v : this.values()) {
            if (v.equals(value)) return true;
        }
        return false;
    }

    /**
     * Increase/reduce the size of the hash table and rehashes all the entries.
     *
     * @param desiredCapacity
     */
    protected void rehash(int desiredCapacity) {
        MapaEncadenamientoSeparado<K, V> nuevoMapa = new MapaEncadenamientoSeparado<>(desiredCapacity);
        for (Map.Entry<K, V> entry : this.entries()) {
            nuevoMapa.put(entry.getKey(), entry.getValue());
        }
        this.table = nuevoMapa.table;
        this.size = nuevoMapa.size;
        this.a = nuevoMapa.a;
        this.b = nuevoMapa.b;
        this.prime = nuevoMapa.prime;
    }

    protected class EntryIterator implements Iterator<Map.Entry<K, V>> {

        private int currentBucket = -1;
        private Iterator<MapEntry<K, V>> iteratorCurrentBucket;

        public EntryIterator() {
            // Find first non-empty bucket
            findNextBucket();
        }

        private void findNextBucket() {
            do {
                currentBucket++;
            } while (currentBucket < table.length && (table[currentBucket] == null || table[currentBucket].isEmpty()));
            if (currentBucket < table.length) {
                // Found a non-empty bucket
                this.iteratorCurrentBucket = table[currentBucket].iterator();
            }
        }

        public boolean hasNext() {
            return this.iteratorCurrentBucket.hasNext();
        }

        public Map.Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Map.Entry<K, V> entry = this.iteratorCurrentBucket.next();
            if (!this.iteratorCurrentBucket.hasNext()) {
                findNextBucket();
            }
            return entry;
        }

    }

    private class EntryIterable implements Iterable<Map.Entry<K, V>> {

        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

    }
}
