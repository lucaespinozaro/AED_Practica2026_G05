public class Main {
    public static void main(String[] args) {
        Cola<Integer> cola = new Cola<>(5); // Capacidad de 5

        // Agregar elementos a la cola
        cola.enqueue(10);
        cola.enqueue(20);
        cola.enqueue(30);

        System.out.println("Primer elemento: " + cola.front()); // 10

        // Quitar elementos
        System.out.println("Elemento removido: " + cola.dequeue()); // 10
        System.out.println("Primer elemento: " + cola.front()); // 20

        cola.dequeue(); // 20
        cola.dequeue(); // 30

        System.out.println("¿La cola está vacía? " + cola.isEmpty()); // true
    }
}