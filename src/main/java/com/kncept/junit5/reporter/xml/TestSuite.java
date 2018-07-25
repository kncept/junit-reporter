package com.kncept.junit5.reporter.xml;

import java.util.LinkedHashMap;
import java.util.List;

import com.kncept.junit5.reporter.domain.TestCase;

public interface TestSuite {

	public String name();
	
	public LinkedHashMap<String, String> systemProperties();
	
	public LinkedHashMap<String, String> testsuiteProperties();
	
	public List<TestCase> testcases();
	
	public List<String> sysOut();
	
	public List<String> sysErr();
	
}
