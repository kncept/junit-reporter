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
            classpath 'com.kncept.junit5.reporter:junit-reporter:0.9.1-M3'
        }
    }

Then, apply the plugin:

    apply plugin: 'com.kncept.junit5.reporter'

This will bind the task 'junit5HTMLReport' to run after the 'check' task.
