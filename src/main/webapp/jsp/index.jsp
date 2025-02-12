<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Welcome</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css" />
    <script defer src="${pageContext.request.contextPath}/js/script.js"></script>

</head>
<body>
<header>
    <div class="search-container">
        <form action="${pageContext.request.contextPath}/searchProduct" method="get">
            <input type="text" id="searchBox" name="nomProduit" placeholder="Rechercher un produit..." />
            <button type="submit">Filtrer</button>
        </form>

    </div>
    <button id="loginButton" onclick="window.location.href='jsp/login.jsp'">Login</button>
</header>

<main>
    <h2>Liste des Produits</h2>
    <div id="productList">
        <!-- Les produits seront affichés ici -->
    </div>

</main>
</body>
</html>
