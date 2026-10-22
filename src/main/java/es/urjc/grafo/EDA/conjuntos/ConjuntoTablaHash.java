package es.urjc.grafo.EDA.conjuntos;

import java.util.*;

public class ConjuntoTablaHash<K> implements Conjunto<K> {

    private static final int INITIAL_CAPACITY = 16;
    private static final float MAX_LOAD_FACTOR = 0.75f;

    private List<K>[] table;
    private int size = 0;
    private final int prime = 2_147_483_647;
    private final int a;
    private final int b;

    public ConjuntoTablaHash() {
        this(INITIAL_CAPACITY);
    }

    public ConjuntoTablaHash(int initialCapacity) {
        Random rand = new Random();
        this.a = 1 + rand.nextInt(this.prime - 1);
        this.b = rand.nextInt(this.prime);
        this.table = (List<K>[]) new List<?>[initialCapacity];
    }

    /**
     * Returns the number of elements in this set.
     *
     * @return the number of elements
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns whether this set contains no elements.
     *
     * @return {@code true} if this set is empty, {@code false} otherwise
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Adds an element to this set if it is not already present.
     *
     * @param key the element to add
     * @return {@code true} if the set changed, {@code false} if it already contained the element
     */
    @Override
    public boolean add(K key) {
        int index = hashValue(key);
        if (table[index] != null && table[index].contains(key)) {
            return false;
        }

        if (table[index] == null) {
            table[index] = new LinkedList<>();
        }
        table[index].add(key);
        size++;

        if (((float) size / table.length) > MAX_LOAD_FACTOR) {
            resize();
        }
        return true;
    }

    /**
     * Removes an element from this set if it is present.
     *
     * @param key the element to remove
     * @return {@code true} if the set contained the element, {@code false} otherwise
     */
    @Override
    public boolean remove(K key) {
        int index = hashValue(key);
        if (table[index] == null || !table[index].remove(key)) {
            return false;
        }

        size--;
        if (table[index].isEmpty()) {
            table[index] = null;
        }
        return true;
    }

    /**
     * Returns whether this set contains the specified element.
     *
     * @param key the element to check
     * @return {@code true} if this set contains the element, {@code false} otherwise
     */
    @Override
    public boolean contains(K key) {
        // TODO
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private int hashValue(K key) {
        return key == null ? 0 : Math.abs(((a * key.hashCode() + b) % prime) % this.table.length);
    }

    private void resize() {
        List<K>[] oldTable = table;
        table = (List<K>[]) new List<?>[oldTable.length * 2];
        for (List<K> bucket : oldTable) {
            if (bucket != null) {
                for (K element : bucket) {
                    int index = hashValue(element);
                    if (table[index] == null) {
                        table[index] = new ArrayList<>();
                    }
                    table[index].add(element);
                }
            }
        }
    }

    /**
     * Returns an iterable over the elements in this set.
     *
     * @return an iterable containing the elements in this set
     */
    @Override
    public Iterable<K> elements() {
        return new ElementsIterable();
    }

    private class ElementsIterable implements Iterable<K> {
        public Iterator<K> iterator() {
            return new ElementsIterator();
        }
    }

    private class ElementsIterator implements Iterator<K> {
        private int bucketIndex;
        private Iterator<K> bucketIterator;

        private ElementsIterator() {
            bucketIndex = -1;
            advanceToNextBucket();
        }

        @Override
        public boolean hasNext() {
            advanceToNextBucket();
            return bucketIterator != null;
        }

        @Override
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return bucketIterator.next();
        }

        private void advanceToNextBucket() {
            while ((bucketIterator == null || !bucketIterator.hasNext())
                    && bucketIndex + 1 < table.length) {
                bucketIndex++;
                List<K> bucket = table[bucketIndex];
                bucketIterator = bucket == null ? null : bucket.iterator();
            }
            if (bucketIterator != null && !bucketIterator.hasNext()) {
                bucketIterator = null;
            }
        }
    }
}
