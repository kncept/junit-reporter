package com.kncept.junit.reporter.gradle;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.UnknownTaskException;

public class TestHTMLReporterPlugin implements Plugin<Project> {
	
	@Override
	public void apply(Project project) {
		project.getExtensions().create(TestHTMLReporterSettings.settingsExtensionName, TestHTMLReporterSettings.class);
		TestHTMLReporterPluginTask junit5HTMLReport = project.getTasks().create("junitHtmlReport", TestHTMLReporterPluginTask.class);

		addFinalizedBy("test", project, junit5HTMLReport);
		addFinalizedBy("check", project, junit5HTMLReport);
	}

	private void addFinalizedBy(String taskName, Project project, TestHTMLReporterPluginTask junit5HTMLReport) {
		try {
			Task task = project.getTasks().getByName(taskName);
			task.finalizedBy(junit5HTMLReport);
		} catch (UnknownTaskException e) {
			System.out.println("unable to finalize " + taskName);
		}
	}
	
}
