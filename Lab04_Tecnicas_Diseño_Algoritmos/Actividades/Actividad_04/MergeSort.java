import java.util.*;

public class MergeSort {
    static int comp = 0;

    static void imprimirSub(int[] v, int ini, int fin) {
        System.out.print("[");
        for (int i = ini; i <= fin; i++) {
            System.out.print(v[i]);
            if (i < fin) System.out.print(", ");
        }
        System.out.print("]");
    }

    static void dividir(int[] v, int[] aux, int ini, int fin, int n) {
        if (v == null || aux == null)
            throw new IllegalArgumentException("Arreglos no pueden ser null");

        if (v.length != aux.length)
            throw new IllegalArgumentException("Los arreglos deben tener el mismo tamaño");

        if (ini < 0 || fin >= v.length || ini > fin)
            throw new IllegalArgumentException("Índices fuera de rango");

        if (ini < fin) {
            int c = (ini + fin) / 2;

            for (int i = 0; i < n; i++) System.out.print("   ");
            System.out.print("Dividiendo: ");
            imprimirSub(v, ini, fin);
            System.out.println();

            dividir(v, aux, ini, c, n + 1);
            dividir(v, aux, c + 1, fin, n + 1);

            juntar(v, aux, ini, c, fin, n);
        }
    }

    static void juntar(int[] v, int[] aux, int ini, int c, int fin, int n) {
        if (v.length != aux.length)
            throw new IllegalArgumentException("Los arreglos deben tener el mismo tamaño");

        if (ini < 0 || fin >= v.length || ini > fin || c < ini || c >= fin)
            throw new IllegalArgumentException("Índices inválidos");

        for (int i = 0; i < n; i++) System.out.print("   ");
        System.out.print("Mezclando: ");
        imprimirSub(v, ini, c);
        System.out.print(" + ");
        imprimirSub(v, c + 1, fin);
        System.out.println();

        for (int i = ini; i <= fin; i++) aux[i] = v[i];

        int i = ini, j = c + 1, k = ini;

        while (i <= c && j <= fin) {
            comp++;
            v[k++] = (aux[i] <= aux[j]) ? aux[i++] : aux[j++];
        }

        while (i <= c) v[k++] = aux[i++];
        while (j <= fin) v[k++] = aux[j++];
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n;
        while (true) {
            System.out.print("Cantidad: ");
            if (sc.hasNextInt()) {
                n = sc.nextInt();
                if (n > 0) break;
            } else {
                sc.next();
            }
            System.out.println("Ingrese un entero mayor a 0.");
        }

        int[] v = new int[n];

        System.out.println("Ingrese los elementos:");
        for (int i = 0; i < n; i++) {
            while (!sc.hasNextInt()) {
                System.out.println("Ingrese un número válido:");
                sc.next();
            }
            v[i] = sc.nextInt();
        }

        sc.close();

        int[] aux = new int[n];

        System.out.println("\nAntes: " + Arrays.toString(v));

        dividir(v, aux, 0, n - 1, 0);

        System.out.println("\nDespués: " + Arrays.toString(v));
        System.out.println("Comparaciones: " + comp);
    }
}
