# junit-reporter [![Build Status](https://travis-ci.org/kncept/junit-reporter.svg?branch=master)](https://travis-ci.org/kncept/junit-reporter)

Junit 5 currently has very limited build integration.
This plugin is a way to remedy that.
It produces a simple html5/javascript report.


Gradle Instructions:
Add this to your buildscript libraries via the mavenCentral repository:

    N.B. This is AS YET UNRELEASED...
    
    buildscript {
        repositories {
            mavenCentral()
        }
    }

Then, apply the plugin:

    apply plugin: 'com.kncept.junit5.reporter'

