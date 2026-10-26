import java.util.*;

/** Menu and all keyboard input/output. */
public class ConsoleUI {
    private final Scanner sc = new Scanner(System.in);
    private final CourseCatalog catalog;
    private final RecommendationEngine engine;
    private Student student;

    public ConsoleUI(CourseCatalog catalog, RecommendationEngine engine) {
        this.catalog = catalog;
        this.engine = engine;
    }

    public void start() {
        System.out.println("=======================================");
        System.out.println("  INTELLIGENT COURSE RECOMMENDATION    ");
        System.out.println("=======================================");
        boolean running = true;
        while (running) {
            System.out.println("\n1. Register student profile");
            System.out.println("2. View all courses");
            System.out.println("3. Get course recommendations");
            System.out.println("4. Mark a course as completed");
            System.out.println("5. View my profile");
            System.out.println("6. Exit");
            int choice = readInt("Enter choice: ", 1, 6);
            switch (choice) {
                case 1: registerStudent(); break;
                case 2: showCourses(); break;
                case 3: showRecommendations(); break;
                case 4: markCompleted(); break;
                case 5: if (checkStudent()) student.printProfile(); break;
                case 6: running = false; System.out.println("Thank you! Goodbye."); break;
            }
        }
    }

    private void registerStudent() {
        System.out.print("Student name: ");
        String name = sc.nextLine().trim();
        double cgpa = readDouble("CGPA (0-10): ", 0, 10);

        System.out.println("\nCareer goals:");
        for (int i = 0; i < CourseCatalog.CAREERS.length; i++)
            System.out.println("  " + (i + 1) + ". " + CourseCatalog.CAREERS[i]);
        int g = readInt("Choose career goal: ", 1, CourseCatalog.CAREERS.length);
        student = new Student(name, cgpa, CourseCatalog.CAREERS[g - 1]);

        System.out.println("\nInterest areas:");
        for (int i = 0; i < CourseCatalog.AREAS.length; i++)
            System.out.println("  " + (i + 1) + ". " + CourseCatalog.AREAS[i]);
        System.out.print("Enter interest numbers separated by commas (e.g. 1,5): ");
        for (String part : sc.nextLine().split(",")) {
            try {
                int n = Integer.parseInt(part.trim());
                if (n >= 1 && n <= CourseCatalog.AREAS.length) {
                    String area = CourseCatalog.AREAS[n - 1];
                    student.addInterest(area);
                    double m = readDouble("  Your past marks in " + area + " (0-100, or 0 to skip): ", 0, 100);
                    if (m > 0) student.setMark(area, m);
                }
            } catch (NumberFormatException e) { /* ignore bad entry */ }
        }
        System.out.println("\nProfile saved!");
    }

    private void showCourses() {
        System.out.println("\n--- Available Courses ---");
        for (Course c : catalog.getAll()) c.display();   // polymorphism in action
    }

    private void showRecommendations() {
        if (!checkStudent()) return;
        List<Recommendation> recs = engine.recommend(student, catalog.getAll(), 5);
        System.out.println("\n--- Top recommendations for " + student.getName() + " ---");
        if (recs.isEmpty()) { System.out.println("No suitable courses found."); return; }
        int rank = 1;
        for (Recommendation r : recs) {
            System.out.print("#" + rank++ + " ");
            r.display();
        }
    }

    private void markCompleted() {
        if (!checkStudent()) return;
        System.out.print("Enter course code (e.g. CS101): ");
        String code = sc.nextLine().trim();
        if (catalog.exists(code)) {
            student.completeCourse(code);
            System.out.println(code.toUpperCase() + " marked as completed.");
        } else System.out.println("Invalid course code.");
    }

    private boolean checkStudent() {
        if (student == null) { System.out.println("Please register a student profile first (option 1)."); return false; }
        return true;
    }

    private int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            if (!sc.hasNextLine()) System.exit(0);
            try {
                int v = Integer.parseInt(sc.nextLine().trim());
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException e) { }
            System.out.println("Please enter a number between " + min + " and " + max + ".");
        }
    }

    private double readDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            if (!sc.hasNextLine()) System.exit(0);
            try {
                double v = Double.parseDouble(sc.nextLine().trim());
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException e) { }
            System.out.println("Please enter a value between " + min + " and " + max + ".");
        }
    }
}
