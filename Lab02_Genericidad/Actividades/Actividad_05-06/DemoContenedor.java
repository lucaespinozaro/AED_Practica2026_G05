public class DemoContenedor {
    public static <T extends Comparable<T>> boolean igualArrays(T[] x, T[] y) {
        if (x == null || y == null) return false;
        
        if (x.length != y.length) {
            return false;
        }

        for (int i = 0; i < x.length; i++) {
            if (!x[i].equals(y[i])) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        Integer[] nums = {1, 2, 3, 4, 5};
        Integer[] nums2 = {1, 2, 3, 4, 5};
        Integer[] nums3 = {1, 2, 7, 4, 5};
        Integer[] nums4 = {1, 2, 3, 4};

        if (igualArrays(nums, nums2))
            System.out.println("nums es igual a nums2");

        if (igualArrays(nums, nums3))
            System.out.println("nums es igual a nums3");

        if (igualArrays(nums, nums4))
            System.out.println("nums es igual a nums4");

        Double[] dvals = {1.1, 2.2, 3.3, 4.4, 5.5};

        if (igualArrays(nums, dvals))
            System.out.println("nums es igual a dvals");

        //Crea un array de double                           //A
        //Double dvals[]={1.1,2.2,3.3,4.4,5.5};             //B
        //if(igualArrays(num,dvals))                        //C
        //  System.out.println("nums es igual a dvals");    //D
    }
}
