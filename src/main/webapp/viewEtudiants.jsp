<%@ page import="com.jeeproject.model.Etudiant" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Liste des Étudiants</title>
</head>
<body>
<h1>Liste des Étudiants</h1>
<table border="1">
    <tr>
        <th>Nom</th>
        <th>Prénom</th>
        <th>Âge</th>
    </tr>
    <% if (request.getAttribute("etudiants") != null) {%>
        <% for (Etudiant etudiant : (List<Etudiant>) request.getAttribute("etudiants")) {%>
            <tr>
                <td><%=etudiant.getNom()%></td>
                <td><%=etudiant.getPrenom()%></td>
                <td><%=etudiant.getAge()%></td>
            </tr>
        <% } %>
    <% } %>

</table>

<h2>Ajouter un Étudiant</h2>
<form action="etudiants" method="post">
    Nom: <input type="text" name="nom" required />
    Prénom: <input type="text" name="prenom" required />
    Âge: <input type="number" name="age" required />
    <input type="submit" value="Ajouter" />
</form>
</body>
</html>
