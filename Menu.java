import java.io.PrintStream;
import java.util.Scanner;


public class Menu {
    private final ListaReproduccion lista;
    private final Scanner entrada;
    private final PrintStream salida;

    public Menu(Scanner entrada, PrintStream salida) {
        this.lista = new ListaReproduccion();
        this.entrada = entrada;
        this.salida = salida;
    }

    public static void main(String[] args) {
        new Menu(new Scanner(System.in), System.out).ejecutar();
    }

    /** Ciclo principal. Termina con la opción 0 o al agotarse la entrada. */
    public void ejecutar() {
        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            String linea = leerLinea("Elige una opción: ");
            if (linea == null) { // fin de la entrada
                break;
            }
            Integer opcion = parsearEntero(linea);
            if (opcion == null) {
                salida.println("Entrada inválida: escribe un número del 1 al 13.");
                continue;
            }
            continuar = procesar(opcion);
        }
        salida.println("Hasta pronto.");
    }

    private void mostrarMenu() {
        salida.println();
        salida.println("--- MENU DE LISTA DE REPRODUCCIÓN ---");
        salida.println("1. Agregar canción al inicio");
        salida.println("2. Agregar canción al final");
        salida.println("3. Buscar canción por ID");
        salida.println("4. Seleccionar canción por ID (hacerla actual)");
        salida.println("5. Eliminar canción por ID");
        salida.println("6. Eliminar la canción actual");
        salida.println("7. Consultar canción actual");
        salida.println("8. Avanzar a la siguiente canción");
        salida.println("9. Retroceder a la canción anterior");
        salida.println("10. Mostrar lista en ambos sentidos");
        salida.println("11. Consultar cantidad de canciones");
        salida.println("12. Simular reproducción de k canciones");
        salida.println("13. Salir");
    }

    /** @return false si se debe salir del programa */
    private boolean procesar(int opcion) {
        switch (opcion) {
            case 1:
                agregar(true);
                break;
            case 2:
                agregar(false);
                break;
            case 3:
                buscar();
                break;
            case 4:
                seleccionar();
                break;
            case 5:
                eliminarPorId();
                break;
            case 6:
                eliminarActual();
                break;
            case 7:
                consultarActual();
                break;
            case 8:
                navegar(true);
                break;
            case 9:
                navegar(false);
                break;
            case 10:
                mostrarLista();
                break;
            case 11:
                salida.println("Cantidad de canciones: " + lista.getTamanio());
                break;
            case 12:
                reproducir();
                break;
            case 13:
                return false;
            default:
                salida.println("Opción inválida: elige un número del 1 al 13.");
        }
        return true;
    }

    // Funciones

    //Agregar
    private void agregar(boolean alInicio) {
        String titulo = leerLinea("Título: ");
        String artista = leerLinea("Artista: ");
        Integer duracion = leerEnteroConMensaje("Duración en segundos: ");
        if (titulo == null || artista == null || duracion == null) {
            return;
        }
        try {
            Cancion c = alInicio
                    ? lista.agregarAlInicio(titulo, artista, duracion)
                    : lista.agregarAlFinal(titulo, artista, duracion);
            salida.println("Canción agregada " + (alInicio ? "al inicio" : "al final") + ": " + c);
        } catch (IllegalArgumentException e) {
            salida.println("No se agregó la canción: " + e.getMessage());
        }
    }

    //Buscar
    private void buscar() {
        if (avisarSiVacia()) {
            return;
        }
        Integer id = leerEnteroConMensaje("ID a buscar: ");
        if (id == null) {
            return;
        }
        Cancion c = lista.buscarPorId(id);
        salida.println(c == null ? "No existe una canción con ID " + id + "." : "Encontrada: " + c);
    }

    //Seleccionar
    private void seleccionar() {
        if (avisarSiVacia()) {
            return;
        }
        Integer id = leerEnteroConMensaje("ID a seleccionar: ");
        if (id == null) {
            return;
        }
        if (lista.seleccionarPorId(id)) {
            salida.println("Canción actual: " + lista.getActual());
        } else {
            salida.println("No existe una canción con ID " + id + ". La lista no cambió.");
        }
    }

    //Eliminar por id
    private void eliminarPorId() {
        if (avisarSiVacia()) {
            return;
        }
        Integer id = leerEnteroConMensaje("ID a eliminar: ");
        if (id == null) {
            return;
        }
        Cancion c = lista.eliminarPorId(id);
        if (c == null) {
            salida.println("No existe una canción con ID " + id + ". La lista no cambió.");
        } else {
            salida.println("Eliminada: " + c);
            informarActual();
        }
    }

    //Eliminar cancion actual
    private void eliminarActual() {
        Cancion c = lista.eliminarActual();
        if (c == null) {
            salida.println("La lista está vacía: no hay canción actual que eliminar.");
        } else {
            salida.println("Eliminada la canción actual: " + c);
            informarActual();
        }
    }

    //Informacion sobre las canciones

    private void informarActual() {
        Cancion c = lista.getActual();
        salida.println(c == null ? "La lista quedó vacía." : "Canción actual: " + c);
    }

    /** @return true (y avisa) si la lista está vacía */
    private boolean avisarSiVacia() {
        if (lista.estaVacia()) {
            salida.println("La lista está vacía.");
            return true;
        }
        return false;
    }


    // Consultar canción actual
    private void consultarActual() {
        Cancion c = lista.getActual();
        salida.println(c == null ? "La lista está vacía: no hay canción actual."
                : "Canción actual: " + c);
    }

    // Navegación circular
    private void navegar(boolean adelante) {
        if (lista.estaVacia()) {
            salida.println("La lista está vacía: no se puede navegar.");
            return;
        }
        Cancion c = adelante ? lista.avanzar() : lista.retroceder();
        salida.println((adelante ? "Siguiente canción: " : "Canción anterior: ") + c);
    }

    // Mostrar todas las canciones en ambos sentidos
    private void mostrarLista() {
        if (lista.estaVacia()) {
            salida.println("La lista está vacía.");
            return;
        }
        salida.println("Canciones hacia adelante (inicio a fin):");
        salida.println(lista.cancionesAdelante());
        salida.println("Canciones hacia atrás (fin a inicio):");
        salida.println(lista.cancionesAtras());
    }

    // Reproducción simulada: muestra k canciones y deja actual en la siguiente.
    private void reproducir() {
        if (lista.estaVacia()) {
            salida.println("La lista está vacía: no hay canciones para reproducir.");
            return;
        }
        Integer k = leerEnteroConMensaje("¿Cuántas canciones deseas reproducir? ");
        if (k == null) {
            return;
        }
        if (k <= 0) {
            salida.println("La cantidad debe ser un entero positivo.");
            return;
        }
        for (int i = 1; i <= k; i++) {
            salida.println("Reproducción " + i + "/" + k + ": " + lista.getActual());
            lista.avanzar();
        }
        salida.println("Siguiente canción a reproducir: " + lista.getActual());
    }

    private String leerLinea(String mensaje) {
        salida.print(mensaje);
        salida.flush();
        if (!entrada.hasNextLine()) {
            return null;
        }
        return entrada.nextLine();
    }

    /** Lee un entero; si no es válido, avisa y devuelve null. */
    private Integer leerEnteroConMensaje(String mensaje) {
        String linea = leerLinea(mensaje);
        if (linea == null) {
            return null;
        }
        Integer valor = parsearEntero(linea);
        if (valor == null) {
            salida.println("Entrada inválida: se esperaba un número entero.");
        }
        return valor;
    }

    static Integer parsearEntero(String texto) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
