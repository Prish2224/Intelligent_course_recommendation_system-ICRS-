/** Links a Course to a score and a reason. Implements Comparable so lists can be sorted. */
public class Recommendation implements Comparable<Recommendation>, Displayable {
    private final Course course;
    private final double score;
    private final String reason;

    public Recommendation(Course course, double score, String reason) {
        this.course = course;
        this.score = score;
        this.reason = reason;
    }

    public Course getCourse() { return course; }
    public double getScore()  { return score; }
    public String getReason() { return reason; }

    @Override
    public int compareTo(Recommendation other) {   // highest score first
        return Double.compare(other.score, this.score);
    }

    @Override
    public void display() {
        System.out.printf("Match: %5.1f%%  ", score);
        course.display();                          // POLYMORPHISM: Core/Elective show differently
        System.out.println("        Why: " + reason);
    }
}
