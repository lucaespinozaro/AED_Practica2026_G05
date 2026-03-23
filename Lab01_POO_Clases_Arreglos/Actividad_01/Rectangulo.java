import java.util.*;

public class Rectangulo {
    private Coordenada esquina1;
    private Coordenada esquina2;

    public Rectangulo(Coordenada c1, Coordenada c2) {
        double minX, minY, maxX, maxY;

        if (c1.getX() < c2.getX()) {
            minX = c1.getX();
            maxX = c2.getX();
        } else {
            minX = c2.getX();
            maxX = c1.getX();
        }

        if (c1.getY() < c2.getY()) {
            minY = c1.getY();
            maxY = c2.getY();
        } else {
            minY = c2.getY();
            maxY = c1.getY();
        }
        
        this.esquina1 = new Coordenada(minX, minY);
        this.esquina2 = new Coordenada(maxX, maxY);
    }

    public void setEsquina1(Coordenada coo) {
        this.esquina1 = new Coordenada(coo);
    }

    public void setEsquina2(Coordenada coo) {
        this.esquina2 = new Coordenada(coo);
    }

    public Coordenada getEsquina1() {
        return esquina1;
    }

    public Coordenada getEsquina2() {
        return esquina2;
    }

    public double calculoArea() {
        double base = esquina2.getX() - esquina1.getX();
        double altura = esquina2.getY() - esquina1.getY();
        return base * altura;
    }

    public String toString() {
        return "(" + esquina1 + ", " + esquina2 + ")";
    }
}
