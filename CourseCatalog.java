import java.util.*;

/** Holds the list of available courses (our "database"). */
public class CourseCatalog {
    private final List<Course> courses = new ArrayList<>();

    public CourseCatalog() {
        add(new CoreCourse("CS101", "Programming Fundamentals", "Programming", 1, 4, l("Software Developer", "Web Developer")));
        add(new CoreCourse("CS102", "Data Structures", "Programming", 2, 4, l("Software Developer", "AI Engineer")));
        add(new CoreCourse("CS103", "Database Management Systems", "Database", 2, 4, l("Software Developer", "Data Scientist")));
        add(new CoreCourse("CS104", "Computer Networks", "Networking", 2, 3, l("Cloud Engineer", "Security Analyst")));
        add(new CoreCourse("CS105", "Operating Systems", "Programming", 3, 4, l("Software Developer", "Cloud Engineer")));
        add(new ElectiveCourse("EL201", "Web Development with HTML/CSS/JS", "Web", 1, 3, l("Web Developer")));
        add(new ElectiveCourse("EL202", "Machine Learning Basics", "AI", 3, 4, l("AI Engineer", "Data Scientist")));
        add(new ElectiveCourse("EL203", "Data Analysis with Python", "Data Science", 2, 3, l("Data Scientist")));
        add(new ElectiveCourse("EL204", "Cyber Security Essentials", "Security", 2, 3, l("Security Analyst")));
        add(new ElectiveCourse("EL205", "Cloud Computing", "Cloud", 2, 3, l("Cloud Engineer", "Software Developer")));
        add(new ElectiveCourse("EL206", "Deep Learning", "AI", 3, 4, l("AI Engineer")));
        add(new ElectiveCourse("EL207", "Full Stack Web Frameworks", "Web", 3, 4, l("Web Developer", "Software Developer")));
        add(new ElectiveCourse("EL208", "Ethical Hacking", "Security", 3, 3, l("Security Analyst")));
        add(new ElectiveCourse("EL209", "Big Data Analytics", "Data Science", 3, 4, l("Data Scientist")));
    }

    private static List<String> l(String... items) { return Arrays.asList(items); }
    private void add(Course c) { courses.add(c); }

    public List<Course> getAll() { return courses; }

    public boolean exists(String code) {
        for (Course c : courses) if (c.getCode().equalsIgnoreCase(code)) return true;
        return false;
    }

    public static final String[] AREAS = {"Programming", "Database", "Networking", "Web", "AI", "Data Science", "Security", "Cloud"};
    public static final String[] CAREERS = {"Software Developer", "Web Developer", "Data Scientist", "AI Engineer", "Security Analyst", "Cloud Engineer"};
}
