public class DpSolution {
    static int getValue(int[] values, int rodLength) {
        // subSolutions[i] guarda el máximo valor para una varilla de largo i
        int[] subSolutions = new int[rodLength + 1];
        subSolutions[0] = 0; // varilla de largo 0 vale nada
 
        for (int i = 1; i <= rodLength; i++) {
            int tmpMax = -1;
            for (int j = 0; j < i; j++) {
                tmpMax = Math.max(tmpMax,
                                  values[j] + subSolutions[i - j - 1]);
            }
            subSolutions[i] = tmpMax; 
        }
        return subSolutions[rodLength];
    }
 
    public static void main(String[] args) {
        int[] values  = {3, 7, 1, 3, 9};
        int rodLength = values.length;
        System.out.println("El valor maximo: " + getValue(values, rodLength));
    }
}
