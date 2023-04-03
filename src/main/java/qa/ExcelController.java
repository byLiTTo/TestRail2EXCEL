package qa;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;

public class ExcelController {

    // ATTRIBUTES    --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    private final Logger LOG = Logger.getLogger("qa.ExcelController");
    private String ctfrExcelPath;
    private String int1frExcelPath;

    private WebDriver webDriver;
    private List<TestRailCase> regressionCTFR;
    private List<TestRailCase> regressionINT1FR;

    private TestRailPage page;

    // PRINCIPAL FUNCTIONS   --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    public void sigIn(String browser) {
        setWebDriverBrowser(browser);
        this.page = new TestRailPage(webDriver);
        page.openMainPage();
    }

    public void update(String browser, String ctfrName, String int1frName) {
        sigIn(browser);

        page.openRegression(ctfrName);

        regressionCTFR = Stream.concat(page.loadFailedTestCases().stream(), page.loadPostponedTestCases().stream())
                .collect(Collectors.toList());
        page.convert2EXCEL(new File(ctfrExcelPath), regressionCTFR);

        page.openMainPage();

        page.openRegression(int1frName);
        regressionINT1FR = Stream.concat(page.loadFailedTestCases().stream(), page.loadPostponedTestCases().stream())
                .collect(Collectors.toList());
        page.convert2EXCEL(new File(int1frExcelPath), regressionINT1FR);

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

    // FUNCTIONS FOR PATHS   --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    private boolean isOutputPathMissing(int option) {
        if (option == Constants.CTFR) {
            return ctfrExcelPath == null || ctfrExcelPath.isEmpty();
        } else {
            return int1frExcelPath == null || int1frExcelPath.isEmpty();
        }
    }

}