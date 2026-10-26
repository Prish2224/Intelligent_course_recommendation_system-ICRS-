# Intelligent Course Recommendation System

A Java desktop application that recommends courses to B.Tech students
based on their interests, career goal and past performance. It has a
modern Swing dashboard built on top of an object-oriented backend, and
it also keeps the original console mode.

-   **Subject:** Object Oriented Techniques using Java (CCSE0355)
-   **Group:** 57
-   **SDG:** 4 - Quality Education

## Team members

  Name              ERP ID / Roll No.
  ----------------- -------------------
  Priyanka Gupta    2501330100281
  Nandani Jaiswal   2501330100237
  Priti Singh       2501330100279
  Rishabh           2501330100303
  Rehan Khan        2501330100301

## Screenshot

### Dashboard

![Dashboard](README_screenshots/dashboard.jpg)

## Features

-   **Dashboard:** statistic cards, top recommended courses and overall
    progress
-   **Student Profile:** name, roll number, branch, semester, CGPA,
    career goal, interests, marks, skills and completed courses
-   **Course Catalog:** search box, filters by type and category, and a
    course details page
-   **Recommendations:** ranked courses with a match percentage and the
    reason for each
-   **Completed Courses:** list of finished courses with a progress
    summary
-   **Progress:** completed, remaining and percentage, with progress
    bars and a bar chart
-   **About:** project purpose and the OOP concepts used

Courses that are marked completed are not recommended again, and every
screen updates automatically when the data changes.

## How the recommendation works

Each course gets a score out of 100:

  Part                                         Points
  ------------------------------------------ --------
  Interest match                                   40
  Career goal match                                30
  Performance fit (marks vs. course level)         20
  Course-type bonus (Core / Elective)              10

The top courses are shown with the reason for each.

## OOP concepts used

  -----------------------------------------------------------------------
  Concept                             Where
  ----------------------------------- -----------------------------------
  Encapsulation                       `Student` keeps its data private
                                      and exposes getters and setters

  Abstraction                         `Course` is an abstract class with
                                      abstract `getCourseType()` and
                                      `calculateBonus()`

  Inheritance                         `CoreCourse` and `ElectiveCourse`
                                      extend `Course`; every GUI screen
                                      extends `BasePage`

  Polymorphism                        Overridden `calculateBonus()` and
                                      `display()`, called through
                                      `Course` references

  Interfaces                          `Displayable` (implemented by
                                      `Course` and `Recommendation`) and
                                      `Comparable` (`Recommendation`)

  Collections                         `ArrayList`, `HashSet` and
                                      `HashMap` for courses, interests
                                      and marks
  -----------------------------------------------------------------------

## Project structure

``` text
Intelligent-Course-Recommendation-System/
├── README.md
├── SAMPLE_TEST_RUN.txt
├── screenshots/
│   └── dashboard.jpg
└── src/
    ├── Main.java
    ├── Student.java, Course.java, CoreCourse.java, ElectiveCourse.java
    ├── CourseCatalog.java, Recommendation.java, RecommendationEngine.java
    ├── Displayable.java, ConsoleUI.java
    ├── GUI.java, AppContext.java, BasePage.java
    ├── DashboardPanel.java, ProfilePanel.java, CourseCatalogPanel.java
    ├── CourseDetailsPanel.java, RecommendationPanel.java
    ├── CompletedCoursesPanel.java, ProgressPanel.java, AboutPanel.java
    └── RecommendationCard, StatCard, RoundedPanel, ModernButton,
        ProgressBarX, BarChart, WrapText, Theme, Ui, CourseInfo
```

## How to run

**Requirements:** JDK 8 or newer (tested with JDK 21).

### IntelliJ IDEA

1.  Download or clone this repository and open the folder in IntelliJ
    IDEA.
2.  Go to **File \> Project Structure** and choose a JDK under **Project
    \> SDK**.
3.  Under **Modules \> Sources**, mark the `src` folder as **Sources**.
4.  Open `src/Main.java` and click the green run arrow next to `main()`.

### Command line

``` text
mkdir out
javac -d out src/*.java
java -cp out Main
```

To start the original console version instead:

``` text
java -cp out Main --console
```

## Quick test

1.  Run the program and open **Student Profile**.
2.  Click **Load demo student**, then **Save Profile**.
3.  Open **Recommendations** to see the ranked courses.
4.  Click **Mark Completed** on a course and watch the Dashboard,
    Progress and Recommendations update.
