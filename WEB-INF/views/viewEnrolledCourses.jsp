<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Cours Inscrits</title>
    <link rel="stylesheet" href="style.css"> 
</head>
<body>
    <header>
        <h1>Vos Cours Inscrits</h1>
        <nav>
            <ul>
                <li><a href="studentDashboard.jsp">Retour au Tableau de Bord</a></li>
                <li><a href="logout">Déconnexion</a></li>
            </ul>
        </nav>
    </header>

    <main>
        <section>
            <h2>Liste des cours</h2>

            <table>
                <thead>
                    <tr>
                        <th>Nom du cours</th>
                        <th>Professeur</th>
                        <th>Date d'inscription</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="course" items="${enrolledCourses}">
                        <tr>
                            <td>${course.name}</td>
                            <td>${course.professorName}</td>
                            <td>${course.enrollmentDate}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </section>
    </main>

    <footer>
        <p>&copy; 2024 - Gestion de scolarité</p>
    </footer>
</body>
</html>
