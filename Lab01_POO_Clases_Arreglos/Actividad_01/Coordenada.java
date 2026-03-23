import java.util.*;

public class Coordenada {
    private double x;
    private double y;

    public Coordenada() {
        this.x = 0;
        this.y = 0;
    }

    public Coordenada(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Coordenada(Coordenada c) {
        this.x = c.getX();
        this.y = c.getY();
    }

    void setX(double x) {
        this.x = x;
    }

    void setY(double y) {
        this.y = y;
    }

    double getX() {
        return x;
    }

    double getY() {
        return y;
    }

    double distancia(Coordenada c) {
        double dif_x = this.x - c.getX();
        double dif_y = this.y - c.getY();
        return Math.sqrt(dif_x * dif_x + dif_y * dif_y);
    }

    static double distancia(Coordenada c1, Coordenada c2) {
        double dif_x = c1.getX() - c2.getX();
        double dif_y = c1.getY() - c2.getY();
        return Math.sqrt(dif_x * dif_x + dif_y * dif_y);
    }

    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
