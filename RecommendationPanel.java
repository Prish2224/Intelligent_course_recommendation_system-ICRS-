import javax.swing.*;
import java.awt.*;
import java.util.List;

/** Full recommendations list produced by the existing RecommendationEngine. */
public class RecommendationPanel extends BasePage {
    private final JPanel list = new Ui.ScrollablePanel(new BorderLayout());
    private final JComboBox<String> topN = new JComboBox<>(new String[]{"Top 5", "Top 10", "All"});
    private final JLabel count = Ui.subtitle("");

    public RecommendationPanel(AppContext ctx, GUI gui) {
        super(ctx, gui);
        JPanel head = Ui.transparent(new BorderLayout());
        head.add(header("Personalized Course Recommendations",
                "Ranked by the RecommendationEngine: interest + career goal + performance fit + course-type bonus."), BorderLayout.CENTER);
        JPanel right = Ui.transparent(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        Ui.styleCombo(topN);
        topN.setToolTipText("How many recommendations to show");
        topN.addActionListener(e -> refresh());
        right.add(topN);
        head.add(right, BorderLayout.EAST);
        add(head, BorderLayout.NORTH);
        add(Ui.scroll(list), BorderLayout.CENTER);
    }

    @Override
    public void refresh() {
        list.removeAll();
        if (!ctx.hasStudent()) {
            list.add(noProfileState(), BorderLayout.CENTER);
            list.setPreferredSize(new Dimension(100, 400));
        } else {
            int n = topN.getSelectedIndex() == 0 ? 5 : topN.getSelectedIndex() == 1 ? 10 : ctx.totalCourses();
            List<Recommendation> recs = ctx.recommendations(n);
            if (recs.isEmpty()) {
                list.add(Ui.emptyState("\u2605", "No suitable courses found",
                        "Add interests or a career goal in your profile, or you may have completed everything.", null), BorderLayout.CENTER);
                list.setPreferredSize(new Dimension(100, 400));
            } else {
                list.setPreferredSize(null);
                JPanel col = new JPanel();
                col.setOpaque(false);
                col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
                int rank = 1;
                for (Recommendation r : recs) {
                    RecommendationCard card = new RecommendationCard(gui, r, rank++, false);
                    card.setAlignmentX(LEFT_ALIGNMENT);
                    col.add(card);
                    col.add(Box.createVerticalStrut(14));
                }
                list.add(col, BorderLayout.NORTH);
            }
        }
        list.revalidate(); list.repaint();
    }
}
