package com.example;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import java.io.File;
import java.util.Collections;

public class GenerateReport {
    public static void main(String[] args) {
        // Path to the JSON file
        String jsonFilePath = "target/cucumber-reports/Cucumber.json";
        
        // Directory where the HTML report will be generated
        File reportOutputDirectory = new File("target/cucumber-reports");

        // List of JSON files to be processed
        java.util.List<String> jsonFiles = Collections.singletonList(jsonFilePath);

        // Configuration for the report
        Configuration configuration = new Configuration(reportOutputDirectory, "My Cucumber Project");
        configuration.setBuildNumber("1");
        // configuration.addClassifications("Platform", "Windows");
        // configuration.addClassifications("Browser", "Chrome");
        // configuration.addClassifications("Branch", "release/1.0");

        // Generate the report
        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
        reportBuilder.generateReports();
    }
}
