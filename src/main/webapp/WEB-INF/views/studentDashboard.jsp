<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Tableau de Bord - Étudiant</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
    <header>
        <h1>Tableau de Bord - Étudiant</h1>
        <nav>
            <ul>
                <li><a href="logout">Déconnexion</a></li>
            </ul>
        </nav>
    </header>

    <main>
        <section>
            <h2>Bienvenue, ${student.firstName} ${student.lastName}</h2>
            <p>Que souhaitez-vous faire aujourd'hui ?</p>

            <ul>
                <!-- Lien vers la page d'affichage des notes et des résultats -->
                <li>
                    <a href="viewGrades.jsp?student_id=${student.id}">
                        Consulter vos notes, moyennes et résultats
                    </a>
                </li>

                <!-- Lien vers la page des cours auxquels l'étudiant est inscrit -->
                <li>
                    <a href="viewEnrolledCourses.jsp?student_id=${student.id}">
                        Voir les cours auxquels vous êtes inscrit
                    </a>
                </li>
            </ul>
        </section>
    </main>

    <footer>
        <p>&copy; 2024 - Gestion de scolarité</p>
    </footer>
</body>
</html>
