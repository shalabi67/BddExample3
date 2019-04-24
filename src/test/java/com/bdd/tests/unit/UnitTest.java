package com.bdd.tests.unit;


import com.bdd.framework.UnitCucumber;
import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(UnitCucumber.class)
@CucumberOptions(features = "features",
        glue = "com.bdd.tests.tests",
        plugin = {
        "pretty",
        "html:target/reports",
        "json:target/reports/cucumber.json"
})
public class UnitTest {
}
