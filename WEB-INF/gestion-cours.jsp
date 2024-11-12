<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Gérer les Cours</title>
</head>
<body>
    <h1>Gestion des Cours</h1>
    <form action="CourseServlet" method="post">
        <h3>Ajouter un Cours</h3>
        Nom du Cours: <input type="text" name="courseName" required><br>
        <input type="submit" value="Ajouter">
    </form>

    <h3>Liste des Cours</h3>
    <table>
        <tr>
            <th>ID</th>
            <th>Nom du Cours</th>
            <th>Actions</th>
        </tr>
        <c:forEach var="course" items="${courses}">
            <tr>
                <td>${course.id}</td>
                <td>${course.name}</td>
                <td>
                    <a href="edit-course.jsp?id=${course.id}">Modifier</a>
                    <a href="CourseServlet?action=delete&id=${course.id}">Supprimer</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
