# Todus

A lightweight Maven plugin that scans your Java project for `@Todo` annotations and outputs a neat, organized report. Perfect for keeping track of pending tasks in your codebase without leaving your IDE.

---

## Features

* ✅ Scans **classes, methods, and fields** for `@Todo` annotations.
* ✅ Generates a **readable table** with description, assignee, priority, due date, and location.
* ✅ Supports **dynamic base package scanning**.
* ✅ Thread-safe and compatible with **compile + runtime dependencies**.
* ✅ Extensible and easy to integrate into Maven projects.

---

## Installation

Add the plugin to your Maven project’s `pom.xml`:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>io.github.odunlamizo</groupId>
            <artifactId>todus-maven-plugin</artifactId>
            <version>1.0.0</version>
            <executions>
                <execution>
                    <goals>
                        <goal>list</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

> **Note:** To use `@Todo` annotations in your project, you also need to include the core dependency:

```xml
<dependency>
    <groupId>io.github.odunlamizo</groupId>
    <artifactId>todus-core</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

---

## Usage

Run the plugin from the command line:

```bash
mvn todus:list
```

Optional: Specify a base package to scan:

```bash
mvn todus:list -Dtodus.basePackage=com.example
```

The plugin will scan your project and print a table like:

```
Description                           Assignee   Priority  Due         Location
----------------------------------------------------------------------------------------------------------------------------------
Check if kyc already exists           Zacchaeus  HIGH      2025-12-31  com.schoolsort.account.service.KycServiceImpl#submitKyc
Send email to user to reset password  Zacchaeus  HIGH      2025-12-31  com.schoolsort.account.service.UserServiceImpl#createUser
```

---

## @Todo Annotation

You can annotate classes, methods, or fields with `@Todo`:

```java
@Todo(
    value = "Implement validation for input",
    assignee = "Zacchaeus",
    priority = Priority.HIGH,
    due = "2025-12-31"
)
public class MyService {
    //...
}
```

---

## How it Works

1. **Scanning** – The plugin uses your project’s classpath and optional base package to locate all classes.
2. **Annotation Extraction** – It reads `@Todo` annotations on classes, methods, and fields.
3. **Table Output** – Generates a clean table with description, assignee, priority, due date, and location.

---

## Contribution

Contributions are welcome!

1. Fork the repository
2. Create a branch: `git checkout -b feature/my-feature`
3. Make your changes and commit: `git commit -am 'Add new feature'`
4. Push to the branch: `git push origin feature/my-feature`
5. Open a Pull Request

---

## License

MIT License © 2025 Zacchaeus Odunlami
