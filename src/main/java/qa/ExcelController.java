package qa;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
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

    private String ctfrExcelPath;
    private String int1frExcelPath;

    private WebDriver webDriver;
    private HashMap<String, TestRailCase> previousRegressionCTFR;
    private List<TestRailCase> failedCTFR;
    private List<TestRailCase> postponedCTFR;
    private HashMap<String, TestRailCase> previousRegressionINT1FR;
    private List<TestRailCase> failedINT1FR;
    private List<TestRailCase> postponedINT1FR;

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

        previousRegressionCTFR = page.convertExcel2Hashmap(new File(ctfrExcelPath));

        failedCTFR = page.mergeFailedCases(previousRegressionCTFR, page.loadFailedTestCases());
        postponedCTFR = page.mergePostponedCases(previousRegressionCTFR, page.loadPostponedTestCases());

        page.convertTestCase2Excel(new File(ctfrExcelPath),
                Stream.concat(failedCTFR.stream(), postponedCTFR.stream()).collect(Collectors.toList()));

        page.openMainPage();
        page.openRegression(int1frName);

        previousRegressionINT1FR = page.convertExcel2Hashmap(new File(int1frExcelPath));

        failedINT1FR = page.mergeFailedCases(previousRegressionINT1FR, page.loadFailedTestCases());
        postponedINT1FR = page.mergePostponedCases(previousRegressionINT1FR, page.loadPostponedTestCases());

        page.convertTestCase2Excel(new File(int1frExcelPath),
                Stream.concat(failedINT1FR.stream(), postponedINT1FR.stream()).collect(Collectors.toList()));

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

    // FUNCTIONS FOR PATHS   --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    private boolean isOutputPathMissing(int option) {
        if (option == Constants.CTFR) {
            return ctfrExcelPath == null || ctfrExcelPath.isEmpty();
        } else {
            return int1frExcelPath == null || int1frExcelPath.isEmpty();
        }
    }

}
