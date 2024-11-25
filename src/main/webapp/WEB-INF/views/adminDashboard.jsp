<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="header.jsp">
    <jsp:param name="pageTitle" value="Tableau de Bord Administrateur" />
</jsp:include>

<div class="container mt-4">
    <h2>Que voulez-vous faire ?</h2>

    <div class="mt-3">
        <a href="professor?action=registerForm" class="btn btn-success">Ajouter un Enseignant</a>
        <a href="student?action=registerForm" class="btn btn-success">Ajouter un Étudiant</a>
        <a href="students.jsp" class="btn btn-secondary">Gérer les étudiants</a>
        <a href="professors.jsp" class="btn btn-secondary">Gérer les professeurs</a>
        <a href="courses.jsp" class="btn btn-info">Gérer les cours</a>
        <a href="enrollStudent.jsp" class="btn btn-success">Inscrire un étudiant à un cours</a>
    </div>
</div>

<jsp:include page="footer.jsp" />
