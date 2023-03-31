package qa.model;

import org.apache.poi.ss.usermodel.IndexedColors;

public class Constants {

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

    public enum EXCEL_FIELDS {
        ASSIGNED_TO,
        TITLE,
        CASE_ID,
        SECTION,
        DESCRIPTION,
        SOLUTION,
        LINK,
        STATUS
    }

}
