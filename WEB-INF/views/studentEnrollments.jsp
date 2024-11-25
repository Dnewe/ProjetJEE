<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inscriptions de l'Étudiant</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h2>Inscriptions de ${student.firstName} ${student.lastName}</h2>

        <!-- Liste des cours inscrits -->
        <h4>Cours Inscrits</h4>
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>Nom</th>
                    <th>Description</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="course" items="${enrolledCourses}">
                    <tr>
                        <td>${course.name}</td>
                        <td>${course.description}</td>
                        <td>
                            <form action="enrollment" method="post" style="display: inline-block;">
                                <input type="hidden" name="action" value="remove">
                                <input type="hidden" name="studentId" value="${student.id}">
                                <input type="hidden" name="courseId" value="${course.id}">
                                <button type="submit" class="btn btn-danger btn-sm">Désinscrire</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <!-- Ajouter un cours -->
        <h4>Ajouter un Cours</h4>
        <form action="enrollment" method="post">
            <input type="hidden" name="action" value="add">
            <input type="hidden" name="studentId" value="${student.id}">
            
            <div class="form-group">
                <label for="course">Sélectionner un Cours</label>
                <select name="courseId" id="course" class="form-control">
                    <option value="">Choisir un cours</option>
                    <c:forEach var="course" items="${availableCourses}">
                        <option value="${course.id}">${course.name}</option>
                    </c:forEach>
                </select>
            </div>
            <button type="submit" class="btn btn-primary">Ajouter</button>
        </form>

        <a href="enrollment?action=list" class="btn btn-secondary mt-3">Retour à la liste des étudiants</a>
    </div>
</body>
</html>
