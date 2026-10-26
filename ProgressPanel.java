import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/** Academic progress dashboard: stat cards, progress bars and a Swing-drawn bar chart. */
public class ProgressPanel extends BasePage {
    private final StatCard total = new StatCard("\u25A4", "Total Courses", Theme.PRIMARY);
    private final StatCard done = new StatCard("\u2714", "Completed Courses", Theme.TEAL);
    private final StatCard left = new StatCard("\u25D4", "Remaining Courses", Theme.AMBER);
    private final StatCard pct = new StatCard("\u25A6", "Completion", new Color(0x7C3AED));
    private final ProgressBarX overall = new ProgressBarX(Theme.TEAL, 16);
    private final ProgressBarX credits = new ProgressBarX(Theme.PRIMARY, 16);
    private final JLabel overallTxt = Ui.label("", Theme.regular(13f), Theme.MUTED);
    private final JLabel creditsTxt = Ui.label("", Theme.regular(13f), Theme.MUTED);
    private final ProgressBarX coreBar = new ProgressBarX(Theme.PRIMARY, 10);
    private final ProgressBarX elecBar = new ProgressBarX(Theme.TEAL, 10);
    private final JLabel coreTxt = Ui.label("", Theme.bold(12.5f), Theme.TEXT);
    private final JLabel elecTxt = Ui.label("", Theme.bold(12.5f), Theme.TEXT);
    private final BarChart chart = new BarChart();
    private final JLabel note = Ui.subtitle("");

    public ProgressPanel(AppContext ctx, GUI gui) {
        super(ctx, gui);
        add(header("Progress", "Your academic progress calculated from the completed courses."), BorderLayout.NORTH);

        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));

        JPanel stats = Ui.transparent(new GridLayout(1, 4, 16, 0));
        stats.add(total); stats.add(done); stats.add(left); stats.add(pct);
        stats.setAlignmentX(LEFT_ALIGNMENT);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        col.add(stats);
        col.add(Box.createVerticalStrut(16));

        RoundedPanel bars = new RoundedPanel();
        bars.setLayout(new GridLayout(1, 2, 30, 0));
        bars.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        bars.add(barBlock("Overall completion", overall, overallTxt));
        bars.add(barBlock("Credits earned", credits, creditsTxt));
        bars.setAlignmentX(LEFT_ALIGNMENT);
        bars.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        col.add(bars);
        col.add(Box.createVerticalStrut(16));

        JPanel two = Ui.transparent(new GridLayout(1, 2, 16, 0));
        RoundedPanel chartCard = new RoundedPanel();
        chartCard.setLayout(new BorderLayout(0, 12));
        chartCard.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        chartCard.add(Ui.section("Completed courses by category"), BorderLayout.NORTH);
        chartCard.add(chart, BorderLayout.CENTER);
        two.add(chartCard);

        RoundedPanel typeCard = new RoundedPanel();
        typeCard.setLayout(new BoxLayout(typeCard, BoxLayout.Y_AXIS));
        typeCard.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        JLabel th = Ui.section("Core vs Elective");
        th.setAlignmentX(LEFT_ALIGNMENT);
        typeCard.add(th);
        typeCard.add(Box.createVerticalStrut(16));
        typeCard.add(rowOf("Core courses", coreBar, coreTxt));
        typeCard.add(Box.createVerticalStrut(18));
        typeCard.add(rowOf("Elective courses", elecBar, elecTxt));
        typeCard.add(Box.createVerticalStrut(18));
        note.setAlignmentX(LEFT_ALIGNMENT);
        typeCard.add(note);
        typeCard.add(Box.createVerticalGlue());
        two.add(typeCard);
        two.setAlignmentX(LEFT_ALIGNMENT);
        two.setPreferredSize(new Dimension(100, 330));
        two.setMaximumSize(new Dimension(Integer.MAX_VALUE, 330));
        col.add(two);

        JPanel wrap = new Ui.ScrollablePanel(new BorderLayout());
        wrap.add(col, BorderLayout.NORTH);
        add(Ui.scroll(wrap), BorderLayout.CENTER);
    }

    private JPanel barBlock(String title, ProgressBarX bar, JLabel text) {
        JPanel p = Ui.transparent(new BorderLayout(0, 10));
        p.add(Ui.label(title, Theme.bold(14f), Theme.TEXT), BorderLayout.NORTH);
        p.add(bar, BorderLayout.CENTER);
        p.add(text, BorderLayout.SOUTH);
        return p;
    }

    private JPanel rowOf(String title, ProgressBarX bar, JLabel value) {
        JPanel p = Ui.transparent(new BorderLayout(0, 6));
        JPanel t = Ui.transparent(new BorderLayout());
        t.add(Ui.label(title, Theme.regular(13f), Theme.MUTED), BorderLayout.WEST);
        t.add(value, BorderLayout.EAST);
        p.add(t, BorderLayout.NORTH);
        p.add(bar, BorderLayout.CENTER);
        p.setAlignmentX(LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        return p;
    }

    @Override
    public void refresh() {
        total.setValue(String.valueOf(ctx.totalCourses()));
        done.setValue(String.valueOf(ctx.completedCount()));
        left.setValue(String.valueOf(ctx.remainingCount()));
        int p = (int) Math.round(ctx.progress() * 100);
        pct.setValue(p + "%");
        overall.setValue(ctx.progress());
        overallTxt.setText(ctx.completedCount() + " of " + ctx.totalCourses() + " courses  (" + p + "%)");
        credits.setValue(ctx.totalCredits() == 0 ? 0 : (double) ctx.earnedCredits() / ctx.totalCredits());
        creditsTxt.setText(ctx.earnedCredits() + " of " + ctx.totalCredits() + " credits");

        int coreT = 0, coreD = 0, elT = 0, elD = 0;
        Map<String, int[]> byArea = new LinkedHashMap<>();
        for (String a : CourseCatalog.AREAS) byArea.put(a, new int[2]);
        for (Course c : ctx.allCourses()) {
            boolean d = ctx.isCompleted(c);
            if (c.getCourseType().equals("CORE")) { coreT++; if (d) coreD++; } else { elT++; if (d) elD++; }
            int[] a = byArea.get(c.getArea());
            if (a != null) { a[1]++; if (d) a[0]++; }
        }
        coreBar.setValue(coreT == 0 ? 0 : (double) coreD / coreT);
        elecBar.setValue(elT == 0 ? 0 : (double) elD / elT);
        coreTxt.setText(coreD + " / " + coreT);
        elecTxt.setText(elD + " / " + elT);
        chart.setData(new ArrayList<>(byArea.keySet()), new ArrayList<>(byArea.values()));
        note.setText(ctx.hasStudent() ? ctx.remainingCount() + " courses still to go. Keep it up!"
                : "Create a profile to start tracking progress.");
    }
}
