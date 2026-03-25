public class TestEstacion {
    public static void main(String[] args) {
        System.out.println(" Zona de smartphones ");
        PowerStation<Smartphone> zonaSmartphones = new PowerStation<Smartphone>();

        Smartphone s1 = new Smartphone("iPhone 15", 20.5);
        Smartphone s2 = new Smartphone("Galaxy S24", 25.0);
        Smartphone s3 = new Smartphone("Xiaomi 13", 18.0);

        zonaSmartphones.conectar(s1);
        zonaSmartphones.conectar(s2);
        zonaSmartphones.conectar(s3);

        s1.cargar(40);
        s2.cargar(60);

        zonaSmartphones.mostrarReporte();

        Smartphone buscar1 = new Smartphone("Galaxy S24", 25.0);
        int pos = zonaSmartphones.buscarDispositivo(buscar1);
        if (pos != -1) {
            System.out.println("\nGalaxy S24 encontrado en posicion: " + pos);
        } else {
            System.out.println("\nGalaxy S24 no encontrado.");
        }

        Smartphone buscar2 = new Smartphone("Motorola G84", 15.0);
        int pos2 = zonaSmartphones.buscarDispositivo(buscar2);
        if (pos2 != -1) {
            System.out.println("Motorola G84 encontrado en posicion: " + pos2);
        } else {
            System.out.println("Motorola G84 no encontrado en la estacion.");
        }

        System.out.println("\n Zona de laptops ");
        PowerStation<Laptop> zonaLaptops = new PowerStation<Laptop>();

        Laptop l1 = new Laptop("Dell XPS", 65.0);
        Laptop l2 = new Laptop("MacBook Pro", 70.0);
        Laptop l3 = new Laptop("Lenovo ThinkPad", 45.0);

        zonaLaptops.conectar(l1);
        zonaLaptops.conectar(l2);
        zonaLaptops.conectar(l3);

        l1.cargar(30);
        l3.cargar(80);

        zonaLaptops.mostrarReporte();

        Laptop buscarL = new Laptop("MacBook Pro", 70.0);
        int posL = zonaLaptops.buscarDispositivo(buscarL);
        if (posL != -1) {
            System.out.println("\nMacBook Pro encontrado en posicion: " + posL);
        } else {
            System.out.println("\nMacBook Pro no encontrado.");
        }
    }
}
