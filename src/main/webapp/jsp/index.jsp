<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Welcome</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css" />
    <!-- 引入 jQuery -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script defer src="${pageContext.request.contextPath}/js/script.js"></script>

</head>
<body>
<header>
    <div class="search-container">
        <form action="${pageContext.request.contextPath}/searchProduct" method="get">
            <input type="text" id="searchBox" name="nomProduit" placeholder="Rechercher un produit..." />
            <button id="searchButton" type="submit">Check</button>
        </form>
        <button id="filter-container">Filtrer</button>
        <div id="filterMenu" class="filter-menu">
            <h3>Catégories</h3>

            <h3>Rayons</h3>
        </div>


    </div>
    <button id="loginButton" onclick="window.location.href='jsp/login.jsp'">Login</button>
</header>

<main>
    <h2>Liste des Produits</h2>
    <div id="productList">
        <!-- Les produits seront affichés ici -->
    </div>

</main>

<script>
    // 统一事件监听（支持动态生成的按钮）
    document.getElementById('productList').addEventListener('click', function(e) {
        if(e.target.closest('.add-btn')) {
            const button = e.target.closest('.add-btn');
            const productId = button.dataset.productId;
            const quantity = parseInt(button.dataset.quantity || 1);
            addToCart(productId, quantity);
        }
    });
</script>
</body>
</html>
