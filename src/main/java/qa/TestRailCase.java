package qa;

public class TestRailCase {

    // ATTRIBUTES    --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    private String assigned;
    private String title;
    private String caseID;
    private String testStatus;
    private String section;
    private String description;
    private String solution;
    private String link;
    private String status;

    // CONSTRUCTORS  --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    public TestRailCase(String title, String caseID, String testStatus, String section) {
        this.assigned = "";
        this.title = title;
        this.caseID = caseID;
        this.testStatus = testStatus;
        this.section = section;
        this.description = "";
        this.solution = "";
        this.link = "";
        this.status = "-";
    }

    public TestRailCase(String assigned, String title, String caseID, String testStatus, String section,
            String description,
            String solution, String link, String status) {
        this.assigned = assigned;
        this.title = title;
        this.caseID = caseID;
        this.testStatus = testStatus;
        this.section = section;
        this.description = description;
        this.solution = solution;
        this.link = link;
        this.status = status;
    }

    // GETTERS & SETTERS --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaseID() {
        return caseID;
    }

    public void setCaseID(String caseID) {
        this.caseID = caseID;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(String testStatus) {
        this.testStatus = testStatus;
    }
}
