# junit-reporter [![Build Status](https://travis-ci.org/kncept/junit-reporter.svg?branch=master)](https://travis-ci.org/kncept/junit-reporter)

## Applying
Add or merge this to the top of your buildscript libraries via the mavenCentral repository:

    buildscript {
        repositories {
            mavenCentral()
        }
        dependencies {
            classpath 'com.kncept.junit5.reporter:junit-reporter:1.0.3'
        }
    }

Then, apply the plugin:

    apply plugin: 'com.kncept.junit5.reporter'

This will bind the task 'junitHtmlReport' to run as a finalizer after the 'test' or 'check' task.
If you want more fine grained control, add it as a finalizer to another task. eg:

    junitPlatformTest.finalizedBy 'junitHtmlReport'
    
And you're done!

## Configuration options

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

## Command Line Usage
Command line support has been built.<br/>
The jar file is executable, and the options have the same names as in the gradle config block.<br/>
The main class name is com.kncept.junit5.reporter.html.TestHTMLReportProcessor.<br/>
Options use a simple equals sign.<br/>
 eg: `java -jar junit-reporter-1.0.3 aggregated=true` to process reports in the current dir

## Final Thoughts

It seems a few people use this. Glad to help. It does take a while to keep things ticking along though.<br />
Donations accepted: <br/>
ETH: 5db8a572ba967f5611740fba29957be46a58cdef <br/>
BTC: 1EnoXwWabBzeSWyRYU775PKqLLKp49Vub1 <br/>
Beer: anytime! <br/>
