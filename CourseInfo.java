import java.util.*;

/**
 * Presentation helpers for a Course. The original Course class has no description or prerequisite
 * field, so text is built only from data the course really has (area, level, credits, careers).
 */
public final class CourseInfo {
    private CourseInfo() {}

    public static String level(Course c) {
        switch (c.getDifficulty()) {
            case 1:  return "Beginner";
            case 2:  return "Intermediate";
            default: return "Advanced";
        }
    }

    public static String shortDescription(Course c) {
        return level(c) + " " + c.getCourseType().toLowerCase() + " course in " + c.getArea()
                + " (" + c.getCredits() + " credits).";
    }

    public static String description(Course c) {
        String kind = c.getCourseType().equals("CORE")
                ? "a foundation (compulsory) subject of the degree"
                : "an optional, specialised subject";
        return c.getTitle() + " is " + kind + " in the " + c.getArea() + " area. It is rated "
                + level(c).toLowerCase() + " (level " + c.getDifficulty() + " of 3) and carries "
                + c.getCredits() + " credits. It supports these career goals: "
                + String.join(", ", c.getCareers()) + ".";
    }

    /** Turns "matches your interest in AI; useful for AI Engineer; " into a readable sentence. */
    public static String prettyReason(String raw) {
        String[] parts = raw.trim().split(";");
        List<String> items = new ArrayList<>();
        for (String p : parts) { p = p.trim(); if (!p.isEmpty()) items.add(p); }
        if (items.isEmpty()) return "";
        String s = String.join(" \u2022 ", items);
        return Character.toUpperCase(s.charAt(0)) + s.substring(1) + ".";
    }
}
