# junit-reporter [![Build Status](https://travis-ci.org/kncept/junit-reporter.svg?branch=master)](https://travis-ci.org/kncept/junit-reporter)

Junit 5 currently has very limited build integration.
This plugin is a way to remedy that.
It produces a simple html5/javascript report.


Gradle Instructions:
Add or merge this to the top of your buildscript libraries via the mavenCentral repository:

    buildscript {
        repositories {
            mavenCentral()
        }
        dependencies {
            classpath 'com.kncept.junit5.reporter:junit-reporter:1.0.0
        }
    }

Then, apply the plugin:

    apply plugin: 'com.kncept.junit5.reporter'

This will bind the task 'junitHtmlReport' to run after the 'check' task.

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
	}
