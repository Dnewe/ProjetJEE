<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inscription Étudiant</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h2>Inscription Étudiant</h2>
    <form action="student?action=create" method="post">

        <!-- Nom -->
        <div class="mb-3">
            <label for="last_name" class="form-label">Nom</label>
            <input type="text" class="form-control" id="last_name" name="last_name" required>
        </div>

        <!-- Prénom -->
        <div class="mb-3">
            <label for="first_name" class="form-label">Prénom</label>
            <input type="text" class="form-control" id="first_name" name="first_name" required>
        </div>

        <!-- Contact -->
        <div class="mb-3">
            <label for="contact" class="form-label">Contact</label>
            <input type="text" class="form-control" id="contact" name="contact" required>
        </div>

        <!-- Date de naissance -->
        <div class="mb-3">
            <label for="date_of_birth" class="form-label">Date de naissance</label>
            <input type="date" class="form-control" id="date_of_birth" name="date_of_birth" required>
        </div>

        <!-- Utilisateur associé -->
        <div class="mb-3">
            <label for="user_id" class="form-label">ID de l'Utilisateur</label>
            <input type="number" class="form-control" id="user_id" name="user_id" required>
        </div>

        <!-- Bouton d'inscription -->
        <button type="submit" class="btn btn-primary">Inscrire Étudiant</button>
    </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
