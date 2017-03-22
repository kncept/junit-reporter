package com.kncept.junit5.reporter;

import java.util.ArrayList;
import java.util.List;

public class TestHTMLReporterExtension {
	
	private List reportDirectories = new ArrayList<>();
	
	public List getReportDirectories() {
		return reportDirectories;
	}
	
	public void setReportDirectories(List reportDirectories) {
		this.reportDirectories = reportDirectories;
	}

}
