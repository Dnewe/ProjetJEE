<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:include page="WEB-INF/util/header.jsp">
    <jsp:param name="pageTitle" value="Tableau de Bord Etudiant" />
</jsp:include>

<div class="container admin-dashboard">
    <h2>Bienvenue sur votre espace étudiant, ${sessionScope.loggedStudent.firstName} ${sessionScope.loggedStudent.lastName}!</h2>
    <jsp:include page="/WEB-INF/util/errorMessage.jsp" />
    <h4>Que voulez-vous faire ?</h4>

    <div class="mt-3">
        <a href="course?action=studentList&student-id=${sessionScope.loggedStudent.id}" class="btn btn-primary">Voir les cours</a>
        <a href="result?action=studentList&student-id=${sessionScope.loggedStudent.id}" class="btn btn-primary">Voir les notes</a>
    </div>
</div>

<jsp:include page="WEB-INF/util/footer.jsp" />