package qa;

public class TestRailCase {

    // ATTRIBUTES    --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    private String assigned;
    private String lastFailed;
    private String titleHyperlinkAddress;
    private String titleHyperlinkLabel;
    private String caseHyperlinkAddress;
    private String caseHyperlinkLabel;
    private String testStatus;
    private String section;
    private String description;
    private String solution;
    private String solHyperlinkAddress;
    private String solHyperlinkLabel;
    private String status;

    // CONSTRUCTORS  --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    public TestRailCase(String titleHyperlinkAddress, String titleHyperlinkLabel, String caseHyperlinkAddress,
            String caseHyperlinkLabel, String testStatus, String section) {
        this.assigned = "";
        this.lastFailed = "";
        this.titleHyperlinkAddress = titleHyperlinkAddress;
        this.titleHyperlinkLabel = titleHyperlinkLabel;
        this.caseHyperlinkAddress = caseHyperlinkAddress;
        this.caseHyperlinkLabel = caseHyperlinkLabel;
        this.testStatus = testStatus;
        this.section = section;
        this.description = "";
        this.solution = "";
        this.solHyperlinkAddress = "";
        this.status = "-";
    }

    public TestRailCase(String assigned, String lastFailed, String titleHyperlinkAddress, String titleHyperlinkLabel,
            String caseURL, String caseID, String testStatus, String section, String description, String solution,
            String solHyperlinkAddress, String solHyperlinkLabel, String status) {
        this.assigned = assigned;
        this.lastFailed = lastFailed;
        this.titleHyperlinkAddress = titleHyperlinkAddress;
        this.titleHyperlinkLabel = titleHyperlinkLabel;
        this.caseHyperlinkAddress = caseURL;
        this.caseHyperlinkLabel = caseID;
        this.testStatus = testStatus;
        this.section = section;
        this.description = description;
        this.solution = solution;
        this.solHyperlinkAddress = solHyperlinkAddress;
        this.solHyperlinkLabel = solHyperlinkLabel;
        this.status = status;
    }

    // GETTERS & SETTERS --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->


    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    public String getLastFailed() {
        return lastFailed;
    }

    public void setLastFailed(String lastFailed) {
        this.lastFailed = lastFailed;
    }

    public String getTitleHyperlinkAddress() {
        return titleHyperlinkAddress;
    }

    public void setTitleHyperlinkAddress(String titleHyperlinkAddress) {
        this.titleHyperlinkAddress = titleHyperlinkAddress;
    }

    public String getTitleHyperlinkLabel() {
        return titleHyperlinkLabel;
    }

    public void setTitleHyperlinkLabel(String titleHyperlinkLabel) {
        this.titleHyperlinkLabel = titleHyperlinkLabel;
    }

    public String getCaseHyperlinkAddress() {
        return caseHyperlinkAddress;
    }

    public void setCaseHyperlinkAddress(String caseHyperlinkAddress) {
        this.caseHyperlinkAddress = caseHyperlinkAddress;
    }

    public String getCaseHyperlinkLabel() {
        return caseHyperlinkLabel;
    }

    public void setCaseHyperlinkLabel(String caseHyperlinkLabel) {
        this.caseHyperlinkLabel = caseHyperlinkLabel;
    }

    public String getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(String testStatus) {
        this.testStatus = testStatus;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getSolHyperlinkAddress() {
        return solHyperlinkAddress;
    }

    public void setSolHyperlinkAddress(String solHyperlinkAddress) {
        this.solHyperlinkAddress = solHyperlinkAddress;
    }

    public String getSolHyperlinkLabel() {
        return solHyperlinkLabel;
    }

    public void setSolHyperlinkLabel(String solHyperlinkLabel) {
        this.solHyperlinkLabel = solHyperlinkLabel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return assigned + "\t" + lastFailed + "\t" + titleHyperlinkAddress + "\t" + titleHyperlinkLabel + "\t"
                + caseHyperlinkAddress + "\t" + caseHyperlinkLabel + "\t" + testStatus + "\t" + section + "\t"
                + description + "\t" + solution + "\t" + solHyperlinkAddress + "\t" + solHyperlinkLabel + "\t" + status;
    }
}
