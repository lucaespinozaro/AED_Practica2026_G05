public class Main {
    public static void main(String[] args) {
        ListLinked<Integer> lista1 = new ListLinked<>();
        lista1.insertLast(3);
        lista1.insertLast(1);
        lista1.insertLast(5);

        ListLinked<Integer> lista2 = new ListLinked<>();
        lista2.insertLast(3);
        lista2.insertLast(1);
        lista2.insertLast(5);

        System.out.println("Lista1: " + lista1.printList());
        System.out.println("Lista2: " + lista2.printList());

        System.out.println("Buscar 1 en lista1: " + OperacionesLista.buscarElemento(lista1, 1));

        ListLinked<Integer> invertida = OperacionesLista.invertirLista(lista1);
        System.out.println("Invertida: " + invertida.printList());

        Node<Integer> head = lista1.getFirstNode();
        head = OperacionesLista.insertarAlFinal(head, 10);

        System.out.print("Insertar al final: ");
        Node<Integer> aux = head;
        while (aux != null) {
            System.out.print(aux.dato + " -> ");
            aux = aux.next;
        }
        System.out.println();

        System.out.println("Contar nodos: " + OperacionesLista.contarNodos(head));

        System.out.println("Son iguales: " + OperacionesLista.sonIguales(lista1, lista2));

        ListLinked<Integer> concat = OperacionesLista.concatenarListas(lista1, lista2);
        System.out.println("Concatenadas: " + concat.printList());

        SortedListLinked<Integer> ordenada = new SortedListLinked<>();
        ordenada.insertOrden(5);
        ordenada.insertOrden(2);
        ordenada.insertOrden(8);
        ordenada.insertOrden(1);

        System.out.println("Ordenada: " + ordenada.printList());
    }
}
