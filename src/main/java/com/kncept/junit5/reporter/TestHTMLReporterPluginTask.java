package com.kncept.junit5.reporter;

import java.io.File;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class TestHTMLReporterPluginTask extends DefaultTask {

	@TaskAction
	public void generateJunitReport() {
		
		//stub
		TestHTMLReporterExtension settings = (TestHTMLReporterExtension)getProject().getExtensions().getByName("junit5HTMLReportSettings");
		
		File buildDir = getProject().getBuildDir();
		
		System.out.println("build dir = " + buildDir.getPath());
		
		if (settings == null)
			System.out.println("settings is null");
		else {
			System.out.println("settings.size = " + settings.getReportDirectories().size());
			for (int i = 0; i < settings.getReportDirectories().size(); i++) {
				Object val = settings.getReportDirectories().get(i);
				System.out.println(i + ": " + val.getClass().getName() + " " + val);
			}
		}
		
		System.out.println("need to generate some html here...");
	}
	
}
