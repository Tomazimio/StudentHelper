package net.venedi.data;

public class ExamException extends Exception {

    private String description;

    public ExamException(String description) {
        super(description);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
