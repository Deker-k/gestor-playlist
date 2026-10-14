
public class ListaReproduccion {
    private Nodo inicio;
    private Nodo fin;
    private Nodo actual;
    private int tamanio;
    private int contadorId; // generador incremental de identificadores

    public ListaReproduccion() {
        inicio = null;
        fin = null;
        actual = null;
        tamanio = 0;
        contadorId = 0;
    }

    // ------------------------------------------------------------------
    // Inserción
    // ------------------------------------------------------------------

    /** Agrega una canción al inicio. O(1). */
    public Cancion agregarAlInicio(String titulo, String artista, int duracion) {
        Nodo nuevo = crearNodo(titulo, artista, duracion);
        if (tamanio == 0) {
            insertarPrimero(nuevo);
        } else {
            nuevo.siguiente = inicio;
            nuevo.anterior = fin;
            fin.siguiente = nuevo;
            inicio.anterior = nuevo;
            inicio = nuevo;
        }
        tamanio++;
        return nuevo.cancion;
    }


    public Cancion agregarAlFinal(String titulo, String artista, int duracion) {
        Nodo nuevo = crearNodo(titulo, artista, duracion);
        if (tamanio == 0) {
            insertarPrimero(nuevo);
        } else {
            nuevo.anterior = fin;
            nuevo.siguiente = inicio;
            fin.siguiente = nuevo;
            inicio.anterior = nuevo;
            fin = nuevo;
        }
        tamanio++;
        return nuevo.cancion;
    }


    private Nodo crearNodo(String titulo, String artista, int duracion) {
        Cancion c = new Cancion(contadorId + 1, titulo, artista, duracion); // puede lanzar excepción
        contadorId++;
        return new Nodo(c);
    }


    private void insertarPrimero(Nodo nuevo) {
        nuevo.anterior = nuevo;
        nuevo.siguiente = nuevo;
        inicio = nuevo;
        fin = nuevo;
        actual = nuevo;
    }

    // ------------------------------------------------------------------
    // Búsqueda y selección
    // ------------------------------------------------------------------


    public Cancion buscarPorId(int id) {
        Nodo n = localizar(id);
        return (n == null) ? null : n.cancion;
    }


    public boolean seleccionarPorId(int id) {
        Nodo n = localizar(id);
        if (n == null) {
            return false;
        }
        actual = n;
        return true;
    }


    private Nodo localizar(int id) {
        Nodo n = inicio;
        for (int i = 0; i < tamanio; i++) {
            if (n.cancion.getId() == id) {
                return n;
            }
            n = n.siguiente;
        }
        return null;
    }

    // ------------------------------------------------------------------
    // Eliminación
    // ------------------------------------------------------------------


    public Cancion eliminarPorId(int id) {
        Nodo n = localizar(id);
        if (n == null) {
            return null;
        }
        return desenlazar(n);
    }


    public Cancion eliminarActual() {
        if (actual == null) {
            return null;
        }
        return desenlazar(actual);
    }


    private Cancion desenlazar(Nodo n) {
        Cancion eliminada = n.cancion;

        if (tamanio == 1) {
            inicio = null;
            fin = null;
            actual = null;
        } else {
            Nodo ant = n.anterior; // conservar referencias antes de cambiar enlaces
            Nodo sig = n.siguiente;
            ant.siguiente = sig;
            sig.anterior = ant;

            if (n == inicio) {
                inicio = sig;
            }
            if (n == fin) {
                fin = ant;
            }
            if (n == actual) {
                actual = sig;
            }
        }

        // El nodo eliminado ya no apunta a la lista (ayuda al recolector y evita usos erróneos).
        n.anterior = null;
        n.siguiente = null;
        tamanio--;
        return eliminada;
    }

    // ------------------------------------------------------------------
    // Consultas
    // ------------------------------------------------------------------


    public Cancion getActual() {
        return (actual == null) ? null : actual.cancion;
    }



    public Cancion avanzar() {
        if (actual == null) {
            return null;
        }
        actual = actual.siguiente;
        return actual.cancion;
    }


    public Cancion retroceder() {
        if (actual == null) {
            return null;
        }
        actual = actual.anterior;
        return actual.cancion;
    }


    public String cancionesAdelante() {
        StringBuilder sb = new StringBuilder();
        Nodo n = inicio;
        for (int i = 0; i < tamanio; i++) {
            if (i > 0) sb.append(System.lineSeparator());
            sb.append(n.cancion);
            n = n.siguiente;
        }
        return sb.toString();
    }


    public String cancionesAtras() {
        StringBuilder sb = new StringBuilder();
        Nodo n = fin;
        for (int i = 0; i < tamanio; i++) {
            if (i > 0) sb.append(System.lineSeparator());
            sb.append(n.cancion);
            n = n.anterior;
        }
        return sb.toString();
    }


    public int getTamanio() {
        return tamanio;
    }

    public boolean estaVacia() {
        return tamanio == 0;
    }

    // ------------------------------------------------------------------
    // Apoyo para verificación (pruebas)
    // ------------------------------------------------------------------


    public String idsAdelante() {
        StringBuilder sb = new StringBuilder("[");
        Nodo n = inicio;
        for (int i = 0; i < tamanio; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(n.cancion.getId());
            n = n.siguiente;
        }
        return sb.append(']').toString();
    }


    public String idsAtras() {
        StringBuilder sb = new StringBuilder("[");
        Nodo n = fin;
        for (int i = 0; i < tamanio; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(n.cancion.getId());
            n = n.anterior;
        }
        return sb.append(']').toString();
    }

    // ------------------------------------------------------------------
    // Verificación de invariantes (para pruebas)
    // ------------------------------------------------------------------


    public boolean esConsistente() {
        if (tamanio == 0) {
            return inicio == null && fin == null && actual == null;
        }
        if (tamanio < 0 || inicio == null || fin == null || actual == null) {
            return false;
        }
        if (fin.siguiente != inicio || inicio.anterior != fin) {
            return false;
        }

        boolean actualEncontrado = false;
        Nodo n = inicio;
        Nodo ultimo = null;
        for (int i = 0; i < tamanio; i++) {
            if (n == null || n.siguiente == null || n.anterior == null) {
                return false;
            }
            if (n.siguiente.anterior != n || n.anterior.siguiente != n) {
                return false;
            }
            if (n == actual) {
                actualEncontrado = true;
            }
            ultimo = n;
            n = n.siguiente;
        }
        // Tras tamanio pasos se debe regresar exactamente al inicio y haber terminado en fin.
        return n == inicio && ultimo == fin && actualEncontrado;
    }
}
