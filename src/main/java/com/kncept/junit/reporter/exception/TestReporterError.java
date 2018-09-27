package com.kncept.junit.reporter.exception;

/**
 * Something serious went wrong - nasty classpath of IO issues
 * @author ebola
 *
 */
public class TestReporterError extends Exception {

	public TestReporterError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public TestReporterError(Throwable cause) {
		super(cause);
	}
}
