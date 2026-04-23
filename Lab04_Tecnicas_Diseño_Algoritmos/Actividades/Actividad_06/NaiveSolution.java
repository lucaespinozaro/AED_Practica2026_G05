public class NaiveSolution {
    static int getValue(int[] values, int length) {
        if (values == null)
            throw new IllegalArgumentException("El arreglo no puede ser null");

        if (length < 0 || length > values.length)
            throw new IllegalArgumentException("Longitud inválida");

        if (length == 0)
            return 0;
 
        int tmpMax = Integer.MIN_VALUE;
        for (int i = 0; i < length; i++) {
            // Probamos cortar un trozo de tamaño (i+1) y resolver el resto
            tmpMax = Math.max(tmpMax,
                              values[i] + getValue(values, length - i - 1));
        }
        return tmpMax;
    }
 
    public static void main(String[] args) {
        int[] values = {3, 7, 1, 3, 9};
        int rodLength = values.length;
        System.out.println("El valor maximo: " + getValue(values, rodLength));
        // Salida: El valor maximo: 17
    }
}
