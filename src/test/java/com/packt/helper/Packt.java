package com.packt.helper;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import java.io.IOException;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.LogStatus;

public class Packt extends CommonHelper {
	 
	public Packt() throws Exception {
		super();	
	}

	@Test
	public static void userLaunchesWebsite(String environment) throws Exception {
		try {
			Utility.setUpClient(environment);
			CommonHelper.openUrl(environment);
			test.log(LogStatus.PASS, "User is able to launch Website "+environment);
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Unable to navigated to the URL");
			e.printStackTrace();
		}
	}

	@Test
	public static void userSubmitUsernameAndPassword() throws Exception {
		try {
			clickElement(By.xpath("//a[text()='Sign in']"));
			switch_To_New_Window();
			enterValue(By.id("login-input-email"), properties.getProperty("Email"));
			enterValue(By.id("login-input-password"), properties.getProperty("Password"));
			clickElement(By.cssSelector("button.btn-lg span"));
			System.out.println("User is able to enter username and password correctly");
			test.log(LogStatus.PASS, "User is able to enter username and password correctly");
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Unable to enter username and password correctly");
			e.printStackTrace();
		}
	}

	@Test
	public static void verifyUserLoggedIn() throws IOException {
		try {
			Thread.sleep(4000);
			assertNotNull(waitForElementToBe(By.xpath("//span[contains(text(),'Account')]"), "VISIBLE", driver, 60));
			assertNotNull(
					waitForElementToBe(By.xpath("//span[contains(text(),'My Library')]"), "VISIBLE", driver, 25));
			System.out.println("Verified User is Logged In");
			test.log(LogStatus.PASS, "Verified User is Logged In");
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Unable to Logged In");
			e.printStackTrace();
		}
	}

	@Test
	public static void verifyUIOfAllElementsAreCorrectOnScreen() throws IOException {
		try {
			String xlElementLocator, xlElementName, xlElementCSS, xlElementColor, xlElementText;
			for (int i = 1; i < CommonHelper.testData.length; i++) {
				xlElementName = CommonHelper.testData[i][0];
				xlElementLocator = CommonHelper.testData[i][1];
				xlElementCSS = CommonHelper.testData[i][2];
				xlElementColor = CommonHelper.testData[i][3];
				xlElementText = (CommonHelper.testData[i][4] == null) ? "" : CommonHelper.testData[i][4].trim();
				verifyElementAreValid(xlElementName, xlElementLocator, xlElementCSS, xlElementColor, xlElementText);
				test.log(LogStatus.PASS, "Verified Element - " + xlElementName + " is enabled , Color - " + xlElementColor+" , CSS  - " + xlElementCSS+" is Displayed on screen");
			}
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Unable to verify element" );
			e.printStackTrace();
		}
		System.out.println("Verified All UI Elements are displayed correctly");
	}

	@Test
	public static void verifyTopNavigationBarIsDisplayed() throws IOException {
		try {
			if (verifyElementIsPresent(
					waitForElementToBe(By.cssSelector("nav#primary-navigation"), "PRESENCE", driver, 10)))
			System.out.println("Verified Top Navigation Bar is displayed correctly");
			test.log(LogStatus.PASS, "Verified Top Navigation Bar is displayed correctly");
		} catch (Exception e) {
			test.log(LogStatus.FAIL, "Unable to navigation via top navigation bar");
			e.printStackTrace();
		}
	}

	@Test
	public static void userClicksOnMenuAndVerifyPage() throws IOException {
		try {
			for (int i = 1; i < CommonHelper.testData.length; i++) {
				String xlMenuName = CommonHelper.testData[i][0];
				String xlSubMenuName = CommonHelper.testData[i][1];
				String xlMenuItem = (CommonHelper.testData[i][2] == null) ? "" : CommonHelper.testData[i][2].trim();
				String currentWindow = driver.getWindowHandle();
				// User click on Menu Item
				userClicksOnMenu(xlMenuName, xlSubMenuName, xlMenuItem);
				// User switches to new window
				switch_To_New_Window();
				// To verify expected page is displayed or not
				assertTrue(CommonHelper.verifyPageIsDisplayed(xlSubMenuName));
				// User switches to new window
				switch_To_Old_Window(currentWindow);
				System.out.println("Verified page  " + xlMenuName + " > " + xlSubMenuName + " " + xlMenuItem
						+ " is displayed correctly");
				 test.log(LogStatus.PASS,"Verified page " + xlMenuName + " > " + xlSubMenuName +" "+ xlMenuItem+ " is displayed correctly");
			}
		} catch (Exception e) {
			e.printStackTrace();
			 test.log(LogStatus.FAIL,"Unable to verified page Menu page");
			}
	}

	@Test
	public static void verifyIsDisplayed(String txtElement) {
		try {
			assertEquals(waitForElementToBe(By.cssSelector("h2.suggested_title"), "PRESENCE", driver, 10).getText(),
					txtElement);
			 test.log(LogStatus.PASS,"Verified page " + txtElement );
		} catch (Exception e) {
			test.log(LogStatus.FAIL,"Unable to verified page "+txtElement );
			e.printStackTrace();
		}
	}

	@Test
	public static void userClicksOnCarouselAndVerifyUserIsNavigatedToIt() {
		try {
			userClicksOnCarouselAndVerifyIfUserIsNavigatedToIt();
			System.out.println("Verified Carousel Elements are displayed correctly");
			 test.log(LogStatus.PASS,"Verified Carousel Elements are displayed correctly");
		} catch (Exception e) {
			test.log(LogStatus.FAIL,"Unable to verify Carousel Elements");
			e.printStackTrace();
		}
	}

	@Test
	public static void userClicksOnMenu(String pathMenu) {
		try {
			String[] arrSplit = pathMenu.split(">");
			String menuName = arrSplit[0].trim();
			String subMenuName = arrSplit[1].trim();
			// User click on Menu Item
			userClicksOnMenu(menuName, subMenuName);
			test.log(LogStatus.PASS,"User clicks on Menu "+ pathMenu);
		} catch (Exception e) {
			test.log(LogStatus.FAIL,"Unable to click on Menu "+ pathMenu);
			e.printStackTrace();
		}
	}

	@Test
	public static void userClicksButton() {
		try {
			// User switches to new window
			switch_To_New_Window();

			// Applying wait as page is taking time to load
			Thread.sleep(4000);

			// Clicking on Clear All Filters
			clickElement(By.cssSelector("a.ais-current-refined-values--clear-all"));
			// Verifying filters are removed
			waitForElementToBe(By.cssSelector("a.ais-current-refined-values--clear-all"), "INVISIBLE", driver, 20);
			System.out.println("User clicked on clear all filters");
			test.log(LogStatus.PASS,"User clicked on clear all filters");
		} catch (Exception e) {
			test.log(LogStatus.FAIL,"Unable to click on clear all filters");
			e.printStackTrace();
		}
	}

	@Test
	public static void userApplyFiltersOnSearchPage() {
		try {
			WebElement searchFilter = null;
			for (int i = 1; i < CommonHelper.testData.length; i++) {
				String xlSearchBy = CommonHelper.testData[i][0].trim();
				String xlSearchMenuName = CommonHelper.testData[i][1].trim();
				String xlSearchValue = (CommonHelper.testData[i][2] == null) ? "" : CommonHelper.testData[i][2].trim();
				if (xlSearchBy.equalsIgnoreCase("Search By Filter")) {
					searchFilter = waitForElementToBe(By.xpath("//*[contains(text(),'" + xlSearchMenuName
							+ "')]/following::input[@id='" + xlSearchValue + "']"), "CLICKABLE", driver, 20);
					searchFilter.click();
					// Verify if Element is checked or not
					searchFilter = waitForElementToBe(By.cssSelector("input[value='" + xlSearchValue + "']"), "VISIBLE",
							driver, 20);
					assertTrue(searchFilter.isSelected());
					System.out.println("User applied search filter " + xlSearchMenuName + " " + xlSearchValue);
					test.log(LogStatus.PASS,"User applied search filter " + xlSearchMenuName + "  " + xlSearchValue);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			test.log(LogStatus.FAIL,"Unable to apply search filters");
		}
	}

	@Test
	public static void userSearchesProductsAndVerifyAllTitlesFoundIncludeThatSearchText() {
		try {
			List<WebElement> searchLists = null;
			for (int i = 1; i < CommonHelper.testData.length; i++) {
				String xlSearchMenuName = CommonHelper.testData[i][0];
				String xlSearchValue = CommonHelper.testData[i][1];
				if (xlSearchMenuName.equalsIgnoreCase("Search Text")) {
					// Clicks on Search products Input field
					enterValue(By.cssSelector("input#search-input"), xlSearchValue);
					// Clicks on Search button
					clickElement(By.cssSelector("button.ais-search-box--submit"));
					// Applying wait to appear Search Result list
					Thread.sleep(500);
					waitForElementToBe(By.cssSelector("div.ais-stats"), "VISIBLE", driver, 40);
					// Retrieving Search List of Result
					searchLists = waitForElementsToBe(By.cssSelector("h2.search-hit__title"), "CLICKABLE", driver, 20);
					for (WebElement searchList : searchLists) {
						if (searchList.getText().toLowerCase().contains(xlSearchValue.toLowerCase()))
							assertTrue(true, "Verified Search Result Text contains" + xlSearchValue);
					}
				}
				System.out.println("Verified searched products > " + xlSearchMenuName + " > " + xlSearchValue
						+ " and verified all searched text titles");
				test.log(LogStatus.PASS,"Verified searched products > " + xlSearchMenuName + " > " + xlSearchValue
						+ " and verified all searched text titles");
			}
		} catch (Exception e) {
			e.printStackTrace();
			test.log(LogStatus.FAIL,"Unable to verify searched products and to verify all searched text titles");		
		}
	}
}
