public class Ejercicio01 {
    public static void main(String[] args) {
        int[] table = new int[11];
        for (int i = 0; i < table.length; i++) {
            table[i] = -1;
        }

        int[] values = {3, 14, 25, 36, 47, 58};

        for (int val : values) {
            int index = val % 11;
            table[index] = val;
        }

        for (int i = 0; i < table.length; i++) {
            if (table[i] == -1) {
                System.out.println(i + ": vacio");
            } else {
                System.out.println(i + ": " + table[i]);
            }
        }
    }
}
