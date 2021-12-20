package com.packt.helper;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.Color;
import org.testng.asserts.SoftAssert;
import io.github.bonigarcia.wdm.WebDriverManager;

public class CommonHelper extends Utility {
	static WebDriver driver;
	public static final String TESTDATA_FOLDER = "src/main/resources/TestDataFiles/";
	public static final String CONFIGS_FOLDER = "src/main/resources/Configs/";
	public static Properties properties;
	public static String[][] testData = null;
	static SoftAssert softassert = new SoftAssert();

	public CommonHelper() throws Exception {
		super();
		properties = Utility.loadProperties();
		
	}

	public static String capture(WebDriver driver) throws IOException {
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		File Dest = new File("src/../BStackImages/" + System.currentTimeMillis() + ".png");
		String errflpath = Dest.getAbsolutePath();
		FileUtils.copyFile(scrFile, Dest);
		return errflpath;
	}

	//To set Chromedriver
	public static void setChromeDriver() throws Exception {
		ChromeOptions options = new ChromeOptions();
		//options.addArguments("start-fullscreen");
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		setDriver(driver);
		setExtendReport();
	}

	//To read values from Excel File Name and Sheet Name and stores in testData variable
	public void readExcel(String sheetName, String fileName) throws IOException {
		String tdFileName = properties.getProperty(fileName);
		// Create an object of File class to open xlsx file
		File file = new File(TESTDATA_FOLDER + tdFileName);
		// Create an object of FileInputStream class to read excel file
		FileInputStream inputStream = new FileInputStream(file);
		Workbook wb = new HSSFWorkbook(inputStream);

		// Read sheet inside the workbook by its name
		Sheet sheet = wb.getSheet(sheetName);

		// To get the last row of the sheet
		int firstRow = sheet.getFirstRowNum();
		int lstRow = sheet.getLastRowNum();
		DataFormatter formatter = new DataFormatter();
		int num = 0;
		for (num = 0; num <= lstRow; num++) {
			String val = formatter.formatCellValue(sheet.getRow(num).getCell(0));
			if (val.trim().length() == 0)
				break;
		}
		lstRow = num;

		// Find number of rows in excel file and setting up the array size
		int rowCount = lstRow - firstRow;
		testData = new String[lstRow][sheet.getRow(sheet.getFirstRowNum()).getLastCellNum()];

		// Create a loop over all the rows of excel file to read it
		for (int i = 0; i < rowCount; i++) {
			Row row = sheet.getRow(i);
			// Create a loop to print cell values in a row
			for (int j = 0; j < row.getLastCellNum(); j++) {
				// Store data in array
				if (formatter.formatCellValue(row.getCell(j)).length() != 0)
					testData[i][j] = formatter.formatCellValue(row.getCell(j)).trim();
			}
		}
	}


	//To kill chrome driver process running background
	public static void killProcess() throws IOException {
		Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
	}

	public static void openUrl(String environment) throws Exception {
		try {
			String EnviornmentValue = properties.getProperty(environment);
			driver.navigate().to(EnviornmentValue);
			driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		} catch (Exception e) {
			Log.error("Unable to Open Browser" + e);
			throw (e);
		}

	}

	//Clicks on Element by locator
	public static void clickElement(By locator) {
		WebElement e = Utility.waitForElementToBe(locator, "CLICKABLE", driver, 10);
		assertNotNull(e);
		e.click();
	}

	//Enters value in WebElement
	public static void enterValue(By locator, String valueToBeEntered) throws InterruptedException {
		WebElement enterValue = Utility.waitForElementToBe(locator, "PRESENCE", driver, 10);
		assertNotNull(enterValue);
		enterValue.clear();
		enterValue.sendKeys(valueToBeEntered);
	}

	//Closes browser
	public static void closeBrowser() {
		try {
			WebDriver driver = getDriver();
			driver.close();
			driver.quit();
		} catch (NoSuchSessionException e) {
			Utility.printInLogFile(Log, "Driver session / Browser is already closed by other process");
		}
	}

	//Verify Elements are displayed,enabled and color are valid or not
	public static void verifyElementAreValid(String elementName, String elementLocator, String elementCSS,
			String elementColor, String eElementText) {
		WebElement element = Utility.waitForElementToBe(Utility.getLocatorType(elementLocator), "PRESENCE", driver, 40);
		assertTrue(element.isDisplayed());
		assertTrue(element.isEnabled());
		softassert.assertEquals(element.getCssValue("display").toLowerCase(), elementCSS.toLowerCase());
		String actualColor = Color.fromString(element.getCssValue("color")).asHex();
		softassert.assertEquals(actualColor, elementColor);
		if (eElementText.length() != 0) {
			softassert.assertEquals(element.getText(), eElementText);
		}
		softassert.assertAll();
		System.out.println("Verified Element - " + elementName + " : Color - " + elementColor+" is Displayed on screen");
	}

	//To switch to new windows and to wait till page loads
	public static void switch_To_New_Window() {
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
			driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		}
	}

	//Verify if element is present or not
	public static boolean verifyElementIsPresent(WebElement element) {
		try {
			Thread.sleep(2000);
			assertTrue(element.isDisplayed());
			assertTrue(element.isEnabled());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	//Clicking on Menu from Navigation Bar 
	//3 Input Parameters : MenuItem > SubMenuItem > MenuName 
	//for eg : Browse > Web Development > Flask
	public static void userClicksOnMenu(String menuItem, String xlSubMenuName, String xlMenuItem) {
		try {
			// User clicks on Menu Item - Browse
			if (clickOnMenu("li.menu__item", menuItem)) {
				// User clicks on Sub Menu Item - Web Development
				if (clickOnMenu("div.row", xlSubMenuName)) {
					// User clicks on Sub Menu Item - Flask
					if (!xlMenuItem.isEmpty()) {
						clickOnMenu("span.category-header-items", xlMenuItem);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//Clicking on Menu from Navigation Bar 
	//2 Input Parameters : MenuItem > SubMenuItem 
	//for eg : Browse > View All Books
	public static void userClicksOnMenu(String menuItem, String xlSubMenuName) {
		try {
			// User clicks on Menu Item
			if (clickOnMenu("li.menu__item", menuItem)) {
				// User clicks on Sub Menu Item
				clickOnMenu("div.row", xlSubMenuName);		
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Click on Menu item and returns true
	//Input Parameters : Locator address of element and Text name of menu
	//Output Parameters: Boolean value
	public static boolean clickOnMenu(String locator, String menuText) {
		try {
			List<WebElement> eleMenuItems = Utility.waitForElementsToBe(By.cssSelector(locator), "PRESENCE", driver,
					20);
			for (WebElement eleMenuItem : eleMenuItems) {
				if (eleMenuItem.getText().trim().equalsIgnoreCase(menuText)) {
					eleMenuItem.click();
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	//Verify if Expected Page is displayed or not
	//Input parameters : Excepted URL
	public static boolean verifyPageIsDisplayed(String expectedURL) {
		try {
			Thread.sleep(1000);
			String currentURL = driver.getCurrentUrl();
			String alteredURL = currentURL.replaceAll("\\d", "").replaceAll("%", "");					//Removes % and numbers 
			expectedURL = expectedURL.replaceAll("\\s", "").replaceAll("&", "");						//Removes & and spaces 
			if ((alteredURL.toLowerCase()).contains(expectedURL.toLowerCase())) {						//Comparing actual and expected URL's
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	//Closes current windows and switches to old window
	public static void switch_To_Old_Window(String currentWindow) {
		driver.close();
		driver.switchTo().window(currentWindow);
	}

	//Verifies Carosuel elements by clicking on it and also verifies title of it 
	public static void userClicksOnCarouselAndVerifyIfUserIsNavigatedToIt() {
		try {
			List<WebElement> carosuelElements = driver.findElements(By.cssSelector("div.productCarousel"));
			int i;
			String expectedTitle;
			for (i = 0; i < carosuelElements.size(); i++) {
				expectedTitle = CommonHelper.testData[i + 1][1];
				if (carosuelElements.get(i) != null) {
					try {
						if (!(carosuelElements.get(i).isDisplayed()))
							ScrollToElementVisible(driver, carosuelElements.get(i));   						//If Element is not displayed then it will scroll to Element
						// Clicks on Carosuel Element
						carosuelElements.get(i).click();
						// Verify Carosuel Title text
						verifyTextOnCarosuelElement(carosuelElements.get(i), expectedTitle);
					} catch (ElementNotInteractableException e) {
						ScrollToElementVisible(driver, carosuelElements.get(i));							// Scrolling to Element if not interactable and clicking on its
						carosuelElements.get(i).click();
						verifyTextOnCarosuelElement(carosuelElements.get(i), expectedTitle);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Verifying main title is appearing correctly
	public static void verifyTextOnCarosuelElement(WebElement carosuelElement, String expectedText) {
		if (carosuelElement.getAttribute("class").contains("book-active")) {													//If carosuel element is clicked,it will return true
			WebElement actualTitle = waitForElementToBe(By.cssSelector("div.productTitle.main-title"), "PRESENCE",
					driver, 20);
			if (!actualTitle.getText().isEmpty()) {
				assertEquals(actualTitle.getText(), expectedText);
				System.out.println(
						"Verified Carousel Title " + actualTitle.getText() + " Expected Title : " + expectedText);
			} else {
				ScrollToElementVisible(driver, actualTitle);
				assertEquals(actualTitle.getText(), expectedText);
				System.out.println(
						"Verified Carousel Title " + actualTitle.getText() + " Expected Title : " + expectedText);
			}
		}
	}
}