package io.github.odunlamizo.todus;

import io.github.odunlamizo.todus.model.TodoItem;
import io.github.odunlamizo.todus.util.OutputFormatter;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

/**
 * Mojo to list all @Todo annotations in the project. Scans the compiled classes in the project
 * using a URLClassLoader.
 */
@Execute(phase = LifecyclePhase.PROCESS_CLASSES)
@Mojo(
        name = "list",
        requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME,
        threadSafe = true)
public class TodusListMojo extends AbstractMojo {

    /** The Maven project instance, automatically injected by Maven */
    @Parameter(defaultValue = "${project}", readonly = true)
    MavenProject project;

    /** Optional base package to scan; defaults to the project's groupId */
    @Parameter(property = "todus.basePackage")
    String basePackage;

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("🚀 Starting TODO scan for project: " + project.getArtifactId());

        try {
            // 1. Get the classpath elements for compilation and runtime
            List<String> compileCp = project.getCompileClasspathElements();
            List<String> runtimeCp = project.getRuntimeClasspathElements();

            // 2. Determine the output directory where compiled classes are placed
            String outputDir =
                    project.getBuild() != null ? project.getBuild().getOutputDirectory() : null;

            // 3. Prepare a list of URLs to load in the custom classloader
            List<URL> urls = new java.util.ArrayList<>();
            if (outputDir != null) {
                File out = new File(outputDir);
                if (out.exists()) {
                    urls.add(out.toURI().toURL());
                    getLog().info("📦 Added compiled classes from: " + outputDir);
                } else {
                    getLog().warn("⚠️ Output directory does not exist: " + outputDir);
                }
            }

            // 4. Helper to safely convert classpath elements to URLs and add to the list
            java.util.function.Consumer<String> addUrl =
                    (path) -> {
                        try {
                            URL url = new File(path).toURI().toURL();
                            if (!urls.contains(url)) {
                                urls.add(url);
                                getLog().debug("🔗 Added classpath URL: " + url);
                            }
                        } catch (Exception exception) {
                            throw new RuntimeException(
                                    "Failed to add classpath URL: " + path, exception);
                        }
                    };
            compileCp.forEach(addUrl);
            runtimeCp.forEach(addUrl);

            // 5. Create a URLClassLoader for the project classes
            try (URLClassLoader projectClassLoader =
                    new URLClassLoader(
                            urls.toArray(new URL[0]),
                            Thread.currentThread().getContextClassLoader())) {

                Thread.currentThread().setContextClassLoader(projectClassLoader);
                getLog().info("🎯 Custom classloader initialized with " + urls.size() + " URLs");

                // 6. Determine the base package to scan
                String basePkg =
                        (basePackage == null || basePackage.isBlank())
                                ? project.getGroupId()
                                : basePackage;
                getLog().info("🔍 Scanning base package: " + basePkg);

                // 7. Perform the scan for @Todo annotations
                TodoScanner scanner = new TodoScanner(projectClassLoader);
                List<TodoItem> todos = scanner.scan(basePkg);

                // 8. Display results
                if (todos.isEmpty()) {
                    getLog().info("✅ No TODOs found! Everything is clean!");
                } else {
                    getLog().info(
                                    "📝 Found "
                                            + todos.size()
                                            + " TODO(s):\n"
                                            + OutputFormatter.toTable(todos));
                }
            }

        } catch (Exception exception) {
            getLog().error("💥 Something went wrong while scanning for TODOs", exception);
            throw new MojoExecutionException("Failed to scan for @Todo annotations", exception);
        }

        getLog().info("🏁 TODO scan completed for project: " + project.getArtifactId());
    }
}
