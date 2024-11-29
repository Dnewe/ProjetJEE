<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="WEB-INF/util/header.jsp">
    <jsp:param name="pageTitle" value="Tableau de Bord Etudiant" />
</jsp:include>

<div class="container">
    <h2>Bienvenue sur votre espace enseignant, ${sessionScope.loggedProfessor.firstName} ${sessionScope.loggedProfessor.lastName}!</h2>
    <h4>Que voulez-vous faire ?</h4>

    <div class="mt-3">
        <a href="course?action=professorList&professor-id=${sessionScope.loggedProfessor.id}" class="btn btn-primary">Voir les cours</a>
        <a href="result?action=professorList&professor-id=${sessionScope.loggedProfessor.id}" class="btn btn-primary">Voir les notes</a>
    </div>
</div>

<jsp:include page="WEB-INF/util/footer.jsp" />