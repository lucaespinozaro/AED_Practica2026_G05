package Ejercicio_02;
import java.util.*;

public class KesimoMasGrande {
    public static int particionar(int[] arr, int inicio, int fin) {
        int pivote = arr[fin];
        int i = inicio;

        for (int j = inicio; j < fin; j++) {
            if (arr[j] >= pivote) {
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                i++;
            }
        }

        int temp = arr[i];
        arr[i] = arr[fin];
        arr[fin] = temp;

        return i; 
    }

    public static int quickSelect(int[] arr, int inicio, int fin, int k) {
        if (arr == null)
            throw new IllegalArgumentException("El arreglo no puede ser null");

        if (inicio < 0 || fin >= arr.length || inicio > fin)
            throw new IllegalArgumentException("Índices inválidos");

        if (k < 1 || k > (fin - inicio + 1))
            throw new IllegalArgumentException("k fuera de rango");

        if (inicio == fin) {
            return arr[inicio];
        }

        int posPivote = particionar(arr, inicio, fin);
        int posicion = posPivote - inicio + 1;

        if (posicion == k) {
            return arr[posPivote];
        } else if (k < posicion) {
            return quickSelect(arr, inicio, posPivote - 1, k);
        } else {
            return quickSelect(arr, posPivote + 1, fin, k - posicion);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Cantidad de elementos: ");
        int n = sc.nextInt();

        int[] arr = new int[n];
        System.out.print("Elementos: ");
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }

        System.out.print("Valor de k: ");
        int k = sc.nextInt();

        if (k < 1 || k > n) {
            System.out.println("Error: k debe estar entre 1 y " + n);
        } else {
            int resultado = quickSelect(arr, 0, n - 1, k);
            System.out.println("El " + k + "-esimo elemento mas grande es: " + resultado);
        }

        sc.close();
    }
}
