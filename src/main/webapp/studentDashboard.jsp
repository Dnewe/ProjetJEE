<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tableau de bord �tudiant</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
    <div class="dashboard-container">
        <h1>Tableau de bord �tudiant</h1>
        <h2>R�sultats</h2>

        <!-- Affichage des r�sultats -->
        <table>
            <thead>
                <tr>
                    <th>Cours</th>
                    <th>Note</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    List<Resultat> resultats = (List<Resultat>) request.getAttribute("resultats");
                    if (resultats != null) {
                        for (Resultat resultat : resultats) { 
                %>
                <tr>
                    <td><%= resultat.getCours() %></td>
                    <td><%= resultat.getNote() %></td>
                </tr>
                <% 
                        } 
                    }
                %>
            </tbody>
        </table>

        <!-- Affichage de la moyenne -->
        <div class="average">
            <h3>Moyenne G�n�rale : <%= request.getAttribute("moyenne") %></h3>
        </div>

        <!-- Bouton de g�n�ration de relev� de notes -->
        <form action="genererReleve" method="post">
            <button type="submit">G�n�rer relev� de notes</button>
        </form>
    </div>
</body>
</html>
