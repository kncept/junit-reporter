package com.kncept.junit5.reporter;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/*
 * This class can be used to generate a static file to test against.
 * It can't contain failing tests for the real build however, as that would fail the build.
 */
public class J5T2Test {

	@Test
	public void j5Stub() {
		
	}
	
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
	public void exceptionWithMessage() {
		throw new RuntimeException("RuntimeException message");
	}
	
	@Test
	public void exceptionWithoutMessage() {
		throw new RuntimeException();
	}
	
}
