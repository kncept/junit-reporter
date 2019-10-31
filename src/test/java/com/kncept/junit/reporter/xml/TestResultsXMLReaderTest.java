package com.kncept.junit.reporter.xml;

import com.kncept.junit.reporter.domain.TestCase;
import com.kncept.junit.reporter.logger.JulWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.kncept.junit.reporter.domain.TestCaseStatus.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestResultsXMLReaderTest {

	@Test
	public void canReadSystemProperties() {
		TestSuite testResults = testResults();
		assertNotNull(testResults.systemProperties());
		assertFalse(testResults.systemProperties().isEmpty());
		for(String key: testResults.systemProperties().keySet()) {
			assertNotNull(testResults.systemProperties().get(key));
		}
	}

	@Test
	public void canReadEnvProperties() {
		TestSuite testResults = testResults();
		assertNotNull(testResults.testsuiteProperties());
		assertFalse(testResults.testsuiteProperties().isEmpty());
		for(String key: testResults.testsuiteProperties().keySet()) {
			assertNotNull(testResults.testsuiteProperties().get(key));
		}
	}
	
	@Test
	public void canReadTestCases() {
		TestSuite testResults = testResults();
		assertNotNull(testResults.testcases());
		assertFalse(testResults.testcases().isEmpty());
	}
	
	@Test
	public void canReadMultilineOutput() {
		TestSuite testResults = testResults();
		List<String> sysout = testResults.testcases().get(0).getSystemOut();
		assertTrue(sysout.size() > 1, "Must contain more than 1 line of output");
	}
	
	@Test
	public void canFindFailedTests() {
		TestCase testCase = testCase("failingTest()");
		assertEquals(Failed, testCase.getStatus());
	}
	
	@Test
	public void canFindErroredTests() {
		TestCase testCase = testCase("exceptionWithoutMessage()");
		assertEquals(Errored, testCase.getStatus());
	}
	
	@Test
	//it seems that JUnit still outputs skipped tests
	public void canFindSkippedTests() {
		TestCase testCase = testCase("skippedTest()");
		assertEquals(Skipped, testCase.getStatus());
	}
	
	@Test
	public void canFindSuccessfulTests() {
		TestCase testCase = testCase("j5Stub()");
		assertEquals(Passed, testCase.getStatus());
	}
	
	@Test
	public void canFindSystemOutOutput() {
		TestCase testCase = testCase("sysout()");
		assertFalse(testCase.getSystemOut().isEmpty());
		System.out.println("How do I get sys out ??");
	}
	
	private TestCase testCase(String name) {
		TestSuite testResults = testResults();
		for(TestCase testCase: testResults.testcases())
			if (testCase.getName().equals(name))
				return testCase;
		throw new RuntimeException("Unable to find a testcase with name " + name);
	}
	private TestSuite testResults() {
		try {
			InputStream in = getClass().getClassLoader().getResourceAsStream("TEST-junit-jupiter.xml");
			return new Junit4DomReader(new JulWrapper(), "junit-jupiter", in);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new RuntimeException(e);
		}
	}

}
