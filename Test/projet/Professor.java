package projet;

public class Professor extends User {
    private String lastName;
    private String firstName;
    private String contact;

    public Professor() {
    }

    public Professor(String email, String password, String lastName, String firstName, String contact) {
        super(email, password, "professor");
        this.lastName = lastName;
        this.firstName = firstName;
        this.contact = contact;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "Professor{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", contact='" + contact + '\'' +
                "} " + super.toString();
    }
}
