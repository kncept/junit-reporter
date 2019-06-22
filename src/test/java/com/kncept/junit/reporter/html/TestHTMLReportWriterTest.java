package com.kncept.junit.reporter.html;

import static com.kncept.junit.reporter.domain.TestCaseStatus.Passed;
import static java.nio.file.Files.createTempDirectory;
import static java.util.Arrays.asList;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kncept.junit.reporter.TestRunResults;
import com.kncept.junit.reporter.domain.CssRagStatus;
import com.kncept.junit.reporter.domain.TestCase;
import com.kncept.junit.reporter.logger.JulWrapper;
import com.kncept.junit.reporter.xml.TestSuite;

public class TestHTMLReportWriterTest {
	private int counter = 0;
	
//	@Test
//	public void category() {
//		TestRunResults writer = new TestRunResults("junit-platform");
//		assertEquals("junit-platform", writer.getCategory());
//	}
	
	@Test
	public void canFindTemplates() throws Exception {
		TestReportWriter writer = new TestReportWriter(new JulWrapper(), asList(generateSimpleResults()));
		try (InputStream template = writer.getTemplate("index.html")) {
			Assertions.assertNotNull(template);
		} catch (IOException e) {
			throw e;
		}
	}
	
	@Test
	public void writesEnoughFiles() throws IOException {
		File htmlDir = createTempDirectory(null).toFile();
		TestReportWriter writer = new TestReportWriter(new JulWrapper(), asList(generateSimpleResults()));

		writer.write(htmlDir, new CssRagStatus());
	}

	private TestRunResults generateSimpleResults() {
		TestRunResults results = new TestRunResults("");
		results.results().add(new TestTestSuite());
		return results;
	}
	
	
	class TestTestSuite implements TestSuite {
		private final String name;
		private List<TestCase> testcases;
		
		public TestTestSuite() {
			name = "testSuite" + counter++;
			testcases = new ArrayList<>();
			testcases.add(generateTestCase());
			testcases.add(generateTestCase());
			testcases.add(generateTestCase());
		}
		
		private TestCase generateTestCase() {
			TestCase testcase = new TestCase(
					"testName" + counter,
					"java.class.name" + counter,
					new BigDecimal(counter),
					Passed);
			counter++;
			return testcase;
		}
		
		@Override
		public String name() {
			return name;
		}
		
		@Override
		public List<String> sysErr() {
			return Collections.emptyList();
		}
		
		@Override
		public List<String> sysOut() {
			return Collections.emptyList();
		}
		
		@Override
		public LinkedHashMap<String, String> systemProperties() {
			return new LinkedHashMap<>();
		}
		
		@Override
		public LinkedHashMap<String, String> testsuiteProperties() {
			return new LinkedHashMap<>();
		}
		
		@Override
		public List<TestCase> testcases() {
			return testcases;
		}
	}

}
