package graph;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

public class GraphPanel extends JPanel {

    // ── Colores ──────────────────────────────────────────────
    private static final Color BG          = new Color(10, 16, 30);
    private static final Color EDGE_DEF    = new Color(50, 80, 130, 180);
    private static final Color EDGE_PATH   = new Color(255, 200, 0);
    private static final Color EDGE_TRAV   = new Color(50, 200, 120);
    private static final Color NODE_DEF    = new Color(25, 45, 90);
    private static final Color NODE_BORDER = new Color(60, 120, 220);
    private static final Color NODE_START  = new Color(30, 160, 90);
    private static final Color NODE_END    = new Color(200, 60, 60);
    private static final Color NODE_VISIT  = new Color(180, 130, 20);
    private static final Color NODE_PATH   = new Color(220, 170, 0);
    private static final Color NODE_TRAV   = new Color(40, 190, 110);
    private static final Color TXT_NODE    = new Color(210, 230, 255);
    private static final Color TXT_WEIGHT  = new Color(140, 200, 255);

    private static final int R = 28; // radio del nodo

    // ── Estado ───────────────────────────────────────────────
    private GraphLink<String>   g;
    private Map<String, Point>  positions    = new LinkedHashMap<>();
    private List<String>        pathHL       = new ArrayList<>();   // ruta Dijkstra
    private List<String>        traversalHL  = new ArrayList<>();   // BFS/DFS visitados
    private String              startNode    = null;
    private String              endNode      = null;
    private String              mode         = "NONE"; // DIJKSTRA / BFS / DFS

    // Para arrastrar nodos
    private String dragging = null;
    private int    dragOffX, dragOffY;

    public GraphPanel(GraphLink<String> g) {
        this.g = g;
        setBackground(BG);
        setPreferredSize(new Dimension(900, 520));

        // Mouse drag para mover nodos
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                dragging = nodeAt(e.getX(), e.getY());
                if (dragging != null) {
                    Point p = positions.get(dragging);
                    dragOffX = e.getX() - p.x;
                    dragOffY = e.getY() - p.y;
                }
            }
            public void mouseReleased(java.awt.event.MouseEvent e) { dragging = null; }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent e) {
                if (dragging != null) {
                    positions.put(dragging, new Point(e.getX() - dragOffX, e.getY() - dragOffY));
                    repaint();
                }
            }
        });
    }

    // ── API pública ──────────────────────────────────────────
    public void setGraph(GraphLink<String> g) {
        this.g = g;
        autoLayout();
        clearHighlight();
        repaint();
    }

    public void setPositions(Map<String, Point> pos) { this.positions = pos; repaint(); }

    public void highlightPath(List<String> path, String start, String end) {
        this.pathHL      = new ArrayList<>(path);
        this.traversalHL = new ArrayList<>();
        this.startNode   = start;
        this.endNode     = end;
        this.mode        = "DIJKSTRA";
        repaint();
    }

    public void highlightTraversal(List<String> order, String start, String modeName) {
        this.traversalHL = new ArrayList<>(order);
        this.pathHL      = new ArrayList<>();
        this.startNode   = start;
        this.endNode     = null;
        this.mode        = modeName;
        repaint();
    }

    public void clearHighlight() {
        pathHL.clear(); traversalHL.clear();
        startNode = null; endNode = null; mode = "NONE";
        repaint();
    }

    public void autoLayout() {
        positions.clear();
        List<String> verts = g.getAllVertices();
        int n = verts.size();
        if (n == 0) return;
        int cx = 450, cy = 260, rx = 340, ry = 200;
        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n - Math.PI / 2;
            int x = cx + (int)(rx * Math.cos(angle));
            int y = cy + (int)(ry * Math.sin(angle));
            positions.put(verts.get(i), new Point(x, y));
        }
    }

    public Map<String, Point> getPositions() { return positions; }

    // ── Pintura ──────────────────────────────────────────────
    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g2 = (Graphics2D) g0;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawGrid(g2);
        if (g == null || g.vertexCount() == 0) { drawEmpty(g2); return; }

        drawEdges(g2);
        drawNodes(g2);
        drawModeLabel(g2);
    }

    private void drawGrid(Graphics2D g2) {
        g2.setColor(new Color(20, 32, 55, 50));
        for (int x = 0; x < getWidth();  x += 35) g2.drawLine(x, 0, x, getHeight());
        for (int y = 0; y < getHeight(); y += 35) g2.drawLine(0, y, getWidth(), y);
    }

    private void drawEmpty(Graphics2D g2) {
        g2.setColor(new Color(60, 90, 140));
        g2.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        String m = "Agrega ciudades y carreteras para visualizar la red vial";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(m, (getWidth() - fm.stringWidth(m)) / 2, getHeight() / 2);
    }

    private void drawEdges(Graphics2D g2) {
        List<String> verts = g.getAllVertices();
        Set<String> drawn = new HashSet<>();

        for (String u : verts) {
            for (String v : g.getNeighbors(u)) {
                String key = u.compareTo(v) < 0 ? u + "–" + v : v + "–" + u;
                if (drawn.contains(key)) continue;
                drawn.add(key);

                Point pu = positions.get(u);
                Point pv = positions.get(v);
                if (pu == null || pv == null) continue;

                boolean inPath = isEdgeInPath(u, v, pathHL);
                boolean inTrav = isEdgeInTraversal(u, v, traversalHL);

                Color ec = EDGE_DEF;
                float stroke = 1.8f;
                if (inPath)      { ec = EDGE_PATH; stroke = 3.2f; }
                else if (inTrav) { ec = EDGE_TRAV; stroke = 2.5f; }

                g2.setColor(ec);
                g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(pu.x, pu.y, pv.x, pv.y);

                // Peso
                int mx = (pu.x + pv.x) / 2;
                int my = (pu.y + pv.y) / 2;
                int w  = g.getWeight(u, v);
                drawWeightLabel(g2, mx, my, w + " km", inPath || inTrav);
            }
        }
    }

    private void drawWeightLabel(Graphics2D g2, int x, int y, String text, boolean hl) {
        Font f = new Font("Consolas", Font.BOLD, 11);
        g2.setFont(f);
        FontMetrics fm = g2.getFontMetrics();
        int tw = fm.stringWidth(text) + 8;
        int th = fm.getHeight();
        g2.setColor(hl ? new Color(60, 50, 10, 200) : new Color(12, 20, 38, 200));
        g2.fillRoundRect(x - tw / 2, y - th / 2 - 2, tw, th + 2, 5, 5);
        g2.setColor(hl ? new Color(255, 220, 60) : TXT_WEIGHT);
        g2.drawString(text, x - fm.stringWidth(text) / 2, y + fm.getAscent() / 2 - 1);
    }

    private void drawNodes(Graphics2D g2) {
        List<String> verts = g.getAllVertices();
        for (String v : verts) {
            Point p = positions.get(v);
            if (p == null) continue;

            Color fill   = NODE_DEF;
            Color border = NODE_BORDER;

            if (v.equals(startNode))         { fill = NODE_START; border = NODE_START.brighter(); }
            else if (v.equals(endNode))      { fill = NODE_END;   border = NODE_END.brighter(); }
            else if (pathHL.contains(v))     { fill = NODE_PATH;  border = EDGE_PATH; }
            else if (traversalHL.contains(v)){ fill = NODE_TRAV;  border = EDGE_TRAV; }

            // Glow
            if (!fill.equals(NODE_DEF)) {
                g2.setColor(new Color(fill.getRed(), fill.getGreen(), fill.getBlue(), 40));
                g2.fillOval(p.x - R - 6, p.y - R - 6, (R + 6) * 2, (R + 6) * 2);
            }

            // Fondo
            GradientPaint gp = new GradientPaint(p.x - R, p.y - R, fill.brighter(),
                                                  p.x + R, p.y + R, fill.darker());
            g2.setPaint(gp);
            g2.fillOval(p.x - R, p.y - R, R * 2, R * 2);

            // Borde
            g2.setColor(border);
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(p.x - R, p.y - R, R * 2, R * 2);

            // Texto
            g2.setColor(TXT_NODE);
            Font f = new Font("Segoe UI", Font.BOLD, 11);
            g2.setFont(f);
            FontMetrics fm = g2.getFontMetrics();
            String label = v.length() > 9 ? v.substring(0, 9) : v;
            g2.drawString(label, p.x - fm.stringWidth(label) / 2, p.y + fm.getAscent() / 2 - 2);

            // Etiqueta índice en traversal
            if (traversalHL.contains(v) && !mode.equals("NONE")) {
                int idx = traversalHL.indexOf(v) + 1;
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Consolas", Font.BOLD, 10));
                FontMetrics fm2 = g2.getFontMetrics();
                String num = String.valueOf(idx);
                g2.fillOval(p.x + R - 8, p.y - R - 4, 16, 16);
                g2.setColor(new Color(10, 10, 10));
                g2.drawString(num, p.x + R - 8 + (16 - fm2.stringWidth(num)) / 2, p.y - R - 4 + 12);
            }
        }
    }

    private void drawModeLabel(Graphics2D g2) {
        if (mode.equals("NONE")) return;
        String label;
        Color  col;
        switch (mode) {
            case "DIJKSTRA": label = "✦ Ruta más corta (Dijkstra)"; col = EDGE_PATH; break;
            case "BFS":      label = "◉ Recorrido en anchura (BFS)"; col = EDGE_TRAV; break;
            case "DFS":      label = "◎ Recorrido en profundidad (DFS)"; col = EDGE_TRAV; break;
            default:         return;
        }
        g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        FontMetrics fm = g2.getFontMetrics();
        int tw = fm.stringWidth(label) + 20;
        g2.setColor(new Color(10, 16, 30, 210));
        g2.fillRoundRect(10, 10, tw, 30, 8, 8);
        g2.setColor(col);
        g2.drawString(label, 20, 30);
    }

    // ── Helpers ──────────────────────────────────────────────
    private boolean isEdgeInPath(String u, String v, List<String> path) {
        for (int i = 0; i < path.size() - 1; i++) {
            if ((path.get(i).equals(u) && path.get(i+1).equals(v)) ||
                (path.get(i).equals(v) && path.get(i+1).equals(u))) return true;
        }
        return false;
    }

    private boolean isEdgeInTraversal(String u, String v, List<String> trav) {
        for (int i = 0; i < trav.size() - 1; i++) {
            if ((trav.get(i).equals(u) && trav.get(i+1).equals(v)) ||
                (trav.get(i).equals(v) && trav.get(i+1).equals(u))) return true;
        }
        return false;
    }

    private String nodeAt(int mx, int my) {
        for (Map.Entry<String, Point> e : positions.entrySet()) {
            Point p = e.getValue();
            if (Math.hypot(mx - p.x, my - p.y) <= R) return e.getKey();
        }
        return null;
    }
}
