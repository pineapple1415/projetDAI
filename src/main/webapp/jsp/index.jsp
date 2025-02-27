<%@ page import="model.User" %>
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
        <div id="filterMenu" class="filter-menu"> </div>
        <button id="trier-container">Trier</button>
        <div id="trierMenu" class="trierMenu">
            <button id="sortAsc">Croissant par prix</button>
            <button id="sortDesc">Décroissant par prix</button>
        </div>


    </div>


    <div id="authSection">
        <%
            User user = (User) session.getAttribute("user");
            if(user == null) {
        %>
        <!-- 未登录时显示登录按钮 -->
        <button id="loginButton" onclick="window.location.href='jsp/login.jsp'">Login</button>

        <%
        } else {
        %>
        <!-- 已登录时显示欢迎信息 -->
        <a href="${pageContext.request.contextPath}/client" style="text-decoration: none;">
            <span style="color: navy; font-weight: bold;">
                Bonjour, <%= user.getNom() %>
            </span>
        </a>

        <%
            }
        %>
    </div>


</header>

<main>
    <h2>Liste des Produits</h2>
    <%
        HttpSession currentSession = request.getSession(false);
        User loggedInUser = (currentSession != null) ? (User) currentSession.getAttribute("user") : null;

        // ✅ 修正：确保 userId 声明，即使 null 也不会报错
        Integer userId = (loggedInUser != null) ? loggedInUser.getIdUser() : null;
    %>



    <button id="promotion">Afficher les promos</button>

    <div class="filter-container">
        <% if (userId != null) { %>
        <button id="sortPurchased">Mes Frequents</button>
        <% } %>
    </div>

    <div id="productList">
        <!-- Les produits seront affichés ici -->
    </div>

</main>


<script>
    // 统一事件监听（支持动态生成的按钮）
    // document.getElementById('productList').addEventListener('click', function(e) {
    //     if(e.target.closest('.add-btn')) {
    //         const button = e.target.closest('.add-btn');
    //         const productId = button.dataset.productId;
    //         const quantity = parseInt(button.dataset.quantity || 1);
    //         addToCart(productId, quantity);
    //     }
    // });
</script>
</body>
</html>
