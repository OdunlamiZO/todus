package io.github.odunlamizo.todus;

import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.FieldInfo;
import io.github.classgraph.MethodInfo;
import io.github.classgraph.ScanResult;
import io.github.odunlamizo.todus.model.TodoItem;
import java.util.ArrayList;
import java.util.List;

/** Scans a given package for @Todo annotations on classes, methods, and fields. */
public class TodoScanner {

    /** Fully qualified name of the @Todo annotation */
    private static final String TODO_ANN_FQCN = "io.github.odunlamizo.todus.annotation.Todo";

    /** ClassLoader to use when scanning project classes */
    private final ClassLoader classLoader;

    public TodoScanner(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * Scan the provided base package for TODO annotations.
     *
     * @param basePackage The root package to scan
     * @return A list of TodoItem representing all found TODOs
     */
    public List<TodoItem> scan(String basePackage) {
        List<TodoItem> todos = new ArrayList<>();

        // 1. Initialize ClassGraph scanner with the proper configuration
        try (ScanResult scan =
                new ClassGraph()
                        .acceptPackages(basePackage) // Only scan classes in this package
                        .overrideClassLoaders(classLoader) // Use the Maven project classloader
                        .ignoreClassVisibility() // Include private classes/members
                        .enableClassInfo() // Enable scanning for classes
                        .enableAnnotationInfo() // Enable scanning for annotations
                        .enableMethodInfo() // Enable scanning for methods
                        .enableFieldInfo() // Enable scanning for fields
                        .scan()) {

            // 2. Retrieve all classes discovered in the package
            ClassInfoList allClasses = scan.getAllClasses();

            for (ClassInfo ci : allClasses) {
                // --- Class-level TODOs ---
                AnnotationInfo classAnnotation = ci.getAnnotationInfo(TODO_ANN_FQCN);
                if (classAnnotation != null) {
                    todos.add(TodoItem.fromAnnotationInfo(classAnnotation, ci.getName()));
                }

                // --- Method-level TODOs ---
                for (MethodInfo mi : ci.getDeclaredMethodInfo()) {
                    AnnotationInfo methodAnnotation = mi.getAnnotationInfo(TODO_ANN_FQCN);
                    if (methodAnnotation != null) {
                        todos.add(
                                TodoItem.fromAnnotationInfo(
                                        methodAnnotation, ci.getName() + "#" + mi.getName()));
                    }
                }

                // --- Field-level TODOs ---
                for (FieldInfo fi : ci.getDeclaredFieldInfo()) {
                    AnnotationInfo fieldAnnotation = fi.getAnnotationInfo(TODO_ANN_FQCN);
                    if (fieldAnnotation != null) {
                        todos.add(
                                TodoItem.fromAnnotationInfo(
                                        fieldAnnotation, ci.getName() + "#" + fi.getName()));
                    }
                }
            }
        }

        return todos;
    }
}
