public class Limits {
    int[] array;
    int first;
    int last;
 
    public Limits(int[] array, int first, int last) {
        if (array == null)
            throw new IllegalArgumentException("Array no puede ser null");
        if (first < 0 || last >= array.length)
            throw new IllegalArgumentException("Índices fuera de rango");

        this.array = array;
        this.first = first;
        this.last  = last;
    }
 
    public int length() {
        return (last >= first) ? last - first + 1 : 0;
    }
}
