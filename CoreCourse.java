import java.util.List;

/** INHERITANCE: CoreCourse IS-A Course (foundation / compulsory subjects). */
public class CoreCourse extends Course {
    public CoreCourse(String code, String title, String area, int difficulty, int credits, List<String> careers) {
        super(code, title, area, difficulty, credits, careers);
    }

    @Override
    public String getCourseType() { return "CORE"; }

    // Core courses are foundations, so easier ones get a higher bonus.
    @Override
    public int calculateBonus(Student student) {
        return getDifficulty() <= 2 ? 10 : 5;
    }

    // METHOD OVERRIDING: extends the parent's display
    @Override
    public void display() {
        super.display();
        System.out.println("        -> Foundation course for your degree");
    }
}
