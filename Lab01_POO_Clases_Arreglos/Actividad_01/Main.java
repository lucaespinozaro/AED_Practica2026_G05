import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Ingrese una esquina del 1er rectangulo: ");
        double x1 = sc.nextDouble();
        double y1 = sc.nextDouble();
        Coordenada a1 = new Coordenada(x1, y1);

        System.out.print("Ingrese la esquina opuesta del 1er rectangulo: ");
        double x2 = sc.nextDouble();
        double y2 = sc.nextDouble();
        Coordenada a2 = new Coordenada(x2, y2);

        Rectangulo A = new Rectangulo(a1, a2);

        System.out.print("Ingrese una esquina del 2do rectangulo: ");
        double x3 = sc.nextDouble();
        double y3 = sc.nextDouble();
        Coordenada b1 = new Coordenada(x3, y3);

        System.out.print("Ingrese la esquina opuesta del 2do rectangulo: ");
        double x4 = sc.nextDouble();
        double y4 = sc.nextDouble();
        Coordenada b2 = new Coordenada(x4, y4);

        Rectangulo B = new Rectangulo(b1, b2);

        System.out.println("\nRectangulo A = " + A);
        System.out.println("Rectangulo B = " + B);
        
        if (Verificador.esSobrePos(A, B)) {
            System.out.println("Rectangulos A y B se sobreponen.");
            Rectangulo S = rectanguloS(A, B);
            System.out.printf("Area de sobreposicion = %.2f\n", S.calculoArea());
        } else if (Verificador.esJunto(A, B)) {
            System.out.println("Rectangulos A y B se juntan");
        } else if (Verificador.esDisjunto(A, B)) {
            System.out.println("Rectangulos A y B son disjuntos");
        }

        sc.close();
    }

    public static Rectangulo rectanguloS(Rectangulo r1, Rectangulo r2) {
        public static Rectangulo rectanguloS(Rectangulo r1, Rectangulo r2) {
        double x_izq, y_abj, x_der, y_arb;

        if (r1.getEsquina1().getX() > r2.getEsquina1().getX()) {
            x_izq = r1.getEsquina1().getX();
        } else {
            x_izq = r2.getEsquina1().getX();
        }

        if (r1.getEsquina1().getY() > r2.getEsquina1().getY()) {
            y_abj = r1.getEsquina1().getY();
        } else {
            y_abj = r2.getEsquina1().getY();
        }

        if (r1.getEsquina2().getX() < r2.getEsquina2().getX()) {
            x_der = r1.getEsquina2().getX();
        } else {
            x_der = r2.getEsquina2().getX();
        }

        if (r1.getEsquina2().getY() < r2.getEsquina2().getY()) {
            y_arb = r1.getEsquina2().getY();
        } else {
            y_arb = r2.getEsquina2().getY();
        }

        return new Rectangulo(new Coordenada(x_izq, y_abj), new Coordenada(x_der, y_arb));
    }
}
