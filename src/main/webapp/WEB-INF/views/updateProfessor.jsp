<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Modifier Professeur</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h2 class="mb-4">${professor == null ? "Ajouter un Professeur" : "Modifier Professeur"}</h2>
    <form action="professor" method="post">
        <input type="hidden" name="action" value="${professor == null ? "create" : "update"}">
        <input type="hidden" name="id" value="${professor != null ? professor.id : ''}">

        <div class="mb-3">
            <label for="lastName" class="form-label">Nom</label>
            <input type="text" class="form-control" id="lastName" name="last_name"
                   value="${professor != null ? professor.lastName : ''}" required>
        </div>

        <div class="mb-3">
            <label for="firstName" class="form-label">Prénom</label>
            <input type="text" class="form-control" id="firstName" name="first_name"
                   value="${professor != null ? professor.firstName : ''}" required>
        </div>

        <div class="mb-3">
            <label for="contact" class="form-label">Contact</label>
            <input type="text" class="form-control" id="contact" name="contact"
                   value="${professor != null ? professor.contact : ''}" required>
        </div>

        <button type="submit" class="btn btn-primary">
            ${professor == null ? "Ajouter" : "Modifier"}
        </button>
        <a href="professor?action=list" class="btn btn-secondary">Annuler</a>
    </form>
</div>
</body>
</html>
