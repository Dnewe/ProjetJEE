<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Gérer les Enseignants</title>
</head>
<body>
    <h1>Gestion des Enseignants</h1>
    <form action="TeacherServlet" method="post">
        <h3>Ajouter un Enseignant</h3>
        Nom: <input type="text" name="firstName" required><br>
        Prénom: <input type="text" name="lastName" required><br>
        <input type="submit" value="Ajouter">
    </form>

    <h3>Liste des Enseignants</h3>
    <table>
        <tr>
            <th>ID</th>
            <th>Nom</th>
            <th>Prénom</th>
            <th>Actions</th>
        </tr>
        <c:forEach var="teacher" items="${teachers}">
            <tr>
                <td>${teacher.id}</td>
                <td>${teacher.firstName}</td>
                <td>${teacher.lastName}</td>
                <td>
                    <a href="edit-teacher.jsp?id=${teacher.id}">Modifier</a>
                    <a href="TeacherServlet?action=delete&id=${teacher.id}">Supprimer</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
