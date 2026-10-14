
public class Pruebas {
    private static int pasadas = 0;
    private static int fallidas = 0;

    private static void comprobar(String nombre, boolean condicion, ListaReproduccion lista) {
        if (condicion && lista.esConsistente()) {
            pasadas++;
            System.out.println("[OK] " + nombre + " | tamaño=" + lista.getTamanio()
                    + " | enlaces consistentes=true");
        } else {
            fallidas++;
            System.out.println("[ERROR] " + nombre + " | tamaño=" + lista.getTamanio()
                    + " | enlaces consistentes=" + lista.esConsistente());
        }
    }

    public static void main(String[] args) {
        ListaReproduccion l = new ListaReproduccion();
        comprobar("Estado inicial vacío", l.estaVacia() && l.getActual() == null, l);
        comprobar("Avanzar en vacío devuelve null", l.avanzar() == null, l);
        comprobar("Retroceder en vacío devuelve null", l.retroceder() == null, l);
        comprobar("Eliminar actual en vacío devuelve null", l.eliminarActual() == null, l);
        comprobar("Buscar ID inexistente en vacío", l.buscarPorId(999) == null, l);

        Cancion a = l.agregarAlFinal("A", "Artista A", 120);
        comprobar("Insertar primera canción la vuelve actual", l.getActual().equals(a), l);
        comprobar("Navegar en ambos sentidos con un nodo", l.avanzar().equals(a)
                && l.retroceder().equals(a), l);

        Cancion b = l.agregarAlFinal("B", "Artista B", 130);
        Cancion c = l.agregarAlInicio("C", "Artista C", 140);
        Cancion d = l.agregarAlFinal("D", "Artista D", 150);
        Cancion e = l.agregarAlFinal("E", "Artista E", 160);
        comprobar("Insertar al inicio/final y conservar actual", l.getTamanio() == 5
                && l.getActual().equals(a) && l.idsAdelante().equals("[3,1,2,4,5]"), l);
        comprobar("Recorrido de cinco canciones en ambos sentidos",
                l.idsAdelante().equals("[3,1,2,4,5]")
                && l.idsAtras().equals("[5,4,2,1,3]"), l);

        l.seleccionarPorId(e.getId());
        comprobar("Avance circular de última a primera", l.avanzar().equals(c), l);
        comprobar("Retroceso circular de primera a última", l.retroceder().equals(e), l);

        comprobar("Buscar ID existente", l.buscarPorId(b.getId()).equals(b), l);
        int sizeAntes = l.getTamanio();
        comprobar("Buscar ID inexistente no modifica lista", l.buscarPorId(999) == null
                && l.getTamanio() == sizeAntes, l);
        comprobar("Eliminar ID inexistente no modifica lista", l.eliminarPorId(999) == null
                && l.getTamanio() == sizeAntes, l);

        l.eliminarPorId(c.getId());
        comprobar("Eliminar primer nodo", l.getTamanio() == 4
                && l.idsAdelante().equals("[1,2,4,5]"), l);
        l.eliminarPorId(e.getId());
        comprobar("Eliminar último nodo", l.getTamanio() == 3
                && l.idsAdelante().equals("[1,2,4]"), l);
        l.eliminarPorId(b.getId());
        comprobar("Eliminar nodo intermedio", l.getTamanio() == 2
                && l.idsAdelante().equals("[1,4]"), l);

        l.seleccionarPorId(d.getId());
        Cancion eliminadaActual = l.eliminarActual();
        comprobar("Eliminar canción actual y seleccionar la siguiente",
                eliminadaActual.equals(d) && l.getActual().getId() == a.getId(), l);

        l.eliminarActual();
        comprobar("Eliminar única canción deja lista vacía", l.estaVacia()
                && l.getActual() == null && l.getTamanio() == 0, l);

        // Simulación equivalente a k reproducciones, con k mayor que el tamaño.
        l.agregarAlFinal("X", "Artista X", 100);
        l.agregarAlFinal("Y", "Artista Y", 110);
        l.agregarAlFinal("Z", "Artista Z", 120);
        l.seleccionarPorId(6);
        for (int i = 0; i < 8; i++) l.avanzar();
        comprobar("Navegación repetida más de dos vueltas", l.getTamanio() == 3
                && l.getActual() != null, l);

        System.out.println();
        System.out.println("Resultado: " + pasadas + " pruebas correctas; " + fallidas + " fallidas.");
        if (fallidas > 0) System.exit(1);
    }
}
