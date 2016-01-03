package net.venedi.model;

import java.util.Date;

public class Exam {

    private int examId;
    private Course course;
    private String description;
    private Date createDatetime;
    private Date examDate;
    private String note;
    private EStatusType taken;
    private String examResult;

    public Exam(){}

    public Exam(Course course, String description, Date examDate, String note) {
        this.course = course;
        this.description = description;
        this.examDate = examDate;
        this.note = note;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public EStatusType getTaken() {
        return taken;
    }

    public void setTaken(EStatusType taken) {
        this.taken = taken;
    }

    public String getExamResult() {
        return examResult;
    }

    public void setExamResult(String examResult) {
        this.examResult = examResult;
    }
}
