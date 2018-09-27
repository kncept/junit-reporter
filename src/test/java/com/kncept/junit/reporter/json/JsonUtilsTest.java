package com.kncept.junit.reporter.json;

import static com.kncept.junit.reporter.json.JsonUtils.addDelimiters;
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
