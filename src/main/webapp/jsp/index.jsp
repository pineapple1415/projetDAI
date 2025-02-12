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
        <input type="text" id="searchBox" placeholder="Rechercher un produit..." />
        <button id="filterButton">Filtrer</button>
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
