package Actividad_02;
import java.util.*;

public class Permutaciones {

    public static void permutar(int[] arr, List<Integer> actual, boolean[] usado) {
        if (actual.size() == arr.length) {
            System.out.println(actual);
            return;
        }

        for (int i = 0; i < arr.length; i++) {
            if (!usado[i]) {
                usado[i] = true;
                actual.add(arr[i]);

                permutar(arr, actual, usado);

                actual.remove(actual.size() - 1);
                usado[i] = false;
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3};
        boolean[] usado = new boolean[arr.length];
        System.out.println(" Permutaciones de {1, 2, 3} ");
        permutar(arr, new ArrayList<>(), usado);
    }
}
