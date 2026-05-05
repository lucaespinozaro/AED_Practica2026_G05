package pilalista;

import Actividad1.ExceptionIsEmpty;

public class MainStackLink {
    public static void main(String[] args) {
        GestorPilaEnlazada<Integer> gestor = new GestorPilaEnlazada<>();
        Integer[] valores = {10, 20, 30, 40, 50};

        System.out.println("Añadiendo elementos a la pila:");
        gestor.agregarTodos(valores);
        gestor.imprimirPila();

        try {
            System.out.println("Elemento en el tope: " + gestor.verTope());
            System.out.println("Eliminando todos los elementos:");
            while (!gestor.estaVacia()) {
                System.out.println("eliminar -> " + gestor.eliminar());
                gestor.imprimirPila();
            }
            System.out.println("¿La pila está vacía? " + gestor.estaVacia());
        } catch (ExceptionIsEmpty e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
