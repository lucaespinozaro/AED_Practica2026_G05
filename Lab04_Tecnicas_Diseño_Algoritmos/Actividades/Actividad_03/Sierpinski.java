import javax.swing.*;
import java.awt.*;

public class Sierpinski extends JPanel {
    int nivelMax;

    public Sierpinski(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("El nivel debe ser mayor a 0");
        nivelMax = n;
    }

    void draw(Graphics g, int x1, int y1, int x2, int y2, int x3, int y3, int n, int p) {
        if (g == null)
            throw new IllegalArgumentException("Graphics no puede ser null");

        float b = 0.3f + 0.7f * (1 - (float)p / nivelMax);
        g.setColor(Color.getHSBColor(0.6f, 1, b));

        g.fillPolygon(new int[]{x1, x2, x3}, new int[]{y1, y2, y3}, 3);

        if (n > 0) {
            int mx12 = (x1 + x2)/2, my12 = (y1 + y2)/2;
            int mx23 = (x2 + x3)/2, my23 = (y2 + y3)/2;
            int mx31 = (x3 + x1)/2, my31 = (y3 + y1)/2;

            draw(g, x1,y1, mx12,my12, mx31,my31, n-1, p+1);
            draw(g, mx12,my12, x2,y2, mx23,my23, n-1, p+1);
            draw(g, mx31,my31, mx23,my23, x3,y3, n-1, p+1);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g, 100, 500, 500, 500, 300, 100, nivelMax, 0);
    }

    public static void main(String[] args) {
        int n = 0;

        while (true) {
            try {
                String input = JOptionPane.showInputDialog("Nivel:");

                if (input == null) return; // usuario canceló

                n = Integer.parseInt(input);

                if (n > 0) break;

                JOptionPane.showMessageDialog(null, "Ingrese un número mayor a 0");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Entrada inválida");
            }
        }

        JFrame f = new JFrame("Sierpinski");
        f.add(new Sierpinski(n));
        f.setSize(600, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}
