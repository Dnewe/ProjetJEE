<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
</head>
<body>
<div class="container mt-4">
    <h2>Bienvenue, Professeur!</h2>
    <p>Que voulez-vous faire ?</p>
    <a href="enterGrades.jsp" class="btn btn-dark">Saisir des notes</a>
</div>
</body>
</html><%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:include page="header.jsp">
    <jsp:param name="pageTitle" value="Accueil Proffesseur" />
</jsp:include>



<jsp:include page="footer.jsp" />
