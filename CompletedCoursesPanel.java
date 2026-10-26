import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/** Shows the courses stored in Student.completedCourses, with a progress summary. */
public class CompletedCoursesPanel extends BasePage {
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"Code", "Course", "Type", "Credits", "Status"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final ProgressBarX bar = new ProgressBarX(Theme.TEAL, 12);
    private final JLabel summary = Ui.label("", Theme.regular(13.5f), Theme.MUTED);
    private final JLabel pct = Ui.label("", Theme.bold(22f), Theme.TEXT);
    private final CardLayout cl = new CardLayout();
    private final JPanel center = Ui.transparent(cl);
    private final JPanel emptyHolder = Ui.transparent(new BorderLayout());
    private final ModernButton view = Ui.button("View Details", ModernButton.Kind.PRIMARY, "Open details of the selected course");
    private final ModernButton undo = Ui.button("Mark as Not Completed", ModernButton.Kind.SECONDARY, "Remove the selected course from completed");

    public CompletedCoursesPanel(AppContext ctx, GUI gui) {
        super(ctx, gui);
        JPanel north = new JPanel();
        north.setOpaque(false);
        north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
        JPanel h = header("Completed Courses", "Courses you have finished. They are never recommended again.");
        h.setAlignmentX(LEFT_ALIGNMENT);
        north.add(h);

        RoundedPanel sum = new RoundedPanel();
        sum.setLayout(new BorderLayout(0, 10));
        sum.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        JPanel row = Ui.transparent(new BorderLayout());
        row.add(Ui.section("Progress summary"), BorderLayout.WEST);
        row.add(pct, BorderLayout.EAST);
        sum.add(row, BorderLayout.NORTH);
        sum.add(bar, BorderLayout.CENTER);
        sum.add(summary, BorderLayout.SOUTH);
        sum.setAlignmentX(LEFT_ALIGNMENT);
        sum.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        north.add(sum);
        north.add(Box.createVerticalStrut(16));

        JPanel bar2 = Ui.transparent(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bar2.add(view); bar2.add(undo);
        bar2.setAlignmentX(LEFT_ALIGNMENT);
        bar2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        north.add(bar2);
        north.add(Box.createVerticalStrut(10));
        add(north, BorderLayout.NORTH);

        Ui.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        int[] w = {80, 380, 100, 80, 120};
        for (int i = 0; i < w.length; i++) table.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        sp.getViewport().setBackground(Color.WHITE);
        center.add(sp, "table");
        center.add(emptyHolder, "empty");
        add(center, BorderLayout.CENTER);

        view.addActionListener(e -> { Course c = selected(); if (c != null) gui.showDetails(c); });
        undo.addActionListener(e -> {
            Course c = selected();
            if (c == null) return;
            int r = JOptionPane.showConfirmDialog(gui, "Mark \"" + c.getTitle() + "\" as not completed?", "Confirm",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (r == JOptionPane.YES_OPTION) ctx.markNotCompleted(c);
        });
    }

    private Course selected() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(gui, "Select a course in the table first.", "No course selected",
                    JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        return ctx.find((String) model.getValueAt(r, 0));
    }

    @Override
    public void refresh() {
        model.setRowCount(0);
        List<Course> done = ctx.completedCourses();
        for (Course c : done)
            model.addRow(new Object[]{c.getCode(), c.getTitle(), c.getCourseType(), c.getCredits(), "Completed"});
        bar.setValue(ctx.progress());
        pct.setText(Math.round(ctx.progress() * 100) + "%");
        summary.setText(done.size() + " of " + ctx.totalCourses() + " courses completed  \u2022  "
                + ctx.earnedCredits() + " of " + ctx.totalCredits() + " credits earned");
        view.setEnabled(!done.isEmpty());
        undo.setEnabled(!done.isEmpty());

        emptyHolder.removeAll();
        if (!ctx.hasStudent()) emptyHolder.add(noProfileState());
        else {
            ModernButton browse = Ui.button("Browse catalog", ModernButton.Kind.PRIMARY, "Go to the Course Catalog");
            browse.addActionListener(e -> gui.navigate(GUI.CATALOG));
            emptyHolder.add(Ui.emptyState("\u2714", "No completed courses yet",
                    "Mark courses as completed from the catalog or the recommendations.", browse));
        }
        cl.show(center, done.isEmpty() ? "empty" : "table");
        emptyHolder.revalidate(); emptyHolder.repaint();
    }
}
