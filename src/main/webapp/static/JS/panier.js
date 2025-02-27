
// 获取商品列表 get liste des produit dans le panier
function loadProducts() {
    fetch('/cart?action=listProducts')
        .then(response => response.json())
        .then(products => {
            const container = document.getElementById('product-list');
            products.forEach(product => {
                container.innerHTML += `
                    <div class="product">
                        <h3>${product.name}</h3>
                        <p>价格: ${product.price}</p>
                        <button onclick="addToPanier(${product.id})">Ajoute dans panier</button>
                    </div>
                `;
            });
        });
}

// 添加到购物车 ajouter dans le panier
function addToCart(productId, quantity) {
    fetch("/ProjetDAI_war/addToPanier",  {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            productId: productId,
            quantity: quantity
        })
    })
        .then(response => {
            if (response.redirected) {
                window.location.href = response.url;
            }
        })
        .catch(error => console.error('Erreur:', error));

    console.log('正在添加商品:', { productId, quantity });
}

// triggers
window.onload = () => {
    loadProducts();
    updateCartDisplay();
};
//to make this function golbal
window.addToCart = addToCart;


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