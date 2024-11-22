<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="header.jsp">
    <jsp:param name="pageTitle" value="Saisir des Notes" />
</jsp:include>

<div class="container mt-4">
    <h2>Saisir des Notes</h2>

    <form action="GradeEntryServlet" method="post">
        <div class="form-group">
            <label for="course">Choisissez un cours :</label>
            <select id="course" name="courseId" class="form-control">
                <c:forEach var="course" items="${courses}">
                    <option value="${course.id}">${course.name}</option>
                </c:forEach>
            </select>
        </div>

        <button type="submit" class="btn btn-primary mt-3">Sélectionner</button>
    </form>

    <c:if test="${not empty students}">
        <form action="SubmitGradesServlet" method="post">
            <input type="hidden" name="courseId" value="${selectedCourseId}">
            <table class="table mt-4">
                <thead>
                    <tr>
                        <th>Nom</th>
                        <th>Prénom</th>
                        <th>Note</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="student" items="${students}">
                        <tr>
                            <td>${student.lastName}</td>
                            <td>${student.firstName}</td>
                            <td>
                                <input type="number" name="grades[${student.id}]" step="0.01" class="form-control">
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <button type="submit" class="btn btn-success mt-3">Soumettre les notes</button>
        </form>
    </c:if>
</div>

<jsp:include page="footer.jsp" />
