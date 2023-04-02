package qa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
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

    private static final String HEADER = "content-header";

    private WebDriver webDriver;

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
            String title = element.findElement(By.xpath("./td[2]/a")).getText();
            String caseID = element.findElement(By.xpath("./td[4]/a")).getText();
            String testStatus = element.findElement(By.xpath("./td/a[@class='dropdownLink status hidden-xs']"))
                    .getText();
            String section = element.findElement(By.xpath("./../../..//*[@class='title pull-left']")).getText();

            temp.add(new TestRailCase(title, caseID, testStatus, section));
        }
        return temp;
    }


    public List<TestRailCase> loadFailedTestCases() {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(80));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(HEADER)));

        sleep(1000);

        return loadTestCases(webDriver.findElements(By.xpath("//td[@class='js-status']//a[text()='F']/../..")));
    }

    public List<TestRailCase> loadPostponedTestCases() {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(80));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(HEADER)));

        sleep(1000);

        return loadTestCases(webDriver.findElements(By.xpath("//td[@class='js-status']//a[text()='Postponed']/../..")));
    }

    public void convert2EXCEL(File file, List<TestRailCase> cases) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            XSSFWorkbook workBook = new XSSFWorkbook(fileInputStream);

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
                cell.setCellValue(testRailCase.getTitle());

                cell = rowTemp.createCell(cellnumTemp++);
                cell.setCellStyle(style);
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
                cell.setCellValue(testRailCase.getLink());

                cell = rowTemp.createCell(cellnumTemp);
                cell.setCellStyle(style);
                cell.setCellValue(testRailCase.getStatus());
            }

            DataValidation dataValidation = null;
            DataValidationConstraint constraint = null;
            DataValidationHelper validationHelper = null;

            validationHelper = new XSSFDataValidationHelper(sheet);
            CellRangeAddressList addressList = new CellRangeAddressList(rowValidation, rowValidation + cases.size() - 1,
                    8,
                    8);
            constraint = validationHelper.createExplicitListConstraint(Constants.Status);
            dataValidation = validationHelper.createValidation(constraint, addressList);
            dataValidation.setSuppressDropDownArrow(true);
            sheet.addValidationData(dataValidation);

            FileOutputStream outputStream = new FileOutputStream(file);
            workBook.write(outputStream);

            outputStream.close();
            workBook.close();
            fileInputStream.close();
        } catch (Exception e) {
        }
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
//        List<WebElement> regressions = webDriver.findElements(By.xpath("//div[@class='detailsContainer']"));
//        WebElement regression = regressions.get(regressions.size() - 1)
//                .findElement(By.xpath("//a[contains(text(),'QapterClaims FR')]"));
        webDriver.findElement(
                By.xpath("//div[@class='detailsContainer'][last()]//a[contains(text(),'QapterClaims FR')]")).click();
        sleep(1000);
    }

}
