import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/** Student profile form. Reads/writes the existing Student object. */
public class ProfilePanel extends BasePage {
    private final JTextField name = new JTextField();
    private final JTextField roll = new JTextField();
    private final JTextField branch = new JTextField();
    private final JComboBox<String> semester = new JComboBox<>(new String[]{"1", "2", "3", "4", "5", "6", "7", "8"});
    private final JTextField cgpa = new JTextField();
    private final JComboBox<String> career = new JComboBox<>(CourseCatalog.CAREERS);
    private final JTextField skills = new JTextField();
    private final Map<String, JCheckBox> interestBoxes = new LinkedHashMap<>();
    private final Map<String, JTextField> markFields = new LinkedHashMap<>();
    private final Map<String, JCheckBox> courseBoxes = new LinkedHashMap<>();
    private final ModernButton save = Ui.button("Save Profile", ModernButton.Kind.PRIMARY, "Validate and save the profile");
    private final ModernButton edit = Ui.button("Edit Profile", ModernButton.Kind.SECONDARY, "Unlock the fields to make changes");
    private final ModernButton demo = Ui.button("Load demo student", ModernButton.Kind.SECONDARY,
            "Fill the form with the sample student from SAMPLE_TEST_RUN.txt");
    private final JLabel status = Ui.label(" ", Theme.bold(12.5f), Theme.TEAL);
    private boolean editing = true;

    public ProfilePanel(AppContext ctx, GUI gui) {
        super(ctx, gui);
        add(header("Student Profile", "Your details feed the recommendation engine. Interests and marks decide the match score."),
                BorderLayout.NORTH);

        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));

        // ---- personal details ----
        RoundedPanel personal = card("Personal details");
        JPanel form = Ui.transparent(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;
        for (JTextField f : new JTextField[]{name, roll, branch, cgpa, skills}) Ui.styleField(f);
        Ui.styleCombo(semester); Ui.styleCombo(career);
        name.setToolTipText("Full name of the student");
        roll.setToolTipText("Roll number / student ID");
        cgpa.setToolTipText("Current CGPA out of 10, for example 8.2");
        skills.setToolTipText("Comma separated, for example: Java, Python, SQL");
        addField(form, c, 0, 0, "Student Name *", name);
        addField(form, c, 1, 0, "Student ID / Roll No.", roll);
        addField(form, c, 0, 1, "Branch", branch);
        addField(form, c, 1, 1, "Semester / Year", semester);
        addField(form, c, 0, 2, "CGPA (0 - 10) *", cgpa);
        addField(form, c, 1, 2, "Career Goal *", career);
        c.gridx = 0; c.gridy = 6; c.gridwidth = 2;
        form.add(fieldBlock("Skills", skills), c);
        personal.add(form, BorderLayout.CENTER);
        personal.setAlignmentX(LEFT_ALIGNMENT);
        col.add(personal);
        col.add(Box.createVerticalStrut(16));

        // ---- interests + marks ----
        RoundedPanel interests = card("Interests and past marks");
        JPanel grid = Ui.transparent(new GridLayout(0, 2, 16, 8));
        for (String area : CourseCatalog.AREAS) {
            JCheckBox cb = new JCheckBox(area);
            cb.setOpaque(false);
            cb.setFont(Theme.regular(13.5f));
            cb.setForeground(Theme.TEXT);
            cb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            cb.setToolTipText("Tick if you are interested in " + area);
            JTextField mk = new JTextField(4);
            Ui.styleField(mk);
            mk.setToolTipText("Your past marks in " + area + " (0-100). Leave blank to estimate from CGPA.");
            cb.addItemListener(e -> mk.setEnabled(editing && cb.isSelected()));
            interestBoxes.put(area, cb);
            markFields.put(area, mk);
            JPanel row = Ui.transparent(new BorderLayout(10, 0));
            row.add(cb, BorderLayout.CENTER);
            JPanel mkp = Ui.transparent(new BorderLayout(6, 0));
            mkp.add(Ui.label("marks", Theme.regular(12f), Theme.MUTED), BorderLayout.WEST);
            mkp.add(mk, BorderLayout.CENTER);
            mkp.setPreferredSize(new Dimension(130, 36));
            row.add(mkp, BorderLayout.EAST);
            grid.add(row);
        }
        interests.add(grid, BorderLayout.CENTER);
        interests.setAlignmentX(LEFT_ALIGNMENT);
        col.add(interests);
        col.add(Box.createVerticalStrut(16));

        // ---- completed courses ----
        RoundedPanel done = card("Completed courses");
        JPanel cg = Ui.transparent(new GridLayout(0, 2, 16, 4));
        for (Course course : ctx.allCourses()) {
            JCheckBox cb = new JCheckBox(course.getCode() + "  " + course.getTitle());
            cb.setOpaque(false);
            cb.setFont(Theme.regular(13f));
            cb.setForeground(Theme.TEXT);
            cb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            courseBoxes.put(course.getCode(), cb);
            cg.add(cb);
        }
        done.add(cg, BorderLayout.CENTER);
        done.setAlignmentX(LEFT_ALIGNMENT);
        col.add(done);
        col.add(Box.createVerticalStrut(16));

        // ---- buttons ----
        JPanel btns = Ui.transparent(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btns.add(save); btns.add(edit); btns.add(demo); btns.add(status);
        btns.setAlignmentX(LEFT_ALIGNMENT);
        col.add(btns);
        col.add(Box.createVerticalStrut(8));

        save.addActionListener(e -> saveProfile());
        edit.addActionListener(e -> setEditing(true));
        demo.addActionListener(e -> loadDemo());

        JPanel wrap = new Ui.ScrollablePanel(new BorderLayout());
        wrap.add(col, BorderLayout.NORTH);
        add(Ui.scroll(wrap), BorderLayout.CENTER);
        setEditing(true);
    }

    private RoundedPanel card(String title) {
        RoundedPanel p = new RoundedPanel();
        p.setLayout(new BorderLayout(0, 10));
        p.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        p.add(Ui.section(title), BorderLayout.NORTH);
        return p;
    }

    private JPanel fieldBlock(String label, JComponent field) {
        JPanel p = Ui.transparent(new BorderLayout(0, 4));
        p.add(Ui.label(label, Theme.bold(12f), Theme.MUTED), BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private void addField(JPanel form, GridBagConstraints c, int x, int y, String label, JComponent f) {
        c.gridx = x; c.gridy = y * 2; c.gridwidth = 1; c.weightx = 1;
        form.add(fieldBlock(label, f), c);
    }

    // ---------------- editing state ----------------
    private void setEditing(boolean on) {
        editing = on;
        for (JTextField f : new JTextField[]{name, roll, branch, cgpa, skills}) f.setEditable(on);
        semester.setEnabled(on); career.setEnabled(on);
        for (String a : CourseCatalog.AREAS) {
            interestBoxes.get(a).setEnabled(on);
            markFields.get(a).setEnabled(on && interestBoxes.get(a).isSelected());
        }
        for (JCheckBox cb : courseBoxes.values()) cb.setEnabled(on);
        save.setEnabled(on);
        edit.setEnabled(!on);
        demo.setEnabled(on);
    }

    // ---------------- backend -> form ----------------
    @Override
    public void refresh() {
        Student s = ctx.getStudent();
        if (s == null) { if (!editing) setEditing(true); return; }
        fillFrom(s);
        setEditing(false);
    }

    private void fillFrom(Student s) {
        name.setText(s.getName());
        roll.setText(s.getRollNo());
        branch.setText(s.getBranch());
        semester.setSelectedItem(String.valueOf(s.getSemester()));
        cgpa.setText(String.valueOf(s.getCgpa()));
        career.setSelectedItem(s.getCareerGoal());
        skills.setText(s.getSkills());
        for (String a : CourseCatalog.AREAS) {
            boolean has = s.hasInterest(a);
            interestBoxes.get(a).setSelected(has);
            markFields.get(a).setText(has && s.hasMark(a) ? trim(s.getMark(a)) : "");
        }
        for (Map.Entry<String, JCheckBox> e : courseBoxes.entrySet()) e.getValue().setSelected(s.hasCompleted(e.getKey()));
    }

    private static String trim(double d) { return d == Math.floor(d) ? String.valueOf((int) d) : String.valueOf(d); }

    private void loadDemo() {
        Student d = new Student("Priyanka", 8.2, "AI Engineer");
        d.setRollNo("21CSE057"); d.setBranch("B.Tech CSE"); d.setSemester(4); d.setSkills("Java, Python, SQL");
        d.addInterest("AI"); d.setMark("AI", 85); d.addInterest("Data Science"); d.setMark("Data Science", 80);
        fillFrom(d);
        setEditing(true);
        status.setForeground(Theme.MUTED);
        status.setText("Demo data loaded - press Save Profile to apply.");
    }

    // ---------------- form -> backend ----------------
    private void saveProfile() {
        String n = name.getText().trim();
        if (n.isEmpty()) { error("Please enter the student name.", name); return; }
        double g;
        try { g = Double.parseDouble(cgpa.getText().trim()); }
        catch (NumberFormatException ex) { error("CGPA must be a number, for example 8.2.", cgpa); return; }
        if (g < 0 || g > 10) { error("CGPA must be between 0 and 10.", cgpa); return; }

        Map<String, Double> marks = new LinkedHashMap<>();
        for (String a : CourseCatalog.AREAS) {
            if (!interestBoxes.get(a).isSelected()) continue;
            String t = markFields.get(a).getText().trim();
            if (t.isEmpty()) continue;
            try {
                double m = Double.parseDouble(t);
                if (m < 0 || m > 100) throw new NumberFormatException();
                if (m > 0) marks.put(a, m);
            } catch (NumberFormatException ex) {
                error("Marks for " + a + " must be a number between 0 and 100.", markFields.get(a));
                return;
            }
        }
        int ok = JOptionPane.showConfirmDialog(gui, "Save this profile?", "Confirm", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (ok != JOptionPane.YES_OPTION) return;

        Student s = ctx.getStudent();
        String goal = (String) career.getSelectedItem();
        if (s == null) s = new Student(n, g, goal);
        else { s.setName(n); s.setCgpa(g); s.setCareerGoal(goal); }
        s.setRollNo(roll.getText().trim());
        s.setBranch(branch.getText().trim());
        s.setSemester(Integer.parseInt((String) semester.getSelectedItem()));
        s.setSkills(skills.getText().trim());
        s.clearInterestsAndMarks();
        for (String a : CourseCatalog.AREAS) if (interestBoxes.get(a).isSelected()) s.addInterest(a);
        for (Map.Entry<String, Double> e : marks.entrySet()) s.setMark(e.getKey(), e.getValue());
        s.clearCompleted();
        for (Map.Entry<String, JCheckBox> e : courseBoxes.entrySet()) if (e.getValue().isSelected()) s.completeCourse(e.getKey());

        boolean first = !ctx.hasStudent();
        if (first) ctx.setStudent(s); else ctx.fireChanged();
        status.setForeground(Theme.TEAL);
        status.setText("Profile saved.");
    }

    private void error(String msg, JComponent focus) {
        JOptionPane.showMessageDialog(gui, msg, "Invalid input", JOptionPane.WARNING_MESSAGE);
        focus.requestFocusInWindow();
    }
}
