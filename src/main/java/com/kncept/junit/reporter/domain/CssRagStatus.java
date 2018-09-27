package com.kncept.junit.reporter.domain;

public class CssRagStatus {

	public String red = "red";
	public String amber = "orange";
	public String green = "green";
	
	public CssRagStatus() {}
	
	public CssRagStatus(String red, String amber, String green) {
		this.red = red;
		this.amber = amber;
		this.green = green;
	}
	
	public String getRed() {
		return red;
	}
	
	public void setRed(String red) {
		this.red = red;
	}
	
	public String getAmber() {
		return amber;
	}
	
	public void setAmber(String amber) {
		this.amber = amber;
	}
	
	public String getGreen() {
		return green;
	}
	
	public void setGreen(String green) {
		this.green = green;
	}
	
}
