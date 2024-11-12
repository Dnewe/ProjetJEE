<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Générer des Rapports</title>
</head>
<body>
    <h1>Générer des Rapports de Performance</h1>
    <form action="ReportServlet" method="post">
        <h3>Sélectionnez un Cours</h3>
        <select name="courseId">
            <c:forEach var="course" items="${courses}">
                <option value="${course.id}">${course.name}</option>
            </c:forEach>
        </select>
        <input type="submit" value="Générer Rapport">
    </form>
</body>
</html>
