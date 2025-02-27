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

    // Récupérer les commandes sélectionnées depuis la session
    String[] commandesSelectionnees = (String[]) session.getAttribute("commandesSelectionnees");

    // Suppression de l'attribut si l'utilisateur a validé une action précédente
    session.removeAttribute("commandesSelectionnees");
%>

<html>
<head>
    <title>Commandes à Préparer</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
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

        .text-center {
            text-align: center;
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

<!-- ✅ Formulaire pour soumettre les commandes sélectionnées -->
<form action="ServletConfirmerSelection" method="post">
    <table class="table table-bordered table-striped">
        <thead class="table-dark">
        <tr>
            <th>Priorité</th>
            <th>Date</th>
            <th>Statut</th>
            <th>Prix Total (€)</th>
            <th>Magasin</th>
            <th>Adresse</th>
            <th>Détails sur la Commande</th>
            <th>Modifier la commande</th>
            <th>Sélectionner</th>
        </tr>
        </thead>
        <tbody>
        <%
            int priorite = 1;
            for (Commande c : commandes) {
                Magasin magasin = c.getMagasin();
                boolean isChecked = false;

                // Vérifier si la commande est dans la liste des commandes sélectionnées
                if (commandesSelectionnees != null) {
                    for (String id : commandesSelectionnees) {
                        if (id.equals(String.valueOf(c.getIdCommande()))) {
                            isChecked = true;
                            break;
                        }
                    }
                }
        %>
        <tr>
            <td><%= priorite++ %></td>
            <td><%= c.getDateCommande() %></td>
            <td><span class="badge bg-primary"><%= c.getStatut() %></span></td>
            <td><%= c.getPrixTotal() %> €</td>
            <td><%= (magasin != null) ? magasin.getNomMagasin() : "Non renseigné" %></td>
            <td><%= (magasin != null) ? magasin.getAdresseMagasin() : "Non renseigné" %></td>
            <td>
                <a href="commande?action=consulter&id=<%= c.getIdCommande() %>" class="btn btn-info btn-sm">🔍 Détails</a>
            </td>
            <td>
                <% if (c.getStatut() == model.Statut.PAYEE) { %>
                <form action="commande" method="post" class="d-inline">
                    <input type="hidden" name="action" value="marquerPreparation">
                    <input type="hidden" name="idCommande" value="<%= c.getIdCommande() %>">
                    <button type="submit" class="btn btn-warning btn-sm">⏳ Marquer en préparation</button>
                </form>
                <% } if (c.getStatut() == model.Statut.EN_COURS) { %>
                <form action="commande" method="post" class="d-inline">
                    <input type="hidden" name="action" value="finaliserPreparation">
                    <input type="hidden" name="idCommande" value="<%= c.getIdCommande() %>">
                    <button type="submit" class="btn btn-success btn-sm">✅ Finaliser Commande</button>
                </form>
                <% } else if (c.getStatut() == model.Statut.PRETE) { %>
                <button class="btn btn-success btn-sm" disabled>✔️ Commande prête</button>
                <% } %>
            </td>
            <td class="text-center">
                <input type="checkbox" name="commandesSelectionnees" value="<%= c.getIdCommande() %>" <%= isChecked ? "checked" : "" %>>
            </td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>

    <div class="d-flex justify-content-between mt-3">
        <a href="commande?action=retour" class="btn btn-secondary">
            <i class="fa-solid fa-arrow-left"></i> Accueil
        </a>
        <button type="submit" class="btn btn-primary">
            <i class="fa-solid fa-check"></i> Selectionnez des Commandes
        </button>
    </div>
</form>

<%
    }
%>

</body>
</html>
