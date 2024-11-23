<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

    <!-- Bouton pour ajouter un nouvel étudiant -->
    <a href="student?action=registerForm" class="btn btn-success mb-3">Ajouter un Étudiant</a>

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

    <!-- Tableau des étudiants -->
    <table class="table table-bordered table-hover">
        <thead class="table-light">
            <tr>
                <th>ID</th>
                <th>Nom</th>
                <th>Prénom</th>
                <th>Contact</th>
                <th>Date de Naissance</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="student" items="${students}">
            <tr>
                <td>${student.id}</td>
                <td>${student.lastName}</td>
                <td>${student.firstName}</td>
                <td>${student.contact}</td>
                <td>${student.dateOfBirth}</td>
                <td>
                    <!-- Bouton pour voir les détails -->
                    <a href="student?action=details&id=${student.id}" class="btn btn-info btn-sm">Détails</a>
                    
                    <!-- Bouton pour modifier -->
                    <a href="student?action=updateForm&id=${student.id}" class="btn btn-warning btn-sm">Modifier</a>
                    
                    <!-- Bouton pour supprimer -->
                    <a href="student?action=delete&id=${student.id}" 
                       class="btn btn-danger btn-sm" 
                       onclick="return confirm('Êtes-vous sûr de vouloir supprimer cet étudiant ?');">
                       Supprimer
                    </a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <!-- Si aucun étudiant n'est disponible -->
    <c:if test="${empty students}">
        <p class="text-center">Aucun étudiant trouvé.</p>
    </c:if>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
