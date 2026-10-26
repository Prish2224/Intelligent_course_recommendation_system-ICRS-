import javax.swing.SwingUtilities;

/**
 * Entry point: creates the catalog and the recommendation engine, then starts the program.
 * Default = modern Swing GUI.   Run with the argument "--console" for the original console menu.
 */
public class Main {
    public static void main(String[] args) {
        CourseCatalog catalog = new CourseCatalog();
        RecommendationEngine engine = new RecommendationEngine();
        if (args.length > 0 && args[0].equalsIgnoreCase("--console")) {
            new ConsoleUI(catalog, engine).start();
        } else {
            SwingUtilities.invokeLater(() -> new GUI(new AppContext(catalog, engine)).setVisible(true));
        }
    }
}
