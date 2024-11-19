<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="header.jsp">
    <jsp:param name="pageTitle" value="Tableau de Bord Administrateur" />
</jsp:include>

<div class="container mt-4">
    <h2>Que voulez-vous faire ?</h2>

    <div class="mt-3">
        <a href="register.jsp" class="btn btn-primary">Inscrire un nouvel utilisateur</a>
        <a href="manageUsers.jsp" class="btn btn-secondary">Gérer les utilisateurs</a>
    </div>
</div>

<jsp:include page="footer.jsp" />
