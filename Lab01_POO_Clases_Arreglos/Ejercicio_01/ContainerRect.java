public class ContainerRect {
    private Rectangulo[] rectangulos;
    private double[] distancias;
    private double[] areas;
    private int n;
    private static int numRec = 0;

    public ContainerRect(int n) {
        this.n = n;
        this.rectangulos = new Rectangulo[n];
        this.distancias = new double[n];
        this.areas = new double[n];
    }

    public void addRectangulo(Rectangulo r) {
        if (numRec < n) {
            rectangulos[numRec] = r;
            distancias[numRec] = r.getEsquina1().distancia(r.getEsquina2());
            areas[numRec] = r.calculoArea();
            numRec++;
        } else {
            System.out.println("Ya no es posible guardar el rectangulo");
        }
    }

    public String toString() {
        String tabla = String.format("%-12s %-30s %-12s %-10s\n", "Rectangulo", "Coordenadas", "Distancia", "Area");
        for (int i = 0; i < numRec; i++) {
            tabla += String.format("%-12d %-30s %-12.3f %-10.2f\n", 
                    (i + 1), rectangulos[i].toString(), distancias[i], areas[i]);
        }
        return tabla;
    }
}
