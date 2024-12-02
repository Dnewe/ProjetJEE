<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Détails de l'étudiant</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="/WEB-INF/util/header.jsp" />
    <div class="container">
        <h2 class="mt-4">Détails de l'Étudiant</h2>
        <jsp:include page="/WEB-INF/util/errorMessage.jsp" />

        <div class="card mt-4">
            <div class="card-header">
                Informations sur l'étudiant
            </div>
            <div class="card-body">
                <p><strong>Nom :</strong> ${student.lastName}</p>
                <p><strong>Prénom :</strong> ${student.firstName}</p>
                <p><strong>Contact :</strong> ${student.contact}</p>
                <p><strong>Date de naissance :</strong> <fmt:formatDate value="${student.dateOfBirth}" pattern="dd/MM/yyyy"/></p>
            </div>
        </div>

        <h4 class="mt-4">Cours inscrtis</h4>
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>Nom du cours</th>
                    <th>Professeur</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="course" items="${enrolledCourses}">
                    <tr>
                        <td>${course.name}</td>
                        <td>${course.professor.lastName} ${course.professor.firstName}</td>
                        <td><form action="${pageContext.request.contextPath}/enrollment" method="post" style="display: inline-block;">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="result-page" value="student?action=details&student-id=${student.id}">
                            <input type="hidden" name="student-id" value="${student.id}">
                            <input type="hidden" name="course-id" value="${course.id}">
                            <button type="submit" class="btn btn-danger btn-sm">Retirer</button>
                        </form></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <br>
        <!-- Formulaire pour inscrire un cours -->
        <h4>Inscrire à un Cours</h4>
        <form class="d-flex" action="${pageContext.request.contextPath}/enrollment" method="post">
            <input type="hidden" name="action" value="create">
            <input type="hidden" name="result-page" value="student?action=details&student-id=${student.id}">
            <input type="hidden" name="student-id" value="${student.id}">

            <select class="form-select form-select-sm mr-sm-2" name="course-id" id="course" class="form-control">
                <option value="">Sélectionner un cours</option>
                <c:forEach var="course" items="${availableCourses}">
                    <option value="${course.id}">${course.name}</option>
                </c:forEach>
            </select>
            <button type="submit" class="btn btn-primary btn-sm">Inscrire</button>
        </form>

        <div class="mt-4">
            <h4>Administratif</h4>
            <a href="${pageContext.request.contextPath}/transcript?student-id=${student.id}" class="btn btn-primary">Créer Relevé de notes</a>
        </div>

        <br>
        <div class="mt-4">
            <a href="${pageContext.request.contextPath}/student?action=updateForm&student-id=${student.id}" class="btn btn-warning">Modifier</a>
            <!-- Formulaire pour la suppression en POST -->
            <form action="${pageContext.request.contextPath}/student" method="post" style="display: inline;">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="student-id" value="${student.id}">
                <button type="submit" class="btn btn-danger" onclick="return confirm('Êtes-vous sûr de vouloir supprimer cet étudiant ?');">Supprimer</button>
            </form>
            <a href="${pageContext.request.contextPath}/student?action=list" class="btn btn-secondary">Retour à la liste</a>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
<jsp:include page="/WEB-INF/util/footer.jsp" />
</html>