package com.kncept.junit.reporter.gradle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import com.kncept.junit.reporter.domain.CssRagStatus;
import com.kncept.junit.reporter.html.TestReportProcessor;

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

		if (settings.getTestResultsDir() == null)
			throw new FileNotFoundException("config for testResultsDir is missing");
		if (settings.getTestReportsDir() == null)
			throw new FileNotFoundException("config for testReportsDir is missing");
		
		File testResultsDir = new File(buildDir, settings.getTestResultsDir());
		File testReportsDir = new File(buildDir, settings.getTestReportsDir());
		
		TestReportProcessor reportProcessor = new TestReportProcessor(
				testResultsDir,
				testReportsDir,
				settings.isAggregated(),
				settings.isFailOnEmpty(),
				new CssRagStatus(settings.getCssRed(), settings.getCssAmber(), settings.getCssGreen())
		);
		reportProcessor.run();
	}
	
}
