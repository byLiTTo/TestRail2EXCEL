package qa.model;

public class TestRailCase {

    // ATTRIBUTES    --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    private String assigned;
    private String title;
    private String caseID;
    private String section;
    private String description;
    private String solution;
    private String link;
    private String status;

    // CONSTRUCTORS  --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    public TestRailCase(String title, String caseID, String section) {
        this.assigned = "";
        this.title = title;
        this.caseID = caseID;
        this.section = section;
        this.description = "";
        this.solution = "";
        this.link = "";
        this.status = "-";
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

    // FUNCTIONS --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    public String[] toCSV() {
        return (this.getAssigned() + "," + this.getTitle() + "," + this.getCaseID() + ","
                + this.getSection() + ","
                + this.getDescription() + "," + this.getSolution() + "," + this.getLink() + ","
                + this.getStatus()).split(",");
    }

    // OVERRIDES --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> --> -->

    @Override
    public String toString() {
        return this.getAssigned() + "\t" + this.getTitle() + "\t" + this.getCaseID() + "\t" + this.getSection() + "\t"
                + this.getDescription() + "\t" + this.getSolution() + "\t" + this.getLink() + "\t"
                + this.getStatus();
    }
}
