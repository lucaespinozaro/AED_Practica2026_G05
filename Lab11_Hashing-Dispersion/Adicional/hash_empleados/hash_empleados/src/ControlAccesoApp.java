import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class ControlAccesoApp extends JFrame {

    // ── Paleta ────────────────────────────────────────────────
    private static final Color BG       = new Color(10, 16, 30);
    private static final Color PANEL    = new Color(15, 22, 42);
    private static final Color CARD     = new Color(20, 32, 58);
    private static final Color BORDER   = new Color(35, 60, 110);
    private static final Color ACCENT   = new Color(55, 125, 220);
    private static final Color GREEN    = new Color(40, 195, 115);
    private static final Color RED      = new Color(210, 65, 65);
    private static final Color YELLOW   = new Color(240, 185, 30);
    private static final Color TXT      = new Color(195, 215, 255);
    private static final Color MUTED    = new Color(95, 125, 175);

    // ── Estado ────────────────────────────────────────────────
    private HashO<Empleado> hashO = new HashO<>(11);
    private HashC<Empleado> hashC = new HashC<>(11);
    private Map<Integer, Empleado> empleados = new LinkedHashMap<>();

    private HashOPanel panelO;
    private HashCPanel panelC;
    private boolean usarCuadratico = false;

    private JTextArea  log;
    private JLabel      lblStatus, lblStatsO, lblStatsC;
    private DefaultTableModel tablaModel;
    private JTable      tabla;

    // Campos
    private JTextField fDni, fNombre, fApellido, fTurno;
    private JComboBox<String> cbArea;
    private JTextField fBuscarDni;
    private JComboBox<String> cbEstrategia;
    private JTextField fSizeInicial;

    // ─────────────────────────────────────────────────────────
    public ControlAccesoApp() {
        super("🔐  Control de Acceso de Empleados  ·  Tablas Hash (Dispersión)");
        initUI();
        loadDemo();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1340, 840));
        setVisible(true);
    }

    // ══════════════════════════════════════════════════════════
    //  UI
    // ══════════════════════════════════════════════════════════
    private void initUI() {
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());
        add(header(), BorderLayout.NORTH);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel(), rightPanel());
        split.setDividerLocation(340);
        split.setDividerSize(3);
        split.setBackground(BG); split.setBorder(null);
        add(split, BorderLayout.CENTER);
        add(statusBar(), BorderLayout.SOUTH);
    }

    private JPanel header() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(PANEL);
        p.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT));

        JLabel title = new JLabel("  🔐  Control de Acceso de Empleados  ·  DNI como clave hash");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(TXT);
        title.setBorder(new EmptyBorder(13, 18, 13, 0));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        right.setOpaque(false);

        JLabel lSize = lbl("Tamaño inicial:");
        fSizeInicial = field(); fSizeInicial.setText("11");
        fSizeInicial.setPreferredSize(new Dimension(50, 26));
        fSizeInicial.setMaximumSize(new Dimension(50, 26));
        JButton btnResize = btn("Re-crear tablas", ACCENT);
        btnResize.addActionListener(e -> recrearTablas());

        JButton btnDemo = btn("⟳ Cargar demo", GREEN);
        JButton btnLimp = btn("✖ Limpiar todo", RED);
        btnDemo.addActionListener(e -> { limpiarTodo(); loadDemo(); });
        btnLimp.addActionListener(e -> limpiarTodo());

        right.add(lSize); right.add(fSizeInicial); right.add(btnResize);
        right.add(Box.createHorizontalStrut(10));
        right.add(btnDemo); right.add(btnLimp);
        right.add(Box.createHorizontalStrut(10));

        p.add(title, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    // ── Panel izquierdo ──────────────────────────────────────
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
        JPanel c = card("➕  Registrar / Dar de baja Empleado");

        fDni = field(); fNombre = field(); fApellido = field(); fTurno = field();
        cbArea = new JComboBox<>(new String[]{
            "Recursos Humanos","Sistemas","Ventas","Producción","Seguridad","Gerencia"});
        styleCombo(cbArea);

        row(c, "DNI:", fDni);
        row(c, "Nombre:", fNombre);
        row(c, "Apellido:", fApellido);
        rowCombo(c, "Área:", cbArea);
        row(c, "Turno:", fTurno);

        JPanel br = new JPanel(new GridLayout(1, 2, 6, 0)); br.setOpaque(false);
        JButton bAdd = btn("✚ Registrar", GREEN);
        JButton bDel = btn("✖ Eliminar", RED);
        bAdd.addActionListener(e -> registrar());
        bDel.addActionListener(e -> eliminar());
        br.add(bAdd); br.add(bDel);
        c.add(Box.createVerticalStrut(6)); c.add(br);
        return c;
    }

    private JPanel cardBusqueda() {
        JPanel c = card("🔍  Buscar por DNI (acceso)");
        fBuscarDni = field();
        row(c, "DNI:", fBuscarDni);
        JButton bBuscar = btn("Verificar acceso", ACCENT);
        bBuscar.addActionListener(e -> buscar());
        c.add(Box.createVerticalStrut(4)); c.add(bBuscar);

        c.add(Box.createVerticalStrut(10));
        JSeparator sep = new JSeparator(); sep.setForeground(BORDER);
        c.add(sep); c.add(Box.createVerticalStrut(8));

        JLabel lblInfo = new JLabel("<html><div style='width:260px'>Estrategia de sondeo para la tabla cerrada (HashC):</div></html>");
        lblInfo.setForeground(MUTED); lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        c.add(lblInfo);
        c.add(Box.createVerticalStrut(4));
        cbEstrategia = new JComboBox<>(new String[]{"Sondeo Lineal", "Sondeo Cuadrático"});
        styleCombo(cbEstrategia);
        cbEstrategia.addActionListener(e -> {
            usarCuadratico = cbEstrategia.getSelectedIndex() == 1;
            log("⚙ Estrategia de sondeo cambiada a: " + cbEstrategia.getSelectedItem());
        });
        c.add(cbEstrategia);

        return c;
    }

    private JPanel cardLog() {
        JPanel c = new JPanel(new BorderLayout());
        c.setBackground(CARD);
        c.setBorder(new CompoundBorder(new EmptyBorder(8, 10, 8, 10), titledBorder("📋  Bitácora de accesos")));
        log = new JTextArea(8, 26);
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

    // ── Panel derecho: pestañas con ambas tablas + tabla resumen ──
    private JPanel rightPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(CARD);
        tabs.setForeground(TXT);
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));

        // Tab 1: HashO
        panelO = new HashOPanel(hashO);
        JScrollPane scO = new JScrollPane(panelO);
        scO.getViewport().setBackground(new Color(10, 16, 30));
        scO.setBorder(null);
        JPanel tabO = new JPanel(new BorderLayout());
        tabO.setBackground(BG);
        lblStatsO = statsLabel();
        tabO.add(scO, BorderLayout.CENTER);
        tabO.add(lblStatsO, BorderLayout.SOUTH);
        tabs.addTab("🔗  Hash Abierto (Encadenamiento)", tabO);

        // Tab 2: HashC
        panelC = new HashCPanel(hashC);
        JScrollPane scC = new JScrollPane(panelC);
        scC.getViewport().setBackground(new Color(10, 16, 30));
        scC.setBorder(null);
        JPanel tabC = new JPanel(new BorderLayout());
        tabC.setBackground(BG);
        lblStatsC = statsLabel();
        tabC.add(scC, BorderLayout.CENTER);
        tabC.add(lblStatsC, BorderLayout.SOUTH);
        tabs.addTab("🧮  Hash Cerrado (Sondeo Lineal/Cuadrático)", tabC);

        // leyenda
        JPanel leyenda = buildLeyenda();

        JPanel top = new JPanel(new BorderLayout(0,4));
        top.setBackground(BG);
        top.add(tabs, BorderLayout.CENTER);
        top.add(leyenda, BorderLayout.SOUTH);

        JPanel tablaPanel = buildTablaPanel();

        JSplitPane vs = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top, tablaPanel);
        vs.setDividerLocation(440);
        vs.setDividerSize(3);
        vs.setBackground(BG); vs.setBorder(null);

        p.add(vs, BorderLayout.CENTER);
        return p;
    }

    private JLabel statsLabel() {
        JLabel l = new JLabel("  ");
        l.setForeground(MUTED);
        l.setFont(new Font("Consolas", Font.PLAIN, 12));
        l.setBorder(new EmptyBorder(4, 8, 4, 8));
        l.setOpaque(true);
        l.setBackground(PANEL);
        return l;
    }

    private JPanel buildLeyenda() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 4));
        p.setBackground(PANEL);
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, BORDER));
        p.add(chip("● Ocupado", new Color(40, 70, 130)));
        p.add(chip("● Vacío", MUTED));
        p.add(chip("● Eliminado (tumba)", new Color(170, 100, 100)));
        p.add(chip("● Posición de sondeo", new Color(255, 150, 0)));
        p.add(chip("● Encontrado / insertado", new Color(255, 210, 0)));
        p.add(chip("● Tarjeta desactivada", new Color(230, 140, 140)));
        return p;
    }

    private JLabel chip(String t, Color c) {
        JLabel l = new JLabel(t);
        l.setForeground(c); l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        return l;
    }

    private JPanel buildTablaPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(PANEL);
        p.setBorder(titledBorder("  👥  Empleados registrados"));

        String[] cols = {"DNI","Nombre","Apellido","Área","Turno","Estado"};
        tablaModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(tablaModel);
        tabla.setBackground(new Color(12, 18, 36));
        tabla.setForeground(TXT);
        tabla.setGridColor(BORDER);
        tabla.setSelectionBackground(ACCENT);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setRowHeight(24); tabla.setShowVerticalLines(false);
        JTableHeader th = tabla.getTableHeader();
        th.setBackground(CARD); th.setForeground(MUTED);
        th.setFont(new Font("Segoe UI", Font.BOLD, 12));

        tabla.getColumnModel().getColumn(5).setCellRenderer((t, val, sel, foc, row, col) -> {
            JLabel l = new JLabel(val != null ? val.toString() : "");
            l.setOpaque(true); l.setHorizontalAlignment(SwingConstants.CENTER);
            l.setFont(new Font("Segoe UI", Font.BOLD, 11));
            boolean activo = "Activa".equals(val);
            l.setBackground(sel ? ACCENT : (activo ? new Color(15, 50, 32) : new Color(55, 18, 18)));
            l.setForeground(activo ? GREEN : RED);
            return l;
        });

        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = tabla.getSelectedRow();
                if (r < 0) return;
                int dni = Integer.parseInt(tablaModel.getValueAt(r, 0).toString());
                fillForm(dni);
                fBuscarDni.setText(String.valueOf(dni));
            }
        });

        JScrollPane sc = new JScrollPane(tabla);
        sc.setBorder(null); sc.setBackground(BG);
        sc.getViewport().setBackground(new Color(12, 18, 36));
        p.add(sc, BorderLayout.CENTER);

        JPanel bRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        bRow.setOpaque(false);
        JButton bAct = btn("✔ Activar tarjeta", GREEN);
        JButton bDes = btn("✖ Desactivar tarjeta", RED);
        bAct.addActionListener(e -> cambiarActivo(true));
        bDes.addActionListener(e -> cambiarActivo(false));
        bRow.add(bAct); bRow.add(bDes);
        p.add(bRow, BorderLayout.SOUTH);
        return p;
    }

    private JPanel statusBar() {
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
    private void registrar() {
        String dniTxt = fDni.getText().trim();
        String nombre = fNombre.getText().trim();
        String apellido = fApellido.getText().trim();
        String turno = fTurno.getText().trim();

        if (dniTxt.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
            err("DNI, Nombre y Apellido son obligatorios."); return;
        }
        int dni;
        try { dni = Integer.parseInt(dniTxt); if (dni <= 0) throw new NumberFormatException(); }
        catch (NumberFormatException ex) { err("El DNI debe ser un número entero positivo."); return; }

        if (empleados.containsKey(dni)) { err("Ya existe un empleado con DNI " + dni); return; }

        Empleado.Area area = mapArea((String) cbArea.getSelectedItem());
        Empleado emp = new Empleado(dni, nombre, apellido, area, turno.isEmpty() ? "Mañana" : turno);
        empleados.put(dni, emp);

        // Insertar en ambas tablas
        int idxO = hashO.insert(new Register<>(dni, emp));
        int[] resC = hashC.insert(new Register<>(dni, emp), usarCuadratico);

        panelO.repaint(); panelO.highlight(idxO);
        animarInsertC(dni, resC[0]);

        refreshTable();
        updateStats();
        log("✅ Empleado registrado: DNI " + dni + " (" + emp.nombreCompleto() + ") → h(" + dni + ") = " + hashO.indexFor(dni) +
            " | HashC: " + resC[1] + " colisión(es)");
        status("Registrado: DNI " + dni);
        clearForm();
    }

    private void eliminar() {
        String dniTxt = fDni.getText().trim();
        if (dniTxt.isEmpty()) { err("Ingresa el DNI a eliminar."); return; }
        int dni;
        try { dni = Integer.parseInt(dniTxt); } catch (NumberFormatException ex) { err("DNI inválido."); return; }
        if (!empleados.containsKey(dni)) { err("Empleado no encontrado: DNI " + dni); return; }

        hashO.delete(dni);
        hashC.delete(dni, usarCuadratico);
        empleados.remove(dni);

        panelO.repaint(); panelO.clearHighlight();
        panelC.clearProbe(); panelC.repaint();
        refreshTable(); updateStats();
        log("🗑  Empleado eliminado: DNI " + dni);
        status("Eliminado: DNI " + dni);
        clearForm();
    }

    private void buscar() {
        String dniTxt = fBuscarDni.getText().trim();
        if (dniTxt.isEmpty()) { err("Ingresa el DNI a buscar."); return; }
        int dni;
        try { dni = Integer.parseInt(dniTxt); } catch (NumberFormatException ex) { err("DNI inválido."); return; }

        Register<Empleado> regO = hashO.search(dni);
        int idxO = hashO.indexFor(dni);
        panelO.highlight(idxO);

        if (regO != null) {
            Empleado emp = regO.getValue();
            String estado = emp.isActivo() ? "✅ ACCESO PERMITIDO" : "🚫 ACCESO DENEGADO (tarjeta desactivada)";
            log("🔍 [HashO] DNI " + dni + " encontrado en slot " + idxO + " → " + emp.nombreCompleto() + " | " + estado);
        } else {
            log("❌ [HashO] DNI " + dni + " no encontrado (calculado a slot " + idxO + ")");
        }

        // Sondeo animado en HashC
        int[] seq = hashC.probeSequence(dni, usarCuadratico);
        Register<Empleado> regC = hashC.search(dni, usarCuadratico);
        int foundIdx = (regC != null) ? seq[seq.length - 1] : -1;
        animateProbe(seq, seq.length > 0 ? seq[0] : -1, foundIdx);

        if (regC != null) {
            Empleado emp = regC.getValue();
            String estado = emp.isActivo() ? "✅ ACCESO PERMITIDO" : "🚫 ACCESO DENEGADO (tarjeta desactivada)";
            log("🔍 [HashC] DNI " + dni + " encontrado tras " + (seq.length - 1) + " colisión(es) → " + estado);
            status("Acceso: " + emp.nombreCompleto() + " — " + (emp.isActivo() ? "PERMITIDO" : "DENEGADO"));
            selectInTable(dni);
        } else {
            log("❌ [HashC] DNI " + dni + " no encontrado tras " + (seq.length) + " sondeo(s).");
            status("DNI " + dni + " no encontrado.");
        }
    }

    private void cambiarActivo(boolean activo) {
        int row = tabla.getSelectedRow();
        if (row < 0) { err("Selecciona un empleado en la tabla."); return; }
        int dni = Integer.parseInt(tablaModel.getValueAt(row, 0).toString());
        Empleado emp = empleados.get(dni);
        if (emp == null) return;
        emp.setActivo(activo);
        refreshTable(); selectInTable(dni);
        panelO.repaint(); panelC.repaint();
        log((activo ? "✔ Tarjeta activada: " : "✖ Tarjeta desactivada: ") + "DNI " + dni + " (" + emp.nombreCompleto() + ")");
        status(emp.nombreCompleto() + (activo ? " — tarjeta activada" : " — tarjeta desactivada"));
    }

    private void recrearTablas() {
        int newSize;
        try { newSize = Integer.parseInt(fSizeInicial.getText().trim()); if (newSize < 3) throw new NumberFormatException(); }
        catch (NumberFormatException ex) { err("Tamaño inválido (mínimo 3)."); return; }

        if (JOptionPane.showConfirmDialog(this,
                "Esto reconstruirá ambas tablas hash con tamaño " + newSize + ".\n¿Continuar?",
                "Re-crear tablas", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;

        hashO = new HashO<>(newSize);
        hashC = new HashC<>(newSize);
        for (Empleado emp : empleados.values()) {
            hashO.insert(new Register<>(emp.getDni(), emp));
            hashC.insert(new Register<>(emp.getDni(), emp), usarCuadratico);
        }
        panelO.setHash(hashO);
        panelC.setHash(hashC);
        updateStats();
        log("🔄 Tablas reconstruidas con tamaño " + hashO.getSize() + " (siguiente primo válido).");
        status("Tablas reconstruidas.");
    }

    private void limpiarTodo() {
        empleados.clear();
        hashO.clearTable();
        hashC.clearTable();
        panelO.repaint(); panelO.clearHighlight();
        panelC.clearProbe(); panelC.repaint();
        tablaModel.setRowCount(0);
        log.setText("");
        updateStats();
        log("🔄 Sistema limpiado.");
        status("Listo.");
    }

    // ══════════════════════════════════════════════════════════
    //  ANIMACIONES
    // ══════════════════════════════════════════════════════════
    private void animateProbe(int[] seq, int baseIndex, int foundIndex) {
        List<Integer> seqList = new ArrayList<>();
        for (int s : seq) seqList.add(s);
        Timer timer = new Timer();
        final int[] step = {0};
        timer.scheduleAtFixedRate(new java.util.TimerTask() {
            public void run() {
                if (step[0] > seqList.size()) { timer.cancel(); return; }
                final int reveal = step[0];
                SwingUtilities.invokeLater(() -> panelC.showProbe(seqList, baseIndex, foundIndex, reveal));
                step[0]++;
            }
        }, 0, 450);
    }

    private void animarInsertC(int dni, int finalIndex) {
        int[] seq = hashC.probeSequence(dni, usarCuadratico);
        animateProbe(seq, seq.length > 0 ? seq[0] : -1, finalIndex);
    }

    // ══════════════════════════════════════════════════════════
    //  HELPERS
    // ══════════════════════════════════════════════════════════
    private Empleado.Area mapArea(String s) {
        switch (s) {
            case "Recursos Humanos": return Empleado.Area.RECURSOS_HUMANOS;
            case "Sistemas":         return Empleado.Area.SISTEMAS;
            case "Ventas":           return Empleado.Area.VENTAS;
            case "Producción":       return Empleado.Area.PRODUCCION;
            case "Seguridad":        return Empleado.Area.SEGURIDAD;
            default:                 return Empleado.Area.GERENCIA;
        }
    }

    private void refreshTable() {
        tablaModel.setRowCount(0);
        List<Empleado> list = new ArrayList<>(empleados.values());
        list.sort(Comparator.comparingInt(Empleado::getDni));
        for (Empleado e : list) {
            tablaModel.addRow(new Object[]{
                e.getDni(), e.getNombre(), e.getApellido(), e.areaTexto(), e.getTurno(),
                e.isActivo() ? "Activa" : "Desactivada"
            });
        }
    }

    private void selectInTable(int dni) {
        for (int i = 0; i < tablaModel.getRowCount(); i++) {
            if (Integer.parseInt(tablaModel.getValueAt(i, 0).toString()) == dni) {
                tabla.setRowSelectionInterval(i, i);
                tabla.scrollRectToVisible(tabla.getCellRect(i, 0, true));
                return;
            }
        }
    }

    private void fillForm(int dni) {
        Empleado e = empleados.get(dni);
        if (e == null) return;
        fDni.setText(String.valueOf(e.getDni()));
        fNombre.setText(e.getNombre());
        fApellido.setText(e.getApellido());
        fTurno.setText(e.getTurno());
        cbArea.setSelectedItem(e.areaTexto());
    }

    private void clearForm() {
        fDni.setText(""); fNombre.setText(""); fApellido.setText(""); fTurno.setText("");
    }

    private void updateStats() {
        lblStatsO.setText(String.format("  Tamaño: %d   |   Elementos: %d   |   Factor de carga: %.2f",
            hashO.getSize(), hashO.getCount(), hashO.loadFactor()));
        lblStatsC.setText(String.format("  Tamaño: %d   |   Elementos: %d   |   Factor de carga: %.2f   |   Estrategia: %s",
            hashC.getSize(), hashC.getCount(), hashC.loadFactor(), usarCuadratico ? "Cuadrático" : "Lineal"));
    }

    private void log(String m) { log.append(m + "\n"); log.setCaretPosition(log.getDocument().getLength()); }
    private void status(String m) { lblStatus.setText("  " + m); }
    private void err(String m) { JOptionPane.showMessageDialog(this, m, "Error", JOptionPane.ERROR_MESSAGE); }

    // ── Datos demo ───────────────────────────────────────────
    private void loadDemo() {
        Object[][] datos = {
            {12345678, "Juan", "Pérez",    "Sistemas",          "Mañana"},
            {23456789, "María", "García",  "Recursos Humanos",  "Tarde"},
            {34567890, "Carlos", "López",  "Ventas",            "Mañana"},
            {45678901, "Ana", "Martínez",  "Producción",        "Noche"},
            {56789012, "Luis", "Sánchez",  "Seguridad",         "Mañana"},
            {67890123, "Rosa", "Torres",   "Gerencia",          "Tarde"},
            {78901234, "Pedro", "Flores",  "Sistemas",          "Noche"},
        };
        for (Object[] d : datos) {
            int dni = (int) d[0];
            Empleado emp = new Empleado(dni, (String) d[1], (String) d[2], mapArea((String) d[3]), (String) d[4]);
            empleados.put(dni, emp);
            hashO.insert(new Register<>(dni, emp));
            hashC.insert(new Register<>(dni, emp), usarCuadratico);
        }
        empleados.get(67890123).setActivo(false); // ejemplo de tarjeta desactivada
        refreshTable(); updateStats();
        panelO.repaint(); panelC.repaint();
        log("🗺  Demo cargado: " + empleados.size() + " empleados registrados (tabla tamaño " + hashO.getSize() + ").");
        status("Demo listo.");
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
        JLabel l = new JLabel(lbl);
        l.setForeground(MUTED); l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setPreferredSize(new Dimension(72, 24));
        r.add(l, BorderLayout.WEST); r.add(f, BorderLayout.CENTER);
        p.add(r); p.add(Box.createVerticalStrut(4));
    }

    private void rowCombo(JPanel p, String lbl, JComboBox<String> f) {
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
        f.setFont(new Font("Consolas", Font.PLAIN, 12));
        f.setBorder(new CompoundBorder(BorderFactory.createLineBorder(BORDER), new EmptyBorder(3, 6, 3, 6)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        return f;
    }

    private void styleCombo(JComboBox<String> cb) {
        cb.setBackground(Color.WHITE); // Fondo blanco unificado para máxima legibilidad
        cb.setForeground(Color.BLACK); // Texto negro unificado
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
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
        l.setForeground(MUTED); l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return l;
    }

    private Border titledBorder(String t) {
        return BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDER), t,
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 12), MUTED);
    }

    // ══════════════════════════════════════════════════════════
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(ControlAccesoApp::new);
    }
}