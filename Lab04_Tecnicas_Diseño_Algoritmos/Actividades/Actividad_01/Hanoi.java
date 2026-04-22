package Actividad_01;
public class Hanoi {

    public static void main(String[] args) {
        int numDiscos = 3;
        System.out.println(" Torres de Hanoi con " + numDiscos + " discos ");
        torresHanoi(numDiscos, 1, 2, 3);
        System.out.println("Total de movimientos: " + (int)(Math.pow(2, numDiscos) - 1));
    }

    public static void torresHanoi(int discos, int origen, int auxiliar, int destino) {
        if (discos <= 0) {
            throw new IllegalArgumentException("El número de discos debe ser mayor que 0");
        }

        if (discos == 1) {
            System.out.println("Mover disco 1 de Torre " + origen + " a Torre " + destino);
        } else {
            torresHanoi(discos - 1, origen, destino, auxiliar);
            System.out.println("Mover disco " + discos + " de Torre " + origen + " a Torre " + destino);
            torresHanoi(discos - 1, auxiliar, origen, destino);
        }
    }
}
