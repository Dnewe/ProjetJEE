<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="header.jsp">
    <jsp:param name="pageTitle" value="Inscrire un étudiant à un cours" />
</jsp:include>

<div class="container mt-4">
    <h2>Inscrire un étudiant à un cours</h2>

    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">${errorMessage}</div>
    </c:if>
    <c:if test="${not empty successMessage}">
        <div class="alert alert-success">${successMessage}</div>
    </c:if>

    <form action="enrollStudent" method="post">
        <div class="mb-3">
            <label for="studentId" class="form-label">Sélectionnez un étudiant</label>
            <select id="studentId" name="studentId" class="form-select" required>
                <c:forEach var="student" items="${students}">
                    <option value="${student.id}">${student.firstName} ${student.lastName}</option>
                </c:forEach>
            </select>
        </div>

        <div class="mb-3">
            <label for="courseId" class="form-label">Sélectionnez un cours</label>
            <select id="courseId" name="courseId" class="form-select" required>
                <c:forEach var="course" items="${courses}">
                    <option value="${course.id}">${course.name}</option>
                </c:forEach>
            </select>
        </div>

        <button type="submit" class="btn btn-primary">Inscrire</button>
    </form>
</div>

<jsp:include page="footer.jsp" />
