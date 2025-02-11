<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Detaille du produit</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css" />
    <script defer src="${pageContext.request.contextPath}/js/script.js"></script>
</head>
<body>
<header>
    <button id="loginButton">expace client</button>
</header>
<main>
    <h2>détaille du projet</h2>
    <div id="productList">
        <!-- Les produits seront affichés ici -->
        <input type="hidden" id="product" value="101"> <!-- 这里的值应该动态填充 -->
        <h2 id="productName">T-shirt en coton</h2>


    </div>

    <button type="button" onclick="addToCart()">Add to Cart</button>

</main>
</body>
</html>