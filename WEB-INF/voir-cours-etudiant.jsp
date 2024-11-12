<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Mes Cours</title>
</head>
<body>
    <h1>Mes Cours Inscrits</h1>
    <table>
        <tr>
            <th>Nom du Cours</th>
            <th>Enseignant</th>
        </tr>
        <c:forEach var="course" items="${courses}">
            <tr>
                <td>${course.name}</td>
                <td>${course.teacherName}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
