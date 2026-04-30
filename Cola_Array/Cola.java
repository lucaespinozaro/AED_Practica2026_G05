public class Cola<E> {
    private E[] array;
    private int inicio;
    private int fin;
    private int capacidad;
    private int cantidad;

    public Cola(int capacidad) {
        this.capacidad = capacidad;
        this.array = (E[]) new Object[capacidad];
        this.inicio = 0;
        this.fin = -1;
        this.cantidad = 0;
    }

    public void enqueue(E x) {
        if (isFull()) {
            throw new RuntimeException("La cola está llena");
        }
        fin = (fin + 1) % capacidad;
        array[fin] = x;
        cantidad++;
    }

    public E dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("La cola está vacía");
        }
        E dato = array[inicio];
        inicio = (inicio + 1) % capacidad;
        cantidad--;
        return dato;
    }

    public E front() {
        if (isEmpty()) {
            throw new RuntimeException("La cola está vacía");
        }
        return array[inicio];
    }

    public boolean isEmpty() {
        return cantidad == 0;
    }

    public boolean isFull() {
        return cantidad == capacidad;
    }
}