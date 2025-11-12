package com.demoblaze.testdata;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ExcelDataProvider {
    public static final String TEST_DATA_FILE = "C:\\Users\\91986\\eclipse-workspace\\Online_Shopping_Application\\src\\test\\resources\\DemoBlaze.xlsx";
    
    @BeforeMethod
    public static void checkExcelSetup() {
        System.out.println("Excel file available: " + ExcelDataProvider.isExcelFileAvailable());
        System.out.println("Available sheets: " + ExcelDataProvider.getAvailableSheets());
    }
    
    // Login Data Providers
    @DataProvider(name = "excelLoginData")
    public static Object[][] excelLoginData(Method method) {
        return getExcelData(method);
    }
    
    @DataProvider(name = "excelIncorrectPasswordLoginData")
    public static Object[][] excelIncorrectPasswordLoginData(Method method) {
        return getExcelData(method);
    }
    
    @DataProvider(name = "excelEmptyFieldsLoginData")
    public static Object[][] excelEmptyFieldsLoginData(Method method) {
        return getExcelData(method);
    }
    
    @DataProvider(name = "excelInvalidEmailLoginData")
    public static Object[][] excelInvalidEmailLoginData(Method method) {
        return getExcelData(method);
    }   
    
    @DataProvider(name = "excelPasswordMaskingLoginData")
    public static Object[][] excelPasswordMaskingLoginData(Method method) {
        return getExcelData(method);
    }
    
    // Signup Data Provider
    @DataProvider(name = "signupdata")
    public static Object[][] getsignupdataData(Method method) {
        return getExcelData(method);
    }
    
//    // Cart Data Provider
    @DataProvider(name = "cartFlowData")
    public static Object[][] getCartFlowData() {
        List<Object[]> flowData = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        File excelFile = new File(TEST_DATA_FILE);
        if (!excelFile.exists()) {
            System.err.println("Excel file not found: " + TEST_DATA_FILE);
            return getFallbackCartFlowData();
        }

        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("Products");
            if (sheet == null) {
                System.err.println("Products sheet not found");
                return getFallbackCartFlowData();
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null && !isRowEmpty(row)) {
                    String category = formatter.formatCellValue(row.getCell(0)).trim();
                    String productName = formatter.formatCellValue(row.getCell(1)).trim();
                    String action = formatter.formatCellValue(row.getCell(2)).trim();
                    
                    if (!category.isEmpty() && !productName.isEmpty() && !action.isEmpty()) {
                        flowData.add(new Object[]{category, productName, action});
                        System.out.println("Loaded cart step: " + category + " - " + productName + " - " + action);
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error reading cart flow data: " + e.getMessage());
            return getFallbackCartFlowData();
        }

        return flowData.toArray(new Object[0][]);
    }


    @DataProvider(name = "orderFlowData")
    public static Object[][] getOrderFlowData() {
        List<Object[]> flowData = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        File excelFile = new File(TEST_DATA_FILE);
        if (!excelFile.exists()) {
            System.err.println("Excel file not found: " + TEST_DATA_FILE);
            return getFallbackOrderFlowData();
        }

        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("OrderFlow");
            if (sheet == null) {
                System.err.println("OrderFlow sheet not found");
                return getFallbackOrderFlowData();
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null && !isRowEmpty(row)) {
                    String category = formatter.formatCellValue(row.getCell(0)).trim();
                    String productName = formatter.formatCellValue(row.getCell(1)).trim();
                    String action = formatter.formatCellValue(row.getCell(2)).trim();
                    
                    if (!category.isEmpty() && !productName.isEmpty() && !action.isEmpty()) {
                        flowData.add(new Object[]{category, productName, action});
                        System.out.println("Loaded order flow step: " + category + " - " + productName + " - " + action);
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error reading order flow data: " + e.getMessage());
            return getFallbackOrderFlowData();
        }

        return flowData.toArray(new Object[0][]);
    }
    
  
    public static Map<String, String> getOrderDetails() {
        Map<String, String> orderDetails = new HashMap<>();
        DataFormatter formatter = new DataFormatter();

        File excelFile = new File(TEST_DATA_FILE);
        if (!excelFile.exists()) {
            System.err.println("Excel file not found: " + TEST_DATA_FILE);
            return getFallbackOrderDetails();
        }

        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("OrderDetails");
            if (sheet == null) {
                System.err.println("OrderDetails sheet not found");
                return getFallbackOrderDetails();
            }

            // Read first data row
            Row row = sheet.getRow(1);
            if (row != null && !isRowEmpty(row)) {
                orderDetails.put("customerName", formatter.formatCellValue(row.getCell(0)).trim());
                orderDetails.put("country", formatter.formatCellValue(row.getCell(1)).trim());
                orderDetails.put("city", formatter.formatCellValue(row.getCell(2)).trim());
                orderDetails.put("creditCard", formatter.formatCellValue(row.getCell(3)).trim());
                orderDetails.put("month", formatter.formatCellValue(row.getCell(4)).trim());
                orderDetails.put("year", formatter.formatCellValue(row.getCell(5)).trim());
            }

        } catch (Exception e) {
            System.err.println("Error reading order details: " + e.getMessage());
            return getFallbackOrderDetails();
        }

        return orderDetails;
    }
    
    private static Object[][] getFallbackOrderFlowData() {
        return new Object[][]{
            {"Phones", "Samsung galaxy s6", "Add"},
            {"Phones", "Nexus 6", "Add"},
            {"Laptops", "Sony vaio i5", "Add"}
        };
    }
    
    private static Map<String, String> getFallbackOrderDetails() {
        Map<String, String> details = new HashMap<>();
        details.put("customerName", "Test User");
        details.put("country", "United States");
        details.put("city", "New York");
        details.put("creditCard", "4111111111111111");
        details.put("month", "12");
        details.put("year", "2025");
        return details;
    }
//     
    
    @DataProvider(name = "logoutData")
    public static Object[][] getLogoutData(Method method) {
        return getExcelDataAsMap(method);
    }
    
  
    @DataProvider(name = "productsByCategory")
    public static Object[][] getProductsByCategory(Method method) {
        String testName = method.getName();
        String category = extractCategoryFromTestName(testName);
        if (category == null) {
            category = "Phones"; // default fallback
        }
        return getExcelData(method);
    }


    public static Object[][] getExcelDataAsMap(Method method) {
        String testName = method.getName();
        String className = method.getDeclaringClass().getSimpleName();
        
        System.out.println("Loading Excel data as Map for: " + testName + " in class: " + className);

        Map<String, String> methodToSheetMap = createMethodToSheetMap();
        String sheetName = methodToSheetMap.getOrDefault(testName, testName);

        System.out.println("Looking for sheet: " + sheetName);

        File excelFile = new File(TEST_DATA_FILE);
        if (!excelFile.exists()) {
            System.err.println("Excel file not found at: " + excelFile.getAbsolutePath());
            return getFallbackMapData(testName);
        }

        try (FileInputStream fis = new FileInputStream(excelFile); 
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                System.err.println("Sheet '" + sheetName + "' not found.");
                return getFallbackMapData(testName);
            }

            return readSheetDataAsMap(sheet, testName);

        } catch (Exception e) {
            System.err.println("Error reading Excel file for test: " + testName);
            e.printStackTrace();
            return getFallbackMapData(testName);
        }
    }

    // Method to read Excel data and return as Map
    private static Object[][] readSheetDataAsMap(Sheet sheet, String testName) {
        List<Map<String, String>> dataList = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        if (sheet.getRow(0) == null) {
            throw new RuntimeException("Header row not found in sheet");
        }

        System.out.println("Reading data as Map from sheet with " + sheet.getLastRowNum() + " rows");

        // Read header row
        Row headerRow = sheet.getRow(0);
        List<String> headers = new ArrayList<>();
        for (int j = 0; j < headerRow.getLastCellNum(); j++) {
            Cell cell = headerRow.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            String headerValue = formatter.formatCellValue(cell).trim();
            if (!headerValue.isEmpty()) {
                headers.add(headerValue);
            }
        }

        // Read data rows
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null && !isRowEmpty(row)) {
                Map<String, String> rowData = new HashMap<>();

                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String cellValue = formatter.formatCellValue(cell).trim();
                    
                    // Handle unique data generation for specific tests
                    if (testName.equals("testSignupWithValidDetails")) {
                        String uniqueId = UUID.randomUUID().toString().substring(0, 5);
                        if (j == 0 && headers.get(j).equalsIgnoreCase("username")) { 
                            cellValue = "Nataraj" + uniqueId;
                        } else if (j == 1 && headers.get(j).equalsIgnoreCase("password")) {   
                            cellValue = "12345" + uniqueId;
                        }
                    }
                    
                    rowData.put(headers.get(j), cellValue);
                }

                System.out.println("Row " + i + " as Map: " + rowData);

                if (hasMeaningfulData(new ArrayList<>(rowData.values()))) {
                    dataList.add(rowData);
                }
            }
        }

        if (dataList.isEmpty()) {
            throw new RuntimeException("No test data found for: " + testName);
        }

        // Convert List<Map> to Object[][]
        Object[][] result = new Object[dataList.size()][1];
        for (int i = 0; i < dataList.size(); i++) {
            result[i][0] = dataList.get(i);
        }

        return result;
    }

    
    private static Map<String, String> createMethodToSheetMap() {
        Map<String, String> map = new HashMap<>();

        // Login Tests
        map.put("testValidLogin", "ValidLogin");
        map.put("testInvalidPassword", "IncorrectPassword");
        map.put("testEmptyFields", "EmptyFields");
        map.put("testInvalidEmailFormat", "InvalidEmail");
        map.put("testPasswordFieldMasksInput", "PasswordMasking");

        // Signup Test
        map.put("testSignupWithValidDetails", "Signup");

//        // Cart Tests
      map.put("testProductToCart", "ValidLogin");
        // Order Tests
        map.put("testPlaceOrderWithValidDetails", "ValidLogin");
        map.put("testPlaceOrderWithEmptyForm", "ValidLogin");
        map.put("testCompleteOrderAndNavigationFlow", "ValidLogin");

        // Logout Tests
        map.put("testLogoutAfterLogin", "Logout");
//        map.put("testLogoutFromDifferentPages", "Logout");

        

        return map;
    }

 
    public static Object[][] getExcelData(Method method) {
        
        String testName = method.getName();
        String className = method.getDeclaringClass().getSimpleName();
        checkExcelSetup();
        System.out.println(" Loading Excel data for: " + testName + " in class: " + className);

        Map<String, String> methodToSheetMap = createMethodToSheetMap();
        String sheetName = methodToSheetMap.getOrDefault(testName, testName);

        System.out.println(" Looking for sheet: " + sheetName);

        File excelFile = new File(TEST_DATA_FILE);
        if (!excelFile.exists()) {
            System.err.println(" Excel file not found at: " + excelFile.getAbsolutePath());
            return getFallbackData(testName);
        }

        try (FileInputStream fis = new FileInputStream(excelFile); Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                System.err.println(" Sheet '" + sheetName + "' not found. Available sheets:");
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    System.err.println(" - " + workbook.getSheetName(i));
                }
                return getFallbackData(testName);
            }

            Object[][] data = readSheetData(sheet, testName);
            return data;

        } catch (Exception e) {
            System.err.println(" Error reading Excel file for test: " + testName);
            e.printStackTrace();
            return getFallbackData(testName);
        }
    }

    
    private static Object[][] getFallbackMapData(String testName) {
        System.out.println("Using fallback Map data for: " + testName);

        switch (testName) {
            case "testCompleteOrderFlow":
            case "testOrderConfirmationDetails":
            case "testPlaceOrderWithValidDetails":
                return new Object[][] {{
                    createOrderDataMap("John Doe", "United States", "New York", 
                                    "4111111111111111", "12", "2025", "Samsung galaxy s6", 
                                    "Thank you for your purchase", "Prompt to fill required fields should be shown")
                }};
            case "testProductBrowsingAndNavigation":
            case "testBrowseProductCategories":
            case "testViewProductDetails":
                return new Object[][] {{
                    createProductDataMap("Phones,Laptops,Monitors", "Samsung galaxy s6")
                }};
            case "testHomeNavigation":
            case "testNavbarNavigation":
                return new Object[][] {{
                    createNavigationDataMap("Home", "Cart", "Cart page displayed")
                }};
            default:
                return new Object[][] {{
                    createDefaultMap()
                }};
        }
    }

    private static Map<String, String> createOrderDataMap(String name, String country, String city, 
                                                         String card, String month, String year, 
                                                         String product, String thankYouMsg, String validationMsg) {
        Map<String, String> map = new HashMap<>();
        map.put("customerName", name);
        map.put("country", country);
        map.put("city", city);
        map.put("creditCard", card);
        map.put("month", month);
        map.put("year", year);
        map.put("productName", product);
        map.put("expectedThankYouMessage", thankYouMsg);
        map.put("emptyFormValidationMessage", validationMsg);
        return map;
    }

    private static Map<String, String> createProductDataMap(String categories, String productName) {
        Map<String, String> map = new HashMap<>();
        map.put("categories", categories);
        map.put("productName", productName);
        return map;
    }

    private static Map<String, String> createNavigationDataMap(String fromPage, String toPage, String expectedResult) {
        Map<String, String> map = new HashMap<>();
        map.put("fromPage", fromPage);
        map.put("toPage", toPage);
        map.put("expectedResult", expectedResult);
        return map;
    }

    private static Map<String, String> createDefaultMap() {
        Map<String, String> map = new HashMap<>();
        map.put("defaultKey", "defaultValue");
        return map;
    }


    private static String extractCategoryFromTestName(String testName) {
        if (testName.contains("Phone")) return "Phones";
        if (testName.contains("Laptop")) return "Laptops";
        if (testName.contains("Monitor")) return "Monitors";
        return null;
    }

    public static Object[][] readSheetData(Sheet sheet, String testName) {
        List<Object[]> data = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        if (sheet.getRow(0) == null) {
            throw new RuntimeException("Header row not found in sheet");
        }

        System.out.println("Reading data from sheet with " + sheet.getLastRowNum() + " rows");

        int dataColumns = getActualDataColumnCount(sheet);
        System.out.println("Data columns: " + dataColumns);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null && !isRowEmpty(row)) {
                Object[] rowData = new Object[dataColumns];

                for (int j = 0; j < dataColumns; j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String cellValue = formatter.formatCellValue(cell).trim();
                    
                    if (testName.equals("testSignupWithValidDetails")) {
                        String uniqueId = UUID.randomUUID().toString().substring(0, 5);
                        String username = "Nataraj" + uniqueId;
                        String password = "12345" + uniqueId;
                        if (j == 0) { 
                            cellValue = username;
                        } else if (j == 1) {   
                            cellValue = password;
                        }
                    }
                    rowData[j] = cellValue;
                }

                System.out.println("Row " + i + ": " + Arrays.toString(rowData));

                if (hasMeaningfulData(Arrays.asList(rowData))) {
                    data.add(rowData);
                }
            }
        }

        if (data.isEmpty()) {
            throw new RuntimeException("No test data found for: " + testName);
        }

        return data.toArray(new Object[0][]);
    }

    private static int getActualDataColumnCount(Sheet sheet) {
        int maxColumns = 0;
        DataFormatter formatter = new DataFormatter();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    if (!formatter.formatCellValue(cell).trim().isEmpty()) {
                        maxColumns = Math.max(maxColumns, j + 1);
                    }
                }
            }
        }

        return maxColumns == 0 ? (sheet.getRow(0) != null ? sheet.getRow(0).getLastCellNum() : 0) : maxColumns;
    }

    private static boolean hasMeaningfulData(List<Object> rowData) {
        for (Object value : rowData) {
            if (value != null && !value.toString().trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private static boolean isRowEmpty(Row row) {
        if (row == null) return true;

        for (int cellNum = 0; cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String cellValue = new DataFormatter().formatCellValue(cell).trim();
                if (!cellValue.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isExcelFileAvailable() {
        File excelFile = new File(TEST_DATA_FILE);
        return excelFile.exists();
    }

    public static List<String> getAvailableSheets() {
        List<String> sheets = new ArrayList<>();
        File excelFile = new File(TEST_DATA_FILE);

        if (!excelFile.exists()) {
            return sheets;
        }
        try (FileInputStream fis = new FileInputStream(excelFile); Workbook workbook = new XSSFWorkbook(fis)) {

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                sheets.add(workbook.getSheetName(i));
            }
        } catch (Exception e) {
            System.err.println("Error reading sheets from Excel file");
        }

        return sheets;
    }

    private static Object[][] getFallbackData(String testName) {
        System.out.println(" Using fallback data for: " + testName);

        switch (testName) {
        case "testValidLogin":
            return new Object[][] { { "valid.username", "valid.password" } };
        case "testInvalidPassword":
            return new Object[][] { { "valid.email", "wrong.password", "false" } };
        case "testInvalidEmailFormat":
            return new Object[][] { { "invalid.email", "valid.password", "false" } };
        case "testPasswordFieldMasksInput":
            return new Object[][] { { "valid.email", "valid.password", "true" } };
        default:
            return new Object[][] { { "default.email", "default.password", "false" } };
        }
    }

    private static Object[][] getFallbackCartFlowData() {
        return new Object[][]{
            {"Phones", "Iphone 6 32gb", "Add"},
            {"Laptops", "MacBook Pro", "Add"},
            {"Monitors", "Apple monitor 24", "Add"},
            {"Phones", "Nokia lumia 1520", "Add"},
            {"Phones", "Nokia lumia 1520", "Remove"}
        };
    }

    public static Object[][] getFallbackAllProducts() {
        System.out.println("Using fallback all products data");
        return new Object[][] {
            {"Phones", "Samsung galaxy s6", "360"},
            {"Phones", "Nokia lumia 1520", "820"},
            {"Phones", "Nexus 6", "650"},
            {"Phones", "Samsung galaxy s7", "800"},
            {"Phones", "Iphone 6 32gb", "790"},
            {"Phones", "Sony xperia z5", "320"}
        };
    }
}