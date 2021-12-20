package com.packt.testrunner;

import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(snippets = SnippetType.CAMELCASE, 
		features = "resources/Features/Packt/Packt_UI_Functionalities.feature"
		,glue={"com.packt.stepdefinitions"},
		plugin = { "pretty" }
		)


public class TestRunnerPackt{
}
