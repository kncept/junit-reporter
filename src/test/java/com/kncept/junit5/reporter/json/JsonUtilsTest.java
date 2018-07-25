package com.kncept.junit5.reporter.json;

import static com.kncept.junit5.reporter.json.JsonUtils.addDelimiters;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class JsonUtilsTest {
	@Test
	public void canDelimitStringCorrectly() {
		assertEquals("\\\\", addDelimiters("\\"));
		assertEquals("\\\"", addDelimiters("\""));
		assertEquals("\\n", addDelimiters("\n"));
		assertEquals("\\t", addDelimiters("\t"));
	}

}
