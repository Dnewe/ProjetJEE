<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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
<jsp:include page="/WEB-INF/util/header.jsp" />
<div class="container mt-5">
    <h2>Inscription Utilisateur</h2>
    <form action="${pageContext.request.contextPath}/register" method="post">
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
                <option value="admin" >Administrateur</option>
                <option value="student" ${selectedRole == 'student' ? 'selected' : ''}>Étudiant</option>
                <option value="professor" ${selectedRole == 'professor' ? 'selected' : ''}>Enseignant</option>
            </select>
        </div>

        <div id="studentFields" style="display: ${selectedRole == 'student' ? 'block' : 'none'};">
            <div class="mb-3">
                <label for="student-last-name" class="form-label">Nom</label>
                <input type="text" class="form-control" id="student-last-name" name="student-last-name">
            </div>
            <div class="mb-3">
                <label for="student-first-name" class="form-label">Prénom</label>
                <input type="text" class="form-control" id="student-first-name" name="student-first-name">
            </div>
            <div class="mb-3">
                <label for="student-date-of-birth" class="form-label">Date de naissance</label>
                <input type="date" class="form-control" id="student-date-of-birth" name="student-date-of-birth">
            </div>
            <div class="mb-3">
                <label for="student-contact" class="form-label">Contact</label>
                <input type="text" class="form-control" id="student-contact" name="student-contact">
            </div>
        </div>

        <div id="professorFields" style="display: ${selectedRole == 'professor' ? 'block' : 'none'};">
            <div class="mb-3">
                <label for="professor-last-name" class="form-label">Nom</label>
                <input type="text" class="form-control" id="professor-last-name" name="professor-last-name">
            </div>
            <div class="mb-3">
                <label for="professor-first-name" class="form-label">Prénom</label>
                <input type="text" class="form-control" id="professor-first-name" name="professor-first-name">
            </div>
            <div class="mb-3">
                <label for="professor-contact" class="form-label">Contact</label>
                <input type="text" class="form-control" id="professor-contact" name="professor-contact">
            </div>
        </div>

        <a href="${pageContext.request.contextPath}/user?action=list" class="btn btn-danger">Annuler</a>
        <button type="submit" class="btn btn-primary">Enregistrer</button>
    </form>
</div>
</body>
<jsp:include page="/WEB-INF/util/footer.jsp" />
</html>