package es.urjc.grafo.EDA.mapas;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

public abstract class AbstractMapa<K, V> implements Mapa<K, V> {

    protected static int CAPACITY = 1001;
    protected int prime;
    protected int a;
    protected int b;
    protected int size;

    /**
     * Creates a hash table
     */
    public AbstractMapa() {
        this(999983, CAPACITY);
    }

    /**
     * Creates a hash table.
     *
     * @param cap initial capacity
     */
    public AbstractMapa(int cap) {
        this(999983, cap);
    }

    /**
     * Creates a hash table with the given prime factor and capacity.
     *
     * @param prime    prime number
     * @param capacity initial capacity
     */
    public AbstractMapa(int prime, int capacity) {
        this.prime = prime;
        CAPACITY = capacity;
        Random rand = new Random(prime);
        a = 1 + rand.nextInt(this.prime - 1);
        b = rand.nextInt(this.prime);
        size = 0;
    }

    /**
     * Hash function applying MAD method to default hash code.
     *
     * @param key Key
     * @return
     */
    protected abstract int hashValue(K key);

    /**
     * Returns the number of entries in the hash table.
     *
     * @return the size
     */
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    public Iterable<K> keys() {
        return new KeyIterable();
    }

    public Iterable<V> values() {
        return new ValueIterable();
    }

    @Override
    public abstract Iterable<Entry<K, V>> entries();

    protected static class MapEntry<K, V> implements Entry<K, V> {

        private K k; // key
        private V v; // value

        public MapEntry(K key, V value) {
            k = key;
            v = value;
        }

        public K getKey() {
            return k;
        }

        protected void setKey(K key) {
            k = key;
        }

        public V getValue() {
            return v;
        }

        public V setValue(V value) {
            V old = v;
            v = value;
            return old;
        }

    }

    private class KeyIterator implements Iterator<K> {

        private final Iterator<Entry<K, V>> entries = entries().iterator();

        public boolean hasNext() {
            return entries.hasNext();
        }

        public K next() {
            return entries.next().getKey();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private class KeyIterable implements Iterable<K> {

        public Iterator<K> iterator() {
            return new KeyIterator();
        }

    }

    private class ValueIterator implements Iterator<V> {

        private final Iterator<Entry<K, V>> entries = entries().iterator();

        public boolean hasNext() {
            return entries.hasNext();
        }

        public V next() {
            return entries.next().getValue();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private class ValueIterable implements Iterable<V> {

        public Iterator<V> iterator() {
            return new ValueIterator();
        }

    }

    protected abstract class EntriesIterator implements Iterator<K> {

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    protected abstract class EntriesIterable implements Iterable<K> {
    }
}
