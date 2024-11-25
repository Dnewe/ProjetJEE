<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Détails du Professeur</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <h2>Détails du Professeur</h2>

        <table class="table table-bordered">
            <tr>
                <th>Nom</th>
                <td>${professor.firstName} ${professor.lastName}</td>
            </tr>
            <tr>
                <th>Email</th>
                <td>${professor.email}</td>
            </tr>
        </table>

        <h4>Cours Assignés</h4>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Nom du Cours</th>
                    <th>Description</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="course" items="${assignedCourses}">
                    <tr>
                        <td>${course.name}</td>
                        <td>${course.description}</td>
                        <td>
                            <form action="professor" method="post" style="display: inline-block;">
                                <input type="hidden" name="action" value="removeCourse">
                                <input type="hidden" name="professorId" value="${professor.id}">
                                <input type="hidden" name="courseId" value="${course.id}">
                                <button type="submit" class="btn btn-danger btn-sm">Supprimer</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <!-- Formulaire pour assigner un cours -->
        <h4>Assigner un Nouveau Cours</h4>
        <form action="professor" method="post">
            <input type="hidden" name="action" value="assignCourse">
            <input type="hidden" name="professorId" value="${professor.id}">
            
            <div class="form-group">
                <label for="course">Choisir un Cours</label>
                <select name="courseId" id="course" class="form-control">
                    <option value="">Sélectionner un cours</option>
                    <c:forEach var="course" items="${availableCourses}">
                        <option value="${course.id}">${course.name}</option>
                    </c:forEach>
                </select>
            </div>
            <button type="submit" class="btn btn-primary">Assigner</button>
        </form>

        <a href="professor?action=list" class="btn btn-secondary mt-3">Retour à la liste des professeurs</a>
    </div>

    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>
