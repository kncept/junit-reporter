package com.kncept.junit.reporter.maven;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.kncept.junit.reporter.TestReportProcessor;
import com.kncept.junit.reporter.TestRunResults;
import com.kncept.junit.reporter.domain.CssRagStatus;
import com.kncept.junit.reporter.exception.TestReporterError;
import com.kncept.junit.reporter.exception.TestReporterFailure;

@Mojo(name = "junit-reporter", defaultPhase = LifecyclePhase.VERIFY)
public class TestHTMLReporterMojo extends AbstractMojo {

	@Parameter
	public boolean failOnEmpty = true;
	
	
	//input directory
	@Parameter(defaultValue = "${project.basedir}/target/")
	public File testResultsDir;
	
	@Parameter
	public int maxDepth = 3;
	
	
	//output directory
	@Parameter(defaultValue = "${project.basedir}/target/junit-reporter")
	public File testReportsDir;
	
	@Parameter
	public String cssRed = "red";
	
	@Parameter
	public String cssAmber = "orange";
	
	@Parameter
	public String cssGreen = "green";
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			TestReportProcessor processor = new TestReportProcessor();
			
			List<TestRunResults> results = processor.scan(testResultsDir, maxDepth);
			if (failOnEmpty && results.isEmpty())
				throw new MojoFailureException("No XML Test reports to process");
			
			processor.write(testReportsDir, new CssRagStatus(cssRed, cssAmber, cssGreen), results);
		
		} catch (TestReporterError e) {
			throw new MojoExecutionException(e.getMessage(), e);
		} catch (TestReporterFailure e) {
			throw new MojoFailureException(e.getMessage());
		}
	}
}
