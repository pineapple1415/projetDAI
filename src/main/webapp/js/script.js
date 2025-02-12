document.addEventListener("DOMContentLoaded", function () {
    const productList = document.getElementById("productList");
    const filterButton = document.getElementById("filterButton");
    const filterMenu = document.getElementById("filterMenu");

    // 切换筛选菜单显示状态
    filterButton.addEventListener("click", function () {
        filterMenu.style.display = (filterMenu.style.display === "block") ? "none" : "block";
    });

    // 点击其他地方时关闭菜单
    document.addEventListener("click", function (event) {
        if (!filterButton.contains(event.target) && !filterMenu.contains(event.target)) {
            filterMenu.style.display = "none";
        }
    });

    document.getElementById('productList').addEventListener('click', function(e) {
        if (e.target.closest('.add-btn')) {
            const button = e.target.closest('.add-btn');
            addToCart(button.dataset.productId, parseInt(button.dataset.quantity || 1));
        }
    });

    document.querySelectorAll(".categoryFilter, .rayonFilter").forEach(cb => {
        cb.addEventListener("change", displayProducts);
    });

    displayProducts();
});

// 引入 ajax.js 中的函数
if (typeof addToCart === 'undefined' || typeof updateCartCount === 'undefined') {
    console.error("ajax.js 未正确加载，请检查文件路径。");
}
