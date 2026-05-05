package pilalista;

import Actividad1.ExceptionIsEmpty;

public class GestorPilaEnlazada<E> {
    private final StackLink<E> pila;

    public GestorPilaEnlazada() {
        this.pila = new StackLink<>();
    }

    public void agregar(E elemento) {
        pila.push(elemento);
    }

    public E eliminar() throws ExceptionIsEmpty {
        return pila.pop();
    }

    public E verTope() throws ExceptionIsEmpty {
        return pila.top();
    }

    public boolean estaVacia() {
        return pila.isEmpty();
    }

    public void agregarTodos(E[] elementos) {
        for (E elemento : elementos) {
            agregar(elemento);
        }
    }

    public void imprimirPila() {
        System.out.println(pila.toString());
    }
}
