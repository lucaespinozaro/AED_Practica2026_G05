import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;
import java.util.Map;

public class PanelArbol extends JPanel {
    private ArbolBST arbol;
    private String isbnResaltado = null;

    private static final int NODO_ANCHO = 90;
    private static final int NODO_ALTO = 42;
    private static final int NIVEL_ALTO = 80;
    private static final int MARGEN_X = 20;

    private Map<NodoBST, Integer> posX = new HashMap<>();
    private Map<NodoBST, Integer> posY = new HashMap<>();
    private int[] contadorX = {0};

    private static final Color COLOR_NODO          = new Color(52, 101, 164);
    private static final Color COLOR_NODO_PRESTADO = new Color(192, 57, 43);
    private static final Color COLOR_NODO_RESALT   = new Color(39, 174, 96);
    private static final Color COLOR_RAIZ          = new Color(142, 68, 173);
    private static final Color COLOR_FLECHA        = new Color(100, 100, 100);
    private static final Color COLOR_FONDO         = new Color(245, 247, 250);
    private static final Color COLOR_TEXTO         = Color.WHITE;

    public PanelArbol(ArbolBST arbol) {
        this.arbol = arbol;
        setBackground(COLOR_FONDO);
        setPreferredSize(new Dimension(900, 500));
    }

    public void setArbol(ArbolBST arbol) {
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

        if (arbol.estaVacio()) {
            dibujarMensajeVacio(g2);
            return;
        }

        posX.clear();
        posY.clear();
        contadorX[0] = 0;
        calcularPosiciones(arbol.getRaiz(), 0);

        int maxX = posX.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        int maxY = posY.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        int prefAncho = Math.max(900, maxX + NODO_ANCHO + MARGEN_X * 2);
        int prefAlto  = Math.max(500, maxY + NODO_ALTO + 40);
        if (getPreferredSize().width != prefAncho || getPreferredSize().height != prefAlto) {
            setPreferredSize(new Dimension(prefAncho, prefAlto));
            revalidate();
        }

        dibujarAristas(g2, arbol.getRaiz());
        dibujarNodos(g2, arbol.getRaiz(), true);
    }

    private void calcularPosiciones(NodoBST nodo, int nivel) {
        if (nodo == null) return;
        calcularPosiciones(nodo.izquierdo, nivel + 1);
        int x = MARGEN_X + contadorX[0] * (NODO_ANCHO + 20);
        int y = 30 + nivel * NIVEL_ALTO;
        posX.put(nodo, x);
        posY.put(nodo, y);
        contadorX[0]++;
        calcularPosiciones(nodo.derecho, nivel + 1);
    }

    private void dibujarAristas(Graphics2D g2, NodoBST nodo) {
        if (nodo == null) return;
        int px = posX.get(nodo) + NODO_ANCHO / 2;
        int py = posY.get(nodo) + NODO_ALTO;

        if (nodo.izquierdo != null) {
            int cx = posX.get(nodo.izquierdo) + NODO_ANCHO / 2;
            int cy = posY.get(nodo.izquierdo);
            g2.setColor(COLOR_FLECHA);
            g2.setStroke(new BasicStroke(2f));
            g2.drawLine(px, py, cx, cy);
            dibujarPunta(g2, px, py, cx, cy);
            dibujarAristas(g2, nodo.izquierdo);
        }
        if (nodo.derecho != null) {
            int cx = posX.get(nodo.derecho) + NODO_ANCHO / 2;
            int cy = posY.get(nodo.derecho);
            g2.setColor(COLOR_FLECHA);
            g2.setStroke(new BasicStroke(2f));
            g2.drawLine(px, py, cx, cy);
            dibujarPunta(g2, px, py, cx, cy);
            dibujarAristas(g2, nodo.derecho);
        }
    }

    private void dibujarPunta(Graphics2D g2, int x1, int y1, int x2, int y2) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        int punLen = 10;
        int[] xp = {x2,
                (int)(x2 - punLen * Math.cos(angle - 0.4)),
                (int)(x2 - punLen * Math.cos(angle + 0.4))};
        int[] yp = {y2,
                (int)(y2 - punLen * Math.sin(angle - 0.4)),
                (int)(y2 - punLen * Math.sin(angle + 0.4))};
        g2.fillPolygon(xp, yp, 3);
    }

    private void dibujarNodos(Graphics2D g2, NodoBST nodo, boolean esRaiz) {
        if (nodo == null) return;

        int x = posX.get(nodo);
        int y = posY.get(nodo);
        String isbn = nodo.libro.getIsbn();

        Color colorFondo;
        if (isbn.equals(isbnResaltado)) {
            colorFondo = COLOR_NODO_RESALT;
        } else if (esRaiz) {
            colorFondo = COLOR_RAIZ;
        } else if (!nodo.libro.isDisponible()) {
            colorFondo = COLOR_NODO_PRESTADO;
        } else {
            colorFondo = COLOR_NODO;
        }

        g2.setColor(new Color(0, 0, 0, 40));
        g2.fill(new RoundRectangle2D.Double(x + 3, y + 3, NODO_ANCHO, NODO_ALTO, 12, 12));

        g2.setColor(colorFondo);
        g2.fill(new RoundRectangle2D.Double(x, y, NODO_ANCHO, NODO_ALTO, 12, 12));

        if (isbn.equals(isbnResaltado)) {
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(2.5f));
            g2.draw(new RoundRectangle2D.Double(x, y, NODO_ANCHO, NODO_ALTO, 12, 12));
        }

        g2.setColor(COLOR_TEXTO);
        g2.setFont(new Font("SansSerif", Font.BOLD, 11));
        String isbnShort = isbn.length() > 10 ? isbn.substring(0, 10) : isbn;
        g2.drawString(isbnShort, x + 6, y + 16);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
        String titulo = nodo.libro.getTitulo();
        if (titulo.length() > 12) titulo = titulo.substring(0, 12) + "…";
        g2.drawString(titulo, x + 6, y + 31);

        if (!nodo.libro.isDisponible() && !isbn.equals(isbnResaltado)) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 10));
            g2.setColor(new Color(255, 220, 100));
            g2.drawString("PRESTADO", x + 4, y + NODO_ALTO - 4);
        }

        dibujarNodos(g2, nodo.izquierdo, false);
        dibujarNodos(g2, nodo.derecho, false);
    }

    private void dibujarMensajeVacio(Graphics2D g2) {
        g2.setColor(new Color(180, 180, 180));
        g2.setFont(new Font("SansSerif", Font.ITALIC, 16));
        String msg = "El árbol está vacío. Agrega libros para visualizarlo.";
        FontMetrics fm = g2.getFontMetrics();
        int w = fm.stringWidth(msg);
        g2.drawString(msg, (getWidth() - w) / 2, getHeight() / 2);
    }

    public void actualizar() {
        repaint();
    }
}
