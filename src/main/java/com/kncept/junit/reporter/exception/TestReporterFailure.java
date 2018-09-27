package com.kncept.junit.reporter.exception;

/**
 * Generally an issue with configuration
 * @author ebola
 *
 */
public class TestReporterFailure extends Exception {

	public TestReporterFailure(String message) {
		super(message);
	}
}
