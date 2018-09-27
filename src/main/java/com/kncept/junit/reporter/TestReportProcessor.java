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
import com.kncept.junit.reporter.xml.Junit4DomReader;
import com.kncept.junit.reporter.xml.TestSuite;

public class TestReportProcessor {
	
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.out.println("Usage:");
			System.out.println(" TestHTMLReportProcessor option=value");
			System.out.println(" with the following options:");
			System.out.println("  testResultsDir");
			System.out.println("  testReportsDir");
			System.out.println("  maxDepth");
			System.out.println("  failOnEmpty");
			System.out.println("  cssRed");
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
		
		
		TestReportProcessor processor = new TestReportProcessor();

		for(String arg: args) {
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
		TestReportWriter writer = new TestReportWriter(runResults);
		writer.write(testReportsDir, cssRagStatus);
		} catch (IOException e) {
			throw new TestReporterError(e);
		}
		System.out.println("Reports written to " + testReportsDir.getAbsolutePath());
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
			return new Junit4DomReader(testCaseName(file), in);
		} catch (ParserConfigurationException e) {
			throw new TestReporterError(e);
		} catch (SAXException e) {
			throw new TestReporterError(e);
		} catch (IOException e) {
			throw new TestReporterError(e);
		}
	}
	
}
