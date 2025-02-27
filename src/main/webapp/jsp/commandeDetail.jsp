<%--
  Created by IntelliJ IDEA.
  User: rachi
  Date: 25/02/2025
  Time: 00:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%@ page import="model.Commande" %>
<%@ page import="model.Composer" %>
<%@ page import="model.Produit" %>
<%@ page import="model.Client" %>
<%

    // Récupération de la commande depuis l'attribut "commande"
    Commande commande = (Commande) request.getAttribute("commande");

    if (commande == null) {
%>
<h2>Commande introuvable.</h2>
<a href="commande?action=listePreparations">Retour à la liste</a>
<%
} else {
    // Récupération des produits associés à la commande (Set<Composer>)
    Set<Composer> produitsCommande = commande.getComposers();
    // Récupération des informations du client
    Client client = (Client) commande.getClient();
%>
<html>
<head>
    <title>Détails de la commande #<%= commande.getIdCommande() %></title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f9;
        }

        .container {
            display: flex;
            justify-content: space-between;
            padding: 20px;
            background-color: #ffffff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .client-info, .commande-info {
            width: 45%;
        }

        h2 {
            text-align: center;
        }

        .client-info p, .commande-info p {
            font-size: 16px;
            line-height: 1.6;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 10px;
            text-align: center;
        }

        th {
            background-color: #f2f2f2;
        }

        .total {
            font-weight: bold;
            color: green;
        }

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
<body>
<div class="container">
    <!-- Section Client -->
    <div class="client-info">
        <h3>Information Personnelle Client :</h3>
        <%
            if (client != null) {
        %>
        <p><strong>Nom Complet:</strong> <%= client.getNom() %> <%= client.getPrenom() %></p>
        <p><strong>Email :</strong> <%= client.getEmail() %></p>
        <p><strong>Téléphone :</strong> <%= client.getTelephone() %></p>
        <p><strong>Adresse :</strong> <%= client.getAdresse() %></p>
        <p><strong>Code Postale :</strong> <%= client.getCodePostal() %></p>
        <%
            }
        %>
    </div>

    <!-- Section Commande -->
    <div class="commande-info">
        <h2>Commande #<%= commande.getIdCommande() %></h2>
        <p><strong>Statut :</strong> <%= commande.getStatut() %></p>
        <p><strong>Date de commande :</strong> <%= commande.getDateCommande() %></p>

        <%
            if (commande.getMagasin() != null) {
        %>
        <p><strong>Magasin :</strong> <%= commande.getMagasin().getNomMagasin() %> - <%= commande.getMagasin().getAdresseMagasin() %></p>
        <%
            }
        %>

        <h3>🛒 Produits commandés :</h3>

        <table>
            <thead>
            <tr>
                <th>Nom du Produit</th>
                <th>Quantité</th>
                <th>Prix Unitaire (€)</th>
                <th>Total (€)</th>
            </tr>
            </thead>
            <tbody>
            <%
                double totalCommande = 0;
                // Vérifier si le Set de produitsCommande n'est pas vide
                if (produitsCommande != null && !produitsCommande.isEmpty()) {
                    for (Composer composition : produitsCommande) {
                        Produit produit = composition.getProduit();
                        int quantite = composition.getQuantite();
                        double prixUnitaire = produit.getPrixUnit();
                        double totalLigne = quantite * prixUnitaire;
                        totalCommande += totalLigne;
            %>
            <tr>
                <td><%= produit.getNomProduit() %></td>
                <td><%= quantite %></td>
                <td><%= prixUnitaire %> €</td>
                <td><%= totalLigne %> €</td>
            </tr>
            <%
                }
            } else {
            %>
            <tr>
                <td colspan="4">Aucun produit dans cette commande.</td>
            </tr>
            <%
                }
            %>
            </tbody>
        </table>

        <h3 class="total">💰 Prix Total de la Commande : <%= totalCommande %> €</h3>
    </div>
</div>

<div class="back-link">
    <a href="commande?action=listePreparations">🔙 Retour à la liste</a>
</div>
</body>
</html>
<%
    }
%>