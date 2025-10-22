package es.urjc.grafo.EDA.mapas;

import java.util.Map.Entry;

public interface Mapa<K, V> {
    /**
     * Returns the number of items in the map.
     */
    int size();

    /**
     * Returns whether the map is empty.
     */
    boolean isEmpty();

    /**
     * If there is an entry with the specified key, replaces the value of this
     * entry with the specified value and returns the old value. Else, adds a
     * new entry with the specified key and value and returns null.
     */
    V put(K key, V value);

    /**
     * Returns the value of the entry containing the given key. Returns null if
     * no such entry exists.
     */
    V get(K key);

    /**
     * If there is an entry with the specified key, removes this entry and
     * returns its value. Else, returns null.
     */
    V remove(K key);

    /**
     * Returns an iterable object containing all the keys in the map.
     */
    Iterable<K> keys();

    /**
     * Returns an iterable object containing all the values in the map.
     */
    Iterable<V> values();

    /**
     * Returns an iterable object containing all the entries in the map.
     */
    Iterable<Entry<K, V>> entries();

    /**
     * Returns true if the map contains an entry with the specified key.
     * Otherwise, returns false.
     *
     * @param key
     * @return true if the map contains an entry with the specified key, false otherwise.
     */
    boolean containsKey(K key);

    /**
     * Returns true if the map contains at least one entry with the specified value.
     * Otherwise, returns false.
     *
     * @param value
     * @return true if the map contains at least one entry with the specified value, false otherwise.
     */
    boolean containsValue(V value);
}
