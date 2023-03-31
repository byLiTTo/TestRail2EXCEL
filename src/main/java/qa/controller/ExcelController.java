package qa.controller;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import qa.model.Constants;
import qa.model.Constants.EXCEL_FIELDS;
import qa.model.TestRailCase;

public class ExcelController {

    // ATTRIBUTES    --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    private String ctfrExcelPath;
    private String int1frExcelPath;

    private WebDriver webDriver;
    private List<TestRailCase> regressionCTFR;
    private List<TestRailCase> regressionINT1FR;

    // PRINCIPAL FUNCTIONS   --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    public void sigIn(String browser, String URL) {
        setWebDriverBrowser(browser);
        webDriver.navigate().to(URL);
        webDriver.manage().window().maximize();
    }

    public void update(String browser, String ctfrURL, String int1frURL) {
        regressionCTFR = new ArrayList<>();
        regressionINT1FR = new ArrayList<>();

        sigIn(browser, ctfrURL);
        loadTestRailCases(Constants.CTFR);
        convert2EXCEL(Constants.CTFR, new File(ctfrExcelPath));

        webDriver.navigate().to(int1frURL);
        loadTestRailCases(Constants.INT1FR);
        convert2EXCEL(Constants.INT1FR, new File(int1frExcelPath));

        webDriver.close();
    }

    // FUNCTION FOR CHOOSE FILE PATH --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    public void chooseCTFR(JPanel jPanel, JTextField outputPathTextField) {
        final JFileChooser fileChooser = new JFileChooser(isOutputPathMissing(Constants.CTFR) ? "." : ctfrExcelPath);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.showOpenDialog(jPanel);
        if (fileChooser.getSelectedFile() != null) {
            outputPathTextField.setText(String.valueOf(fileChooser.getSelectedFile()));
            this.ctfrExcelPath = String.valueOf(fileChooser.getSelectedFile());
        }
    }

    public void chooseINT1FR(JPanel jPanel, JTextField outputPathTextField) {
        final JFileChooser fileChooser = new JFileChooser(
                isOutputPathMissing(Constants.INT1FR) ? "." : int1frExcelPath);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.showOpenDialog(jPanel);
        if (fileChooser.getSelectedFile() != null) {
            outputPathTextField.setText(String.valueOf(fileChooser.getSelectedFile()));
            this.int1frExcelPath = String.valueOf(fileChooser.getSelectedFile());
        }
    }

    // BROWSER   --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    private void setWebDriverBrowser(String browser) {
        switch (browser) {
            case "Firefox":
                ProfilesIni profile = new ProfilesIni();
                FirefoxProfile myprofile = profile.getProfile("profileToolsQA");
                FirefoxOptions options = new FirefoxOptions().setProfile(myprofile);
                webDriver = new FirefoxDriver(options);
                break;
            case "Edge":
                webDriver = new EdgeDriver();
                break;
            default:
                ChromeOptions co = new ChromeOptions().addArguments("--remote-allow-origins=*");
                co.setBrowserVersion("91");
                WebDriverManager.chromedriver().setup();
                webDriver = new ChromeDriver(co);
        }
    }

    // LOAD TESTRAIL CASES   --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    private void loadTestRailCases(int option) {
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(80));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("content-header")));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<WebElement> temp = webDriver.findElements(By.xpath("//td[@class='js-status']//a[text()='F']/../.."));
        List<TestRailCase> cases = new ArrayList<>();

        for (WebElement element : temp) {
            String title = element.findElement(By.xpath("./td[2]/a")).getText();
            String caseID = element.findElement(By.xpath("./td[4]/a")).getText();
            String section = element.findElement(By.xpath("./../../..//*[@class='title pull-left']")).getText();

            cases.add(new TestRailCase(title, caseID, section));
        }
        if (option == Constants.CTFR) {
            regressionCTFR.clear();
            regressionCTFR = cases;
        } else {
            regressionINT1FR.clear();
            regressionINT1FR = cases;
        }
    }

    // SHOW CASE INFORMATION --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->
    private void readEXCEL() {
        try {
            File myFile = new File(ctfrExcelPath);
            FileInputStream fis = new FileInputStream(myFile);
            XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);

            XSSFSheet mySheet = myWorkBook.getSheetAt(0);

            Iterator<Row> rowIterator = mySheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            System.out.print(cell.getStringCellValue() + "\t");
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            System.out.print(cell.getNumericCellValue() + "\t");
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            System.out.print(cell.getBooleanCellValue() + "\t");
                            break;
                        default:
                    }
                }
                System.out.println();
            }
            myWorkBook.close();
            fis.close();
        } catch (Exception e) {
        }

    }

    // TESRAIL CASE TO EXCEL --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    private void convert2EXCEL(int option, File file) {
        try {
            FileInputStream fileInputStream;
            XSSFWorkbook workBook;

            fileInputStream = new FileInputStream(file);
            workBook = new XSSFWorkbook(fileInputStream);

            XSSFSheet sheet = workBook.getSheetAt(0);

            int rownNum = sheet.getLastRowNum() + 1;
            int cellnum = 0;

            Row row = sheet.createRow(rownNum++);
            for (int i = 0; i < EXCEL_FIELDS.values().length; i++) {
                row.createCell(cellnum++).setCellValue(" ");
            }

            row = sheet.createRow(rownNum++);
            cellnum = 0;
            Cell cell = row.createCell(cellnum++);
            cell.setCellStyle(getHeaderStyle(sheet.getWorkbook().createCellStyle()));
            cell.setCellValue(DateTimeFormatter.ofPattern("MM/dd/yyyy").format(LocalDateTime.now()));
            for (int i = 1; i < EXCEL_FIELDS.values().length; i++) {
                row.createCell(cellnum++).setCellValue(" ");
            }

            row = sheet.createRow(rownNum++);
            cellnum = 0;
            for (EXCEL_FIELDS field : EXCEL_FIELDS.values()) {
                cell = row.createCell(cellnum++);
                cell.setCellStyle(getHeaderStyle(sheet.getWorkbook().createCellStyle()));
                cell.setCellValue(field.toString());
            }

            int color = 0;
            List<TestRailCase> aux;
            if (option == Constants.CTFR) {
                aux = regressionCTFR;
            } else {
                aux = regressionINT1FR;
            }
            for (int i = 0; i < aux.size(); i++) {
                TestRailCase testRailCase = aux.get(i);

                if (option == Constants.CTFR) {
                    color = setSectionColor(i, color, Constants.CTFR);
                } else {
                    color = setSectionColor(i, color, Constants.INT1FR);
                }
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

            FileOutputStream outputStream = new FileOutputStream(file);
            workBook.write(outputStream);

            if (option == Constants.CTFR) {
                System.out.println("Writing CTFR Regression on XLSX file Finished ...");
            } else {
                System.out.println("Writing INT1FR Regression on XLSX file Finished ...");
            }

            outputStream.close();
            workBook.close();
            fileInputStream.close();
        } catch (Exception e) {

        }
    }

    // FUNCTIONS FOR PATHS   --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    private boolean isOutputPathMissing(int option) {
        if (option == Constants.CTFR) {
            return ctfrExcelPath == null || ctfrExcelPath.isEmpty();
        } else {
            return int1frExcelPath == null || int1frExcelPath.isEmpty();
        }
    }

    // FUNCTIONS FOR CELL_STYLE  --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

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

    private int setSectionColor(int i, int color, int option) {
        if (i == 0) {
            return 0;
        } else {
            List<TestRailCase> cases;
            if (option == Constants.CTFR) {
                cases = regressionCTFR;
            } else {
                cases = regressionINT1FR;
            }
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

}
