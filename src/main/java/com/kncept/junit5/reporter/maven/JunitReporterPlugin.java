package com.kncept.junit5.reporter.maven;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;



@Mojo(name="junit5HTMLReport")
public class JunitReporterPlugin extends AbstractMojo {

    @Parameter( defaultValue = "${project.build.outputDirectory}", readonly = true, required = true )
    private File reportDirectory;
	
    @Parameter( defaultValue = "${project.build.directory}", readonly = true, required = true )
    private File directory;
    
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("junit5HTMLReport in execute");
		
	}

}
