package es.urjc.grafo.EDA.mapas;

import es.urjc.grafo.EDA.utils.Pair;

import java.util.Map;

public class MapaDireccionamientoAbiertoPruebaCuadratica<K, V> extends AbstractMapaDireccionamientoAbierto<K, V> {

    private final int constante1 = 7;
    private final int constante2 = 3;

    public MapaDireccionamientoAbiertoPruebaCuadratica() {
        super();
    }

    public MapaDireccionamientoAbiertoPruebaCuadratica(int capacity) {
        super(capacity);
    }

    public MapaDireccionamientoAbiertoPruebaCuadratica(int capacity, double loadFactor) {
        super(capacity, loadFactor);
    }

    public MapaDireccionamientoAbiertoPruebaCuadratica(double loadFactor) {
        super(loadFactor);
    }

    @Override
    protected void rehash(int desiredCapacity) {
        MapaDireccionamientoAbiertoPruebaCuadratica<K, V> nuevoMapa = new MapaDireccionamientoAbiertoPruebaCuadratica<>(desiredCapacity);
        for (Map.Entry<K, V> entry : this.entries()) {
            nuevoMapa.put(entry.getKey(), entry.getValue());
        }
        this.table = nuevoMapa.table;
        this.size = nuevoMapa.size;
        this.a = nuevoMapa.a;
        this.b = nuevoMapa.b;
        this.prime = nuevoMapa.prime;
    }

    @Override
    protected Pair<Boolean, Integer> findEntry(K key) {
        int firstAvailableSlot = -1; // no slot available (thus far)
        int prueba = 0; // index while scanning table
        int hashValue = hashValue(key);
        do {
            int currentIndex = (hashValue + constante1 * prueba + constante2 * (prueba ^ 2)) % this.table.length;
            if (isAvailable(currentIndex)) { // may be either empty or available
                if (firstAvailableSlot == -1) firstAvailableSlot = currentIndex; // this is the first available slot!
                if (this.table[currentIndex] == null) break; // if empty, search fails immediately
            } else if (this.table[currentIndex].getKey().equals(key)) {
                return new Pair<>(true, currentIndex); // successful match
            }
            prueba++;
        } while (prueba < this.table.length); // stop if we return to the start
        return new Pair<>(false, firstAvailableSlot); // search has failed
    }

}
