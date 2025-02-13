// 定义更新数量的函数
function updateQuantity(productId, delta) {
    // 获取商品行元素
    const idp = `tr[data-product-id="${productId}"]`
    const row = document.querySelector(idp);
    if (!row) {
        console.error('错误：找不到商品ID对应的行', productId);
        return;
    }

    // 获取当前数量和单价
    const quantityElement = row.querySelector('.quantity');
    const unitPrice = parseFloat(row.dataset.price);
    let newQuantity = parseInt(quantityElement.innerText) + delta;

    // 数量下限保护（至少为1）
    if (newQuantity < 1) newQuantity = 1;

    // 发送请求到后端更新数据
    fetch(`${window.location.origin}/ProjetDAI_war/updateCart`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ productId: productId, quantity: newQuantity })
    }).then(response => {
        if (response.ok) {
            // 更新本地显示的数值
            quantityElement.innerText = newQuantity;
            row.querySelector('.total-price').innerText =
                '€' + (unitPrice * newQuantity).toFixed(2);
            updateTotalPrice(); // 更新总价
        }
    }).catch(error => console.error('请求失败:', error));
}

// 更新总价函数
function updateTotalPrice() {
    let total = 0;
    document.querySelectorAll('tr[data-product-id]').forEach(row => {
        const price = parseFloat(row.dataset.price);
        const quantity = parseInt(row.querySelector('.quantity').innerText);
        total += price * quantity;
    });
    document.getElementById('total-price').innerText =
        '总价: €' + total.toFixed(2);
}





// 修改后的addToCart函数
function addToCart() {
    const productId = document.getElementById('productId').value;

    fetch(`${window.location.origin}/ProjetDAI_war/addToPanier`, {
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


function clearCart() {
    if (confirm('确定要清空购物车吗？')) {
        fetch(`${window.location.origin}/ProjetDAI_war/annulerPanier`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            }
        })
            .then(response => {

                    window.location.reload(); // 强制刷新页面

            })
            .catch(error => console.error('Error:', error));
    }
}