import java.util.*;

/**
 * The recommendation logic. Score out of 100:
 *   interest match (40) + career match (30) + performance fit (20) + course-type bonus (10).
 */
public class RecommendationEngine {
    private static final int INTEREST_WEIGHT = 40;
    private static final int CAREER_WEIGHT   = 30;
    private static final int PERFORMANCE_WEIGHT = 20;

    public List<Recommendation> recommend(Student student, List<Course> catalog, int topN) {
        List<Recommendation> result = new ArrayList<>();

        for (Course c : catalog) {
            if (student.hasCompleted(c.getCode())) continue;   // skip finished courses

            double score = 0;
            StringBuilder why = new StringBuilder();

            if (student.hasInterest(c.getArea())) {
                score += INTEREST_WEIGHT;
                why.append("matches your interest in ").append(c.getArea()).append("; ");
            }
            if (c.suitsCareer(student.getCareerGoal())) {
                score += CAREER_WEIGHT;
                why.append("useful for ").append(student.getCareerGoal()).append("; ");
            }
            double fit = performanceFit(student, c);
            score += fit;
            if (fit >= 15) why.append("your past performance suits this level; ");

            score += c.calculateBonus(student);                // polymorphic call

            if (score > 20) {
                if (why.length() == 0) why.append("general suggestion for your profile; ");
                result.add(new Recommendation(c, Math.min(score, 100), why.toString()));
            }
        }
        Collections.sort(result);
        return result.subList(0, Math.min(topN, result.size()));
    }

    /** Compares student's marks in the area with the marks needed for the course level. */
    private double performanceFit(Student s, Course c) {
        int[] required = {0, 50, 65, 80};                      // by difficulty 1..3
        double gap = required[c.getDifficulty()] - s.getMark(c.getArea());
        if (gap <= 0) return PERFORMANCE_WEIGHT;
        return Math.max(0, PERFORMANCE_WEIGHT - gap * 0.5);
    }
}
