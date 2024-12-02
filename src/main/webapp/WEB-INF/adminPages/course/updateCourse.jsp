<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modifier Cours</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="/WEB-INF/util/header.jsp" />
<div class="container mt-5">
    <h2>Modifier Cours</h2>
    <jsp:include page="/WEB-INF/util/errorMessage.jsp" />

    <form action="${pageContext.request.contextPath}/course?action=update" method="post">
        <!-- ID professeur caché -->
        <input type="hidden" name="course-id" value="${course.id}">
        <input type="hidden" name="result-page" value="course?action=details&course-id=${course.id}">

        <!-- Nom -->
        <div class="mb-3">
            <label for="name" class="form-label">Nom</label>
            <input type="text" class="form-control" id="name" name="name" value="${course.name}" required>
        </div>

        <!-- Prénom -->
        <div class="mb-3">
            <label for="description" class="form-label">Description</label>
            <input type="text" class="form-control" id="description" name="description" value="${course.description}" required>
        </div>

        <!-- Boutons -->
        <a href="${pageContext.request.contextPath}/course?action=details&course-id=${course.id}" class="btn btn-danger">Annuler</a>
        <button type="submit" class="btn btn-primary">Sauvegarder</button>
    </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
<jsp:include page="/WEB-INF/util/footer.jsp" />
</html>