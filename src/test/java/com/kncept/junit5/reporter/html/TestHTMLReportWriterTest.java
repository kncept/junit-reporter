package com.kncept.junit5.reporter.html;

import static com.kncept.junit5.reporter.domain.TestCaseStatus.Passed;
import static java.nio.file.Files.createTempDirectory;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kncept.junit5.reporter.domain.CssRagStatus;
import com.kncept.junit5.reporter.domain.TestCase;

public class TestHTMLReportWriterTest {
	private int counter = 0;
	
	@Test
	public void category() {
		TestReportWriter writer = new TestReportWriter("junit-platform");
		assertEquals("junit-platform", writer.getCategory());
	}
	
	@Test
	public void canFindTemplates() throws Exception {
		TestReportWriter writer = new TestReportWriter("");
		try (InputStream template = writer.getTemplate("index.html")) {
			Assertions.assertNotNull(template);
		} catch (IOException e) {
			throw e;
		}
	}
	
	@Test
	public void writesEnoughFiles() throws IOException {
		File htmlDir = createTempDirectory(null).toFile();
		TestReportWriter writer = new TestReportWriter("junit-platform");

		writer.include(generateTestCase());
		writer.include(generateTestCase());
		writer.include(generateTestCase());

		writer.write(htmlDir, new CssRagStatus());
	}
	
	@Test
	public void canDelimitStringCorrectly() {
		TestReportWriter writer = new TestReportWriter("");
		assertEquals("\\\\", writer.addDelimiters("\\"));
		assertEquals("\\\"", writer.addDelimiters("\""));
		assertEquals("\\n", writer.addDelimiters("\n"));
		assertEquals("\\t", writer.addDelimiters("\t"));
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

}
