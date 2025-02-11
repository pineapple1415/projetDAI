document.addEventListener("DOMContentLoaded", function () {
    const productList = document.getElementById("productList");
    const searchBox = document.getElementById("searchBox");
    const filterButton = document.getElementById("filterButton");

    // 预设的产品数据（如果产品来自数据库，建议使用 AJAX 动态获取）
    const products = [
        { name: "Produit 1", price: "10€" },
        { name: "Produit 2", price: "15€" },
        { name: "Produit 3", price: "20€" },
        { name: "Produit 4", price: "25€" }
    ];

    /**
     * 显示产品列表
     * @param {string} filter 搜索过滤条件
     */
    function displayProducts(filter = "") {
        productList.innerHTML = ""; // 清空当前产品列表

        products
            .filter(p => p.name.toLowerCase().includes(filter.toLowerCase())) // 过滤产品
            .forEach(p => {
                const div = document.createElement("div");
                div.className = "product-item";
                div.innerHTML = `<h3>${p.name}</h3><p>Prix: ${p.price}</p>`;
                productList.appendChild(div);
            });
    }

    /**
     * 添加商品到购物车
     */
    function addToCart() {
        const productIdElement = document.getElementById("productId");
        const productNameElement = document.getElementById("productName");

        if (!productIdElement || !productNameElement) {
            alert("Erreur : produit non trouvé !");
            return;
        }

        let productId = productIdElement.value; // 产品 ID
        let productName = productNameElement.innerText; // 产品名称

        // 发送 AJAX 请求到 Servlet
        fetch(`${window.location.origin}/addToCart`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ productId: productId, productName: productName })
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert("Produit ajouté au panier avec succès !");
                    updateCartCount(data.cartCount); // 更新购物车数量
                } else {
                    alert("Erreur lors de l'ajout au panier.");
                }
            })
            .catch(error => console.error("Erreur AJAX:", error));
    }

    /**
     * 更新购物车数量
     * @param {number} count 购物车中的商品数量
     */
    function updateCartCount(count) {
        let cartCounter = document.getElementById("cartCount");
        if (cartCounter) {
            cartCounter.innerText = count;
        }
    }

    /**
     * 监听过滤按钮事件
     */
    filterButton.addEventListener("click", function () {
        displayProducts(searchBox.value);
    });

    // 页面加载时，显示所有产品
    displayProducts();
});
