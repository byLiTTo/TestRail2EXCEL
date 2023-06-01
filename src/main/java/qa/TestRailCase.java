package qa;

import java.util.Comparator;

public class TestRailCase {

    // ATTRIBUTES    --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    private String assigned;
    private String failRatio;
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
        this.failRatio = "";
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

    public TestRailCase(String failRatio, String titleHyperlinkAddress, String titleHyperlinkLabel, String caseHyperlinkAddress,
            String caseHyperlinkLabel, String testStatus, String section) {
        this.assigned = "";
        this.failRatio = failRatio;
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

    public TestRailCase(String assigned, String failRatio, String titleHyperlinkAddress, String titleHyperlinkLabel,
            String caseURL, String caseID, String testStatus, String section, String description, String solution,
            String solHyperlinkAddress, String solHyperlinkLabel, String status) {
        this.assigned = assigned;
        this.failRatio = failRatio;
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

    public String getFailRatio() {
        return failRatio;
    }

    public void setFailRatio(String failRatio) {
        this.failRatio = failRatio;
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

    // OVERRIDES --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    @Override
    public String toString() {
        return assigned + "\t" + failRatio + "\t" + titleHyperlinkAddress + "\t" + titleHyperlinkLabel + "\t"
                + caseHyperlinkAddress + "\t" + caseHyperlinkLabel + "\t" + testStatus + "\t" + section + "\t"
                + description + "\t" + solution + "\t" + solHyperlinkAddress + "\t" + solHyperlinkLabel + "\t" + status;
    }
}

@SuppressWarnings("checkstyle:OneTopLevelClass")
class TestRailCaseComparator implements Comparator<TestRailCase> {

    @Override
    public int compare(TestRailCase o1, TestRailCase o2) {
        int proportion1;
        String[] failRatio1 = o1.getFailRatio().split("/");
        int fail1 = Integer.parseInt(failRatio1[0]);
        int pass1 = Integer.parseInt(failRatio1[1]);
//        int post1 = Integer.getInteger(failRatio1[2]);
        if ((fail1 + pass1) == 0) {
            proportion1 = 0;
        } else {
            proportion1 = fail1 / (fail1 + pass1);
        }

        int proportion2;
        String[] failRatio2 = o2.getFailRatio().split("/");
        int fail2 = Integer.parseInt(failRatio2[0]);
        int pass2 = Integer.parseInt(failRatio2[1]);
//        int post2 = Integer.getInteger(failRatio2[2]);
        if ((fail2 + pass2) == 0) {
            proportion2 = 0;
        } else {
            proportion2 = fail2 / (fail2 + pass2);
        }

        return Integer.compare(proportion1, proportion2);
    }
}
