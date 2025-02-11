document.addEventListener("DOMContentLoaded", function () {
    const productList = document.getElementById("productList");
    const searchBox = document.getElementById("searchBox");
    const filterButton = document.getElementById("filterButton");

    // Simulated product data
    const products = [
        { name: "Produit 1", price: "10€" },
        { name: "Produit 2", price: "15€" },
        { name: "Produit 3", price: "20€" },
        { name: "Produit 4", price: "25€" }
    ];

    // Function to display products
    function displayProducts(filter = "") {
        productList.innerHTML = "";
        products
            .filter(p => p.name.toLowerCase().includes(filter.toLowerCase()))
            .forEach(p => {
                const div = document.createElement("div");
                div.className = "product-item";
                div.innerHTML = `<h3>${p.name}</h3><p>Prix: ${p.price}</p>`;
                productList.appendChild(div);
            });
    }

    function addToCart() {
        // 假设产品 ID 和名称可以通过 HTML 元素获取（你可以动态设置）
        let productId = document.getElementById("productId").value; // 产品 ID
        let productName = document.getElementById("productName").innerText; // 产品名称

        // 发送 AJAX 请求到 Servlet
        fetch('${pageContext.request.contextPath}/addToCart', {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ productId: productId, productName: productName })
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert("Produit ajouté au panier avec succès !");
                    // 可选：更新购物车 UI
                    updateCartCount(data.cartCount);
                } else {
                    alert("Erreur lors de l'ajout au panier.");
                }
            })
            .catch(error => console.error("Erreur AJAX:", error));
    }

// 更新购物车数量
    function updateCartCount(count) {
        let cartCounter = document.getElementById("cartCount");
        if (cartCounter) {
            cartCounter.innerText = count;
        }
    }

    // Initial display
    displayProducts();

    // Filter products
    filterButton.addEventListener("click", function () {
        displayProducts(searchBox.value);
    });
});
