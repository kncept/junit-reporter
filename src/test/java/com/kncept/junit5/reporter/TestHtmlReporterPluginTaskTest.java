package com.kncept.junit5.reporter;

import static com.kncept.junit5.reporter.gradle.TestHTMLReporterPluginTask.isXmlTestFile;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;


public class TestHtmlReporterPluginTaskTest {

	
	@Test
	public void xmlFileTesterIsValid() throws IOException {
		File validTestFile = File.createTempFile("TEST-", ".xml");
		File inalidTestFile = File.createTempFile("non-", ".xml");
		assertTrue(isXmlTestFile(validTestFile));
		assertFalse(isXmlTestFile(inalidTestFile));
		validTestFile.delete();
		validTestFile.mkdir();
		//now its INVALID because its a directory (not really defined...)
		assertFalse(isXmlTestFile(inalidTestFile));
		
	}
}
