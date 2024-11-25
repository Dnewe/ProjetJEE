<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Register</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script>
        function toggleFields(role) {
            document.getElementById("studentFields").style.display = (role === "student") ? "block" : "none";
            document.getElementById("professorFields").style.display = (role === "professor") ? "block" : "none";
        }
    </script>
</head>
<body>
<div class="container mt-5">
    <h2>Inscription Utilisateur</h2>
    <form action="RegisterServlet" method="post">
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
            <select class="form-select" id="role" name="role" onchange="toggleFields(this.value)" required>
                <option value="admin">Administrateur</option>
                <option value="student">Étudiant</option>
                <option value="professor">Enseignant</option>
            </select>
        </div>

        <div id="studentFields" style="display: none;">
            <div class="mb-3">
                <label for="lastName" class="form-label">Nom</label>
                <input type="text" class="form-control" id="lastName" name="lastName">
            </div>
            <div class="mb-3">
                <label for="firstName" class="form-label">Prénom</label>
                <input type="text" class="form-control" id="firstName" name="firstName">
            </div>
            <div class="mb-3">
                <label for="dateOfBirth" class="form-label">Date de naissance</label>
                <input type="date" class="form-control" id="dateOfBirth" name="dateOfBirth">
            </div>
            <div class="mb-3">
                <label for="contact" class="form-label">Contact</label>
                <input type="text" class="form-control" id="contact" name="contact">
            </div>
        </div>

        <div id="professorFields" style="display: none;">
            <div class="mb-3">
                <label for="lastName" class="form-label">Nom</label>
                <input type="text" class="form-control" id="lastName" name="lastName">
            </div>
            <div class="mb-3">
                <label for="firstName" class="form-label">Prénom</label>
                <input type="text" class="form-control" id="firstName" name="firstName">
            </div>
            <div class="mb-3">
                <label for="contact" class="form-label">Contact</label>
                <input type="text" class="form-control" id="contact" name="contact">
            </div>
        </div>

        <button type="submit" class="btn btn-primary">S'inscrire</button>
    </form>
</div>
</body>
</html>
