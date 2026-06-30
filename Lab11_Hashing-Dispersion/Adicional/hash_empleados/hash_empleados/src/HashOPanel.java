import javax.swing.*;
import java.awt.*;
import java.util.*;

/** Visualiza la tabla Hash con encadenamiento (HashO): cada slot dibuja su cadena. */
public class HashOPanel extends JPanel {

    private static final Color BG       = new Color(10, 16, 30);
    private static final Color SLOT_BG  = new Color(18, 28, 52);
    private static final Color SLOT_BD  = new Color(45, 80, 140);
    private static final Color SLOT_HL  = new Color(255, 200, 0);
    private static final Color TXT      = new Color(190, 215, 255);
    private static final Color TXT_MUT  = new Color(90, 120, 165);
    private static final Color NODE_BG  = new Color(28, 48, 90);
    private static final Color NODE_INACTIVE = new Color(70, 40, 40);
    private static final Color ARROW    = new Color(60, 110, 190);

    private static final int SLOT_W = 70;
    private static final int SLOT_H = 40;
    private static final int NODE_W = 130;
    private static final int NODE_H = 40;
    private static final int GAP_X  = 18;

    private HashO<Empleado> hash;
    private int highlightIndex = -1;

    public HashOPanel(HashO<Empleado> hash) {
        this.hash = hash;
        setBackground(BG);
        setPreferredSize(new Dimension(900, 520));
    }

    public void setHash(HashO<Empleado> h) { this.hash = h; repaint(); }
    public void highlight(int index) { this.highlightIndex = index; repaint(); }
    public void clearHighlight() { this.highlightIndex = -1; repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int size = hash.getSize();
        int y0 = 20;

        // Ajustar tamaño preferido dinámicamente
        int maxChain = 1;
        for (int i = 0; i < size; i++) maxChain = Math.max(maxChain, hash.chainSize(i));
        int neededH = y0 + size * (SLOT_H + 14) + 20;
        int neededW = 130 + maxChain * (NODE_W + GAP_X) + 40;
        Dimension pref = new Dimension(Math.max(700, neededW), Math.max(500, neededH));
        if (!pref.equals(getPreferredSize())) {
            setPreferredSize(pref);
            revalidate();
        }

        for (int i = 0; i < size; i++) {
            int y = y0 + i * (SLOT_H + 14);
            boolean hl = (i == highlightIndex);

            // índice / slot
            g2.setColor(hl ? SLOT_HL.darker() : SLOT_BG);
            g2.fillRoundRect(20, y, SLOT_W, SLOT_H, 8, 8);
            g2.setColor(hl ? SLOT_HL : SLOT_BD);
            g2.setStroke(new BasicStroke(hl ? 2.2f : 1.3f));
            g2.drawRoundRect(20, y, SLOT_W, SLOT_H, 8, 8);

            g2.setFont(new Font("Consolas", Font.BOLD, 14));
            g2.setColor(hl ? Color.BLACK : TXT);
            String idx = String.valueOf(i);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(idx, 20 + (SLOT_W - fm.stringWidth(idx)) / 2, y + SLOT_H / 2 + 5);

            // cadena
            int x = 20 + SLOT_W + 20;
            int chainLen = hash.chainSize(i);
            if (chainLen == 0) {
                g2.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                g2.setColor(TXT_MUT);
                g2.drawString("(vacío)", x, y + SLOT_H / 2 + 5);
                continue;
            }

            ListLinked.Node<Register<Empleado>> node = hash.getChain(i).getFirstNode();
            int idxInChain = 0;
            int prevX = 20 + SLOT_W;
            int prevY = y + SLOT_H / 2;
            while (node != null) {
                Empleado emp = node.dato.getValue();
                boolean active = emp.isActivo();

                // flecha desde anterior
                g2.setColor(ARROW);
                g2.setStroke(new BasicStroke(1.6f));
                g2.drawLine(prevX, prevY, x, y + NODE_H / 2);
                // punta de flecha
                g2.fillPolygon(new int[]{x, x - 7, x - 7}, new int[]{y + NODE_H / 2, y + NODE_H / 2 - 4, y + NODE_H / 2 + 4}, 3);

                Color fillC = active ? NODE_BG : NODE_INACTIVE;
                Color bordC = hl ? SLOT_HL : SLOT_BD;
                g2.setColor(fillC);
                g2.fillRoundRect(x, y, NODE_W, NODE_H, 8, 8);
                g2.setColor(bordC);
                g2.setStroke(new BasicStroke(1.4f));
                g2.drawRoundRect(x, y, NODE_W, NODE_H, 8, 8);

                g2.setFont(new Font("Consolas", Font.BOLD, 11));
                g2.setColor(active ? TXT : new Color(230, 140, 140));
                String l1 = "DNI " + emp.getDni();
                g2.drawString(l1, x + 8, y + 16);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                String l2 = emp.nombreCompleto();
                if (l2.length() > 18) l2 = l2.substring(0, 18);
                g2.drawString(l2, x + 8, y + 31);

                prevX = x + NODE_W;
                prevY = y + NODE_H / 2;
                x += NODE_W + GAP_X;
                idxInChain++;
                node = node.next;
            }

            // null final
            g2.setColor(ARROW);
            g2.drawLine(prevX, prevY, x, y + NODE_H / 2);
            g2.setFont(new Font("Consolas", Font.ITALIC, 11));
            g2.setColor(TXT_MUT);
            g2.drawString("NULL", x + 4, y + NODE_H / 2 + 4);
        }
    }
}
