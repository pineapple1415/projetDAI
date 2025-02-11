document.addEventListener("DOMContentLoaded", function () {
    const productList = document.getElementById("productList");
    const searchBox = document.getElementById("searchBox");

    const filterButton = document.getElementById("filterButton");
    const filterMenu = document.getElementById("filterMenu");

    filterButton.addEventListener("click", function () {
            // 切换菜单的显示和隐藏
        if (filterMenu.style.display === "block") {
            filterMenu.style.display = "none";
        } else {
            filterMenu.style.display = "block";
        }

        // 点击其他地方时关闭菜单
    document.addEventListener("click", function (event) {
        if (!filterButton.contains(event.target) && !filterMenu.contains(event.target)) {
            filterMenu.style.display = "none";
        }
    });
});



    const products = [
        { id: 1, name: "T-shirt", price: "10", category: "Vetements", rayon: "Homme" },
        { id: 2, name: "Pantalon", price: "20", category: "Vetements", rayon: "Femme" },
        { id: 3, name: "Chaussures de sport", price: "50", category: "Chaussures", rayon: "Homme" },
        { id: 4, name: "Sac à main", price: "80", category: "Accessoires", rayon: "Femme" },
        { id: 5, name: "Manteau", price: "120", category: "Vetements", rayon: "Homme" },
        { id: 6, name: "Echarpe", price: "25", category: "Accessoires", rayon: "Femme" },
        { id: 7, name: "Baskets", price: "60", category: "Chaussures", rayon: "Homme" },
        { id: 8, name: "Jean slim", price: "40", category: "Vetements", rayon: "Femme" },
        { id: 9, name: "Ceinture en cuir", price: "35", category: "Accessoires", rayon: "Homme" },
        { id: 10, name: "Robe", price: "70", category: "Vetements", rayon: "Femme" },
        { id: 11, name: "Lunettes de soleil", price: "45", category: "Accessoires", rayon: "Unisexe" },
        { id: 12, name: "Bottes d'hiver", price: "85", category: "Chaussures", rayon: "Femme" },
        { id: 13, name: "Montre ", price: "150", category: "Accessoires", rayon: "Unisexe" }
    ];

    /**
     * 显示产品列表
     */
    function displayProducts() {
        productList.innerHTML = "";
        let selectedCategories = Array.from(document.querySelectorAll(".categoryFilter:checked")).map(cb => cb.value);
        let selectedRayons = Array.from(document.querySelectorAll(".rayonFilter:checked")).map(cb => cb.value);

        let filteredProducts = products.filter(p =>
            (selectedCategories.length === 0 || selectedCategories.includes(p.category)) &&
            (selectedRayons.length === 0 || selectedRayons.includes(p.rayon))
        );

        filteredProducts.forEach(p => {
            const div = document.createElement("div");
            div.className = "product-item";
            div.innerHTML = `
                <h3>${p.name}</h3>
                <p>Prix: ${p.price}</p>
                <a href="/product-details?id=${p.id}" class="details-link">Voir les détails</a>
            `;
            productList.appendChild(div);
        });
    }

    // 监听筛选框变化
    document.querySelectorAll(".categoryFilter, .rayonFilter").forEach(cb => {
        cb.addEventListener("change", displayProducts);
    });

    // 页面加载时显示所有产品
    displayProducts();
});
