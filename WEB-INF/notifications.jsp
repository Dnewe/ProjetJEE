<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Notifications</title>
</head>
<body>
    <h1>Mes Notifications</h1>
    <table>
        <tr>
            <th>Date</th>
            <th>Message</th>
        </tr>
        <c:forEach var="notification" items="${notifications}">
            <tr>
                <td>${notification.date}</td>
                <td>${notification.message}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
