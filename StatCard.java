import javax.swing.*;
import java.awt.*;

/** Small dashboard card: coloured icon, big number, caption. */
public class StatCard extends RoundedPanel {
    private final JLabel value = new JLabel("0");

    public StatCard(String icon, String caption, Color accent) {
        super(18, Theme.CARD);
        setLayout(new BorderLayout(12, 0));
        setBorder(BorderFactory.createEmptyBorder(16, 14, 16, 8));

        JLabel ic = new JLabel(icon, SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.alpha(getForeground(), 28));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        ic.setFont(Theme.icon(22f));
        ic.setForeground(accent);
        ic.setPreferredSize(new Dimension(40, 40));
        add(ic, BorderLayout.WEST);

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        value.setFont(Theme.bold(26f));
        value.setForeground(Theme.TEXT);
        JLabel cap = new JLabel(caption);
        cap.setFont(Theme.regular(11.5f));
        cap.setForeground(Theme.MUTED);
        text.add(value);
        text.add(cap);
        add(text, BorderLayout.CENTER);
    }

    public void setValue(String v) { value.setText(v); }
}
