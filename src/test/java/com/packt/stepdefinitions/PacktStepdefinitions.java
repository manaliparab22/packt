package com.packt.stepdefinitions;
import com.packt.helper.CommonHelper;
import com.packt.helper.Packt;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PacktStepdefinitions extends CommonHelper {
	CommonHelper commonHelpher;

	public PacktStepdefinitions() throws Exception {
		super();
		commonHelpher = new CommonHelper();
	}

	@Given("^user launches \"([^\"]*)\" website$")
	public void userLaunchesWebsite(String environment) throws Exception {
		try {
			Packt.userLaunchesWebsite(environment);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("^user submit username and password$")
	public void userSubmitUsernameAndPassword() {
		try {
			Packt.userSubmitUsernameAndPassword();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("^user should be logged in$")
	public void userShouldBeLoggedIn() {
		try {
			Packt.verifyUserLoggedIn();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("^user read the test data sheet \"([^\"]*)\" from file \"([^\"]*)\"$")
	public void userReadTheTestDataSheetFromFile(String packtSheet, String packtTestdata) throws Throwable {
		try {
			commonHelpher.readExcel(packtSheet, packtTestdata);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("^verify UI of all elements are correct on screen$")
	public void verifyUIOfAllElementsAreCorrectOnScreen() throws Throwable {
		try {
			Packt.verifyUIOfAllElementsAreCorrectOnScreen();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("^verify Top navigation bar is displayed$")
	public void verifyTopNavigationBarIsDisplayed() throws Throwable {
		try {
			Packt.verifyTopNavigationBarIsDisplayed();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("^user clicks on menu and verify user is navigated to it$")
	public void userClicksOnMenuAndVerifyUserIsNavigatedToIt() throws Throwable {
		try {
			Packt.userClicksOnMenuAndVerifyPage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Given("^verify \"([^\"]*)\" is displayed$")
	public void verifyIsDisplayed(String txtElement) throws Throwable {
		try {
			Packt.verifyIsDisplayed(txtElement);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("^user clicks on each carousel and verify user is navigated to it$")
	public void userClicksOnEachCarouselAndVerifyUserIsNavigatedToIt() {
		try {
			Packt.userClicksOnCarouselAndVerifyUserIsNavigatedToIt();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("^user clicks on \"([^\"]*)\" menu$")
	public void userClicksOnMenu(String menuName) throws Throwable {
		try {
			Packt.userClicksOnMenu(menuName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("^user clicks on Clear all$")
	public void userClicksOnClearAll() throws Throwable {
		try {
			Packt.userClicksButton();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("^user clicks on \"([^\"]*)\"$")
	public void userClicksOn() throws Throwable {
		try {
			Packt.userClicksButton();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("^user apply filters on Search Page$")
	public void userApplyFiltersOnSearchPage() throws Throwable {
		try {
			Packt.userApplyFiltersOnSearchPage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("^user searches products and verify all titles found include that search text$")
	public void userSearchesProductsAndVerifyAllTitlesFoundIncludeThatSearchText() throws Throwable {
		try {
			Packt.userSearchesProductsAndVerifyAllTitlesFoundIncludeThatSearchText();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("^User closes the browser$")
	public void userClosesTheBrowser() throws Throwable {
		try {
			CommonHelper.closeBrowser();
			CommonHelper.endExtendReport();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
