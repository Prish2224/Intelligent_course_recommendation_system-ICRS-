import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/** Simple grouped horizontal bar chart (completed vs total per label) - Swing/Java2D only. */
public class BarChart extends JComponent {
    private final List<String> labels = new ArrayList<>();
    private final List<int[]> data = new ArrayList<>();   // {completed, total}

    public BarChart() {
        setOpaque(false);
        setPreferredSize(new Dimension(400, 280));
    }

    public void setData(List<String> labels, List<int[]> values) {
        this.labels.clear(); this.labels.addAll(labels);
        this.data.clear();   this.data.addAll(values);
        setPreferredSize(new Dimension(400, Math.max(120, labels.size() * 34 + 10)));
        revalidate(); repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(Theme.regular(12.5f));
        FontMetrics fm = g2.getFontMetrics();
        int labelW = 110, numW = 46, rowH = 34, barH = 14;
        int trackW = Math.max(40, getWidth() - labelW - numW - 8);
        for (int i = 0; i < labels.size(); i++) {
            int y = i * rowH + 6;
            int done = data.get(i)[0], total = Math.max(1, data.get(i)[1]);
            g2.setColor(Theme.MUTED);
            g2.drawString(labels.get(i), 0, y + barH - 2);
            g2.setColor(Theme.TRACK);
            g2.fillRoundRect(labelW, y, trackW, barH, barH, barH);
            int fw = (int) Math.round(trackW * (double) done / total);
            if (fw > 0) {
                g2.setColor(Theme.TEAL);
                g2.fillRoundRect(labelW, y, Math.max(fw, barH), barH, barH, barH);
            }
            g2.setColor(Theme.TEXT);
            String n = done + "/" + data.get(i)[1];
            g2.drawString(n, labelW + trackW + 10, y + barH - 2);
        }
        g2.dispose();
    }
}
