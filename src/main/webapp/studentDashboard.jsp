<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="WEB-INF/util/header.jsp">
    <jsp:param name="pageTitle" value="Tableau de Bord Etudiant" />
</jsp:include>

<div class="container">
    <h2>Bienvenue, ${sessionScope.loggedStudent.firstName} ${sessionScope.loggedStudent.lastName}!</h2>
    <h4>Que voulez-vous faire ?</h4>

    <div class="mt-3">
        <a href="course?action=studentList&student-id=${sessionScope.loggedStudent.id}" class="btn btn-primary">Voir les cours</a>
        <a href="result?action=studentList&student-id=${sessionScope.loggedStudent.id}" class="btn btn-primary">Voir les notes</a>
        <a href="student?action=downloadPdf&studentId=${sessionScope.loggedStudent.id} "class="btn btn-primary">Télécharger le relevé de notes (PDF)</a>
    </div>
</div>

<jsp:include page="WEB-INF/util/footer.jsp" />