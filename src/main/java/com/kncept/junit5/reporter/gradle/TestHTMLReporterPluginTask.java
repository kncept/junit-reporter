package com.kncept.junit5.reporter.gradle;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.xml.sax.SAXException;

import com.kncept.junit5.reporter.html.TestHTMLReportWriter;
import com.kncept.junit5.reporter.xml.Junit4DomReader;
import com.kncept.junit5.reporter.xml.XMLTestResults;

public class TestHTMLReporterPluginTask extends DefaultTask {
	
	public TestHTMLReporterPluginTask() {
		super();
		setGroup("documentation");
		setDescription("Parses the JUnit5 produced test xml and produces a simple html report");
	}

	@TaskAction
	public void generateJunitReport() throws IOException {
		//stub
		TestHTMLReporterSettings settings = (TestHTMLReporterSettings)getProject().getExtensions().getByName(TestHTMLReporterSettings.settingsExtensionName);
		if (settings == null)
			settings = new TestHTMLReporterSettings(); //use defaults
		File buildDir = getProject().getBuildDir();
		
		//junit 5: test-results/junit-platform
			// 1 file per execution
		//junit 4: test-results/test
			// 1 file per class
			//reports/tests/test

		
		File testResultsDir = new File(buildDir, settings.getTestResultsDir());
		File testReportsDir = new File(buildDir, settings.getTestReportsDir());
		
		TestHTMLReportWriter reporter =null;
		
		for(File file: testResultsDir.listFiles()) {
			if (!file.isDirectory() && !settings.isAggregated())
				continue;
			if (!settings.isAggregated())
				reporter = null;
			
				if (file.isDirectory()) {
					for(File testFile: file.listFiles()) {
						if (isXmlTestFile(testFile)) {
							XMLTestResults restResults = readFile(testFile);
							if (reporter == null)
								reporter = new TestHTMLReportWriter(settings.isAggregated() ? null : file.getName());
							reporter.include(restResults);
						}
					}
					if (reporter != null && !settings.isAggregated())
						reporter.write(testReportsDir, settings);
				} else if (isXmlTestFile(file) && settings.isAggregated()) {
					XMLTestResults restResults = readFile(file);
					if (reporter == null)
						reporter = new TestHTMLReportWriter(settings.isAggregated() ? null : file.getName());
					reporter.include(restResults);
				}
		}
		if (reporter != null && settings.isAggregated())
			reporter.write(testReportsDir, settings);
		
		if (settings.isAggregated()) {
			System.out.println("Reports written to " + testReportsDir.getAbsolutePath() + File.separator + "index.html");
		} else {
			System.out.println("Reports written to " + testReportsDir.getAbsolutePath() + File.separator + "junit-platform" + File.separator + "index.html");
		}
	}
	
	public static boolean isXmlTestFile(File file) {
		return file.isFile() && 
				file.getName().startsWith("TEST-") && 
				file.getName().endsWith(".xml");
	}
	
	
	private XMLTestResults readFile(File file) throws IOException {
		try (InputStream in = new FileInputStream(file)) {
			return new Junit4DomReader(in);
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		}
		
	}
	
}
