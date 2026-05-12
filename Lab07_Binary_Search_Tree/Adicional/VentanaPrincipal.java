package EjercicioAdicional;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
 
public class VentanaPrincipal extends JFrame {
    private Controlador controlador;
    private PanelArbol panelArbol;
 
    private JTextField txtIsbn, txtTitulo, txtAutor, txtAnio, txtCategoria;
    private JTextField txtBuscarIsbn;
 
    private DefaultTableModel modeloTabla;
    private JTable tablaLibros;
 
    private JTextArea areaHistorial;
 
    private JLabel lblTotal, lblDisponibles, lblPrestados, lblAltura;
 
    private static final Color C_PRIMARIO    = new Color(41, 128, 185);
    private static final Color C_EXITO       = new Color(39, 174, 96);
    private static final Color C_PELIGRO     = new Color(192, 57, 43);
    private static final Color C_ADVERTENCIA = new Color(211, 84, 0);
    private static final Color C_FONDO       = new Color(236, 240, 241);
    private static final Color C_PANEL       = new Color(255, 255, 255);
    private static final Color C_TEXTO_DARK  = new Color(44, 62, 80);
 
    public VentanaPrincipal() {
        LinkedBST<Libro> arbol = new LinkedBST<>();
        controlador = new Controlador(arbol);
        cargarDatosEjemplo(arbol);
        initUI();
        actualizarTodo();
        setVisible(true);
    }
 
    private void initUI() {
        setTitle("📚 Sistema de Gestión de Biblioteca — LinkedBST");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 820);
        setMinimumSize(new Dimension(1100, 720));
        setLocationRelativeTo(null);
        getContentPane().setBackground(C_FONDO);
        setLayout(new BorderLayout(8, 8));
 
        add(crearCabecera(), BorderLayout.NORTH);
 
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                crearPanelArbol(), crearPanelDerecho());
        split.setDividerLocation(700);
        split.setResizeWeight(0.6);
        split.setBorder(new EmptyBorder(0, 8, 0, 8));
        split.setBackground(C_FONDO);
        add(split, BorderLayout.CENTER);
 
        add(crearBarraEstado(), BorderLayout.SOUTH);
    }
 
    private JPanel crearCabecera() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(C_PRIMARIO);
        panel.setBorder(new EmptyBorder(12, 20, 12, 20));
 
        JLabel titulo = new JLabel("📚 Biblioteca Universidad — LinkedBST");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(Color.WHITE);
 
        JLabel subtitulo = new JLabel("Estructura de Datos II — Árbol Binario de Búsqueda (LinkedBST)");
        subtitulo.setFont(new Font("SansSerif", Font.ITALIC, 13));
        subtitulo.setForeground(new Color(189, 215, 238));
 
        JPanel textos = new JPanel(new GridLayout(2, 1));
        textos.setOpaque(false);
        textos.add(titulo);
        textos.add(subtitulo);
        panel.add(textos, BorderLayout.WEST);
 
        JPanel stats = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        stats.setOpaque(false);
        lblTotal       = crearLabelStat("Total: 0");
        lblDisponibles = crearLabelStat("Disponibles: 0");
        lblPrestados   = crearLabelStat("Prestados: 0");
        lblAltura      = crearLabelStat("Altura: 0");
        stats.add(lblTotal);
        stats.add(lblDisponibles);
        stats.add(lblPrestados);
        stats.add(lblAltura);
        panel.add(stats, BorderLayout.EAST);
        return panel;
    }
 
    private JLabel crearLabelStat(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(Color.WHITE);
        return l;
    }
 
    private JScrollPane crearPanelArbol() {
        panelArbol = new PanelArbol(controlador.getArbol());
        JScrollPane scroll = new JScrollPane(panelArbol);
        scroll.setBorder(crearBorde("🌳 Visualización del Árbol BST"));
        scroll.getViewport().setBackground(new Color(244, 246, 250));
        return scroll;
    }
 
    private JTabbedPane crearPanelDerecho() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabs.setBackground(C_PANEL);
        tabs.addTab("➕ Gestionar",  crearTabGestionar());
        tabs.addTab("📋 Catálogo",   crearTabCatalogo());
        tabs.addTab("📜 Historial",  crearTabHistorial());
        tabs.addTab("ℹ️ Recorridos", crearTabRecorridos());
        return tabs;
    }
 
    private JPanel crearTabGestionar() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(C_PANEL);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
 
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(C_PANEL);
        form.setBorder(crearBorde("Datos del Libro"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
 
        txtIsbn      = new JTextField(12);
        txtTitulo    = new JTextField(12);
        txtAutor     = new JTextField(12);
        txtAnio      = new JTextField(6);
        txtCategoria = new JTextField(12);
 
        String[] labels = {"ISBN *:", "Título *:", "Autor *:", "Año:", "Categoría:"};
        JTextField[] campos = {txtIsbn, txtTitulo, txtAutor, txtAnio, txtCategoria};
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
            form.add(lbl, gbc);
            gbc.gridx = 1; gbc.weightx = 1.0;
            campos[i].setFont(new Font("SansSerif", Font.PLAIN, 12));
            form.add(campos[i], gbc);
        }
 
        JPanel botones = new JPanel(new GridLayout(2, 3, 8, 8));
        botones.setBackground(C_PANEL);
        botones.setBorder(crearBorde("Operaciones BST"));
 
        JButton btnInsertar = crearBoton("➕ Insertar",  C_EXITO);
        JButton btnEliminar = crearBoton("🗑 Eliminar",  C_PELIGRO);
        JButton btnBuscar   = crearBoton("🔍 Buscar",    C_PRIMARIO);
        JButton btnPrestar  = crearBoton("📤 Prestar",   C_ADVERTENCIA);
        JButton btnDevolver = crearBoton("📥 Devolver",  new Color(22, 160, 133));
        JButton btnLimpiar  = crearBoton("🧹 Limpiar",  new Color(127, 140, 141));
 
        botones.add(btnInsertar); botones.add(btnEliminar); botones.add(btnBuscar);
        botones.add(btnPrestar);  botones.add(btnDevolver); botones.add(btnLimpiar);
 
        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        panelBuscar.setBackground(C_PANEL);
        panelBuscar.setBorder(crearBorde("Búsqueda paso a paso"));
        JLabel lbBuscar = new JLabel("ISBN:");
        lbBuscar.setFont(new Font("SansSerif", Font.BOLD, 12));
        txtBuscarIsbn = new JTextField(14);
        txtBuscarIsbn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        JButton btnBuscarRapido = crearBoton("🔍 Ver pasos", C_PRIMARIO);
        panelBuscar.add(lbBuscar);
        panelBuscar.add(txtBuscarIsbn);
        panelBuscar.add(btnBuscarRapido);
 
        panel.add(form, BorderLayout.NORTH);
        JPanel centro = new JPanel(new BorderLayout(8, 8));
        centro.setBackground(C_PANEL);
        centro.add(botones, BorderLayout.NORTH);
        centro.add(panelBuscar, BorderLayout.SOUTH);
        panel.add(centro, BorderLayout.CENTER);
 
        btnInsertar.addActionListener(e -> accionInsertar());
        btnEliminar.addActionListener(e -> accionEliminar());
        btnBuscar.addActionListener(e -> accionBuscarSimple());
        btnPrestar.addActionListener(e -> accionPrestar());
        btnDevolver.addActionListener(e -> accionDevolver());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnBuscarRapido.addActionListener(e -> accionBuscarPasos());
 
        return panel;
    }
 
    private JScrollPane crearTabCatalogo() {
        String[] columnas = {"ISBN", "Título", "Autor", "Año", "Categoría", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaLibros = new JTable(modeloTabla);
        tablaLibros.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tablaLibros.setRowHeight(24);
        tablaLibros.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tablaLibros.getTableHeader().setBackground(C_PRIMARIO);
        tablaLibros.getTableHeader().setForeground(Color.WHITE);
        tablaLibros.setSelectionBackground(new Color(174, 214, 241));
        tablaLibros.setGridColor(new Color(220, 220, 220));
        tablaLibros.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tablaLibros.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaLibros.getSelectedRow() >= 0) {
                String isbn = (String) modeloTabla.getValueAt(tablaLibros.getSelectedRow(), 0);
                llenarFormulario(isbn);
                panelArbol.resaltarNodo(isbn);
            }
        });
        JScrollPane scroll = new JScrollPane(tablaLibros);
        scroll.setBorder(crearBorde("📋 Catálogo (In-Orden = ordenado por ISBN)"));
        return scroll;
    }
 
    private JScrollPane crearTabHistorial() {
        areaHistorial = new JTextArea();
        areaHistorial.setEditable(false);
        areaHistorial.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaHistorial.setBackground(new Color(28, 28, 35));
        areaHistorial.setForeground(new Color(160, 255, 140));
        areaHistorial.setCaretColor(Color.WHITE);
        JScrollPane scroll = new JScrollPane(areaHistorial);
        scroll.setBorder(crearBorde("📜 Historial de Operaciones"));
        return scroll;
    }
 
    private JPanel crearTabRecorridos() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(C_PANEL);
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));
 
        JTextArea areaRec = new JTextArea();
        areaRec.setEditable(false);
        areaRec.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaRec.setLineWrap(true);
 
        JPanel botRec = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 6));
        botRec.setBackground(C_PANEL);
 
        JButton btnIn  = crearBoton("In-Orden",   new Color(41, 128, 185));
        JButton btnPre = crearBoton("Pre-Orden",  new Color(142, 68, 173));
        JButton btnPos = crearBoton("Post-Orden", new Color(39, 174, 96));
 
        btnIn.addActionListener(e -> {
            List<Libro> lista = controlador.getArbol().inOrden();
            StringBuilder sb = new StringBuilder("── IN-ORDEN (Izq → Raíz → Der) ──\n\n");
            for (int i = 0; i < lista.size(); i++)
                sb.append(String.format("%2d. %s%n", i + 1, lista.get(i)));
            areaRec.setText(sb.toString());
        });
        btnPre.addActionListener(e -> {
            List<Libro> lista = controlador.getArbol().preOrden();
            StringBuilder sb = new StringBuilder("── PRE-ORDEN (Raíz → Izq → Der) ──\n\n");
            for (int i = 0; i < lista.size(); i++)
                sb.append(String.format("%2d. %s%n", i + 1, lista.get(i)));
            areaRec.setText(sb.toString());
        });
        btnPos.addActionListener(e -> {
            List<Libro> lista = controlador.getArbol().postOrden();
            StringBuilder sb = new StringBuilder("── POST-ORDEN (Izq → Der → Raíz) ──\n\n");
            for (int i = 0; i < lista.size(); i++)
                sb.append(String.format("%2d. %s%n", i + 1, lista.get(i)));
            areaRec.setText(sb.toString());
        });
 
        botRec.add(btnIn); botRec.add(btnPre); botRec.add(btnPos);
        panel.add(botRec, BorderLayout.NORTH);
        panel.add(new JScrollPane(areaRec), BorderLayout.CENTER);
 
        JLabel infoLabel = new JLabel("<html><b>Dato:</b> In-Orden en un BST devuelve los elementos <b>ordenados</b> por ISBN.</html>");
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        infoLabel.setForeground(C_TEXTO_DARK);
        infoLabel.setBorder(new EmptyBorder(6, 0, 0, 0));
        panel.add(infoLabel, BorderLayout.SOUTH);
        return panel;
    }
 
    private JPanel crearBarraEstado() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 4));
        panel.setBackground(new Color(44, 62, 80));
        JLabel lbl = new JLabel("💡 Selecciona un libro en la tabla para resaltarlo  |  Usa LinkedBST como estructura principal");
        lbl.setForeground(new Color(189, 195, 199));
        lbl.setFont(new Font("SansSerif", Font.ITALIC, 12));
        panel.add(lbl);
        return panel;
    }
 
    private void accionInsertar() {
        String isbn     = txtIsbn.getText().trim();
        String titulo   = txtTitulo.getText().trim();
        String autor    = txtAutor.getText().trim();
        String anioStr  = txtAnio.getText().trim();
        String categoria = txtCategoria.getText().trim();
 
        if (isbn.isEmpty() || titulo.isEmpty() || autor.isEmpty()) {
            mostrarError("ISBN, Título y Autor son obligatorios.");
            return;
        }
        int anio = 2024;
        if (!anioStr.isEmpty()) {
            try { anio = Integer.parseInt(anioStr); }
            catch (NumberFormatException ex) { mostrarError("El año debe ser un número."); return; }
        }
        if (categoria.isEmpty()) categoria = "General";
 
        Libro libro = new Libro(isbn, titulo, autor, anio, categoria);
        if (controlador.insertar(libro)) { actualizarTodo(); limpiarFormulario(); }
        else mostrarError("Ya existe un libro con ISBN: " + isbn);
    }
 
    private void accionEliminar() {
        String isbn = txtIsbn.getText().trim();
        if (isbn.isEmpty()) { mostrarError("Ingresa el ISBN a eliminar."); return; }
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el libro con ISBN: " + isbn + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controlador.eliminar(isbn)) actualizarTodo();
            else mostrarError("No se encontró el libro con ISBN: " + isbn);
        }
    }
 
    private void accionBuscarSimple() {
        String isbn = txtIsbn.getText().trim();
        if (isbn.isEmpty()) { mostrarError("Ingresa el ISBN a buscar."); return; }
        Libro libro = controlador.buscar(isbn);
        if (libro != null) {
            panelArbol.resaltarNodo(isbn);
            llenarFormulario(isbn);
            JOptionPane.showMessageDialog(this, "Libro encontrado:\n" + libro,
                    "Resultado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            panelArbol.limpiarResaltado();
            JOptionPane.showMessageDialog(this, "No se encontró ISBN: " + isbn,
                    "No encontrado", JOptionPane.WARNING_MESSAGE);
        }
    }
 
    private void accionBuscarPasos() {
        String isbn = txtBuscarIsbn.getText().trim();
        if (isbn.isEmpty()) { mostrarError("Ingresa el ISBN para buscar paso a paso."); return; }
        List<String> pasos = controlador.buscarConPasos(isbn);
        Libro libro = controlador.buscar(isbn);
        if (libro != null) panelArbol.resaltarNodo(isbn);
        else panelArbol.limpiarResaltado();
 
        JTextArea area = new JTextArea(String.join("\n", pasos));
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setBackground(new Color(28, 28, 35));
        area.setForeground(new Color(160, 255, 140));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(420, 230));
        JOptionPane.showMessageDialog(this, scroll,
                "🔍 Pasos de Búsqueda — ISBN: " + isbn, JOptionPane.PLAIN_MESSAGE);
        actualizarHistorial();
    }
 
    private void accionPrestar() {
        String isbn = txtIsbn.getText().trim();
        if (isbn.isEmpty()) { mostrarError("Ingresa el ISBN a prestar."); return; }
        if (controlador.prestarLibro(isbn)) actualizarTodo();
        else mostrarError("No se puede prestar. Verifica ISBN o estado.");
    }
 
    private void accionDevolver() {
        String isbn = txtIsbn.getText().trim();
        if (isbn.isEmpty()) { mostrarError("Ingresa el ISBN a devolver."); return; }
        if (controlador.devolverLibro(isbn)) actualizarTodo();
        else mostrarError("No se puede devolver. Verifica el ISBN.");
    }
 
    private void limpiarFormulario() {
        txtIsbn.setText(""); txtTitulo.setText(""); txtAutor.setText("");
        txtAnio.setText(""); txtCategoria.setText("");
        panelArbol.limpiarResaltado();
    }
 
    private void llenarFormulario(String isbn) {
        Libro l = controlador.buscar(isbn);
        if (l == null) return;
        txtIsbn.setText(l.getIsbn());
        txtTitulo.setText(l.getTitulo());
        txtAutor.setText(l.getAutor());
        txtAnio.setText(String.valueOf(l.getAnio()));
        txtCategoria.setText(l.getCategoria());
    }
 
    private void actualizarTodo() {
        panelArbol.setArbol(controlador.getArbol());
        actualizarTabla();
        actualizarHistorial();
        actualizarStats();
    }
 
    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Libro l : controlador.getArbol().inOrden()) {
            modeloTabla.addRow(new Object[]{
                l.getIsbn(), l.getTitulo(), l.getAutor(),
                l.getAnio(), l.getCategoria(),
                l.isDisponible() ? "✅ Disponible" : "🔴 Prestado"
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
        LinkedBST<Libro> arbol = controlador.getArbol();
        lblTotal.setText("Total: " + arbol.totalNodos());
        lblDisponibles.setText("Disponibles: " + arbol.totalDisponibles());
        lblPrestados.setText("Prestados: " + arbol.totalPrestados());
        lblAltura.setText("Altura: " + arbol.altura());
    }
 
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(color.brighter()); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(color); }
        });
        return btn;
    }
 
    private TitledBorder crearBorde(String titulo) {
        TitledBorder b = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true), titulo);
        b.setTitleFont(new Font("SansSerif", Font.BOLD, 12));
        b.setTitleColor(C_TEXTO_DARK);
        return b;
    }
 
    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
 
    private void cargarDatosEjemplo(LinkedBST<Libro> arbol) {
        Object[][] datos = {
            {"ISBN-500", "Estructuras de Datos",   "Mark Allen Weiss", 2014, "Informática"},
            {"ISBN-300", "Algoritmos",              "Thomas Cormen",    2009, "Informática"},
            {"ISBN-700", "Cálculo Multivariable",   "James Stewart",    2016, "Matemáticas"},
            {"ISBN-150", "El Quijote",              "Cervantes",        1605, "Literatura" },
            {"ISBN-400", "Bases de Datos",          "Silberschatz",     2011, "Informática"},
            {"ISBN-600", "Física Universitaria",    "Sears & Zemansky", 2013, "Ciencias"  },
            {"ISBN-800", "Ingeniería de Software",  "Ian Sommerville",  2016, "Ingeniería" },
            {"ISBN-200", "Diseño de Compiladores",  "Aho, Ullman",      2007, "Informática"},
            {"ISBN-450", "Redes de Computadores",   "Tanenbaum",        2012, "Redes"      },
        };
        for (Object[] d : datos)
            arbol.insertar(new Libro((String)d[0], (String)d[1], (String)d[2], (int)d[3], (String)d[4]));
        arbol.prestarLibro(new Libro("ISBN-300", "", "", 0, ""));
        arbol.prestarLibro(new Libro("ISBN-600", "", "", 0, ""));
    }
}

//Borrar