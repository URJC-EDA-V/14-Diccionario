package es.urjc.grafo.EDA.mapas;

public class Alumno {

    private final String nombre;
    private final int dni;

    public Alumno(String nombre, int dni) {
        this.nombre = nombre;
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve un valor de código hash para el objeto. Este método está
     * pensado para su uso en tablas hash como las proporcionadas por
     * {@link java.util.HashMap}.
     * <p>
     * Este {@code hashCode} se calcula usando primero los bytes de {@code nombre}
     * y después {@code dni}. Utiliza acumulación polinómica para calcular
     * el {@code hashCode}.
     * <p>
     * El contrato general de {@code hashCode} es:
     * <ul>
     * <li>Siempre que se invoque sobre el mismo objeto más de una vez durante
     * la ejecución de una aplicación Java, el método {@code hashCode}
     * debe devolver de forma consistente el mismo entero, siempre que no se
     * modifique ninguna información utilizada en las comparaciones de {@code equals}
     * del objeto. </li>
     * <li>Si dos objetos son iguales según el método {@code equals(Object)},
     * entonces al invocar el método {@code hashCode} en cada uno de los dos objetos
     * se debe producir el mismo resultado entero.</li>
     * <li>No se requiere que si dos objetos no son iguales según el
     * método {@link Object#equals(Object)}, entonces al invocar el método
     * {@code hashCode} en cada uno de los dos objetos se produzcan resultados enteros
     * distintos. No obstante, el programador debe ser consciente de que producir
     * resultados enteros distintos para objetos desiguales puede mejorar el rendimiento
     * de las tablas hash.</li>
     * </ul>
     *
     * @return código hash de este objeto
     */
    @Override
    public int hashCode() {
        int cont = 0;
        int hash = 0;
        int a = 33;
        for (byte aByte : nombre.getBytes()) {
            hash += (int) (aByte * Math.pow(a, cont));
            cont++;
        }
        hash = dni + hash * a;

        return hash;

        // En realidad, en la práctica usamos lo siguiente,
        // que se encarga de hacer la acumulación polinómica por nosotros:
        // return Objects.hash(nombre, dni);

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Alumno other = (Alumno) obj;
        if (this.dni != other.dni) {
            return false;
        }
        return this.nombre.equals(other.nombre);
    }
}
