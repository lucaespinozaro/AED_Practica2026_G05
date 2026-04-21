public class Moda3Main {
    public static void main(String[] args) {
        int[] array = {3, 1, 4, 4, 2, 4, 1, 2, 4, 3};
        int moda = moda3(array, 0, array.length - 1);
        System.out.println("La moda es: " + moda); // Esperado: 4
    }
}
