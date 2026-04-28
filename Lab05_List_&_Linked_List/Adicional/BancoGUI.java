import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BancoGUI extends JFrame {

    // ── Paleta de colores ──────────────────────────────────────────────
    private static final Color C_BG        = new Color(0xF0F4F8);
    private static final Color C_PRIMARY   = new Color(0x1A3A5C);
    private static final Color C_ACCENT    = new Color(0xE8A020);
    private static final Color C_PREF      = new Color(0xC0392B);
    private static final Color C_NORMAL    = new Color(0x2980B9);
    private static final Color C_SUCCESS   = new Color(0x27AE60);
    private static final Color C_CARD_BG   = Color.WHITE;
    private static final Color C_TEXT      = new Color(0x2C3E50);
    private static final Color C_ARROW     = new Color(0x95A5A6);

    // ── Datos ─────────────────────────────────────────────────────────
    private ListLinked<Cliente> colaEspera   = new ListLinked<>();
    private ListLinked<Cliente> historialAtencion = new ListLinked<>();
    private int totalAtendidos = 0;

    // ── Componentes UI ───────────────────────────────────────────────
    private JTextField txtNombre;
    private JComboBox<String> cmbOperacion;
    private JCheckBox chkPreferencial;
    private LinkedListPanel panelCola;
    private LinkedListPanel panelHistorial;
    private JLabel lblEstado;
    private JLabel lblContadorEspera;
    private JLabel lblContadorAtendidos;
    private JPanel panelVentanilla;
    private Cliente clienteActual = null;

    public BancoGUI() {
        setTitle("Sistema de Cola Bancaria — Banco Andino");
        setSize(1150, 760);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(C_BG);

        buildUI();
        refreshAll();
    }

    // ─────────────────────────────────────────────────────────────────
    //  BUILD UI
    // ─────────────────────────────────────────────────────────────────
    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(C_BG);
        setContentPane(root);

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildCenter(), BorderLayout.CENTER);
        root.add(buildStatusBar(), BorderLayout.SOUTH);
    }

    // ── Header ────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0,0, C_PRIMARY, getWidth(), 0, new Color(0x0F2540));
                g2.setPaint(gp); g2.fillRect(0,0,getWidth(),getHeight());
                // stripe dorada
                g2.setColor(C_ACCENT);
                g2.fillRect(0, getHeight()-4, getWidth(), 4);
                g2.dispose();
            }
        };
        header.setPreferredSize(new Dimension(0, 72));
        header.setBorder(new EmptyBorder(0, 24, 4, 24));

        // Logo / título
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);
        JLabel ico = new JLabel("🏦");
        ico.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        JLabel title = new JLabel("BANCO ANDINO");
        title.setFont(new Font("Georgia", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        JLabel sub = new JLabel("  Sistema de Cola de Atención");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(0xB8C9DC));
        left.add(ico); left.add(title); left.add(sub);

        // Contadores header
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        right.setOpaque(false);
        lblContadorEspera   = makeBadgeLabel("En espera: 0", C_NORMAL);
        lblContadorAtendidos = makeBadgeLabel("Atendidos: 0", C_SUCCESS);
        right.add(lblContadorEspera);
        right.add(lblContadorAtendidos);

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JLabel makeBadgeLabel(String txt, Color bg) {
        JLabel l = new JLabel(txt) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg); g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        l.setForeground(Color.WHITE);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setBorder(new EmptyBorder(4,12,4,12));
        l.setOpaque(false);
        return l;
    }

    // ── Centro ────────────────────────────────────────────────────────
    private JPanel buildCenter() {
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(C_BG);
        center.setBorder(new EmptyBorder(16, 16, 8, 16));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.BOTH;
        g.insets = new Insets(0,6,0,6);

        // Columna izquierda: formulario + ventanilla
        g.gridx=0; g.gridy=0; g.weightx=0.28; g.weighty=1.0;
        center.add(buildLeftColumn(), g);

        // Columna centro: cola de espera (lista enlazada)
        g.gridx=1; g.weightx=0.44;
        center.add(buildQueueColumn(), g);

        // Columna derecha: historial
        g.gridx=2; g.weightx=0.28;
        center.add(buildHistorialColumn(), g);

        return center;
    }

    private JPanel buildLeftColumn() {
        JPanel col = new JPanel(new BorderLayout(0,12));
        col.setOpaque(false);
        col.add(buildFormCard(), BorderLayout.NORTH);
        col.add(buildVentanillaCard(), BorderLayout.CENTER);
        return col;
    }

    // ── Formulario ────────────────────────────────────────────────────
    private JPanel buildFormCard() {
        JPanel card = createCard("➕  Nuevo Turno");

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(4,0,4,0);
        g.anchor = GridBagConstraints.WEST;

        // Nombre
        g.gridx=0; g.gridy=0; g.weightx=1; g.fill=GridBagConstraints.HORIZONTAL;
        form.add(fieldLabel("👤 Nombre del cliente"), g);
        g.gridy=1;
        txtNombre = styledField("Ej: María Quispe");
        form.add(txtNombre, g);

        // Operación
        g.gridy=2; form.add(fieldLabel("💼 Tipo de operación"), g);
        g.gridy=3;
        cmbOperacion = new JComboBox<>(new String[]{"Depósito","Retiro","Préstamo","Consulta","Cambio de divisa"});
        styleCombo(cmbOperacion);
        form.add(cmbOperacion, g);

        // Preferencial
        g.gridy=4; g.insets=new Insets(8,0,4,0);
        chkPreferencial = new JCheckBox("⭐ Cliente preferencial (adulto mayor / embarazada)");
        chkPreferencial.setOpaque(false);
        chkPreferencial.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkPreferencial.setForeground(C_TEXT);
        form.add(chkPreferencial, g);

        // Botones
        g.gridy=5; g.insets=new Insets(10,0,4,0);
        JPanel btns = new JPanel(new GridLayout(1,2,8,0));
        btns.setOpaque(false);
        JButton btnNormal = createBtn("Turno Normal", C_NORMAL);
        JButton btnPref   = createBtn("Turno Prioritario", C_PREF);
        btnNormal.addActionListener(e -> agregarCliente(false));
        btnPref.addActionListener(e -> agregarCliente(true));
        btns.add(btnNormal); btns.add(btnPref);
        form.add(btns, g);

        card.add(form, BorderLayout.CENTER);
        return card;
    }

    // ── Ventanilla ────────────────────────────────────────────────────
    private JPanel buildVentanillaCard() {
        JPanel card = createCard("🏧  Ventanilla de Atención");

        panelVentanilla = new JPanel(new BorderLayout(0,10));
        panelVentanilla.setOpaque(false);

        JLabel lbl = new JLabel("Sin cliente en atención", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lbl.setForeground(new Color(0x95A5A6));
        panelVentanilla.add(lbl, BorderLayout.CENTER);

        JPanel btnsV = new JPanel(new GridLayout(2,1,0,6));
        btnsV.setOpaque(false);
        JButton btnAtender  = createBtn("▶  Atender Siguiente", C_SUCCESS);
        JButton btnFinalizar = createBtn("✔  Finalizar Atención", C_PRIMARY);
        btnAtender.addActionListener(e -> atenderSiguiente());
        btnFinalizar.addActionListener(e -> finalizarAtencion());
        btnsV.add(btnAtender); btnsV.add(btnFinalizar);

        card.add(panelVentanilla, BorderLayout.CENTER);
        card.add(btnsV, BorderLayout.SOUTH);
        return card;
    }

    // ── Cola de espera ────────────────────────────────────────────────
    private JPanel buildQueueColumn() {
        JPanel col = new JPanel(new BorderLayout(0,0));
        col.setOpaque(false);

        // título sección
        JLabel title = sectionTitle("📋  Cola de Espera — Lista Enlazada");
        col.add(title, BorderLayout.NORTH);

        panelCola = new LinkedListPanel();
        JScrollPane sp = new JScrollPane(panelCola);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(C_BG);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        col.add(sp, BorderLayout.CENTER);

        // Botones extras
        JPanel extras = new JPanel(new GridLayout(1,2,8,0));
        extras.setOpaque(false);
        extras.setBorder(new EmptyBorder(8,0,0,0));
        JButton btnBuscar  = createBtn("🔍 Buscar", new Color(0x8E44AD));
        JButton btnEliminar= createBtn("🗑 Eliminar por Nº", new Color(0xE74C3C));
        btnBuscar.addActionListener(e -> buscarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());
        extras.add(btnBuscar); extras.add(btnEliminar);
        col.add(extras, BorderLayout.SOUTH);
        return col;
    }

    // ── Historial ─────────────────────────────────────────────────────
    private JPanel buildHistorialColumn() {
        JPanel col = new JPanel(new BorderLayout(0,0));
        col.setOpaque(false);

        JLabel title = sectionTitle("📝  Historial de Atendidos");
        col.add(title, BorderLayout.NORTH);

        panelHistorial = new LinkedListPanel();
        panelHistorial.setShowAsHistory(true);
        JScrollPane sp = new JScrollPane(panelHistorial);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(C_BG);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        col.add(sp, BorderLayout.CENTER);

        JButton btnLimpiar = createBtn("🧹 Limpiar Historial", new Color(0x7F8C8D));
        btnLimpiar.setBorder(new EmptyBorder(8,0,0,0));
        btnLimpiar.addActionListener(e -> { historialAtencion = new ListLinked<>(); refreshAll(); });
        col.add(btnLimpiar, BorderLayout.SOUTH);
        return col;
    }

    // ── Status bar ────────────────────────────────────────────────────
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(0xDDE3EC));
        bar.setPreferredSize(new Dimension(0, 28));
        bar.setBorder(new EmptyBorder(0, 16, 0, 16));

        lblEstado = new JLabel("✅  Sistema listo. Agregue clientes para comenzar.");
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEstado.setForeground(C_TEXT);
        bar.add(lblEstado, BorderLayout.WEST);

        JLabel info = new JLabel("ListLinked<Cliente>  |  Estructura de Datos II");
        info.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        info.setForeground(new Color(0x7F8C8D));
        bar.add(info, BorderLayout.EAST);
        return bar;
    }

    // ─────────────────────────────────────────────────────────────────
    //  LÓGICA DE NEGOCIO
    // ─────────────────────────────────────────────────────────────────
    private void agregarCliente(boolean forzarPreferencial) {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            setEstado("⚠️  Por favor ingresa el nombre del cliente.", C_PREF);
            txtNombre.requestFocus(); return;
        }
        boolean pref = forzarPreferencial || chkPreferencial.isSelected();
        String op = (String) cmbOperacion.getSelectedItem();
        Cliente c = new Cliente(nombre, op, pref);

        if (pref) {
            colaEspera.insertFirst(c);     // preferencial va al frente
            setEstado("⭐  Cliente preferencial N°" + c.getNumero() + " agregado al inicio.", C_PREF);
        } else {
            colaEspera.insertLast(c);      // normal va al final
            setEstado("✅  Cliente N°" + c.getNumero() + " agregado a la cola.", C_SUCCESS);
        }

        txtNombre.setText("");
        chkPreferencial.setSelected(false);
        refreshAll();
    }

    private void atenderSiguiente() {
        if (clienteActual != null) {
            setEstado("⚠️  Finaliza la atención actual antes de llamar al siguiente.", C_ACCENT);
            return;
        }
        if (colaEspera.isEmpty()) {
            setEstado("ℹ️  La cola está vacía. No hay clientes en espera.", C_NORMAL);
            return;
        }
        // tomar el primero de la lista
        clienteActual = colaEspera.getFirstNode().dato;
        colaEspera.remove(clienteActual);
        refreshVentanilla();
        setEstado("🔔  Llamando a " + clienteActual.getEtiqueta() + " — " + clienteActual.getTipoOperacion(), C_SUCCESS);
        refreshAll();
    }

    private void finalizarAtencion() {
        if (clienteActual == null) {
            setEstado("ℹ️  No hay cliente en atención.", C_NORMAL); return;
        }
        historialAtencion.insertLast(clienteActual);
        totalAtendidos++;
        setEstado("✔️  " + clienteActual.getEtiqueta() + " atendido correctamente.", C_SUCCESS);
        clienteActual = null;
        refreshVentanilla();
        refreshAll();
    }

    private void buscarCliente() {
        String input = JOptionPane.showInputDialog(this, "Ingresa el número de ticket a buscar:", "Buscar cliente", JOptionPane.QUESTION_MESSAGE);
        if (input == null || input.trim().isEmpty()) return;
        try {
            int num = Integer.parseInt(input.trim());
            // recorrer manualmente para encontrar
            Node<Cliente> aux = colaEspera.getFirstNode();
            while (aux != null) {
                if (aux.dato.getNumero() == num) {
                    setEstado("🔍  Encontrado: " + aux.dato.getEtiqueta() + " — " + aux.dato.getTipoOperacion(), C_SUCCESS);
                    panelCola.setHighlightedNumber(num);
                    panelCola.repaint();
                    return;
                }
                aux = aux.next;
            }
            setEstado("❌  Cliente N°" + num + " no encontrado en la cola.", C_PREF);
            panelCola.setHighlightedNumber(-1);
        } catch (NumberFormatException ex) {
            setEstado("⚠️  Ingresa un número de ticket válido.", C_PREF);
        }
    }

    private void eliminarCliente() {
        String input = JOptionPane.showInputDialog(this, "Ingresa el número de ticket a eliminar:", "Eliminar cliente", JOptionPane.QUESTION_MESSAGE);
        if (input == null || input.trim().isEmpty()) return;
        try {
            int num = Integer.parseInt(input.trim());
            Node<Cliente> aux = colaEspera.getFirstNode();
            while (aux != null) {
                if (aux.dato.getNumero() == num) {
                    colaEspera.remove(aux.dato);
                    setEstado("🗑️  Cliente N°" + num + " eliminado de la cola.", C_ACCENT);
                    refreshAll(); return;
                }
                aux = aux.next;
            }
            setEstado("❌  No se encontró el cliente N°" + num + " en la cola.", C_PREF);
        } catch (NumberFormatException ex) {
            setEstado("⚠️  Ingresa un número válido.", C_PREF);
        }
    }

    // ─────────────────────────────────────────────────────────────────
    //  REFRESH
    // ─────────────────────────────────────────────────────────────────
    private void refreshAll() {
        // reconstruir lista para el panel
        List<Cliente> listaEspera = new ArrayList<>();
        Node<Cliente> aux = colaEspera.getFirstNode();
        while (aux != null) { listaEspera.add(aux.dato); aux = aux.next; }
        panelCola.setClientes(listaEspera);

        List<Cliente> listaHist = new ArrayList<>();
        aux = historialAtencion.getFirstNode();
        while (aux != null) { listaHist.add(aux.dato); aux = aux.next; }
        panelHistorial.setClientes(listaHist);

        lblContadorEspera.setText("En espera: " + colaEspera.size());
        lblContadorAtendidos.setText("Atendidos: " + totalAtendidos);
    }

    private void refreshVentanilla() {
        panelVentanilla.removeAll();
        if (clienteActual == null) {
            JLabel lbl = new JLabel("Sin cliente en atención", SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            lbl.setForeground(new Color(0x95A5A6));
            panelVentanilla.add(lbl, BorderLayout.CENTER);
        } else {
            JPanel info = new JPanel(new GridLayout(4,1,4,4));
            info.setOpaque(false);
            info.add(centeredLabel("🎫 Ticket N°" + clienteActual.getNumero(), 20, Font.BOLD, C_PRIMARY));
            info.add(centeredLabel(clienteActual.getNombre(), 15, Font.BOLD, C_TEXT));
            info.add(centeredLabel("💼 " + clienteActual.getTipoOperacion(), 12, Font.PLAIN, new Color(0x555555)));
            String tipo = clienteActual.isPreferencial() ? "⭐ Preferencial" : "Normal";
            Color tc = clienteActual.isPreferencial() ? C_PREF : C_NORMAL;
            info.add(centeredLabel(tipo, 12, Font.BOLD, tc));
            panelVentanilla.add(info, BorderLayout.CENTER);
        }
        panelVentanilla.revalidate();
        panelVentanilla.repaint();
    }

    private void setEstado(String msg, Color color) {
        lblEstado.setText(msg);
        lblEstado.setForeground(color);
    }

    // ─────────────────────────────────────────────────────────────────
    //  HELPERS UI
    // ─────────────────────────────────────────────────────────────────
    private JPanel createCard(String titulo) {
        JPanel card = new JPanel(new BorderLayout(0, 10)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(C_CARD_BG); g2.fillRoundRect(0,0,getWidth(),getHeight(),14,14);
                g2.setColor(new Color(0,0,0,15));
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,14,14);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(14,14,14,14));

        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(C_PRIMARY);
        lbl.setBorder(new MatteBorder(0,0,1,0,new Color(0xE0E8F0)));
        card.add(lbl, BorderLayout.NORTH);
        return card;
    }

    private JLabel sectionTitle(String txt) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        l.setForeground(C_PRIMARY);
        l.setBorder(new EmptyBorder(0,0,10,0));
        return l;
    }

    private JLabel fieldLabel(String txt) {
        JLabel l = new JLabel(txt);
        l.setFont(new Font("Segoe UI", Font.BOLD, 11));
        l.setForeground(C_TEXT);
        return l;
    }

    private JTextField styledField(String placeholder) {
        JTextField f = new JTextField(14) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setColor(new Color(0xAAAAAA));
                    g2.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                    Insets i = getInsets();
                    g2.drawString(placeholder, i.left+2, getHeight()/2 + 5);
                    g2.dispose();
                }
            }
        };
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xC5D0DC), 1, true),
            new EmptyBorder(6,8,6,8)));
        return f;
    }

    private void styleCombo(JComboBox<String> cb) {
        cb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cb.setBackground(Color.WHITE);
        cb.setBorder(BorderFactory.createLineBorder(new Color(0xC5D0DC),1,true));
    }

    private JButton createBtn(String txt, Color bg) {
        JButton b = new JButton(txt) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isPressed() ? bg.darker() : getModel().isRollover() ? bg.brighter() : bg;
                g2.setColor(c); g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setForeground(Color.WHITE);
        b.setOpaque(false); b.setContentAreaFilled(false); b.setBorderPainted(false);
        b.setBorder(new EmptyBorder(8,12,8,12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JLabel centeredLabel(String txt, int size, int style, Color color) {
        JLabel l = new JLabel(txt, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", style, size));
        l.setForeground(color);
        return l;
    }

    // ─────────────────────────────────────────────────────────────────
    //  INNER CLASS — Panel visual de lista enlazada
    // ─────────────────────────────────────────────────────────────────
    class LinkedListPanel extends JPanel {
        private List<Cliente> clientes = new ArrayList<>();
        private boolean showAsHistory = false;
        private int highlightedNumber = -1;

        // dimensiones de cada nodo
        private static final int NODE_W = 190;
        private static final int NODE_H = 72;
        private static final int ARROW_H = 24;
        private static final int MARGIN  = 14;

        public LinkedListPanel() {
            setBackground(C_BG);
            setLayout(null);
        }

        public void setClientes(List<Cliente> list) {
            this.clientes = list;
            int total = list.size();
            int prefHeight = total * (NODE_H + ARROW_H) + MARGIN * 2 + 60;
            setPreferredSize(new Dimension(NODE_W + MARGIN * 2, Math.max(300, prefHeight)));
            revalidate();
            repaint();
        }

        public void setShowAsHistory(boolean h) { this.showAsHistory = h; }
        public void setHighlightedNumber(int n) { this.highlightedNumber = n; }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int x = MARGIN;
            int y = MARGIN;

            if (clientes.isEmpty()) {
                g2.setColor(new Color(0xBBBBBB));
                g2.setFont(new Font("Segoe UI", Font.ITALIC, 13));
                g2.drawString(showAsHistory ? "Aún no se ha atendido a nadie." : "La cola está vacía.", x+10, y+40);
                g2.dispose(); return;
            }

            // Etiqueta HEAD
            g2.setColor(C_PRIMARY);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2.drawString("HEAD", x + NODE_W/2 - 16, y + 14);
            y += 20;

            for (int i = 0; i < clientes.size(); i++) {
                Cliente c = clientes.get(i);
                boolean isHighlighted = (c.getNumero() == highlightedNumber);
                boolean isPref = c.isPreferencial();

                // Sombra
                g2.setColor(new Color(0, 0, 0, 18));
                g2.fillRoundRect(x+3, y+3, NODE_W, NODE_H, 12, 12);

                // Fondo nodo
                Color nodeBg = isHighlighted ? new Color(0xFFF3CD)
                             : isPref ? new Color(0xFDECEB)
                             : C_CARD_BG;
                g2.setColor(nodeBg);
                g2.fillRoundRect(x, y, NODE_W, NODE_H, 12, 12);

                // Borde izquierdo de color
                Color borderColor = isPref ? C_PREF : (showAsHistory ? C_SUCCESS : C_NORMAL);
                g2.setColor(borderColor);
                g2.fillRoundRect(x, y, 5, NODE_H, 4, 4);

                // Borde exterior
                g2.setColor(isHighlighted ? C_ACCENT : new Color(0,0,0,20));
                g2.setStroke(new BasicStroke(isHighlighted ? 2f : 1f));
                g2.drawRoundRect(x, y, NODE_W, NODE_H, 12, 12);

                // Ticket número
                g2.setColor(borderColor);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                String ticketLabel = (isPref ? "★ " : "") + "N°" + c.getNumero();
                g2.drawString(ticketLabel, x + 12, y + 18);

                // Nombre
                g2.setColor(C_TEXT);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                String nombre = truncate(c.getNombre(), 18);
                g2.drawString(nombre, x + 12, y + 36);

                // Operación
                g2.setColor(new Color(0x666666));
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                g2.drawString("💼 " + c.getTipoOperacion(), x + 12, y + 52);

                // Puntero next en la derecha del nodo
                g2.setColor(new Color(0xDDDDDD));
                g2.fillRoundRect(x + NODE_W - 28, y + NODE_H/2 - 8, 22, 16, 6, 6);
                g2.setColor(new Color(0x999999));
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
                String nextTxt = (i < clientes.size()-1) ? "next" : "null";
                g2.drawString(nextTxt, x + NODE_W - 26, y + NODE_H/2 + 4);

                y += NODE_H;

                // Flecha hacia el siguiente
                if (i < clientes.size()-1) {
                    drawArrow(g2, x + NODE_W/2, y, x + NODE_W/2, y + ARROW_H);
                    y += ARROW_H;
                } else {
                    // NULL final
                    y += 8;
                    g2.setColor(new Color(0x95A5A6));
                    g2.setFont(new Font("Consolas", Font.BOLD, 12));
                    g2.drawString("NULL", x + NODE_W/2 - 14, y + 14);
                }
            }
            g2.dispose();
        }

        private void drawArrow(Graphics2D g2, int x1, int y1, int x2, int y2) {
            g2.setColor(C_ARROW);
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(x1, y1, x2, y2);
            // cabeza de flecha
            int[] xp = {x2-6, x2+6, x2};
            int[] yp = {y2-8, y2-8, y2};
            g2.fillPolygon(xp, yp, 3);
        }

        private String truncate(String s, int max) {
            return s.length() > max ? s.substring(0, max-2)+"…" : s;
        }
    }

    // ─────────────────────────────────────────────────────────────────
    //  MAIN
    // ─────────────────────────────────────────────────────────────────
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new BancoGUI().setVisible(true));
    }
}
