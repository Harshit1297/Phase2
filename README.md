# Static code analyzer

A reworked version of the static-code-analyzer with web API 

## Project Overview
This web application is an integration of the various static code analysis tools which generates a unified report for a given file/file directory. Using API calls we can obtain the various issues present in the code based on the tool used. A GO or NO-GO status is provided when all the issues in the code are cleared or not.

## Standards 
The code coverage for the project is 85% 

## Tools Used
Checkstyle, PMD, Similarity Analyzer

## Steps to install Checkstyle 
Download the checkstyle jar from the link provided below:
https://github.com/checkstyle/checkstyle/releases/download/checkstyle-8.24/checkstyle-8.24-all.jar
and update the path of the checkstyle jar in applications.properties file

## Steps to install PMD 
Download PMD from the link below:
https://github.com/pmd/pmd/releases/download/pmd_releases%2F6.17.0/pmd-bin-6.17.0.zip
after installation add the pmd.bat in the path variable
(for eg: C:\pmd-bin-6.16.0\pmd-bin-6.16.0\bin)

## Steps to install Simian
Download the Simian tool jar from the below link:
https://www.harukizaemon.com/simian/simian-2.5.10.tar.gz
after install, update the path of the simian jar in applications.properties file

## Steps to run the web application
Once the paths are set into the properties file, run the `AnalyzerWebApplication.java` as java application.
With the spring web app running, use the following api calls to run the static code tools and obtain reports.

```http://localhost:8080/api/<tool>?path=<filepath>```

All API calls are get requests and the `filepath` should be encoded and appended after the `?path=`. The `tool` can be substituted with ```pmd, cs, sim, all``` to use PMD analyzer, Checkstyle analyzer, Simian analyzer and the integrated analyzer respectively

Link to the API documentation can be accessed after launching webapp using the link: 

```http://localhost:8080/swagger-ui.html#/```


