// 统一事件监听（支持动态生成的按钮）
document.getElementById('productList').addEventListener('click', function(e) {
    if(e.target.closest('.add-btn')) {
        const button = e.target.closest('.add-btn');
        const productId = button.dataset.productId;
        const quantity = parseInt(button.dataset.quantity || 1);
        addToCart(productId, quantity);
    }
});

// 修改后的addToCart函数
function addToCart(productId, quantity = 1) {
    fetch('/ProjetDAI_war/addToPanier', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            productId: productId,
            quantity: quantity
        })
    })
        .then(response => response.json())
        .then(data => {
            if(data.type === 'db') {
                // 登录用户：更新全局购物车数量
                fetch(`/getCartCount?userId=${data.userId}`)
                    .then(res => res.json())
                    .then(updateCartCount);
            } else {
                // 未登录用户：解析Cookie更新数量
                const panierCookie = document.cookie.split('; ')
                    .find(c => c.startsWith('panier='));
                if(panierCookie) {
                    const items = JSON.parse(decodeURIComponent(panierCookie.split('=')[1]));
                    updateCartCount(Object.values(items).reduce((a,b) => a+b, 0));
                }
            }
        })
        .catch(error => console.error('Error:', error));
}

// 修改产品展示添加数据属性
function displayProducts() {
    products.forEach(p => {
        div.innerHTML = `
            <button class="add-btn" 
                data-product-id="${p.id}"
                data-quantity="1">
                Add to Cart
            </button>
        `;
    });
}


function updateCartCount(count) {
    let cartCounter = document.getElementById("cartCount");
    if (cartCounter) {
        cartCounter.innerText = count;
    }
}