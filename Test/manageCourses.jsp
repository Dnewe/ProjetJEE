<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="header.jsp">
    <jsp:param name="pageTitle" value="Gestion des Cours" />
</jsp:include>

<div class="container mt-4">
    <h2>Gestion des Cours</h2>
    
    <h3 class="mt-4">Liste des Cours</h3>
    <table class="table table-bordered mt-3">
        <thead>
            <tr>
                <th>ID</th>
                <th>Nom du Cours</th>
                <th>Description</th>
                <th>Professeur Assigné</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <% 
                projet.CourseDAO courseDAO = new projet.CourseDAO();
                projet.UserDAO userDAO = new projet.UserDAO();
                List<projet.Course> courses = courseDAO.getAllCourses();
                for (projet.Course course : courses) {
                    String professorName = userDAO.getProfessorNameById(course.getProfessorId());
            %>
                <tr>
                    <td><%= course.getId() %></td>
                    <td><%= course.getName() %></td>
                    <td><%= course.getDescription() %></td>
                    <td><%= professorName != null ? professorName : "Aucun" %></td>
                    <td>
                        <form action="CourseServlet" method="post" class="d-inline">
                            <input type="hidden" name="action" value="assignProfessor" />
                            <input type="hidden" name="courseId" value="<%= course.getId() %>" />
                            <select name="professorId" class="form-select d-inline-block" required>
                                <option value="" disabled selected>Choisir un professeur</option>
                                <% 
                                    List<projet.Professor> professors = userDAO.getAllProfessors();
                                    for (projet.Professor professor : professors) { 
                                %>
                                    <option value="<%= professor.getId() %>">
                                        <%= professor.getLastName() + " " + professor.getFirstName() %>
                                    </option>
                                <% } %>
                            </select>
                            <button type="submit" class="btn btn-primary btn-sm">Assigner</button>
                        </form>

                        <form action="CourseServlet" method="post" class="d-inline">
                            <input type="hidden" name="action" value="deleteCourse" />
                            <input type="hidden" name="courseId" value="<%= course.getId() %>" />
                            <button type="submit" class="btn btn-danger btn-sm">Supprimer</button>
                        </form>
                    </td>
                </tr>
            <% } %>
        </tbody>
    </table>

    <h3 class="mt-4">Ajouter un Nouveau Cours</h3>
    <form action="CourseServlet" method="post" class="mt-3">
        <input type="hidden" name="action" value="addCourse" />
        <div class="mb-3">
            <label for="courseName" class="form-label">Nom du Cours</label>
            <input type="text" id="courseName" name="courseName" class="form-control" required />
        </div>
        <div class="mb-3">
            <label for="description" class="form-label">Description</label>
            <textarea id="description" name="description" class="form-control" rows="3" required></textarea>
        </div>
        <button type="submit" class="btn btn-success">Ajouter le Cours</button>
    </form>
</div>

<jsp:include page="footer.jsp" />
