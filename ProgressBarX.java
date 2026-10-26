import javax.swing.*;
import java.awt.*;

/** Rounded progress bar (value 0.0 - 1.0) drawn with plain Swing/Java2D. */
public class ProgressBarX extends JComponent {
    private double value;
    private Color color;
    private final int barHeight;

    public ProgressBarX(Color color, int barHeight) {
        this.color = color;
        this.barHeight = barHeight;
        setOpaque(false);
        setPreferredSize(new Dimension(100, barHeight));
        setMinimumSize(new Dimension(20, barHeight));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, barHeight));
    }

    public void setValue(double v) { this.value = Math.max(0, Math.min(1, v)); repaint(); }
    public double getValue()       { return value; }
    public void setColor(Color c)  { this.color = c; repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = barHeight, y = (getHeight() - h) / 2;
        g2.setColor(Theme.TRACK);
        g2.fillRoundRect(0, y, w, h, h, h);
        int fw = (int) Math.round(w * value);
        if (fw > 0) {
            g2.setPaint(new GradientPaint(0, 0, color, w, 0, color.brighter()));
            g2.fillRoundRect(0, y, Math.max(fw, h), h, h, h);
        }
        g2.dispose();
    }
}
