package com.demoblaze.pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SignUpPage {
	WebDriver driver;
	WebDriverWait wait;

	@FindBy(id = "signin2")
	WebElement signUpLink;

	@FindBy(id = "sign-username")
	WebElement usernameField;

	@FindBy(id = "sign-password")
	WebElement passwordField;

	@FindBy(xpath = "//button[contains(text(),'Sign up')]")
	WebElement signUpButton;

	public SignUpPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		PageFactory.initElements(driver, this);
	}

	public void clickSignUpLink() {
		wait.until(ExpectedConditions.elementToBeClickable(signUpLink)).click();
	}

	public void enterUsername(String username) {
		wait.until(ExpectedConditions.visibilityOf(usernameField)).clear();
		wait.until(ExpectedConditions.visibilityOf(usernameField)).sendKeys(username);
	}

	public void enterPassword(String password) {
		wait.until(ExpectedConditions.visibilityOf(passwordField)).clear();
		wait.until(ExpectedConditions.visibilityOf(passwordField)).sendKeys(password);
	}

	public void clickSignUpButton() {
		wait.until(ExpectedConditions.elementToBeClickable(signUpButton)).click();
	}

	public String getAlertText() {
		try {
			WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(5));
			alertWait.until(ExpectedConditions.alertIsPresent());
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