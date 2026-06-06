import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Biblioteca {
    private BTree<Libro> arbolLibros;
    private int totalLibros;

    public Biblioteca() {
        this.arbolLibros = null;
        this.totalLibros = 0;
    }

    public void agregarLibro(Libro libro) {
        if (arbolLibros == null) {
            System.out.println("Debe inicializar o cargar la biblioteca primero para establecer el orden del árbol.");
            return;
        }
        if (arbolLibros.search(libro)) {
            System.out.println("Error: El libro con ISBN " + libro.getIsbn() + " ya existe.");
            return;
        }
        arbolLibros.insert(libro);
        totalLibros++;
        System.out.println("Libro agregado exitosamente.");
    }

    public void eliminarLibro(String isbn) {
        if (arbolLibros == null || arbolLibros.isEmpty()) {
            System.out.println("La biblioteca está vacía.");
            return;
        }
        Libro fiktivo = new Libro(isbn, "", "", 0);
        if (!arbolLibros.search(fiktivo)) {
            System.out.println("El libro con ISBN " + isbn + " no se encuentra en la biblioteca.");
            return;
        }
        arbolLibros.delete(fiktivo);
        totalLibros--;
        System.out.println("Libro eliminado exitosamente.");
    }

    public void cargarDesdeArchivo(String nombreArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String primeraLinea = br.readLine();
            if (primeraLinea == null) {
                System.out.println("El archivo está vacío.");
                return;
            }
            int orden = Integer.parseInt(primeraLinea.trim());
            this.arbolLibros = new BTree<>(orden);
            this.totalLibros = 0;

            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] partes = linea.split(",");
                if (partes.length == 4) {
                    String isbn = partes[0].trim();
                    String titulo = partes[1].trim();
                    String autor = partes[2].trim();
                    int anio = Integer.parseInt(partes[3].trim());

                    Libro nuevoLibro = new Libro(isbn, titulo, autor, anio);
                    if (arbolLibros.search(nuevoLibro)) {
                        System.out.println("Aviso: Registro omitido. ISBN duplicado encontrado en archivo: " + isbn);
                    } else {
                        arbolLibros.insert(nuevoLibro);
                        totalLibros++;
                    }
                }
            }
            System.out.println("Información cargada de manera automática desde el archivo.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error al procesar el archivo: " + e.getMessage());
        }
    }

    public void buscarLibroConCamino(String isbn) {
        if (arbolLibros == null || arbolLibros.isEmpty()) {
            System.out.println("La biblioteca está vacía.");
            return;
        }
        System.out.println("Camino recorrido durante la búsqueda:");
        Libro objetivo = new Libro(isbn, "", "", 0);
        boolean encontrado = buscarCaminoRecursivo(arbolLibros.getRoot(), objetivo);
        if (!encontrado) {
            System.out.println("Resultado: El libro con ISBN " + isbn + " no existe.");
        }
    }

    private boolean buscarCaminoRecursivo(BNode<Libro> current, Libro objetivo) {
        if (current == null) return false;
        
        System.out.println(" -> Visitando Nodo " + current.getIdNode() + " " + current.toString());
        int[] pos = new int[1];
        boolean encontrado = current.searchNode(objetivo, pos);
        
        if (encontrado) {
            System.out.println("\n¡Libro encontrado con éxito!");
            System.out.println(current.keys.get(pos[0]));
            return true;
        }
        if (current.childs.get(pos[0]) == null) {
            return false;
        }
        return buscarCaminoRecursivo(current.childs.get(pos[0]), objetivo);
    }

    public void mostrarLibrosOrdenados() {
        if (arbolLibros == null || arbolLibros.isEmpty()) {
            System.out.println("No hay libros registrados.");
            return;
        }
        mostrarInOrder(arbolLibros.getRoot());
    }

    private void mostrarInOrder(BNode<Libro> current) {
        if (current == null) return;
        int i;
        for (i = 0; i < current.count; i++) {
            mostrarInOrder(current.childs.get(i));
            System.out.println(current.keys.get(i));
        }
        mostrarInOrder(current.childs.get(i));
    }

    public void mostrarEstructuraArbol() {
        if (arbolLibros == null) {
            System.out.println("BTree is empty...");
        } else {
            System.out.print(arbolLibros.toString());
        }
    }

    public int obtenerAltura() {
        if (arbolLibros == null || arbolLibros.isEmpty()) return 0;
        int altura = 0;
        BNode<Libro> aux = arbolLibros.getRoot();
        while (aux != null) {
            altura++;
            aux = aux.childs.get(0);
        }
        return altura;
    }

    public int getCantidadTotalLibros() {
        return this.totalLibros;
    }
}
