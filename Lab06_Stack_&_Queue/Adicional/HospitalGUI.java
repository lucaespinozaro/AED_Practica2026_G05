import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class HospitalGUI extends JFrame {
    private final ColaPrioridad<Paciente> colaEspera = new ColaPrioridad<>(3);
    private final StackLink<Paciente> historialAtenciones = new StackLink<>();

    private static final Color BG_DARK      = new Color(10, 14, 26);
    private static final Color BG_CARD      = new Color(16, 22, 40);
    private static final Color BG_INPUT     = new Color(22, 30, 52);
    private static final Color ACCENT_BLUE  = new Color(41, 121, 255);
    private static final Color ACCENT_CYAN  = new Color(0, 212, 255);
    private static final Color COLOR_ROJO   = new Color(255, 75, 75);
    private static final Color COLOR_AMARI  = new Color(255, 196, 0);
    private static final Color COLOR_VERDE  = new Color(39, 209, 89);
    private static final Color TEXT_PRIMARY = new Color(220, 230, 255);
    private static final Color TEXT_MUTED   = new Color(120, 150, 250);
    private static final Color BORDER_COLOR = new Color(35, 50, 90);

    private JTextField txtNombre, txtEdad, txtSintoma;
    private JComboBox<String> cbPrioridad;
    private JTextField txtDiagnostico, txtTratamiento;

    private DefaultTableModel modeloCola;
    private DefaultTableModel modeloHistorial;
    private JTable tablaEspera, tablaHistorial;

    private JLabel lblTotalEspera, lblRojo, lblAmarillo, lblVerde;
    private JLabel lblTotalAtendidos;
    private JTextArea areaLog;

    public HospitalGUI() {
        super("Sistema de Emergencias Hospitalarias — Triaje & Atención");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1280, 800));
        setPreferredSize(new Dimension(1440, 860));
        setBackground(BG_DARK);
        initUI();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        log("Sistema iniciado. Listo para registrar pacientes.", ACCENT_CYAN);
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BG_DARK);
        root.add(buildHeader(), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(BG_DARK);
        center.setBorder(new EmptyBorder(12, 14, 12, 14));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.insets = new Insets(0, 6, 0, 6);
        gc.gridy = 0;
        gc.weighty = 1.0;

        gc.gridx = 0; gc.weightx = 0.25;
        center.add(buildLeftPanel(), gc);

        gc.gridx = 1; gc.weightx = 0.42;
        center.add(buildQueuePanel(), gc);

        gc.gridx = 2; gc.weightx = 0.33;
        center.add(buildRightPanel(), gc);

        root.add(center, BorderLayout.CENTER);
        setContentPane(root);
    }

    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(8, 12, 22));
        p.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(12, 24, 12, 24)
        ));

        JLabel title = new JLabel("SISTEMA DE EMERGENCIAS HOSPITALARIAS");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT_PRIMARY);

        JLabel sub = new JLabel("  Stack: Historial de Atenciones  |  Priority Queue: Sala de Espera (Triaje)");
        sub.setFont(new Font("Monospaced", Font.PLAIN, 11));
        sub.setForeground(ACCENT_CYAN);

        JPanel titles = new JPanel();
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        titles.setOpaque(false);
        titles.add(title);
        titles.add(Box.createVerticalStrut(4));
        titles.add(sub);

        lblRojo           = new JLabel("0 crítico");
        lblAmarillo       = new JLabel("0 moderado");
        lblVerde          = new JLabel("0 leve");
        lblTotalEspera    = new JLabel("0 en espera");
        lblTotalAtendidos = new JLabel("0 atendidos");

        applyStatStyle(lblRojo,           COLOR_ROJO);
        applyStatStyle(lblAmarillo,       COLOR_AMARI);
        applyStatStyle(lblVerde,          COLOR_VERDE);
        applyStatStyle(lblTotalEspera,    ACCENT_CYAN);
        applyStatStyle(lblTotalAtendidos, ACCENT_BLUE);

        JPanel stats = new JPanel(new FlowLayout(FlowLayout.RIGHT, 22, 0));
        stats.setOpaque(false);
        stats.add(lblRojo);
        stats.add(lblAmarillo);
        stats.add(lblVerde);

        JSeparator vsep = new JSeparator(JSeparator.VERTICAL);
        vsep.setPreferredSize(new Dimension(1, 18));
        vsep.setForeground(BORDER_COLOR);
        stats.add(vsep);

        stats.add(lblTotalEspera);
        stats.add(lblTotalAtendidos);

        p.add(titles, BorderLayout.WEST);
        p.add(stats, BorderLayout.EAST);
        return p;
    }

    private void applyStatStyle(JLabel lbl, Color color) {
        lbl.setFont(new Font("Monospaced", Font.BOLD, 12));
        lbl.setForeground(color);
    }

    private JPanel buildLeftPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG_CARD);
        outer.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(BG_CARD);
        p.setBorder(new EmptyBorder(16, 16, 16, 16));

        p.add(sectionLabel("REGISTRAR NUEVO PACIENTE"));
        p.add(Box.createVerticalStrut(14));

        txtNombre  = styledField("Ej: Juan Pérez");
        txtEdad    = styledField("Ej: 34");
        txtSintoma = styledField("Ej: Dolor torácico agudo");
        cbPrioridad = styledCombo(new String[]{
            "Verde — Leve (gripe, malestar...)",
            "Amarillo — Moderado (fractura, fiebre alta...)",
            "Rojo — Crítico (infarto, trauma grave...)"
        });

        p.add(formRow("Nombre completo:", txtNombre));
        p.add(Box.createVerticalStrut(8));
        p.add(formRow("Edad:", txtEdad));
        p.add(Box.createVerticalStrut(8));
        p.add(formRow("Síntoma principal:", txtSintoma));
        p.add(Box.createVerticalStrut(8));
        p.add(formRow("Nivel de triaje:", cbPrioridad));
        p.add(Box.createVerticalStrut(14));

        JButton btnRegistrar = accentButton("REGISTRAR PACIENTE", ACCENT_BLUE);
        btnRegistrar.addActionListener(e -> registrarPaciente());
        p.add(btnRegistrar);

        p.add(Box.createVerticalStrut(20));

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_COLOR);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        p.add(sep);
        p.add(Box.createVerticalStrut(16));

        p.add(sectionLabel("ATENDER SIGUIENTE PACIENTE"));
        p.add(Box.createVerticalStrut(12));

        txtDiagnostico = styledField("Diagnóstico médico...");
        txtTratamiento = styledField("Tratamiento a aplicar...");
        p.add(formRow("Diagnóstico:", txtDiagnostico));
        p.add(Box.createVerticalStrut(8));
        p.add(formRow("Tratamiento:", txtTratamiento));
        p.add(Box.createVerticalStrut(14));

        JButton btnAtender = accentButton("ATENDER (dequeue)", COLOR_VERDE);
        btnAtender.addActionListener(e -> atenderPaciente());
        p.add(btnAtender);

        p.add(Box.createVerticalStrut(10));

        JButton btnDeshacer = accentButton("DESHACER ÚLTIMA ATENCIÓN (pop)", new Color(180, 80, 255));
        btnDeshacer.addActionListener(e -> deshacerAtencion());
        p.add(btnDeshacer);

        outer.add(p, BorderLayout.NORTH);
        return outer;
    }

    private JPanel buildQueuePanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG_CARD);
        outer.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(14, 16, 10, 16));
        header.add(sectionLabel("COLA DE ESPERA  —  Priority Queue (Triaje)"), BorderLayout.WEST);

        String[] cols = {"#", "Paciente", "Edad", "Triaje", "Síntoma", "Ingreso"};
        modeloCola = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaEspera = styledTable(modeloCola);
        tablaEspera.getColumnModel().getColumn(0).setMaxWidth(38);
        tablaEspera.getColumnModel().getColumn(0).setMinWidth(38);
        tablaEspera.getColumnModel().getColumn(2).setMaxWidth(48);
        tablaEspera.getColumnModel().getColumn(2).setMinWidth(48);
        tablaEspera.getColumnModel().getColumn(3).setMaxWidth(110);
        tablaEspera.getColumnModel().getColumn(3).setMinWidth(110);
        tablaEspera.getColumnModel().getColumn(5).setMaxWidth(72);
        tablaEspera.getColumnModel().getColumn(5).setMinWidth(72);
        tablaEspera.setDefaultRenderer(Object.class, new PriorityRenderer());

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(8, 16, 14, 16));

        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        legend.setOpaque(false);
        legend.add(legendDot(COLOR_ROJO,   "Crítico (alta prioridad)"));
        legend.add(legendDot(COLOR_AMARI,  "Moderado"));
        legend.add(legendDot(COLOR_VERDE,  "Leve (baja prioridad)"));

        JLabel diag = new JLabel("↑ Orden: Rojo → Amarillo → Verde  (mismo nivel: FIFO)");
        diag.setFont(new Font("Monospaced", Font.ITALIC, 11));
        diag.setForeground(TEXT_MUTED);
        diag.setBorder(new EmptyBorder(4, 0, 0, 0));

        footer.add(legend, BorderLayout.NORTH);
        footer.add(diag,   BorderLayout.SOUTH);

        outer.add(header,                  BorderLayout.NORTH);
        outer.add(darkScroll(tablaEspera), BorderLayout.CENTER);
        outer.add(footer,                  BorderLayout.SOUTH);
        return outer;
    }

    private JPanel buildRightPanel() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setOpaque(false);

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1.0; gc.insets = new Insets(0, 0, 8, 0);

        gc.gridy = 0; gc.weighty = 0.62;
        outer.add(buildHistorialPanel(), gc);

        gc.gridy = 1; gc.weighty = 0.38; gc.insets = new Insets(0, 0, 0, 0);
        outer.add(buildLogPanel(), gc);

        return outer;
    }

    private JPanel buildHistorialPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG_CARD);
        outer.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(14, 16, 10, 16));
        header.add(sectionLabel("HISTORIAL DE ATENCIONES  —  Stack"), BorderLayout.WEST);

        JLabel subLbl = new JLabel("tope = más reciente");
        subLbl.setFont(new Font("Monospaced", Font.ITALIC, 10));
        subLbl.setForeground(TEXT_MUTED);
        header.add(subLbl, BorderLayout.EAST);

        String[] cols = {"#", "Paciente", "Triaje", "Diagnóstico", "Hora"};
        modeloHistorial = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaHistorial = styledTable(modeloHistorial);
        tablaHistorial.getColumnModel().getColumn(0).setMaxWidth(60);
        tablaHistorial.getColumnModel().getColumn(0).setMinWidth(60);
        tablaHistorial.getColumnModel().getColumn(2).setMaxWidth(90);
        tablaHistorial.getColumnModel().getColumn(2).setMinWidth(90);
        tablaHistorial.getColumnModel().getColumn(4).setMaxWidth(62);
        tablaHistorial.getColumnModel().getColumn(4).setMinWidth(62);
        tablaHistorial.setDefaultRenderer(Object.class, new HistorialRenderer());

        outer.add(header,                     BorderLayout.NORTH);
        outer.add(darkScroll(tablaHistorial), BorderLayout.CENTER);
        return outer;
    }

    private JPanel buildLogPanel() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(BG_CARD);
        outer.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(12, 16, 8, 16));
        header.add(sectionLabel("LOG DEL SISTEMA"), BorderLayout.WEST);

        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setBackground(new Color(8, 12, 22));
        areaLog.setForeground(COLOR_VERDE);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 11));
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);
        areaLog.setBorder(new EmptyBorder(6, 8, 6, 8));

        JScrollPane logScroll = new JScrollPane(areaLog,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        logScroll.setBorder(null);
        logScroll.getViewport().setBackground(new Color(8, 12, 22));
        logScroll.getVerticalScrollBar().setUI(new DarkScrollBarUI());

        outer.add(header,    BorderLayout.NORTH);
        outer.add(logScroll, BorderLayout.CENTER);
        return outer;
    }

    private void registrarPaciente() {
        String nombre  = txtNombre.getText().trim();
        String edadStr = txtEdad.getText().trim();
        String sintoma = txtSintoma.getText().trim();

        if (nombre.isEmpty() || edadStr.isEmpty() || sintoma.isEmpty()) {
            showError("Por favor completa todos los campos.");
            return;
        }

        int edad;
        try {
            edad = Integer.parseInt(edadStr);
            if (edad <= 0 || edad > 130) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showError("La edad debe ser un número válido (1–130).");
            return;
        }

        Prioridad[] prioridades = {Prioridad.VERDE, Prioridad.AMARILLO, Prioridad.ROJO};
        Prioridad prioridad = prioridades[cbPrioridad.getSelectedIndex()];

        Paciente pac = new Paciente(nombre, edad, sintoma, prioridad);
        colaEspera.enqueue(pac, prioridad.getNivel());

        log("✚ INGRESÓ: " + pac.getNombre() + " — " + prioridad.getDescripcion(), getColorPrioridad(prioridad));
        actualizarTablaEspera();
        actualizarStats();
        limpiarFormRegistro();
    }

    private void atenderPaciente() {
        if (colaEspera.isEmpty()) {
            showError("No hay pacientes en espera.");
            return;
        }
        String diagnostico = txtDiagnostico.getText().trim();
        String tratamiento = txtTratamiento.getText().trim();
        if (diagnostico.isEmpty() || tratamiento.isEmpty()) {
            showError("Ingresa diagnóstico y tratamiento antes de atender.");
            return;
        }
        Paciente pac = colaEspera.dequeue();
        pac.atender(diagnostico, tratamiento);
        historialAtenciones.push(pac);

        log("✔ ATENDIDO: " + pac.getNombre() + " — Dx: " + diagnostico, ACCENT_CYAN);
        actualizarTablaEspera();
        actualizarTablaHistorial();
        actualizarStats();
        txtDiagnostico.setText("");
        txtTratamiento.setText("");
    }

    private void deshacerAtencion() {
        if (historialAtenciones.isEmpty()) {
            showError("No hay atenciones para deshacer.");
            return;
        }
        try {
            Paciente pac = historialAtenciones.pop();
            colaEspera.enqueue(pac, pac.getPrioridad().getNivel());
            log("↩ DESHECHO: " + pac.getNombre() + " regresa a la cola.", COLOR_AMARI);
            actualizarTablaEspera();
            actualizarTablaHistorial();
            actualizarStats();
        } catch (ExceptionIsEmpty e) {
            showError(e.getMessage());
        }
    }

    private void actualizarTablaEspera() {
        modeloCola.setRowCount(0);
        int[] orden = {2, 1, 0};
        int posGlobal = 1;
        for (int nivel : orden) {
            List<Paciente> lista = colaEspera.getByPriority(nivel);
            for (Paciente pac : lista) {
                modeloCola.addRow(new Object[]{
                    posGlobal++,
                    pac.getNombre(),
                    pac.getEdad(),
                    pac.getPrioridad().getDescripcion(),
                    pac.getSintoma(),
                    pac.getHoraIngresoStr()
                });
            }
        }
    }

    private void actualizarTablaHistorial() {
        modeloHistorial.setRowCount(0);
        List<Paciente> lista = historialAtenciones.toList();
        for (int i = 0; i < lista.size(); i++) {
            Paciente pac = lista.get(i);
            modeloHistorial.addRow(new Object[]{
                i == 0 ? "TOPE" : String.valueOf(i + 1),
                pac.getNombre(),
                pac.getPrioridad().getDescripcion(),
                pac.getDiagnostico(),
                pac.getHoraAtencionStr()
            });
        }
    }

    private void actualizarStats() {
        lblTotalEspera.setText(colaEspera.totalSize() + " en espera");
        lblRojo.setText(colaEspera.countByPriority(2) + " crítico");
        lblAmarillo.setText(colaEspera.countByPriority(1) + " moderado");
        lblVerde.setText(colaEspera.countByPriority(0) + " leve");
        int atendidos = historialAtenciones.toList().size();
        lblTotalAtendidos.setText(atendidos + " atendidos");
    }

    private void log(String msg, Color c) {
        String line = "[" + java.time.LocalTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + msg + "\n";
        areaLog.append(line);
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Monospaced", Font.BOLD, 11));
        l.setForeground(ACCENT_CYAN);
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    private JTextField styledField(String placeholder) {
        JTextField f = new JTextField(18);
        f.setBackground(BG_INPUT);
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(ACCENT_CYAN);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        f.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            new EmptyBorder(5, 8, 5, 8)
        ));
        f.putClientProperty("JTextField.placeholderText", placeholder);
        return f;
    }

    private JComboBox<String> styledCombo(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setBackground(BG_INPUT);
        cb.setForeground(Color.BLACK);
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cb.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        cb.setRenderer(new TriajeComboRenderer());
        return cb;
    }

    private JPanel formRow(String label, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(4, 3));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(TEXT_MUTED);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        row.add(lbl,   BorderLayout.NORTH);
        row.add(field, BorderLayout.CENTER);
        return row;
    }

    private JButton accentButton(String text, Color accent) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(accent.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(accent.brighter());
                } else {
                    g2.setColor(accent);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        return btn;
    }

    private JTable styledTable(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setBackground(BG_CARD);
        t.setForeground(TEXT_PRIMARY);
        t.setGridColor(BORDER_COLOR);
        t.setRowHeight(26);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        t.setSelectionBackground(new Color(41, 121, 255, 80));
        t.setSelectionForeground(TEXT_PRIMARY);
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false);
        t.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader header = t.getTableHeader();
        header.setBackground(new Color(12, 17, 32));
        header.setForeground(ACCENT_CYAN);
        header.setFont(new Font("Monospaced", Font.BOLD, 10));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        header.setReorderingAllowed(false);
        return t;
    }

    private JScrollPane darkScroll(JComponent comp) {
        JScrollPane sp = new JScrollPane(comp,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setBorder(null);
        sp.getViewport().setBackground(BG_CARD);
        sp.setBackground(BG_CARD);
        sp.getVerticalScrollBar().setUI(new DarkScrollBarUI());
        return sp;
    }

    private JPanel legendDot(Color color, String text) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        p.setOpaque(false);
        JLabel dot = new JLabel("●");
        dot.setForeground(color);
        dot.setFont(new Font("Serif", Font.PLAIN, 13));
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(TEXT_MUTED);
        p.add(dot);
        p.add(lbl);
        return p;
    }

    private Color getColorPrioridad(Prioridad p) {
        return switch (p) {
            case ROJO     -> COLOR_ROJO;
            case AMARILLO -> COLOR_AMARI;
            case VERDE    -> COLOR_VERDE;
        };
    }

    private void limpiarFormRegistro() {
        txtNombre.setText("");
        txtEdad.setText("");
        txtSintoma.setText("");
        cbPrioridad.setSelectedIndex(0);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    class TriajeComboRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            setBackground(isSelected ? new Color(30, 42, 70) : BG_INPUT);
            setBorder(new EmptyBorder(4, 8, 4, 8));
            if (value != null) {
                String v = value.toString();
                if (v.startsWith("Rojo"))     setForeground(COLOR_ROJO);
                else if (v.startsWith("Amarillo")) setForeground(COLOR_AMARI);
                else                           setForeground(COLOR_VERDE);
            }
            return this;
        }
    }

    class PriorityRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
            c.setBackground(isSelected ? new Color(41, 121, 255, 60) : BG_CARD);
            c.setForeground(TEXT_PRIMARY);
            if (column == 3 && value != null) {
                String v = value.toString();
                if      (v.contains("Crítico"))  c.setForeground(COLOR_ROJO);
                else if (v.contains("Moderado")) c.setForeground(COLOR_AMARI);
                else if (v.contains("Leve"))     c.setForeground(COLOR_VERDE);
            }
            ((JLabel) c).setBorder(new EmptyBorder(0, 8, 0, 8));
            return c;
        }
    }

    class HistorialRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
            c.setBackground(isSelected ? new Color(41, 121, 255, 60) : BG_CARD);
            c.setForeground(TEXT_PRIMARY);
            if (column == 0 && value != null && value.toString().equals("TOPE")) {
                c.setForeground(ACCENT_CYAN);
                ((JLabel) c).setFont(new Font("Monospaced", Font.BOLD, 10));
            }
            if (column == 2 && value != null) {
                String v = value.toString();
                if      (v.contains("Crítico"))  c.setForeground(COLOR_ROJO);
                else if (v.contains("Moderado")) c.setForeground(COLOR_AMARI);
                else if (v.contains("Leve"))     c.setForeground(COLOR_VERDE);
            }
            ((JLabel) c).setBorder(new EmptyBorder(0, 8, 0, 8));
            return c;
        }
    }

    static class DarkScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            thumbColor = new Color(41, 121, 255, 120);
            trackColor = new Color(16, 22, 40);
        }
        @Override protected JButton createDecreaseButton(int o) { return zeroButton(); }
        @Override protected JButton createIncreaseButton(int o) { return zeroButton(); }
        private JButton zeroButton() {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(0, 0));
            return b;
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        UIManager.put("OptionPane.background",        new Color(16, 22, 40));
        UIManager.put("Panel.background",             new Color(16, 22, 40));
        UIManager.put("OptionPane.messageForeground", new Color(220, 230, 255));
        SwingUtilities.invokeLater(HospitalGUI::new);
    }
}
