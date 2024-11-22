<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="header.jsp">
    <jsp:param name="pageTitle" value="Liste des étudiants et leurs cours" />
</jsp:include>

<div class="container mt-4">
    <h2>Liste des étudiants et leurs cours</h2>

    <c:forEach var="entry" items="${studentCoursesMap}">
        <h3>${entry.key.firstName} ${entry.key.lastName}</h3>
        <ul>
            <c:forEach var="course" items="${entry.value}">
                <li>${course}</li>
            </c:forEach>
        </ul>
    </c:forEach>
</div>

<jsp:include page="footer.jsp" />
