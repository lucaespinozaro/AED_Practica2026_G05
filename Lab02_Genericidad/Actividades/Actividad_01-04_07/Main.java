public class Main {
    public static void main(String[] args) {
        Bolsa<Chocolatina> bolsaChoco = new Bolsa<>(3);
        bolsaChoco.add(new Chocolatina("Milka"));
        bolsaChoco.add(new Chocolatina("Ferrero"));
        bolsaChoco.add(new Chocolatina("Lindt"));

        for (Chocolatina c : bolsaChoco) {
            System.out.println(c.getMarca());
        }

        System.out.println("-----");

        Bolsa<Golosina> bolsaGolo = new Bolsa<>(4);
        bolsaGolo.add(new Golosina("Gomitas", 50));
        bolsaGolo.add(new Golosina("Caramelo", 20));
        bolsaGolo.add(new Golosina("Chocolate", 30));

        for (Golosina g : bolsaGolo) {
            System.out.println(g.getNombre() + " - " + g.getPeso());
        }
    }
}
