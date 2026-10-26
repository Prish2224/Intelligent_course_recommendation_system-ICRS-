import java.util.*;

/**
 * Shared application state: the existing backend objects (CourseCatalog, RecommendationEngine, Student)
 * plus a tiny listener list so every screen refreshes automatically when data changes (Observer idea).
 * No business logic lives here - it only delegates to the original classes.
 */
public class AppContext {
    private final CourseCatalog catalog;
    private final RecommendationEngine engine;
    private Student student;                       // null until a profile is saved
    private final List<Runnable> listeners = new ArrayList<>();

    public AppContext(CourseCatalog catalog, RecommendationEngine engine) {
        this.catalog = catalog;
        this.engine = engine;
    }

    // ---------- state ----------
    public CourseCatalog getCatalog() { return catalog; }
    public Student getStudent()       { return student; }
    public boolean hasStudent()       { return student != null; }
    public void setStudent(Student s) { this.student = s; fireChanged(); }

    public void addListener(Runnable r) { listeners.add(r); }
    public void fireChanged()           { for (Runnable r : listeners) r.run(); }

    /** Same sample student used in SAMPLE_TEST_RUN.txt - handy for demonstrations. */
    public void loadDemoStudent() {
        Student s = new Student("Priyanka", 8.2, "AI Engineer");
        s.setRollNo("21CSE057"); s.setBranch("B.Tech CSE"); s.setSemester(4);
        s.setSkills("Java, Python, SQL");
        s.addInterest("AI");           s.setMark("AI", 85);
        s.addInterest("Data Science"); s.setMark("Data Science", 80);
        setStudent(s);
    }

    // ---------- courses ----------
    public List<Course> allCourses() { return catalog.getAll(); }

    public Course find(String code) {
        for (Course c : catalog.getAll()) if (c.getCode().equalsIgnoreCase(code)) return c;
        return null;
    }

    public boolean isCompleted(Course c) { return student != null && student.hasCompleted(c.getCode()); }

    public void markCompleted(Course c) {
        if (student == null) return;
        student.completeCourse(c.getCode());
        fireChanged();
    }

    public void markNotCompleted(Course c) {
        if (student == null) return;
        student.uncompleteCourse(c.getCode());
        fireChanged();
    }

    public void addInterest(Course c) {
        if (student == null) return;
        student.addInterest(c.getArea());
        fireChanged();
    }

    public List<Course> completedCourses() {
        List<Course> out = new ArrayList<>();
        for (Course c : catalog.getAll()) if (isCompleted(c)) out.add(c);
        return out;
    }

    // ---------- recommendations (always from the existing RecommendationEngine) ----------
    public List<Recommendation> recommendations(int topN) {
        if (student == null) return new ArrayList<>();
        return engine.recommend(student, catalog.getAll(), topN);
    }

    public Recommendation recommendationFor(Course c) {
        for (Recommendation r : recommendations(catalog.getAll().size()))
            if (r.getCourse() == c) return r;
        return null;
    }

    // ---------- progress numbers ----------
    public int totalCourses()     { return catalog.getAll().size(); }
    public int completedCount()   { return completedCourses().size(); }
    public int remainingCount()   { return totalCourses() - completedCount(); }
    public double progress()      { return totalCourses() == 0 ? 0 : (double) completedCount() / totalCourses(); }

    public int totalCredits() { int t = 0; for (Course c : catalog.getAll()) t += c.getCredits(); return t; }
    public int earnedCredits() { int t = 0; for (Course c : completedCourses()) t += c.getCredits(); return t; }
}
