# junit-reporter [![Build Status](https://travis-ci.org/kncept/junit-reporter.svg?branch=master)](https://travis-ci.org/kncept/junit-reporter)

A plugin to convert XML test results to HTML and perform some aggregation.<br/>
No config required by default.<br/>
Also useful if you need the XML files and still want a human-readable output.

# Gradle

### Applying the easy way
This plugin is now released as a community plugin

    plugins {
        id 'com.kncept.junit.reporter' version '2.0.1'
    }

### Applying the long way from maven central
Add or merge this to the top of your buildscript libraries via the mavenCentral repository:

    buildscript {
        repositories {
            mavenCentral()
        }
        dependencies {
            classpath 'com.kncept.junit.reporter:junit-reporter:2.0.1'
        }
    }

Then, apply the plugin:

    apply plugin: 'com.kncept.junit.reporter'
    
### Plugin Execution

The task 'junitHtmlReport' will run as a finalizer after the 'test' or 'check' tasks.<br/>
If you want more fine grained control, add it as a finalizer to another task. eg:

    junitPlatformTest.finalizedBy 'junitHtmlReport'
    
And you're done!<br/>
Reports are generated into the gradle 'reports' directory. <br/>
eg: junit-reporter/build/reports/tests/junit-platform/index.html<br/>
<br/>
Please be aware that the plugin may take a few hours to become available after release.<br/>
<br/>

### Configuration Options
If you need to do any customisation (changing output directories, or you just don't like red...), Its possible to customise.
Use the following config block (shown with default values):

	junitHtmlReport {
		// The maximum depth to traverse from the results dir.
		// Any eligible reports will be included
		maxDepth = 3
		
		//RAG status css overrides
		cssRed = 'red'
		cssAmber = 'orange'
		cssGreen = 'green'
		
		//Processing directories
		testResultsDir = 'test-results'
		testReportsDir = 'reports/tests'
		
		//Fail build when no XML files to process
		failOnEmpty = true
	}

# Maven (Experimental support only)

Maven doesn't like running plugins after test failures.
The plugin can be run directly (assuming the configuration below) with the following command:

    mvn com.kncept.junit.reporter:junit-reporter:2.0.1:junit-reporter

### Applying

In the project/build/plugins element, add the plugin. Suggested execution binding is for the 'verify' phase.

	<plugin>
		<groupId>com.kncept.junit.reporter</groupId>
		<artifactId>junit-reporter</artifactId>
		<version>2.0.1</version>
		<executions>
			<execution>
				<id>junit-reporter</id>
				<phase>verify</phase>
				<goals>
					<goal>junit-reporter</goal>
				</goals>
			</execution>
		</executions>
	</plugin>
	
	
### Configuration Options

Add the configuration element to the junit-reporter plugin element.<br/>
Defaults are shown below, just delete what you don't need.

	<configuration>
		<maxDepth>3</maxDepth>
		
		<cssRed>red</cssRed>
		<cssAmber>orange</cssAmber>
		<cssGreen>green</cssGreen>
		
		<failOnEmpty>true</tailOnEmpty>
	</configuration>
  


# Command Line Usage
Command line support has been built.<br/>
The jar file is executable, and the options have the same names as in build configuration blocks.<br/>
The main class name is com.kncept.junit.reporter.TestReportProcessor.<br/>
Options use a simple equals sign.<br/>
 eg: `java -jar junit-reporter-2.0.1.jar failOnEmpty=false` to process reports in the current dir

