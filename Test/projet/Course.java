package projet;

public class Course {
    private int id;
    private String name;
    private String description;
    private int professorId;

    public Course() {}

    public Course(int id, String name, String description, int professorId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.professorId = professorId;
    }

    public Course(String name, String description, int professorId) {
        this.name = name;
        this.description = description;
        this.professorId = professorId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProfessorId() {
        return professorId;
    }

    public void setProfessorId(int professorId) {
        this.professorId = professorId;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", professorId=" + professorId +
                '}';
    }
}
