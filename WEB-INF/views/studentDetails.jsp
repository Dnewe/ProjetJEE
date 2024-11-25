<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Détails de l'étudiant</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <h2 class="mt-4">Détails de l'Étudiant</h2>

        <div class="card mt-4">
            <div class="card-header">
                Informations sur l'étudiant
            </div>
            <div class="card-body">
                <p><strong>Nom :</strong> ${student.lastName}</p>
                <p><strong>Prénom :</strong> ${student.firstName}</p>
                <p><strong>Contact :</strong> ${student.contact}</p>
                <p><strong>Date de naissance :</strong> ${student.dateOfBirth}</p>
                <p><strong>Utilisateur :</strong> ${student.user.username} (${student.user.role})</p>
            </div>
        </div>

        <h4 class="mt-4">Cours assignés à cet étudiant :</h4>
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>Nom du cours</th>
                    <th>Professeur</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="course" items="${courses}">
                    <tr>
                        <td>${course.name}</td>
                        <td>${course.professor.lastName} ${course.professor.firstName}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div class="mt-4">
            <a href="student?action=updateForm&id=${student.id}" class="btn btn-warning">Modifier</a>
            <a href="student?action=delete&id=${student.id}" class="btn btn-danger" onclick="return confirm('Êtes-vous sûr de vouloir supprimer cet étudiant ?');">Supprimer</a>
            <a href="student?action=list" class="btn btn-secondary">Retour à la liste</a>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>
