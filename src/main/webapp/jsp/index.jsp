<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Welcome</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css" />
</head>
<body>
<header>
    <div class="search-container">
        <input type="text" id="searchBox" placeholder="Rechercher un produit..." />
        <button id="searchButton">check</button>
        <div class="filter-wrapper">
            <button id="filterButton">Filtrer</button>
            <div id="filterMenu" class="filter-menu">
                <h3>Catégories</h3>
                <label><input type="checkbox" value="Vetements"> Vêtements</label>
                <label><input type="checkbox" value="Chaussures"> Chaussures</label>
                <label><input type="checkbox" value="Accessoires"> Accessoires</label>

                <h3>Rayons</h3>
                <label><input type="checkbox" value="Homme"> Homme</label>
                <label><input type="checkbox" value="Femme"> Femme</label>
                <label><input type="checkbox" value="Enfant"> Enfant</label>
            </div>
        </div>

    </div>
    <button id="loginButton">Login</button>
    <span id="cartCount">cartCount:0</span> <!-- 购物车数量显示 -->
</header>

<main>
    <h2>Liste des Produits</h2>
    <div id="productList">
        <!-- 产品将在这里动态添加 -->
    </div>
</main>

<!-- 先加载 AJAX 逻辑，再加载 UI 逻辑 -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/ajax.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/script.js"></script>

</body>
</html>
