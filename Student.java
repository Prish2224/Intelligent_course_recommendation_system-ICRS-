import java.util.*;

/** Stores student data safely. Concept: ENCAPSULATION (private fields, controlled access). */
public class Student {
    private String name;
    private double cgpa;                               // out of 10
    private String careerGoal;
    // Extra profile details shown in the GUI (optional)
    private String rollNo = "";
    private String branch = "";
    private int semester = 1;
    private String skills = "";
    private final Set<String> interests = new HashSet<>();
    private final Map<String, Double> areaMarks = new HashMap<>(); // marks (0-100) per area
    private final Set<String> completedCourses = new HashSet<>();

    public Student(String name, double cgpa, String careerGoal) {
        this.name = name;
        this.cgpa = cgpa;
        this.careerGoal = careerGoal;
    }

    public String getName()       { return name; }
    public double getCgpa()       { return cgpa; }
    public String getCareerGoal() { return careerGoal; }

    // Controlled setters (used when the profile is edited in the GUI)
    public void setName(String name)              { this.name = name; }
    public void setCgpa(double cgpa)              { this.cgpa = cgpa; }
    public void setCareerGoal(String careerGoal)  { this.careerGoal = careerGoal; }

    public String getRollNo()   { return rollNo; }
    public String getBranch()   { return branch; }
    public int getSemester()    { return semester; }
    public String getSkills()   { return skills; }
    public void setRollNo(String rollNo)     { this.rollNo = rollNo; }
    public void setBranch(String branch)     { this.branch = branch; }
    public void setSemester(int semester)    { this.semester = semester; }
    public void setSkills(String skills)     { this.skills = skills; }

    // Read-only views so the GUI can display data without breaking encapsulation
    public Set<String> getInterests()          { return Collections.unmodifiableSet(interests); }
    public Set<String> getCompletedCourses()   { return Collections.unmodifiableSet(completedCourses); }
    public boolean hasMark(String area)        { return areaMarks.containsKey(area.toLowerCase()); }
    public void clearInterestsAndMarks()       { interests.clear(); areaMarks.clear(); }
    public void clearCompleted()               { completedCourses.clear(); }
    public void uncompleteCourse(String code)  { completedCourses.remove(code.toUpperCase()); }

    public void addInterest(String area)            { interests.add(area.toLowerCase()); }
    public boolean hasInterest(String area)         { return interests.contains(area.toLowerCase()); }
    public void setMark(String area, double marks)  { areaMarks.put(area.toLowerCase(), marks); }

    /** If no marks were given for an area, estimate from CGPA (CGPA x 10). */
    public double getMark(String area) {
        return areaMarks.getOrDefault(area.toLowerCase(), cgpa * 10);
    }

    public void completeCourse(String code)     { completedCourses.add(code.toUpperCase()); }
    public boolean hasCompleted(String code)    { return completedCourses.contains(code.toUpperCase()); }

    public void printProfile() {
        System.out.println("Name: " + name + " | CGPA: " + cgpa + " | Career goal: " + careerGoal);
        System.out.println("Interests: " + interests + " | Completed: " + completedCourses);
    }
}
