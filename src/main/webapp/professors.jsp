<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Gestion des Professeurs</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h2 class="mb-4">Gestion des Professeurs</h2>
    <a href="professor?action=addForm" class="btn btn-success mb-3">Ajouter un Professeur</a>

    <table class="table table-striped">
        <thead>
        <tr>
            <th>#</th>
            <th>Nom</th>
            <th>Prénom</th>
            <th>Contact</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="professor" items="${professors}">
            <tr>
                <td>${professor.id}</td>
                <td>${professor.lastName}</td>
                <td>${professor.firstName}</td>
                <td>${professor.contact}</td>
                <td>
                    <a href="professor?action=details&id=${professor.id}" class="btn btn-info btn-sm">Détails</a>
                    <a href="updateProfessor?action=updateForm&id=${professor.id}" class="btn btn-warning btn-sm">Modifier</a>

                    <a href="professor?action=delete&id=${professor.id}" class="btn btn-danger btn-sm"
                       onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce professeur ?');">Supprimer</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
