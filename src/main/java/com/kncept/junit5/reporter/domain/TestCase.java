package com.kncept.junit5.reporter.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestCase {

	private final String name; //test name;
	private final String classname;
	private final BigDecimal duration; //time
	private final TestCaseStatus status;
	private final List<String> systemOut;
	private final List<String> systemErr;
	
	private String unsuccessfulMessage;
	private String stackTrace;
	
	public TestCase(String name, String classname, BigDecimal duration, TestCaseStatus status) {
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
		if (classname.contains("."))
			return classname.substring(0, classname.lastIndexOf("."));
		return ""; //no package!!
	}
	
	public BigDecimal getDuration() {
		return duration;
	}
	
	public TestCaseStatus getStatus() {
		return status;
	}
	
	public List<String> getSystemOut() {
		return systemOut;
	}
	
	public List<String> getSystemErr() {
		return systemErr;
	}
	
	public void setUnsuccessfulMessage(String unsuccessfulMessage) {
		this.unsuccessfulMessage = unsuccessfulMessage;
	}
	
	public String getUnsuccessfulMessage() {
		return unsuccessfulMessage;
	}
	
	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}
	
	public String getStackTrace() {
		return stackTrace;
	}			
}
