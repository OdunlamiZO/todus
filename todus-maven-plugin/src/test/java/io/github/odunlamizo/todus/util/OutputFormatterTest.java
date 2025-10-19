package io.github.odunlamizo.todus.util;

import static org.junit.jupiter.api.Assertions.*;

import io.github.odunlamizo.todus.model.TodoItem;
import java.util.List;
import org.junit.jupiter.api.Test;

class OutputFormatterTest {

    @Test
    void testToTableFormatsCorrectly() {
        List<TodoItem> todos =
                List.of(
                        new TodoItem(
                                "check tenant", "Zacchaeus", "HIGH", "today", "MyClass#method"),
                        new TodoItem(
                                "reset password",
                                "Alice",
                                "MEDIUM",
                                "tomorrow",
                                "UserService#createUser"));

        String table = OutputFormatter.toTable(todos);

        // Check headers
        assertTrue(table.contains("Description"));
        assertTrue(table.contains("Assignee"));
        assertTrue(table.contains("Priority"));
        assertTrue(table.contains("Due"));
        assertTrue(table.contains("Location"));

        // Check content
        assertTrue(table.contains("Check tenant"));
        assertTrue(table.contains("Reset password"));
        assertTrue(table.contains("Zacchaeus"));
        assertTrue(table.contains("Alice"));
        assertTrue(table.contains("HIGH"));
        assertTrue(table.contains("MEDIUM"));
        assertTrue(table.contains("MyClass#method"));
        assertTrue(table.contains("UserService#createUser"));
    }
}
