package com.kncept.junit5.reporter;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
	@DisplayName("test that has been renamed")
	public void renamedTest() {
	}
	
	@Test
	@Disabled
	public void skippedTest() {
		fail("should have skipped this");
	}
	
	@Test
	@Disabled
	public void failingTest() {
		fail("Failure Message passed into Assertions.fail");
	}
	
	@Test
	@Disabled
	public void exception() {
		throw new RuntimeException("RuntimeException message");
	}
	
}
