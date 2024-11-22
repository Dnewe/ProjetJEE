package projet;

public class Student extends User {
    private String firstName;
    private String lastName;

    public Student() {}

    public Student(int id, String email, String password, String firstName, String lastName) {
        super(id, email, password, "student");
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Student{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                "} " + super.toString();
    }
}
