package com.kncept.junit.reporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.kncept.junit.reporter.domain.CssRagStatus;
import com.kncept.junit.reporter.exception.TestReporterError;
import com.kncept.junit.reporter.exception.TestReporterFailure;
import com.kncept.junit.reporter.html.TestReportWriter;
import com.kncept.junit.reporter.logger.JulWrapper;
import com.kncept.junit.reporter.logger.Log;
import com.kncept.junit.reporter.logger.LogFactory;
import com.kncept.junit.reporter.xml.Junit4DomReader;
import com.kncept.junit.reporter.xml.TestSuite;

public class TestReportProcessor {
	
	private final LogFactory logFactory;
	private final Log log;
	
	public TestReportProcessor(LogFactory logFactory) {
		this.logFactory = logFactory;
		this.log = logFactory.logger(getClass().getName());
	}
	
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.out.println("Usage:");
			System.out.println(" TestHTMLReportProcessor option=value");
			System.out.println(" with the following options:");
			System.out.println("  dir            (sets results and reports directories)");
			System.out.println("  testResultsDir (sets results directory))");
			System.out.println("  testReportsDir (sets reports directory)");
			System.out.println("  maxDepth       (nested directories to scan. default 3)");
			System.out.println("  failOnEmpty    (fail on no results found)");
			System.out.println("  cssRed)");
			System.out.println("  cssAmber");
			System.out.println("  cssGreen");
			System.exit(0);
		}
		
		//input params
		File testResultsDir = new File(".");
		Integer maxDepth = 3;

		//output params
		File testReportsDir = new File(".");
		String cssRed = "red";
		String cssAmber = "orange";
		String cssGreen = "green";
		
		
		TestReportProcessor processor = new TestReportProcessor(new JulWrapper());

		for(String arg: args) {
			testResultsDir = processArg("dir", arg, dir -> new File(dir), testResultsDir);
			testReportsDir = processArg("dir", arg, dir -> new File(dir), testReportsDir);

			testResultsDir = processArg("testResultsDir", arg, dir -> new File(dir), testResultsDir);
			testReportsDir = processArg("testReportsDir", arg, dir -> new File(dir), testReportsDir);
			maxDepth = processArg("maxDepth", arg, i -> Integer.parseInt(i), maxDepth);
			cssRed = processArg("cssRed", arg, css -> css, cssRed);
			cssAmber = processArg("cssAmber", arg, css -> css, cssAmber);
			cssGreen = processArg("cssGreen", arg, css -> css, cssGreen);
		}
		
		processor.write(
				testReportsDir,
				new CssRagStatus(cssRed, cssAmber, cssGreen),
				processor.scan(testResultsDir, maxDepth));
	}
	
	private static <T> T processArg(String name, String arg, Function<String, T> consumer, T defaultValue) {
		if (arg.startsWith(name +"="))
			return consumer.apply(arg.substring(name.length() + 1));
		return defaultValue;
	}
	
	public List<TestRunResults> scan(File testResultsDir, int maxDepth) throws TestReporterError, TestReporterFailure {
		List<TestRunResults> allRunResults = scan(testResultsDir, ".", maxDepth);
		log.debug("Total results found: " + allRunResults.size());
		return allRunResults;
	}
	
	private List<TestRunResults> scan(File resultsDir, String category, int depthRemaining) throws TestReporterError {
		depthRemaining--;
		List<TestRunResults> allRunResults = new ArrayList<>();
		TestRunResults runResults = null;
		if (resultsDir.exists() && resultsDir.isDirectory()) for(File file: resultsDir.listFiles()) {
			if (file.isDirectory()) {
				if (depthRemaining >= 0)
					allRunResults.addAll(scan(file, category + "/" + file.getName(), depthRemaining));
			} else {
				if (xmlTestFile(file)) {
					if (runResults == null) {
						runResults = new TestRunResults(category);
						allRunResults.add(runResults);
					}
					log.debug("parsing file " + file.getPath());
					runResults.results().add(readFile(file));
				}
			}
		}
		return allRunResults;
	}
	
	public void write(File testReportsDir, CssRagStatus cssRagStatus, List<TestRunResults> runResults) throws TestReporterError {
		if (runResults.isEmpty())
			return;
		try {
		TestReportWriter writer = new TestReportWriter(logFactory, runResults);
		writer.write(testReportsDir, cssRagStatus);
		} catch (IOException e) {
			throw new TestReporterError(e);
		}
		log.info("Reports written to file://" + testReportsDir.getAbsolutePath() + File.separator + "index.html");
	}
	
	
	public static boolean xmlTestFile(File file) {
		return file.isFile() && 
				file.getName().startsWith("TEST-") && 
				file.getName().endsWith(".xml");
	}
	
	public static String testCaseName(File file) {
		if (xmlTestFile(file))
			return file.getName().substring(5, file.getName().length() - 4);
		throw new UnsupportedOperationException("Unable to determine test case name from file " + file.getName());
	}
	
	private TestSuite readFile(File file) throws TestReporterError {
		try (InputStream in = new FileInputStream(file)) {
			return new Junit4DomReader(logFactory, testCaseName(file), in);
		} catch (ParserConfigurationException e) {
			throw new TestReporterError(e);
		} catch (SAXException e) {
			throw new TestReporterError(e);
		} catch (IOException e) {
			throw new TestReporterError(e);
		}
	}
	
}
