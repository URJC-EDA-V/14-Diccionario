package es.urjc.grafo.EDA.mapas;

import es.urjc.grafo.EDA.utils.Pair;

import java.util.Map;

public class MapaDireccionamientoAbiertoDobleHash<K, V> extends AbstractMapaDireccionamientoAbierto<K, V> {

    private final int primoSegundoHash = 7;

    public MapaDireccionamientoAbiertoDobleHash() {
        super();
    }

    public MapaDireccionamientoAbiertoDobleHash(int capacity) {
        super(capacity);
    }

    public MapaDireccionamientoAbiertoDobleHash(int capacity, double loadFactor) {
        super(capacity, loadFactor);
    }

    public MapaDireccionamientoAbiertoDobleHash(double loadFactor) {
        super(loadFactor);
    }

    @Override
    protected void rehash(int desiredCapacity) {
        MapaDireccionamientoAbiertoDobleHash<K, V> nuevoMapa = new MapaDireccionamientoAbiertoDobleHash<>(desiredCapacity);
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
        int prueba = 0;
        int hashValue = this.hashValue(key);
        int alternativeHashValue = alternativeHash(key);
        do {
            int currentIndex = Math.abs(hashValue + (prueba * alternativeHashValue)) % this.table.length;
            if (isAvailable(currentIndex)) { // may be either empty or defunct (available)
                if (firstAvailableSlot == -1) firstAvailableSlot = currentIndex; // this is the first available slot!
                if (this.table[currentIndex] == null) break; // if empty, search fails immediately
            } else if (this.table[currentIndex].getKey().equals(key)) {
                return new Pair<>(true, currentIndex); // successful match
            }
            prueba++;
        } while (prueba < this.table.length); // stop if we return to the start
        return new Pair<>(false, firstAvailableSlot); // search has failed
    }

    private int alternativeHash(K k) {
        return this.primoSegundoHash - (k.hashCode() % this.primoSegundoHash);
    }

}
