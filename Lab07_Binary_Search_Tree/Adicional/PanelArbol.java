import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;
import java.util.Map;

public class PanelArbol extends JPanel {
    private LinkedBST<Libro> arbol;
    private String isbnResaltado = null;

    private static final int NODO_ANCHO = 96;
    private static final int NODO_ALTO  = 44;
    private static final int NIVEL_ALTO = 84;
    private static final int MARGEN_X   = 20;

    private Map<LinkedBST<Libro>.Node, Integer> posX = new HashMap<>();
    private Map<LinkedBST<Libro>.Node, Integer> posY = new HashMap<>();
    private int[] contadorX = {0};

    private static final Color COLOR_NODO          = new Color(41, 98, 170);
    private static final Color COLOR_NODO_PRESTADO = new Color(185, 50, 38);
    private static final Color COLOR_NODO_RESALT   = new Color(34, 163, 84);
    private static final Color COLOR_RAIZ          = new Color(130, 55, 168);
    private static final Color COLOR_FLECHA        = new Color(110, 110, 120);
    private static final Color COLOR_FONDO         = new Color(244, 246, 250);
    private static final Color COLOR_TEXTO         = Color.WHITE;

    public PanelArbol(LinkedBST<Libro> arbol) {
        this.arbol = arbol;
        setBackground(COLOR_FONDO);
        setPreferredSize(new Dimension(900, 500));
    }

    public void setArbol(LinkedBST<Libro> arbol) {
        this.arbol = arbol;
        repaint();
    }

    public void resaltarNodo(String isbn) {
        this.isbnResaltado = isbn;
        repaint();
    }

    public void limpiarResaltado() {
        this.isbnResaltado = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (arbol.isEmpty()) {
            dibujarMensajeVacio(g2);
            return;
        }

        posX.clear();
        posY.clear();
        contadorX[0] = 0;
        calcularPosiciones(arbol.getRoot(), 0);

        int maxX = posX.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        int maxY = posY.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        int prefAncho = Math.max(900, maxX + NODO_ANCHO + MARGEN_X * 2);
        int prefAlto  = Math.max(500, maxY + NODO_ALTO + 40);
        if (getPreferredSize().width != prefAncho || getPreferredSize().height != prefAlto) {
            setPreferredSize(new Dimension(prefAncho, prefAlto));
            revalidate();
        }

        dibujarAristas(g2, arbol.getRoot());
        dibujarNodos(g2, arbol.getRoot(), true);
    }

    private void calcularPosiciones(LinkedBST<Libro>.Node nodo, int nivel) {
        if (nodo == null) return;
        calcularPosiciones(nodo.left, nivel + 1);
        posX.put(nodo, MARGEN_X + contadorX[0] * (NODO_ANCHO + 20));
        posY.put(nodo, 30 + nivel * NIVEL_ALTO);
        contadorX[0]++;
        calcularPosiciones(nodo.right, nivel + 1);
    }

    private void dibujarAristas(Graphics2D g2, LinkedBST<Libro>.Node nodo) {
        if (nodo == null) return;
        int px = posX.get(nodo) + NODO_ANCHO / 2;
        int py = posY.get(nodo) + NODO_ALTO;

        if (nodo.left != null) {
            int cx = posX.get(nodo.left) + NODO_ANCHO / 2;
            int cy = posY.get(nodo.left);
            g2.setColor(COLOR_FLECHA);
            g2.setStroke(new BasicStroke(2f));
            g2.drawLine(px, py, cx, cy);
            dibujarPunta(g2, px, py, cx, cy);
            dibujarAristas(g2, nodo.left);
        }
        if (nodo.right != null) {
            int cx = posX.get(nodo.right) + NODO_ANCHO / 2;
            int cy = posY.get(nodo.right);
            g2.setColor(COLOR_FLECHA);
            g2.setStroke(new BasicStroke(2f));
            g2.drawLine(px, py, cx, cy);
            dibujarPunta(g2, px, py, cx, cy);
            dibujarAristas(g2, nodo.right);
        }
    }

    private void dibujarPunta(Graphics2D g2, int x1, int y1, int x2, int y2) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        int len = 10;
        int[] xp = {x2, (int)(x2 - len * Math.cos(angle - 0.4)), (int)(x2 - len * Math.cos(angle + 0.4))};
        int[] yp = {y2, (int)(y2 - len * Math.sin(angle - 0.4)), (int)(y2 - len * Math.sin(angle + 0.4))};
        g2.fillPolygon(xp, yp, 3);
    }

    private void dibujarNodos(Graphics2D g2, LinkedBST<Libro>.Node nodo, boolean esRaiz) {
        if (nodo == null) return;

        int x = posX.get(nodo);
        int y = posY.get(nodo);
        String isbn = nodo.data.getIsbn();

        Color colorFondo;
        if (isbn.equals(isbnResaltado))       colorFondo = COLOR_NODO_RESALT;
        else if (esRaiz)                       colorFondo = COLOR_RAIZ;
        else if (!nodo.data.isDisponible())    colorFondo = COLOR_NODO_PRESTADO;
        else                                   colorFondo = COLOR_NODO;

        g2.setColor(new Color(0, 0, 0, 40));
        g2.fill(new RoundRectangle2D.Double(x + 3, y + 3, NODO_ANCHO, NODO_ALTO, 14, 14));

        g2.setColor(colorFondo);
        g2.fill(new RoundRectangle2D.Double(x, y, NODO_ANCHO, NODO_ALTO, 14, 14));

        if (isbn.equals(isbnResaltado)) {
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(2.5f));
            g2.draw(new RoundRectangle2D.Double(x, y, NODO_ANCHO, NODO_ALTO, 14, 14));
        }

        g2.setColor(COLOR_TEXTO);
        g2.setFont(new Font("SansSerif", Font.BOLD, 11));
        String isbnShort = isbn.length() > 10 ? isbn.substring(0, 10) : isbn;
        g2.drawString(isbnShort, x + 6, y + 16);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
        String titulo = nodo.data.getTitulo();
        if (titulo.length() > 13) titulo = titulo.substring(0, 13) + "…";
        g2.drawString(titulo, x + 6, y + 30);

        if (!nodo.data.isDisponible() && !isbn.equals(isbnResaltado)) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 9));
            g2.setColor(new Color(255, 215, 80));
            g2.drawString("PRESTADO", x + 4, y + NODO_ALTO - 4);
        }

        dibujarNodos(g2, nodo.left, false);
        dibujarNodos(g2, nodo.right, false);
    }

    private void dibujarMensajeVacio(Graphics2D g2) {
        g2.setColor(new Color(180, 180, 190));
        g2.setFont(new Font("SansSerif", Font.ITALIC, 16));
        String msg = "El árbol está vacío. Agrega libros para visualizarlo.";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
    }

    public void actualizar() { repaint(); }
}
