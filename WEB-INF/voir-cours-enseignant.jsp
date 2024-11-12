<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Cours Assignés</title>
</head>
<body>
    <h1>Mes Cours Assignés</h1>
    <table>
        <tr>
            <th>ID</th>
            <th>Nom du Cours</th>
        </tr>
        <c:forEach var="course" items="${assignedCourses}">
            <tr>
                <td>${course.id}</td>
                <td>${course.name}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
