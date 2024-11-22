<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="header.jsp">
    <jsp:param name="pageTitle" value="Gérer les Utilisateurs" />
</jsp:include>

<div class="container mt-4">
    <h2>Liste des Utilisateurs</h2>

    <table class="table table-striped mt-3">
        <thead>
            <tr>
                <th>ID</th>
                <th>Email</th>
                <th>Role</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="user" items="${userList}">
                <tr>
                    <td>${user.id}</td>
                    <td>${user.email}</td>
                    <td>${user.role}</td>
                    <td>
                        <a href="editUser.jsp?id=${user.id}" class="btn btn-warning btn-sm">Modifier</a>
                        <a href="deleteUser?id=${user.id}" class="btn btn-danger btn-sm" onclick="return confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur ?')">Supprimer</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<jsp:include page="footer.jsp" />
