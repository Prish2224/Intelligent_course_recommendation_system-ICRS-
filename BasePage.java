import javax.swing.*;
import java.awt.*;

/** Parent class of every screen (inheritance). Each page rebuilds its data in refresh(). */
public abstract class BasePage extends JPanel {
    protected final AppContext ctx;
    protected final GUI gui;

    protected BasePage(AppContext ctx, GUI gui) {
        this.ctx = ctx;
        this.gui = gui;
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(24, 30, 24, 30));
    }

    /** Re-read the data from the backend and update the widgets. */
    public abstract void refresh();

    /** Standard page header: big title + muted subtitle. */
    protected JPanel header(String title, String subtitle) {
        JPanel h = new JPanel();
        h.setOpaque(false);
        h.setLayout(new BoxLayout(h, BoxLayout.Y_AXIS));
        h.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));
        JLabel t = Ui.title(title), s = Ui.subtitle(subtitle);
        t.setAlignmentX(LEFT_ALIGNMENT); s.setAlignmentX(LEFT_ALIGNMENT);
        h.add(t); h.add(Box.createVerticalStrut(4)); h.add(s);
        return h;
    }

    /** Message + button shown when no student profile exists yet. */
    protected JPanel noProfileState() {
        ModernButton b = Ui.button("Create profile", ModernButton.Kind.PRIMARY, "Go to the Student Profile screen");
        b.addActionListener(e -> gui.navigate(GUI.PROFILE));
        return Ui.emptyState("\u263A", "No student profile yet",
                "Create your profile first so the system can personalise this screen.", b);
    }
}
