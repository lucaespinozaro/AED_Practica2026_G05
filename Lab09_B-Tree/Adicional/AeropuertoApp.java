import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class AeropuertoApp extends JFrame {

    // ── Paleta ────────────────────────────────────────────────
    private static final Color BG       = new Color(10, 14, 26);
    private static final Color PANEL    = new Color(15, 22, 42);
    private static final Color CARD     = new Color(20, 30, 55);
    private static final Color BORDER   = new Color(35, 60, 110);
    private static final Color ACCENT   = new Color(50, 120, 220);
    private static final Color GREEN    = new Color(40, 200, 120);
    private static final Color RED      = new Color(210, 65, 65);
    private static final Color YELLOW   = new Color(240, 180, 30);
    private static final Color TXT      = new Color(195, 215, 255);
    private static final Color TXT_MUT  = new Color(100, 130, 175);

    // ── Estado ────────────────────────────────────────────────
    private BTree<Vuelo>            tree     = new BTree<>(5);
    private Map<String, Vuelo>      vuelos   = new LinkedHashMap<>();

    // ── UI ────────────────────────────────────────────────────
    private BTreePanel              treePanel;
    private JScrollPane             treeScroll;
    private DefaultTableModel       tablaModel;
    private JTable                  tabla;
    private JTextArea               log;
    private JLabel                  lblStatus;

    // Campos formulario
    private JTextField fCodigo, fAerolinea, fOrigen, fDestino, fHora, fPuerta;
    private JTextField fBuscar, fRangoA, fRangoB;

    // ─────────────────────────────────────────────────────────
    public AeropuertoApp() {
        super("✈  Control de Vuelos — Aeropuerto Internacional  |  B-Tree Índice");
        initUI();
        demo();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1300, 820));
        setVisible(true);
    }

    // ══════════════════════════════════════════════════════════
    //  CONSTRUCCIÓN DE UI
    // ══════════════════════════════════════════════════════════
    private void initUI() {
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());
        add(header(), BorderLayout.NORTH);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel(), rightPanel());
        split.setDividerLocation(320);
        split.setDividerSize(3);
        split.setBackground(BG); split.setBorder(null);
        add(split, BorderLayout.CENTER);
        add(statusBar(), BorderLayout.SOUTH);
    }

    // ── Header ───────────────────────────────────────────────
    private JPanel header() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(PANEL);
        p.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT));

        JLabel title = new JLabel("  ✈   Control de Vuelos  ·  Índice B-Tree");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(TXT);
        title.setBorder(new EmptyBorder(12, 18, 12, 0));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        right.setOpaque(false);

        JLabel lOrd = lbl("Orden árbol:");
        JComboBox<String> cbOrd = new JComboBox<>(new String[]{"3","4","5","6","7"});
        cbOrd.setSelectedItem("5");
        style(cbOrd);
        cbOrd.addActionListener(e -> {
            int ord = Integer.parseInt((String) cbOrd.getSelectedItem());
            if (ord == tree.getOrden()) return;
            if (JOptionPane.showConfirmDialog(this,
                    "Cambiar orden a " + ord + " reconstruirá el árbol. ¿Continuar?",
                    "Cambiar orden", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                BNode.resetCounter();
                tree = new BTree<>(ord);
                for (Vuelo v : vuelos.values()) tree.insert(v);
                treePanel.setTree(tree);
                refreshTable();
                log("🔄 Árbol reconstruido con orden " + ord);
            } else cbOrd.setSelectedItem(String.valueOf(tree.getOrden()));
        });

        JButton btnReset = btn("⟳ Reiniciar", RED);
        btnReset.addActionListener(e -> reset());

        right.add(lOrd); right.add(cbOrd);
        right.add(Box.createHorizontalStrut(8));
        right.add(btnReset);
        right.add(Box.createHorizontalStrut(10));

        p.add(title, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    // ── Panel izquierdo (formularios) ────────────────────────
    private JPanel leftPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(PANEL);
        p.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, BORDER));
        p.add(cardRegistro());
        p.add(cardBusqueda());
        p.add(cardLog());
        return p;
    }

    private JPanel cardRegistro() {
        JPanel c = card("➕  Registrar / Cancelar Vuelo");
        fCodigo    = field(); fAerolinea = field();
        fOrigen    = field(); fDestino   = field();
        fHora      = field(); fPuerta    = field();
        row(c, "Código:",    fCodigo);
        row(c, "Aerolínea:", fAerolinea);
        row(c, "Origen:",    fOrigen);
        row(c, "Destino:",   fDestino);
        row(c, "Hora:",      fHora);
        row(c, "Puerta:",    fPuerta);

        JPanel br = new JPanel(new GridLayout(1, 2, 8, 0));
        br.setOpaque(false);
        JButton bAdd = btn("✚ Registrar", GREEN);
        JButton bDel = btn("✖ Eliminar",  RED);
        bAdd.addActionListener(e -> registrar());
        bDel.addActionListener(e -> eliminar());
        br.add(bAdd); br.add(bDel);
        c.add(Box.createVerticalStrut(6)); c.add(br);
        return c;
    }

    private JPanel cardBusqueda() {
        JPanel c = card("🔍  Buscar Vuelo");
        fBuscar = field();
        row(c, "Código:", fBuscar);
        JButton bBuscar = btn("Buscar exacto", ACCENT);
        bBuscar.addActionListener(e -> buscar());
        c.add(Box.createVerticalStrut(4)); c.add(bBuscar);

        c.add(Box.createVerticalStrut(10));
        JSeparator sep = new JSeparator(); sep.setForeground(BORDER);
        c.add(sep); c.add(Box.createVerticalStrut(8));

        fRangoA = field(); fRangoB = field();
        row(c, "Desde:", fRangoA);
        row(c, "Hasta:", fRangoB);
        JButton bRango = btn("Buscar rango", YELLOW);
        bRango.addActionListener(e -> buscarRango());
        c.add(Box.createVerticalStrut(4)); c.add(bRango);
        return c;
    }

    private JPanel cardLog() {
        JPanel c = new JPanel(new BorderLayout());
        c.setBackground(CARD);
        c.setBorder(new CompoundBorder(new EmptyBorder(8, 10, 8, 10),
            titledBorder("📋  Bitácora de operaciones")));
        log = new JTextArea(7, 26);
        log.setEditable(false);
        log.setBackground(new Color(8, 12, 22));
        log.setForeground(new Color(130, 200, 140));
        log.setFont(new Font("Consolas", Font.PLAIN, 11));
        log.setLineWrap(true); log.setWrapStyleWord(true);
        log.setBorder(new EmptyBorder(6, 8, 6, 8));
        JScrollPane sc = new JScrollPane(log);
        sc.setBorder(BorderFactory.createLineBorder(BORDER));
        c.add(sc);
        return c;
    }

    // ── Panel derecho (árbol + tabla) ───────────────────────
    private JPanel rightPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(10, 10, 10, 10));

        // árbol
        treePanel  = new BTreePanel(tree);
        treeScroll = new JScrollPane(treePanel);
        treeScroll.setBackground(BG);
        treeScroll.getViewport().setBackground(new Color(10, 14, 26));
        treeScroll.setBorder(titledBorderAccent(
            "  🌳  B-Tree de vuelos  (clave = código de vuelo, ej: LA2045)"));
        treeScroll.setPreferredSize(new Dimension(900, 360));

        // leyenda
        JPanel leyenda = leyenda();

        // tabla
        JPanel tablaPanel = buildTablaPanel();

        // split vertical
        JPanel top = new JPanel(new BorderLayout(0, 4));
        top.setBackground(BG);
        top.add(treeScroll, BorderLayout.CENTER);
        top.add(leyenda, BorderLayout.SOUTH);

        JSplitPane vs = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, tablaPanel);
        vs.setDividerLocation(400);
        vs.setDividerSize(3);
        vs.setBackground(BG); vs.setBorder(null);

        p.add(vs, BorderLayout.CENTER);
        return p;
    }

    private JPanel leyenda() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 4));
        p.setBackground(PANEL);
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, BORDER));
        p.add(chip("● En horario",  TXT_MUT));
        p.add(chip("● Embarcando",  GREEN));
        p.add(chip("● Retrasado",   YELLOW));
        p.add(chip("● Cancelado",   RED));
        p.add(chip("● Encontrado",  new Color(255, 200, 0)));
        p.add(chip("● En rango",    new Color(40, 210, 130)));
        return p;
    }

    private JLabel chip(String t, Color c) {
        JLabel l = new JLabel(t);
        l.setForeground(c);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        return l;
    }

    private JPanel buildTablaPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(PANEL);
        p.setBorder(titledBorder("  ✈  Panel de salidas  (ordenado por código — recorrido inorden del B-Tree)"));

        String[] cols = {"Código","Aerolínea","Origen","Destino","Hora","Puerta","Estado"};
        tablaModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(tablaModel);
        tabla.setBackground(new Color(12, 18, 36));
        tabla.setForeground(TXT);
        tabla.setGridColor(BORDER);
        tabla.setSelectionBackground(ACCENT);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(26);
        tabla.setShowVerticalLines(false);
        JTableHeader th = tabla.getTableHeader();
        th.setBackground(CARD); th.setForeground(TXT_MUT);
        th.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // renderer estado
        tabla.getColumnModel().getColumn(6).setCellRenderer(
            (t2, val, sel, foc, row, col) -> {
                JLabel l = new JLabel(val != null ? val.toString() : "");
                l.setOpaque(true); l.setHorizontalAlignment(SwingConstants.CENTER);
                l.setFont(new Font("Segoe UI", Font.BOLD, 11));
                String s = val != null ? val.toString() : "";
                Color bg = new Color(12, 18, 36);
                Color fg = TXT_MUT;
                if (s.equals("Embarcando")) { bg = new Color(12, 45, 28); fg = GREEN; }
                else if (s.equals("Retrasado"))  { bg = new Color(50, 30, 8);  fg = YELLOW; }
                else if (s.equals("Cancelado"))  { bg = new Color(12, 45, 28); fg = GREEN; }
                else if (s.equals("Aterrizado")) { bg = new Color(20, 20, 40); fg = TXT_MUT; }
                l.setBackground(sel ? ACCENT : bg);
                l.setForeground(fg);
                return l;
            });

        // click → highlight árbol + llena form
        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = tabla.getSelectedRow();
                if (r < 0) return;
                String cod = tablaModel.getValueAt(r, 0).toString();
                treePanel.highlight(cod);
                fillForm(cod);
            }
        });

        JScrollPane sc = new JScrollPane(tabla);
        sc.setBorder(null); sc.setBackground(BG);
        sc.getViewport().setBackground(new Color(12, 18, 36));
        p.add(sc, BorderLayout.CENTER);

        // botones de estado
        JPanel bRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        bRow.setOpaque(false);
        String[] etiq = {"✔ En horario","🕐 Retrasado","🚪 Embarcando","✖ Cancelado","🛬 Aterrizado"};
        Vuelo.Estado[] ests = Vuelo.Estado.values();
        Color[] cols2 = {ACCENT, YELLOW, GREEN, RED, TXT_MUT};
        for (int i = 0; i < etiq.length; i++) {
            final Vuelo.Estado est = ests[i];
            JButton b = btn(etiq[i], cols2[i]);
            b.addActionListener(e -> cambiarEstado(est));
            bRow.add(b);
        }
        JButton bClear = btn("✖ Limpiar selección", BORDER);
        bClear.addActionListener(e -> { tabla.clearSelection(); treePanel.clearHL(); clearForm(); });
        bRow.add(bClear);
        p.add(bRow, BorderLayout.SOUTH);
        return p;
    }

    private JPanel statusBar() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBackground(PANEL);
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER));
        lblStatus = new JLabel("  Listo");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(TXT_MUT);
        p.add(lblStatus);
        return p;
    }

    // ══════════════════════════════════════════════════════════
    //  OPERACIONES
    // ══════════════════════════════════════════════════════════
    private void registrar() {
        String cod  = fCodigo.getText().trim().toUpperCase();
        String air  = fAerolinea.getText().trim();
        String org  = fOrigen.getText().trim().toUpperCase();
        String dst  = fDestino.getText().trim().toUpperCase();
        String hora = fHora.getText().trim();
        String prt  = fPuerta.getText().trim().toUpperCase();

        if (cod.isEmpty() || dst.isEmpty()) {
            err("Código y Destino son obligatorios."); return;
        }
        if (vuelos.containsKey(cod)) {
            err("El vuelo " + cod + " ya está registrado."); return;
        }
        Vuelo v = new Vuelo(cod,
            air.isEmpty()  ? "—" : air,
            org.isEmpty()  ? "—" : org,
            dst,
            hora.isEmpty() ? "--:--" : hora,
            prt.isEmpty()  ? "—" : prt);
        vuelos.put(cod, v);
        tree.insert(v);
        refreshTable();
        treePanel.highlight(cod);
        log("✅ Vuelo " + cod + " registrado → " + dst);
        status("Registrado: " + cod); clearForm();
    }

    private void eliminar() {
        String cod = fCodigo.getText().trim().toUpperCase();
        if (cod.isEmpty()) { err("Ingresa el código del vuelo a eliminar."); return; }
        if (!vuelos.containsKey(cod)) { err("Vuelo no encontrado: " + cod); return; }
        Vuelo v = vuelos.get(cod);
        tree.delete(v);
        vuelos.remove(cod);
        refreshTable();
        treePanel.clearHL();
        log("🗑  Vuelo " + cod + " eliminado del árbol.");
        status("Eliminado: " + cod); clearForm();
    }

    private void buscar() {
        String cod = fBuscar.getText().trim().toUpperCase();
        if (cod.isEmpty()) { err("Ingresa un código de vuelo."); return; }
        Vuelo key = new Vuelo(cod, "", "", "x", "", "");
        if (tree.search(key) && vuelos.containsKey(cod)) {
            Vuelo v = vuelos.get(cod);
            treePanel.highlight(cod);
            selectInTable(cod);
            log("🔍 Encontrado: " + cod + " → " + v.getDestino() + " (" + v.estadoTexto() + ")");
            status("Encontrado: " + cod);
        } else {
            treePanel.clearHL();
            log("❌ No encontrado: " + cod);
            status("No encontrado: " + cod);
        }
    }

    private void buscarRango() {
        String a = fRangoA.getText().trim().toUpperCase();
        String b = fRangoB.getText().trim().toUpperCase();
        if (a.isEmpty() || b.isEmpty()) { err("Ingresa ambos límites del rango."); return; }
        Vuelo vA = new Vuelo(a, "", "", "x", "", "");
        Vuelo vB = new Vuelo(b, "", "", "x", "", "");
        List<Vuelo> res = tree.searchRange(vA, vB);
        List<String> cods = new ArrayList<>();
        for (Vuelo v : res) cods.add(v.getCodigo());
        treePanel.highlightRange(cods);
        if (res.isEmpty()) { log("🔍 Rango [" + a + "–" + b + "]: sin resultados."); status("Sin resultados."); }
        else { log("🔍 Rango [" + a + "–" + b + "]: " + res.size() + " vuelo(s) → " + cods); status(res.size() + " vuelo(s) en rango."); }
    }

    private void cambiarEstado(Vuelo.Estado est) {
        int row = tabla.getSelectedRow();
        if (row < 0) { err("Selecciona un vuelo en la tabla."); return; }
        String cod = tablaModel.getValueAt(row, 0).toString();
        Vuelo v = vuelos.get(cod);
        if (v == null) return;
        v.setEstado(est);
        refreshTable(); selectInTable(cod);
        treePanel.highlight(cod);
        log("✏  " + cod + " → " + v.estadoTexto());
        status(cod + " : " + v.estadoTexto());
    }

    private void reset() {
        if (JOptionPane.showConfirmDialog(this,
                "¿Reiniciar todos los vuelos?", "Reiniciar",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        vuelos.clear(); BNode.resetCounter();
        tree = new BTree<>(tree.getOrden());
        treePanel.setTree(tree);
        demo();
        refreshTable(); treePanel.clearHL();
        log("🔄 Sistema reiniciado con datos de ejemplo.");
        status("Reiniciado.");
    }

    // ══════════════════════════════════════════════════════════
    //  HELPERS
    // ══════════════════════════════════════════════════════════
    private void refreshTable() {
        tablaModel.setRowCount(0);
        for (Vuelo v : tree.inorder()) {
            tablaModel.addRow(new Object[]{
                v.getCodigo(), v.getAerolinea(), v.getOrigen(),
                v.getDestino(), v.getHora(), v.getPuerta(), v.estadoTexto()
            });
        }
        treePanel.repaint();
    }

    private void selectInTable(String cod) {
        for (int i = 0; i < tablaModel.getRowCount(); i++) {
            if (tablaModel.getValueAt(i, 0).toString().equals(cod)) {
                tabla.setRowSelectionInterval(i, i);
                tabla.scrollRectToVisible(tabla.getCellRect(i, 0, true));
                return;
            }
        }
    }

    private void fillForm(String cod) {
        Vuelo v = vuelos.get(cod);
        if (v == null) return;
        fCodigo.setText(v.getCodigo());    fAerolinea.setText(v.getAerolinea());
        fOrigen.setText(v.getOrigen());    fDestino.setText(v.getDestino());
        fHora.setText(v.getHora());        fPuerta.setText(v.getPuerta());
        fBuscar.setText(v.getCodigo());
    }

    private void clearForm() {
        fCodigo.setText(""); fAerolinea.setText(""); fOrigen.setText("");
        fDestino.setText(""); fHora.setText(""); fPuerta.setText("");
    }

    private void log(String m)    { log.append(m + "\n"); log.setCaretPosition(log.getDocument().getLength()); }
    private void status(String m) { lblStatus.setText("  " + m); }
    private void err(String m)    { JOptionPane.showMessageDialog(this, m, "Error", JOptionPane.ERROR_MESSAGE); }

    // ── Datos de demostración ────────────────────────────────
    private void demo() {
        Object[][] d = {
            {"AA1100","American","MIA","LAX","06:15","A1"},
            {"IB3400","Iberia",  "MAD","BOG","08:30","B3"},
            {"LA2045","LATAM",   "LIM","SCL","09:00","C5"},
            {"LA3100","LATAM",   "BOG","LIM","10:20","C2"},
            {"AV5500","Avianca", "BOG","GRU","11:45","D4"},
            {"CM7200","Copa",    "PTY","LIM","12:00","A6"},
            {"UA8810","United",  "EWR","LIM","13:30","B1"},
            {"KL0034","KLM",     "AMS","LIM","14:10","E2"},
            {"BA0241","British", "LHR","LIM","15:50","F3"},
            {"QR0155","Qatar",   "DOH","GRU","17:30","G1"},
        };
        for (Object[] r : d) {
            Vuelo v = new Vuelo((String)r[0],(String)r[1],(String)r[2],(String)r[3],(String)r[4],(String)r[5]);
            vuelos.put(v.getCodigo(), v);
            tree.insert(v);
        }
        vuelos.get("AA1100").setEstado(Vuelo.Estado.ATERRIZADO);
        vuelos.get("IB3400").setEstado(Vuelo.Estado.EMBARCANDO);
        vuelos.get("LA2045").setEstado(Vuelo.Estado.RETRASADO);
        vuelos.get("CM7200").setEstado(Vuelo.Estado.CANCELADO);
    }

    // ── Widget helpers ───────────────────────────────────────
    private JPanel card(String title) {
        JPanel c = new JPanel();
        c.setBackground(CARD);
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.setBorder(new CompoundBorder(new EmptyBorder(8, 10, 4, 10), titledBorder(title)));
        return c;
    }

    private void row(JPanel p, String lbl, JTextField f) {
        JPanel r = new JPanel(new BorderLayout(6, 0));
        r.setOpaque(false); r.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        JLabel l = lbl(lbl); l.setPreferredSize(new Dimension(78, 24));
        r.add(l, BorderLayout.WEST); r.add(f, BorderLayout.CENTER);
        p.add(r); p.add(Box.createVerticalStrut(4));
    }

    private JTextField field() {
        JTextField f = new JTextField();
        f.setBackground(new Color(8, 14, 28)); f.setForeground(TXT);
        f.setCaretColor(TXT);
        f.setFont(new Font("Consolas", Font.PLAIN, 12));
        f.setBorder(new CompoundBorder(BorderFactory.createLineBorder(BORDER), new EmptyBorder(3,6,3,6)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        return f;
    }

    private JButton btn(String t, Color c) {
        JButton b = new JButton(t);
        b.setBackground(c);
        b.setForeground(Color.BLACK);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setBorder(new EmptyBorder(6, 12, 6, 12));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        return b;
    }

    private JLabel lbl(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(TXT_MUT); l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return l;
    }

    private void style(JComboBox<String> cb) {
        cb.setBackground(CARD); cb.setForeground(TXT);
        cb.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }

    private Border titledBorder(String t) {
        return BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDER),
            t, TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12), TXT_MUT);
    }

    private Border titledBorderAccent(String t) {
        return BorderFactory.createTitledBorder(BorderFactory.createLineBorder(ACCENT, 1),
            t, TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12), ACCENT);
    }

    // ══════════════════════════════════════════════════════════
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(AeropuertoApp::new);
    }
}