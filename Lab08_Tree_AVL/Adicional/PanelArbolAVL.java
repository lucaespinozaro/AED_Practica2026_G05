import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;
import java.util.Map;

public class PanelArbolAVL extends JPanel {
    private AVLTree<Contacto> arbol;
    private String nombreResaltado = null;

    private static final int NODO_ANCHO = 110;
    private static final int NODO_ALTO  = 52;
    private static final int NIVEL_ALTO = 90;
    private static final int MARGEN_X   = 24;

    private Map<Object, Integer> posX = new HashMap<>();
    private Map<Object, Integer> posY = new HashMap<>();
    private int[] contadorX = {0};

    private static final Color COLOR_NODO          = new Color(30, 90, 160);
    private static final Color COLOR_NODO_RAIZ      = new Color(100, 40, 160);
    private static final Color COLOR_NODO_FAVORITO  = new Color(180, 120, 0);
    private static final Color COLOR_NODO_RESALT    = new Color(20, 150, 80);
    private static final Color COLOR_DESBALANCE     = new Color(180, 40, 40);
    private static final Color COLOR_FLECHA         = new Color(100, 120, 160);
    private static final Color COLOR_FONDO          = new Color(15, 20, 40);
    private static final Color COLOR_TEXTO          = Color.WHITE;
    private static final Color COLOR_BF_OK          = new Color(80, 220, 130);
    private static final Color COLOR_BF_MAL         = new Color(255, 90, 90);

    public PanelArbolAVL(AVLTree<Contacto> arbol) {
        this.arbol = arbol;
        setBackground(COLOR_FONDO);
        setPreferredSize(new Dimension(900, 520));
    }

    public void setArbol(AVLTree<Contacto> arbol) {
        this.arbol = arbol;
        repaint();
    }

    public void resaltarNodo(String nombre) {
        this.nombreResaltado = nombre;
        repaint();
    }

    public void limpiarResaltado() {
        this.nombreResaltado = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        dibujarFondoEstrellado(g2);

        if (arbol.isEmpty()) {
            dibujarMensajeVacio(g2);
            return;
        }

        posX.clear();
        posY.clear();
        contadorX[0] = 0;
        calcularPosiciones(arbol.getRootAVL(), 0);

        int maxX = posX.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        int maxY = posY.values().stream().mapToInt(Integer::intValue).max().orElse(0);
        int prefAncho = Math.max(900, maxX + NODO_ANCHO + MARGEN_X * 2);
        int prefAlto  = Math.max(520, maxY + NODO_ALTO + 50);
        if (getPreferredSize().width != prefAncho || getPreferredSize().height != prefAlto) {
            setPreferredSize(new Dimension(prefAncho, prefAlto));
            revalidate();
        }

        dibujarAristas(g2, arbol.getRootAVL());
        dibujarNodos(g2, arbol.getRootAVL(), true);
    }

    private void dibujarFondoEstrellado(Graphics2D g2) {
        g2.setColor(COLOR_FONDO);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(new Color(255, 255, 255, 18));
        java.util.Random rng = new java.util.Random(42);
        for (int i = 0; i < 80; i++) {
            int sx = rng.nextInt(Math.max(1, getWidth()));
            int sy = rng.nextInt(Math.max(1, getHeight()));
            int sr = rng.nextInt(2) + 1;
            g2.fillOval(sx, sy, sr, sr);
        }
    }

    private void calcularPosiciones(AVLTree<Contacto>.NodeAVL nodo, int nivel) {
        if (nodo == null) return;
        calcularPosiciones((AVLTree<Contacto>.NodeAVL) nodo.left, nivel + 1);
        posX.put(nodo, MARGEN_X + contadorX[0] * (NODO_ANCHO + 18));
        posY.put(nodo, 36 + nivel * NIVEL_ALTO);
        contadorX[0]++;
        calcularPosiciones((AVLTree<Contacto>.NodeAVL) nodo.right, nivel + 1);
    }

    private void dibujarAristas(Graphics2D g2, AVLTree<Contacto>.NodeAVL nodo) {
        if (nodo == null) return;
        int px = posX.get(nodo) + NODO_ANCHO / 2;
        int py = posY.get(nodo) + NODO_ALTO;

        if (nodo.left != null) {
            int cx = posX.get(nodo.left) + NODO_ANCHO / 2;
            int cy = posY.get(nodo.left);
            dibujarLinea(g2, px, py, cx, cy);
            dibujarAristas(g2, (AVLTree<Contacto>.NodeAVL) nodo.left);
        }
        if (nodo.right != null) {
            int cx = posX.get(nodo.right) + NODO_ANCHO / 2;
            int cy = posY.get(nodo.right);
            dibujarLinea(g2, px, py, cx, cy);
            dibujarAristas(g2, (AVLTree<Contacto>.NodeAVL) nodo.right);
        }
    }

    private void dibujarLinea(Graphics2D g2, int x1, int y1, int x2, int y2) {
        g2.setColor(COLOR_FLECHA);
        g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(x1, y1, x2, y2);
        double angle = Math.atan2(y2 - y1, x2 - x1);
        int len = 9;
        int[] xp = {x2, (int)(x2 - len * Math.cos(angle - 0.4)), (int)(x2 - len * Math.cos(angle + 0.4))};
        int[] yp = {y2, (int)(y2 - len * Math.sin(angle - 0.4)), (int)(y2 - len * Math.sin(angle + 0.4))};
        g2.setColor(COLOR_FLECHA);
        g2.fillPolygon(xp, yp, 3);
    }

    private void dibujarNodos(Graphics2D g2, AVLTree<Contacto>.NodeAVL nodo, boolean esRaiz) {
        if (nodo == null) return;
        int x = posX.get(nodo);
        int y = posY.get(nodo);
        String nombre = nodo.data.getNombre();
        int bf = nodo.bf;

        Color colorBase;
        if (nombre.equalsIgnoreCase(nombreResaltado)) colorBase = COLOR_NODO_RESALT;
        else if (esRaiz)                               colorBase = COLOR_NODO_RAIZ;
        else if (nodo.data.isFavorito())               colorBase = COLOR_NODO_FAVORITO;
        else                                           colorBase = COLOR_NODO;

        g2.setColor(new Color(0, 0, 0, 60));
        g2.fill(new RoundRectangle2D.Double(x + 3, y + 3, NODO_ANCHO, NODO_ALTO, 16, 16));

        GradientPaint grad = new GradientPaint(x, y, colorBase.brighter(), x, y + NODO_ALTO, colorBase.darker());
        g2.setPaint(grad);
        g2.fill(new RoundRectangle2D.Double(x, y, NODO_ANCHO, NODO_ALTO, 16, 16));

        if (nombre.equalsIgnoreCase(nombreResaltado)) {
            g2.setColor(new Color(100, 255, 160));
            g2.setStroke(new BasicStroke(2.5f));
            g2.draw(new RoundRectangle2D.Double(x, y, NODO_ANCHO, NODO_ALTO, 16, 16));
        }

        g2.setColor(COLOR_TEXTO);
        g2.setFont(new Font("SansSerif", Font.BOLD, 11));
        String nombreCorto = nombre.length() > 14 ? nombre.substring(0, 14) + "…" : nombre;
        g2.drawString(nombreCorto, x + 6, y + 16);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
        String tel = nodo.data.getTelefono();
        if (tel.length() > 14) tel = tel.substring(0, 14);
        g2.setColor(new Color(200, 220, 255));
        g2.drawString(tel, x + 6, y + 28);

        Color bfColor = (bf == 0 || bf == 1 || bf == -1) ? COLOR_BF_OK : COLOR_BF_MAL;
        g2.setColor(bfColor);
        g2.setFont(new Font("Monospaced", Font.BOLD, 10));
        String bfStr = "bf=" + bf;
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(bfStr, x + NODO_ANCHO - fm.stringWidth(bfStr) - 5, y + NODO_ALTO - 5);

        if (nodo.data.isFavorito()) {
            g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
            g2.setColor(new Color(255, 215, 60));
            g2.drawString("★", x + 5, y + NODO_ALTO - 5);
        }

        if (esRaiz) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 9));
            g2.setColor(new Color(220, 180, 255));
            g2.drawString("RAÍZ", x + NODO_ANCHO / 2 - 10, y - 6);
        }

        dibujarNodos(g2, (AVLTree<Contacto>.NodeAVL) nodo.left, false);
        dibujarNodos(g2, (AVLTree<Contacto>.NodeAVL) nodo.right, false);
    }

    private void dibujarMensajeVacio(Graphics2D g2) {
        g2.setColor(new Color(80, 100, 160));
        g2.setFont(new Font("SansSerif", Font.ITALIC, 15));
        String msg = "La agenda está vacía. Agrega contactos para visualizar el AVL.";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
    }

    public void actualizar() { repaint(); }
}
