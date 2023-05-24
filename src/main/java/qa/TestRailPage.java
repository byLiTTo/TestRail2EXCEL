package qa;

import static org.apache.poi.common.usermodel.Hyperlink.LINK_URL;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import qa.Constants.EXCEL_FIELDS;

public class TestRailPage {

    // ATTRIBUTES--> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    private static final String HEADER = "content-header";

    private WebDriver webDriver;
    private int titleIndex = 3;
    private int caseIDIndex = 5;
    //table[@id='grid-14959939']//tr[@class='header']/th[3]

    public TestRailPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    private void sleep(int timemillis) {
        try {
            Thread.sleep(timemillis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void openMainPage() {
        webDriver.navigate().to(Constants.TESTRAIL_LINK);
        webDriver.manage().window().maximize();
    }

    private List<TestRailCase> loadTestCases(List<WebElement> cases) {
        List<TestRailCase> temp = new ArrayList<>();
        for (WebElement element : cases) {
            String title = element.findElement(By.xpath("./td[" + titleIndex + "]/a")).getText();
            WebElement tmp = element.findElement(By.xpath("./td[" + caseIDIndex + "]/a"));
            String caseURL = tmp.getAttribute("href");
            String caseID = tmp.getText();
            String testStatus = element.findElement(By.xpath("./td/a[@class='dropdownLink status hidden-xs']"))
                    .getText();
            String section = element.findElement(By.xpath("./../../..//*[@class='title pull-left']")).getText();

            temp.add(new TestRailCase(title, caseURL, caseID, testStatus, section));
        }
        return temp;
    }


    public List<TestRailCase> loadFailedTestCases() {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(80));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(HEADER)));

        sleep(1000);

        return loadTestCases(webDriver.findElements(By.xpath("//td[@class='js-status']//a[text()='Failed']/../..")));
    }

    public List<TestRailCase> loadPostponedTestCases() {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(80));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(HEADER)));

        sleep(1000);

        return loadTestCases(webDriver.findElements(By.xpath("//td[@class='js-status']//a[text()='Postponed']/../..")));
    }

    public void convertTestCase2Excel(File file, List<TestRailCase> cases) {

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        XSSFWorkbook workBook = null;
        try {
            workBook = new XSSFWorkbook(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        XSSFSheet sheet = workBook.getSheetAt(0);

        // Initialize rows and columns
        int rownNum = sheet.getLastRowNum() + 1;
        int cellnum = 0;

        // Writing empty row
        Row row = sheet.createRow(rownNum++);
        for (int i = 0; i < EXCEL_FIELDS.values().length; i++) {
            row.createCell(cellnum++).setCellValue(" ");
        }

        // Writing Date row
        row = sheet.createRow(rownNum++);
        cellnum = 0;
        Cell cell = row.createCell(cellnum++);
        cell.setCellStyle(getHeaderStyle(sheet.getWorkbook().createCellStyle()));
        cell.setCellValue(DateTimeFormatter.ofPattern("MM/dd/yyyy").format(LocalDateTime.now()));
        for (int i = 1; i < EXCEL_FIELDS.values().length; i++) {
            row.createCell(cellnum++).setCellValue(" ");
        }

        // Writing Title row
        row = sheet.createRow(rownNum++);
        cellnum = 0;
        for (EXCEL_FIELDS field : EXCEL_FIELDS.values()) {
            cell = row.createCell(cellnum++);
            cell.setCellStyle(getHeaderStyle(sheet.getWorkbook().createCellStyle()));
            cell.setCellValue(field.toString());
        }

        // Writing testcases
        int rowValidation = rownNum;
        int color = 0;
        for (int i = 0; i < cases.size(); i++) {
            TestRailCase testRailCase = cases.get(i);
            color = setSectionColor(i, color, cases);
            CellStyle style = getStyle(color, sheet.getWorkbook().createCellStyle());

            Row rowTemp = sheet.createRow(rownNum++);
            int cellnumTemp = 0;
            cell = rowTemp.createCell(cellnumTemp++);
            cell.setCellStyle(style);
            cell.setCellValue(testRailCase.getAssigned());

            cell = rowTemp.createCell(cellnumTemp++);
            cell.setCellStyle(style);
            cell.setCellValue(testRailCase.getLastFailed());

            cell = rowTemp.createCell(cellnumTemp++);
            cell.setCellStyle(style);
            cell.setCellValue(testRailCase.getTitle());

            cell = rowTemp.createCell(cellnumTemp++);
            cell.setCellStyle(style);
            XSSFHyperlink link = workBook.getCreationHelper().createHyperlink(LINK_URL);
            link.setAddress(testRailCase.getCaseURL());
            cell.setHyperlink(link);
            cell.setCellValue(testRailCase.getCaseID());

            cell = rowTemp.createCell(cellnumTemp++);
            cell.setCellStyle(style);
            cell.setCellValue(testRailCase.getTestStatus());

            cell = rowTemp.createCell(cellnumTemp++);
            cell.setCellStyle(style);
            cell.setCellValue(testRailCase.getSection());

            cell = rowTemp.createCell(cellnumTemp++);
            cell.setCellStyle(style);
            cell.setCellValue(testRailCase.getDescription());

            cell = rowTemp.createCell(cellnumTemp++);
            cell.setCellStyle(style);
            cell.setCellValue(testRailCase.getSolution());

            cell = rowTemp.createCell(cellnumTemp++);
            cell.setCellStyle(style);
            cell.setCellValue(testRailCase.getSolutionLink());

            cell = rowTemp.createCell(cellnumTemp);
            cell.setCellStyle(style);
            cell.setCellValue(testRailCase.getStatus());
        }

        DataValidation dataValidation = null;
        DataValidationConstraint constraint = null;
        DataValidationHelper validationHelper = null;

        validationHelper = new XSSFDataValidationHelper(sheet);
        CellRangeAddressList addressList = new CellRangeAddressList(rowValidation, rowValidation + cases.size() - 1,
                9,
                9);
        constraint = validationHelper.createExplicitListConstraint(Constants.Status);
        dataValidation = validationHelper.createValidation(constraint, addressList);
        dataValidation.setSuppressDropDownArrow(true);
        sheet.addValidationData(dataValidation);

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            workBook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            workBook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            fileInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public List<TestRailCase> convertExcel2TestCase(File file) {
        List<TestRailCase> testRailCases = new ArrayList<>();

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        XSSFSheet sheet = workbook.getSheetAt(0);
        int rownNum = sheet.getLastRowNum();
        int regresionRow = rownNum;
        while (!sheet.getRow(regresionRow).getCell(0).getStringCellValue()
                .equals(EXCEL_FIELDS.ASSIGNED_TO.toString())) {
            regresionRow--;
        }

        Iterator<Row> rowIterator = sheet.getRow(regresionRow).getSheet().rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            testRailCases.add(new TestRailCase(
                    cellIterator.next().getStringCellValue(),       // Assigned to
                    cellIterator.next().getStringCellValue(),       // Last Failed
                    cellIterator.next().getStringCellValue(),       // Title
                    cellIterator.next().getHyperlink().getAddress(),
                    cellIterator.next().getStringCellValue(),       // Code ID
                    cellIterator.next().getStringCellValue(),       // Test status
                    cellIterator.next().getStringCellValue(),       // Section
                    cellIterator.next().getStringCellValue(),       // Description
                    cellIterator.next().getStringCellValue(),       // Solution
                    cellIterator.next().getStringCellValue(),       // Link
                    cellIterator.next().getStringCellValue()        // Status
            ));
            testRailCases.get(testRailCases.size() - 1)
                    .setLastFailed(String.valueOf(row.getRowNum() + 1));
        }

        try {
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            fileInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return testRailCases;
    }

    public HashMap<String, TestRailCase> convertExcel2Hashmap(File file) {
        HashMap<String, TestRailCase> testRailCaseHashMap = new HashMap<>();

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        XSSFSheet sheet = workbook.getSheetAt(0);
        int rownNum = sheet.getLastRowNum();
        if (rownNum == 0) {
            return testRailCaseHashMap;
        }
        int regresionRow = rownNum;
        while (!sheet.getRow(regresionRow).getCell(0).getStringCellValue()
                .equals(EXCEL_FIELDS.ASSIGNED_TO.toString())) {
            regresionRow--;
        }

        Iterator<Row> rowIterator = sheet.getRow(regresionRow).getSheet().rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            String assignedTo = cellIterator.next().getStringCellValue();
            String lastFailed = cellIterator.next().getStringCellValue();
            String title = cellIterator.next().getStringCellValue();
            Hyperlink aux = cellIterator.next().getHyperlink();
            String caseURL;
            String caseID;
            if (aux == null) {
                caseURL = "";
                caseID = "";
            } else {
                caseURL = aux.getAddress();
                caseID = aux.getLabel();
            }
            String tesStatus = cellIterator.next().getStringCellValue();
            String section = cellIterator.next().getStringCellValue();
            String description = cellIterator.next().getStringCellValue();
            String solution = cellIterator.next().getStringCellValue();
            String solutionLink = cellIterator.next().getStringCellValue();
            String status = cellIterator.next().getStringCellValue();

            TestRailCase temp = new TestRailCase(
                    assignedTo,
                    lastFailed,
                    title,
                    caseURL,
                    caseID,
                    tesStatus,
                    section,
                    description,
                    solution,
                    solutionLink,
                    status
            );
            temp.setLastFailed(String.valueOf(row.getRowNum() + 1));
            testRailCaseHashMap.put(temp.getCaseID(), temp);
        }

        try {
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            fileInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return testRailCaseHashMap;
    }

    private CellStyle getHeaderStyle(CellStyle style) {
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());

        return style;
    }

    private CellStyle getStyle(int color, CellStyle style) {
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setWrapText(true);
        style.setFillForegroundColor(Constants.colors[color].getIndex());

        return style;
    }

    private int setSectionColor(int i, int color, List<TestRailCase> cases) {
        if (i == 0) {
            return 0;
        } else {
            if (cases.get(i - 1).getSection().equals(cases.get(i).getSection())) {
                return color;
            } else {
                if (color == Constants.colors.length - 1) {
                    return 0;
                } else {
                    return ++color;
                }
            }
        }
    }

    public void openRegression(String regressionName) {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(80));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(HEADER)));
        webDriver.findElement(By.xpath(
                        "//div[@class='table']//div[@class='summary-title text-ppp']/a[text()='" + regressionName + "']"))
                .click();
        sleep(1000);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(HEADER)));
        ((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        sleep(1000);
        webDriver.findElement(
                By.xpath("//div[@class='detailsContainer'][last()]//a[contains(text(),'QapterClaims FR')]")).click();
        sleep(1000);
    }

    public List<TestRailCase> mergeFailedCases(HashMap<String, TestRailCase> previous, List<TestRailCase> actual) {
        List<TestRailCase> temp = new ArrayList<>();
        for (TestRailCase test : actual) {
            String id = test.getCaseID();
            if (previous.containsKey(id)) {
                TestRailCase aux = previous.get(id);
                test.setLastFailed(aux.getLastFailed());
            }
            temp.add(test);
        }
        return temp;
    }

    public List<TestRailCase> mergePostponedCases(HashMap<String, TestRailCase> previous, List<TestRailCase> actual) {
        List<TestRailCase> temp = new ArrayList<>();
        for (TestRailCase test : actual) {
            String id = test.getCaseID();
            if (previous.containsKey(id)) {
                temp.add(previous.get(id));
            } else {
                temp.add(test);
            }
        }
        return temp;
    }
}
