import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * Main window: dark sidebar + a CardLayout content area.
 * Every screen is its own class (extends BasePage); this class only wires navigation together.
 */
public class GUI extends JFrame {
    public static final String DASHBOARD = "Dashboard", PROFILE = "Student Profile", CATALOG = "Course Catalog",
            RECOMMEND = "Recommendations", COMPLETED = "Completed Courses", PROGRESS = "Progress", ABOUT = "About",
            DETAILS = "Course Details";

    private static final String[][] NAV = {
            {"\u2302", DASHBOARD}, {"\u263A", PROFILE}, {"\u25A4", CATALOG}, {"\u2605", RECOMMEND},
            {"\u2714", COMPLETED}, {"\u25A6", PROGRESS}, {"\u2139", ABOUT}};

    private final AppContext ctx;
    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);
    private final Map<String, BasePage> pages = new LinkedHashMap<>();
    private final Map<String, NavButton> navButtons = new LinkedHashMap<>();
    private CourseDetailsPanel detailsPanel;
    private String current = DASHBOARD;
    private String cameFrom = DASHBOARD;

    public GUI(AppContext ctx) {
        super("Intelligent Course Recommendation System");
        this.ctx = ctx;
        applyLookAndFeel();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 640));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.add(buildSidebar(), BorderLayout.WEST);

        content.setBackground(Theme.BG);
        addPage(DASHBOARD, new DashboardPanel(ctx, this));
        addPage(PROFILE,   new ProfilePanel(ctx, this));
        addPage(CATALOG,   new CourseCatalogPanel(ctx, this));
        addPage(RECOMMEND, new RecommendationPanel(ctx, this));
        addPage(COMPLETED, new CompletedCoursesPanel(ctx, this));
        addPage(PROGRESS,  new ProgressPanel(ctx, this));
        addPage(ABOUT,     new AboutPanel(ctx, this));
        detailsPanel = new CourseDetailsPanel(ctx, this);
        addPage(DETAILS,   detailsPanel);
        root.add(content, BorderLayout.CENTER);
        setContentPane(root);

        // any data change -> every page refreshes automatically
        ctx.addListener(() -> { for (BasePage p : pages.values()) p.refresh(); });
        ctx.fireChanged();
        installShortcuts();
        navigate(DASHBOARD);
    }

    private void applyLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
                if ("Nimbus".equals(info.getName())) { UIManager.setLookAndFeel(info.getClassName()); break; }
        } catch (Exception ignored) { }
        UIManager.put("OptionPane.messageFont", Theme.regular(13.5f));
        UIManager.put("OptionPane.buttonFont", Theme.bold(12.5f));
        UIManager.put("ToolTip.font", Theme.regular(12f));
    }

    private void addPage(String name, BasePage page) {
        pages.put(name, page);
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(Theme.BG);
        wrap.add(page, BorderLayout.CENTER);
        content.add(wrap, name);
    }

    // ---------------- sidebar ----------------
    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setBackground(Theme.SIDEBAR);
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBorder(BorderFactory.createEmptyBorder(24, 14, 18, 14));
        side.setPreferredSize(new Dimension(236, 100));

        JLabel brand = Ui.label("\u25C6  Course Advisor", Theme.bold(19f), Color.WHITE);
        brand.setAlignmentX(LEFT_ALIGNMENT);
        brand.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        JLabel tag = Ui.label("Course Recommendation System", Theme.regular(11.5f), Theme.SIDEBAR_TEXT);
        tag.setAlignmentX(LEFT_ALIGNMENT);
        tag.setBorder(BorderFactory.createEmptyBorder(2, 10, 0, 0));
        side.add(brand); side.add(tag);
        side.add(Box.createVerticalStrut(28));

        ButtonGroup g = new ButtonGroup();
        int n = 1;
        for (String[] item : NAV) {
            NavButton b = new NavButton(item[0], item[1], n++);
            b.addActionListener(e -> navigate(item[1]));
            g.add(b);
            navButtons.put(item[1], b);
            b.setAlignmentX(LEFT_ALIGNMENT);
            side.add(b);
            side.add(Box.createVerticalStrut(4));
        }
        side.add(Box.createVerticalGlue());
        JLabel foot = Ui.label("OOTS Project \u2022 Group 57", Theme.regular(11f), Theme.SIDEBAR_TEXT);
        foot.setAlignmentX(LEFT_ALIGNMENT);
        foot.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        side.add(foot);
        return side;
    }

    private void installShortcuts() {
        JRootPane rp = getRootPane();
        for (int i = 0; i < NAV.length; i++) {
            final String page = NAV[i][1];
            KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_1 + i, InputEvent.CTRL_DOWN_MASK);
            rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, "nav" + i);
            rp.getActionMap().put("nav" + i, new AbstractAction() {
                public void actionPerformed(ActionEvent e) { navigate(page); }
            });
        }
    }

    // ---------------- navigation ----------------
    public void navigate(String page) {
        current = page;
        cards.show(content, page);
        NavButton b = navButtons.get(page);
        if (b != null) b.setSelected(true);
        BasePage p = pages.get(page);
        if (p != null) { p.refresh(); scrollToTop(p); }
    }

    /** Every time a screen is opened it starts at the top. */
    private void scrollToTop(Container c) {
        for (Component child : c.getComponents()) {
            if (child instanceof JScrollPane) {
                final JScrollPane sp = (JScrollPane) child;
                SwingUtilities.invokeLater(() -> sp.getVerticalScrollBar().setValue(0));
            } else if (child instanceof Container) scrollToTop((Container) child);
        }
    }

    /** Opens the details screen inside the main area (no pop-up) and remembers where to go back to. */
    public void showDetails(Course c) {
        if (!current.equals(DETAILS)) cameFrom = current;
        detailsPanel.setCourse(c);
        navigate(DETAILS);
        // keep the section the user came from highlighted in the sidebar
        NavButton b = navButtons.get(cameFrom);
        if (b != null) b.setSelected(true);
    }

    public void goBack() { navigate(cameFrom); }

    // ---------------- shared actions with confirmation dialogs ----------------
    public boolean requireProfile() {
        if (ctx.hasStudent()) return true;
        JOptionPane.showMessageDialog(this, "Please create your student profile first.", "Profile required",
                JOptionPane.INFORMATION_MESSAGE);
        navigate(PROFILE);
        return false;
    }

    public void confirmAndComplete(Course c) {
        if (!requireProfile()) return;
        if (ctx.isCompleted(c)) {
            JOptionPane.showMessageDialog(this, c.getTitle() + " is already marked as completed.", "Already completed",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int r = JOptionPane.showConfirmDialog(this, "Mark \"" + c.getTitle() + "\" (" + c.getCode() + ") as completed?",
                "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (r == JOptionPane.YES_OPTION) ctx.markCompleted(c);
    }

    // ---------------- sidebar button ----------------
    private static class NavButton extends JToggleButton {
        private final String icon;
        private final String text;
        private boolean hover;

        NavButton(String icon, String text, int number) {
            this.icon = icon; this.text = text;
            setFocusPainted(false); setBorderPainted(false); setContentAreaFilled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(208, 44));
            setMaximumSize(new Dimension(Short.MAX_VALUE, 44));
            setToolTipText(text + "  (Ctrl+" + number + ")");
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
            });
            addItemListener(e -> repaint());
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            if (isSelected()) {
                g2.setPaint(new GradientPaint(0, 0, Theme.PRIMARY, w, 0, Theme.TEAL));
                g2.fillRoundRect(0, 0, w, h, 12, 12);
            } else if (hover || hasFocus()) {
                g2.setColor(Theme.SIDEBAR_HOVER);
                g2.fillRoundRect(0, 0, w, h, 12, 12);
            }
            Color fg = isSelected() ? Color.WHITE : hover ? Color.WHITE : Theme.SIDEBAR_TEXT;
            g2.setColor(fg);
            g2.setFont(Theme.icon(17f));
            g2.drawString(icon, 16, h / 2 + 6);
            g2.setFont(isSelected() ? Theme.bold(13.5f) : Theme.regular(13.5f));
            g2.drawString(text, 46, h / 2 + 5);
            g2.dispose();
        }
    }
}
