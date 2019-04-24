package com.bdd.tests.integration;

import com.bdd.framework.IntegrationCucumber;
import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(IntegrationCucumber.class)
@CucumberOptions(features = "features",
        glue = {"com.bdd.tests.integration", "com.bdd.tests.tests"},
        plugin = {
        "pretty",
        "html:target/reports",
        "json:target/reports/cucumber.json"
})
public class EmployeesIT {
}
