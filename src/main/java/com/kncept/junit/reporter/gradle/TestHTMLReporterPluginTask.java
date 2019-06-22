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
import com.kncept.junit.reporter.logger.Log;
import com.kncept.junit.reporter.logger.LogFactory;
import com.kncept.junit.reporter.logger.Slf4JWrapper;

public class TestHTMLReporterPluginTask extends DefaultTask {

	public TestHTMLReporterPluginTask() {
		super();
		setGroup("documentation");
		setDescription("Parses the JUnit5 produced test xml and produces a simple html report");
	}

	@TaskAction
	public void generateJunitReport() throws TestReporterError, TestReporterFailure {
		LogFactory logFactory = new Slf4JWrapper(); //gradle uses slf4j
		Log log = logFactory.logger(getClass().getName());
		
		log.info("Starting generateJunitReport() task");
		TestHTMLReporterSettings settings = (TestHTMLReporterSettings)getProject().getExtensions().getByName(TestHTMLReporterSettings.settingsExtensionName);
		if (settings == null)
			settings = new TestHTMLReporterSettings(); //use defaults
		File buildDir = getProject().getBuildDir();
		log.debug("buildDir = " + buildDir.getAbsolutePath());

		if (settings.getTestResultsDir() == null)
			throw new TestReporterFailure("config for testResultsDir is missing");
		if (settings.getTestReportsDir() == null)
			throw new TestReporterFailure("config for testReportsDir is missing");
		
		File testResultsDir = new File(buildDir, settings.getTestResultsDir());
		log.debug("testResultsDir = " + buildDir.getAbsolutePath());
		File testReportsDir = new File(buildDir, settings.getTestReportsDir());
		log.debug("testReportsDir = " + buildDir.getAbsolutePath());
		
		log.debug("maxDepth = " + settings.getMaxDepth());
		log.debug("failOnEmpty = " + settings.isFailOnEmpty());
		TestReportProcessor processor = new TestReportProcessor(new Slf4JWrapper());
		List<TestRunResults> results = processor.scan(testResultsDir, settings.getMaxDepth());
		log.debug("found " + results.size() + " test run results");
		if (settings.isFailOnEmpty() && results.isEmpty()) {
			log.info("Aborting generateJunitReport() task");
			throw new TestReporterFailure("No XML Reports to process");
		}
		
		processor.write(testReportsDir, new CssRagStatus(settings.getCssRed(), settings.getCssAmber(), settings.getCssGreen()), results);
		log.info("Completed generateJunitReport() task");
	}
	
}
