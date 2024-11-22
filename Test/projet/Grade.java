package projet;

import java.sql.Date;

public class Grade {
	private int id;
    private int enrollmentId;  
    private double grade;      
    private Date date;         

    public Grade(int enrollmentId, double grade, Date date) {
        this.enrollmentId = enrollmentId;
        this.grade = grade;
        this.date = date;
    }
    
    public Grade(int id, int enrollmentId, double grade, Date date) {
        this.id = id;
        this.enrollmentId = enrollmentId;
        this.grade = grade;
        this.date = date;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "enrollmentId=" + enrollmentId +
                ", grade=" + grade +
                ", date=" + date +
                '}';
    }
}
