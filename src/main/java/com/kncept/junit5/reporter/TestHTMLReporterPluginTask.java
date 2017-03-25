package com.kncept.junit5.reporter;

import java.io.File;
import java.io.IOException;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import com.kncept.junit5.reporter.html.TestHTMLReportWriter;
import com.kncept.junit5.reporter.xml.JSoupTestResultsXMLReader;
import com.kncept.junit5.reporter.xml.XMLTestResults;

public class TestHTMLReporterPluginTask extends DefaultTask {

	@TaskAction
	public void generateJunitReport() throws IOException {
		
		//stub
		TestHTMLReporterExtension settings = (TestHTMLReporterExtension)getProject().getExtensions().getByName("junit5HTMLReportSettings");
		
		File buildDir = getProject().getBuildDir();
		
		//junit 5: test-results/junit-platform
			// 1 file per execution
		//junit 4: test-results/test
			// 1 file per class
			//reports/tests/test
		
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
		
		File testResultsDir = new File(buildDir, "test-results");
		File testReportsDir = new File(buildDir, "reports/tests");
		
		for(File file: testResultsDir.listFiles()) {
			TestHTMLReportWriter reporter = new TestHTMLReportWriter(file.getName());
			for(File testFile: file.listFiles()) {
				if (
						testFile.isFile() && 
						testFile.getName().startsWith("TEST-") && 
						testFile.getName().endsWith(".xml")) {
					XMLTestResults restResults = readFile(testFile);
					reporter.include(restResults);
				}
			}
			reporter.write(testReportsDir);
		}
	}
	
	private XMLTestResults readFile(File file) throws IOException {
		return new JSoupTestResultsXMLReader(file);
	}
	
}
