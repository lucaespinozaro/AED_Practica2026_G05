public class Ejercicio03 {
    public static void main(String[] args) {
        HashO<String> hashO = new HashO<>(7);

        hashO.insert(new Register<>(10, "Juan"));
        hashO.insert(new Register<>(17, "Ana"));
        hashO.insert(new Register<>(24, "Luis"));
        hashO.insert(new Register<>(31, "Rosa"));
        hashO.insert(new Register<>(5, "Pedro"));
        hashO.insert(new Register<>(12, "Carla"));

        System.out.println("============= Estado Final de la Tabla (con Colisiones) =============");
        hashO.printTable();

        System.out.println("============= 1. Busqueda y Localizacion de la Clave 24 =============");
        Register<String> persona = hashO.search(24);
        int[] coords = hashO.locate(24);
        
        if (persona != null && coords != null) {
            System.out.println("Nombre asociado: " + persona.getValue());
            System.out.println("Ubicacion en la tabla: Indice " + coords[0]);
            System.out.println("Ubicacion en la cadena: Nodo " + coords[1] + " (0-based)");
        }

        System.out.println("============= 2. Eliminacion de la Clave 17 =============");
        hashO.delete(17);
        hashO.printTable();
        System.out.println("Nodos remanentes en la cadena del indice 3: " + hashO.chainSize(3));
    }
}
