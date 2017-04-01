package com.kncept.junit5.reporter.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.gradle.internal.impldep.com.google.common.io.Files;
import org.junit.jupiter.api.Test;

import com.kncept.junit5.reporter.domain.TestCase;
import com.kncept.junit5.reporter.domain.TestCase.Status;
import com.kncept.junit5.reporter.html.TestHTMLReportWriter;

public class TestResultsXMLReaderTest {

	@Test
	public void canReadProperties() throws Exception {
		XMLTestResults testResults = testResults();
		assertNotNull(testResults.properties());
		assertFalse(testResults.properties().isEmpty());
		for(String key: testResults.properties().keySet()) {
			System.out.println(key + testResults.properties().get(key));
		}
	}
	
	@Test
	public void canReadTestCases() throws Exception {
		XMLTestResults testResults = testResults();
		assertNotNull(testResults.testcases());
		assertFalse(testResults.testcases().isEmpty());
	}
	
	@Test
	public void canReadMultilineOutput() throws Exception {
		XMLTestResults testResults = testResults();
		List<String> sysout = testResults.testcases().get(0).getSystemOut();
		assertTrue(sysout.size() > 1, "Must contain more than 1 line of output");
	}
	
	@Test
	public void canFindFailedTests() throws Exception {
		TestCase testCase = testCase("failingTest()");
		assertEquals(Status.Failed, testCase.getStatus());
	}
	
	@Test
	public void canFindErroredTests() throws Exception {
		TestCase testCase = testCase("exception()");
		assertEquals(Status.Errored, testCase.getStatus());
	}
	
	@Test
	//it seems that JUnit still outputs skipped tests
	public void canFindSkippedTests() throws Exception {
		TestCase testCase = testCase("skippedTest()");
		assertEquals(Status.Skipped, testCase.getStatus());
	}
	
	@Test
	public void canFindSuccessfulTests() throws Exception {
		TestCase testCase = testCase("j5Stub()");
		assertEquals(Status.Passed, testCase.getStatus());
	}
	
	private TestCase testCase(String name) throws Exception {
		XMLTestResults testResults = testResults();
		for(TestCase testCase: testResults.testcases())
			if (testCase.getName().equals(name))
				return testCase;
		throw new RuntimeException("Unable to find a testcase with name " + name);
	}
	
	
	private XMLTestResults testResults() throws Exception {
		InputStream in = getClass().getClassLoader().getResourceAsStream("TEST-junit-jupiter.xml");
		return new Junit4DomReader(in);
	}
	
	
	
	@org.junit.Test
	public void writeFilesForSample() throws Exception {
		XMLTestResults testResults = testResults();
		
		File htmlDir = Files.createTempDir();
		System.out.println("HTML reports in " + htmlDir.getAbsolutePath());
		
		TestHTMLReportWriter writer = new TestHTMLReportWriter("junit-platform");
		
		writer.include(testResults);
		
		writer.write(htmlDir);
	}
	
}
