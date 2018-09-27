package com.kncept.junit.reporter.gradle;

import java.io.File;
import java.util.List;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import com.kncept.junit.reporter.TestReportProcessor;
import com.kncept.junit.reporter.TestRunResults;
import com.kncept.junit.reporter.domain.CssRagStatus;
import com.kncept.junit.reporter.exception.TestReporterError;
import com.kncept.junit.reporter.exception.TestReporterFailure;

public class TestHTMLReporterPluginTask extends DefaultTask {
	
	public TestHTMLReporterPluginTask() {
		super();
		setGroup("documentation");
		setDescription("Parses the JUnit5 produced test xml and produces a simple html report");
	}

	@TaskAction
	public void generateJunitReport() throws TestReporterError, TestReporterFailure {
		TestHTMLReporterSettings settings = (TestHTMLReporterSettings)getProject().getExtensions().getByName(TestHTMLReporterSettings.settingsExtensionName);
		if (settings == null)
			settings = new TestHTMLReporterSettings(); //use defaults
		File buildDir = getProject().getBuildDir();

		if (settings.getTestResultsDir() == null)
			throw new TestReporterFailure("config for testResultsDir is missing");
		if (settings.getTestReportsDir() == null)
			throw new TestReporterFailure("config for testReportsDir is missing");
		
		File testResultsDir = new File(buildDir, settings.getTestResultsDir());
		File testReportsDir = new File(buildDir, settings.getTestReportsDir());
		
		TestReportProcessor processor = new TestReportProcessor();
		List<TestRunResults> results = processor.scan(testResultsDir, settings.getMaxDepth());
		if (settings.isFailOnEmpty() && results.isEmpty())
			throw new TestReporterFailure("No XML Reports to process");
		
		processor.write(testReportsDir, new CssRagStatus(settings.getCssRed(), settings.getCssAmber(), settings.getCssGreen()), results);
	}
	
}
