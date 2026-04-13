package Actividades;
import java.util.*;

public class Subconjuntos {

    public static void generarSubconjuntos(int[] arr, List<Integer> actual, int i) {
        if (i == arr.length) {
            System.out.println(actual);
            return;
        }

        actual.add(arr[i]);
        generarSubconjuntos(arr, actual, i + 1);

        actual.remove(actual.size() - 1);

        generarSubconjuntos(arr, actual, i + 1);
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3};
        System.out.println(" Subconjuntos de {1, 2, 3} ");
        generarSubconjuntos(arr, new ArrayList<>(), 0);
    }
}
