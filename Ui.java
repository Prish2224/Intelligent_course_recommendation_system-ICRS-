import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/** Small factory methods so every screen builds labels, chips and fields the same way. */
public final class Ui {
    private Ui() {}

    public static JLabel label(String text, Font font, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(font);
        l.setForeground(color);
        return l;
    }

    public static JLabel title(String text)    { return label(text, Theme.bold(24f), Theme.TEXT); }
    public static JLabel subtitle(String text) { return label(text, Theme.regular(13.5f), Theme.MUTED); }
    public static JLabel section(String text)  { return label(text, Theme.bold(16f), Theme.TEXT); }

    /** Label that wraps its text at the given pixel width (simple HTML wrapping). */
    public static WrapText wrap(String text, int ignoredWidth, Font font, Color color) {
        return new WrapText(text, font, color);
    }

    public static String escape(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    /** Small coloured pill, e.g. CORE / ELECTIVE / area. */
    public static JLabel chip(String text, Color color) {
        JLabel l = new JLabel(text, SwingConstants.CENTER) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.alpha(color, 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setFont(Theme.bold(11f));
        l.setForeground(color);
        l.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        return l;
    }

    public static Color typeColor(Course c) { return c.getCourseType().equals("CORE") ? Theme.PRIMARY : Theme.TEAL; }

    public static JLabel typeChip(Course c) { return chip(c.getCourseType(), typeColor(c)); }

    public static void styleField(JTextField f) {
        f.setFont(Theme.regular(13.5f));
        f.setForeground(Theme.TEXT);
        f.setBackground(Color.WHITE);
        f.setCaretColor(Theme.PRIMARY);
        f.setDisabledTextColor(Theme.TEXT);
        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(0xCBD5E1), 1, true),
                BorderFactory.createEmptyBorder(7, 10, 7, 10)));
    }

    public static void styleCombo(JComboBox<?> c) {
        c.setFont(Theme.regular(13.5f));
        c.setBackground(Color.WHITE);
        c.setForeground(Theme.TEXT);
        c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static JPanel transparent(LayoutManager lm) {
        JPanel p = new JPanel(lm);
        p.setOpaque(false);
        return p;
    }

    public static ModernButton button(String text, ModernButton.Kind kind, String tooltip) {
        ModernButton b = new ModernButton(text, kind);
        b.setToolTipText(tooltip);
        return b;
    }

    public static JScrollPane scroll(Component c) {
        JScrollPane sp = new JScrollPane(c, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.setViewportBorder(BorderFactory.createEmptyBorder());
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.getVerticalScrollBar().setUnitIncrement(18);
        return sp;
    }

    /** Centered "nothing here yet" message with an optional action button. */
    public static JPanel emptyState(String icon, String heading, String message, JButton action) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setOpaque(false);
        JPanel box = new JPanel();
        box.setOpaque(false);
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        JLabel i = label(icon, Theme.icon(44f), new Color(0x94A3B8));
        JLabel h = label(heading, Theme.bold(18f), Theme.TEXT);
        JLabel m = label(message, Theme.regular(13.5f), Theme.MUTED);
        for (JComponent c : new JComponent[]{i, h, m}) c.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(i); box.add(Box.createVerticalStrut(8));
        box.add(h); box.add(Box.createVerticalStrut(6));
        box.add(m);
        if (action != null) {
            action.setAlignmentX(Component.CENTER_ALIGNMENT);
            box.add(Box.createVerticalStrut(16));
            box.add(action);
        }
        p.add(box);
        return p;
    }

    /** Panel for scrolling pages: tracks viewport width so content never needs a horizontal scrollbar. */
    public static class ScrollablePanel extends JPanel implements Scrollable {
        public ScrollablePanel(LayoutManager lm) { super(lm); setOpaque(false); }
        public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        public int getScrollableUnitIncrement(Rectangle r, int o, int d) { return 18; }
        public int getScrollableBlockIncrement(Rectangle r, int o, int d) { return 120; }
        public boolean getScrollableTracksViewportWidth() { return true; }
        public boolean getScrollableTracksViewportHeight() { return false; }
    }

    public static void styleTable(JTable t) {
        t.setFont(Theme.regular(13.5f));
        t.setRowHeight(38);
        t.setShowVerticalLines(false);
        t.setShowHorizontalLines(true);
        t.setGridColor(new Color(0xEEF2F7));
        t.setIntercellSpacing(new Dimension(0, 1));
        t.setSelectionBackground(new Color(0xDBEAFE));
        t.setSelectionForeground(Theme.TEXT);
        t.setForeground(Theme.TEXT);
        t.setBackground(Color.WHITE);
        t.setFillsViewportHeight(true);
        t.setFocusable(true);
        javax.swing.table.JTableHeader h = t.getTableHeader();
        h.setReorderingAllowed(false);
        h.setPreferredSize(new Dimension(100, 40));
        h.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable tb, Object v, boolean s, boolean f, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(tb, v, false, false, r, c);
                l.setFont(Theme.bold(12f));
                l.setForeground(Theme.MUTED);
                l.setBackground(new Color(0xF8FAFC));
                l.setOpaque(true);
                l.setBorder(BorderFactory.createCompoundBorder(
                        new MatteBorder(0, 0, 1, 0, Theme.BORDER), BorderFactory.createEmptyBorder(0, 12, 0, 12)));
                return l;
            }
        });
        t.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable tb, Object v, boolean s, boolean f, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(tb, v, s, false, r, c);
                l.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                String text = String.valueOf(v);
                if (text.equals("CORE"))          { l.setForeground(Theme.PRIMARY); l.setFont(Theme.bold(12f)); }
                else if (text.equals("ELECTIVE")) { l.setForeground(Theme.TEAL);    l.setFont(Theme.bold(12f)); }
                else if (text.equals("Completed")) { l.setForeground(Theme.TEAL);  l.setFont(Theme.bold(12.5f)); }
                else { l.setForeground(Theme.TEXT); l.setFont(Theme.regular(13.5f)); }
                return l;
            }
        });
    }
}
