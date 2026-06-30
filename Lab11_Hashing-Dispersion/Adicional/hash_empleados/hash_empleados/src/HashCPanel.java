import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/** Visualiza la tabla Hash cerrada (HashC) como una grilla de slots, animando el sondeo. */
public class HashCPanel extends JPanel {

    private static final Color BG          = new Color(10, 16, 30);
    private static final Color SLOT_EMPTY  = new Color(16, 24, 44);
    private static final Color SLOT_OCC    = new Color(22, 44, 80);
    private static final Color SLOT_DEL    = new Color(50, 30, 30);
    private static final Color SLOT_BORDER = new Color(45, 80, 140);
    private static final Color SLOT_PROBE  = new Color(255, 150, 0);
    private static final Color SLOT_FOUND  = new Color(255, 210, 0);
    private static final Color SLOT_BASE   = new Color(40, 200, 120);
    private static final Color TXT         = new Color(195, 220, 255);
    private static final Color TXT_MUT     = new Color(90, 120, 165);

    private static final int CELL_W = 110;
    private static final int CELL_H = 56;
    private static final int COLS_MAX = 6;

    private HashC<Empleado> hash;
    private List<Integer> probeSeq = new ArrayList<>();
    private int probeStep = -1; // hasta qué índice de probeSeq mostrar (para animar)
    private int baseIndex = -1;
    private int foundIndex = -1;

    public HashCPanel(HashC<Empleado> hash) {
        this.hash = hash;
        setBackground(BG);
        setPreferredSize(new Dimension(820, 420));
    }

    public void setHash(HashC<Empleado> h) { this.hash = h; clearProbe(); }

    public void showProbe(List<Integer> seq, int baseIndex, int foundIndex, int revealCount) {
        this.probeSeq = seq;
        this.baseIndex = baseIndex;
        this.foundIndex = foundIndex;
        this.probeStep = revealCount;
        repaint();
    }

    public void clearProbe() {
        probeSeq = new ArrayList<>();
        probeStep = -1; baseIndex = -1; foundIndex = -1;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int size = hash.getSize();
        int cols = Math.min(COLS_MAX, Math.max(1, getWidth() / CELL_W));
        if (cols < 1) cols = 1;
        int rows = (int) Math.ceil((double) size / cols);

        Dimension pref = new Dimension(cols * CELL_W + 40, rows * CELL_H + 50);
        if (!pref.equals(getPreferredSize())) { setPreferredSize(pref); revalidate(); }

        for (int i = 0; i < size; i++) {
            int col = i % cols, row = i / cols;
            int x = 20 + col * CELL_W;
            int y = 30 + row * CELL_H;

            HashC.Element<Empleado> el = hash.getElement(i);

            Color fill = SLOT_EMPTY;
            Color border = SLOT_BORDER;
            if (el.mark == 1) fill = SLOT_OCC;
            else if (el.mark == -1) fill = SLOT_DEL;

            boolean isBase  = (i == baseIndex);
            boolean inProbe = probeStep >= 0 && probeSeq.indexOf(i) >= 0 && probeSeq.indexOf(i) < probeStep;
            boolean isFound = (i == foundIndex) && probeStep >= probeSeq.size();

            if (isFound)      { fill = SLOT_FOUND; border = SLOT_FOUND; }
            else if (inProbe) { fill = SLOT_PROBE; border = SLOT_PROBE; }
            else if (isBase)  { border = SLOT_BASE; }

            g2.setColor(fill);
            g2.fillRoundRect(x, y, CELL_W - 8, CELL_H - 10, 8, 8);
            g2.setColor(border);
            g2.setStroke(new BasicStroke((inProbe || isFound || isBase) ? 2.4f : 1.2f));
            g2.drawRoundRect(x, y, CELL_W - 8, CELL_H - 10, 8, 8);

            // índice
            g2.setFont(new Font("Consolas", Font.PLAIN, 9));
            g2.setColor(TXT_MUT);
            g2.drawString("[" + i + "]", x + 5, y + 11);

            g2.setFont(new Font("Consolas", Font.BOLD, 11));
            if (el.mark == 1) {
                g2.setColor(el.register.getValue().isActivo() ? TXT : new Color(230, 150, 150));
                String l1 = "DNI " + el.register.getKey();
                g2.drawString(l1, x + 6, y + 27);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
                String nom = el.register.getValue().nombreCompleto();
                if (nom.length() > 14) nom = nom.substring(0, 14);
                g2.drawString(nom, x + 6, y + 41);
            } else if (el.mark == -1) {
                g2.setColor(new Color(170, 100, 100));
                g2.drawString("eliminado", x + 6, y + 30);
            } else {
                g2.setColor(TXT_MUT);
                g2.setFont(new Font("Segoe UI", Font.ITALIC, 10));
                g2.drawString("vacío", x + 6, y + 30);
            }
        }

        // leyenda de pasos de sondeo (numeritos) si hay animación activa
        if (probeStep >= 0 && !probeSeq.isEmpty()) {
            for (int s = 0; s < Math.min(probeStep, probeSeq.size()); s++) {
                int idx = probeSeq.get(s);
                int col = idx % cols, row = idx / cols;
                int x = 20 + col * CELL_W + (CELL_W - 8) - 16;
                int y = 30 + row * CELL_H + 2;
                g2.setColor(new Color(255, 255, 255, 220));
                g2.fillOval(x, y, 16, 16);
                g2.setColor(new Color(20, 20, 20));
                g2.setFont(new Font("Consolas", Font.BOLD, 9));
                String num = String.valueOf(s + 1);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(num, x + (16 - fm.stringWidth(num)) / 2, y + 12);
            }
        }
    }
}
