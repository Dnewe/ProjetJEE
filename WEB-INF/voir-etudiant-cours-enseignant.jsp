<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Étudiants dans le Cours</title>
</head>
<body>
    <h1>Étudiants Inscrits</h1>
    <table>
        <tr>
            <th>Nom</th>
            <th>Note</th>
        </tr>
        <c:forEach var="student" items="${students}">
            <tr>
                <td>${student.firstName} ${student.lastName}</td>
                <td>${student.grade}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
