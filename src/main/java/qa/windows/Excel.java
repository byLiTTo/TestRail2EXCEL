package qa.windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import qa.controller.ExcelController;

public class Excel extends JFrame {

    // CONSTANTS --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    private static final String CALIBRI = "Calibri";
    private static final String DEFAULT = "Default";

    // ATTRIBUTES   -->  --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    private JComboBox comboBox;
    private JTextField textFieldRegreCT;
    private JTextField textFieldExCT;
    private JButton openCTButton;
    private JTextField textFieldRegreINT;
    private JTextField textFieldExINT;
    private JButton openINTButton;
    private JButton updateButton;
    private JPanel jPanel;
    private JLabel browserLabel;
    private JLabel regressionCTFRLabel;
    private JLabel excelCTFRLabel;
    private JLabel regressionINT1FRLabel;
    private JLabel excelINT1FRLabel;

    // CONSTRUCTORS  --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    public Excel(String appTitle) {
        super(appTitle);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(jPanel);
        this.setVisible(true);
        this.pack();
        this.setBounds(0, 0, 800, 480);
        this.setLocationRelativeTo(null);
//        this.setIconImage(new ImageIcon("src/main/java/org/testrail2word/assets/clipboard.png").getImage());
        jPanel.setBackground(new Color(230, 242, 255));

        browserLabel.setFont(new Font(CALIBRI, Font.PLAIN, 18));
        regressionCTFRLabel.setFont(new Font(CALIBRI, Font.PLAIN, 18));
        excelCTFRLabel.setFont(new Font(CALIBRI, Font.PLAIN, 18));
        regressionINT1FRLabel.setFont(new Font(CALIBRI, Font.PLAIN, 18));
        excelINT1FRLabel.setFont(new Font(CALIBRI, Font.PLAIN, 18));

        comboBox.setFont(new Font(DEFAULT, Font.PLAIN, 14));

        openCTButton.setFont(new Font(DEFAULT, Font.PLAIN, 14));
        openINTButton.setFont(new Font(DEFAULT, Font.PLAIN, 14));
        updateButton.setFont(new Font(DEFAULT, Font.PLAIN, 14));

        textFieldRegreCT.setFont(new Font(DEFAULT, Font.PLAIN, 14));
        textFieldExCT.setFont(new Font(DEFAULT, Font.PLAIN, 14));
        textFieldRegreINT.setFont(new Font(DEFAULT, Font.PLAIN, 14));
        textFieldExINT.setFont(new Font(DEFAULT, Font.PLAIN, 14));

        comboBox.setBackground(Color.white);

        openCTButton.setBackground(Color.white);
        openINTButton.setBackground(Color.white);
        updateButton.setBackground(Color.white);

        openCTButton.setEnabled(true);
        openINTButton.setEnabled(true);
        updateButton.setEnabled(true);

        ExcelController controller = new ExcelController();

        // LISTENERS     --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

        openCTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.chooseCTFR(jPanel, textFieldExCT);
            }
        });
        openINTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.chooseINT1FR(jPanel, textFieldExINT);
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.update(String.valueOf(comboBox.getSelectedItem()), textFieldRegreCT.getText(),
                        textFieldRegreINT.getText());
            }
        });
    }

    // Main Function --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    public static void runApplication() {
        new Excel("TestRail2Excel");
    }
}