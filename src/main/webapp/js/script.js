document.addEventListener("DOMContentLoaded", function () {
    // 新过滤功能初始化
    console.log("Document ready, attaching event listener");
    const filterContainer = document.getElementById("filter-container");
    if (filterContainer) {
        filterContainer.addEventListener("click", function () {
            console.log("Filter button clicked");
            loadFilters();
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


// 过滤器加载函数（新功能保持原样）
function loadFilters() {
    fetch(`${window.location.origin}/ProjetDAI_war/filtrer?action=listfiltrer`, {
        method: "GET",
        headers: { "Accept": "application/json" }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("Données reçues pour les filtres:", data);

            const filterMenu = document.getElementById("filterMenu");
            if (!filterMenu) {
                console.error("❌ ERREUR: filterMenu introuvable !");
                return;
            }

            filterMenu.innerHTML = "";

            // 处理 Catégories
            filterMenu.innerHTML += "<h3>Catégories</h3>";
            if (Array.isArray(data.categories) && data.categories.length > 0) {
                data.categories.forEach(categorie => {
                    let checkbox = `<input type="checkbox" class="filter-checkbox" data-type="categorie" value="${categorie}">
                        <label>${categorie}</label><br>`;
                    filterMenu.innerHTML += checkbox;
                });
            } else {
                filterMenu.innerHTML += "<label>Aucune catégorie trouvée</label>";
            }

            // 处理 Rayons
            filterMenu.innerHTML += "<h3>Rayons</h3>";
            if (Array.isArray(data.rayons) && data.rayons.length > 0) {
                data.rayons.forEach(rayon => {
                    let checkbox = `<input type="checkbox" class="filter-checkbox" data-type="rayon" value="${rayon}">
                        <label>${rayon}</label><br>`;
                    filterMenu.innerHTML += checkbox;
                });
            } else {
                filterMenu.innerHTML += "<label>Aucun rayon trouvé</label>";
            }

            // 修复 label 颜色
            document.querySelectorAll("#filterMenu label").forEach(label => {
                label.style.display = "inline-block";
                label.style.visibility = "visible";
                label.style.color = "black";
            });

            // 添加按钮
            filterMenu.innerHTML += '<button id="applyFilters">Appliquer le Filtre</button>';

            // 监听复选框事件
            document.querySelectorAll(".filter-checkbox").forEach(checkbox => {
                checkbox.addEventListener("change", updateSessionStorage);
            });

            // 监听 "Appliquer le Filtre" 按钮点击事件
            document.getElementById("applyFilters").addEventListener("click", function () {
                applyFilters();
                toggleFilterMenu(); // 过滤后隐藏
            });
        })
        .catch(error => {
            console.error("❌ Erreur lors du chargement des données:", error);
        });
}


// 过滤菜单切换（新功能）
function toggleFilterMenu() {
    const filterMenu = document.getElementById("filterMenu");
    if (filterMenu.style.display === "none" || filterMenu.style.display === "") {
        filterMenu.style.display = "block";
    } else {
        filterMenu.style.display = "none";
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



function applyFilters() {
    let filters = sessionStorage.getItem("selectedFilters");

    console.log("🔍 Filtres récupérés depuis sessionStorage:", filters);

    if (!filters) {
        console.error("❌ Aucun filtre trouvé dans sessionStorage !");
        return;
    }

    const url = "/ProjetDAI_war/produits?filters=" + encodeURIComponent(filters);
    console.log("🔗 URL de la requête:", url);

    fetch(url, {
        method: "GET",
        headers: { "Accept": "application/json" }
    })
        .then(response => {
            console.log("📡 Réponse du serveur reçue, statut:", response.status);
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("📦 Données reçues:", data);

            const productList = document.getElementById("productList");
            productList.innerHTML = "";

            if (Array.isArray(data) && data.length > 0) {
                data.forEach(produit => {
                    let productHTML = `<div class="product-item">
                                <img src="${produit.imageUrl}" alt="${produit.nomProduit}" style="width: 150px; height: auto;"/>
                                <h3>${produit.nomProduit}</h3>
                                <p>Prix: ${produit.prixUnit}€</p>
                                <a href="\`${window.location.origin}/ProjetDAI_war/jsp/article.jsp" class="detail-link" data-nom="${produit.nomProduit}">Détail du produit</a>
                            </div>`;
                    productList.innerHTML += productHTML;
                });
            } else {
                productList.innerHTML = "<p>Aucun produit trouvé</p>";
            }
        })
        .catch(error => {
            console.error("❌ Erreur lors du filtrage des produits:", error);
        });
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