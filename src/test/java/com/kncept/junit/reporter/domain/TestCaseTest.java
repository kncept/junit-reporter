package com.kncept.junit.reporter.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class TestCaseTest {

	@Test
	// https://github.com/kncept/junit-reporter/issues/1
	public void packageNameExtraction() {
		TestCase testCase = new TestCase("name", "package.name.classname", BigDecimal.ZERO, TestCaseStatus.Skipped);
		assertEquals("package.name", testCase.getPackagename());
	}
	
	@Test
	// https://github.com/kncept/junit-reporter/issues/1
	public void defaultPackageNameExtraction() {
		TestCase testCase = new TestCase("name", "classname", BigDecimal.ZERO, TestCaseStatus.Skipped);
		assertEquals("", testCase.getPackagename());
	}
	
}
