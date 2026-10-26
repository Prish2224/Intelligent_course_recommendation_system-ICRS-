import javax.swing.*;
import java.awt.*;

/** Flat rounded button with hover / pressed / keyboard-focus feedback. */
public class ModernButton extends JButton {
    public enum Kind { PRIMARY, SUCCESS, SECONDARY, DANGER }

    private final Kind kind;

    public ModernButton(String text, Kind kind) {
        super(text);
        this.kind = kind;
        setFont(Theme.bold(13f));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(9, 18, 9, 18));
        setForeground(kind == Kind.SECONDARY ? Theme.TEXT : Color.WHITE);
        getModel().addChangeListener(e -> repaint());
    }

    private Color base() {
        switch (kind) {
            case SUCCESS:   return Theme.TEAL;
            case DANGER:    return Theme.DANGER;
            case SECONDARY: return Color.WHITE;
            default:        return Theme.PRIMARY;
        }
    }

    private Color hover() {
        switch (kind) {
            case SUCCESS:   return Theme.TEAL_DARK;
            case DANGER:    return new Color(0xB91C1C);
            case SECONDARY: return new Color(0xEFF6FF);
            default:        return Theme.PRIMARY_DARK;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight();
        Color c = !isEnabled() ? new Color(0xCBD5E1)
                : getModel().isPressed() ? hover().darker()
                : getModel().isRollover() ? hover() : base();
        g2.setColor(c);
        g2.fillRoundRect(0, 0, w - 1, h - 1, 12, 12);
        if (kind == Kind.SECONDARY) {
            g2.setColor(getModel().isRollover() ? Theme.PRIMARY : new Color(0xCBD5E1));
            g2.drawRoundRect(0, 0, w - 1, h - 1, 12, 12);
        }
        if (hasFocus() && isEnabled()) {                       // keyboard focus ring
            g2.setColor(Theme.alpha(Theme.PRIMARY, 160));
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(2, 2, w - 5, h - 5, 10, 10);
        }
        g2.dispose();
        if (!isEnabled()) setForeground(Color.WHITE);
        super.paintComponent(g);
    }
}
