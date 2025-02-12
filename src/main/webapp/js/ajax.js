function addToCart(productId, quantity = 1) {
    fetch('/ProjetDAI_war/addToPanier', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ productId, quantity })
    })
        .then(response => response.json())
        .then(data => {
            if (data.type === 'db') {
                fetch(`/getCartCount?userId=${data.userId}`)
                    .then(res => res.json())
                    .then(updateCartCount);
            } else {
                const panierCookie = document.cookie.split('; ').find(c => c.startsWith('panier='));
                if (panierCookie) {
                    const items = JSON.parse(decodeURIComponent(panierCookie.split('=')[1]));
                    updateCartCount(Object.values(items).reduce((a, b) => a + b, 0));
                }
            }
        })
        .catch(error => console.error('Error:', error));
}

function updateCartCount(count) {
    let cartCounter = document.getElementById("cartCount");
    if (cartCounter) {
        cartCounter.innerText = count;
    }
}

// 确保脚本正确加载
console.log("ajax.js loaded successfully");
