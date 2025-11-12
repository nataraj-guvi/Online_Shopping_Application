package com.demoblaze.tests;

import com.demoblaze.reports.ExtentManager;
import com.aventstack.extentreports.Status;
import com.demoblaze.base.TestBase;
import com.demoblaze.pages.SignUpPage;
import com.demoblaze.testdata.ExcelDataProvider;
import java.util.concurrent.TimeoutException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SignUpTest extends TestBase {
	SignUpPage signUpPage;

	@BeforeMethod
	public void pageSetup() {
		signUpPage = new SignUpPage(driver);
		  logger.info("Page setup completed - SignUpPage initialized");
	}

	@Test(priority = 1, dataProvider = "signupdata", dataProviderClass = ExcelDataProvider.class, description = "Verify user sign-up with unique credentials")
	public void testSignupWithValidDetails(String username, String password) throws TimeoutException {
		test = ExtentManager.getTest().createNode("Signup contact with valid details");
		try {
			logger.info("Starting testSignUpWithValidInputs for user: {} {}", username, password);
			logger.info("Navigating to signup page");
			System.out.println("Testing signup with - Username: " + username + ", Password: " + password);
			signUpPage.clickSignUpLink();
			signUpPage.enterUsername(username);
			signUpPage.enterPassword(password);
			logger.debug("Successfully navigated to signup page");
			logger.info("Filling signup form with provided data");
			signUpPage.clickSignUpButton();
			String alertText = signUpPage.getAlertText();
			Assert.assertTrue(alertText.contains("Sign up successful."),
					"Sign up success message not found: " + alertText);
			logger.debug("Signup form submitted successfully");
		} catch (AssertionError e) {
			System.out.println("Assertion failed: " + e.getMessage());
			logger.error("Signup process interrupted: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			System.out.println("Unexpected exception during signup: " + e.getMessage());
			Assert.fail("Unexpected exception during signup: " + e.getMessage());
			test.log(Status.FAIL, "Test failed with error: {} " + e.getMessage());
		}
	}

	@Test(priority = 2, dataProvider = "excelLoginData", dataProviderClass = ExcelDataProvider.class, description = "Verify sign-up with existing username")
	public void testValidLogin(String username, String password) {
		test = ExtentManager.getTest().createNode("Signup contact with existing username");
		logger.info("Starting Verify sign-up with existing username for user: {} {}", username, password);
		logger.info("Navigating to signup page");
		signUpPage.clickSignUpLink();
		signUpPage.enterUsername(username);
		signUpPage.enterPassword(password);
		signUpPage.clickSignUpButton();
		String alertText = signUpPage.getAlertText();
		Assert.assertTrue(alertText.contains("This user already exist"),
				"Existing user error message not found: " + alertText);
		signUpPage.closeOrderModal();
	}

	@Test(priority = 3, dataProvider = "excelEmptyFieldsLoginData", dataProviderClass = ExcelDataProvider.class, description = "Verify sign-up with empty fields")
	public void testEmptyFields(String usernameKey, String passwordKey) {
		test = ExtentManager.getTest().createNode("Signup contact with EmptyFields");
		logger.info("Starting sign-up with empty fields for user: {} {}", usernameKey, passwordKey);
		logger.info("Navigating to signup page");
		signUpPage.clickSignUpLink();
		String username = config.getProperty(usernameKey);
		String password = config.getProperty(passwordKey);
		signUpPage.enterUsername(username);
		signUpPage.enterPassword(password);
		signUpPage.clickSignUpButton();
		String alertText = signUpPage.getAlertText();
		Assert.assertTrue(alertText.contains("Please fill out Username and Password."),
				"Empty fields error message not found: " + alertText);
		signUpPage.closeOrderModal();
	}
}