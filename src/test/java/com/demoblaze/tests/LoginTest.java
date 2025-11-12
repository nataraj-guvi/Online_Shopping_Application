package com.demoblaze.tests;

import com.demoblaze.testdata.ExcelDataProvider;
import com.demoblaze.reports.ExtentManager;
import com.aventstack.extentreports.Status;
import com.demoblaze.base.TestBase;
import com.demoblaze.pages.LoginPage;
import com.demoblaze.pages.LogoutPage;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTest extends TestBase {
	LoginPage loginPage;
	LogoutPage logoutPage;

	@BeforeMethod
	public void pageSetup() {
		loginPage = new LoginPage(driver);
		logoutPage = new LogoutPage();
		  logger.info("Page setup completed - LoginPage initialized");
	}

	@Test(priority = 1, dataProvider = "excelLoginData", dataProviderClass = ExcelDataProvider.class, description = "Verify login with valid credentials")
	public void testValidLogin(String username, String password) throws TimeoutException {
		test = ExtentManager.getTest().createNode("Valid Login Test");
		try {
			logger.info("Starting valid login test with data provider keys: {}, {}", username, password);
			loginPage.login(username, password);
			logger.info("Attempting login");
			WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(5));
			alertWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nameofuser")));
			Assert.assertTrue(loginPage.isWelcomeMessageDisplayed(), "Welcome message is displayed after login");
			String welcomeText = loginPage.getWelcomeMessageText();
			Assert.assertTrue(welcomeText.contains("Welcome"), "Welcome message contains expected text");
			System.out.println(welcomeText);
			logger.info("Valid login test passed");
			logoutPage.clickLogout();
			test.log(Status.PASS, "Valid login test passed");
		} catch (AssertionError e) {
			System.out.println("Assertion failed: " + e.getMessage());
			logger.info("Verifying login failure handling");
			throw e;
			
		} catch (Exception e) {
			System.out.println("Unexpected exception: " + e.getMessage());
			logger.info("Verifying login failure handling");
			Assert.fail("Unexpected exception: " + e.getMessage());
			test.log(Status.FAIL, "Test failed with error: {} " + e.getMessage());
		}
	}

	@Test(priority = 2, dataProvider = "excelIncorrectPasswordLoginData", dataProviderClass = ExcelDataProvider.class, description = "Verify login with incorrect password")
	public void testInvalidPassword(String username, String password) {
		test = ExtentManager.getTest().createNode("Incorrect Password Data Login Test");
		logger.info("Starting InvalidPassword test with data provider keys: {}, {}", username, password);
		loginPage.login(username, password);
		logger.info("Attempting login");
		WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(5));
		alertWait.until(ExpectedConditions.alertIsPresent());
		String alertText = loginPage.getAlertText();
		Assert.assertEquals(alertText, "Wrong password.",
				"Alert message. Expected: 'Wrong password.', Actual: '" + alertText + "'");
		System.out.println(alertText);
		loginPage.closeOrderModal();

	}

	@Test(priority = 3, dataProvider = "excelEmptyFieldsLoginData", dataProviderClass = ExcelDataProvider.class, description = "Verify login with EmptyFields")
	public void testEmptyFields(String usernameKey, String passwordKey) throws InterruptedException {
		test = ExtentManager.getTest().createNode("Login with empty fields");
		logger.info("Starting EmptyFields test with data provider keys: {}, {}", usernameKey, passwordKey);
		String username = config.getProperty(usernameKey);
		String password = config.getProperty(passwordKey);
		loginPage.login(username, password);
		logger.info("Attempting login");
		WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(5));
		alertWait.until(ExpectedConditions.alertIsPresent());
		String alertText = loginPage.getAlertText();
		Assert.assertEquals(alertText, "Please fill out Username and Password.",
				"Alert message. Expected: 'Please fill out Username and Password.', Actual: '" + alertText + "'");
		System.out.println(alertText);
		loginPage.closeOrderModal();
	}

	@Test(priority = 4, dataProvider = "excelInvalidEmailLoginData", dataProviderClass = ExcelDataProvider.class, description = "Verify login with InvalidEmailFormat")
	public void testInvalidEmailFormat(String username, String password) throws InterruptedException {
		test = ExtentManager.getTest().createNode("Login with InvalidEmail");
		logger.info("Starting InvalidEmailFormat test with data provider keys: {}, {}", username, password);
		loginPage.login(username, password);
		logger.info("Attempting login");
		WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(5));
		alertWait.until(ExpectedConditions.alertIsPresent());
		String alertText = loginPage.getAlertText();
		Assert.assertEquals(alertText, "Wrong password.",
				"Alert message.Expected: 'Wrong password.', Actual: '" + alertText + "'");
		System.out.println(alertText);
		loginPage.closeOrderModal();
	}

	@Test(priority = 5, dataProvider = "excelPasswordMaskingLoginData", dataProviderClass = ExcelDataProvider.class, description = "Verify password field masks input")
	public void testPasswordFieldMasksInput(String username, String password) throws TimeoutException {
		test = ExtentManager.getTest().createNode("Login with PasswordMasking");
		try {
			logger.info("Starting PasswordFieldMasksInput test with data provider keys: {}, {}", username, password);
			loginPage.login(username, password);
			logger.info("Attempting login");
			
			WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(5));
			WebElement passwordField = alertWait
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginpassword")));
			String fieldType = passwordField.getAttribute("value");
			System.out.println("Password field type: " + fieldType);
			logoutPage.clickLogout();
		} catch (AssertionError e) {
			System.out.println("Assertion failed: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			System.out.println("Unexpected exception: " + e.getMessage());
			Assert.fail("Unexpected exception: " + e.getMessage());
			test.log(Status.FAIL, "Test failed with error: {} " + e.getMessage());
		}
	}
}