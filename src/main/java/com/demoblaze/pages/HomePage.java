package com.demoblaze.pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.demoblaze.utils.WaitUtils;

import java.time.Duration;
import java.util.List;

public class HomePage {
	WebDriver driver;
	WebDriverWait wait;

	@FindBy(id = "nava")
	WebElement homeLink;

	@FindBy(xpath = "//a[contains(text(),'Phones')]")
	WebElement phonesCategory;

	@FindBy(xpath = "//a[contains(text(),'Laptops')]")
	WebElement laptopsCategory;

	@FindBy(xpath = "//a[contains(text(),'Monitors')]")
	WebElement monitorsCategory;

	@FindBy(xpath = "//a[contains(text(),'Cart')]")
	WebElement cartLink;

	@FindBy(xpath = "//a[contains(text(),'Contact')]")
	WebElement contactLink;

	@FindBy(className = "card-title")
	List<WebElement> productTitles;

	@FindBy(className = "card-block")
	List<WebElement> productCards;

	@FindBy(id = "recipient-email")
	WebElement contactEmailField;

	@FindBy(id = "recipient-name")
	WebElement contactNameField;

	@FindBy(id = "message-text")
	WebElement contactMessageField;

	@FindBy(xpath = "//h5[text()='New message']")
	WebElement contactModalTitle;

	@FindBy(xpath = "//button[contains(text(),'Close')]")
	WebElement closeContactModalButton;

	@FindBy(id = "login2")
	WebElement loginLink;

	@FindBy(id = "nameofuser")
	WebElement welcomeUserText;

	@FindBy(id = "logout2")
	WebElement logoutLink;

	@FindBy(id = "loginusername")
	WebElement loginUsernameField;

	@FindBy(id = "loginpassword")
	WebElement loginPasswordField;

	@FindBy(xpath = "//button[contains(text(),'Log in')]")
	WebElement loginButton;

	@FindBy(id = "logInModal")
	WebElement loginModal;

	@FindBy(xpath = "//div[@id='navbarExample']//a[contains(text(),'Home')]")
	WebElement navbarHomeLink;

	@FindBy(className = "card")
	List<WebElement> productCardsGrid;

	@FindBy(id = "cat")
	WebElement categoriesSection;

	public HomePage(WebDriver driver) {
		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		PageFactory.initElements(driver, this);
	}

	public void navigateToHome() {
		driver.get("https://www.demoblaze.com/");
		waitForPageLoad();
	}

	public void clickHomeLink() {
		wait.until(ExpectedConditions.elementToBeClickable(homeLink)).click();
		waitForPageLoad();
	}

	public void clickCategory(String categoryName) {
		WebElement categoryElement = getCategoryElement(categoryName);
		wait.until(ExpectedConditions.elementToBeClickable(categoryElement)).click();
		waitForCategoryPageLoad(categoryName);
	}

	public WebElement getCategoryElement(String categoryName) {
		switch (categoryName.toLowerCase()) {
		case "phones":
			return phonesCategory;
		case "laptops":
			return laptopsCategory;
		case "monitors":
			return monitorsCategory;
		default:
			throw new IllegalArgumentException("Invalid category: " + categoryName);
		}
	}

	public void clickCartLink() {
		wait.until(ExpectedConditions.elementToBeClickable(cartLink)).click();
		waitForPageLoad();
	}

	public void clickContactLink() {
		wait.until(ExpectedConditions.elementToBeClickable(contactLink)).click();
		wait.until(ExpectedConditions.visibilityOf(contactModalTitle));
	}

	public void clickNavbarLink(String linkText) {
		try {
			WebElement navbarLink = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//div[@id='navbarExample']//a[contains(text(),'" + linkText + "')]")));

			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", navbarLink);

			waitForPageLoad();
			System.out.println("Navigated to: " + linkText);

		} catch (Exception e) {
			throw new RuntimeException("Failed to click navbar link: " + linkText, e);
		}
	}

	public void clickProduct(String productName) {
		WebElement product = findProductByName(productName);
		wait.until(ExpectedConditions.elementToBeClickable(product)).click();
		waitForProductDetailPage();
	}

	private WebElement findProductByName(String productName) {
		for (WebElement productTitle : productTitles) {
			if (productTitle.getText().equalsIgnoreCase(productName)) {
				return productTitle;
			}
		}
		throw new RuntimeException("Product not found: " + productName);
	}

	public void addProductToCart(String productName) {
		clickProduct(productName);
		clickAddToCartButton();
	}

	private void clickAddToCartButton() {
		WebElement addToCartBtn = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(),'Add to cart')]")));

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", addToCartBtn);
		handleAlert();
	}

	public void handleAlert() {
		try {
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			alert.accept();
			System.out.println("Alert handled: " + alertText);
		} catch (Exception e) {
			System.err.println("No alert present or failed to handle alert: " + e.getMessage());
		}
	}

	public String handleAlertAndGetText() {
		try {
			WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(5));
			alertWait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			alert.accept();

			return alertText;
		} catch (Exception e) {
			throw new RuntimeException("Alert was not present within timeout period", e);
		}
	}

	public boolean isHomepageDisplayed() {
		try {
			return driver.getCurrentUrl().contains("index.html") || driver.getTitle().contains("STORE")
					|| homeLink.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public boolean areAllProductsDisplayed() {
		try {
			wait.until(ExpectedConditions.visibilityOfAllElements(productTitles));
			return productTitles.size() > 0 && categoriesSection.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public boolean areCategoryProductsDisplayed(String category) {
		try {
			wait.until(ExpectedConditions.visibilityOfAllElements(productTitles));
			return productTitles.size() > 0
					&& driver.findElement(By.xpath("//a[contains(text(),'" + category + "')]")).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isProductDetailDisplayed() {
		try {
			return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[@class='name']")))
					.isDisplayed()
					&& wait.until(
							ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[@class='price-container']")))
							.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public int getProductCount() {
		try {
			wait.until(ExpectedConditions.visibilityOfAllElements(productTitles));
			return productTitles.size();
		} catch (Exception e) {
			return 0;
		}
	}

	public void clickLogoutLink() {
		try {
			WaitUtils.waitForElementClickable(driver, logoutLink).click();
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.visibilityOf(loginLink));

			System.out.println("Logout completed successfully");
		} catch (Exception e) {
			throw new RuntimeException("Failed to logout", e);
		}
	}

	public boolean hasProductImage() {
		try {
			return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//img[@class='img-fluid']")))
					.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public boolean hasProductDescription() {
		try {
			WebElement description = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='more-information']//p")));
			return description.isDisplayed() && !description.getText().trim().isEmpty();
		} catch (Exception e) {
			return false;
		}
	}

	public boolean hasProductPrice() {
		try {
			WebElement price = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[@class='price-container']")));
			return price.isDisplayed() && price.getText().contains("$");
		} catch (Exception e) {
			return false;
		}
	}

	public void clickLoginLink() {
		try {
			WaitUtils.waitForElementClickable(driver, loginLink).click();
			waitForLoginModalToLoad();
		} catch (Exception e) {
			throw new RuntimeException("Failed to click login link", e);
		}
	}

	private void waitForLoginModalToLoad() {
		WaitUtils.waitForElementVisible(driver, loginModal);
	}

	public boolean isUserLoggedOut() {
		try {
			return WaitUtils.waitForElementVisible(driver, loginLink).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public void login(String username, String password) {
		clickLoginLink();
		enterLoginUsername(username);
		enterLoginPassword(password);
		clickLoginButton();

	}

	public void enterLoginUsername(String username) {
		try {
			WaitUtils.waitForElementVisible(driver, loginUsernameField).clear();
			loginUsernameField.sendKeys(username);
		} catch (Exception e) {
			throw new RuntimeException("Failed to enter login username", e);
		}
	}

	public void enterLoginPassword(String password) {
		try {
			WaitUtils.waitForElementVisible(driver, loginPasswordField).clear();
			loginPasswordField.sendKeys(password);
		} catch (Exception e) {
			throw new RuntimeException("Failed to enter login password", e);
		}
	}

	public void clickLoginButton() {
		try {
			WaitUtils.waitForElementClickable(driver, loginButton).click();
		} catch (Exception e) {
			throw new RuntimeException("Failed to click login button", e);
		}
	}

	public boolean isLoginModalDisplayed() {
		try {
			return WaitUtils.waitForElementVisible(driver, loginModal).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isContactModalDisplayed() {
		return wait.until(ExpectedConditions.visibilityOf(contactModalTitle)).isDisplayed();
	}

	public boolean isContactEmailFieldDisplayed() {
		return contactEmailField.isDisplayed();
	}

	public boolean isContactNameFieldDisplayed() {
		return contactNameField.isDisplayed();
	}

	public boolean isContactMessageFieldDisplayed() {
		return contactMessageField.isDisplayed();
	}

	public void closeContactModal() {
		wait.until(ExpectedConditions.elementToBeClickable(closeContactModalButton)).click();
	}

	private void waitForPageLoad() {
		try {
			wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
		} catch (Exception e) {
			System.out.println("Page load wait completed with possible timeout");
		}
	}

	private void waitForCategoryPageLoad(String category) {
		try {
			wait.until(ExpectedConditions.visibilityOfAllElements(productTitles));
			System.out.println(category + " category loaded with " + productTitles.size() + " products");
		} catch (Exception e) {
			System.err.println("Category page didn't load properly: " + e.getMessage());
		}
	}

	private void waitForProductDetailPage() {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Add to cart')]")));
		} catch (Exception e) {
			throw new RuntimeException("Product detail page didn't load", e);
		}
	}

	public void clickProductDynamic(String productName) {
		try {
			String xpath = "//a[contains(text(),'" + productName + "')]";
			WebElement product = WaitUtils.waitForElementClickable(driver, By.xpath(xpath));

			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", product);

		} catch (Exception e) {
			throw new RuntimeException("Failed to click product using dynamic locator: " + productName, e);
		}
	}

	public void addProductToCartDynamic(String productName) {
		try {
			if (productName == null || productName.isEmpty()) {
				throw new IllegalArgumentException("Product name cannot be null or empty");
			}

			clickProductDynamic(productName);
			waitForProductDetailPage();

			WebElement addToCartBtn = WaitUtils.waitForElementClickable(driver,
					By.xpath("//a[contains(text(),'Add to cart')]"));

			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", addToCartBtn);

			handleAlert();

		} catch (Exception e) {
			throw new RuntimeException("Failed to add product to cart: " + productName, e);
		}
	}
}