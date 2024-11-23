<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="header.jsp">
    <jsp:param name="pageTitle" value="Résultats des étudiants" />
</jsp:include>

<div class="container mt-4">
    <h2>Résultats des étudiants</h2>

    <table class="table mt-4">
        <thead>
            <tr>
                <th>Nom</th>
                <th>Prénom</th>
                <th>Note</th>
                <th>Date</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="result" items="${results}">
                <tr>
                    <td>${result.studentLastName}</td>
                    <td>${result.studentFirstName}</td>
                    <td>${result.grade}</td>
                    <td>${result.date}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<jsp:include page="footer.jsp" />
