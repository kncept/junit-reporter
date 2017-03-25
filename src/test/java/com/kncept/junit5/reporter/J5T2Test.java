package com.kncept.junit5.reporter;

import static org.junit.jupiter.api.Assertions.fail;

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
	public void failingTest() {
		fail("Failure Message passed into Assertions.fail");
	}
	
	@Test
	public void exception() {
		throw new RuntimeException("RuntimeException message");
	}
	
}
