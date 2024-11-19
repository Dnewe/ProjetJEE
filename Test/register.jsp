<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="header.jsp">
    <jsp:param name="pageTitle" value="Inscription" />
</jsp:include>

<div class="container mt-4">
    <h2>Inscription d'un Utilisateur</h2>
    <form action="register" method="post">
        <div class="mb-3">
            <label for="email" class="form-label">Email</label>
            <input type="email" class="form-control" id="email" name="email" required>
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">Mot de passe</label>
            <input type="password" class="form-control" id="password" name="password" required>
        </div>
        <div class="mb-3">
            <label for="role" class="form-label">Rôle</label>
            <select class="form-select" id="role" name="role" required>
                <option value="student">Étudiant</option>
                <option value="professor">Professeur</option>
                <option value="admin">Administrateur</option>
            </select>
        </div>
        <button type="submit" class="btn btn-primary">Inscrire</button>
    </form>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger mt-3">${errorMessage}</div>
    </c:if>
</div>

<jsp:include page="footer.jsp" />
