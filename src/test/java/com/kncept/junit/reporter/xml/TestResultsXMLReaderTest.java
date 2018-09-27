package com.kncept.junit.reporter.xml;

import static com.kncept.junit.reporter.domain.TestCaseStatus.Errored;
import static com.kncept.junit.reporter.domain.TestCaseStatus.Failed;
import static com.kncept.junit.reporter.domain.TestCaseStatus.Passed;
import static com.kncept.junit.reporter.domain.TestCaseStatus.Skipped;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.kncept.junit.reporter.domain.TestCase;

public class TestResultsXMLReaderTest {

	@Test
	public void canReadProperties() throws Exception {
		TestSuite testResults = testResults();
		assertNotNull(testResults.systemProperties());
		assertFalse(testResults.systemProperties().isEmpty());
		for(String key: testResults.systemProperties().keySet()) {
			assertNotNull(testResults.systemProperties().get(key));
		}
	}
	
	@Test
	public void canReadTestCases() throws Exception {
		TestSuite testResults = testResults();
		assertNotNull(testResults.testcases());
		assertFalse(testResults.testcases().isEmpty());
	}
	
	@Test
	public void canReadMultilineOutput() throws Exception {
		TestSuite testResults = testResults();
		List<String> sysout = testResults.testcases().get(0).getSystemOut();
		assertTrue(sysout.size() > 1, "Must contain more than 1 line of output");
	}
	
	@Test
	public void canFindFailedTests() throws Exception {
		TestCase testCase = testCase("failingTest()");
		assertEquals(Failed, testCase.getStatus());
	}
	
	@Test
	public void canFindErroredTests() throws Exception {
		TestCase testCase = testCase("exceptionWithoutMessage()");
		assertEquals(Errored, testCase.getStatus());
	}
	
	@Test
	//it seems that JUnit still outputs skipped tests
	public void canFindSkippedTests() throws Exception {
		TestCase testCase = testCase("skippedTest()");
		assertEquals(Skipped, testCase.getStatus());
	}
	
	@Test
	public void canFindSuccessfulTests() throws Exception {
		TestCase testCase = testCase("j5Stub()");
		assertEquals(Passed, testCase.getStatus());
	}
	
	private TestCase testCase(String name) throws Exception {
		TestSuite testResults = testResults();
		for(TestCase testCase: testResults.testcases())
			if (testCase.getName().equals(name))
				return testCase;
		throw new RuntimeException("Unable to find a testcase with name " + name);
	}
	private TestSuite testResults() throws Exception {
		InputStream in = getClass().getClassLoader().getResourceAsStream("TEST-junit-jupiter.xml");
		return new Junit4DomReader("junit-jupiter", in);
	}

}
