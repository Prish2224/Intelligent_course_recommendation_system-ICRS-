import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/** Browsable, searchable, filterable catalog - data comes from CourseCatalog. */
public class CourseCatalogPanel extends BasePage {
    private static final String[] COLS = {"Code", "Course Name", "Type", "Credits", "Category", "Level", "Status"};
    private final DefaultTableModel model = new DefaultTableModel(COLS, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
    private final JTextField search = new JTextField(18);
    private final JComboBox<String> type = new JComboBox<>(new String[]{"All types", "CORE", "ELECTIVE"});
    private final JComboBox<String> area = new JComboBox<>();
    private final JLabel count = Ui.subtitle("");
    private final CardLayout cl = new CardLayout();
    private final JPanel center = Ui.transparent(cl);
    private final ModernButton view = Ui.button("View Details", ModernButton.Kind.PRIMARY, "Open details of the selected course (or double-click a row)");

    public CourseCatalogPanel(AppContext ctx, GUI gui) {
        super(ctx, gui);
        add(header("Course Catalog", "All courses available in the CourseCatalog. Search, filter and open any course."), BorderLayout.NORTH);

        // toolbar
        JPanel bar = Ui.transparent(new FlowLayout(FlowLayout.LEFT, 10, 0));
        Ui.styleField(search);
        search.setToolTipText("Search by course name, code or category");
        search.putClientProperty("JTextField.placeholderText", "Search courses...");
        area.addItem("All categories");
        for (String a : CourseCatalog.AREAS) area.addItem(a);
        Ui.styleCombo(type); Ui.styleCombo(area);
        type.setToolTipText("Filter by course type");
        area.setToolTipText("Filter by category / domain");
        bar.add(Ui.label("Search", Theme.bold(12.5f), Theme.MUTED));
        bar.add(search); bar.add(type); bar.add(area); bar.add(view);

        // table
        Ui.styleTable(table);
        table.setRowSorter(sorter);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        int[] widths = {70, 300, 90, 70, 120, 110, 110};
        for (int i = 0; i < widths.length; i++) table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        sp.getViewport().setBackground(Color.WHITE);

        center.add(sp, "table");
        center.add(Ui.emptyState("\u2315", "No courses match your search", "Try a different keyword or clear the filters.", null), "empty");

        JPanel body = Ui.transparent(new BorderLayout(0, 12));
        body.add(bar, BorderLayout.NORTH);
        body.add(center, BorderLayout.CENTER);
        body.add(count, BorderLayout.SOUTH);
        add(body, BorderLayout.CENTER);

        DocumentListener dl = new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { applyFilter(); }
            public void removeUpdate(DocumentEvent e)  { applyFilter(); }
            public void changedUpdate(DocumentEvent e) { applyFilter(); }
        };
        search.getDocument().addDocumentListener(dl);
        type.addActionListener(e -> applyFilter());
        area.addActionListener(e -> applyFilter());
        view.addActionListener(e -> openSelected());
        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { if (e.getClickCount() == 2) openSelected(); }
        });
        table.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "open");
        table.getActionMap().put("open", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { openSelected(); }
        });
    }

    private void openSelected() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(gui, "Select a course in the table first.", "No course selected",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String code = (String) model.getValueAt(table.convertRowIndexToModel(r), 0);
        gui.showDetails(ctx.find(code));
    }

    private void applyFilter() {
        final String q = search.getText().trim().toLowerCase();
        final String t = (String) type.getSelectedItem();
        final String a = (String) area.getSelectedItem();
        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> en) {
                String code = en.getStringValue(0), nm = en.getStringValue(1), ty = en.getStringValue(2), ar = en.getStringValue(4);
                if (!t.equals("All types") && !ty.equals(t)) return false;
                if (!a.equals("All categories") && !ar.equals(a)) return false;
                return q.isEmpty() || code.toLowerCase().contains(q) || nm.toLowerCase().contains(q) || ar.toLowerCase().contains(q);
            }
        });
        updateCount();
    }

    private void updateCount() {
        int shown = table.getRowCount(), all = model.getRowCount();
        count.setText("Showing " + shown + " of " + all + " courses");
        cl.show(center, shown == 0 ? "empty" : "table");
    }

    @Override
    public void refresh() {
        String selected = table.getSelectedRow() >= 0 ? (String) model.getValueAt(table.convertRowIndexToModel(table.getSelectedRow()), 0) : null;
        model.setRowCount(0);
        for (Course c : ctx.allCourses()) {
            model.addRow(new Object[]{c.getCode(), c.getTitle(), c.getCourseType(), c.getCredits(), c.getArea(),
                    CourseInfo.level(c), ctx.isCompleted(c) ? "Completed" : "Not taken"});
        }
        applyFilter();
        if (selected != null)
            for (int i = 0; i < table.getRowCount(); i++)
                if (model.getValueAt(table.convertRowIndexToModel(i), 0).equals(selected)) { table.setRowSelectionInterval(i, i); break; }
    }
}
