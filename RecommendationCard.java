import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** One recommendation (a Recommendation object from the engine) shown as a card. */
public class RecommendationCard extends RoundedPanel {
    private boolean hover;

    /** @param compact true = small dashboard version, false = full recommendations-page version */
    public RecommendationCard(GUI gui, Recommendation rec, int rank, boolean compact) {
        super(18, Theme.CARD);
        Course c = rec.getCourse();
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

        // ---- top row: title + chips (+ match score) ----
        JPanel top = Ui.transparent(new BorderLayout(10, 0));
        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        WrapText name = Ui.wrap((compact ? "" : "#" + rank + "  ") + c.getTitle(), 0, Theme.bold(compact ? 14.5f : 16.5f), Theme.TEXT);
        JLabel code = Ui.label(compact ? c.getCode() + "  \u2022  " + c.getArea()
                : c.getCode() + "  \u2022  " + c.getArea() + "  \u2022  " + c.getCredits() + " credits  \u2022  " + CourseInfo.level(c),
                Theme.regular(12f), Theme.MUTED);
        name.setAlignmentX(LEFT_ALIGNMENT); code.setAlignmentX(LEFT_ALIGNMENT);
        titles.add(name); titles.add(Box.createVerticalStrut(3)); titles.add(code);
        top.add(titles, BorderLayout.CENTER);
        top.add(Ui.typeChip(c), BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        // ---- middle: description, match bar, reason ----
        JPanel mid = new JPanel();
        mid.setOpaque(false);
        mid.setLayout(new BoxLayout(mid, BoxLayout.Y_AXIS));
        int textW = compact ? 250 : 620;

        WrapText desc = Ui.wrap(CourseInfo.shortDescription(c), textW, Theme.regular(13f), Theme.MUTED);
        desc.setAlignmentX(LEFT_ALIGNMENT);
        mid.add(desc);
        mid.add(Box.createVerticalStrut(10));

        JPanel match = Ui.transparent(new BorderLayout(10, 0));
        match.setAlignmentX(LEFT_ALIGNMENT);
        match.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        ProgressBarX bar = new ProgressBarX(rec.getScore() >= 70 ? Theme.TEAL : rec.getScore() >= 45 ? Theme.PRIMARY : Theme.AMBER, 8);
        bar.setValue(rec.getScore() / 100.0);
        match.add(Ui.label("Match", Theme.regular(12f), Theme.MUTED), BorderLayout.WEST);
        match.add(bar, BorderLayout.CENTER);
        match.add(Ui.label(String.format("%.0f%%", rec.getScore()), Theme.bold(13f), Theme.TEXT), BorderLayout.EAST);
        mid.add(match);
        mid.add(Box.createVerticalStrut(10));

        WrapText why = Ui.wrap("Why: " + CourseInfo.prettyReason(rec.getReason()), textW, Theme.regular(12.5f), new Color(0x334155));
        why.setAlignmentX(LEFT_ALIGNMENT);
        mid.add(why);
        add(mid, BorderLayout.CENTER);

        // ---- buttons ----
        JPanel btns = Ui.transparent(new FlowLayout(FlowLayout.LEFT, 8, 0));
        ModernButton view = Ui.button(compact ? "Explore" : "View Details", ModernButton.Kind.PRIMARY, "Open the full course details");
        view.addActionListener(e -> gui.showDetails(c));
        btns.add(view);
        if (!compact) {
            ModernButton done = Ui.button("Mark Completed", ModernButton.Kind.SECONDARY, "Add this course to your completed courses");
            done.addActionListener(e -> gui.confirmAndComplete(c));
            btns.add(done);
        }
        add(btns, BorderLayout.SOUTH);

        // hover effect
        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { hover = true;  setBorderColor(Theme.PRIMARY); }
            @Override public void mouseExited(MouseEvent e)  { hover = false; setBorderColor(Theme.BORDER); }
        });
    }
}
