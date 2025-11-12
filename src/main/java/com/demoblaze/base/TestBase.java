package com.demoblaze.base;

import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import com.demoblaze.reports.ExtentManager;
import com.demoblaze.utils.ConfigReader;

import io.github.bonigarcia.wdm.WebDriverManager;

public abstract class TestBase { 
    protected ExtentTest test;
    protected ExtentReports extent;
    protected static final Logger logger = LogManager.getLogger(TestBase.class);
    protected WebDriver driver;
    protected ConfigReader config;
    protected WebDriverWait wait;

    @BeforeSuite
    public void suiteSetup() {
        
        ExtentManager.initializeReport();
        logger.info("Test Suite Started - Extent Report Initialized");
    }


    @Parameters("browser")
    @BeforeMethod
    public void setUp(@Optional("chrome") String browser, ITestResult result) {
    	 String methodName = result.getMethod().getMethodName();
         String className = this.getClass().getSimpleName();
         test = ExtentManager.createTest(className + " - " + methodName, "Automation test for " + methodName);
         logger.info("Extent test created for: {} - {}", className, methodName);
        config = new ConfigReader();

        String actualBrowser = browser != null ? browser : config.getProperty("browser", "chrome");
        ThreadContext.put("browser", actualBrowser.toUpperCase());
        ThreadContext.put("thread", Thread.currentThread().getName());

        logger.info("========== Test Setup Started ==========");
        logger.info("Initializing browser: {}", actualBrowser);
        logger.info("Thread: {}", Thread.currentThread().getName());
        initializeDriver(actualBrowser);
        logger.info("Browser initialized successfully");

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.manage().window().maximize();
        logger.debug("Browser window maximized");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        logger.debug("Implicit wait set to 10 seconds");
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));

        String baseUrl = (config.getProperty("base.url"));

        logger.info("Navigating to URL: {}", baseUrl);
        driver.get(baseUrl);
        logger.info("========== Test Setup Completed ==========");
    }

    public void waitForPageLoad() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));

            try {
                wait.until(ExpectedConditions
                        .jsReturnsValue("return (typeof jQuery === 'undefined') || jQuery.active === 0"));
            } catch (Exception e) {
                System.out.println("jQuery not detected, continuing...");
            }

            System.out.println("✓ Page fully loaded");

        } catch (Exception e) {
            System.err.println("Page load wait timeout: " + e.getMessage());
        }
    }

    public void waitForElementToBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitForTextToBePresent(WebElement element, String text) {
        wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    public void waitForAlertToBePresent() {
        wait.until(ExpectedConditions.alertIsPresent());
    }

    public void waitForElementToDisappear(WebElement element) {
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    private void initializeDriver(String browser) {
        switch (browser.toLowerCase()) {
        case "chrome":
            logger.debug("Setting up ChromeDriver");
            WebDriverManager.chromedriver().setup();
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--disable-notifications");
            chromeOptions.addArguments("--disable-popup-blocking");
            chromeOptions.addArguments("--disable-infobars");
            driver = new ChromeDriver(chromeOptions);
            logger.info("ChromeDriver initialized successfully");
            break;

        case "firefox":
            logger.debug("Setting up FirefoxDriver");
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.addArguments("--disable-notifications");
            driver = new FirefoxDriver(firefoxOptions);
            logger.info("FirefoxDriver initialized successfully");
            break;
        case "edge":
            logger.debug("Setting up EdgeDriver");
            WebDriverManager.edgedriver().setup();
            EdgeOptions edgeOptions = new EdgeOptions();
            edgeOptions.addArguments("--disable-notifications");
            driver = new EdgeDriver(edgeOptions);
            logger.info("EdgeDriver initialized successfully");
            break;
        case "safari":
            logger.debug("Setting up SafariDriver");
            SafariOptions safariOptions = new SafariOptions();
            driver = new SafariDriver(safariOptions);
            logger.info("SafariDriver initialized successfully");
            break;
        default:
            logger.error("Unsupported browser: {}", browser);
            throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    public WebDriver getDriver() {
        return driver;
    }

   

    public void refreshPage() {
        driver.navigate().refresh();
    }

    public void navigateBack() {
        driver.navigate().back();
    }

    public void navigateForward() {
        driver.navigate().forward();
    }

    @AfterMethod
    public void tearDown() {
      
        ExtentManager.removeTest();
        
        if (driver != null) {
            driver.quit();
            System.out.println("Browser closed successfully");
        }
    }

    @AfterSuite
    public void suiteTearDown() {
      
        ExtentManager.flushReport();
        logger.info("Test Suite Completed - Report Generated");
    }
}