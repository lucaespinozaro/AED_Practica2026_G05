public class Limits {
    int[] array;
    int first;
    int last;
 
    public Limits(int[] array, int first, int last) {
        this.array = array;
        this.first = first;
        this.last  = last;
    }
 
    // Devuelve cuántos elementos tiene este subarreglo
    public int length() {
        return (last >= first) ? last - first + 1 : 0;
    }
}
