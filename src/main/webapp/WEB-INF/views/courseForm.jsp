<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Créer un Cours</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container">
        <h2 class="mt-4">Créer un Cours</h2>

        <form action="course" method="post" class="mt-4">
            <input type="hidden" name="action" value="create">

            <div class="form-group">
                <label for="name">Nom du Cours</label>
                <input type="text" class="form-control" id="name" name="name" required>
            </div>

            <div class="form-group">
                <label for="description">Description</label>
                <textarea class="form-control" id="description" name="description" required></textarea>
            </div>

            <div class="form-group">
                <label for="professor_id">Professeur</label>
                <select class="form-control" id="professor_id" name="professor_id">
                    <option value="-1">Aucun professeur assigné</option>
                    <c:forEach var="professor" items="${professors}">
                        <option value="${professor.id}">${professor.lastName} ${professor.firstName}</option>
                    </c:forEach>
                </select>
            </div>

            <button type="submit" class="btn btn-primary">Créer le Cours</button>
            <a href="course?action=list" class="btn btn-secondary">Retour à la liste</a>
        </form>
    </div>

    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>
