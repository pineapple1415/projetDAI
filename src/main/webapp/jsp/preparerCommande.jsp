<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, model.Composer, model.Commande" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Préparer Commande</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/preparateur.css">
    <script defer src="${pageContext.request.contextPath}/js/preparateur.js"></script>
</head>
<body>

<header>
    <h1>Préparation de la Commande #<%= request.getAttribute("commandeId") %></h1>
</header>

<main>
    <table>
        <tr>
            <th>ID Produit</th>
            <th>Nom Produit</th>
            <th>Prix Unitaire</th>
            <th>Quantité</th>
            <th>Action</th>
        </tr>
        <%
            List<Composer> produits = (List<Composer>) request.getAttribute("produits");
            for (Composer p : produits) {
        %>
        <tr>
            <td><%= p.getProduit().getIdProduit() %></td>
            <td><%= p.getProduit().getNomProduit() %></td>
            <td><%= p.getProduit().getPrixUnit() %>€</td>
            <td><%= p.getQuantite() %></td>
            <td><button class="confirmer-btn">Confirmer</button></td>
        </tr>
        <% } %>
    </table>

    <button id="finaliser-btn" disabled>Finaliser la préparation</button>
</main>

</body>
</html>
