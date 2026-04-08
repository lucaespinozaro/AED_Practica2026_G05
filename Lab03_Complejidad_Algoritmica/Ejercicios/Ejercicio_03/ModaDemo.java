import java.util.HashMap;
import java.util.Map;
public class ModaDemo {
    public static int moda(int[] v) {
        Map<Integer, Integer> frecuencia = new HashMap<>();     // O(1)
        int maxFrecuencia = 0;                                  // O(1)
        int moda = v[0];                                        // O(1)

        for (int num : v) {                                     // O(n)
            int f = frecuencia.getOrDefault(num, 0) + 1;            // O(1)
            frecuencia.put(num, f);                                 // O(1)

            if (f > maxFrecuencia) {                                // O(1)
                maxFrecuencia = f;                                      // O(1)
                moda = num;                                             // O(1)
            }
        }

        return moda;                                            // O(1)
    }
}
