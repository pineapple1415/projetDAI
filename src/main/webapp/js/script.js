document.addEventListener("DOMContentLoaded", function () {
    const productList = document.getElementById("productList");
    const searchBox = document.getElementById("searchBox");
    const filterButton = document.getElementById("filterButton");

    let products = []; // 用于存储从后端获取的产品数据

    /**
     * 获取产品数据 (AJAX 从 Servlet `/api/products` 获取)
     */
    function fetchProducts() {
        fetch(`${window.location.origin}/api/products`) // 访问 Servlet
            .then(response => response.json())
            .then(data => {
                products = data; // 存储数据
                displayProducts(); // 显示产品
            })
            .catch(error => console.error("获取产品列表失败:", error));
    }

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
                div.innerHTML = `
                    <h3>${p.name}</h3>
                    <p>Prix: ${p.price}€</p>
                    <button class="addToCartBtn" data-id="${p.id}" data-name="${p.name}">Ajouter au panier</button>
                `;
                productList.appendChild(div);
            });

        // 重新绑定“添加到购物车”按钮的点击事件
        bindAddToCartEvents();
    }

    /**
     * 绑定"添加到购物车"按钮的点击事件
     */
    function bindAddToCartEvents() {
        document.querySelectorAll(".addToCartBtn").forEach(button => {
            button.addEventListener("click", function () {
                let productId = this.getAttribute("data-id");
                let productName = this.getAttribute("data-name");
                addToCart(productId, productName);
            });
        });
    }

    /**
     * 添加商品到购物车 (POST 请求到 Servlet `/addToCart`)
     */
    function addToCart(productId, productName) {
        fetch(`${window.location.origin}/addToCart`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ productId: productId, productName: productName })
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert("Produit ajouté au panier avec succès !");
                    updateCartCount(data.cartCount);
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

    // 页面加载时，从后端获取数据
    fetchProducts();
});
