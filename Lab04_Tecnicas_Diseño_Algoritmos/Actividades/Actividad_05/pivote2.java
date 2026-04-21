private static void pivote2(int[] array, int mediana,
                             int first, int last,
                             int[] izq, int[] der) {
    int lo  = first;   
    int mid = first;   
    int hi  = last;    
 
    while (mid <= hi) {
        if (array[mid] < mediana) {
            swap(array, lo, mid);
            lo++;
            mid++;
        } else if (array[mid] == mediana) {
            mid++;
        } else {
            swap(array, mid, hi);
            hi--;
        }
    }
 
    izq[0] = lo;   // p2 empieza aquí  (iguales)
    der[0] = mid;  // p3 empieza aquí  (mayores)
}
 
private static void swap(int[] array, int i, int j) {
    int tmp   = array[i];
    array[i]  = array[j];
    array[j]  = tmp;
}
