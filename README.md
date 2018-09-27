# junit-reporter [![Build Status](https://travis-ci.org/kncept/junit-reporter.svg?branch=master)](https://travis-ci.org/kncept/junit-reporter)

# SNAPSHOT version (including documentation).
Browse repo at release [1.2](https://github.com/kncept/junit-reporter/tree/d2fd607393bedd309c35738ef0de54891f2db5f2) for accurate docs.

# Gradle

### Applying
Add or merge this to the top of your buildscript libraries via the mavenCentral repository:

    buildscript {
        repositories {
            mavenCentral()
        }
        dependencies {
            classpath 'com.kncept.junit5.reporter:junit-reporter:2.0.0'
        }
    }

Then, apply the plugin:

    apply plugin: 'com.kncept.junit.reporter'

This will bind the task 'junitHtmlReport' to run as a finalizer after the 'test' or 'check' task.<br/>
If you want more fine grained control, add it as a finalizer to another task. eg:

    junitPlatformTest.finalizedBy 'junitHtmlReport'
    
And you're done!<br/>
Reports are generated into the gradle 'reports' directory. <br/>
eg: junit-reporter/build/reports/tests/junit-platform/index.html<br/>
<br/>
Please be aware that the plugin may take a few hours to become available after release.<br/>
<br/>

### Configuration Options
If you need to do any customisation (aggregation, or you just don't like red...), Its possible to customise.
Use the following config block (shown with default values):

	junitHtmlReport {
		// If true, then instead of producing multiple reports per test folder (test run), 
		// aggregate them all together into the test-reports root directory.
		//
		// Also use this if all your test results end up directly in the test-results directory
		aggregated = false
		
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

# Maven

### Applying

In the project/build/plugins element, add the plugin.

	<plugin>
		<groupId>com.kncept.junit.reporter</groupId>
		<artifactId>junit-reporter</artifactId>
		<version>2.0.0</version>
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
		<aggregated>false</aggregated>
		
		<cssRed>red</cssRed>
		<cssAmber>orange</cssAmber>
		<cssGreen>green</cssGreen>
		
		<failOnEmpty>true</tailOnEmpty>
	</configuration>
  


# Command Line Usage
Command line support has been built.<br/>
The jar file is executable, and the options have the same names as in build configuration blocks.<br/>
The main class name is com.kncept.junit.reporter.html.TestReportProcessor.<br/>
Options use a simple equals sign.<br/>
 eg: `java -jar junit-reporter-2.0.0 aggregated=true` to process reports in the current dir

# Final Thoughts

It seems a few people use this. Glad to help. It does take some effort to keep things ticking along though.<br/>
<br/>
Donations accepted: <br/>
ETH: 5db8a572ba967f5611740fba29957be46a58cdef <br/>
BTC: 1EnoXwWabBzeSWyRYU775PKqLLKp49Vub1 <br/>
Beer: anytime! <br/>
