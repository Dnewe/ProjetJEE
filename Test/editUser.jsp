<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="header.jsp">
    <jsp:param name="pageTitle" value="Modifier Utilisateur" />
</jsp:include>

<div class="container mt-4">
    <h2>Modifier les informations de l'utilisateur</h2>

    <form action="updateUser" method="post">
        <input type="hidden" name="id" value="${user.id}"/>

        <div class="mb-3">
            <label for="email" class="form-label">Email</label>
            <input type="email" class="form-control" id="email" name="email" value="${user.email}" required />
        </div>

        <div class="mb-3">
            <label for="role" class="form-label">Role</label>
            <select class="form-select" id="role" name="role" required>
                <option value="etudiant" <c:if test="${user.role == 'etudiant'}">selected</c:if>>Étudiant</option>
                <option value="enseignant" <c:if test="${user.role == 'enseignant'}">selected</c:if>>Enseignant</option>
                <option value="admin" <c:if test="${user.role == 'admin'}">selected</c:if>>Administrateur</option>
            </select>
        </div>

        <div id="etudiantFields" style="display: none;">
            <div class="mb-3">
                <label for="prenom" class="form-label">Prénom</label>
                <input type="text" class="form-control" id="prenom" name="prenom" value="${user.prenom}" />
            </div>

            <div class="mb-3">
                <label for="nom" class="form-label">Nom</label>
                <input type="text" class="form-control" id="nom" name="nom" value="${user.nom}" />
            </div>

            <div class="mb-3">
                <label for="contact" class="form-label">Contact</label>
                <input type="text" class="form-control" id="contact" name="contact" value="${user.contact}" />
            </div>

            <div class="mb-3">
                <label for="dob" class="form-label">Date de Naissance</label>
                <input type="date" class="form-control" id="dob" name="dob" value="${user.dob}" />
            </div>
        </div>

        <div id="enseignantFields" style="display: none;">
            <div class="mb-3">
                <label for="prenom" class="form-label">Prénom</label>
                <input type="text" class="form-control" id="prenom" name="prenom" value="${user.prenom}" />
            </div>

            <div class="mb-3">
                <label for="nom" class="form-label">Nom</label>
                <input type="text" class="form-control" id="nom" name="nom" value="${user.nom}" />
            </div>

            <div class="mb-3">
                <label for="contact" class="form-label">Contact</label>
                <input type="text" class="form-control" id="contact" name="contact" value="${user.contact}" />
            </div>
        </div>

        <button type="submit" class="btn btn-primary">Mettre à jour</button>
        <a href="manageUsers" class="btn btn-secondary">Annuler</a>
    </form>
</div>

<jsp:include page="footer.jsp" />

<script>
    document.getElementById("role").addEventListener("change", function() {
        var role = this.value;

        // Hide all additional fields
        document.getElementById("etudiantFields").style.display = "none";
        document.getElementById("enseignantFields").style.display = "none";

        // Show the fields based on role
        if (role === "etudiant") {
            document.getElementById("etudiantFields").style.display = "block";
        } else if (role === "enseignant") {
            document.getElementById("enseignantFields").style.display = "block";
        }
    });

    // Initialize display based on current role
    document.getElementById("role").dispatchEvent(new Event('change'));
</script>
