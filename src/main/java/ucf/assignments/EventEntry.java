package ucf.assignments;

public class EventEntry {
    private String description;
    private String status;
    private String date;

    public EventEntry(){
        this.description = description;
        this.status = status;
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String input){
        this.date = input;
    }
    //Will store description, due date, and
    //completion status
}
