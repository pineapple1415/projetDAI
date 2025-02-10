
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
function addToPanier(IdProduit) {
    fetch('/cart', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ action: 'add', IdProduit: IdProduit })
    }).then(response => updateCartDisplay());
}

// triggers
window.onload = () => {
    loadProducts();
    updateCartDisplay();
};