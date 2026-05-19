import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    private Controlador controlador;
    private PanelArbolAVL panelArbol;

    private JTextField txtNombre, txtTelefono, txtEmail, txtCategoria;
    private JTextField txtBuscarNombre;

    private DefaultTableModel modeloTabla;
    private JTable tablaContactos;

    private JTextArea areaHistorial;

    private JLabel lblTotal, lblFavoritos, lblAltura, lblBalance;

    private static final Color C_BG_DARK     = new Color(12, 16, 35);
    private static final Color C_PANEL_DARK  = new Color(20, 26, 52);
    private static final Color C_PANEL_MED   = new Color(28, 36, 68);
    private static final Color C_ACENTO      = new Color(70, 130, 220);
    private static final Color C_VERDE       = new Color(30, 180, 100);
    private static final Color C_ROJO        = new Color(210, 55, 55);
    private static final Color C_AMARILLO    = new Color(210, 160, 20);
    private static final Color C_VIOLETA     = new Color(130, 60, 200);
    private static final Color C_TEXTO_CLARO = new Color(100, 120, 160);
    private static final Color C_TEXTO_DIM   = new Color(120, 140, 180);

    public VentanaPrincipal() {
        AVLTree<Contacto> arbol = new AVLTree<>();
        controlador = new Controlador(arbol);
        cargarDatosEjemplo(arbol);
        initUI();
        actualizarTodo();
        setVisible(true);
    }

    private void initUI() {
        setTitle("📱 Agenda Inteligente — AVL Tree");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1340, 840);
        setMinimumSize(new Dimension(1100, 720));
        setLocationRelativeTo(null);
        getContentPane().setBackground(C_BG_DARK);
        setLayout(new BorderLayout(6, 6));

        add(crearCabecera(), BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                crearPanelArbol(), crearPanelDerecho());
        split.setDividerLocation(720);
        split.setResizeWeight(0.6);
        split.setBorder(new EmptyBorder(0, 8, 0, 8));
        split.setBackground(C_BG_DARK);
        split.setDividerSize(6);
        add(split, BorderLayout.CENTER);

        add(crearBarraEstado(), BorderLayout.SOUTH);
    }

    private JPanel crearCabecera() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(C_PANEL_DARK);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, C_ACENTO),
                new EmptyBorder(14, 22, 14, 22)));

        JLabel titulo = new JLabel("📱  Agenda Inteligente — AVL Tree");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);

        JLabel subtitulo = new JLabel("Estructuras de Datos II — Árbol AVL Auto-Balanceado  |  Búsqueda O(log n) garantizada");
        subtitulo.setFont(new Font("SansSerif", Font.ITALIC, 12));
        subtitulo.setForeground(C_TEXTO_DIM);

        JPanel textos = new JPanel(new GridLayout(2, 1, 0, 2));
        textos.setOpaque(false);
        textos.add(titulo);
        textos.add(subtitulo);
        panel.add(textos, BorderLayout.WEST);

        JPanel stats = new JPanel(new FlowLayout(FlowLayout.RIGHT, 18, 0));
        stats.setOpaque(false);
        lblTotal    = crearChip("Contactos: 0",   C_ACENTO);
        lblFavoritos = crearChip("Favoritos: 0",  C_AMARILLO);
        lblAltura   = crearChip("Altura: 0",      C_VIOLETA);
        lblBalance  = crearChip("✔ Balanceado",   C_VERDE);
        stats.add(lblTotal);
        stats.add(lblFavoritos);
        stats.add(lblAltura);
        stats.add(lblBalance);
        panel.add(stats, BorderLayout.EAST);
        return panel;
    }

    private JLabel crearChip(String texto, Color color) {
        JLabel l = new JLabel("  " + texto + "  ") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color.darker().darker());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                super.paintComponent(g);
            }
        };
        l.setOpaque(false);
        l.setForeground(color.brighter());
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        l.setBorder(new EmptyBorder(4, 2, 4, 2));
        return l;
    }

    private JScrollPane crearPanelArbol() {
        panelArbol = new PanelArbolAVL(controlador.getArbol());
        JScrollPane scroll = new JScrollPane(panelArbol);
        scroll.setBorder(crearBorde("🌳  Visualización del Árbol AVL  (bf = factor de balance)"));
        scroll.getViewport().setBackground(new Color(12, 16, 35));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JTabbedPane crearPanelDerecho() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabs.setBackground(C_PANEL_DARK);
        tabs.setForeground(C_TEXTO_CLARO);
        tabs.addTab("➕ Gestionar",   crearTabGestionar());
        tabs.addTab("📋 Contactos",   crearTabContactos());
        tabs.addTab("📜 Historial",   crearTabHistorial());
        tabs.addTab("🔄 Recorridos",  crearTabRecorridos());
        tabs.addTab("ℹ️ AVL Info",    crearTabInfo());
        return tabs;
    }

    private JPanel crearTabGestionar() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(C_PANEL_DARK);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(C_PANEL_MED);
        form.setBorder(crearBorde("Datos del Contacto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombre    = crearTextField();
        txtTelefono  = crearTextField();
        txtEmail     = crearTextField();
        txtCategoria = crearTextField();

        String[] labels = {"Nombre *:", "Teléfono *:", "Email:", "Categoría:"};
        JTextField[] campos = {txtNombre, txtTelefono, txtEmail, txtCategoria};
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
            lbl.setForeground(C_TEXTO_CLARO);
            form.add(lbl, gbc);
            gbc.gridx = 1; gbc.weightx = 1.0;
            form.add(campos[i], gbc);
        }

        JPanel botones = new JPanel(new GridLayout(2, 3, 8, 8));
        botones.setBackground(C_PANEL_MED);
        botones.setBorder(crearBorde("Operaciones AVL"));

        JButton btnInsertar  = crearBoton("➕ Insertar",    C_VERDE);
        JButton btnEliminar  = crearBoton("🗑 Eliminar",    C_ROJO);
        JButton btnBuscar    = crearBoton("🔍 Buscar",      C_ACENTO);
        JButton btnFavorito  = crearBoton("★ Favorito",    C_AMARILLO);
        JButton btnLimpiar   = crearBoton("🧹 Limpiar",    C_TEXTO_DIM);
        JButton btnBuscarPaso = crearBoton("🔎 Ver pasos", new Color(80, 180, 180));

        botones.add(btnInsertar); botones.add(btnEliminar); botones.add(btnBuscar);
        botones.add(btnFavorito); botones.add(btnLimpiar);  botones.add(btnBuscarPaso);

        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        panelBuscar.setBackground(C_PANEL_MED);
        panelBuscar.setBorder(crearBorde("Búsqueda paso a paso (recorrido AVL)"));
        JLabel lbBuscar = new JLabel("Nombre:");
        lbBuscar.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbBuscar.setForeground(C_TEXTO_CLARO);
        txtBuscarNombre = crearTextField();
        txtBuscarNombre.setPreferredSize(new Dimension(160, 28));
        panelBuscar.add(lbBuscar);
        panelBuscar.add(txtBuscarNombre);
        panelBuscar.add(crearBoton("🔎 Ver pasos", new Color(80, 180, 180)));

        panel.add(form, BorderLayout.NORTH);
        JPanel centro = new JPanel(new BorderLayout(8, 8));
        centro.setBackground(C_PANEL_DARK);
        centro.add(botones, BorderLayout.NORTH);
        centro.add(panelBuscar, BorderLayout.SOUTH);
        panel.add(centro, BorderLayout.CENTER);

        btnInsertar.addActionListener(e -> accionInsertar());
        btnEliminar.addActionListener(e -> accionEliminar());
        btnBuscar.addActionListener(e -> accionBuscarSimple());
        btnFavorito.addActionListener(e -> accionFavorito());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnBuscarPaso.addActionListener(e -> accionBuscarPasos());

        for (Component c : panelBuscar.getComponents())
            if (c instanceof JButton && ((JButton)c).getText().contains("Ver pasos"))
                ((JButton)c).addActionListener(e -> accionBuscarPasos());

        return panel;
    }

    private JScrollPane crearTabContactos() {
        String[] columnas = {"Nombre", "Teléfono", "Email", "Categoría", "Favorito"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaContactos = new JTable(modeloTabla);
        tablaContactos.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tablaContactos.setRowHeight(26);
        tablaContactos.setBackground(C_PANEL_MED);
        tablaContactos.setForeground(C_TEXTO_CLARO);
        tablaContactos.setGridColor(new Color(40, 55, 90));
        tablaContactos.setSelectionBackground(new Color(50, 90, 160));
        tablaContactos.setSelectionForeground(Color.WHITE);
        tablaContactos.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tablaContactos.getTableHeader().setBackground(C_PANEL_DARK);
        tablaContactos.getTableHeader().setForeground(C_TEXTO_CLARO);
        tablaContactos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tablaContactos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaContactos.getSelectedRow() >= 0) {
                String nombre = (String) modeloTabla.getValueAt(tablaContactos.getSelectedRow(), 0);
                llenarFormulario(nombre);
                panelArbol.resaltarNodo(nombre);
            }
        });
        JScrollPane scroll = new JScrollPane(tablaContactos);
        scroll.setBorder(crearBorde("📋 Contactos (In-Orden AVL = orden alfabético)"));
        scroll.getViewport().setBackground(C_PANEL_MED);
        return scroll;
    }

    private JScrollPane crearTabHistorial() {
        areaHistorial = new JTextArea();
        areaHistorial.setEditable(false);
        areaHistorial.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaHistorial.setBackground(new Color(10, 14, 28));
        areaHistorial.setForeground(new Color(80, 220, 130));
        areaHistorial.setCaretColor(Color.WHITE);
        JScrollPane scroll = new JScrollPane(areaHistorial);
        scroll.setBorder(crearBorde("📜 Historial de Operaciones"));
        return scroll;
    }

    private JPanel crearTabRecorridos() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(C_PANEL_DARK);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JTextArea areaRec = new JTextArea();
        areaRec.setEditable(false);
        areaRec.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaRec.setBackground(new Color(10, 14, 28));
        areaRec.setForeground(new Color(160, 200, 255));
        areaRec.setLineWrap(true);

        JPanel botRec = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        botRec.setBackground(C_PANEL_DARK);

        JButton btnIn  = crearBoton("In-Orden",    new Color(40, 120, 200));
        JButton btnPre = crearBoton("Pre-Orden",   new Color(130, 50, 190));
        JButton btnPos = crearBoton("Post-Orden",  new Color(30, 160, 90));

        btnIn.addActionListener(e -> {
            List<Contacto> lista = controlador.getArbol().inOrden();
            StringBuilder sb = new StringBuilder("── IN-ORDEN (Izq → Raíz → Der) — Resultado: ORDEN ALFABÉTICO ──\n\n");
            for (int i = 0; i < lista.size(); i++)
                sb.append(String.format("%3d. %s%n", i + 1, lista.get(i)));
            areaRec.setText(sb.toString());
        });
        btnPre.addActionListener(e -> {
            List<Contacto> lista = controlador.getArbol().preOrden();
            StringBuilder sb = new StringBuilder("── PRE-ORDEN (Raíz → Izq → Der) — Orden de inserción AVL ──\n\n");
            for (int i = 0; i < lista.size(); i++)
                sb.append(String.format("%3d. %s%n", i + 1, lista.get(i)));
            areaRec.setText(sb.toString());
        });
        btnPos.addActionListener(e -> {
            List<Contacto> lista = controlador.getArbol().postOrden();
            StringBuilder sb = new StringBuilder("── POST-ORDEN (Izq → Der → Raíz) — Orden de eliminación segura ──\n\n");
            for (int i = 0; i < lista.size(); i++)
                sb.append(String.format("%3d. %s%n", i + 1, lista.get(i)));
            areaRec.setText(sb.toString());
        });

        botRec.add(btnIn); botRec.add(btnPre); botRec.add(btnPos);
        panel.add(botRec, BorderLayout.NORTH);
        JScrollPane sc = new JScrollPane(areaRec);
        sc.getViewport().setBackground(new Color(10, 14, 28));
        panel.add(sc, BorderLayout.CENTER);

        JLabel info = new JLabel("<html><b style='color:#78b4ff'>In-Orden</b> en AVL siempre devuelve contactos en orden <b style='color:#78b4ff'>alfabético</b> gracias al auto-balanceo.</html>");
        info.setFont(new Font("SansSerif", Font.PLAIN, 12));
        info.setForeground(C_TEXTO_DIM);
        info.setBorder(new EmptyBorder(6, 0, 0, 0));
        panel.add(info, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearTabInfo() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(C_PANEL_DARK);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setBackground(new Color(10, 14, 28));
        area.setForeground(new Color(160, 200, 255));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setText(
            "╔══════════════════════════════════════════════════════════╗\n" +
            "║          ¿Por qué AVL en una Agenda de Contactos?        ║\n" +
            "╚══════════════════════════════════════════════════════════╝\n\n" +
            "PROBLEMA CON UN BST NORMAL:\n" +
            "  Si insertamos contactos en orden alfabético (Alicia, Bob,\n" +
            "  Carlos...) un BST se degrada a una lista enlazada con\n" +
            "  búsquedas O(n). Una agenda con millones de contactos\n" +
            "  sería inutilizable.\n\n" +
            "SOLUCIÓN — ÁRBOL AVL:\n" +
            "  El AVL mantiene |bf| ≤ 1 en TODOS los nodos siempre.\n" +
            "  Esto garantiza altura O(log n) y búsquedas O(log n).\n\n" +
            "FACTOR DE BALANCE (bf):\n" +
            "  bf = altura(subárbol_derecho) - altura(subárbol_izquierdo)\n" +
            "  • bf = -1, 0, +1 → NODO BALANCEADO  ✔\n" +
            "  • bf = -2 o +2   → ROTACIÓN necesaria\n\n" +
            "ROTACIONES AVL:\n" +
            "  • Rotación Simple Izquierda (RSL)  → bf = +2, hijo bf = +1\n" +
            "  • Rotación Simple Derecha  (RSD)   → bf = -2, hijo bf = -1\n" +
            "  • Rotación Doble Izq-Der   (RDID)  → bf = -2, hijo bf = +1\n" +
            "  • Rotación Doble Der-Izq   (RDDI)  → bf = +2, hijo bf = -1\n\n" +
            "COMPLEJIDADES:\n" +
            "  Operación      BST (peor)   AVL (garantizado)\n" +
            "  ─────────────  ──────────   ─────────────────\n" +
            "  Búsqueda       O(n)         O(log n)\n" +
            "  Inserción      O(n)         O(log n)\n" +
            "  Eliminación    O(n)         O(log n)\n\n" +
            "APLICACIONES REALES:\n" +
            "  • Agendas de contactos (esta demo)\n" +
            "  • Bases de datos (índices B-tree derivados)\n" +
            "  • Sistemas de archivos (ext4, NTFS)\n" +
            "  • Autocompletado en motores de búsqueda\n" +
            "  • Routers de red (tablas de enrutamiento)\n"
        );

        JScrollPane sc = new JScrollPane(area);
        sc.getViewport().setBackground(new Color(10, 14, 28));
        panel.add(sc, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearBarraEstado() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 5));
        panel.setBackground(new Color(8, 12, 28));
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, C_ACENTO));
        JLabel lbl = new JLabel("💡 Selecciona un contacto en la tabla para resaltarlo en el árbol  |  bf = factor de balance AVL (siempre entre -1 y +1)");
        lbl.setForeground(C_TEXTO_DIM);
        lbl.setFont(new Font("SansSerif", Font.ITALIC, 11));
        panel.add(lbl);
        return panel;
    }

    private void accionInsertar() {
        String nombre    = txtNombre.getText().trim();
        String telefono  = txtTelefono.getText().trim();
        String email     = txtEmail.getText().trim();
        String categoria = txtCategoria.getText().trim();

        if (nombre.isEmpty() || telefono.isEmpty()) {
            mostrarError("Nombre y Teléfono son obligatorios.");
            return;
        }
        if (email.isEmpty()) email = "—";
        if (categoria.isEmpty()) categoria = "General";

        Contacto c = new Contacto(nombre, telefono, email, categoria);
        if (controlador.insertar(c)) { actualizarTodo(); limpiarFormulario(); }
        else mostrarError("Ya existe un contacto con el nombre: " + nombre);
    }

    private void accionEliminar() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) { mostrarError("Ingresa el nombre del contacto a eliminar."); return; }
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el contacto: " + nombre + "?\nEl árbol se rebalanceará automáticamente.",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controlador.eliminar(nombre)) actualizarTodo();
            else mostrarError("No se encontró contacto: " + nombre);
        }
    }

    private void accionBuscarSimple() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) { mostrarError("Ingresa el nombre a buscar."); return; }
        Contacto c = controlador.buscar(nombre);
        if (c != null) {
            panelArbol.resaltarNodo(nombre);
            llenarFormulario(nombre);
            JOptionPane.showMessageDialog(this,
                    "✅ Contacto encontrado:\n\n" +
                    "Nombre:    " + c.getNombre() + "\n" +
                    "Teléfono:  " + c.getTelefono() + "\n" +
                    "Email:     " + c.getEmail() + "\n" +
                    "Categoría: " + c.getCategoria() + "\n" +
                    "Favorito:  " + (c.isFavorito() ? "★ Sí" : "No"),
                    "Contacto encontrado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            panelArbol.limpiarResaltado();
            JOptionPane.showMessageDialog(this,
                    "❌ No se encontró: " + nombre,
                    "No encontrado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void accionBuscarPasos() {
        String nombre = txtBuscarNombre.getText().trim();
        if (nombre.isEmpty()) nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) { mostrarError("Ingresa el nombre para buscar paso a paso."); return; }

        List<String> pasos = controlador.buscarConPasos(nombre);
        Contacto c = controlador.buscar(nombre);
        if (c != null) panelArbol.resaltarNodo(nombre);
        else panelArbol.limpiarResaltado();

        JTextArea area = new JTextArea(String.join("\n", pasos));
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setBackground(new Color(10, 14, 28));
        area.setForeground(new Color(80, 220, 130));
        area.setMargin(new Insets(8, 8, 8, 8));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(480, 260));
        JOptionPane.showMessageDialog(this, scroll,
                "🔍 Pasos de búsqueda AVL — Nombre: " + nombre, JOptionPane.PLAIN_MESSAGE);
        actualizarHistorial();
    }

    private void accionFavorito() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) { mostrarError("Ingresa el nombre del contacto."); return; }
        if (controlador.marcarFavorito(nombre)) actualizarTodo();
        else mostrarError("No se encontró contacto: " + nombre);
    }

    private void limpiarFormulario() {
        txtNombre.setText(""); txtTelefono.setText("");
        txtEmail.setText(""); txtCategoria.setText("");
        panelArbol.limpiarResaltado();
    }

    private void llenarFormulario(String nombre) {
        Contacto c = controlador.buscar(nombre);
        if (c == null) return;
        txtNombre.setText(c.getNombre());
        txtTelefono.setText(c.getTelefono());
        txtEmail.setText(c.getEmail());
        txtCategoria.setText(c.getCategoria());
    }

    private void actualizarTodo() {
        panelArbol.setArbol(controlador.getArbol());
        actualizarTabla();
        actualizarHistorial();
        actualizarStats();
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Contacto c : controlador.getArbol().inOrden()) {
            modeloTabla.addRow(new Object[]{
                c.getNombre(), c.getTelefono(), c.getEmail(),
                c.getCategoria(), c.isFavorito() ? "★" : ""
            });
        }
    }

    private void actualizarHistorial() {
        StringBuilder sb = new StringBuilder();
        List<String> hist = controlador.getArbol().getHistorial();
        for (int i = hist.size() - 1; i >= 0; i--)
            sb.append(hist.get(i)).append("\n");
        areaHistorial.setText(sb.toString());
    }

    private void actualizarStats() {
        AVLTree<Contacto> arbol = controlador.getArbol();
        lblTotal.setText("  Contactos: " + arbol.totalNodos() + "  ");
        lblFavoritos.setText("  Favoritos: " + arbol.totalFavoritos() + "  ");
        lblAltura.setText("  Altura: " + arbol.altura() + "  ");
        boolean bal = arbol.estaBalanceado();
        lblBalance.setText(bal ? "  ✔ Balanceado  " : "  ✖ Desbalanceado  ");
        lblBalance.setForeground(bal ? C_VERDE.brighter() : C_ROJO.brighter());
    }

    private JTextField crearTextField() {
        JTextField tf = new JTextField(14);
        tf.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tf.setBackground(new Color(22, 30, 60));
        tf.setForeground(C_TEXTO_CLARO);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 80, 140)),
                new EmptyBorder(4, 6, 4, 6)));
        return tf;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color base = getModel().isRollover() ? color.brighter() : color.darker();
                g2.setColor(base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 34));
        return btn;
    }

    private TitledBorder crearBorde(String titulo) {
        TitledBorder b = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(50, 80, 140), 1, true), titulo);
        b.setTitleFont(new Font("SansSerif", Font.BOLD, 12));
        b.setTitleColor(C_TEXTO_CLARO);
        return b;
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void cargarDatosEjemplo(AVLTree<Contacto> arbol) {
        Object[][] datos = {
            {"Laura Mendoza",   "+51 987 654 321", "laura@email.com",   "Familia"},
            {"Carlos Quispe",   "+51 912 345 678", "carlos@gmail.com",  "Trabajo"},
            {"Ana Torres",      "+51 956 789 012", "ana@hotmail.com",   "Amigos"},
            {"Miguel Rojas",    "+51 934 567 890", "miguel@email.com",  "Trabajo"},
            {"Sofia Vargas",    "+51 978 901 234", "sofia@gmail.com",   "Amigos"},
            {"Diego Paredes",   "+51 945 678 901", "diego@email.com",   "Familia"},
            {"Valeria Chávez",  "+51 967 890 123", "vale@email.com",    "Trabajo"},
            {"Andrés Lima",     "+51 923 456 789", "andres@gmail.com",  "Amigos"},
            {"Camila Flores",   "+51 989 012 345", "cami@hotmail.com",  "Familia"},
            {"José Mamani",     "+51 901 234 567", "jose@email.com",    "Trabajo"},
            {"Isabella Ramos",  "+51 945 123 789", "isa@gmail.com",     "Amigos"},
        };
        for (Object[] d : datos) {
            try {
                Contacto c = new Contacto((String)d[0], (String)d[1], (String)d[2], (String)d[3]);
                arbol.insert(c);
            } catch (ItemDuplicated ignored) {}
        }
        Contacto laura = arbol.buscar(new Contacto("Laura Mendoza", "", "", ""));
        Contacto ana   = arbol.buscar(new Contacto("Ana Torres", "", "", ""));
        if (laura != null) laura.setFavorito(true);
        if (ana   != null) ana.setFavorito(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(VentanaPrincipal::new);
    }
}
