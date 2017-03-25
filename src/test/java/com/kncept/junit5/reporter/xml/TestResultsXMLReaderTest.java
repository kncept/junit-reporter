package com.kncept.junit5.reporter.xml;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TestResultsXMLReaderTest {

	@Test
	public void canReadProperties() throws IOException {
		XMLTestResults testResults = testResults();
		assertNotNull(testResults.properties());
		assertFalse(testResults.properties().isEmpty());
	}
	
	@Test
	public void canReadTestCases() throws IOException {
		XMLTestResults testResults = testResults();
		assertNotNull(testResults.testcases());
		assertFalse(testResults.testcases().isEmpty());
	}
	
	@Test
	@org.junit.Test
	public void canReadMultilineOutput() throws IOException {
		XMLTestResults testResults = testResults();
		List<String> sysout = testResults.testcases().get(0).getSystemOut();
		assertTrue(sysout.size() > 1, "Must contain more than 1 line of output");
	}
	
	private XMLTestResults testResults() throws IOException {
		InputStream in = getClass().getClassLoader().getResourceAsStream("TEST-junit-jupiter.xml");
		return new JSoupTestResultsXMLReader(in);
	}
	
}
