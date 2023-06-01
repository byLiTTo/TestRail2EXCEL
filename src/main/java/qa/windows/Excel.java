package qa.windows;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import qa.ExcelController;

public class Excel extends JFrame {

    // CONSTANTS --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --

    private static final String CALIBRI = "Calibri";
    private static final String DEFAULT = "Default";

    // ATTRIBUTES   -->  --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

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
    private JLabel soleraLogo;
    private JLabel version;
    private JLabel statusLog;

    // CONSTRUCTORS  --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    public Excel(String appTitle) {
        super(appTitle);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(jPanel);
        this.setVisible(true);
        this.pack();
        this.setBounds(0, 0, 800, 480);
        this.setLocationRelativeTo(null);
        this.setIconImage(new ImageIcon("src/main/resources/img/Logo2.png").getImage());
        jPanel.setBackground(new Color(127, 133, 245));

        browserLabel.setFont(new Font(CALIBRI, Font.BOLD, 18));
        regressionCTFRLabel.setFont(new Font(CALIBRI, Font.BOLD, 18));
        excelCTFRLabel.setFont(new Font(CALIBRI, Font.BOLD, 18));
        regressionINT1FRLabel.setFont(new Font(CALIBRI, Font.BOLD, 18));
        excelINT1FRLabel.setFont(new Font(CALIBRI, Font.BOLD, 18));

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
                setCursor(Cursor.WAIT_CURSOR);
                controller.update(String.valueOf(comboBox.getSelectedItem()),
                        textFieldRegreCT.getText(),
                        textFieldRegreINT.getText());
                setCursor(Cursor.DEFAULT_CURSOR);
            }
        });
        updateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                setCursor(Cursor.HAND_CURSOR);
            }
        });
        updateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                setCursor(Cursor.DEFAULT_CURSOR);
            }
        });
        comboBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                setCursor(Cursor.HAND_CURSOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                setCursor(Cursor.DEFAULT_CURSOR);
            }
        });
        openCTButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                setCursor(Cursor.HAND_CURSOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                setCursor(Cursor.DEFAULT_CURSOR);
            }
        });
        openINTButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                setCursor(Cursor.HAND_CURSOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                setCursor(Cursor.DEFAULT_CURSOR);
            }
        });
    }

    // Main Function --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    public static void runApplication() {
        new Excel("QapterClaims FR - Maintenance tool");
    }
}