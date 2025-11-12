package com.demoblaze.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class WaitUtils {
	
	protected WebDriver driver;
	protected WebDriverWait wait;
	private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(20);
    private static final Duration POLLING_INTERVAL = Duration.ofMillis(50);
    
    public static <T> T waitForCondition(WebDriver driver, Function<WebDriver, T> condition, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(condition);
    }
    
    public static void waitForMillis(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

	public WaitUtils(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}
	 public boolean isElementVisible(WebElement element, int timeoutSeconds) {
	        try {
	            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
	            customWait.until(ExpectedConditions.visibilityOf(element));
	            return true;
	        } catch (Exception e) {
	            return false;
	        }
	    }
	  public static FluentWait<WebDriver> getFluentWait(WebDriver driver) {
	        return new FluentWait<>(driver)
	                .withTimeout(DEFAULT_TIMEOUT)
	                .pollingEvery(POLLING_INTERVAL)
	                .ignoring(NoSuchElementException.class)
	                .ignoring(StaleElementReferenceException.class);
	    }
	    
	  public static WebElement waitForElementVisible(WebDriver driver, By locator) {
	        return getFluentWait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
	    }
	  public static WebElement waitForElementVisible(WebDriver driver, WebElement element) {
	        return getFluentWait(driver).until(ExpectedConditions.visibilityOf(element));
	    }
	   public static WebElement waitForElementClickable(WebDriver driver, By locator) {
		
	        return getFluentWait(driver).until(ExpectedConditions.elementToBeClickable(locator));
	    }
	    
	    public static WebElement waitForElementClickable(WebDriver driver, WebElement element) {
	        return getFluentWait(driver).until(ExpectedConditions.elementToBeClickable(element));
	    }
	    public static Alert waitForAlert(WebDriver driver) {
	        return getFluentWait(driver).until(ExpectedConditions.alertIsPresent());
	    }
	    public static boolean waitForTextToBePresent(WebDriver driver, WebElement element, String text) {
	        return getFluentWait(driver).until(ExpectedConditions.textToBePresentInElement(element, text));
	    }
	    
	    public static boolean waitForElementToDisappear(WebDriver driver, By locator) {
	        return getFluentWait(driver).until(ExpectedConditions.invisibilityOfElementLocated(locator));
	    }
	    
	    public static void waitForPageLoad(WebDriver driver) {
	        getFluentWait(driver).until(webDriver -> {
	            String state = (String) ((JavascriptExecutor) webDriver)
	                    .executeScript("return document.readyState");
	            return state.equals("complete");
	        });
	    }
	    public static <T> T waitForCondition(WebDriver driver, Function<WebDriver, T> condition) {
	        return getFluentWait(driver).until(condition);
	    }

	public void  waitForElementInvisible(WebElement element) {
		wait.until(ExpectedConditions.invisibilityOf(element));
	}
	public void 	ForTextToBePresent(WebElement element, String text) {
		wait.until(ExpectedConditions.textToBePresentInElement(element, text));
	}
	public void waitForUrlToContain(String text) {
		wait.until(ExpectedConditions.urlContains(text));
	}
	public void waitForTitleToContain(String text) {
		wait.until(ExpectedConditions.titleContains(text));
	}
	public void waitForAlertToBePresent() {
		wait.until(ExpectedConditions.alertIsPresent());
	}
	public void waitForFrameToBeAvailable(String frameId) {
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameId));
	}
	public void waitForAllElementsVisible(List<WebElement> elements) {
		wait.until(ExpectedConditions.visibilityOfAllElements(elements));
	}
	public void waitForAnyElementVisible(List<WebElement> elements) {
		wait.until(driver -> {
			for (WebElement element : elements) {
				if (element.isDisplayed()) {
					return true;
				}
			}
			return false;
		});
	}
	
    public void waitForProductRemoval() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//div[contains(@class,'loading') or contains(@class,'progress')]")));
    }
	public void waitForElementVisible(WebElement element, String message) {
		WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(10));
		customWait.withMessage(message).until(ExpectedConditions.visibilityOf(element));
	}
	public FluentWait<WebDriver> getFluentWait(int timeoutSeconds, int pollingSeconds) {
		return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
				.pollingEvery(Duration.ofSeconds(pollingSeconds)).ignoring(Exception.class);
	}
	public void waitForElementVisible(WebElement element, int timeoutSeconds) {
		WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
		customWait.until(ExpectedConditions.visibilityOf(element));
	}
	public void waitForJavascriptToLoad() {
		wait.until(driver -> {
			String script = "return document.readyState";
			Object result = ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(script);
			return result.equals("complete");
		});
	}
	public void waitForJQueryToLoad() {
		wait.until(driver -> {
			String script = "return jQuery.active == 0";
			Object result = ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(script);
			return Boolean.TRUE.equals(result);
		});
	}

}