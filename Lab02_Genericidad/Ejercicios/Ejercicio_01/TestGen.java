public class TestGen {
    public static <T> boolean exist(T[] arreglo, T elemento) {
        for (T item : arreglo) {
            if (item.equals(elemento)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String[] v = {"Perez", "Sanchez", "Rodriguez"};
        Integer[] w = {12, 34, 56};

        System.out.println(exist(v, "Sanchez"));
        System.out.println(exist(w, 34));

        Golosina[] g = {new Golosina("Gomitas", 40), new Golosina("Caramelo", 15)};
        Chocolatina[] c = {new Chocolatina("Sublime"), new Chocolatina("Ferrero")};

        System.out.println(exist(g, new Golosina("Gomitas", 40)));
        System.out.println(exist(c, new Chocolatina("Sublime")));
    }
}
