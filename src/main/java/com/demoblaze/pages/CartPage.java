package com.demoblaze.pages;

import com.demoblaze.base.TestBase;
import com.demoblaze.utils.WaitUtils;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class CartPage extends TestBase {
	WebDriver driver;
	WebDriverWait wait;

	@FindBy(id = "cartur")
	WebElement cartLink;

	@FindBy(xpath = "//button[contains(text(),'Place Order')]")
	WebElement placeOrderButton;

	@FindBy(id = "totalp")
	WebElement totalPrice;

	@FindBy(xpath = "//tbody/tr")
	List<WebElement> cartItems;

	@FindBy(xpath = "//a[contains(text(),'Home')]")
	WebElement homeLink;

	@FindBy(xpath = "//a[contains(text(),'Add to cart')]")
	WebElement addToCartButton;
	By cartItemsLocator = By.xpath("//tbody/tr");
	By productTitlesLocator = By.xpath("//tbody/tr/td[2]");
	By productPricesLocator = By.xpath("//tbody/tr/td[3]");
	By deleteButtonsLocator = By.xpath("//tbody/tr/td[4]/a");

	public CartPage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		PageFactory.initElements(driver, this);
	}

	public void goToCart() {
		wait.until(ExpectedConditions.elementToBeClickable(cartLink)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("totalp")));
	}

	public void clickCartLink() {
		WaitUtils.waitForElementClickable(driver, cartLink).click();
		WaitUtils.waitForElementVisible(driver, placeOrderButton);
	}

	public void goToCategory(String categoryName) {
		try {

			int attempts = 0;
			while (attempts < 3) {
				try {
					WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
					WebElement categoryLink = wait.until(ExpectedConditions
							.elementToBeClickable(By.xpath("//a[contains(text(),'" + categoryName + "')]")));

					categoryLink.click();
					System.out.println("Navigated to category: " + categoryName);
					break;

				} catch (StaleElementReferenceException e) {
					attempts++;
					System.out.println("Stale element encountered, retry attempt: " + attempts);
					if (attempts == 3)
						throw e;
				}
			}

			waitForPageLoad();

		} catch (Exception e) {
			System.err.println("Failed to navigate to category: " + categoryName);
			throw e;
		}
	}

	public void goToHome() {
		wait.until(ExpectedConditions.elementToBeClickable(homeLink)).click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'card')]//h4/a")));
	}

	public void addProductToCart(String productName) {
		try {

			String productXpath = "//a[contains(text(),'" + productName + "')]";
			WebElement productLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(productXpath)));
			productLink.click();

			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Add to cart')]")))
					.click();

		} catch (Exception e) {

			String productXpath = "//a[contains(text(),'" + productName + "')]";
			WebElement productLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(productXpath)));
			productLink.click();
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Add to cart')]")))
					.click();
		}
	}

	public void deleteProductByName(String productName) {
		try {
			String deleteXpath = "//td[contains(text(),'" + productName
					+ "')]/following-sibling::td/a[contains(text(),'Delete')]";
			WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(deleteXpath)));

			int initialCount = getCartItemCount();
			deleteButton.click();

			wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//tbody/tr"), initialCount - 1));

		} catch (Exception e) {

			String deleteXpath = "//td[contains(text(),'" + productName
					+ "')]/following-sibling::td/a[contains(text(),'Delete')]";
			WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(deleteXpath)));

			int initialCount = getCartItemCount();
			deleteButton.click();
			wait.until(ExpectedConditions.numberOfElementsToBe(By.xpath("//tbody/tr"), initialCount - 1));
		}
	}

	public void clickPlaceOrderButton() {
		WaitUtils.waitForElementClickable(driver, placeOrderButton).click();
	}

	public void clickAddToCart() {
		WaitUtils.waitForElementClickable(driver, addToCartButton).click();
	}

	public List<String> getAvailableProducts() {
		List<String> products = new ArrayList<>();
		try {
			// Locator for all product links
			By productLinksLocator = By.xpath("//div[@class='card-block']/h4/a");
			WaitUtils.waitForElementVisible(driver, productLinksLocator);

			List<WebElement> productElements = driver.findElements(productLinksLocator);
			for (WebElement element : productElements) {
				products.add(element.getText());
			}
		} catch (Exception e) {
			System.out.println("Error getting available products: " + e.getMessage());
		}
		return products;
	}

	public String getAlertText() {
		try {
			Alert alert = WaitUtils.waitForAlert(driver);
			String alertText = alert.getText();
			alert.accept();
			return alertText;
		} catch (Exception e) {
			return "No alert present";
		}
	}

	public boolean isCartPageDisplayed() {
		try {
			WaitUtils.waitForElementVisible(driver, placeOrderButton);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public int getCartItemCount() {
		try {
			WaitUtils.waitForElementVisible(driver, cartItemsLocator);
			return driver.findElements(cartItemsLocator).size();
		} catch (Exception e) {
			return 0;
		}
	}

	public List<String> getCartProductTitles() {
		List<String> productTitles = new ArrayList<>();
		try {
			WaitUtils.waitForElementVisible(driver, productTitlesLocator);
			List<WebElement> titleElements = driver.findElements(productTitlesLocator);
			for (WebElement element : titleElements) {
				productTitles.add(element.getText());
			}
		} catch (Exception e) {

		}
		return productTitles;
	}

	public List<String> getCartProductPrices() {
		List<String> productPrices = new ArrayList<>();
		try {
			WaitUtils.waitForElementVisible(driver, productPricesLocator);
			List<WebElement> priceElements = driver.findElements(productPricesLocator);
			for (WebElement element : priceElements) {
				productPrices.add(element.getText());
			}
		} catch (Exception e) {

		}
		return productPrices;
	}

	public String getTotalPrice() {
		try {
			return WaitUtils.waitForElementVisible(driver, totalPrice).getText();
		} catch (Exception e) {
			return "0";
		}
	}

	public void deleteProductFromCart(int index) {
		try {
			WaitUtils.waitForElementVisible(driver, cartItemsLocator);
			List<WebElement> deleteLinks = driver.findElements(deleteButtonsLocator);
			if (index < deleteLinks.size()) {
				int initialCount = getCartItemCount();
				deleteLinks.get(index).click();

				WaitUtils.waitForCondition(driver, d -> getCartItemCount() < initialCount);
			}
		} catch (Exception e) {
			System.out.println("Error deleting product: " + e.getMessage());
		}
	}

	public void deleteFirstProductFromCart() {
		deleteProductFromCart(0);
	}

	public int calculateExpectedTotal() {
		List<String> prices = getCartProductPrices();
		int total = 0;
		for (String price : prices) {
			total += Integer.parseInt(price);
		}
		return total;
	}

	public void waitForCartToBeEmpty() {
		try {
			WaitUtils.waitForElementToDisappear(driver, cartItemsLocator);
		} catch (Exception e) {

		}
	}

	public void navigateToHome() {
		homeLink.click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='card-block']/h4/a")));

	}

	public void waitForCartItemCountToDecrease(int previousCount) {
		new WebDriverWait(driver, Duration.ofSeconds(10)).until((WebDriver d) -> getCartItemCount() < previousCount);
	}

	public void waitForProductRemoval() {

		new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.invisibilityOfElementLocated(
				By.xpath("//div[contains(@class,'loading') or contains(@class,'progress')]")));
	}

	public void waitForCartItemCount(int expectedCount) {
		WaitUtils.waitForCondition(driver, d -> getCartItemCount() == expectedCount);
	}

	public void waitForCartItemsToLoad() {
		new WebDriverWait(driver, Duration.ofSeconds(10)).until((WebDriver d) -> {
			int count = getCartItemCount();
			return count >= 0;
		});
	}

	public void handleAlert() throws TimeoutException {
		Alert alert = new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.alertIsPresent());
		alert.accept();
	}

	public String getAlertMessage() {
		wait.until(ExpectedConditions.alertIsPresent());
		String alertText = driver.switchTo().alert().getText();
		driver.switchTo().alert().accept();
		return alertText;
	}

	public List<String> getCartProductNames() {
		List<String> productNames = new ArrayList<>();
		List<WebElement> rows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//tbody/tr")));

		for (WebElement row : rows) {
			String title = row.findElement(By.xpath("./td[2]")).getText();
			productNames.add(title);
		}
		return productNames;
	}

	public int deleteAllProductsFromCart() {
		int deletedCount = 0;
		try {
			List<WebElement> deleteLinks = driver.findElements(By.xpath("//a[contains(text(),'Delete')]"));

			for (int i = deleteLinks.size() - 1; i >= 0; i--) {
				try {
					List<WebElement> currentDeleteLinks = driver
							.findElements(By.xpath("//a[contains(text(),'Delete')]"));
					if (i < currentDeleteLinks.size()) {
						WebElement deleteLink = currentDeleteLinks.get(i);
						JavascriptExecutor js = (JavascriptExecutor) driver;
						js.executeScript("arguments[0].click();", deleteLink);

						WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
						wait.until(ExpectedConditions.numberOfElementsToBeLessThan(
								By.xpath("//tr[contains(@class,'success')]"), getCartItemCount() + 1));

						deletedCount++;
						System.out.println("Deleted product " + (deletedCount) + " from cart");
					}
				} catch (Exception e) {
					System.err.println("Failed to delete product: " + e.getMessage());
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to delete products from cart", e);
		}
		return deletedCount;
	}

	public void deleteProductFromCart(String productName) {
		try {
			WebElement deleteLink = driver.findElement(By.xpath(
					"//td[contains(text(),'" + productName + "')]/following-sibling::td/a[contains(text(),'Delete')]"));

			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", deleteLink);

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			wait.until(ExpectedConditions.invisibilityOfElementWithText(
					By.xpath("//td[contains(text(),'" + productName + "')]"), productName));

			System.out.println("✓ Deleted product: " + productName);
		} catch (Exception e) {
			throw new RuntimeException("Failed to delete product: " + productName, e);
		}
	}
}