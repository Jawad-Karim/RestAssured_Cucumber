package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

//@RunWith(Cucumber.class)
@CucumberOptions(
		features = "src/test/resources/featureFiles",
		glue = {"stepDefinitions"},
		dryRun = false,
		monochrome = true
		)

public class TestRunner_TestNG extends AbstractTestNGCucumberTests{

}
