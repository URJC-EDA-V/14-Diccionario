package es.urjc.grafo.EDA.mapas;

import es.urjc.grafo.EDA.utils.Pair;

import java.util.Map;

public class MapaDireccionamientoAbiertoPruebaLineal<K, V> extends AbstractMapaDireccionamientoAbierto<K, V> {

    public MapaDireccionamientoAbiertoPruebaLineal() {
        super();
    }

    public MapaDireccionamientoAbiertoPruebaLineal(int capacity) {
        super(capacity);
    }

    public MapaDireccionamientoAbiertoPruebaLineal(int capacity, double loadFactor) {
        super(capacity, loadFactor);
    }

    public MapaDireccionamientoAbiertoPruebaLineal(double loadFactor) {
        super(loadFactor);
    }

    @Override
    protected void rehash(int desiredCapacity) {
        MapaDireccionamientoAbiertoPruebaLineal<K, V> nuevoMapa = new MapaDireccionamientoAbiertoPruebaLineal<>(desiredCapacity);
        for (Map.Entry<K, V> entry : this.entries()) {
            nuevoMapa.put(entry.getKey(), entry.getValue());
        }
        this.table = nuevoMapa.table;
        this.size = nuevoMapa.size;
        this.a = nuevoMapa.a;
        this.b = nuevoMapa.b;
        this.prime = nuevoMapa.prime;
    }

    /**
     * Busca la entrada correspondiente a la clave key. Si la encuentra, devuelve un par (true, índice) donde índice es la posición
     * en la tabla hash donde se encuentra la entrada. Si no la encuentra, devuelve un par (false, índice) donde índice es la
     * primera posición disponible (vacía o desactivada) donde se podría insertar una nueva entrada con dicha clave.
     *
     * @param key la clave a buscar
     * @return un par (boolean, int) indicando si se encontró la clave y el índice en el que se encuentra
     * (si el boolean es true) o se podría insertar (si el boolean es false)
     */
    @Override
    protected Pair<Boolean, Integer> findEntry(K key) {
        int firstAvailableSlot = -1;
        int start = this.hashValue(key);
        int currentIndex = start;
        do {
            if (this.isAvailable(currentIndex)) {
                // Esta posición está disponible.
                if (firstAvailableSlot == -1) {
                    // Guardamos la primera posición disponible encontrada. Si no encontramos la clave,
                    // esta será la posición donde insertar la nueva entrada.
                    firstAvailableSlot = currentIndex;
                }
                if (this.table[currentIndex] == null) {
                    // Si la posición está disponible, entonces es null (nunca ha sido ocupada) o AVAILABLE (ha sido ocupada pero luego eliminada).
                    // Si la posición es null, hemos llegado a una posición nunca ocupada, por lo que la clave no está en la tabla.
                    // Si fuese AVAILABLE, tenemos que seguir mirando, porque la clave puede estar más adelante.
                    return new Pair<>(false, firstAvailableSlot);
                }
            } else if (this.table[currentIndex].getKey().equals(key)) {
                // Hemos encontrado la clave.
                return new Pair<>(true, currentIndex);
            }
            currentIndex = (currentIndex + 1) % this.table.length;
        } while (currentIndex != start); // Si volvemos a la posición inicial, hemos recorrido toda la tabla sin encontrar la clave.
        return new Pair<>(false, firstAvailableSlot); // No hemos encontrado la clave.
    }

}
