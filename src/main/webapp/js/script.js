document.addEventListener("DOMContentLoaded", function () {
    console.log("Document ready, attaching event listener");

    // 新过滤功能初始化
    const filterContainer = document.getElementById("filter-container");
    if (filterContainer) {
        filterContainer.addEventListener("click", function () {
            console.log("Filter button clicked");

            // ✅ 仅在 `filterMenu` 为空时加载过滤器
            const filterMenu = document.getElementById("filterMenu");
            if (!filterMenu || filterMenu.innerHTML.trim() === "") {
                console.log("🔄 Chargement des filtres...");
                loadFilters();
            }

            // ✅ 切换菜单显示/隐藏
            toggleFilterMenu();
        });

    }
    fetchProduits();


    // 事件委托处理（整合点击事件）
    document.addEventListener('click', function(e) {
        // 处理购物车数量按钮
        if (e.target.closest('.quantity-btn')) {
            const btn = e.target.closest('.quantity-btn');
            const productId = btn.dataset.productId;
            const delta = parseInt(btn.dataset.delta);
            updateQuantity(productId, delta);
        }

        // 处理产品列表的Add按钮
        if (e.target.closest('.add-btn')) {
            const button = e.target.closest('.add-btn');
            const productId = button.dataset.productId;
            const quantity = parseInt(button.dataset.quantity || 1);
            addToCart(productId, quantity);
        }

        // 处理清空购物车
        if (e.target.closest('#clearCart')) {
            clearCart();
        }
    });

});


// 清空购物车函数（原有）
function clearCart() {
    if (confirm('确定要清空购物车吗？')) {
        fetch(`${window.location.origin}/ProjetDAI_war/annulerPanier`, {
            method: 'POST'
        }).then(() => window.location.reload())
            .catch(error => console.error('Error:', error));
    }
}

// 加载所有 Rayons（点击 "Filtrer" 按钮后调用）
function loadFilters() {
    fetch(`${window.location.origin}/ProjetDAI_war/filtrer?action=listRayons`, {
        method: "GET",
        headers: { "Accept": "application/json" }
    })
        .then(response => response.json())
        .then(data => {
            console.log("✅ Données reçues pour les rayons:", data);

            const filterMenu = document.getElementById("filterMenu");
            if (!filterMenu) {
                console.error("❌ ERREUR: filterMenu introuvable !");
                return;
            }

            // 清空过滤菜单
            filterMenu.innerHTML = "<h3>Rayons</h3>";

            if (Array.isArray(data.rayons) && data.rayons.length > 0) {
                data.rayons.forEach(rayon => {
                    let button = `<button class="rayon-button" data-rayon="${rayon}">${rayon}</button><br>`;
                    filterMenu.innerHTML += button;
                });
            } else {
                filterMenu.innerHTML += "<label>Aucun rayon trouvé</label>";
            }

            // 监听 Rayon 按钮点击事件
            document.querySelectorAll(".rayon-button").forEach(button => {
                button.addEventListener("click", function () {
                    let selectedRayon = this.getAttribute("data-rayon");
                    sessionStorage.setItem("selectedRayon", selectedRayon); // 存储选择的 Rayon
                    showCategories(selectedRayon);
                });
            });

            // 显示过滤菜单
            toggleFilterMenu(true);
        })
        .catch(error => console.error("❌ Erreur lors du chargement des rayons:", error));
}

// 显示选定 Rayon 的 Categories
function showCategories(rayon) {
    fetch(`${window.location.origin}/ProjetDAI_war/filtrer?action=listCategoriesByRayon&rayon=${encodeURIComponent(rayon)}`, {
        method: "GET",
        headers: { "Accept": "application/json" }
    })
        .then(response => response.json())
        .then(data => {
            console.log(`✅ Catégories pour Rayon "${rayon}":`, data);

            const filterMenu = document.getElementById("filterMenu");
            filterMenu.innerHTML = `<h3>Catégories (${rayon})</h3>`;

            if (Array.isArray(data.categories) && data.categories.length > 0) {
                data.categories.forEach(categorie => {
                    let checkbox = `<input type="checkbox" class="filter-checkbox" value="${categorie}">
                                <label>${categorie}</label><br>`;
                    filterMenu.innerHTML += checkbox;
                });
            } else {
                filterMenu.innerHTML += "<label>Aucune catégorie trouvée</label>";
            }

            // 添加按钮 "Appliquer le Filtre" 和 "Revenir au Rayon"
            filterMenu.innerHTML += `
            <button id="applyFilters">Appliquer le Filtre</button>
            <button id="backToRayons">Revenir au Rayon</button>
        `;

            // 监听 "Appliquer le Filtre"
            document.getElementById("applyFilters").addEventListener("click", applyFilters);

            // 监听 "Revenir au Rayon"
            document.getElementById("backToRayons").addEventListener("click", loadFilters);

            // 监听复选框状态更新
            document.querySelectorAll(".filter-checkbox").forEach(checkbox => {
                checkbox.addEventListener("change", updateSelectedCategories);
            });
        })
        .catch(error => console.error("❌ Erreur lors du chargement des catégories:", error));
}

// 更新选中的 Categories
function updateSelectedCategories() {
    let selected = [];
    document.querySelectorAll(".filter-checkbox:checked").forEach(checkbox => {
        selected.push(checkbox.value);
    });
    sessionStorage.setItem("selectedCategories", JSON.stringify(selected));
}

function applyFilters() {
    let selectedCategories = JSON.parse(sessionStorage.getItem("selectedCategories") || "[]");

    console.log("🔍 Catégories sélectionnées:", selectedCategories);

    if (selectedCategories.length === 0) {
        console.error("❌ Aucun filtre sélectionné !");
        return;
    }

    let params = new URLSearchParams();
    params.append("categories", selectedCategories.join(","));

    const url = `/ProjetDAI_war/filtrer?action=appliquerFiltre&${params.toString()}`;
    console.log("🔗 URL de la requête:", url);

    fetch(url, {
        method: "GET",
        headers: { "Accept": "application/json" }
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(`HTTP error! Status: ${response.status}, Message: ${text}`);
                });
            }
            return response.json();
        })
        .then(data => {
            console.log("📦 Produits filtrés:", data);

            const productList = document.getElementById("productList");
            productList.innerHTML = ""; // 清空原来的产品列表

            if (Array.isArray(data) && data.length > 0) {
                data.forEach(produit => {
                    let productHTML = `
                    <div class="product-item">
                        <img src="${produit.imageUrl}" alt="${produit.nomProduit}" style="width: 150px; height: auto;"/>
                        <h3>${produit.nomProduit}</h3>
                        <p>Prix: ${produit.prixUnit}€</p>
                        <a href="\`${window.location.origin}/ProjetDAI_war/jsp/article.jsp" 
                           class="detail-link" data-nom="${produit.nomProduit}">Détail du produit</a>
                    </div>`;
                    productList.innerHTML += productHTML;
                });
            } else {
                productList.innerHTML = "<p>Aucun produit trouvé</p>";
            }

            toggleFilterMenu(false);
        })
        .catch(error => {
            console.error("❌ Erreur lors du filtrage des produits:", error);
        });
}


// 切换过滤菜单的显示状态
function toggleFilterMenu() {
    const filterMenu = document.getElementById("filterMenu");
    if (!filterMenu) {
        console.error("❌ ERREUR: Element filterMenu introuvable !");
        return;
    }

    // ✅ 如果 `filterMenu` 是 "block"，则隐藏，否则显示
    if (filterMenu.style.display === "block") {
        filterMenu.style.display = "none";
    } else {
        filterMenu.style.display = "block";
    }
}




// 产品获取函数（整合版）
function fetchProduits() {
    var xhr = new XMLHttpRequest();

    xhr.onload = function () {
        if (xhr.status === 200) {
            var elt = document.getElementById("productList");

            if (!elt) {
                console.warn("productList元素不存在，跳过渲染");
                return;
            }

            var produits = JSON.parse(xhr.responseText);
            var ch = "";

            produits.forEach(function (produit) {
                ch += `<div class="product-item">
                        <img src="${produit.imageUrl}" alt="${produit.nomProduit}" style="width: 150px; height: auto;"/>
                        <h3>${produit.nomProduit}</h3>
                        <p>Prix: ${produit.prixUnit}€</p>
                        <a href="${window.location.origin}/ProjetDAI_war/jsp/article.jsp" class="detail-link" data-nom="${produit.nomProduit}">Détail du produit</a>
                    </div>`;
            });

            elt.innerHTML = ch;
        } else {
            console.error("Erreur lors de la récupération des produits. Statut :", xhr.status);
        }
    };

    xhr.open("GET", "/ProjetDAI_war/produits", true);
    xhr.setRequestHeader("Accept", "application/json");
    xhr.send();
}


// 过滤相关函数（新功能保持原样）
function updateSessionStorage() {
    let selectedFilters = { categories: [], rayons: [] };

    var checkboxes = document.getElementsByClassName("filter-checkbox");
    for (var i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].checked) {
            var type = checkboxes[i].getAttribute("data-type");
            var value = checkboxes[i].value;
            selectedFilters[type + "s"].push(value); // "categories" 或 "rayons"
        }
    }

    sessionStorage.setItem("selectedFilters", JSON.stringify(selectedFilters));
}



// 产品详情跳转处理（新功能）
document.getElementById("productList")?.addEventListener("click", function (e) {
    if (e.target.classList.contains("detail-link")) {
        e.preventDefault();
        sessionStorage.setItem("nomProduit", e.target.dataset.nom);
        window.location.href = "/ProjetDAI_war/jsp/article.jsp";
    }
});

document.querySelector(".product-container")?.addEventListener("click", function (e) {
    if (e.target.classList.contains("detail-link")) {
        e.preventDefault();
        sessionStorage.setItem("nomProduit", e.target.dataset.nom);

        // ✅ 确保 URL 生成正确
        const basePath = window.location.origin + "/" + window.location.pathname.split('/')[1];
        window.location.href = basePath + "/jsp/article.jsp";
    }
});



// 更新总价函数（原有）
function updateTotalPrice() {
    let total = 0;
    document.querySelectorAll('tr[data-product-id]').forEach(row => {
        const price = parseFloat(row.dataset.price);
        const quantity = parseInt(row.querySelector('.quantity').innerText);
        total += price * quantity;
    });
    const totalElement = document.getElementById('total-price');
    if (totalElement) {
        totalElement.innerText = '总价: €' + total.toFixed(2);
    }
}



function updateQuantity(productId, delta) {
    const idp = `tr[data-product-id="${productId}"]`;
    const row = document.querySelector(idp);
    if (!row) {
        console.error('错误：找不到商品ID对应的行', productId);
        return;
    }

    const quantityElement = row.querySelector('.quantity');
    const unitPrice = parseFloat(row.dataset.price);
    let newQuantity = parseInt(quantityElement.innerText) + delta;

    if (newQuantity < 1) newQuantity = 1;

    fetch(`${window.location.origin}/ProjetDAI_war/updateCart`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ productId: productId, quantity: newQuantity })
    }).then(response => {
        if (response.ok) {
            quantityElement.innerText = newQuantity;
            row.querySelector('.total-price').innerText =
                '€' + (unitPrice * newQuantity).toFixed(2);
            updateTotalPrice();
        }
    }).catch(error => console.error('请求失败:', error));
}




// 统一的addToCart函数（整合版）
function addToCart(productId, quantity = 1) {
    fetch(`${window.location.origin}/ProjetDAI_war/addToPanier`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ productId, quantity })
    }).then(response => {
        if (response.redirected) {
            window.location.href = response.url;
        } else if (response.ok) {
            updateCartCount(parseInt(document.getElementById("cartCount").innerText) + 1);
        }
    }).catch(error => console.error('Erreur:', error));
}




// 全局函数处理（原有需要保留的功能）
function updateCartCount(count) {
    const cartCounter = document.getElementById("cartCount");
    if (cartCounter) cartCounter.innerText = count;
}

// 产品展示函数（兼容处理）
function displayProducts(products, div) {
    div.innerHTML = products.map(p => `
        <button class="add-btn" 
            data-product-id="${p.id}"
            data-quantity="1">
            Add to Cart
        </button>
    `).join('');
}