package com.demoblaze.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static boolean isInitialized = false;

    
  
    public static void initializeReport() {
        if (!isInitialized) {
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            String reportPath = "C:\\Users\\91986\\eclipse-workspace\\Online_Shopping_Application\\src\\main\\java\\extent_results\\demoblaze_complete_report_" + timeStamp + ".html";
            
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath)
                    .viewConfigurer()
                    .viewOrder()
                    .as(new ViewName[] { 
                        ViewName.DASHBOARD, 
                        ViewName.TEST, 
                        ViewName.CATEGORY, 
                        ViewName.AUTHOR,
                        ViewName.DEVICE, 
                        ViewName.EXCEPTION 
                    })
                    .apply();

            sparkReporter.config().setDocumentTitle("Online_Shopping_Application");
            sparkReporter.config().setReportName("Automation Test Results");
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setEncoding("utf-8");
            sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

           
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("OS Version", System.getProperty("os.version"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("Application", "DemoBlaze Online Shopping");
            extent.setSystemInfo("Testing Framework", "TestNG");
            
            isInitialized = true;
            System.out.println("Extent Report initialized for complete project");
        }
    }

    public static ExtentReports getExtent() {
        if (!isInitialized) {
            initializeReport();
        }
        return extent;
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void setTest(ExtentTest extentTest) {
        test.set(extentTest);
    }
    
   
    public static ExtentTest createTest(String testName, String description) {
        ExtentTest extentTest = getExtent().createTest(testName, description);
        setTest(extentTest);
        return extentTest;
    }
    
   
    public static ExtentTest createTest(String testName, String description, String category, String author) {
        ExtentTest extentTest = createTest(testName, description);
        if (category != null && !category.isEmpty()) {
            extentTest.assignCategory(category);
        }
        if (author != null && !author.isEmpty()) {
            extentTest.assignAuthor(author);
        }
        return extentTest;
    }

   
    public static void flushReport() {
        if (extent != null) {
            extent.flush();
            System.out.println("Complete Extent Report flushed successfully");
        }
    }
    
   
    public static void removeTest() {
        test.remove();
    }
    
   
    public static void logInfo(String message) {
        if (getTest() != null) {
            getTest().info(message);
        }
    }
    
    public static void logPass(String message) {
        if (getTest() != null) {
            getTest().pass(message);
        }
    }
    
    public static void logFail(String message) {
        if (getTest() != null) {
            getTest().fail(message);
        }
    }
    
    public static void logWarning(String message) {
        if (getTest() != null) {
            getTest().warning(message);
        }
    }
    
    public static void logSkip(String message) {
        if (getTest() != null) {
            getTest().skip(message);
        }
    }
}