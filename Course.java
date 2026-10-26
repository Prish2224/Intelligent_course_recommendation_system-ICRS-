import java.util.List;

/**
 * Base class for all courses.
 * Concepts: ABSTRACT CLASS, ENCAPSULATION (private fields + getters), ABSTRACTION.
 */
public abstract class Course implements Displayable {
    private final String code;
    private final String title;
    private final String area;          // e.g. Programming, AI, Web
    private final int difficulty;       // 1 = easy, 2 = medium, 3 = hard
    private final int credits;
    private final List<String> careers; // careers this course helps

    public Course(String code, String title, String area, int difficulty, int credits, List<String> careers) {
        this.code = code;
        this.title = title;
        this.area = area;
        this.difficulty = difficulty;
        this.credits = credits;
        this.careers = careers;
    }

    public String getCode()       { return code; }
    public String getTitle()      { return title; }
    public String getArea()       { return area; }
    public int getDifficulty()    { return difficulty; }
    public int getCredits()       { return credits; }
    public List<String> getCareers() { return careers; }

    public boolean suitsCareer(String goal) {
        for (String c : careers) if (c.equalsIgnoreCase(goal)) return true;
        return false;
    }

    /** Each subclass says what type it is. */
    public abstract String getCourseType();

    /** Each subclass gives its own bonus points in the scoring (POLYMORPHISM). */
    public abstract int calculateBonus(Student student);

    @Override
    public void display() {
        System.out.printf("%-7s %-34s %-13s Level:%d  Credits:%d  [%s]%n",
                code, title, area, difficulty, credits, getCourseType());
    }
}
