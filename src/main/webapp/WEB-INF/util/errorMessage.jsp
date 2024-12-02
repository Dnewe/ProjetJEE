<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<!-- Messages d'erreur ou de succès -->
<c:if test="${not empty errorMessage}">
  <div class="alert alert-danger">
      ${errorMessage}
  </div>
</c:if>
<c:if test="${not empty successMessage}">
  <div class="alert alert-success">
      ${successMessage}
  </div>
</c:if>
</body>
</html>
