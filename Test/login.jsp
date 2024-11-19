<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Connexion</title>
</head>
<body>
    <h2>Connexion</h2>
    <form action="ConnexionServlet" method="post">
        <label for="username">Email :</label>
        <input type="email" id="username" name="username" required><br><br>
        <label for="password">Mot de passe :</label>
        <input type="password" id="password" name="password" required><br><br>
        <input type="submit" value="Se connecter">
    </form>

    <!-- Gestion des messages d'erreur -->
    <%
        String error = request.getParameter("error");
        if ("invalidCredentials".equals(error)) {
    %>
        <p style="color:red;">Email ou mot de passe incorrect.</p>
    <%
        } else if ("serverError".equals(error)) {
    %>
        <p style="color:red;">Une erreur est survenue. Veuillez réessayer plus tard.</p>
    <%
        }
    %>
</body>
</html>
