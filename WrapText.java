import javax.swing.*;
import java.awt.*;

/** Read-only text that wraps to whatever width the layout gives it (reflows when the window is resized). */
public class WrapText extends JTextArea {
    public WrapText(String text, Font font, Color color) {
        super(text);
        setFont(font);
        setForeground(color);
        setEditable(false);
        setFocusable(false);
        setOpaque(false);
        setLineWrap(true);
        setWrapStyleWord(true);
        setBorder(BorderFactory.createEmptyBorder());
        setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void setBounds(int x, int y, int w, int h) {
        boolean widthChanged = w != getWidth();
        super.setBounds(x, y, w, h);
        if (widthChanged) SwingUtilities.invokeLater(this::revalidate);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        int w = getWidth();
        if (w > 0) {
            int oldH = getHeight();
            setSize(w, Short.MAX_VALUE);
            d = new Dimension(d.width, super.getPreferredSize().height);
            setSize(w, oldH);
        } else {
            d = new Dimension(10, d.height);
        }
        return d;
    }

    @Override
    public Dimension getMinimumSize() { return new Dimension(10, getPreferredSize().height); }
}
