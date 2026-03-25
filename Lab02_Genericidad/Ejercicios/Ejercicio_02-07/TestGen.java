public class TestGen {
    public static <T extends Comparable<T>> boolean exist(T[] arreglo, T elemento) {
        for (T item : arreglo) {
            if (item == null || elemento == null) {
                if (item == elemento) {
                    return true;
                }
            } else if (item.equals(elemento)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Cajoneria<Golosina> cajoneria = new Cajoneria<>(6);
        Caja<Golosina> c1 = new Caja<>("Rojo", new Golosina("Gomitas", 40));
        Caja<Golosina> c2 = new Caja<>("Amarillo", new Golosina("Caramelo", 10));
        Caja<Golosina> c3 = new Caja<>("Verde", new Golosina("Chocolate", 20));
        Caja<Golosina> c4 = new Caja<>("Azul", new Golosina("Gomitas", 40));
        Caja<Golosina> c5 = new Caja<>("Blanco", new Golosina("Caramelo", 10));

        cajoneria.add(c1);
        cajoneria.add(c2);
        cajoneria.add(c3);
        cajoneria.add(c4);
        cajoneria.add(c5);

        Rastreo<Golosina> b = cajoneria.search(new Golosina("Gomitas", 40));
        if (b != null) {
            System.out.println(b.getPosicion() + " - " + b.getColor() + " - " + b.getObjeto());
        }

        System.out.println("Eliminado: " + cajoneria.delete(new Golosina("Caramelo", 10)));
        System.out.println("Cantidad de Gomitas: " + cajoneria.contar(new Golosina("Gomitas", 40)));

        System.out.println("--- Cajoneria Golosinas ---");
        System.out.println(cajoneria);

        Cajoneria<Chocolatina> cajoneriaChoco = new Cajoneria<>(3);
        cajoneriaChoco.add(new Caja<>("Rojo", new Chocolatina("Sublime")));
        cajoneriaChoco.add(new Caja<>("Azul", new Chocolatina("Mecano")));

        System.out.println("--- Cajoneria Chocolatinas ---");
        System.out.println(cajoneriaChoco);
    }
}
