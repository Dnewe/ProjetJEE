<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Saisir les Notes</title>
</head>
<body>
    <h1>Saisir les Notes</h1>
    <form action="GradeServlet" method="post">
        <h3>Sélectionnez un Cours</h3>
        <select name="courseId">
            <c:forEach var="course" items="${courses}">
                <option value="${course.id}">${course.name}</option>
            </c:forEach>
        </select>
        <h3>Liste des Étudiants</h3>
        <table>
            <tr>
                <th>Nom</th>
                <th>Note</th>
            </tr>
            <c:forEach var="student" items="${students}">
                <tr>
                    <td>${student.firstName} ${student.lastName}</td>
                    <td><input type="text" name="grade_${student.id}"></td>
                </tr>
            </c:forEach>
        </table>
        <input type="submit" value="Enregistrer les Notes">
    </form>
</body>
</html>
