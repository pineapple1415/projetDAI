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
// 修改后的addToCart函数
function addToCart() {
    const productId = document.getElementById('productId').value;

    fetch('${pageContext.request.contextPath}/addToPanier', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            productId: productId,
            quantity: currentQuantity
        })
    })
        .then(response => {
            if(response.redirected) {
                window.location.href = response.url; // 跳转到成功页面
            }
        })
        .catch(error => console.error('Erreur:', error));
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