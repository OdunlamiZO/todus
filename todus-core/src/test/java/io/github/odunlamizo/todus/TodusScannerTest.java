package io.github.odunlamizo.todus;

import static org.junit.jupiter.api.Assertions.*;

import io.github.odunlamizo.todus.model.TodoItem;
import java.util.List;
import org.junit.jupiter.api.Test;

class TodusScannerTest {

    @Test
    void testScanFindsTodo() {
        TodoScanner scanner = new TodoScanner(Thread.currentThread().getContextClassLoader());
        List<TodoItem> todos = scanner.scan("io.github.odunlamizo.todus.model");

        assertFalse(todos.isEmpty());
        TodoItem item = todos.get(0);
        assertEquals("fill properties", item.getDescription());
        assertEquals("Zacchaeus", item.getAssignee());
        assertEquals("HIGH", item.getPriority());
        assertEquals("future", item.getDue());
    }
}
