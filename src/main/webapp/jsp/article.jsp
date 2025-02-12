<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Détails du produit</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css" />
    <style>
        /* 添加数量选择器样式 */
        .quantity-selector {
            margin: 15px 0;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .quantity-btn {
            width: 30px;
            height: 30px;
            border-radius: 50%;
            border: 1px solid #ccc;
            background: #f8f8f8;
            cursor: pointer;
        }
        #quantity {
            font-size: 18px;
            min-width: 40px;
            text-align: center;
        }
    </style>
</head>
<body>
<header>
    <button id="loginButton">Espace client</button>
</header>
<main>
    <h2>Détails du produit</h2>
    <div id="productDetail">
        <!-- 固定商品信息 -->
        <input type="hidden" id="productId" value="101">
        <h2 id="productName" data-product-id="101" data-product-name="T-shirt en coton">
            T-shirt en coton - 25€
        </h2>

        <!-- 数量选择器 -->
        <div class="quantity-selector">
            <button class="quantity-btn" onclick="updateQuantity(-1)">-</button>
            <span id="quantity">1</span>
            <button class="quantity-btn" onclick="updateQuantity(1)">+</button>
        </div>
    </div>

    <button type="button" id="ajoutePanier"
            data-product-id="101"
            data-product-name="T-shirt en coton">
        Ajouter au panier
    </button>
</main>

<script>
    // 数量控制逻辑
    let currentQuantity = 1;

    function updateQuantity(change) {
        currentQuantity = Math.max(1, currentQuantity + change);
        document.getElementById('quantity').textContent = currentQuantity;
    }

    // 修改后的加入购物车函数
    function addToCart() {
        const productId = document.getElementById('productId').value;
        const productName = document.getElementById('productName').dataset.productName;

        fetch('${pageContext.request.contextPath}/addToPanier', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                productId: productId,
                productName: productName,
                quantity: currentQuantity
            })
        })
            .then(response => response.json())
            .then(data => {
                if(data.type === 'db') {
                    alert('Ajouté au panier permanent !');
                } else {
                    alert('Panier temporaire mis à jour');
                }
                // 更新全局购物车数量显示
                if(typeof updateCartDisplay === 'function') {
                    updateCartDisplay();
                }
            })
            .catch(error => console.error('Erreur:', error));
    }

    // 绑定点击事件（替代onclick属性）
    document.getElementById('ajoutePanier').addEventListener('click', addToCart);
</script>

</body>
</html>