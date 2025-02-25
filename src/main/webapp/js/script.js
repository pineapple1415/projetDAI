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

    console.log("Document ready, attaching event listener for Trier");

    const trierContainer = document.getElementById("trier-container");
    const sortAsc = document.getElementById("sortAsc");
    const sortDesc = document.getElementById("sortDesc");

    if (trierContainer) {
        trierContainer.addEventListener("click", function () {
            console.log("Trier button clicked");
            toggleTrierMenu();
        });
    }

    document.getElementById("sortAsc").addEventListener("click", function () {
        sortProduits("asc"); // 升序排序
    });

    document.getElementById("sortDesc").addEventListener("click", function () {
        sortProduits("desc"); // 降序排序
    });

    document.getElementById("promotion").addEventListener("click", togglePromotions);

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
            filterMenu.innerHTML = "<h3>Rayons</h3><ul id='rayonList'></ul>";

            const rayonList = document.getElementById("rayonList");

            if (Array.isArray(data.rayons) && data.rayons.length > 0) {
                data.rayons.forEach(rayon => {
                    let listItem = `<li><a href="#" class="rayon-link" data-rayon="${rayon}">${rayon}</a></li>`;
                    rayonList.innerHTML += listItem;
                });
            } else {
                filterMenu.innerHTML += "<label>Aucun rayon trouvé</label>";
            }

            // 监听 Rayon 点击事件
            document.querySelectorAll(".rayon-link").forEach(link => {
                link.addEventListener("click", function (event) {
                    event.preventDefault(); // 防止页面跳转
                    let selectedRayon = this.getAttribute("data-rayon");
                    showCategories(selectedRayon);
                });
            });

            // 显示过滤菜单
            toggleFilterMenu(true);
        })
        .catch(error => console.error("❌ Erreur lors du chargement des rayons:", error));
}

// 显示选定 Rayon 的 Categories，支持多个 Rayon 同时展开
function showCategories(rayon) {
    fetch(`${window.location.origin}/ProjetDAI_war/filtrer?action=listCategoriesByRayon&rayon=${encodeURIComponent(rayon)}`, {
        method: "GET",
        headers: { "Accept": "application/json" }
    })
        .then(response => response.json())
        .then(data => {
            console.log(`✅ Catégories pour Rayon "${rayon}":`, data);

            const rayonElement = document.querySelector(`.rayon-link[data-rayon="${rayon}"]`);
            if (!rayonElement) {
                console.error(`❌ Rayon introuvable: ${rayon}`);
                return;
            }

            let categorySectionId = `categories-${rayon.replace(/\s+/g, '-')}`;

            // 检查是否已展开
            if (document.getElementById(categorySectionId)) {
                console.log(`ℹ️ Les catégories pour "${rayon}" sont déjà affichées.`);
                return;
            }

            // 创建 Categories 容器（无 h3 标题）
            let categorySection = document.createElement("div");
            categorySection.id = categorySectionId;
            categorySection.style.marginLeft = "15px"; // 让 categories 和 Rayon 有间隔

            if (Array.isArray(data.categories) && data.categories.length > 0) {
                data.categories.forEach(categorie => {
                    let categoryItem = document.createElement("div");
                    categoryItem.style.display = "flex";
                    categoryItem.style.alignItems = "center";

                    let checkbox = document.createElement("input");
                    checkbox.type = "checkbox";
                    checkbox.className = "filter-checkbox";
                    checkbox.value = categorie;
                    checkbox.id = `checkbox-${categorie.replace(/\s+/g, '-')}`;
                    checkbox.addEventListener("change", updateSelectedCategories);

                    let label = document.createElement("label");
                    label.innerText = categorie;
                    label.htmlFor = checkbox.id;  // 绑定 label 和 checkbox
                    label.style.marginLeft = "5px";

                    categoryItem.appendChild(checkbox);
                    categoryItem.appendChild(label);
                    categorySection.appendChild(categoryItem);
                });
            }
            else {
                categorySection.innerHTML = "<label>Aucune catégorie trouvée</label>";
            }

            // 直接插入到 Rayon 下方
            rayonElement.insertAdjacentElement("afterend", categorySection);

            // 确保 "Appliquer le Filtre" 按钮存在
            let filterMenu = document.getElementById("filterMenu");
            if (!document.getElementById("applyFilters")) {
                let applyButton = document.createElement("button");
                applyButton.id = "applyFilters";
                applyButton.innerText = "Appliquer le Filtre";
                applyButton.style.marginTop = "10px";
                applyButton.style.display = "block"; // 确保是块级元素，不会错位
                applyButton.style.padding = "8px 12px";
                applyButton.style.backgroundColor = "#ff9800"; // 按钮颜色
                applyButton.style.color = "white";
                applyButton.style.border = "none";
                applyButton.style.borderRadius = "5px";
                applyButton.style.cursor = "pointer";

                applyButton.addEventListener("click", applyFilters);

                filterMenu.appendChild(applyButton);
            }
        })
        .catch(error => console.error("❌ Erreur lors du chargement des catégories:", error));
}

// 更新选中的 Categories
function updateSelectedCategories() {
    let selected = new Set(JSON.parse(sessionStorage.getItem("selectedCategories") || "[]"));

    document.querySelectorAll(".filter-checkbox:checked").forEach(checkbox => {
        selected.add(checkbox.value);
    });

    sessionStorage.setItem("selectedCategories", JSON.stringify([...selected]));
    console.log("✅ Catégories sélectionnées mises à jour:", [...selected]);
}

// 过滤产品
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
                // ✅ 存储筛选后的产品数据
                window.currentProduits = data;
                window.originalProduits = [...data]; // ✅ 备份数据，供“Afficher les promos” 按钮使用

                // ✅ 直接使用 `renderProduits(produits);` 进行渲染，保证折扣显示
                renderProduits(data);
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

            window.currentProduits = produits;
            window.originalProduits = [...produits]; // ✅ 备份原始数据

            renderProduits(produits);
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
    const unitPriceApresPromotion = parseFloat(row.dataset.pricereduit);
    let newQuantity = parseInt(quantityElement.innerText) + delta;

    if (newQuantity < 1) newQuantity = 1;

    fetch(`${window.location.origin}/ProjetDAI_war/updateCart`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ productId: productId, quantity: newQuantity })
    }).then(response => {
        if (response.ok) {
            quantityElement.innerText = newQuantity;
            row.querySelector('#total-price-avant').innerText =
                '€' + (unitPrice * newQuantity).toFixed(2);
            row.querySelector('#total-price-apres').innerText =
                '€' + (unitPriceApresPromotion * newQuantity).toFixed(2);
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

function toggleTrierMenu() {
    const trierMenu = document.getElementById("trierMenu");
    if (!trierMenu) {
        console.error("❌ ERREUR: Element trierMenu introuvable !");
        return;
    }

    // ✅ 确保 `display` 正确切换
    if (trierMenu.style.display === "none" || trierMenu.style.display === "") {
        trierMenu.style.display = "block";
    } else {
        trierMenu.style.display = "none";
    }
}


// ✅ 重新渲染产品到页面
// ✅ 重新渲染产品列表，始终显示折扣信息
function renderProduits(produits) {
    var elt = document.getElementById("productList");
    if (!elt) {
        console.warn("❌ ERREUR: `productList` 不存在");
        return;
    }

    elt.innerHTML = produits.map(produit => {
        // ✅ 仅当 promotion > 0 时显示折扣
        let promoBadge = produit.promotion > 0 ?
            `<div class="promotion-badge">-${(produit.promotion * 100).toFixed(0)}%</div>` : '';

        return `
            <div class="product-item">
                ${promoBadge}  
                <img src="${produit.imageUrl}" alt="${produit.nomProduit}" style="width: 150px; height: auto;"/>
                <h3>${produit.nomProduit}</h3>
                <p>Prix: ${produit.prixUnit}€</p>
                <a href="${window.location.origin}/ProjetDAI_war/jsp/article.jsp" class="detail-link" data-nom="${produit.nomProduit}">Détail du produit</a>
            </div>
        `;
    }).join('');
}


// ✅ 仅前端排序，不请求服务器
function sortProduits(order) {
    if (!window.currentProduits || window.currentProduits.length === 0) {
        console.warn("❌ Aucun produit à trier !");
        return;
    }

    let sortedProduits = [...window.currentProduits];
    if (order === "asc") {
        sortedProduits.sort((a, b) => a.prixUnit - b.prixUnit);
    } else if (order === "desc") {
        sortedProduits.sort((a, b) => b.prixUnit - a.prixUnit);
    }

    renderProduits(sortedProduits);
}

// ✅ 只显示打折商品
function togglePromotions() {
    let btn = document.getElementById("promotion");
    if (!btn) {
        console.error("❌ ERREUR: Bouton 'promotion' introuvable !");
        return;
    }

    // ✅ 如果当前已显示促销商品，则恢复所有商品
    if (btn.dataset.filter === "on") {
        renderProduits(window.originalProduits);
        btn.innerText = "Afficher les promos";
        btn.dataset.filter = "off";
    } else {
        // ✅ 过滤出打折商品
        let discountedProducts = window.originalProduits.filter(p => p.promotion > 0);
        renderProduits(discountedProducts);
        btn.innerText = "Afficher tous les produits";
        btn.dataset.filter = "on";
    }
}
