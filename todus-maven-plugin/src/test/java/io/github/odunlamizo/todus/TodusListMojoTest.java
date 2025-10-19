package io.github.odunlamizo.todus;

import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;

class TodusListMojoPlainTest {

    @Test
    void testMojoExecuteWithoutMavenHarness() throws Exception {
        TodusListMojo mojo = new TodusListMojo();

        // MavenProject
        MavenProject project = new MavenProject();
        project.setGroupId("io.github.odunlamizo");
        project.setArtifactId("todus-test");
        project.setVersion("1.0-SNAPSHOT");
        // Optional: set output directory so scanner can find classes
        project.getBuild().setOutputDirectory("target/classes");
        mojo.project = project;

        // Set the base package to scan
        mojo.basePackage = "io.github.odunlamizo.todus";

        // Call execute
        mojo.execute();
    }
}
