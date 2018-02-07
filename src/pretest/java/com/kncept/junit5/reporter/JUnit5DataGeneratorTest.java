package com.kncept.junit5.reporter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JUnit5DataGeneratorTest {

	@Test
	public void j5Stub() {
	}
	
	/*
	 * JUnit 5 doesn't catch INPUT and OUTPUT the way that JUnit4 does.
	 * This is a real shame... it would make test reporting soooo much nicer.
	 */
	@Test
	public void sysout() {
		System.out.println("sysout l1");
		System.out.println("sysout l2");
	}
	
	@Test
	public void syserr() {
		System.err.println("syserr l1");
		System.err.println("syserr l1");
	}
	
	@Test
	public void errAndOut() {
		System.out.println("sysout l1");
		System.err.println("syserr l1");
		System.out.println("sysout l2");
		System.err.println("syserr l1");
	}
	
	@Test
	@DisplayName("This test has a fancy display name")
	public void renamedTest() {
	}
	
	@Test
	@Disabled
	public void skippedTest() {
		fail("should have skipped this");
	}
	
	@Test
	@Disabled("It's good practice to include a REASON when you disable a test")
	public void skippedTestWithReason() {
		fail("should have skipped this");
	}
	
	
	@Test
	public void failingTest() {
		fail("Failure Message passed into Assertions.fail");
	}
	
	@Test
	public void assertAllFailure() {
		assertAll("assertAll header for TWO nested failures",
				() -> fail("failure one"),
				() -> assertTrue(true), //this bit wont fail
				() -> assertTrue(false)
		);
	}
	
	@Test
	public void multilineFailure() {
		fail("Multiline\nfailure!");
	}
	
	@Test
	public void exceptionWithMessage() {
		throw new RuntimeException("RuntimeException message");
	}
	
	@Test
	public void exceptionWithoutMessage() {
		throw new RuntimeException();
	}
	
}
