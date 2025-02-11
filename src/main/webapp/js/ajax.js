/**
 * 处理所有 AJAX 请求
 */
const API_BASE_URL = window.location.origin;

/**
 * 获取产品数据 (从 Servlet `/api/products` 获取)
 */
function fetchProducts(callback) {
    fetch(`${window.location.origin}/ProjetDAI_war/products`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("API erreur:", data);
            callback(data);
        })
        .catch(error => console.error("ne pas catching la list:", error));
}


/**
 * 添加商品到购物车 (POST 请求到 `/addToCart`)
 */
function addToCart(productId, productName, callback) {
    fetch(`${API_BASE_URL}/addToCart`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ productId, productName })
    })
        .then(response => response.json())
        .then(data => callback(data))
        .catch(error => console.error("Erreur AJAX:", error));
}
