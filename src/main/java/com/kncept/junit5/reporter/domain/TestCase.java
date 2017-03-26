package com.kncept.junit5.reporter.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestCase {
	
	public static enum Status {Passed, Skipped, Failed, Errored}

	private final String name; //test name;
	private final String classname;
	private final BigDecimal duration; //time
	private final Status status;
	private final List<String> systemOut;
	private final List<String> systemErr;
	
//	<testsuite name="JUnit Jupiter" tests="7" skipped="0" failures="0" errors="0" time="10.016" hostname="Trajan" timestamp="2017-03-23T12:55:12">
	
	public TestCase(String name, String classname, BigDecimal duration, Status status) {
		this.name = name;
		this.classname = classname;
		this.duration = duration == null ? BigDecimal.ZERO : duration;
		this.status = status;
		systemOut = new ArrayList<>();
		systemErr = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}
	
	public String getClassname() {
		return classname;
	}
	
	public String getPackagename() {
		return classname.substring(0, classname.lastIndexOf("."));
	}
	
	public BigDecimal getDuration() {
		return duration;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public List<String> getSystemOut() {
		return systemOut;
	}
	
	public List<String> getSystemErr() {
		return systemErr;
	}
}
