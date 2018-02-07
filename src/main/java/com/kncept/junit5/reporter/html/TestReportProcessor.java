package com.kncept.junit5.reporter.html;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.kncept.junit5.reporter.domain.CssRagStatus;
import com.kncept.junit5.reporter.xml.Junit4DomReader;
import com.kncept.junit5.reporter.xml.XMLTestResults;

public class TestReportProcessor {
	
	//input directory
	File testResultsDir;
	
	//output directory
	File testReportsDir;
	
	//junit 5: test-results/junit-platform
	// 1 file per execution
	//junit 4: test-results/test
	// 1 file per class
	//reports/tests/test
	boolean aggregatedReporting;
	
	boolean failOnEmpty;
	CssRagStatus cssRagStatus;
	
	public static void main(String[] args) throws Exception {
		
		if (args.length == 0) {
			System.out.println("Usage:");
			System.out.println(" TestHTMLReportProcessor option=value");
			System.out.println(" with the following options:");
			System.out.println("  testResultsDir");
			System.out.println("  testReportsDir");
			System.out.println("  aggregatedReporting");
			System.out.println("  failOnEmpty");
			System.out.println("  cssRed");
			System.out.println("  cssAmber");
			System.out.println("  cssGreen");
		}
		
		TestReportProcessor processor = new TestReportProcessor(
				new File("."),
				new File("."),
				false,
				false,
				new CssRagStatus());

		for(String arg: args) {
			processArg("testResultsDir", arg, dir -> processor.testResultsDir = new File(dir));
			processArg("testReportsDir", arg, dir -> processor.testReportsDir = new File(dir));
			processArg("aggregatedReporting", arg, b -> processor.aggregatedReporting = Boolean.parseBoolean(b));
			processArg("failOnEmpty", arg, b -> processor.failOnEmpty = Boolean.parseBoolean(b));
			processArg("cssRed", arg, css -> processor.cssRagStatus.red = css);
			processArg("cssAmber", arg, css -> processor.cssRagStatus.amber = css);
			processArg("cssGreen", arg, css -> processor.cssRagStatus.green = css);
		}
		
		processor.run();
	}
	
	private static void processArg(String name, String arg, Consumer<String> consumer) {
		if (arg.startsWith(name +"="))
			consumer.accept(arg.substring(name.length() + 1));
	}
	
	
	public TestReportProcessor(
			File testResultsDir,
			File testReportsDir,
			boolean aggregatedReporting,
			boolean failOnEmpty,
			CssRagStatus cssRagStatus) {
		this.testResultsDir = testResultsDir;
		this.testReportsDir = testReportsDir;
		this.aggregatedReporting = aggregatedReporting;
		this.failOnEmpty = failOnEmpty;
		this.cssRagStatus = cssRagStatus;
	}
	
	
	public void run() throws IOException {
		TestReportWriter reporter =null;
		
		for(File file: testResultsDir.listFiles()) {
			if (!file.isDirectory() && !aggregatedReporting)
				continue;
			if (!aggregatedReporting)
				reporter = null;
			
				if (file.isDirectory()) {
					for(File testFile: file.listFiles()) {
						if (isXmlTestFile(testFile)) {
							XMLTestResults restResults = readFile(testFile);
							if (reporter == null)
								reporter = new TestReportWriter(aggregatedReporting ? null : file.getName());
							reporter.include(restResults);
						}
					}
					if (reporter != null && !aggregatedReporting)
						reporter.write(testReportsDir, cssRagStatus);
				} else if (isXmlTestFile(file) && aggregatedReporting) {
					XMLTestResults restResults = readFile(file);
					if (reporter == null)
						reporter = new TestReportWriter(aggregatedReporting ? null : file.getName());
					reporter.include(restResults);
				}
		}
		if (reporter == null) { //no files? break the build by default.
			if (failOnEmpty)
				throw new RuntimeException("No Test XML Reports to generate a HTML Report found.");
			else {
				System.out.println("No Test XML Reports to generate a HTML Report found.");
				return;
			}
		}
		if (aggregatedReporting)
			reporter.write(testReportsDir, cssRagStatus);
		
		if (aggregatedReporting) {
			System.out.println("Reports written to " + testReportsDir.getAbsolutePath() + File.separator + "index.html");
		} else {
			System.out.println("Reports written to " + testReportsDir.getAbsolutePath() + File.separator + reporter.getCategory() + File.separator + "index.html");
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
