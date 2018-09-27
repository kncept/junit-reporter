package com.kncept.junit.reporter;

import java.util.ArrayList;
import java.util.List;

import com.kncept.junit.reporter.xml.TestSuite;

public class TestRunResults {

	private final String category;
	
	private final List<TestSuite> results = new ArrayList<>();
	
	public TestRunResults(String category) {
		this.category = category;
	}
	
	public String category() {
		return category;
	}
	
	public List<TestSuite> results() {
		return results;
	}
	
	public SummaryBucket totals() {
		SummaryBucket totals = new SummaryBucket("totals");
		results.forEach(suite -> suite.testcases().forEach(test -> totals.include(test)));
		return totals;
	}
	
}
