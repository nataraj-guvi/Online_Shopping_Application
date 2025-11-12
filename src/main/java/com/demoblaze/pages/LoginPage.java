package com.demoblaze.pages;

import java.time.Duration;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.demoblaze.base.TestBase;
import com.demoblaze.utils.WaitUtils;

public class LoginPage extends TestBase {
	protected WebDriver driver;
	protected WaitUtils waitUtils;

	@FindBy(id = "login2")
	WebElement loginLink;

	@FindBy(id = "loginusername")
	WebElement usernameField;

	@FindBy(id = "loginpassword")
	WebElement passwordField;

	@FindBy(xpath = "//button[contains(text(),'Log in')]")
	WebElement loginButton;

	@FindBy(id = "nameofuser")
	WebElement welcomeMessage;

	@FindBy(xpath = "//button[contains(text(),'Close')]")
	WebElement closeOrderModalButton;

	public LoginPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		PageFactory.initElements(driver, this);
	}

	public void clickLoginLink() {
		
		wait.until(ExpectedConditions.elementToBeClickable(loginLink)).click();
	}

	public void enterUsername(String username) {
		wait.until(ExpectedConditions.visibilityOf(usernameField)).sendKeys(username);
	}

	public void enterPassword(String password) {
		wait.until(ExpectedConditions.visibilityOf(passwordField)).sendKeys(password);
	}

	public void clickLoginButton() {
		wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
	}

	public boolean isWelcomeMessageDisplayed() {
		try {

			return wait.until(ExpectedConditions.visibilityOf(welcomeMessage)).isDisplayed();

		} catch (Exception e) {
			return false;
		}
	}

	public String getWelcomeMessageText() {
		return wait.until(ExpectedConditions.visibilityOf(welcomeMessage)).getText();
	}

	public WebElement getPasswordField() {
		return wait.until(ExpectedConditions.visibilityOf(passwordField));
	}

	public void login(String username, String password) {
		clickLoginLink();
		enterUsername(username);
		enterPassword(password);
		clickLoginButton();
	}

	public String getAlertText() {
		try {
			wait.until(ExpectedConditions.alertIsPresent());
			String alertText = driver.switchTo().alert().getText();
			driver.switchTo().alert().accept();
			return alertText;
		} catch (Exception e) {
			return "No alert present";
		}
	}

	public void closeOrderModal() {
		try {
			if (driver == null)
				return;
			Actions actions = new Actions(driver);
			actions.sendKeys(Keys.ESCAPE).perform();
			} catch (Exception e) {
			System.out.println("Emergency close failed: " + e.getMessage());
		}
	}
}