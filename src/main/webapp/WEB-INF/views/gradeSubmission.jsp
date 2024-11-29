<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Saisie des notes</title>
</head>
<body>
    <h1>Saisie des notes</h1>
    
    <form action="professor?action=submitGrades" method="post">
        <!-- Liste déroulante des cours -->
        <label for="course">Sélectionner un cours :</label>
        <select name="course_id" id="course" onchange="this.form.submit()">
            <option value="">-- Choisir un cours --</option>
            <c:forEach var="course" items="${courses}">
                <option value="${course.id}" ${course.id == selectedCourseId ? 'selected' : ''}>
                    ${course.name}
                </option>
            </c:forEach>
        </select>
    </form>
    
    <!-- Liste des �tudiants et champs pour les notes -->
    <c:if test="${not empty students}">
        <form action="professor?action=saveGrades" method="post">
            <input type="hidden" name="course_id" value="${selectedCourseId}">
            <table border="1">
                <thead>
                    <tr>
                        <th>�tudiant</th>
                        <th>Note</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="student" items="${students}">
                        <tr>
                            <td>${student.name}</td>
                            <td>
                                <input type="number" name="grades[${student.id}]" step="0.01" min="0" max="20" required>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <button type="submit">Enregistrer les notes</button>
        </form>
    </c:if>
</body>
</html>
