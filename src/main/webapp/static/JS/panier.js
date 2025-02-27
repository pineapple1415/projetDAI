
// 获取商品列表 get liste des produit dans le panier
// ✅ 修正 loadProducts() 方法
function loadProducts() {
    fetch('/cart?action=listProducts')
        .then(response => response.json())
        .then(data => {
            const container = document.getElementById('product-list');
            container.innerHTML = ""; // 确保不会重复渲染

            // **修正 items 遍历**
            data.items.forEach(item => {
                container.innerHTML += `
                    <div class="product">
                        <h3>${item.produit.nom}</h3>
                        <p>prix: ${item.produit.prixUnit} €</p>
                        <p>quantite: ${item.quantity}</p>
                        <button onclick="addToCart(${item.produit.idProduit}, 1)">➕ 增加</button>
                        <button onclick="removeFromCart(${item.produit.idProduit})">➖ 减少</button>
                    </div>
                `;
            });

            // **更新总价显示**
            document.getElementById('cartTotal').textContent = `总价: ${data.total} €`;
        })
        .catch(error => console.error("❌ 加载购物车失败:", error));
}

/// **将 `addToCart` 改成 async 版本**
function addToCartAsync(productId, quantity) {
    return fetch("/ProjetDAI_war/addToPanier", {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            productId: productId,
            quantity: quantity
        })
    }).then(response => {
        if (response.redirected) {
            window.location.href = response.url;
        }
    }).catch(error => console.error('Erreur:', error));
}
//to make this function golbal
window.addToCart = addToCart;

// triggers
window.onload = () => {
    loadProducts();
    updateCartDisplay();
};



// 统一Cookie处理函数
function getPanierData() {
    const panierCookie = document.cookie.split('; ')
        .find(c => c.startsWith('panier='));
    if(!panierCookie) return {};

    try {
        return JSON.parse(
            decodeURIComponent(panierCookie.split('=')[1])
        );
    } catch {
        return {};
    }
}

// 更新购物车显示
function updateCartDisplay() {
    const panierData = getPanierData();
    const totalCount = Object.values(panierData).reduce((a,b) => a+b, 0);
    document.getElementById('cartCount').textContent = totalCount;
}