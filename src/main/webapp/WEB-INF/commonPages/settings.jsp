<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion des Étudiants</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h2 class="mb-4">Gestion des Étudiants</h2>

    <!-- Messages d'erreur ou de succès -->
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">
                ${errorMessage}
        </div>
    </c:if>
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success">
                ${successMessage}
        </div>
    </c:if>

    <!-- Formulaire pour changer de mot de passe -->
    <h4>Modifier votre mot de passe.</h4>
    <form action="${pageContext.request.contextPath}/settings" method="post">
        <!-- attribut caché -->
        <input type="hidden" name="action" value="changePassword"></input>

        <!-- NEW MDP -->
        <div class="mb-3">
            <label for="old-password" class="form-label">Ancien mot de passe</label>
            <input type="password" class="form-control" id="old-password" name="old-password" required>
        </div>

        <!-- OLD MDP -->
        <div class="mb-3">
            <label for="new-password" class="form-label">Nouveau mot de passe</label>
            <input type="password" class="form-control" id="new-password" name="new-password" required>
        </div>

        <!-- Boutons -->
        <button type="submit" class="btn btn-primary">Modifier</button>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>