import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CourseRegistrationGUI extends JFrame {

    // ─── Data ─────────────────────────────────────────────────────────────────
    private final ArrayList<Course> allCourses = new ArrayList<>();
    private ArrayList<Course> displayedCourses = new ArrayList<>();
    private int nextId = 1;

    // ─── Colors & Fonts ───────────────────────────────────────────────────────
    private static final Color PRIMARY      = new Color(25,  84,  166);
    private static final Color PRIMARY_DARK = new Color(15,  60,  120);
    private static final Color ACCENT       = new Color(0,  168,  107);
    private static final Color DANGER       = new Color(214,  48,   49);
    private static final Color WARN         = new Color(225, 112,   85);
    private static final Color BG           = new Color(245, 247, 251);
    private static final Color CARD         = Color.WHITE;
    private static final Color HEADER_BG    = new Color(37,  52,  83);
    private static final Color ROW_ALT      = new Color(237, 242, 253);
    private static final Color BORDER_COLOR = new Color(210, 218, 232);

    private static final Font TITLE_FONT   = new Font("Segoe UI", Font.BOLD,  20);
    private static final Font LABEL_FONT   = new Font("Segoe UI", Font.BOLD,  12);
    private static final Font INPUT_FONT   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font BUTTON_FONT  = new Font("Segoe UI", Font.BOLD,  13);
    private static final Font TABLE_FONT   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font HEADER_FONT  = new Font("Segoe UI", Font.BOLD,  13);

    // ─── Input Fields ─────────────────────────────────────────────────────────
    private JTextField tfCode, tfName, tfInstructor, tfCredits, tfCapacity, tfEnrolled, tfSearch;
    private JLabel lblStatus;

    // ─── Table ────────────────────────────────────────────────────────────────
    private JTable table;
    private CourseTableModel tableModel;

    // ─── Selected course being edited ─────────────────────────────────────────
    private Course selectedCourse = null;

    // ─── Constructor ─────────────────────────────────────────────────────────
    public CourseRegistrationGUI() {
        setTitle("Course Registration System");
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 580));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        seedData();

        add(buildHeader(),   BorderLayout.NORTH);
        add(buildCenter(),   BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);

        refreshTable(allCourses);
    }

    // ─── Header ───────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_BG);
        header.setBorder(new EmptyBorder(14, 24, 14, 24));

        JLabel title = new JLabel("  Course Registration System");
        title.setFont(TITLE_FONT);
        title.setForeground(Color.WHITE);
        title.setIcon(colorIcon(PRIMARY, 10, 10));

        JLabel subtitle = new JLabel("Manage all registered courses");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(180, 195, 220));

        JPanel titleBox = new JPanel();
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.setOpaque(false);
        titleBox.add(title);
        titleBox.add(Box.createVerticalStrut(2));
        titleBox.add(subtitle);

        header.add(titleBox, BorderLayout.WEST);
        return header;
    }

    // ─── Center (Form + Table) ────────────────────────────────────────────────
    private JSplitPane buildCenter() {
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, buildFormCard(), buildTableCard());
        split.setDividerSize(6);
        split.setDividerLocation(240);
        split.setBorder(null);
        split.setBackground(BG);
        return split;
    }

    // ─── Form Card ────────────────────────────────────────────────────────────
    private JPanel buildFormCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG);
        card.setBorder(new EmptyBorder(14, 16, 6, 16));

        JPanel inner = new JPanel(new BorderLayout(0, 10));
        inner.setBackground(CARD);
        inner.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(14, 18, 14, 18)
        ));

        JLabel sectionLabel = new JLabel("Course Details");
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sectionLabel.setForeground(PRIMARY);
        sectionLabel.setBorder(new EmptyBorder(0, 0, 6, 0));

        JPanel fields = buildFieldsPanel();
        JPanel buttons = buildButtonsPanel();

        inner.add(sectionLabel, BorderLayout.NORTH);
        inner.add(fields,       BorderLayout.CENTER);
        inner.add(buttons,      BorderLayout.SOUTH);

        card.add(inner, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildFieldsPanel() {
        JPanel grid = new JPanel(new GridLayout(2, 6, 12, 8));
        grid.setBackground(CARD);

        tfCode       = styledField("e.g. CS101");
        tfName       = styledField("e.g. Introduction to Programming");
        tfInstructor = styledField("e.g. Dr. Alice Reyes");
        tfCredits    = styledField("e.g. 3");
        tfCapacity   = styledField("e.g. 40");
        tfEnrolled   = styledField("e.g. 0");

        grid.add(labeledField("Course Code *",  tfCode));
        grid.add(labeledField("Course Name *",  tfName));
        grid.add(labeledField("Instructor *",   tfInstructor));
        grid.add(labeledField("Credits *",      tfCredits));
        grid.add(labeledField("Capacity *",     tfCapacity));
        grid.add(labeledField("Enrolled",       tfEnrolled));

        return grid;
    }

    private JPanel buildButtonsPanel() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
        row.setBackground(CARD);

        JButton btnAdd    = styledButton("Add Course",    ACCENT,   "ADD");
        JButton btnUpdate = styledButton("Update",        PRIMARY,  "UPDATE");
        JButton btnDelete = styledButton("Delete",        DANGER,   "DELETE");
        JButton btnClear  = styledButton("Clear / New",   WARN,     "CLEAR");

        btnAdd.addActionListener(e -> addCourse());
        btnUpdate.addActionListener(e -> updateCourse());
        btnDelete.addActionListener(e -> deleteCourse());
        btnClear.addActionListener(e -> clearForm());

        row.add(btnAdd);
        row.add(btnUpdate);
        row.add(btnDelete);
        row.add(Box.createHorizontalStrut(12));
        row.add(btnClear);

        return row;
    }

    // ─── Table Card ───────────────────────────────────────────────────────────
    private JPanel buildTableCard() {
        JPanel card = new JPanel(new BorderLayout(0, 0));
        card.setBackground(BG);
        card.setBorder(new EmptyBorder(6, 16, 14, 16));

        JPanel inner = new JPanel(new BorderLayout());
        inner.setBackground(CARD);
        inner.setBorder(new LineBorder(BORDER_COLOR, 1, true));

        inner.add(buildSearchBar(), BorderLayout.NORTH);
        inner.add(buildTable(),     BorderLayout.CENTER);

        card.add(inner, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildSearchBar() {
        JPanel bar = new JPanel(new BorderLayout(8, 0));
        bar.setBackground(new Color(250, 251, 255));
        bar.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(10, 14, 10, 14)
        ));

        JLabel lbl = new JLabel("Search: ");
        lbl.setFont(LABEL_FONT);
        lbl.setForeground(PRIMARY);

        tfSearch = styledField("Search by code, name, or instructor…");
        tfSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { applySearch(); }
            public void removeUpdate(DocumentEvent e)  { applySearch(); }
            public void changedUpdate(DocumentEvent e) { applySearch(); }
        });

        JButton btnClearSearch = new JButton("✕");
        btnClearSearch.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnClearSearch.setForeground(Color.GRAY);
        btnClearSearch.setBorderPainted(false);
        btnClearSearch.setContentAreaFilled(false);
        btnClearSearch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnClearSearch.addActionListener(e -> tfSearch.setText(""));

        bar.add(lbl,           BorderLayout.WEST);
        bar.add(tfSearch,      BorderLayout.CENTER);
        bar.add(btnClearSearch, BorderLayout.EAST);
        return bar;
    }

    private JScrollPane buildTable() {
        displayedCourses = new ArrayList<>(allCourses);
        tableModel = new CourseTableModel(displayedCourses);
        table = new JTable(tableModel);

        table.setFont(TABLE_FONT);
        table.setRowHeight(32);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(213, 228, 253));
        table.setSelectionForeground(Color.BLACK);
        table.setFillsViewportHeight(true);
        table.setGridColor(BORDER_COLOR);
        table.setBackground(CARD);

        JTableHeader header = table.getTableHeader();
        header.setFont(HEADER_FONT);
        header.setBackground(new Color(235, 240, 255));
        header.setForeground(PRIMARY_DARK);
        header.setReorderingAllowed(false);
        header.setBorder(new MatteBorder(0, 0, 2, 0, BORDER_COLOR));
        header.setPreferredSize(new Dimension(header.getWidth(), 36));

        table.setDefaultRenderer(Object.class,  new StripedRenderer());
        table.setDefaultRenderer(Integer.class, new StripedRenderer());

        TableColumnModel cm = table.getColumnModel();
        cm.getColumn(0).setPreferredWidth(40);
        cm.getColumn(1).setPreferredWidth(90);
        cm.getColumn(2).setPreferredWidth(240);
        cm.getColumn(3).setPreferredWidth(170);
        cm.getColumn(4).setPreferredWidth(65);
        cm.getColumn(5).setPreferredWidth(80);
        cm.getColumn(6).setPreferredWidth(80);
        cm.getColumn(7).setPreferredWidth(80);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) populateFormFromSelection();
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(CARD);
        return scroll;
    }

    // ─── Status Bar ───────────────────────────────────────────────────────────
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(230, 235, 245));
        bar.setBorder(new EmptyBorder(5, 16, 5, 16));

        lblStatus = new JLabel("Ready — select a row to edit, or fill the form to add a new course.");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStatus.setForeground(new Color(80, 90, 110));
        bar.add(lblStatus, BorderLayout.WEST);

        return bar;
    }

    // ─── CRUD Actions ────────────────────────────────────────────────────────

    private void addCourse() {
        if (!validateForm()) return;

        String code = tfCode.getText().trim().toUpperCase();
        if (findByCode(code, -1) != null) {
            showError("A course with code \"" + code + "\" already exists.");
            return;
        }

        Course c = new Course(
            nextId++,
            code,
            tfName.getText().trim(),
            tfInstructor.getText().trim(),
            Integer.parseInt(tfCredits.getText().trim()),
            Integer.parseInt(tfCapacity.getText().trim()),
            tfEnrolled.getText().trim().isEmpty() ? 0 : Integer.parseInt(tfEnrolled.getText().trim())
        );

        allCourses.add(c);
        applySearch();
        clearForm();
        setStatus("Course \"" + c.courseName + "\" added successfully.", ACCENT);
    }

    private void updateCourse() {
        if (selectedCourse == null) {
            showError("Please select a course from the table to update.");
            return;
        }
        if (!validateForm()) return;

        String code = tfCode.getText().trim().toUpperCase();
        Course clash = findByCode(code, selectedCourse.id);
        if (clash != null) {
            showError("Course code \"" + code + "\" is already used by another course.");
            return;
        }

        int capacity = Integer.parseInt(tfCapacity.getText().trim());
        int enrolled  = tfEnrolled.getText().trim().isEmpty() ? 0 : Integer.parseInt(tfEnrolled.getText().trim());

        if (enrolled > capacity) {
            showError("Enrolled count cannot exceed capacity.");
            return;
        }

        selectedCourse.courseCode  = code;
        selectedCourse.courseName  = tfName.getText().trim();
        selectedCourse.instructor  = tfInstructor.getText().trim();
        selectedCourse.credits     = Integer.parseInt(tfCredits.getText().trim());
        selectedCourse.capacity    = capacity;
        selectedCourse.enrolled    = enrolled;

        applySearch();
        setStatus("Course \"" + selectedCourse.courseName + "\" updated successfully.", PRIMARY);
    }

    private void deleteCourse() {
        if (selectedCourse == null) {
            showError("Please select a course from the table to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete \"" + selectedCourse.courseName + "\"?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            String name = selectedCourse.courseName;
            allCourses.remove(selectedCourse);
            selectedCourse = null;
            applySearch();
            clearForm();
            setStatus("Course \"" + name + "\" deleted.", DANGER);
        }
    }

    private void clearForm() {
        selectedCourse = null;
        table.clearSelection();
        tfCode.setText("");
        tfName.setText("");
        tfInstructor.setText("");
        tfCredits.setText("");
        tfCapacity.setText("");
        tfEnrolled.setText("");
        tfCode.requestFocus();
        setStatus("Form cleared — ready to add a new course.", new Color(100, 100, 100));
    }

    // ─── Search ──────────────────────────────────────────────────────────────

    private void applySearch() {
        String kw = tfSearch.getText().trim().toLowerCase();
        displayedCourses.clear();

        if (kw.isEmpty()) {
            displayedCourses.addAll(allCourses);
        } else {
            for (Course c : allCourses) {
                if (c.courseCode.toLowerCase().contains(kw)
                        || c.courseName.toLowerCase().contains(kw)
                        || c.instructor.toLowerCase().contains(kw)) {
                    displayedCourses.add(c);
                }
            }
        }
        tableModel.refresh();
        lblStatus.setText("Showing " + displayedCourses.size() + " of " + allCourses.size() + " courses.");
    }

    private void refreshTable(ArrayList<Course> list) {
        displayedCourses.clear();
        displayedCourses.addAll(list);
        if (tableModel != null) tableModel.refresh();
    }

    // ─── Form Helpers ────────────────────────────────────────────────────────

    private void populateFormFromSelection() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        selectedCourse = tableModel.getCourseAt(row);
        tfCode.setText(selectedCourse.courseCode);
        tfName.setText(selectedCourse.courseName);
        tfInstructor.setText(selectedCourse.instructor);
        tfCredits.setText(String.valueOf(selectedCourse.credits));
        tfCapacity.setText(String.valueOf(selectedCourse.capacity));
        tfEnrolled.setText(String.valueOf(selectedCourse.enrolled));
        setStatus("Editing: " + selectedCourse.courseName, PRIMARY);
    }

    private boolean validateForm() {
        if (tfCode.getText().trim().isEmpty()) { showError("Course Code is required."); tfCode.requestFocus(); return false; }
        if (tfName.getText().trim().isEmpty()) { showError("Course Name is required."); tfName.requestFocus(); return false; }
        if (tfInstructor.getText().trim().isEmpty()) { showError("Instructor is required."); tfInstructor.requestFocus(); return false; }

        try {
            int cr = Integer.parseInt(tfCredits.getText().trim());
            if (cr <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showError("Credits must be a positive whole number."); tfCredits.requestFocus(); return false;
        }

        try {
            int cap = Integer.parseInt(tfCapacity.getText().trim());
            if (cap <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showError("Capacity must be a positive whole number."); tfCapacity.requestFocus(); return false;
        }

        if (!tfEnrolled.getText().trim().isEmpty()) {
            try {
                int en = Integer.parseInt(tfEnrolled.getText().trim());
                if (en < 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                showError("Enrolled must be a non-negative whole number."); tfEnrolled.requestFocus(); return false;
            }
        }

        return true;
    }

    private Course findByCode(String code, int excludeId) {
        for (Course c : allCourses) {
            if (c.courseCode.equalsIgnoreCase(code) && c.id != excludeId) return c;
        }
        return null;
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    private void setStatus(String msg, Color color) {
        lblStatus.setText(msg);
        lblStatus.setForeground(color);
    }

    // ─── UI Factories ─────────────────────────────────────────────────────────

    private JTextField styledField(String placeholder) {
        JTextField tf = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(new Color(180, 185, 200));
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.drawString(placeholder, 8, getHeight() / 2 + 5);
                }
            }
        };
        tf.setFont(INPUT_FONT);
        tf.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(4, 8, 4, 8)
        ));
        tf.setBackground(Color.WHITE);
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { tf.setBorder(BorderFactory.createCompoundBorder(new LineBorder(PRIMARY, 2, true), new EmptyBorder(3, 7, 3, 7))); }
            public void focusLost(FocusEvent e)   { tf.setBorder(BorderFactory.createCompoundBorder(new LineBorder(BORDER_COLOR, 1, true), new EmptyBorder(4, 8, 4, 8))); }
        });
        return tf;
    }

    private JPanel labeledField(String label, JTextField field) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBackground(CARD);
        JLabel lbl = new JLabel(label);
        lbl.setFont(LABEL_FONT);
        lbl.setForeground(new Color(70, 80, 100));
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private JButton styledButton(String text, Color bg, String action) {
        JButton btn = new JButton(text);
        btn.setFont(BUTTON_FONT);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 18, 8, 18));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }

    private Icon colorIcon(Color color, int w, int h) {
        return new Icon() {
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.setColor(color);
                g.fillRoundRect(x, y, w, h, 4, 4);
            }
            public int getIconWidth()  { return w; }
            public int getIconHeight() { return h; }
        };
    }

    // ─── Striped Table Renderer ───────────────────────────────────────────────

    private class StripedRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? CARD : ROW_ALT);
            }
            setBorder(new EmptyBorder(0, 10, 0, 10));
            if (col == 7) {
                int slots = (value instanceof Integer) ? (int) value : 0;
                setForeground(slots == 0 ? DANGER : (slots < 5 ? WARN : new Color(30, 130, 76)));
            } else {
                setForeground(isSelected ? Color.BLACK : new Color(40, 50, 70));
            }
            return c;
        }
    }

    // ─── Seed Data ────────────────────────────────────────────────────────────

    private void seedData() {
        allCourses.add(new Course(nextId++, "CS101",   "Introduction to Programming",     "Dr. Alice Reyes",    3, 40, 38));
        allCourses.add(new Course(nextId++, "CS201",   "Data Structures & Algorithms",    "Prof. Ben Torres",   3, 35, 20));
        allCourses.add(new Course(nextId++, "MATH101", "Calculus I",                      "Dr. Clara Mendoza",  4, 50, 50));
        allCourses.add(new Course(nextId++, "ENG101",  "Technical Writing",               "Ms. Diana Cruz",     2, 30, 15));
        allCourses.add(new Course(nextId++, "CS301",   "Database Management Systems",     "Prof. Edwin Santos", 3, 30, 28));
        allCourses.add(new Course(nextId++, "CS401",   "Software Engineering",            "Dr. Fiona Lim",      3, 25, 10));
        allCourses.add(new Course(nextId++, "PHYS101", "Physics for Engineers",           "Dr. George Tan",     4, 45, 40));
        allCourses.add(new Course(nextId++, "CS202",   "Object-Oriented Programming",     "Prof. Ben Torres",   3, 35, 25));
    }

    // ─── Main ─────────────────────────────────────────────────────────────────

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new CourseRegistrationGUI().setVisible(true));
    }
}
