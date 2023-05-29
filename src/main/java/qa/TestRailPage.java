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

    private static final String SCROLL_SCRIPT = "arguments[0].scrollTop = arguments[0].scrollHeight";

    private static final String HEADER = "content-header";
    private static final String DISPLAYER = "./td[@class='action']";
    private static final String HISTORY_TAB = "//div[@class='tab-header']/a[@id= 'historyTab']";
    private static final String SHOW_ALL = "//div[@id='showHistory']/span[@class='showAll text-secondary pull-right']/a";
    private static final String STATISTICS_PANEL = "//div[@class='chart-legend-title' and contains(text(),'In the past 30 days:')]/..";
    private static final String HISTORY_PASSED_TEST = "//div[@id='history']//tr/td[@class='box']/span[text()='Passed']";
    private static final String HISTORY_FAILED_TEST = "//div[@id='history']//tr/td[@class='box']/span[text()='Failed']";
    private static final String HISTORY_POSTPONED_TEST = "//div[@id='history']//tr/td[@class='box']/span[text()='Postponed']";

    private static final String HISTORY_RECENTLY_PASSED_TEST = "//div[@id='history']//div[@class='chart-legend-name text-ppp' and contains(text(),'Passed')]";
    private static final String HISTORY_RECENTLY_FAILED_TEST = "//div[@id='history']//div[@class='chart-legend-name text-ppp' and contains(text(),'Failed')]";
    private static final String HISTORY_RECENTLY_POSTPONED_TEST = "//div[@id='history']//div[@class='chart-legend-name text-ppp' and contains(text(),'Postponed')]";

    private WebDriver webDriver;
    private WebDriverWait wait;
    private int titleIndex = 2;
    private int caseIDIndex = 4;
    //table[@id='grid-14959939']//tr[@class='header']/th[3]

    public TestRailPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        this.wait = new WebDriverWait(webDriver, Duration.ofSeconds(30));
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
            WebElement tmp = element.findElement(By.xpath("./td[" + titleIndex + "]/a"));
            String titleHyperlinkAddress = tmp.getAttribute("href");
            String titleHyperlinkLabel = tmp.getText();

            tmp = element.findElement(By.xpath("./td[" + caseIDIndex + "]/a"));
            String caseHyperlinkAddress = tmp.getAttribute("href");
            String caseHyperlinkLabel = tmp.getText();

            String testStatus = element.findElement(By.xpath("./td/a[@class='dropdownLink status hidden-xs']"))
                    .getText();

            String section = element.findElement(By.xpath("./../../..//*[@class='title pull-left']")).getText();

            String failRatio = loadRecentlyHistory(element);

            temp.add(new TestRailCase(failRatio, titleHyperlinkAddress, titleHyperlinkLabel, caseHyperlinkAddress,
                    caseHyperlinkLabel, testStatus, section));
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
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("window.scrollTo(0, 0)");
        sleep(1000);

        return loadTestCases(webDriver.findElements(By.xpath("//td[@class='js-status']//a[text()='Postponed']/../..")));
    }

    private String loadHistory(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) webDriver;

        js.executeScript(SCROLL_SCRIPT, element.findElement(By.xpath(DISPLAYER)));
        sleep(500);
        wait.until(ExpectedConditions.visibilityOf(element.findElement(By.xpath(DISPLAYER))));
        wait.until(ExpectedConditions.elementToBeClickable(element.findElement(By.xpath(DISPLAYER)))).click();
        sleep(500);
        wait.until(ExpectedConditions.attributeContains(
                element.findElement(By.xpath(DISPLAYER + "//span[@class='action-collapse hidden']")), "style",
                "display: inline;"));

        js.executeScript(SCROLL_SCRIPT, webDriver.findElement(By.xpath(HISTORY_TAB)));
        sleep(500);
        wait.until(ExpectedConditions.visibilityOf(webDriver.findElement(By.xpath(HISTORY_TAB))));
        wait.until(ExpectedConditions.elementToBeClickable(webDriver.findElement(By.xpath(HISTORY_TAB)))).click();
        sleep(500);

        wait.until(ExpectedConditions.visibilityOf(webDriver.findElement(By.id("activityChart"))));
        wait.until(ExpectedConditions.elementToBeClickable(webDriver.findElement(By.xpath(SHOW_ALL))));
        js.executeScript(SCROLL_SCRIPT, webDriver.findElement(By.xpath(SHOW_ALL)));
        sleep(500);
        wait.until(ExpectedConditions.elementToBeClickable(webDriver.findElement(By.xpath(SHOW_ALL)))).click();
        wait.until(ExpectedConditions.invisibilityOf(webDriver.findElement(By.xpath(SHOW_ALL))));
        sleep(500);

        int totalPassed = webDriver.findElements(By.xpath(HISTORY_PASSED_TEST)).size();
        int totalFail = webDriver.findElements(By.xpath(HISTORY_FAILED_TEST)).size();
        int totalPostponed = webDriver.findElements(By.xpath(HISTORY_POSTPONED_TEST)).size();

        return totalFail + "/" + totalPassed + "/" + totalPostponed;
    }

    private String loadRecentlyHistory(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) webDriver;

        js.executeScript(SCROLL_SCRIPT, element.findElement(By.xpath(DISPLAYER)));
        sleep(500);
        wait.until(ExpectedConditions.visibilityOf(element.findElement(By.xpath(DISPLAYER))));
        wait.until(ExpectedConditions.elementToBeClickable(element.findElement(By.xpath(DISPLAYER)))).click();
        sleep(500);
        wait.until(ExpectedConditions.attributeContains(
                element.findElement(By.xpath(DISPLAYER + "//span[@class='action-collapse hidden']")), "style",
                "display: inline;"));

        js.executeScript(SCROLL_SCRIPT, webDriver.findElement(By.xpath(HISTORY_TAB)));
        sleep(500);
        wait.until(ExpectedConditions.visibilityOf(webDriver.findElement(By.xpath(HISTORY_TAB))));
        wait.until(ExpectedConditions.elementToBeClickable(webDriver.findElement(By.xpath(HISTORY_TAB)))).click();
        sleep(500);

//        wait.until(ExpectedConditions.visibilityOf(webDriver.findElement(By.xpath(STATISTICS_PANEL))));
        String totalPassed = webDriver.findElement(By.xpath(HISTORY_RECENTLY_PASSED_TEST)).getText().split(" ")[0];
        String totalFail = webDriver.findElement(By.xpath(HISTORY_RECENTLY_FAILED_TEST)).getText().split(" ")[0];
        String totalPostponed = webDriver.findElement(By.xpath(HISTORY_RECENTLY_POSTPONED_TEST)).getText()
                .split(" ")[0];

        return totalFail + "/" + totalPassed + "/" + totalPostponed;
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
            cell.setCellValue(testRailCase.getFailRatio());

            cell = rowTemp.createCell(cellnumTemp++);
            cell.setCellStyle(style);
            XSSFHyperlink link = workBook.getCreationHelper().createHyperlink(LINK_URL);
            link.setAddress(testRailCase.getTitleHyperlinkAddress());
            cell.setHyperlink(link);
            cell.setCellValue(testRailCase.getTitleHyperlinkLabel());

            cell = rowTemp.createCell(cellnumTemp++);
            cell.setCellStyle(style);
            link = workBook.getCreationHelper().createHyperlink(LINK_URL);
            link.setAddress(testRailCase.getCaseHyperlinkAddress());
            cell.setHyperlink(link);
            cell.setCellValue(testRailCase.getCaseHyperlinkLabel());

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
            link = workBook.getCreationHelper().createHyperlink(LINK_URL);
            link.setAddress(testRailCase.getSolHyperlinkAddress());
            cell.setHyperlink(link);
            cell.setCellValue(testRailCase.getSolHyperlinkLabel());

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
        int rowNum = sheet.getLastRowNum();
        if (rowNum == 0) {
            return testRailCaseHashMap;
        }
        int regressionRow = rowNum;
        while (!sheet.getRow(regressionRow).getCell(0).getStringCellValue()
                .equals(EXCEL_FIELDS.ASSIGNED_TO.toString())) {
            regressionRow--;
        }

        Iterator<Row> rowIterator = sheet.getRow(regressionRow).getSheet().rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            String assignedTo = cellIterator.next().getStringCellValue();
            String failRatio = cellIterator.next().getStringCellValue();
            Cell aux = cellIterator.next();
            String titleHyperlinkAddress;
            String titleHyperlinkLabel;
            if (aux.getHyperlink() == null) {
                titleHyperlinkAddress = "";
            } else {
                titleHyperlinkAddress = aux.getHyperlink().getAddress();
            }
            titleHyperlinkLabel = aux.getStringCellValue();
            aux = cellIterator.next();
            String caseHyperlinkAddress;
            String caseHyperlinkLabel;
            if (aux.getHyperlink() == null) {
                caseHyperlinkAddress = "";
            } else {
                caseHyperlinkAddress = aux.getHyperlink().getAddress();
            }
            caseHyperlinkLabel = aux.getStringCellValue();
            String tesStatus = cellIterator.next().getStringCellValue();
            String section = cellIterator.next().getStringCellValue();
            String description = cellIterator.next().getStringCellValue();
            String solution = cellIterator.next().getStringCellValue();
            aux = cellIterator.next();
            String solHyperlinkAddress;
            String solHyperlinkLabel;
            if (aux.getHyperlink() == null) {
                solHyperlinkAddress = "";
            } else if (aux.getHyperlink().getAddress() == null) {
                solHyperlinkAddress = "";
            } else {
                solHyperlinkAddress = aux.getHyperlink().getAddress();
            }
            solHyperlinkLabel = aux.getStringCellValue();
            String status = cellIterator.next().getStringCellValue();

            TestRailCase temp = new TestRailCase(
                    assignedTo,
                    failRatio,
                    titleHyperlinkAddress,
                    titleHyperlinkLabel,
                    caseHyperlinkAddress,
                    caseHyperlinkLabel,
                    tesStatus,
                    section,
                    description,
                    solution,
                    solHyperlinkAddress,
                    solHyperlinkLabel,
                    status
            );
            testRailCaseHashMap.put(caseHyperlinkLabel, temp);
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
//        List<TestRailCase> temp = new ArrayList<>();
//        for (TestRailCase test : actual) {
//            String id = test.getCaseHyperlinkLabel();
//            if (previous.containsKey(id)) {
//                TestRailCase aux = previous.get(id);
//                test.setLastFailed(aux.getLastFailed());
//            }
//            temp.add(test);
//        }
        return actual;
    }

    public List<TestRailCase> mergePostponedCases(HashMap<String, TestRailCase> previous, List<TestRailCase> actual) {
        List<TestRailCase> temp = new ArrayList<>();
        for (TestRailCase test : actual) {
            String id = test.getCaseHyperlinkLabel();
            if (previous.containsKey(id)) {
                TestRailCase tmp = previous.get(id);
                test.setAssigned(tmp.getAssigned());
                test.setDescription(tmp.getDescription());
                test.setSolution(tmp.getSolution());
                test.setSolHyperlinkAddress(tmp.getSolHyperlinkAddress());
                test.setSolHyperlinkLabel(tmp.getSolHyperlinkLabel());
                test.setStatus(tmp.getStatus());
            }
            temp.add(test);
        }
        return temp;
    }
}
