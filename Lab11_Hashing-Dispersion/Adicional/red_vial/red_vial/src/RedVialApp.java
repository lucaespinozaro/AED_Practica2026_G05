import graph.GraphLink;
import graph.GraphPanel;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class RedVialApp extends JFrame {

    // ── Paleta ────────────────────────────────────────────────
    private static final Color BG      = new Color(10, 16, 30);
    private static final Color PANEL   = new Color(15, 22, 42);
    private static final Color CARD    = new Color(20, 32, 58);
    private static final Color BORDER  = new Color(35, 60, 110);
    private static final Color ACCENT  = new Color(55, 125, 220);
    private static final Color GREEN   = new Color(40, 195, 115);
    private static final Color RED     = new Color(210, 65, 65);
    private static final Color YELLOW  = new Color(240, 185, 30);
    private static final Color TXT     = new Color(195, 215, 255);
    private static final Color MUTED   = new Color(95, 125, 175);

    // ── Estado ────────────────────────────────────────────────
    private GraphLink<String> graph = new GraphLink<>();
    private GraphPanel        gPanel;
    private JTextArea         logArea;
    private JLabel            lblStatus;
    private DefaultTableModel edgesModel;

    // Campos
    private JTextField fCiudad, fCarrA, fCarrB, fKm;
    private JTextField fDijkA, fDijkB, fTravStart;

    // ─────────────────────────────────────────────────────────
    public RedVialApp() {
        super("🚦  Red Vial Interactiva  ·  Grafos con Dijkstra / BFS / DFS");
        initUI();
        loadDemo();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1300, 820));
        setVisible(true);
    }

    // ══════════════════════════════════════════════════════════
    //  UI
    // ══════════════════════════════════════════════════════════
    private void initUI() {
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());
        add(buildHeader(), BorderLayout.NORTH);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            buildLeft(), buildRight());
        split.setDividerLocation(330);
        split.setDividerSize(3);
        split.setBackground(BG); split.setBorder(null);
        add(split, BorderLayout.CENTER);
        add(buildStatus(), BorderLayout.SOUTH);
    }

    // ── Header ───────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(PANEL);
        p.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT));

        JLabel title = new JLabel("  🚦   Red Vial Interactiva  ·  Grafos Ponderados No Dirigidos");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(TXT);
        title.setBorder(new EmptyBorder(13, 18, 13, 0));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        right.setOpaque(false);

        JButton btnDemo  = btn("⟳ Cargar demo", ACCENT);
        JButton btnLimp  = btn("✖ Limpiar todo", RED);
        JButton btnAuto  = btn("⬡ Auto-layout",  MUTED);
        btnDemo .addActionListener(e -> { clearAll(); loadDemo(); });
        btnLimp .addActionListener(e -> clearAll());
        btnAuto .addActionListener(e -> { gPanel.autoLayout(); gPanel.repaint(); });

        right.add(btnAuto); right.add(btnDemo); right.add(btnLimp);
        right.add(Box.createHorizontalStrut(8));

        p.add(title, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    // ── Panel izquierdo ──────────────────────────────────────
    private JPanel buildLeft() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(PANEL);
        p.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, BORDER));
        p.add(cardCiudad());
        p.add(cardCarretera());
        p.add(cardAlgoritmos());
        p.add(cardLog());
        return p;
    }

    private JPanel cardCiudad() {
        JPanel c = card("🏙  Ciudad (Vértice)");
        fCiudad = field();
        row(c, "Ciudad:", fCiudad);
        JPanel br = new JPanel(new GridLayout(1, 2, 6, 0)); br.setOpaque(false);
        JButton bAdd = btn("✚ Agregar", GREEN);
        JButton bDel = btn("✖ Eliminar", RED);
        bAdd.addActionListener(e -> agregarCiudad());
        bDel.addActionListener(e -> eliminarCiudad());
        br.add(bAdd); br.add(bDel);
        c.add(Box.createVerticalStrut(6)); c.add(br);
        return c;
    }

    private JPanel cardCarretera() {
        JPanel c = card("🛣  Carretera (Arista Ponderada)");
        fCarrA = field(); fCarrB = field(); fKm = field();
        row(c, "Ciudad A:", fCarrA);
        row(c, "Ciudad B:", fCarrB);
        row(c, "Km:", fKm);
        JPanel br = new JPanel(new GridLayout(1, 2, 6, 0)); br.setOpaque(false);
        JButton bAdd = btn("✚ Agregar", GREEN);
        JButton bDel = btn("✖ Eliminar", RED);
        bAdd.addActionListener(e -> agregarCarretera());
        bDel.addActionListener(e -> eliminarCarretera());
        br.add(bAdd); br.add(bDel);
        c.add(Box.createVerticalStrut(6)); c.add(br);
        return c;
    }

    private JPanel cardAlgoritmos() {
        JPanel c = card("⚙  Algoritmos");

        // Dijkstra
        fDijkA = field(); fDijkB = field();
        row(c, "Origen:",  fDijkA);
        row(c, "Destino:", fDijkB);
        JButton bDijk = btn("✦ Ruta más corta (Dijkstra)", YELLOW);
        bDijk.setForeground(new Color(10,10,10));
        bDijk.addActionListener(e -> runDijkstra());
        c.add(Box.createVerticalStrut(3)); c.add(bDijk);

        c.add(Box.createVerticalStrut(10));
        JSeparator sep = new JSeparator(); sep.setForeground(BORDER);
        c.add(sep); c.add(Box.createVerticalStrut(8));

        // BFS / DFS
        fTravStart = field();
        row(c, "Inicio:", fTravStart);
        JPanel br2 = new JPanel(new GridLayout(1, 2, 6, 0)); br2.setOpaque(false);
        JButton bBFS = btn("◉ BFS", GREEN);
        JButton bDFS = btn("◎ DFS", ACCENT);
        bBFS.addActionListener(e -> runTraversal("BFS"));
        bDFS.addActionListener(e -> runTraversal("DFS"));
        br2.add(bBFS); br2.add(bDFS);
        c.add(Box.createVerticalStrut(3)); c.add(br2);

        c.add(Box.createVerticalStrut(8));
        JButton bConexo = btn("🔗 ¿Es conexo?", MUTED);
        bConexo.addActionListener(e -> checkConexo());
        c.add(bConexo);

        JButton bClear = btn("✖ Limpiar resaltado", new Color(40, 50, 80));
        bClear.addActionListener(e -> { gPanel.clearHighlight(); status("Resaltado limpiado."); });
        c.add(Box.createVerticalStrut(4)); c.add(bClear);

        return c;
    }

    private JPanel cardLog() {
        JPanel c = new JPanel(new BorderLayout());
        c.setBackground(CARD);
        c.setBorder(new CompoundBorder(new EmptyBorder(8, 10, 8, 10),
            titledBorder("📋  Bitácora")));
        logArea = new JTextArea(6, 26);
        logArea.setEditable(false);
        logArea.setBackground(new Color(8, 12, 22));
        logArea.setForeground(new Color(130, 200, 140));
        logArea.setFont(new Font("Consolas", Font.PLAIN, 11));
        logArea.setLineWrap(true); logArea.setWrapStyleWord(true);
        logArea.setBorder(new EmptyBorder(6, 8, 6, 8));
        JScrollPane sc = new JScrollPane(logArea);
        sc.setBorder(BorderFactory.createLineBorder(BORDER));
        c.add(sc);
        return c;
    }

    // ── Panel derecho ────────────────────────────────────────
    private JPanel buildRight() {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(10, 10, 10, 10));

        gPanel = new GraphPanel(graph);
        JScrollPane sc = new JScrollPane(gPanel);
        sc.setBorder(titledBorderAccent(
            "  🗺  Mapa de la Red Vial  (arrastra las ciudades para reorganizar)"));
        sc.setBackground(BG);
        sc.getViewport().setBackground(new Color(10, 16, 30));

        // Leyenda
        JPanel ley = buildLeyenda();

        // Tabla de aristas
        JPanel tab = buildTablaAristas();

        JSplitPane vs = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
            wrapTop(sc, ley), tab);
        vs.setDividerLocation(420);
        vs.setDividerSize(3);
        vs.setBackground(BG); vs.setBorder(null);

        p.add(vs, BorderLayout.CENTER);
        return p;
    }

    private JPanel wrapTop(JComponent graph, JComponent ley) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBackground(BG);
        p.add(graph, BorderLayout.CENTER);
        p.add(ley,   BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildLeyenda() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 5));
        p.setBackground(PANEL);
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, BORDER));
        p.add(chip("● Ciudad normal",      new Color(60, 120, 220)));
        p.add(chip("● Ciudad origen",      new Color(30, 160, 90)));
        p.add(chip("● Ciudad destino",     new Color(200, 60, 60)));
        p.add(chip("● En ruta / visitado", new Color(220, 170, 0)));
        p.add(chip("── Carretera normal",  new Color(50, 80, 130)));
        p.add(chip("── Ruta más corta",    new Color(255, 200, 0)));
        p.add(chip("── BFS / DFS",         new Color(50, 200, 120)));
        return p;
    }

    private JLabel chip(String t, Color c) {
        JLabel l = new JLabel(t);
        l.setForeground(c); l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        return l;
    }

    private JPanel buildTablaAristas() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(PANEL);
        p.setBorder(titledBorder("  📊  Carreteras registradas  (recorrido de aristas del grafo)"));

        edgesModel = new DefaultTableModel(
            new String[]{"Ciudad A", "Ciudad B", "Distancia (km)"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabla = new JTable(edgesModel);
        tabla.setBackground(new Color(12, 18, 36));
        tabla.setForeground(TXT);
        tabla.setGridColor(BORDER);
        tabla.setSelectionBackground(ACCENT);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(24); tabla.setShowVerticalLines(false);
        JTableHeader th = tabla.getTableHeader();
        th.setBackground(CARD); th.setForeground(MUTED);
        th.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane sc = new JScrollPane(tabla);
        sc.setBorder(null); sc.setBackground(BG);
        sc.getViewport().setBackground(new Color(12, 18, 36));
        p.add(sc, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildStatus() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBackground(PANEL);
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER));
        lblStatus = new JLabel("  Listo");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(MUTED);
        p.add(lblStatus);
        return p;
    }

    // ══════════════════════════════════════════════════════════
    //  OPERACIONES
    // ══════════════════════════════════════════════════════════
    private void agregarCiudad() {
        String c = fCiudad.getText().trim();
        if (c.isEmpty()) { err("Ingresa el nombre de la ciudad."); return; }
        if (graph.searchVertex(c)) { err("La ciudad '" + c + "' ya existe."); return; }
        graph.insertVertex(c);
        gPanel.autoLayout();
        gPanel.clearHighlight();
        log("🏙 Ciudad agregada: " + c);
        status("Ciudad: " + c + " agregada.");
        fCiudad.setText("");
        refreshEdgeTable();
    }

    private void eliminarCiudad() {
        String c = fCiudad.getText().trim();
        if (c.isEmpty()) { err("Ingresa el nombre de la ciudad a eliminar."); return; }
        if (!graph.searchVertex(c)) { err("Ciudad no encontrada: " + c); return; }
        graph.removeVertex(c);
        gPanel.autoLayout();
        gPanel.clearHighlight();
        log("🗑  Ciudad eliminada: " + c);
        status("Ciudad eliminada: " + c);
        fCiudad.setText("");
        refreshEdgeTable();
    }

    private void agregarCarretera() {
        String a = fCarrA.getText().trim();
        String b = fCarrB.getText().trim();
        String km = fKm.getText().trim();
        if (a.isEmpty() || b.isEmpty() || km.isEmpty()) { err("Rellena Ciudad A, Ciudad B y Km."); return; }
        int dist;
        try { dist = Integer.parseInt(km); if (dist <= 0) throw new NumberFormatException(); }
        catch (NumberFormatException ex) { err("Los kilómetros deben ser un número entero positivo."); return; }
        if (!graph.searchVertex(a)) { err("Ciudad no encontrada: " + a); return; }
        if (!graph.searchVertex(b)) { err("Ciudad no encontrada: " + b); return; }
        if (graph.searchEdge(a, b)) { err("Ya existe una carretera entre " + a + " y " + b + "."); return; }
        graph.insertEdgeWeight(a, b, dist);
        gPanel.clearHighlight();
        gPanel.repaint();
        log("🛣  Carretera: " + a + " ↔ " + b + " (" + dist + " km)");
        status("Carretera " + a + "↔" + b + " agregada.");
        fCarrA.setText(""); fCarrB.setText(""); fKm.setText("");
        refreshEdgeTable();
    }

    private void eliminarCarretera() {
        String a = fCarrA.getText().trim();
        String b = fCarrB.getText().trim();
        if (a.isEmpty() || b.isEmpty()) { err("Ingresa Ciudad A y Ciudad B."); return; }
        if (!graph.searchEdge(a, b)) { err("No existe carretera entre " + a + " y " + b + "."); return; }
        graph.removeEdge(a, b);
        gPanel.clearHighlight();
        gPanel.repaint();
        log("🗑  Carretera eliminada: " + a + " ↔ " + b);
        status("Carretera eliminada: " + a + "↔" + b);
        fCarrA.setText(""); fCarrB.setText("");
        refreshEdgeTable();
    }

    private void runDijkstra() {
        String a = fDijkA.getText().trim();
        String b = fDijkB.getText().trim();
        if (a.isEmpty() || b.isEmpty()) { err("Ingresa origen y destino."); return; }
        if (!graph.searchVertex(a)) { err("Ciudad no encontrada: " + a); return; }
        if (!graph.searchVertex(b)) { err("Ciudad no encontrada: " + b); return; }
        if (a.equals(b)) { err("Origen y destino son la misma ciudad."); return; }

        List<String> path = graph.dijkstra(a, b);
        if (path.isEmpty()) {
            gPanel.clearHighlight();
            log("❌ No existe ruta entre " + a + " y " + b);
            status("Sin ruta entre " + a + " y " + b + ".");
            return;
        }
        int cost = graph.pathCost(path);
        gPanel.highlightPath(path, a, b);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < path.size(); i++) {
            sb.append(path.get(i));
            if (i < path.size() - 1) sb.append(" → ");
        }
        log("✦ Dijkstra [" + a + "→" + b + "]: " + sb + " | " + cost + " km");
        status("Ruta más corta: " + cost + " km  (" + path.size() + " ciudades)");

        // Animación paso a paso
        animatePath(path, a, b);
    }

    private void animatePath(List<String> path, String start, String end) {
        Timer timer = new Timer();
        final int[] step = {0};
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (step[0] > path.size()) { timer.cancel(); return; }
                List<String> partial = path.subList(0, step[0]);
                SwingUtilities.invokeLater(() ->
                    gPanel.highlightPath(new ArrayList<>(partial), start, end));
                step[0]++;
            }
        }, 0, 400);
    }

    private void runTraversal(String mode) {
        String start = fTravStart.getText().trim();
        if (start.isEmpty()) { err("Ingresa la ciudad de inicio."); return; }
        if (!graph.searchVertex(start)) { err("Ciudad no encontrada: " + start); return; }

        List<String> order = mode.equals("BFS") ? graph.bfs(start) : graph.dfs(start);
        gPanel.highlightTraversal(new ArrayList<>(), start, mode);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < order.size(); i++) {
            sb.append(order.get(i));
            if (i < order.size() - 1) sb.append(" → ");
        }
        log((mode.equals("BFS") ? "◉" : "◎") + " " + mode + " desde " + start + ": " + sb);
        status(mode + " desde " + start + ": " + order.size() + " ciudades visitadas.");

        // Animación
        Timer timer = new Timer();
        final int[] step = {0};
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (step[0] > order.size()) { timer.cancel(); return; }
                List<String> partial = order.subList(0, step[0]);
                SwingUtilities.invokeLater(() ->
                    gPanel.highlightTraversal(new ArrayList<>(partial), start, mode));
                step[0]++;
            }
        }, 0, 500);
    }

    private void checkConexo() {
        boolean conexo = graph.isConexo();
        String msg = conexo
            ? "✅ El grafo ES CONEXO — desde cualquier ciudad se puede llegar a todas las demás."
            : "❌ El grafo NO ES CONEXO — existen ciudades aisladas o grupos desconectados.";
        log(msg);
        status(conexo ? "Grafo conexo ✓" : "Grafo NO conexo ✗");
        JOptionPane.showMessageDialog(this, msg,
            "Verificación de Conectividad",
            conexo ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
    }

    // ══════════════════════════════════════════════════════════
    //  DATOS DEMO
    // ══════════════════════════════════════════════════════════
    private void loadDemo() {
        String[] cities = {"Arequipa","Cusco","Puno","Tacna","Moquegua","Lima","Juliaca","Abancay"};
        for (String c : cities) graph.insertVertex(c);

        int[][] roads = {
            // idx A, idx B, km
        };
        graph.insertEdgeWeight("Arequipa", "Cusco",    510);
        graph.insertEdgeWeight("Arequipa", "Moquegua", 230);
        graph.insertEdgeWeight("Arequipa", "Juliaca",  280);
        graph.insertEdgeWeight("Moquegua", "Tacna",    160);
        graph.insertEdgeWeight("Cusco",    "Puno",     390);
        graph.insertEdgeWeight("Cusco",    "Abancay",  195);
        graph.insertEdgeWeight("Puno",     "Juliaca",   44);
        graph.insertEdgeWeight("Puno",     "Tacna",    420);
        graph.insertEdgeWeight("Lima",     "Abancay",  595);
        graph.insertEdgeWeight("Lima",     "Cusco",    1100);

        // Posiciones fijas aproximadas al mapa de Perú
        Map<String, java.awt.Point> pos = new LinkedHashMap<>();
        pos.put("Lima",     new java.awt.Point(180, 155));
        pos.put("Abancay",  new java.awt.Point(360, 230));
        pos.put("Cusco",    new java.awt.Point(480, 255));
        pos.put("Arequipa", new java.awt.Point(390, 370));
        pos.put("Juliaca",  new java.awt.Point(510, 335));
        pos.put("Puno",     new java.awt.Point(555, 365));
        pos.put("Moquegua", new java.awt.Point(360, 430));
        pos.put("Tacna",    new java.awt.Point(380, 490));
        gPanel.setPositions(pos);

        refreshEdgeTable();
        log("🗺  Demo cargado: " + cities.length + " ciudades, 10 carreteras del sur del Perú.");
        status("Demo listo. " + graph.vertexCount() + " ciudades, " + graph.edgeCount() + " carreteras.");
    }

    private void clearAll() {
        graph = new GraphLink<>();
        gPanel.setGraph(graph);
        edgesModel.setRowCount(0);
        logArea.setText("");
        log("🔄 Todo limpiado.");
        status("Listo.");
    }

    private void refreshEdgeTable() {
        edgesModel.setRowCount(0);
        List<String> verts = graph.getAllVertices();
        Set<String> seen = new HashSet<>();
        for (String u : verts) {
            for (String v : graph.getNeighbors(u)) {
                String key = u.compareTo(v) < 0 ? u + "–" + v : v + "–" + u;
                if (seen.contains(key)) continue;
                seen.add(key);
                edgesModel.addRow(new Object[]{u, v, graph.getWeight(u, v) + " km"});
            }
        }
    }

    // ══════════════════════════════════════════════════════════
    //  HELPERS
    // ══════════════════════════════════════════════════════════
    private void log(String m) {
        logArea.append(m + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
    private void status(String m) { lblStatus.setText("  " + m); }
    private void err(String m)    { JOptionPane.showMessageDialog(this, m, "Error", JOptionPane.ERROR_MESSAGE); }

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
        JLabel l = new JLabel(lbl);
        l.setForeground(MUTED); l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setPreferredSize(new Dimension(72, 24));
        r.add(l, BorderLayout.WEST); r.add(f, BorderLayout.CENTER);
        p.add(r); p.add(Box.createVerticalStrut(4));
    }

    private JTextField field() {
        JTextField f = new JTextField();
        f.setBackground(new Color(8, 14, 28)); f.setForeground(TXT);
        f.setCaretColor(TXT);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        f.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(BORDER), new EmptyBorder(3, 6, 3, 6)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        return f;
    }

    private JButton btn(String t, Color c) {
        JButton b = new JButton(t);
        b.setBackground(c); b.setForeground(Color.BLACK);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setBorder(new EmptyBorder(6, 12, 6, 12));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        return b;
    }

    private Border titledBorder(String t) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER), t,
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12), MUTED);
    }

    private Border titledBorderAccent(String t) {
        return BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(ACCENT, 1), t,
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12), ACCENT);
    }

    // ══════════════════════════════════════════════════════════
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(RedVialApp::new);
    }
}