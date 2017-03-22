package com.kncept.junit5.reporter;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class TestHTMLReporterPlugin implements Plugin<Project> {
	
	@Override
	public void apply(Project project) {
		project.getExtensions().create("junit5HTMLReportSettings", TestHTMLReporterExtension.class);
		TestHTMLReporterPluginTask junit5HTMLReport = project.getTasks().create("junit5HTMLReport", TestHTMLReporterPluginTask.class);

		junit5HTMLReport.dependsOn(project.getTasks().getByName("check"));
		project.getTasks().getByName("assemble").dependsOn(junit5HTMLReport);
	}

}
