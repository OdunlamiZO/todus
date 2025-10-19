package io.github.odunlamizo.todus.model;

import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.AnnotationParameterValue;

/** Represents a TODO item discovered via @Todo annotations in code. */
public class TodoItem {

    private final String description;
    private final String assignee;
    private final String priority;
    private final String due;
    private final String location;

    public TodoItem(
            String description, String assignee, String priority, String due, String location) {
        this.description = description;
        this.assignee = assignee;
        this.priority = priority;
        this.due = due;
        this.location = location;
    }

    /**
     * Factory method to create a TodoItem from ClassGraph's AnnotationInfo
     *
     * @param annotationInfo ClassGraph annotation metadata
     * @param location Location in code (class#method or class#field)
     * @return A fully populated TodoItem
     */
    public static TodoItem fromAnnotationInfo(AnnotationInfo annotationInfo, String location) {
        String value = getStringParam(annotationInfo, "value"); // @Todo.value
        String assignee = getStringParam(annotationInfo, "assignee"); // @Todo.assignee
        String due = getStringParam(annotationInfo, "due"); // @Todo.due
        String priority = getEnumName(annotationInfo, "priority"); // @Todo.priority
        return new TodoItem(value, assignee, priority, due, location);
    }

    /** Extracts a String parameter from AnnotationInfo safely. */
    private static String getStringParam(AnnotationInfo ai, String param) {
        AnnotationParameterValue pv = ai.getParameterValues().get(param);
        return pv != null && pv.getValue() != null ? pv.getValue().toString() : "";
    }

    /** Extracts the enum name (e.g., LOW, MEDIUM, HIGH) from AnnotationInfo. */
    private static String getEnumName(AnnotationInfo ai, String param) {
        AnnotationParameterValue pv = ai.getParameterValues().get(param);
        if (pv == null || pv.getValue() == null) return "";
        String val = pv.getValue().toString();
        int idx = val.lastIndexOf('.');
        return idx >= 0 ? val.substring(idx + 1) : val;
    }

    public String getDescription() {
        return description;
    }

    public String getAssignee() {
        return assignee;
    }

    public String getPriority() {
        return priority;
    }

    public String getDue() {
        return due;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return String.format(
                "📝 @Todo(value='%s', assignee='%s', priority=%s, due='%s') found at %s",
                description, assignee, priority, due, location);
    }
}
