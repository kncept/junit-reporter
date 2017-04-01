package com.kncept.junit5.reporter.html;

import static com.kncept.junit5.reporter.domain.TestCase.Status.Passed;
import static org.gradle.internal.impldep.com.google.common.io.Files.createTempDir;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.kncept.junit5.reporter.domain.TestCase;

public class TestHTMLReportWriterTest {
	private int counter = 0;
	
	@Test
	public void category() {
		TestHTMLReportWriter writer = new TestHTMLReportWriter("junit-platform");
		assertEquals("junit-platform", writer.getCategory());
	}
	
	@Test
	public void canFindTemplates() throws Exception {
		TestHTMLReportWriter writer = new TestHTMLReportWriter("");
		try (InputStream template = writer.getTemplate("index.html")) {
			Assertions.assertNotNull(template);
		} catch (IOException e) {
			throw e;
		}
	}
	
	@Test
	public void writesEnoughFiles() throws IOException {
		File htmlDir = createTempDir();
		TestHTMLReportWriter writer = new TestHTMLReportWriter("junit-platform");
		
		writer.include(generateTestCase());
		writer.include(generateTestCase());
		writer.include(generateTestCase());
		
		writer.write(htmlDir);
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
