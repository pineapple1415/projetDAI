<%--
  Created by IntelliJ IDEA.
  User: rachi
  Date: 25/02/2025
  Time: 00:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>
<%@ page import="model.Commande" %>
<%@ page import="model.Magasin" %>
<%
    // Récupération de la liste des commandes depuis l'attribut "commandes"
    List<Commande> commandes = (List<Commande>) request.getAttribute("commandes");

    if (commandes != null) {
        // Trier la liste des commandes par date (ordre croissant)
        commandes.sort((c1, c2) -> c1.getDateCommande().compareTo(c2.getDateCommande()));
    }
%>
<html>
<head>
    <title>Commandes à Préparer</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        .back-link {
            display: block;
            margin-top: 20px;
            text-align: center;
            font-size: 18px;
        }

        .back-link a {
            text-decoration: none;
            color: #007BFF;
        }

        .back-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body class="container mt-4">

<h2 class="text-center">📦 Liste des Commandes à Préparer</h2>

<%
    if (commandes == null || commandes.isEmpty()) {
%>
<div class="alert alert-warning text-center">⚠ Aucune commande à préparer.</div>
<%
} else {
%>

<table class="table table-bordered table-striped">
    <thead class="table-dark">
    <tr>
        <th>Priorité</th>  <!-- ✅ Nouvelle colonne Priorité -->
        <th>Date</th>
        <th>Statut</th>
        <th>Prix Total (€)</th>
        <th>Magasin</th>
        <th>Adresse</th>
        <th>Détails sur la Commande</th>
        <th>Modifier la commande</th>
    </tr>
    </thead>
    <tbody>
    <%
        int priorite = 1; // Définir la priorité à 1
        for (Commande c : commandes) {
            Magasin magasin = c.getMagasin();
    %>
    <tr>
        <td><%= priorite++ %></td>  <!-- ✅ Numérotation des priorités -->
        <td><%= c.getDateCommande() %></td>
        <td><span class="badge bg-primary"><%= c.getStatut() %></span></td>
        <td><%= c.getPrixTotal() %> €</td>
        <td><%= (magasin != null) ? magasin.getNomMagasin() : "Non renseigné" %></td>
        <td><%= (magasin != null) ? magasin.getAdresseMagasin() : "Non renseigné" %></td>

        <!-- ✅ Colonne pour les détails -->
        <td>
            <a href="commande?action=consulter&id=<%= c.getIdCommande() %>" class="btn btn-info btn-sm">🔍 Détails</a>
        </td>

        <!-- ✅ Colonne pour changer le statut -->
        <td>
            <% if (c.getStatut() == model.Statut.PAYEE) { %>
            <!-- Formulaire pour marquer la commande en préparation -->
            <form action="commande" method="post" class="d-inline">
                <input type="hidden" name="action" value="marquerPreparation">
                <input type="hidden" name="idCommande" value="<%= c.getIdCommande() %>">
                <button type="submit" class="btn btn-warning btn-sm">⏳ Marquer en préparation</button>
            </form>
            <% } if (c.getStatut() == model.Statut.EN_COURS) { %>
            <!-- Formulaire pour finaliser la commande -->
            <form action="commande" method="post" class="d-inline">
                <input type="hidden" name="action" value="finaliserPreparation">
                <input type="hidden" name="idCommande" value="<%= c.getIdCommande() %>">
                <button type="submit" class="btn btn-success btn-sm">✅ Finaliser Commande</button>
            </form>
            <% } else if (c.getStatut() == model.Statut.PRETE) { %>
            <!-- Message indiquant que la commande est prête -->
            <button class="btn btn-success btn-sm" disabled>✔️ Commande prête</button>
            <% } %>
        </td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>

<%
    }
%>
<div class="back-link">
    <a href="commande?action=retour">🔙 Retour à la liste</a>
</div>

</body>
</html>