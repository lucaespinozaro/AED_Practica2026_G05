public class Main {
    public static void main(String[] args) {
        ColaReproduccion<Cancion> cola = new ColaReproduccion<>();

        cola.agregarCancion(new Cancion("Bohemian Rhapsody", "Queen", 354));
        cola.agregarCancion(new Cancion("Blinding Lights", "The Weeknd", 200));
        cola.agregarCancion(new Cancion("Shape of You", "Ed Sheeran", 234));
        cola.agregarCancion(new Cancion("Smells Like Teen Spirit", "Nirvana", 301));
        cola.agregarCancion(new Cancion("Rolling in the Deep", "Adele", 228));
        cola.agregarCancion(new Cancion("Hotel California", "Eagles", 391));

        System.out.println("=== Cola de Reproducción Inicial ===");
        cola.mostrarCola();

        System.out.println("\nAvanzando 3 canciones...");
        for (int i = 0; i < 3; i++) {
            Cancion c = cola.reproducirSiguiente();
            if (c != null)
                System.out.println("► " + c);
        }

        System.out.println("\nRetrocediendo 1 canción...");
        Cancion anterior = cola.reproducirAnterior();
        if (anterior != null)
            System.out.println("◄ " + anterior);

        System.out.println("\n=== Mezclando... ===");
        cola.mezclar();
        cola.mostrarCola();

        int total = cola.duracionTotal();
        int min = total / 60;
        int seg = total % 60;

        System.out.printf("\nDuración total: %d:%02d\n", min, seg);
    }
}
