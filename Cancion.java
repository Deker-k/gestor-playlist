
public class Cancion {
    private final int id;
    private final String titulo;
    private final String artista;
    private final int duracionSegundos;


    public Cancion(int id, String titulo, String artista, int duracionSegundos) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }
        if (artista == null || artista.trim().isEmpty()) {
            throw new IllegalArgumentException("El artista no puede estar vacío.");
        }
        if (duracionSegundos <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor que cero.");
        }
        this.id = id;
        this.titulo = titulo.trim();
        this.artista = artista.trim();
        this.duracionSegundos = duracionSegundos;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getArtista() {
        return artista;
    }

    public int getDuracionSegundos() {
        return duracionSegundos;
    }

    @Override
    public String toString() {
        int min = duracionSegundos / 60;
        int seg = duracionSegundos % 60;
        return String.format("[ID %d] %s - %s (%d:%02d, %d s)",
                id, titulo, artista, min, seg, duracionSegundos);
    }
}
