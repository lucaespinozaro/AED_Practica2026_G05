package Actividades;

import java.util.*;

public class SubconjuntoSuma {

    public static boolean esBloqueadoPorPar(int[] arr, int i) {
        if (i > 0 && arr[i] % 2 == 0 && arr[i - 1] % 2 == 0) {
            return true;
        }
        return false;
    }

    public static boolean resolver(int[] arr, int i, int sumaActual, int objetivo) {
        if (i == arr.length) {
            return sumaActual == objetivo;
        }

        int elemento = arr[i];
        boolean esMultiploDe3 = (elemento % 3 == 0);
        boolean estaBloqueado = esBloqueadoPorPar(arr, i);

        if (esMultiploDe3) {
            return resolver(arr, i + 1, sumaActual + elemento, objetivo);
        }

        if (estaBloqueado) {
            return resolver(arr, i + 1, sumaActual, objetivo);
        }

        boolean incluyendo = resolver(arr, i + 1, sumaActual + elemento, objetivo);
        boolean sinIncluir = resolver(arr, i + 1, sumaActual, objetivo);

        return incluyendo || sinIncluir;
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

        System.out.print("Objetivo: ");
        int objetivo = sc.nextInt();

        boolean resultado = resolver(arr, 0, 0, objetivo);
        System.out.println("Resultado: " + resultado);

        sc.close();
    }
}