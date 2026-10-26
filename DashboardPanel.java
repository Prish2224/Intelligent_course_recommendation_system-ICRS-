import javax.swing.*;
import java.awt.*;
import java.util.List;

/** Home screen: welcome, statistic cards, top recommendations and overall progress. */
public class DashboardPanel extends BasePage {
    private final JLabel welcome = Ui.title("Welcome back, Student!");
    private final JLabel sub = Ui.subtitle("");
    private final StatCard total     = new StatCard("\u25A4", "Total Courses", Theme.PRIMARY);
    private final StatCard recommended = new StatCard("\u2605", "Recommended Courses", Theme.AMBER);
    private final StatCard completed = new StatCard("\u2714", "Completed Courses", Theme.TEAL);
    private final StatCard progress  = new StatCard("\u25A6", "Progress", new Color(0x7C3AED));
    private final JPanel recoArea = Ui.transparent(new BorderLayout());
    private final ProgressBarX bar = new ProgressBarX(Theme.TEAL, 14);
    private final JLabel progText = Ui.label("", Theme.regular(13f), Theme.MUTED);
    private final JLabel progPct = Ui.label("", Theme.bold(22f), Theme.TEXT);

    public DashboardPanel(AppContext ctx, GUI gui) {
        super(ctx, gui);
        JPanel body = new Ui.ScrollablePanel(new BorderLayout());
        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));

        JPanel head = Ui.transparent(new BorderLayout());
        JPanel hl = new JPanel();
        hl.setOpaque(false);
        hl.setLayout(new BoxLayout(hl, BoxLayout.Y_AXIS));
        welcome.setAlignmentX(LEFT_ALIGNMENT); sub.setAlignmentX(LEFT_ALIGNMENT);
        hl.add(welcome); hl.add(Box.createVerticalStrut(4)); hl.add(sub);
        head.add(hl, BorderLayout.CENTER);
        head.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));
        head.setAlignmentX(LEFT_ALIGNMENT);
        col.add(head);

        JPanel stats = Ui.transparent(new GridLayout(1, 4, 16, 0));
        stats.add(total); stats.add(recommended); stats.add(completed); stats.add(progress);
        stats.setAlignmentX(LEFT_ALIGNMENT);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        col.add(stats);
        col.add(Box.createVerticalStrut(26));

        JPanel recHead = Ui.transparent(new BorderLayout());
        recHead.add(Ui.section("Recommended For You"), BorderLayout.WEST);
        ModernButton all = Ui.button("See all \u2192", ModernButton.Kind.SECONDARY, "Open the Recommendations screen");
        all.addActionListener(e -> gui.navigate(GUI.RECOMMEND));
        recHead.add(all, BorderLayout.EAST);
        recHead.setAlignmentX(LEFT_ALIGNMENT);
        recHead.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        col.add(recHead);
        col.add(Box.createVerticalStrut(10));
        recoArea.setAlignmentX(LEFT_ALIGNMENT);
        col.add(recoArea);
        col.add(Box.createVerticalStrut(26));

        RoundedPanel prog = new RoundedPanel();
        prog.setLayout(new BorderLayout(0, 12));
        prog.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        JPanel pt = Ui.transparent(new BorderLayout());
        pt.add(Ui.section("Your Progress"), BorderLayout.WEST);
        pt.add(progPct, BorderLayout.EAST);
        prog.add(pt, BorderLayout.NORTH);
        prog.add(bar, BorderLayout.CENTER);
        prog.add(progText, BorderLayout.SOUTH);
        prog.setAlignmentX(LEFT_ALIGNMENT);
        prog.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        col.add(prog);

        body.add(col, BorderLayout.NORTH);
        add(Ui.scroll(body), BorderLayout.CENTER);
    }

    @Override
    public void refresh() {
        boolean has = ctx.hasStudent();
        String first = has ? ctx.getStudent().getName().trim().split("\\s+")[0] : "Student";
        welcome.setText("Welcome back, " + first + "!");
        sub.setText(has ? "Here is your personalised academic overview."
                        : "Create your profile to unlock personalised recommendations.");

        List<Recommendation> recs = ctx.recommendations(ctx.totalCourses());
        total.setValue(String.valueOf(ctx.totalCourses()));
        recommended.setValue(String.valueOf(recs.size()));
        completed.setValue(String.valueOf(ctx.completedCount()));
        int pct = (int) Math.round(ctx.progress() * 100);
        progress.setValue(pct + "%");

        bar.setValue(ctx.progress());
        progPct.setText(pct + "%");
        progText.setText(ctx.completedCount() + " of " + ctx.totalCourses() + " courses completed  \u2022  "
                + ctx.earnedCredits() + " of " + ctx.totalCredits() + " credits earned");

        recoArea.removeAll();
        if (!has) {
            RoundedPanel box = new RoundedPanel();
            box.setLayout(new BorderLayout());
            box.add(noProfileState(), BorderLayout.CENTER);
            box.setPreferredSize(new Dimension(100, 290));
            JPanel demo = Ui.transparent(new FlowLayout(FlowLayout.CENTER, 0, 10));
            ModernButton d = Ui.button("Load demo student", ModernButton.Kind.SECONDARY,
                    "Fill in the sample student from SAMPLE_TEST_RUN.txt");
            d.addActionListener(e -> ctx.loadDemoStudent());
            demo.add(d);
            box.add(demo, BorderLayout.SOUTH);
            recoArea.add(box, BorderLayout.CENTER);
        } else if (recs.isEmpty()) {
            RoundedPanel box = new RoundedPanel();
            box.setLayout(new BorderLayout());
            box.setPreferredSize(new Dimension(100, 200));
            box.add(Ui.emptyState("\u2605", "No recommendations right now",
                    "Add interests to your profile, or you may have completed every suitable course.", null), BorderLayout.CENTER);
            recoArea.add(box, BorderLayout.CENTER);
        } else {
            int n = Math.min(3, recs.size());
            JPanel grid = Ui.transparent(new GridLayout(1, 3, 16, 0));
            for (int i = 0; i < 3; i++) {
                if (i < n) grid.add(new RecommendationCard(gui, recs.get(i), i + 1, true));
                else grid.add(Ui.transparent(new BorderLayout()));
            }
            recoArea.add(grid, BorderLayout.CENTER);
        }
        recoArea.revalidate(); recoArea.repaint();
    }
}
