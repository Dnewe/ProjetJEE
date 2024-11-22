<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="header.jsp">
    <jsp:param name="pageTitle" value="Cours de l'étudiant" />
</jsp:include>

<div class="container mt-4">
    <h2>Cours de l'étudiant</h2>

    <ul>
        <c:forEach var="course" items="${courses}">
            <li>${course.name} - ${course.description}</li>
        </c:forEach>
    </ul>
</div>

<jsp:include page="footer.jsp" />
