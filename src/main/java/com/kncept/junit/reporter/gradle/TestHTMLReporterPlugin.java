package com.kncept.junit.reporter.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class TestHTMLReporterPlugin implements Plugin<Project> {
	
	@Override
	public void apply(Project project) {
		project.getExtensions().create(TestHTMLReporterSettings.settingsExtensionName, TestHTMLReporterSettings.class);
		TestHTMLReporterPluginTask junit5HTMLReport = project.getTasks().create("junitHtmlReport", TestHTMLReporterPluginTask.class);
		addFinalizedBy("test", project, junit5HTMLReport);
		addFinalizedBy("check", project, junit5HTMLReport);
	}

	private void addFinalizedBy(String taskName, Project project, TestHTMLReporterPluginTask junit5HTMLReport) {
		boolean applied = false;
		project.getTasks().forEach(task -> {
			if (plainTaskName(task.getName()).equals(taskName))
				task.finalizedBy(junit5HTMLReport);
		});
	}
	
	public String plainTaskName(String taskName) {
		if (taskName.contains(":"))
			taskName = taskName.substring(taskName.lastIndexOf(":") + 1);
		return taskName;
	}
	
}
