package com.demoblaze.tests;

import com.demoblaze.pages.*;
import com.demoblaze.reports.ExtentManager;
import com.aventstack.extentreports.Status;
import com.demoblaze.base.TestBase;
import com.demoblaze.testdata.ExcelDataProvider;
import com.demoblaze.utils.WaitUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
public class OrderTest extends TestBase {
	 LoginPage loginPage;
	    LogoutPage logoutPage;
	    CartPage cartPage;
	    OrderPage orderPage;
	    HomePage homePage;
	     @BeforeMethod
	    public void pageSetup() {
	        loginPage = new LoginPage(driver);
	        logoutPage = new LogoutPage();
	        cartPage = new CartPage(driver);
	        orderPage = new OrderPage(driver);
	        homePage = new HomePage(driver);
	     
	        logger.info("Page setup completed - LoginPage and OrderPage initialized");
	     
	    }
	    
	    @Test(priority = 1, dataProvider = "excelLoginData", dataProviderClass = ExcelDataProvider.class)    
public void testPlaceOrderWithValidDetails(String username, String password) throws TimeoutException {
	    	test = ExtentManager.getTest().createNode("Adding the Product to the cart and Place the order");
	        logger.info("Starting testPlaceOrderWithValidDetails with username: {}", username);
	        try {
	            logger.info("Attempting to login with username: {}", username);
	            loginPage.login(username, password);
	            logger.info("User logged in successfully");
	            Object[][] products = ExcelDataProvider.getCartFlowData();
	            logger.info("Retrieved {} products from Excel data", products.length);
	            String productToRemove = null;
	            for (Object[] product : products) {
	                try {
	                    String category = (String) product[0];
	                    String productName = (String) product[1];
	                    String action = (String) product[2];
	                    logger.debug("Processing product: {} | Category: {} | Action: {}", 
	                                productName, category, action);
	                    if ("Add".equalsIgnoreCase(action)) {
	                        logger.info("Adding product to cart: {} from category: {}", productName, category);
	                        cartPage.goToCategory(category);
	                        cartPage.addProductToCart(productName);
	                        String alertMessage = cartPage.getAlertMessage();
	                        logger.info("Alert message received: {}", alertMessage);
	                        Assert.assertTrue(alertMessage.contains("Product added"), 
	                            "Expected 'Product added' alert but got: " + alertMessage);
	                        logger.info("Product added successfully: {}", productName);
	                        cartPage.goToHome();
	                        logger.debug("Navigated back to home page");
	                        
	                    } else if ("Remove".equalsIgnoreCase(action)) {
	                        productToRemove = productName;
	                        logger.info("Product marked for removal: {}", productToRemove);
	                    }
	                } catch (Exception e) {
	                    logger.error("Error processing product: {} - Error: {}", product[1], e.getMessage(), e);
	                    throw e;
	                }
	            }
	            logger.info("Navigating to cart to verify added products");
	            cartPage.goToCart();
	            
	            List<String> cartItems = cartPage.getCartProductNames();
	            List<String> cartPrices = cartPage.getCartProductPrices();
	            
	            logger.info("Cart contains {} products:", cartItems.size());
	            for (int i = 0; i < cartItems.size(); i++) {
	                logger.info("  - {} - {}", cartItems.get(i), cartPrices.get(i));
	            }

	            int itemCount = cartPage.getCartItemCount();
	            logger.info("Cart item count: {}", itemCount);
	            Assert.assertTrue(itemCount >= 1, "Cart should have at least one product to place order");
	            logger.info("Cart verification passed");

	            if (productToRemove != null) {
	                try {
	                    logger.info("Removing product before order: {}", productToRemove);
	                    cartPage.deleteProductByName(productToRemove);
	                    logger.info("Product removed successfully: {}", productToRemove);
	                } catch (Exception e) {
	                    logger.warn("Failed to remove product {}, continuing with order", productToRemove);
	                }
	            }

	            logger.info("Starting order placement process");
	            orderPage.clickPlaceOrder();
	            Assert.assertTrue(orderPage.isOrderModalDisplayed(), "Order modal should be displayed");
	            logger.info("Order modal displayed successfully");
	            Map<String, String> orderDetails = ExcelDataProvider.getOrderDetails();
	            logger.info("Retrieved order details from Excel");
	            orderPage.completePurchase(
	                orderDetails.get("customerName"),
	                orderDetails.get("country"),
	                orderDetails.get("city"),
	                orderDetails.get("creditCard"),
	                orderDetails.get("month"),
	                orderDetails.get("year")
	            );
	            logger.info("Order form completed with customer details");
	           
	           
	            String thankYouMessage = orderPage.getThankYouMessage();
	            Assert.assertTrue(thankYouMessage.contains("Thank you for your purchase"),
	                            "Thank you message should be displayed");
	          
	            logger.info("Thank you message verified: {}", thankYouMessage);
	            String orderDetailsText = orderPage.getOrderDetails();
	            String orderId = orderPage.extractOrderId(orderDetailsText);
	            String amount = orderPage.extractAmount(orderDetailsText);

	            logger.info("Order placed successfully - Order ID: {}, Amount: {}", orderId, amount);
	            System.out.println("Order placed successfully!");
	            System.out.println("Order ID: " + orderId);
	            System.out.println("Amount: " + amount);
	            System.out.println("Thank you message: " + thankYouMessage);
	            orderPage.clickConfirmationOk();
	            logger.info("Purchase completed");
	            logger.info("Confirmation dialog closed");
	            logger.info("Closing order modal");
	          logger.info("Order modal closed successfully");
         orderPage.closeOrderModal();
	            System.out.println("Order modal closed");
	            logger.info("Initiating user logout");
	            logoutPage.clickLogout();
	            logger.info("User logged out successfully");

	            logger.info("All order scenarios completed successfully!");
	            System.out.println("All order scenarios completed successfully!");

	        } catch (AssertionError e) {
	            logger.error("Test assertion failed: {}", e.getMessage(), e);
	            throw e;
	        } catch (Exception e) {
	            logger.error("Unexpected exception in testPlaceOrderWithValidDetails: {}", e.getMessage(), e);
	            Assert.fail("Test execution failed: " + e.getMessage());
	            test.log(Status.FAIL, "Test failed with error: {} " + e.getMessage());
	        } finally {
	            logger.info("testPlaceOrderWithValidDetails2 execution completed");
	        }
	    }

	    

	    @Test(priority = 2, dataProvider = "excelLoginData", dataProviderClass = ExcelDataProvider.class)
public void testPlaceOrderWithEmptyForm(String username, String password) {
	    	test = ExtentManager.getTest().createNode("Place the Order With EmptyForm");
	 logger.info("Starting testPlaceOrderWithEmptyForm with username: {}", username);
	    try {
	 logger.info("Attempting to login with username: {}", username);
    loginPage.login(username, password);
    logger.info("User logged in successfully");
    System.out.println(" User logged in successfully");
    Object[][] orderFlowData = ExcelDataProvider.getOrderFlowData();
    logger.info("Retrieved order flow data with {} entries", orderFlowData.length);
    String testCategory = "";
    String testProduct = "";
    for (Object[] product : orderFlowData) {
    	try {
        String category = (String) product[0];
        String productName = (String) product[1];
        String action = (String) product[2];
        logger.debug("Processing product: {} | Category: {} | Action: {}", 
                productName, category, action);
        if ("Add".equalsIgnoreCase(action)) {
            testCategory = category;
            testProduct = productName;
            logger.info("Selected product for cart: {} from category: {}", testProduct, testCategory);
            break;         }
    
	    }catch (Exception e) {
        logger.error("Error processing product data: {}", e.getMessage(), e);
        throw e;
    }
    
	  logger.info("Adding product to cart: {} from category: {}", testProduct, testCategory);
    cartPage.goToCategory(testCategory);
    cartPage.addProductToCart(testProduct);
    String alertMessage = cartPage.getAlertMessage();
    logger.info("Product add alert message: {}", alertMessage);
    Assert.assertTrue(alertMessage.contains("Product added"), 
        "Expected 'Product added' alert but got: " + alertMessage);
    logger.info("Product added successfully to cart");
    System.out.println("Product added to cart: " + testProduct);
    cartPage.goToHome();
    logger.debug("Navigated back to home page");
    logger.info("Navigating to cart to verify products");
    cartPage.goToCart();
    int itemCount = cartPage.getCartItemCount();
    logger.info("Cart item count: {}", itemCount);
    Assert.assertTrue(itemCount >= 1, "Cart should have at least one product");
    logger.info("Cart verification passed - contains {} products", itemCount);
    System.out.println("Cart contains " + itemCount + " products");
    logger.info("Opening order placement modal");
    orderPage.clickPlaceOrder();
    Assert.assertTrue(orderPage.isOrderModalDisplayed(), "Order modal should be displayed");
    logger.info("Order modal opened successfully");
    System.out.println("Order modal opened successfully");
    logger.info("Verifying that all required form fields are empty");
    Assert.assertTrue(orderPage.areRequiredFieldsEmpty(), "All form fields should be empty");
    logger.info("All form fields are empty as expected");
    System.out.println("All form fields are empty as expected");
    logger.info("Attempting to place order with empty form fields");
    orderPage.clickPurchase();
    logger.info("Purchase attempt completed with empty form");
    System.out.println("Attempted purchase with empty form");
    String validationAlertMessage = "";
    try {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        logger.info("Waiting for validation alert to appear");
        wait.until(ExpectedConditions.alertIsPresent());
        Alert validationAlert = driver.switchTo().alert();
        validationAlertMessage = validationAlert.getText();
        validationAlert.accept();
        logger.info("Validation alert handled successfully: {}", validationAlertMessage);
        System.out.println("Validation alert handled: " + validationAlertMessage);
        
    } catch (Exception e) {
        Assert.fail("Expected validation alert but none appeared");
        logger.error("Expected validation alert but none appeared: {}", e.getMessage());
    }
    logger.info("Verifying validation alert message");
    Assert.assertTrue(validationAlertMessage.contains("Please fill out Name and Creditcard."),
        "Validation message should match expected. Actual: " + validationAlertMessage);
    logger.info("Validation message verified successfully: {}", validationAlertMessage);
    System.out.println("Validation prompt verified: " + validationAlertMessage);
    logger.info("Closing order modal");
    orderPage.closeOrderModal();
    logger.info("Order modal closed successfully");
    System.out.println("Order modal closed");
    logger.info("Initiating user logout");
    logoutPage.clickLogout();
    Assert.assertTrue(homePage.isUserLoggedOut(), "User should be logged out successfully");
    logger.info("User logged out successfully");
    System.out.println("User logged out successfully");
    logger.info("All empty form validation scenarios completed successfully!");
    System.out.println("All empty form validation scenarios completed successfully!");
 } 
	    }
	    catch (AssertionError e) {
    logger.error("Assertion failed in testPlaceOrderWithEmptyForm: {}", e.getMessage(), e);
    System.out.println("Assertion failed: " + e.getMessage());
    throw e;
} catch (Exception e) {
    logger.error("Unexpected exception in testPlaceOrderWithEmptyForm: {}", e.getMessage(), e);
    System.out.println(" Test failed with exception: " + e.getMessage());
    Assert.fail("Test execution failed: " + e.getMessage());
    test.log(Status.FAIL, "Test failed with error: {} " + e.getMessage());
} 
}


    @Test(priority = 3,dataProvider = "excelLoginData", dataProviderClass = ExcelDataProvider.class)
   
public void testCompleteOrderAndNavigationFlow(String username, String password) {
    	test = ExtentManager.getTest().createNode("Order And Navigation Flow");
    	        System.out.println("Testing navbar navigation...");
    	        homePage.clickNavbarLink("Home");
    	        waitForHomePageToLoad();
    	        Assert.assertTrue(homePage.isHomepageDisplayed(), "Homepage should be displayed");
    	        Assert.assertTrue(homePage.areAllProductsDisplayed(), "All products should be visible on homepage");
    	        System.out.println("Home navigation working correctly");
    	        homePage.clickNavbarLink("Cart");
    	        waitForCartPageToLoad();
    	        Assert.assertTrue(cartPage.isCartPageDisplayed(), "Cart page should be displayed");
    	        System.out.println("Cart navigation working correctly");
    	        homePage.clickNavbarLink("Contact");
    	        waitForContactModalToLoad();
    	        Assert.assertTrue(homePage.isContactModalDisplayed(), "Contact modal should be displayed");
    	        System.out.println("Contact navigation working correctly");
    	      homePage.closeContactModal();
    	        homePage.clickNavbarLink("Home");
    	        waitForHomePageToLoad();
    	        Assert.assertTrue(homePage.isHomepageDisplayed(), "Should return to homepage");
    	        System.out.println("Return to Home navigation working correctly");
    	    }
     private void waitForHomePageToLoad() {
        WaitUtils.waitForCondition(driver, d -> 
            homePage.isHomepageDisplayed() && homePage.areAllProductsDisplayed());
    }

    private void waitForCartPageToLoad() {
        WaitUtils.waitForCondition(driver, d -> 
            cartPage.isCartPageDisplayed());
    }

    private void waitForContactModalToLoad() {
        WaitUtils.waitForCondition(driver, d -> 
            homePage.isContactModalDisplayed());
    }
  }