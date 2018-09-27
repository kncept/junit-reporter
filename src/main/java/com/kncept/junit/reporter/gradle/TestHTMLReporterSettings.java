package com.kncept.junit.reporter.gradle;

public class TestHTMLReporterSettings {
	public static final String settingsExtensionName = "junitHtmlReport";
	
	private boolean aggregated = false;
	
	//RAG status
	private String cssRed = "red";
	private String cssAmber = "orange";
	private String cssGreen = "green";
	
	private String testResultsDir = "test-results";
	private String testReportsDir = "reports/tests";
	
	private boolean failOnEmpty = true;
	
	public void setAggregated(boolean aggregated) {
		this.aggregated = aggregated;
	}
	
	public boolean isAggregated() {
		return aggregated;
	}
	
	public void setCssRed(String cssRed) {
		this.cssRed = cssRed;
	}
	
	public String getCssRed() {
		return cssRed;
	}
	
	public void setCssAmber(String cssAmber) {
		this.cssAmber = cssAmber;
	}
	
	public String getCssAmber() {
		return cssAmber;
	}
	
	public void setCssGreen(String cssGreen) {
		this.cssGreen = cssGreen;
	}
	
	public String getCssGreen() {
		return cssGreen;
	}
	
	public void setTestReportsDir(String testReportsDir) {
		this.testReportsDir = testReportsDir;
	}
	
	public String getTestReportsDir() {
		return testReportsDir;
	}
	
	public void setTestResultsDir(String testResultsDir) {
		this.testResultsDir = testResultsDir;
	}
	
	public String getTestResultsDir() {
		return testResultsDir;
	}
	
	public void setFailOnEmpty(boolean failOnEmpty) {
		this.failOnEmpty = failOnEmpty;
	}
	
	public boolean isFailOnEmpty() {
		return failOnEmpty;
	}

}
