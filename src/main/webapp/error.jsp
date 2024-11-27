<%--
  Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 21-Nov-24
  Time: 16:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Erreur</title>
</head>
<body>
    <% if (request.getAttribute("error") != null) {%>
    <p style="color:red;"><%=request.getAttribute("error")%></p>
    <% } %>

    <% if (request.getAttribute("error") == null) {%>
    <p style="color:red;">pas d'erreur</p>
    <% } %>

</body>
</html>
