<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, model.Commande, model.Preparateur" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Commandes à Préparer</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/preparateur.css">
</head>
<body>

<header>
    <%
        Preparateur preparateur = (Preparateur) session.getAttribute("user");
        if (preparateur == null) {
            out.println("<p style='color:red;'>❌ Erreur : Aucun préparateur trouvé en session !</p>");
        } else {
            out.println("<p>✅ Utilisateur trouvé : " + preparateur.getNom() + " " + preparateur.getPrenom() + "</p>");
        }
    %>

    <h1>Bienvenue, <%= preparateur != null ? preparateur.getNom() : "Utilisateur inconnu" %>!</h1>
    <h2>Magasin: <%= (preparateur != null && preparateur.getMagasin() != null) ? preparateur.getMagasin().getAdresseMagasin() : "Non assigné" %></h2>
</header>

<main>
    <h2>Commandes à Préparer</h2>
    <table>
        <tr>
            <th>ID Commande</th>
            <th>Client</th>
            <th>Prix Total</th>
            <th>Date</th>
            <th>Action</th>
        </tr>
        <%
            List<Commande> commandes = (List<Commande>) request.getAttribute("commandes");
            if (commandes != null) {
                for (Commande cmd : commandes) {
        %>
        <tr>
            <td><%= cmd.getIdCommande() %></td>
            <td><%= cmd.getClient().getNom() %></td>
            <td><%= cmd.getPrixTotal() %>€</td>
            <td><%= cmd.getDateCommande() %></td>
            <td><button onclick="window.location.href='preparerCommande?id=<%= cmd.getIdCommande() %>'">Préparer</button></td>
        </tr>
        <%
                }
            }
        %>
    </table>
</main>

</body>
</html>
