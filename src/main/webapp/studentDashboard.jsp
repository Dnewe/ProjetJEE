<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tableau de bord étudiant</title>
    <link rel="stylesheet" href="css/styles.css">
</head>
<body>
    <div class="dashboard-container">
        <h1>Tableau de bord étudiant</h1>
        <h2>Résultats</h2>

        <!-- Affichage des résultats -->
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
            <h3>Moyenne Générale : <%= request.getAttribute("moyenne") %></h3>
        </div>

        <!-- Bouton de génération de relevé de notes -->
        <form action="genererReleve" method="post">
            <button type="submit">Générer relevé de notes</button>
        </form>
    </div>
</body>
</html>
