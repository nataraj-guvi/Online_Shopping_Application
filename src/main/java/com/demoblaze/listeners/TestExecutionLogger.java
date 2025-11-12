package com.demoblaze.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestExecutionLogger implements ITestListener {
    private static final Logger logger = LogManager.getLogger(TestExecutionLogger.class);

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("Test STARTED: {}", result.getName());
        logger.debug("Test Method: {}", result.getMethod().getMethodName());
        logger.debug("Test Parameters: {}", result.getParameters());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info(" Test PASSED: {}", result.getName());
        logger.info("Execution Time: {} ms", (result.getEndMillis() - result.getStartMillis()));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error(" Test FAILED: {}", result.getName());
        logger.error("Failure Cause: {}", result.getThrowable().getMessage());
        logger.debug("Stack Trace:", result.getThrowable());
    }


    @Override
    public void onStart(ITestContext context) {
    	logger.info("Test Suite STARTED: {}", context.getName());
        logger.info("Total Tests: {}", context.getAllTestMethods().length);
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Test Suite COMPLETED: {}", context.getName());
        logger.info("Passed: {}, Failed: {}, Skipped: {}", 
                   context.getPassedTests().size(),
                   context.getFailedTests().size(),
                   context.getSkippedTests().size());
        logger.info("Total execution time: {} ms", (System.currentTimeMillis() - context.getStartDate().getTime()));
    }
}