public class ColaArreglo<E> {
    private int inicio;
    private int fin;
    private int tamanio;
    private E[] array;
    private int cantidad;

    @SuppressWarnings("unchecked")
    public ColaArreglo(int tamanio) {
        this.tamanio = tamanio;
        this.array = (E[]) new Object[tamanio];
        this.inicio = 0;
        this.fin = -1;
        this.cantidad = 0;
    }

    public boolean isFull() {
        return cantidad == tamanio;
    }

    public boolean isEmpty() {
        return cantidad == 0;
    }

    public void enqueue(E d) {
        if (isFull()) {
            throw new RuntimeException("Cola llena");
        }
        fin = (fin + 1) % tamanio;
        array[fin] = d;
        cantidad++;
    }

    public E dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Cola vacia");
        }
        E d = array[inicio];
        array[inicio] = null;
        inicio = (inicio + 1) % tamanio;
        cantidad--;
        return d;
    }

    public E peek() {
        if (isEmpty()) {
            throw new RuntimeException("Cola vacia");
        }
        return array[inicio];
    }
}
