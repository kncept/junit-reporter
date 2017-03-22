package com.kncept.junit5.reporter;

import static org.gradle.testfixtures.ProjectBuilder.builder;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.junit.jupiter.api.Test;

public class TestHTMLReporterPluginTest {

	@Test
	public void taskIsAppliedToProject() {
		Project project = builder().build();
		project.getPlugins().apply(TestHTMLReporterPlugin.class);
		
		Task task = project.getTasks().getByName("junit5HTMLReport");
		assertNotNull(task);
		
	}
	
}
