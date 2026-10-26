import javax.swing.*;
import java.awt.*;

/** Static information screen. OOP concepts listed here are the ones present in the source code. */
public class AboutPanel extends BasePage {
    /** Add your team member names here to show them on the About screen. */
    private static final String[] TEAM_MEMBERS = {};

    public AboutPanel(AppContext ctx, GUI gui) {
        super(ctx, gui);
        add(header("About", "Intelligent Course Recommendation System"), BorderLayout.NORTH);

        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));

        col.add(block("Project purpose",
                "Helps a B.Tech student choose the right courses. The system scores every course using the student's "
                + "interests, career goal and past performance, then recommends the best matches with a clear reason. "
                + "It supports SDG-4 (Quality Education)."));
        col.add(Box.createVerticalStrut(14));
        col.add(bullets("Main features",
                "Student profile with interests, marks and completed courses",
                "Course catalog with search and filters",
                "Personalised recommendations with a match percentage and the reason",
                "Mark courses as completed - recommendations update automatically",
                "Progress dashboard with bars and a chart"));
        col.add(Box.createVerticalStrut(14));
        col.add(bullets("OOP concepts demonstrated",
                "Encapsulation \u2013 Student keeps its data private and exposes it through getters and setters",
                "Abstraction \u2013 Course is an abstract class with abstract getCourseType() and calculateBonus()",
                "Inheritance \u2013 CoreCourse and ElectiveCourse extend Course; every GUI screen extends BasePage",
                "Polymorphism \u2013 overridden calculateBonus() and display(); the engine calls them on Course references",
                "Interfaces \u2013 Displayable (Course, Recommendation) and Comparable (Recommendation)",
                "Collections \u2013 ArrayList, HashSet, HashMap used for courses, interests and marks",
                "Separation of concerns \u2013 GUI classes only call the existing backend classes"));
        col.add(Box.createVerticalStrut(14));
        col.add(bullets("Technologies used",
                "Java (JDK 8 or newer)", "Java Swing and Java 2D for the interface", "IntelliJ IDEA"));
        col.add(Box.createVerticalStrut(14));
        StringBuilder team = new StringBuilder("Course: Object Oriented Techniques using Java (CCSE0355)\nGroup 57");
        if (TEAM_MEMBERS.length > 0) team.append("\nTeam: ").append(String.join(", ", TEAM_MEMBERS));
        col.add(block("Project team", team.toString()));

        JPanel wrap = new Ui.ScrollablePanel(new BorderLayout());
        wrap.add(col, BorderLayout.NORTH);
        add(Ui.scroll(wrap), BorderLayout.CENTER);
    }

    @Override public void refresh() { /* static content */ }

    private RoundedPanel card(String title) {
        RoundedPanel p = new RoundedPanel();
        p.setLayout(new BorderLayout(0, 8));
        p.setBorder(BorderFactory.createEmptyBorder(16, 22, 16, 22));
        p.add(Ui.section(title), BorderLayout.NORTH);
        p.setAlignmentX(LEFT_ALIGNMENT);
        return p;
    }

    private RoundedPanel block(String title, String text) {
        RoundedPanel p = card(title);
        p.add(Ui.wrap(text.replace("\n", " | "), 720, Theme.regular(13.5f), new Color(0x334155)), BorderLayout.CENTER);
        return p;
    }

    private RoundedPanel bullets(String title, String... items) {
        RoundedPanel p = card(title);
        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        for (String s : items) {
            WrapText l = Ui.wrap("\u2022  " + s, 720, Theme.regular(13.5f), new Color(0x334155));
            l.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
            l.setAlignmentX(LEFT_ALIGNMENT);
            list.add(l);
        }
        p.add(list, BorderLayout.CENTER);
        return p;
    }
}
