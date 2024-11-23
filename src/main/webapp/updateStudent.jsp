<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modifier Étudiant</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h2>Modifier Étudiant</h2>
    <form action="student?action=update" method="post">
        <!-- ID étudiant caché -->
        <input type="hidden" name="id" value="${student.id}">

        <!-- Nom -->
        <div class="mb-3">
            <label for="last_name" class="form-label">Nom</label>
            <input type="text" class="form-control" id="last_name" name="last_name" value="${student.lastName}" required>
        </div>

        <!-- Prénom -->
        <div class="mb-3">
            <label for="first_name" class="form-label">Prénom</label>
            <input type="text" class="form-control" id="first_name" name="first_name" value="${student.firstName}" required>
        </div>

        <!-- Contact -->
        <div class="mb-3">
            <label for="contact" class="form-label">Contact</label>
            <input type="text" class="form-control" id="contact" name="contact" value="${student.contact}" required>
        </div>

        <!-- Date de naissance -->
        <div class="mb-3">
            <label for="date_of_birth" class="form-label">Date de naissance</label>
            <input type="date" class="form-control" id="date_of_birth" name="date_of_birth" value="${student.dateOfBirth}" required>
        </div>

        <!-- Bouton pour sauvegarder -->
        <button type="submit" class="btn btn-primary">Sauvegarder</button>
    </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
