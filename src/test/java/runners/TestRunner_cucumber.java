package runners;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "src/test/resources/featureFiles",
		glue = {"stepDefinitions"},
		monochrome = true
		)
public class TestRunner_cucumber {

}
