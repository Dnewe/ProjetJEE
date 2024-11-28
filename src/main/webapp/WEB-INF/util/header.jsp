<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="false" %>

<!DOCTYPE html>

<html lang="en">
<link rel="stylesheet" href="css/styles.css">

<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-expand-lg" style="background-color: #87CEEB;">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">
                <img src="logo.png" alt="Logo" width="50" height="50" class="d-inline-block align-text-top">
                Cy-Tech Gestion
            </a>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <form action="logout" method="get" style="display:inline;">
    						<button type="submit" class="btn btn-danger">Déconnexion</button>
						</form>

                    </li>
                </ul>
            </div>
        </div>
    </nav>
</body>
</html>
