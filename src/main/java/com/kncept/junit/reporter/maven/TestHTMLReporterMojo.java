package com.kncept.junit.reporter.maven;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.kncept.junit.reporter.domain.CssRagStatus;
import com.kncept.junit.reporter.exception.TestReporterError;
import com.kncept.junit.reporter.exception.TestReporterFailure;
import com.kncept.junit.reporter.html.TestReportProcessor;

@Mojo(name = "junit-reporter", defaultPhase = LifecyclePhase.VERIFY)
public class TestHTMLReporterMojo extends AbstractMojo {

	@Parameter
	public boolean aggregated = false;
	
	@Parameter
	public boolean failOnEmpty = true;
	
	//input directory
	@Parameter(defaultValue = "${project.basedir}/target/")
	public File testResultsDir;
	
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
			if (System.getProperty("dump-info", "false").equals("true"))
					dumpInfo();
			TestReportProcessor reportProcessor = new TestReportProcessor(
					testResultsDir,
					testReportsDir,
					aggregated,
					failOnEmpty,
					new CssRagStatus(cssRed, cssAmber, cssGreen)
			);
			reportProcessor.run();
		
		} catch (TestReporterError e) {
			throw new MojoExecutionException(e.getMessage(), e);
		} catch (TestReporterFailure e) {
			throw new MojoFailureException(e.getMessage());
		}
	}
	
	private void dumpInfo() {
		getLog().info("aggregated = " + aggregated);
		getLog().info("failOnEmpty = " + failOnEmpty);
		getLog().info("cssRed = " + cssRed);
		getLog().info("cssAmber = " + cssAmber);
		getLog().info("cssGreen = " + cssGreen);
		
		getLog().info("testResultsDir = " + testResultsDir);
		getLog().info("testReportsDir = " + testReportsDir);
		
		getPluginContext().keySet().forEach(key -> {
			String type = getPluginContext().get(key).getClass().getName();
			getLog().info("context: " + key + " of type " + type);
		});

//[INFO] context: project of type org.apache.maven.project.MavenProject
//[INFO] context: pluginDescriptor of type org.apache.maven.plugin.descriptor.PluginDescriptor
	}
}
