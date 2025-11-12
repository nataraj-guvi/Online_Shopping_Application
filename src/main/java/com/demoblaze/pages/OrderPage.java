package com.demoblaze.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.demoblaze.utils.WaitUtils;

import java.time.Duration;
import java.util.List;

public class OrderPage {
	WebDriver driver;
	WebDriverWait wait;

	@FindBy(xpath = "//button[contains(text(),'Place Order')]")
	WebElement placeOrderButton;

	@FindBy(id = "orderModal")
	WebElement orderModal;

	@FindBy(id = "name")
	WebElement nameField;

	@FindBy(id = "country")
	WebElement countryField;

	@FindBy(id = "city")
	WebElement cityField;

	@FindBy(id = "card")
	WebElement cardField;

	@FindBy(id = "month")
	WebElement monthField;

	@FindBy(id = "year")
	WebElement yearField;

	@FindBy(xpath = "//button[contains(text(),'Purchase')]")
	WebElement purchaseButton;

	@FindBy(xpath = "//button[contains(text(),'Close')]")
	WebElement closeOrderModalButton;

	@FindBy(xpath = "//div[contains(@class,'sweet-alert') and contains(@class,'visible')]")
	WebElement confirmationModal;

	@FindBy(xpath = "//h2[contains(text(),'Thank you for your purchase!')]")
	WebElement thankYouMessage;

	@FindBy(xpath = "//p[@class='lead text-muted ']")
	WebElement orderDetails;

//	@FindBy(xpath = "//button[contains(text(),'OK')]")
	@FindBy(css = "button.confirm.btn.btn-lg.btn-primary")
	WebElement confirmationOkButton;

	@FindBy(xpath = "//div[contains(@class,'invalid-feedback')]")
	WebElement validationMessage;

	@FindBy(css = ".modal-footer .btn-secondary") // Update with actual selector
	WebElement closeModalButton;

	@FindBy(css = ".modal") // Update with actual selector
	private WebElement modalElement;

	public OrderPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		PageFactory.initElements(driver, this);
	}

	public WebElement getCloseModalElement() {
		return closeModalButton;
	}

	public WebElement getModalElement() {
		return modalElement;
	}

	public void clickPlaceOrder() {
		try {
			WebElement placeOrderBtn = wait.until(ExpectedConditions.elementToBeClickable(placeOrderButton));
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", placeOrderBtn);
			waitForOrderModalToLoad();
		} catch (Exception e) {
			throw new RuntimeException("Failed to click Place Order button", e);
		}
	}

	public boolean isOrderModalDisplayed() {
		try {
			return WaitUtils.waitForCondition(driver, d -> orderModal.isDisplayed() && nameField.isDisplayed());
		} catch (Exception e) {
			return false;
		}
	}

	private void waitForOrderModalToLoad() {
		WaitUtils.waitForCondition(driver, d -> isOrderModalDisplayed() && purchaseButton.isDisplayed());
	}
	public void closeOrderModal() {
	    try {
	        if (driver == null) return;
	        
	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        
	        // Direct JavaScript click on the Close button
	        js.executeScript("document.querySelector('button[data-dismiss=\"modal\"].btn-secondary').click();");
	        
	        System.out.println("Close button clicked via JavaScript");
	        
	    } catch (Exception e) {
	        System.out.println("JavaScript close failed: " + e.getMessage());
	        forceRemoveModal();
	    }
	}

//	public void closeOrderModal() {
//	    try {
//	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//	        JavascriptExecutor js = (JavascriptExecutor) driver;
//
//	        // Wait for close button and click with JavaScript
//	        WebElement closeBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
//	            By.cssSelector(".modal-footer .btn-secondary, .close[data-dismiss='modal']")
//	        ));
//	        
//	        js.executeScript("arguments[0].click();", closeBtn);
//	        
//	        // Wait for modal to close
//	        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("orderModal")));
//	        System.out.println("Modal closed successfully");
//	        
//	    } catch (Exception e) {
//	        System.out.println("Close button method failed: " + e.getMessage());
//	        forceRemoveModal();
//	    }
//	}

	private void forceRemoveModal() {
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    js.executeScript(
	        "document.querySelectorAll('.modal, .modal-backdrop').forEach(el => el.remove());" +
	        "document.body.classList.remove('modal-open');"
	    );
	    System.out.println("Modal force-removed");
	}
//	public void closeOrderModal() {
//	    try {
//	        JavascriptExecutor js = (JavascriptExecutor) driver;
//	        
//	        // Single JavaScript command to remove all modal artifacts
//	        js.executeScript(
//	            "document.querySelectorAll('#orderModal, .modal-backdrop').forEach(e => e.remove());" +
//	            "document.body.classList.remove('modal-open');" +
//	            "document.body.style.overflow = 'auto';" +
//	            "document.body.style.paddingRight = '0px';"
//	        );
//	        
//	        System.out.println("Modal completely removed");
//	        
//	    } catch (Exception e) {
//	        System.out.println("Modal removal failed: " + e.getMessage());
//	    }
//	}	
//	public void closeOrderModal() {
//	    try {
//	        if (driver == null) return;
//	        
//	        // Use the correct close button from your HTML
//	        WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(
//	            By.cssSelector(".modal-footer .btn-secondary")));
//	        
//	        JavascriptExecutor js = (JavascriptExecutor) driver;
//	        js.executeScript("arguments[0].click();", closeBtn);
//	        
//	        System.out.println("Order modal closed successfully");
//	        
//	    } catch (Exception e) {
//	        System.out.println("Failed to close modal: " + e.getMessage());
//	        // Don't throw exception, just log and continue
//	    }
//	}
//	public void closeOrderModal() {
//		try {
//			if (driver == null)
//				return;
//			Actions actions = new Actions(driver);
//			actions.sendKeys(Keys.ESCAPE).perform();
//		} catch (Exception e) {
//			System.out.println("Emergency close failed: " + e.getMessage());
//		}
//	}

	public void completePurchase(String name, String country, String city, String card, String month, String year) {
		try {
			fillFieldWithWait(nameField, name, "Name");
			fillFieldWithWait(countryField, country, "Country");
			fillFieldWithWait(cityField, city, "City");
			fillFieldWithWait(cardField, card, "Card");
			fillFieldWithWait(monthField, month, "Month");
			fillFieldWithWait(yearField, year, "Year");

			clickPurchase();

		} catch (Exception e) {
			throw new RuntimeException("Failed to complete purchase", e);
		}
	}

	private void fillFieldWithWait(WebElement field, String value, String fieldName) {
		try {
			wait.until(ExpectedConditions.visibilityOf(field)).clear();
			field.sendKeys(value);
			System.out.println("Filled " + fieldName + ": " + value);
		} catch (Exception e) {
			throw new RuntimeException("Failed to fill field: " + fieldName, e);
		}
	}

	public void clickPurchase() {
		try {
			WebElement purchaseBtn = wait.until(ExpectedConditions.elementToBeClickable(purchaseButton));
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", purchaseBtn);
			System.out.println(" Purchase button clicked");
		} catch (Exception e) {
			throw new RuntimeException("Failed to click Purchase button", e);
		}
	}
	public void clickConfirmationOk() {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(confirmationOkButton)).click();
			WaitUtils.waitForCondition(driver, d -> !isConfirmationModalDisplayed());
			  System.out.println("Ok button Clicked");
		} catch (Exception e) {
			throw new RuntimeException("Failed to click confirmation OK button", e);
		}
	}
	public void attemptPurchaseWithEmptyForm() {
		try {

			clickPurchase();
		} catch (Exception e) {
			System.out.println("Empty form prevented purchase as expected");
		}
	}

	public boolean isFormSubmissionPrevented() {
		return WaitUtils.waitForCondition(driver, d -> isOrderModalDisplayed() && !isConfirmationModalDisplayed());
	}

	public boolean areRequiredFieldsEmpty() {
		try {
			return nameField.getAttribute("value").isEmpty() || countryField.getAttribute("value").isEmpty()
					|| cardField.getAttribute("value").isEmpty();
		} catch (Exception e) {
			return true;
		}
	}

	public boolean isConfirmationModalDisplayed() {
		try {
			return WaitUtils.waitForCondition(driver,
					d -> confirmationModal.isDisplayed() && thankYouMessage.isDisplayed());
		} catch (Exception e) {
			return false;
		}
	}

	public String getThankYouMessage() {
		try {
			return wait.until(ExpectedConditions.visibilityOf(thankYouMessage)).getText();
		} catch (Exception e) {
			return "";
		}
	}

	

	public boolean isOrderIdValid() {
		String orderId = getOrderIdFromConfirmation();
		return orderId != null && !orderId.isEmpty() && orderId.length() > 5;
	}

	public boolean isAmountValid() {
		String amount = getAmountFromConfirmation();
		return amount != null && !amount.isEmpty() && (amount.contains("$") || amount.contains("USD"));
	}

	public String getOrderIdFromConfirmation() {
		try {
			String details = getOrderDetails();
			return extractOrderId(details);
		} catch (Exception e) {
			return "";
		}
	}

	public String getAmountFromConfirmation() {
		try {
			String details = getOrderDetails();
			return extractAmount(details);
		} catch (Exception e) {
			return "";
		}
	}

	public String getOrderDetails() {
		try {
			return wait.until(ExpectedConditions.visibilityOf(orderDetails)).getText();
		} catch (Exception e) {
			return "";
		}
	}

	public String extractOrderId(String orderDetails) {
		if (orderDetails.contains("Id:")) {
			String[] parts = orderDetails.split("Id:");
			if (parts.length > 1) {
				String idPart = parts[1].trim();
				String[] idParts = idPart.split("\\s+");
				if (idParts.length > 0) {
					return idParts[0].trim();
				}
			}
		}
		return "";
	}

	public String extractAmount(String orderDetails) {
		if (orderDetails.contains("Amount:")) {
			String[] parts = orderDetails.split("Amount:");
			if (parts.length > 1) {
				String amountPart = parts[1].trim();
				String[] amountParts = amountPart.split("\\s+");
				if (amountParts.length > 0) {
					return amountParts[0].trim() + (amountParts.length > 1 ? " " + amountParts[1].trim() : "");
				}
			}
		}
		return "";
	}

	public boolean isPurchaseSuccessful() {
		return isConfirmationModalDisplayed();
	}

	public boolean isPurchaseFailed() {
		return !isConfirmationModalDisplayed() && isOrderModalDisplayed();
	}

	public WebElement getOrderModalElement() {
		return orderModal;
	}

	public WebElement getConfirmationModalElement() {
		return confirmationModal;
	}

	public WebElement getValidationErrorElement() {
		return driver.findElement(By.xpath("//div[contains(@class,'invalid-feedback') or contains(@class,'alert')]"));
	}

	public String getValidationMessage() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			String nameValidation = (String) js.executeScript("return arguments[0].validationMessage;", nameField);
			if (nameValidation != null && !nameValidation.isEmpty()) {
				return nameValidation;
			}
			List<WebElement> errorElements = driver.findElements(
					By.xpath("//div[contains(@class,'invalid-feedback')] | //div[contains(@class,'alert-danger')]"));
			for (WebElement error : errorElements) {
				if (error.isDisplayed()) {
					return error.getText();
				}
			}
			if (isFormSubmissionPrevented() && areRequiredFieldsEmpty()) {
				return "Please fill out all required fields";
			}

		} catch (Exception e) {
		}
		return "Form validation required";
	}

	public boolean hasValidationErrors() {
		try {
			return !getValidationMessage().equals("Form validation required") || isFormSubmissionPrevented();
		} catch (Exception e) {
			return false;
		}
	}
}