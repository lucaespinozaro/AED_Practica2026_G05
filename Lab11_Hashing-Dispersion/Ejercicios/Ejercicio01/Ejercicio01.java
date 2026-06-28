public class Ejercicio01 {
    public static void main(String[] args) {
        HashC<Integer> table = new HashC<>(11);

        int[] values = {3, 14, 25, 36, 47, 58};

        for (int val : values) {
            table.insert(new Register<>(val, val));
        }

        table.printTable();
    }
}
