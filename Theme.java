import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;

/** Central place for colours and fonts so the whole GUI looks consistent. */
public final class Theme {
    private Theme() {}

    public static final Color SIDEBAR      = new Color(0x0F172A);
    public static final Color SIDEBAR_HOVER= new Color(0x1E293B);
    public static final Color SIDEBAR_TEXT = new Color(0x94A3B8);
    public static final Color BG           = new Color(0xF1F5F9);
    public static final Color CARD         = Color.WHITE;
    public static final Color BORDER       = new Color(0xE2E8F0);
    public static final Color TEXT         = new Color(0x0F172A);
    public static final Color MUTED        = new Color(0x64748B);
    public static final Color PRIMARY      = new Color(0x2563EB);
    public static final Color PRIMARY_DARK = new Color(0x1D4ED8);
    public static final Color TEAL         = new Color(0x0D9488);
    public static final Color TEAL_DARK    = new Color(0x0F766E);
    public static final Color AMBER        = new Color(0xD97706);
    public static final Color DANGER       = new Color(0xDC2626);
    public static final Color TRACK        = new Color(0xE2E8F0);

    private static final String FAMILY = pickFamily();

    private static String pickFamily() {
        String[] wanted = {"Segoe UI", "Inter", "Helvetica Neue", "SF Pro Text", "Ubuntu", "Roboto", "Noto Sans", "DejaVu Sans"};
        java.util.List<String> have = Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        for (String f : wanted) if (have.contains(f)) return f;
        return Font.SANS_SERIF;
    }

    public static Font font(int style, float size) { return new Font(FAMILY, style, 1).deriveFont(style, size); }
    public static Font regular(float size) { return font(Font.PLAIN, size); }
    public static Font bold(float size)    { return font(Font.BOLD, size); }

    /** Font used for the simple Unicode icons. */
    public static Font icon(float size) {
        // Prefer a symbol font that really has the glyphs; otherwise the logical "Dialog" font, which falls back automatically.
        String[] wanted = {"Segoe UI Symbol", "Apple Symbols", "DejaVu Sans"};
        java.util.List<String> have = Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        for (String f : wanted) {
            Font ft = new Font(f, Font.PLAIN, 1).deriveFont(Font.PLAIN, size);
            if (have.contains(f) && ft.canDisplayUpTo("\u2302\u263A\u25A4\u2605\u2714\u25A6\u2139\u25D4") == -1) return ft;
        }
        return new Font(Font.DIALOG, Font.PLAIN, 1).deriveFont(Font.PLAIN, size);
    }

    public static Color alpha(Color c, int a) { return new Color(c.getRed(), c.getGreen(), c.getBlue(), a); }
}
