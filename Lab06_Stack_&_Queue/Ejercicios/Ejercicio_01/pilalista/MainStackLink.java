package actividadPilaLista;

import actividad1.ExceptionIsEmpty;

public class MainStackLink {
    public static void main(String[] args) {
        Stack<Integer> pila = new StackLink<>();
        Integer[] valores = {10, 20, 30, 40, 50};

        for (Integer v : valores) {
            pila.push(v);
        }

        System.out.println(pila.toString());

        try {
            System.out.println(pila.top());
            while (!pila.isEmpty()) {
                System.out.println(pila.pop());
                System.out.println(pila.toString());
            }
            System.out.println(pila.isEmpty());
        } catch (ExceptionIsEmpty e) {
            System.out.println(e.getMessage());
        }
    }
}
