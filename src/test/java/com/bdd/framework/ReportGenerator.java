package com.bdd.framework;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReportGenerator {
    @Ignore
    @Test
    public void generateReport() throws IOException {
        File reportOutputDirectory = new File("target/reports");
        List<String> jsonFiles = new ArrayList<>();
        jsonFiles.add("target/reports/cucumber.json");

        String buildNumber = "1.0";
        String projectName = "Members Rest API";
        Configuration configuration = new Configuration(reportOutputDirectory, projectName);
        configuration.setBuildNumber(buildNumber);


        ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
        reportBuilder.generateReports();
    }
}
