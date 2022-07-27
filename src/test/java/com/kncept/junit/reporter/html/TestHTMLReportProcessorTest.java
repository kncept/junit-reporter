package com.kncept.junit.reporter.html;

import static com.kncept.junit.reporter.TestReportProcessor.xmlTestFile;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;

public class TestHTMLReportProcessorTest {

	
	@Test
	public void xmlFileTesterIsValid() throws IOException {
		File validTestFile = Files.createTempDirectory("TEST-" + ".xml").toFile();
		File inalidTestFile = File.createTempFile("non-", ".xml");
		assertTrue(xmlTestFile(validTestFile));
		assertFalse(xmlTestFile(inalidTestFile));
		//now its INVALID because its a directory (not really defined...)
		assertFalse(xmlTestFile(inalidTestFile));
		
	}
}
