package es.urjc.grafo.EDA.conjuntos;

public interface Conjunto<K> {
    /**
     * Returns the number of elements in this set.
     *
     * @return the number of elements
     */
    int size();

    /**
     * Returns whether this set contains no elements.
     *
     * @return {@code true} if this set is empty, {@code false} otherwise
     */
    boolean isEmpty();

    /**
     * Adds an key to this set if it is not already present.
     *
     * @param key the key to add
     * @return {@code true} if the set changed, {@code false} if it already contained the key
     */
    boolean add(K key);

    /**
     * Removes an key from this set if it is present.
     *
     * @param key the key to remove
     * @return {@code true} if the set contained the key, {@code false} otherwise
     */
    boolean remove(K key);

    /**
     * Returns whether this set contains the specified key.
     *
     * @param key the key to check
     * @return {@code true} if this set contains the key, {@code false} otherwise
     */
    boolean contains(K key);

    /**
     * Returns an iterable over the elements in this set.
     *
     * @return an iterable containing the elements in this set
     */
    Iterable<K> elements();

}
