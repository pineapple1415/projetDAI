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

    // Initial display
    displayProducts();

    // Filter products
    filterButton.addEventListener("click", function () {
        displayProducts(searchBox.value);
    });
});
