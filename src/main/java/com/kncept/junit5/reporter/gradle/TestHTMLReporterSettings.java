package com.kncept.junit5.reporter.gradle;

public class TestHTMLReporterSettings {
	public static final String settingsExtensionName = "junitHtmlReport";
	
	private boolean aggregated;
	
	//RAG status
	
	private String cssRed = "red";
	private String cssAmber = "orange";
	private String cssGreen = "green";
	
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

}
