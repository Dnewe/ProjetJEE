<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Vos Notes et Résultats</title>
    <link rel="stylesheet" href="style.css"> 
</head>
<body>
    <header>
        <h1>Vos Notes et Résultats</h1>
        <nav>
            <ul>
                <li><a href="studentDashboard.jsp">Retour au Tableau de Bord</a></li>
                <li><a href="logout">Déconnexion</a></li>
            </ul>
        </nav>
    </header>

    <main>
        <section>
            <h2>Notes et Moyennes</h2>

            <table>
                <thead>
                    <tr>
                        <th>Nom du cours</th>
                        <th>Note obtenue</th>
                        <th>Moyenne du cours</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="grade" items="${grades}">
                        <tr>
                            <td>${grade.courseName}</td>
                            <td>${grade.score}</td>
                            <td>${grade.average}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </section>

        <section>
            <h2>Relevé de Notes</h2>
            <form action="generateTranscript" method="post">
                <input type="hidden" name="student_id" value="${student.id}" />
                <button type="submit">Télécharger votre Relevé de Notes</button>
            </form>
        </section>
    </main>

    <footer>
        <p>&copy; 2024 - Gestion de scolarité</p>
    </footer>
</body>
</html>
