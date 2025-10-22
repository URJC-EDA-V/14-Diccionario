package es.urjc.grafo.EDA.diccionarios;

import es.urjc.grafo.EDA.mapas.Mapa;

import java.util.Map.Entry;

/**
 * Dictionary interface.
 * A dictionary behaves similarly to a map, but allows multiple entries with the same key.
 *
 * @param <K>
 * @param <V>
 */
public interface Dictionary<K, V> extends Mapa<K, V>, Iterable<Entry<K, V>> {

    /**
     * Devuelve un Iterable que contiene todos los valores asociados a la clave key.
     *
     * @param key used in the search
     * @return un Iterable con todos los valores asociados a la clave key
     */
    Iterable<V> getAll(K key);

    /**
     * Elimina todas las entradas con la clave dada.
     * Devuelve un Iterable con todos los valores asociados a las entradas eliminadas.
     *
     * @param key la clave cuyas entradas se desean eliminar
     * @return un Iterable con los valores de las entradas eliminadas
     */
    Iterable<V> removeAll(K key);

}
