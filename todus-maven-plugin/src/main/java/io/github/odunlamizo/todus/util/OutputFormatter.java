package io.github.odunlamizo.todus.util;

import io.github.odunlamizo.todus.model.TodoItem;
import java.util.List;

public class OutputFormatter {

    public static String toTable(List<TodoItem> todos) {
        // Column headers
        String[] headers = {"Description", "Assignee", "Priority", "Due", "Location"};

        // Compute the max width for each column
        int descWidth =
                Math.max(
                        headers[0].length(),
                        todos.stream().mapToInt(t -> t.getDescription().length()).max().orElse(0));
        int assigneeWidth =
                Math.max(
                        headers[1].length(),
                        todos.stream().mapToInt(t -> t.getAssignee().length()).max().orElse(0));
        int priorityWidth =
                Math.max(
                        headers[2].length(),
                        todos.stream().mapToInt(t -> t.getPriority().length()).max().orElse(0));
        int dueWidth =
                Math.max(
                        headers[3].length(),
                        todos.stream().mapToInt(t -> t.getDue().length()).max().orElse(0));
        int locationWidth =
                Math.max(
                        headers[4].length(),
                        todos.stream().mapToInt(t -> t.getLocation().length()).max().orElse(0));

        StringBuilder sb = new StringBuilder();

        // Header row
        sb.append(
                String.format(
                        "%-"
                                + descWidth
                                + "s  %-"
                                + assigneeWidth
                                + "s  %-"
                                + priorityWidth
                                + "s  %-"
                                + dueWidth
                                + "s  %s%n",
                        headers[0],
                        headers[1],
                        headers[2],
                        headers[3],
                        headers[4]));

        // Separator
        sb.append(
                        "-"
                                .repeat(
                                        descWidth
                                                + assigneeWidth
                                                + priorityWidth
                                                + dueWidth
                                                + locationWidth
                                                + 10))
                .append("\n");

        // Data rows
        for (TodoItem t : todos) {
            sb.append(
                    String.format(
                            "%-"
                                    + descWidth
                                    + "s  %-"
                                    + assigneeWidth
                                    + "s  %-"
                                    + priorityWidth
                                    + "s  %-"
                                    + dueWidth
                                    + "s  %s%n",
                            truncate(capitalize(t.getDescription()), descWidth),
                            truncate(capitalize(t.getAssignee()), assigneeWidth),
                            truncate(t.getPriority(), priorityWidth),
                            truncate(capitalize(t.getDue()), dueWidth),
                            truncate(t.getLocation(), locationWidth)));
        }

        return sb.toString();
    }

    // Truncate long text with ellipsis
    private static String truncate(String text, int maxWidth) {
        if (text.length() <= maxWidth) return text;
        return text.substring(0, maxWidth - 1) + "…";
    }

    // Helper to capitalize the first letter
    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
