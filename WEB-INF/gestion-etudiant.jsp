<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Gérer les Étudiants</title>
</head>
<body>
    <h1>Gestion des Étudiants</h1>
    <form action="StudentServlet" method="post">
        <h3>Ajouter un Étudiant</h3>
        Nom: <input type="text" name="firstName" required><br>
        Prénom: <input type="text" name="lastName" required><br>
        Date de Naissance: <input type="date" name="dob" required><br>
        Contact: <input type="text" name="contact" required><br>
        <input type="submit" value="Ajouter">
    </form>
    
    <h3>Liste des Étudiants</h3>
    <table>
        <!-- Boucle pour afficher la liste des étudiants -->
        <tr>
            <th>ID</th>
            <th>Nom</th>
            <th>Prénom</th>
            <th>Actions</th>
        </tr>
        <c:forEach var="student" items="${students}">
            <tr>
                <td>${student.id}</td>
                <td>${student.firstName}</td>
                <td>${student.lastName}</td>
                <td>
                    <a href="edit-student.jsp?id=${student.id}">Modifier</a>
                    <a href="StudentServlet?action=delete&id=${student.id}">Supprimer</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
