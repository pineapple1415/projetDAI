<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Produit" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Résultats de la recherche</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/articleMotCle.css">
</head>
<body>
<header>
    <h1>Résultats de la recherche</h1>
    <button onclick="window.location.href='index.jsp'">Retour à l'accueil</button>
</header>

<main>
    <div class="product-container">
        <%
            List<Produit> produits = (List<Produit>) request.getAttribute("produits");
            if (produits == null || produits.isEmpty()) {
        %>
        <p class="no-results">Aucun produit trouvé.</p>
        <%
        } else {
            for (Produit produit : produits) {
        %>
        <div class="product-card">
            <img src="<%= produit.getImageUrl() %>" alt="<%= produit.getNomProduit() %>">
            <p class="product-name"><%= produit.getNomProduit() %></p>
        </div>
        <%
                }
            }
        %>
    </div>
</main>

</body>
</html>
