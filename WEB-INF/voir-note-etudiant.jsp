<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Mes Résultats</title>
</head>
<body>
    <h1>Mes Résultats</h1>
    <table>
        <tr>
            <th>Cours</th>
            <th>Note</th>
            <th>Moyenne</th>
        </tr>
        <c:forEach var="result" items="${results}">
            <tr>
                <td>${result.courseName}</td>
                <td>${result.grade}</td>
                <td>${result.average}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
