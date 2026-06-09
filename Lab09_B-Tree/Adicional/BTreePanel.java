import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class BTreePanel extends JPanel {

    private BTree<Vuelo> tree;
    private String        highlightCodigo = null;
    private java.util.List<String> rangeHL = new ArrayList<>();

    // ── Paleta ────────────────────────────────────────────────
    private static final Color C_BG       = new Color(10, 14, 26);
    private static final Color C_NODE     = new Color(18, 28, 52);
    private static final Color C_BORDER   = new Color(40, 100, 200);
    private static final Color C_TEXT     = new Color(180, 210, 255);
    private static final Color C_DIV      = new Color(40, 100, 200, 80);
    private static final Color C_EDGE     = new Color(35, 80, 160, 180);
    private static final Color C_FIND     = new Color(255, 200, 0);
    private static final Color C_RANGE    = new Color(40, 210, 130);
    private static final Color C_CANCEL   = new Color(200, 60, 60);
    private static final Color C_BOARD    = new Color(30, 160, 90);
    private static final Color C_DELAY    = new Color(220, 140, 30);

    private static final int NODE_H = 48;
    private static final int KEY_W  = 88;
    private static final int V_GAP  = 72;
    private static final int ARC    = 8;

    public BTreePanel(BTree<Vuelo> tree) {
        this.tree = tree;
        setBackground(C_BG);
        setPreferredSize(new Dimension(1200, 480));
    }

    public void setTree(BTree<Vuelo> t) { this.tree = t; repaint(); }
    public void highlight(String cod)   { highlightCodigo = cod; rangeHL.clear(); repaint(); }
    public void highlightRange(java.util.List<String> list) { rangeHL = list; highlightCodigo = null; repaint(); }
    public void clearHL()               { highlightCodigo = null; rangeHL.clear(); repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // grid de puntos de fondo (estética HUD)
        drawGrid(g2);

        if (tree.isEmpty()) {
            drawEmptyMsg(g2);
            return;
        }
        Map<BNode<Vuelo>, Point> pos = new HashMap<>();
        computePos(tree.getRoot(), 0, 0, getWidth(), pos);
        drawEdges(g2, tree.getRoot(), pos);
        drawNodes(g2, tree.getRoot(), pos);
    }

    private void drawGrid(Graphics2D g2) {
        g2.setColor(new Color(25, 38, 65, 60));
        for (int x = 0; x < getWidth();  x += 30) g2.drawLine(x, 0, x, getHeight());
        for (int y = 0; y < getHeight(); y += 30) g2.drawLine(0, y, getWidth(), y);
    }

    private void drawEmptyMsg(Graphics2D g2) {
        g2.setColor(new Color(60, 90, 140));
        g2.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        String m = "✈  Sin vuelos registrados. Agrega un vuelo para ver el B-Tree.";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(m, (getWidth() - fm.stringWidth(m)) / 2, getHeight() / 2);
    }

    private int nw(BNode<Vuelo> n) { return n.count * KEY_W + 16; }

    private void computePos(BNode<Vuelo> n, int depth, int xL, int xR,
                             Map<BNode<Vuelo>, Point> pos) {
        if (n == null) return;
        int cx = (xL + xR) / 2;
        int y  = 44 + depth * (NODE_H + V_GAP);
        pos.put(n, new Point(cx, y));
        int kids = n.count + 1;
        int sw   = Math.max(1, (xR - xL) / kids);
        for (int i = 0; i <= n.count; i++)
            if (n.childs.get(i) != null)
                computePos(n.childs.get(i), depth + 1, xL + i * sw, xL + (i + 1) * sw, pos);
    }

    private void drawEdges(Graphics2D g2, BNode<Vuelo> n, Map<BNode<Vuelo>, Point> pos) {
        if (n == null) return;
        Point p  = pos.get(n);
        int   nw = nw(n);
        for (int i = 0; i <= n.count; i++) {
            BNode<Vuelo> child = n.childs.get(i);
            if (child == null) continue;
            Point cp = pos.get(child);
            // anchor point on parent bottom
            int ax = (i < n.count)
                ? p.x - nw / 2 + i * KEY_W + KEY_W / 2
                : p.x - nw / 2 + n.count * KEY_W - KEY_W / 2 + KEY_W;

            GradientPaint gp = new GradientPaint(
                ax, p.y + NODE_H / 2, new Color(40, 100, 200, 160),
                cp.x, cp.y - NODE_H / 2, new Color(40, 100, 200, 30));
            g2.setPaint(gp);
            g2.setStroke(new BasicStroke(1.8f));
            g2.drawLine(ax, p.y + NODE_H / 2, cp.x, cp.y - NODE_H / 2);
            drawEdges(g2, child, pos);
        }
    }

    private void drawNodes(Graphics2D g2, BNode<Vuelo> n, Map<BNode<Vuelo>, Point> pos) {
        if (n == null) return;
        Point p  = pos.get(n);
        int   nw = nw(n);
        int   x  = p.x - nw / 2;
        int   y  = p.y - NODE_H / 2;

        // outer glow for highlighted node
        boolean nodeHL = false;
        for (int i = 0; i < n.count; i++) {
            Vuelo v = n.keys.get(i);
            if (v == null) continue;
            if ((highlightCodigo != null && v.getCodigo().equals(highlightCodigo))
                || rangeHL.contains(v.getCodigo())) { nodeHL = true; break; }
        }
        if (nodeHL) {
            g2.setColor(new Color(255, 220, 50, 35));
            g2.fillRoundRect(x - 5, y - 5, nw + 10, NODE_H + 10, ARC + 4, ARC + 4);
        }

        // node body
        GradientPaint bg = new GradientPaint(x, y, new Color(22, 35, 65), x, y + NODE_H, new Color(14, 22, 44));
        g2.setPaint(bg);
        g2.fillRoundRect(x, y, nw, NODE_H, ARC, ARC);
        g2.setColor(nodeHL ? new Color(255, 200, 0) : C_BORDER);
        g2.setStroke(new BasicStroke(nodeHL ? 2f : 1.3f));
        g2.drawRoundRect(x, y, nw, NODE_H, ARC, ARC);

        Font kf = new Font("Consolas", Font.BOLD, 11);
        g2.setFont(kf);
        FontMetrics fm = g2.getFontMetrics();

        for (int i = 0; i < n.count; i++) {
            Vuelo v = n.keys.get(i);
            if (v == null) continue;
            int kx = x + 8 + i * KEY_W;
            int kw = KEY_W - 4;

            // key background by state / highlight
            Color keyBg = null;
            Color keyFg = C_TEXT;
            if (highlightCodigo != null && v.getCodigo().equals(highlightCodigo)) {
                keyBg = C_FIND; keyFg = new Color(10, 10, 10);
            } else if (rangeHL.contains(v.getCodigo())) {
                keyBg = C_RANGE; keyFg = new Color(10, 10, 10);
            } else {
                switch (v.getEstado()) {
                    case CANCELADO:  keyBg = new Color(60, 18, 18); keyFg = C_CANCEL; break;
                    case EMBARCANDO: keyBg = new Color(12, 48, 28); keyFg = C_BOARD;  break;
                    case RETRASADO:  keyBg = new Color(55, 35, 10); keyFg = C_DELAY;  break;
                    default: break;
                }
            }
            if (keyBg != null) {
                g2.setColor(keyBg);
                g2.fillRoundRect(kx, y + 2, kw, NODE_H - 4, 5, 5);
            }

            // flight code (top)
            g2.setColor(keyFg);
            String cod = v.getCodigo();
            g2.drawString(cod, kx + (kw - fm.stringWidth(cod)) / 2, y + 18);

            // destination (bottom, smaller)
            Font sf = new Font("Segoe UI", Font.PLAIN, 9);
            g2.setFont(sf);
            FontMetrics sfm = g2.getFontMetrics();
            String dest = "→ " + v.getDestino();
            if (dest.length() > 10) dest = dest.substring(0, 10);
            g2.setColor(keyFg.equals(C_TEXT) ? new Color(100, 140, 200) : keyFg);
            g2.drawString(dest, kx + (kw - sfm.stringWidth(dest)) / 2, y + NODE_H - 8);
            g2.setFont(kf);

            // divider
            if (i < n.count - 1) {
                g2.setColor(C_DIV);
                g2.drawLine(kx + KEY_W, y + 7, kx + KEY_W, y + NODE_H - 7);
            }
        }
        for (int i = 0; i <= n.count; i++) drawNodes(g2, n.childs.get(i), pos);
    }
}
