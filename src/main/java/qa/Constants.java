package qa;

import org.apache.poi.ss.usermodel.IndexedColors;

public class Constants {

    public static final String TESTRAIL_LINK = "https://axn-testrail01-p.axadmin.net/index.php?/runs/overview/128";

    public static final int CTFR = 1;
    public static final int INT1FR = 2;

    public static IndexedColors[] colors = {
            IndexedColors.LIGHT_GREEN,
            IndexedColors.LIGHT_TURQUOISE,
            IndexedColors.LIGHT_YELLOW,
            IndexedColors.ROSE,
            IndexedColors.LIGHT_ORANGE,
            IndexedColors.LIGHT_CORNFLOWER_BLUE
    };
    public static final String[] Status = new String[]{"-", "DEVELOPING", "SOLVED", "PR_CREATED", "PR_MERGED",
            "POSSIBLE_BUG", "UPDATING_BUG", "WAITING_NEXT_EXECUTION", "WAITING_SUPPORT"};

    public enum EXCEL_FIELDS {
        ASSIGNED_TO,
        TITLE,
        CASE_ID,
        TEST_STATUS,
        SECTION,
        DESCRIPTION,
        SOLUTION,
        LINK,
        STATUS
    }

}
