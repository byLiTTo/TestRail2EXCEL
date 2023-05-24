package qa;

public class TestRailCase {

    // ATTRIBUTES    --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    private String assigned;
    private String lastFailed;
    private String title;
    private String caseURL;
    private String caseID;
    private String testStatus;
    private String section;
    private String description;
    private String solution;
    private String solutionLink;
    private String status;

    // CONSTRUCTORS  --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    public TestRailCase(String title, String caseURL, String caseID, String testStatus, String section) {
        this.assigned = "";
        this.title = title;
        this.lastFailed = "";
        this.caseURL = caseURL;
        this.caseID = caseID;
        this.testStatus = testStatus;
        this.section = section;
        this.description = "";
        this.solution = "";
        this.solutionLink = "";
        this.status = "-";
    }

    public TestRailCase(String assigned, String lastFailed, String title, String caseURL, String caseID,
            String testStatus, String section, String description, String solution, String solutionLink,
            String status) {
        this.assigned = assigned;
        this.lastFailed = lastFailed;
        this.title = title;
        this.caseURL = caseURL;
        this.caseID = caseID;
        this.testStatus = testStatus;
        this.section = section;
        this.description = description;
        this.solution = solution;
        this.solutionLink = solutionLink;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaseURL() {
        return caseURL;
    }

    public void setCaseURL(String caseURL) {
        this.caseURL = caseURL;
    }

    public String getCaseID() {
        return caseID;
    }

    public void setCaseID(String caseID) {
        this.caseID = caseID;
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

    public String getSolutionLink() {
        return solutionLink;
    }

    public void setSolutionLink(String solutionLink) {
        this.solutionLink = solutionLink;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
