package es.urjc.grafo.EDA.mapas;

import es.urjc.grafo.EDA.utils.Pair;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public abstract class AbstractMapaDireccionamientoAbierto<K, V> extends AbstractMapa<K, V> {

    protected final Map.Entry<K, V> AVAILABLE = new MapEntry<>(null, null);
    protected Map.Entry<K, V>[] table;
    private double loadFactor = 0.5;
    private int numberOfSentinels = 0;

    public AbstractMapaDireccionamientoAbierto() {
        super();
        initializeObject(CAPACITY, this.loadFactor);
    }

    public AbstractMapaDireccionamientoAbierto(double loadFactor) {
        super();
        initializeObject(CAPACITY, loadFactor);
    }

    public AbstractMapaDireccionamientoAbierto(int capacity) {
        super();
        initializeObject(capacity, this.loadFactor);
    }

    public AbstractMapaDireccionamientoAbierto(int capacity, double loadFactor) {
        super();
        initializeObject(capacity, loadFactor);
    }

    private void initializeObject(int capacity, double loadFactor) {
        table = new MapEntry[capacity];
        this.loadFactor = loadFactor;
    }

    @Override
    protected int hashValue(K key) {
        return key == null ? 0 : Math.abs(((a * key.hashCode() + b) % prime) % this.table.length);
    }

    @Override
    public V put(K key, V value) {
        Pair<Boolean, Integer> resultado = findEntry(key);
        if (resultado.getFirst()) { // this key has an existing entry
            return table[resultado.getSecond()].setValue(value);
        } else {
            if (table[resultado.getSecond()] == AVAILABLE) {
                this.numberOfSentinels--;
            }
            table[resultado.getSecond()] = new MapEntry<>(key, value); // convert to proper index
            this.size++;
            rehashIfNeeded();
            return null;
        }
    }

    private void rehashIfNeeded() {
        if (this.size() + this.numberOfSentinels > this.table.length * this.loadFactor) // keep load factor <= 0.5
            rehash();
    }

    @Override
    public V get(K key) {
        Pair<Boolean, Integer> resultado = findEntry(key);
        if (resultado.getFirst()) {
            return this.table[resultado.getSecond()].getValue();
        } else {
            return null;
        }
    }

    @Override
    public V remove(K key) {
        Pair<Boolean, Integer> resultado = findEntry(key);
        if (!resultado.getFirst()) return null; // nothing to remove
        else {
            V answer = this.table[resultado.getSecond()].getValue();
            this.table[resultado.getSecond()] = AVAILABLE; // mark this slot as deactivated
            this.numberOfSentinels++;
            this.size--;
            return answer;
        }
    }

    @Override
    public Iterable<Map.Entry<K, V>> entries() {
        return new EntryIterable();
    }

    @Override
    public boolean containsKey(K key) {
        return this.findEntry(key).getFirst();
    }

    @Override
    public boolean containsValue(V value) {
        for (Map.Entry<K, V> entry : table) {
            if (entry != null && entry != AVAILABLE && entry.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if location is either empty or the ”defunct” sentinel.
     */
    protected boolean isAvailable(int j) {
        return (table[j] == null || table[j] == AVAILABLE);
    }

    /**
     * Doubles the size of the hash table and rehashes all the entries.
     */
    private void rehash() {
        this.rehash(2 * this.table.length - 1);
    }

    /**
     * Creates a new hash table with the specified capacity and rehashes all the entries. The new capacity
     * must be at least as large as the number of entries currently in the map.
     *
     * @param desiredCapacity new capacity
     */
    protected abstract void rehash(int desiredCapacity);


    /**
     * Busca la entrada correspondiente a la clave key. Si la encuentra, devuelve un par (true, índice) donde índice es la posición
     * en la tabla hash donde se encuentra la entrada. Si no la encuentra, devuelve un par (false, índice) donde índice es la
     * primera posición disponible (vacía o desactivada) donde se podría insertar una nueva entrada con dicha clave.
     *
     * @param key la clave a buscar
     * @return un par (boolean, int) indicando si se encontró la clave y el índice en el que se encuentra
     * (si el boolean es true) o se podría insertar (si el boolean es false)
     */
    protected abstract Pair<Boolean, Integer> findEntry(K key);

    private class EntryIterator implements Iterator<Map.Entry<K, V>> {

        private int currentIndex = 0;

        public EntryIterator() {
            advanceToNext();
        }

        private void advanceToNext() {
            while (currentIndex < table.length && (table[currentIndex] == null || table[currentIndex] == AVAILABLE)) {
                currentIndex++;
            }
        }

        public boolean hasNext() {
            return currentIndex < table.length;
        }

        public Map.Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Map.Entry<K, V> entry = table[currentIndex];
            currentIndex++;
            advanceToNext();
            return entry;
        }

    }

    private class EntryIterable implements Iterable<Map.Entry<K, V>> {

        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

    }
}
