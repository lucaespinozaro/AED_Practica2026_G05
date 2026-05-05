public class Main {
    public static void main(String[] args) {
        ColaArreglo<Integer> cola = new ColaArreglo<>(5);
        try {
            cola.enqueue(101);
            cola.enqueue(102);
            cola.enqueue(103);
            cola.enqueue(104);
            cola.enqueue(105);
            cola.enqueue(106);
        } catch (RuntimeException e) {
            System.out.println("Cola llena");
        }

        System.out.println("Atendiendo cliente: " + cola.dequeue());
        System.out.println("Atendiendo cliente: " + cola.dequeue());

        System.out.println("Cliente en frente: " + cola.peek());

        cola.enqueue(106);
        cola.enqueue(107);

        while (!cola.isEmpty()) {
            System.out.println("Atendiendo cliente: " + cola.dequeue());
        }

        try {
            cola.dequeue();
        } catch (RuntimeException e) {
            System.out.println("Cola vacía");
        }
    }
}
