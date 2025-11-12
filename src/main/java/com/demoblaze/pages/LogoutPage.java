package com.demoblaze.pages;

import com.demoblaze.base.TestBase;
import com.demoblaze.utils.WaitUtils;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LogoutPage extends TestBase{
	WebDriver driver;

	@FindBy(id = "logout2")
	WebElement logoutButton;

	@FindBy(id = "login2")
	WebElement loginButton;

	@FindBy(id = "nameofuser")
	WebElement welcomeMessage;

	@FindBy(id = "nava")
	WebElement homeLink;

	By logoutButtonLocator = By.id("logout2");
	By loginButtonLocator = By.id("login2");

//	public LogoutPage(WebDriver driver) {
//		this.driver = driver;
//		PageFactory.initElements(driver, this);
//	}
//	public void safeLogout() {
//	    try {
//	        // First try normal logout
//	        clickLogout();
//	    } catch (ElementClickInterceptedException e) {
//	        logger.warn("Logout intercepted, using JavaScript click");
//	        // Use JavaScript to click the logout button
//	        JavascriptExecutor js = (JavascriptExecutor) driver;
//	        WebElement logoutBtn = driver.findElement(By.id("logout2"));
//	        js.executeScript("arguments[0].click();", logoutBtn);
//	    }
//	}

//	public void clickLogout() {
//		WaitUtils.waitForElementClickable(driver, logoutButton).click();
//		waitForLogoutToComplete();
//	}
	public void clickLogout() {
	    try {
	        // Add null check and try multiple selectors
	        List<By> logoutSelectors = Arrays.asList(
	            By.id("logout2"),
	            By.xpath("//a[contains(text(),'Log out')]"),
	            By.cssSelector("a[onclick*='logOut']")
	        );

	        for (By selector : logoutSelectors) {
	            try {
	                List<WebElement> elements = driver.findElements(selector);
	                if (!elements.isEmpty() && elements.get(0).isDisplayed()) {
	                    WebElement logoutElem = wait.until(ExpectedConditions.elementToBeClickable(elements.get(0)));
	                    JavascriptExecutor js = (JavascriptExecutor) driver;
	                    js.executeScript("arguments[0].click();", logoutElem);
	                    System.out.println("Logout clicked using: " + selector);
	                    return;
	                }
	            } catch (Exception e) {
	                // Continue to next selector
	            }
	        }
	        
	        System.out.println("No logout element found");
	        
	    } catch (Exception e) {
	        System.out.println("Logout failed: " + e.getMessage());
	        // Don't throw exception, just log and continue
	    }
	}

	public void waitForLogoutToComplete() {
		WaitUtils.waitForElementVisible(driver, loginButtonLocator);
		WaitUtils.waitForElementToDisappear(driver, By.id("nameofuser"));
	}

	public boolean isLogoutButtonDisplayed() {
		try {
			return WaitUtils.waitForElementVisible(driver, logoutButton).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isLoginButtonDisplayed() {
		try {
			return WaitUtils.waitForElementVisible(driver, loginButton).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isWelcomeMessageDisplayed() {
		try {
			return welcomeMessage.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public String getWelcomeMessageText() {
		try {
			return welcomeMessage.getText();
		} catch (Exception e) {
			return "";
		}
	}

	public boolean isUserOnHomePage() {
		try {
			String currentUrl = driver.getCurrentUrl();
			return currentUrl.contains("index.html") || currentUrl.equals("https://www.demoblaze.com/");
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isUserLoggedOut() {
		return isLoginButtonDisplayed() && !isWelcomeMessageDisplayed() && !isLogoutButtonDisplayed();
	}

	public boolean isUserLoggedIn() {
		return isLogoutButtonDisplayed() && isWelcomeMessageDisplayed() && !isLoginButtonDisplayed();
	}
}