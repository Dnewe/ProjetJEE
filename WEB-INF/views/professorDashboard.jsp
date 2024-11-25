<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="header.jsp">
    <jsp:param name="pageTitle" value="Tableau de Bord Proffesseurs" />
</jsp:include>

<div class="container mt-4">
    <h2>Que voulez-vous faire ?</h2>

    <div class="mt-3">
        <a href="gradeSubmission.jsp" class="btn btn-success">Saisir des notes</a>
    </div>
</div>

<jsp:include page="footer.jsp" />
