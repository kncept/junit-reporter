package com.kncept.junit5.reporter.xml;

import java.util.LinkedHashMap;
import java.util.List;

import com.kncept.junit5.reporter.domain.TestCase;

public interface XMLTestResults {

	public LinkedHashMap<String, String> systemProperties();
	
	public LinkedHashMap<String, String> testsuiteProperties();
	
	public List<TestCase> testcases();
	
}
