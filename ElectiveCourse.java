import java.util.List;

/** INHERITANCE: ElectiveCourse IS-A Course (optional / specialised subjects). */
public class ElectiveCourse extends Course {
    public ElectiveCourse(String code, String title, String area, int difficulty, int credits, List<String> careers) {
        super(code, title, area, difficulty, credits, careers);
    }

    @Override
    public String getCourseType() { return "ELECTIVE"; }

    // Strong students (CGPA >= 7.5) get a bonus for hard electives; others get a small bonus.
    @Override
    public int calculateBonus(Student student) {
        if (student.getCgpa() >= 7.5 && getDifficulty() == 3) return 10;
        return 4;
    }

    @Override
    public void display() {
        super.display();
        System.out.println("        -> Helps careers: " + String.join(", ", getCareers()));
    }
}
