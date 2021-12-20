package com.packt.helper;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class Utility {

	public static final String TESTDATA_FOLDER = "src/main/resources/TestDataFiles/";
	public static final String CONFIGS_FOLDER = "src/main/resources/Configs/";
	static WebDriverWait wait;
	public static Properties properties;
	public static Logger Log = LogManager.getLogger();
	public static String configFileName = "";
	public static String testEnvironment = "";
	public static String[][] testData = null;
	static ExtentTest test;
	static ExtentReports report;

	public static void printInLogFile(Logger Log, String string) {
		Log.info(string);
	}

	public static WebDriver getDriver() {
		return CommonHelper.driver;
	}

	public static void setDriver(WebDriver driver) {
		CommonHelper.driver = driver;
	}

	public static void setExtendReport() {
		report = new ExtentReports(System.getProperty("user.dir") + "/reports/ExtentReportResults.html");
		test = report.startTest("Packt UI Functionalities");
	}

	// End Report
	public static void endExtendReport() {
		report.endTest(test);
		report.flush();
	}

	public static WebDriverWait getWait(WebDriver driver) {
		wait = new WebDriverWait(driver, 180);
		return wait;

	}

	public static Properties loadProperties() throws Exception {
		properties = getProperties();
		return properties;
	}

	public static Properties getProperties() throws Exception {
		try {
			if (properties == null) {
				properties = new Properties();
				InputStream input = null;
				input = new FileInputStream(CONFIGS_FOLDER + "Config.properties");
				properties.load(input);
			}
		} catch (Exception e) {
			Log.error("Unable to load Properties" + e);
			throw (e);
		}
		return properties;
	}

	public static void setTestEnvironment(String strTestEnvironment) throws Exception {

		testEnvironment = strTestEnvironment;
		Utility.printInLogFile(Log, "Test is running for Environment : " + testEnvironment);
	}

	public static void setUpClient(String environment) throws Exception {
		properties = loadProperties();
		switch (properties.getProperty("Browser")) {
		case "Chrome":
			CommonHelper.setChromeDriver();
		case "Firefox":
			// CommonHelper.setFireFoxDriver();
		}
	}

	public static WebElement waitForElementToBe(By locator, String expectedCondition, WebDriver driver,
			int valueOfDuration) {
		try {
			getWait(driver);
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(valueOfDuration))
					.pollingEvery(Duration.ofSeconds(1));
			switch (expectedCondition) {
			case ("PRESENCE"):
				wait.until(ExpectedConditions.presenceOfElementLocated(locator));
				break;
			case ("CLICKABLE"):
				wait.until(ExpectedConditions.elementToBeClickable(locator));
				break;
			case ("VISIBLE"):
				wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
				break;
			case ("SELECTED"):
				wait.until(ExpectedConditions.elementToBeSelected(locator));
				break;
			case ("INVISIBLE"):
				wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
				break;
			}
		} catch (Exception e) {
			return null;
		}
		return driver.findElement(locator);
	}

	public static List<WebElement> waitForElementsToBe(By locator, String expectedCondition, WebDriver driver,
			int valueOfDuration) throws Exception {
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(valueOfDuration))
					.pollingEvery(Duration.ofSeconds(1));

			switch (expectedCondition) {
			case ("PRESENCE"):
				wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
				return driver.findElements(locator);
			case ("CLICKABLE"):
				wait.until(ExpectedConditions.elementToBeClickable(locator));
				return driver.findElements(locator);
			case ("VISIBLE"):
				wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
				return driver.findElements(locator);
			case ("SELECTED"):
				wait.until(ExpectedConditions.elementToBeSelected(locator));
				return driver.findElements(locator);
			case ("INVISIBLE"):
				wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
				return null;
			}
		} catch (NoSuchElementException e) {
			return null;
		} catch (ElementNotVisibleException e) {
			List<WebElement> els = driver.findElements(locator);
			if (els.isEmpty())
				return null;
			else
				return getDisplayedElements(els).isEmpty() ? null : getDisplayedElements(els);
		} catch (TimeoutException e) {
			List<WebElement> els = driver.findElements(locator);
			if (els.isEmpty())
				return null;
			else
				return getDisplayedElements(els).isEmpty() ? null : getDisplayedElements(els);
		} catch (Exception e) {
			Log.error("Error while processing request in waitForElementToBe " + e.getCause().toString());
			throw (e);
		}
		return null;
	}

	public WebElement getDisplayedElement(List<WebElement> elements) {
		try {
			for (WebElement element : elements) {
				if (element.isDisplayed()) {
					return element;
				}
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	public static List<WebElement> getDisplayedElements(List<WebElement> elements) throws Exception {
		List<WebElement> displayedWebElements = new ArrayList<WebElement>();
		try {
			for (WebElement element : elements) {
				if (element.isDisplayed()) {
					displayedWebElements.add(element);
				}
			}
		} catch (Exception e) {
			throw (e);
		}
		return displayedWebElements;
	}

	public static void ScrollToElementVisible(WebDriver driver, WebElement Element) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// This will scroll the page till the element is found
		js.executeScript("arguments[0].scrollIntoView();", Element);
	}

	public static By getLocatorType(String elementLocator) {
		try {
			String[] arrSplit = elementLocator.split(">");
			String locatorType = arrSplit[0].trim();
			String locatorAddress = arrSplit[1].trim();

			switch (locatorType) {
			case "ID":
				return By.id(locatorAddress);
			case "Name":
				return By.name(locatorAddress);
			case "CSS":
				return By.cssSelector(locatorAddress);
			case "XPATH":
				return By.xpath(locatorAddress);
			case "PARTIALLINKTEXT":
				return By.partialLinkText(locatorAddress);
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}
}
