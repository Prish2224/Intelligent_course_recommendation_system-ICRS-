import javax.swing.*;
import java.awt.*;

/** Detail screen for one Course (shown inside the main area, not as a pop-up). */
public class CourseDetailsPanel extends BasePage {
    private Course course;
    private final JPanel box = Ui.transparent(new BorderLayout());

    public CourseDetailsPanel(AppContext ctx, GUI gui) {
        super(ctx, gui);
        add(Ui.scroll(box), BorderLayout.CENTER);
    }

    public void setCourse(Course c) { this.course = c; refresh(); }

    @Override
    public void refresh() {
        box.removeAll();
        if (course == null) { box.revalidate(); return; }
        Course c = course;

        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));

        ModernButton back = Ui.button("\u2190 Back", ModernButton.Kind.SECONDARY, "Return to the previous screen");
        back.addActionListener(e -> gui.goBack());
        JPanel backRow = Ui.transparent(new FlowLayout(FlowLayout.LEFT, 0, 0));
        backRow.add(back);
        backRow.setAlignmentX(LEFT_ALIGNMENT);
        backRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        col.add(backRow);
        col.add(Box.createVerticalStrut(14));

        RoundedPanel card = new RoundedPanel();
        card.setLayout(new BorderLayout(0, 16));
        card.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JPanel top = Ui.transparent(new BorderLayout(12, 0));
        JPanel t = new JPanel();
        t.setOpaque(false);
        t.setLayout(new BoxLayout(t, BoxLayout.Y_AXIS));
        JLabel title = Ui.label(c.getTitle(), Theme.bold(24f), Theme.TEXT);
        JLabel code = Ui.label(c.getCode() + "  \u2022  " + c.getArea(), Theme.regular(13.5f), Theme.MUTED);
        title.setAlignmentX(LEFT_ALIGNMENT); code.setAlignmentX(LEFT_ALIGNMENT);
        t.add(title); t.add(Box.createVerticalStrut(4)); t.add(code);
        top.add(t, BorderLayout.CENTER);
        top.add(Ui.typeChip(c), BorderLayout.EAST);
        card.add(top, BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        JLabel dh = Ui.label("Description", Theme.bold(13f), Theme.MUTED);
        WrapText d = Ui.wrap(CourseInfo.description(c), 640, Theme.regular(14f), Theme.TEXT);
        dh.setAlignmentX(LEFT_ALIGNMENT); d.setAlignmentX(LEFT_ALIGNMENT);
        body.add(dh); body.add(Box.createVerticalStrut(4)); body.add(d);
        body.add(Box.createVerticalStrut(18));

        JPanel facts = Ui.transparent(new GridLayout(2, 3, 14, 14));
        facts.add(fact("Course code", c.getCode()));
        facts.add(fact("Type", c.getCourseType()));
        facts.add(fact("Credits", String.valueOf(c.getCredits())));
        facts.add(fact("Category", c.getArea()));
        facts.add(fact("Level", CourseInfo.level(c) + "  (" + c.getDifficulty() + "/3)"));
        facts.add(fact("Prerequisites", "None listed in catalog"));
        facts.setAlignmentX(LEFT_ALIGNMENT);
        body.add(facts);
        body.add(Box.createVerticalStrut(18));

        JLabel ch = Ui.label("Helps these careers", Theme.bold(13f), Theme.MUTED);
        ch.setAlignmentX(LEFT_ALIGNMENT);
        body.add(ch); body.add(Box.createVerticalStrut(6));
        JPanel chips = Ui.transparent(new FlowLayout(FlowLayout.LEFT, 0, 0));
        for (String career : c.getCareers()) {
            JPanel w = Ui.transparent(new BorderLayout());
            w.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
            w.add(Ui.chip(career, Theme.PRIMARY));
            chips.add(w);
        }
        chips.setAlignmentX(LEFT_ALIGNMENT);
        body.add(chips);
        body.add(Box.createVerticalStrut(18));

        // recommended status (from the engine)
        JLabel sh = Ui.label("Recommended status", Theme.bold(13f), Theme.MUTED);
        sh.setAlignmentX(LEFT_ALIGNMENT);
        body.add(sh); body.add(Box.createVerticalStrut(4));
        String status; Color col2;
        Recommendation rec = ctx.hasStudent() ? ctx.recommendationFor(c) : null;
        if (!ctx.hasStudent())          { status = "Create a profile to see personalised status."; col2 = Theme.MUTED; }
        else if (ctx.isCompleted(c))    { status = "Completed"; col2 = Theme.TEAL; }
        else if (rec != null)           { status = String.format("Recommended - %.0f%% match.  %s", rec.getScore(),
                                              CourseInfo.prettyReason(rec.getReason())); col2 = Theme.PRIMARY; }
        else                            { status = "Not recommended for your current profile."; col2 = Theme.AMBER; }
        WrapText st = Ui.wrap(status, 640, Theme.bold(14f), col2);
        st.setAlignmentX(LEFT_ALIGNMENT);
        body.add(st);
        card.add(body, BorderLayout.CENTER);

        // actions
        JPanel btns = Ui.transparent(new FlowLayout(FlowLayout.LEFT, 10, 0));
        boolean done = ctx.isCompleted(c);
        ModernButton complete = Ui.button(done ? "Completed" : "Mark as Completed", ModernButton.Kind.SUCCESS,
                "Add this course to your completed courses");
        complete.setEnabled(!done);
        complete.addActionListener(e -> gui.confirmAndComplete(c));
        ModernButton interest = Ui.button("Add to Interests", ModernButton.Kind.PRIMARY,
                "Add \"" + c.getArea() + "\" to your interests so similar courses rank higher");
        boolean has = ctx.hasStudent() && ctx.getStudent().hasInterest(c.getArea());
        interest.setEnabled(!has);
        if (has) interest.setText("In Interests");
        interest.addActionListener(e -> {
            if (!gui.requireProfile()) return;
            int r = JOptionPane.showConfirmDialog(gui, "Add \"" + c.getArea() + "\" to your interests?\nThis changes your recommendations.",
                    "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (r == JOptionPane.YES_OPTION) ctx.addInterest(c);
        });
        ModernButton b2 = Ui.button("Back", ModernButton.Kind.SECONDARY, "Return to the previous screen");
        b2.addActionListener(e -> gui.goBack());
        btns.add(complete); btns.add(interest); btns.add(b2);
        card.add(btns, BorderLayout.SOUTH);

        card.setAlignmentX(LEFT_ALIGNMENT);
        col.add(card);
        JPanel wrap = new Ui.ScrollablePanel(new BorderLayout());
        wrap.add(col, BorderLayout.NORTH);
        box.add(wrap, BorderLayout.CENTER);
        box.revalidate(); box.repaint();
    }

    private JPanel fact(String k, String v) {
        RoundedPanel p = new RoundedPanel(12, new Color(0xF8FAFC));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        JLabel a = Ui.label(k, Theme.regular(11.5f), Theme.MUTED);
        JLabel b = Ui.label(v, Theme.bold(14f), Theme.TEXT);
        a.setAlignmentX(LEFT_ALIGNMENT); b.setAlignmentX(LEFT_ALIGNMENT);
        p.add(a); p.add(Box.createVerticalStrut(2)); p.add(b);
        return p;
    }
}
