# Intelligent Course Recommendation System

Course: Object Oriented Techniques using Java (CCSE0355)
Group: 57 | SDG-4 (Quality Education)
Type: Standalone Java desktop application (Swing GUI + original console mode)

## How to run

**IntelliJ IDEA**
1. File > Open... and choose this folder (`Intelligent_Course_Recommendation_System`).
2. Right-click the `src` folder > Mark Directory as > Sources Root (if IntelliJ has not done it already). Use JDK 8 or newer.
3. Open `src/Main.java` and press the green Run arrow next to `main()`.
   The Swing dashboard opens. (Run with the program argument `--console` to get the original console menu.)

**Terminal / Command Prompt** (inside this folder)

    mkdir out
    javac -d out src/*.java
    java -cp out Main

## What it does
A student enters a profile (name, CGPA, career goal, interest areas and marks).
The system scores every course and shows the top 5 matches with the reason.
Score (out of 100) = interest match (40) + career match (30) + performance fit (20) + course-type bonus (10).
Courses already completed are not recommended again.

## Files (all inside `src/`)
| File | Purpose |
|---|---|
| Main.java | Entry point; creates the catalog and engine, then starts the GUI (or console with `--console`) |
| ConsoleUI.java | Menu, keyboard input and output |
| CourseCatalog.java | List of available courses |
| Course.java | Abstract parent class of all courses |
| CoreCourse.java | Core (foundation) course type |
| ElectiveCourse.java | Elective (optional) course type |
| Student.java | Stores student profile data |
| Recommendation.java | A recommended course with its score and reason |
| RecommendationEngine.java | Scoring and ranking logic |
| Displayable.java | Interface for classes that can display themselves |

## OOP concepts used
Classes and objects, encapsulation, abstraction (abstract class and interface),
inheritance, polymorphism (method overriding), constructors,
collections (ArrayList, HashSet, HashMap) and exception handling.

## GUI classes (new)
| File | Purpose |
|---|---|
| GUI.java | Main window: sidebar navigation + screen switching |
| AppContext.java | Shared state (catalog, engine, student) and change notifications |
| BasePage.java | Parent class of every screen |
| DashboardPanel / ProfilePanel / CourseCatalogPanel / RecommendationPanel / CompletedCoursesPanel / ProgressPanel / AboutPanel / CourseDetailsPanel | One class per screen |
| RecommendationCard, StatCard, RoundedPanel, ModernButton, ProgressBarX, BarChart, WrapText | Reusable visual components |
| Theme, Ui, CourseInfo | Colours/fonts, widget factories, course text helpers |

Small backend additions: `Student` got extra profile fields, setters and read-only getters;
`Recommendation` got `getReason()`. All scoring logic is unchanged.
