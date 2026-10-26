import javax.swing.*;
import java.awt.*;

/** A panel with rounded corners and a soft border - used for all "cards". */
public class RoundedPanel extends JPanel {
    private final int arc;
    private Color fill;
    private Color border = Theme.BORDER;

    public RoundedPanel(int arc, Color fill) {
        this.arc = arc;
        this.fill = fill;
        setOpaque(false);
    }

    public RoundedPanel() { this(18, Theme.CARD); }

    public void setFill(Color c)   { this.fill = c; repaint(); }
    public void setBorderColor(Color c) { this.border = c; repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(fill);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
        if (border != null) {
            g2.setColor(border);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
        }
        g2.dispose();
        super.paintComponent(g);
    }
}
