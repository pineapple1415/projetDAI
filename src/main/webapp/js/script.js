document.addEventListener("DOMContentLoaded", function () {
    console.log("Document ready, attaching event listener");

    document.getElementById("filter-container").addEventListener("click", function () {
        console.log("Filter button clicked");
        loadFilters();
        toggleFilterMenu();
    });

    fetchProduits();

    // 过滤器加载函数
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

    // **2️⃣ 过滤菜单的显示/隐藏**
    function toggleFilterMenu() {
        const filterMenu = document.getElementById("filterMenu");
        if (filterMenu.style.display === "none" || filterMenu.style.display === "") {
            filterMenu.style.display = "block";
        } else {
            filterMenu.style.display = "none";
        }
    }

    // **3️⃣ 监听产品列表的 "Add to Cart" 按钮**
    document.getElementById("productList").addEventListener("click", function (e) {
        if (e.target.closest('.add-btn')) {
            const button = e.target.closest('.add-btn');
            const productId = button.dataset.productId;
            const quantity = parseInt(button.dataset.quantity || 1);
            addToCart(productId, quantity);
        }
    });


    //**
// 统一事件监听（支持动态生成的按钮）
//document.getElementById('productList').addEventListener('click', function(e)
//   if(e.target.closest('.add-btn')) {
    //   const button = e.target.closest('.add-btn');
//      const productId = button.dataset.productId;
//        const quantity = parseInt(button.dataset.quantity || 1);
    //       addToCart(productId, quantity);
    // }
//});

// 修改后的addToCart函数
// 修改后的addToCart函数
    // **4️⃣ 购物车添加函数**
    function addToCart() {
        const productId = document.getElementById('productId').value;

        fetch(`${window.location.origin}/ProjetDAI_war/addToPanier`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ productId: productId, quantity: 1 })
        })
            .then(response => {
                if (response.redirected) {
                    window.location.href = response.url; // 跳转到成功页面
                }
            })
            .catch(error => console.error('Erreur:', error));
    }

    // 修改产品展示添加数据属性
    function displayProducts() {
        products.forEach(p => {
            div.innerHTML = `
        <button class="add-btn" 
            data-product-id="${p.id}"
            data-quantity="1">
            Add to Cart
        </button>
    `;
        });
    }


    function updateCartCount(count) {
        let cartCounter = document.getElementById("cartCount");
        if (cartCounter) {
            cartCounter.innerText = count;
        }
    }



    // **5️⃣ 显示产品列表**
    function fetchProduits() {
        var xhr = new XMLHttpRequest();

        xhr.onload = function () {
            if (xhr.status === 200) {
                var elt = document.getElementById("productList");
                var produits = JSON.parse(xhr.responseText);
                var ch = "";

                produits.forEach(function (produit) {
                    ch += `<div class="product-item">
                        <img src="${produit.imageUrl}" alt="${produit.nomProduit}" style="width: 150px; height: auto;"/>
                        <h3>${produit.nomProduit}</h3>
                        <p>Prix: ${produit.prixUnit}€</p>
                        <a href="\`${window.location.origin}/ProjetDAI_war/jsp/article.jsp" class="detail-link" data-nom="${produit.nomProduit}">Détail du produit</a>
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

    // **6️⃣ 存储选中的过滤条件到 sessionStorage**
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

    // **7️⃣ 过滤产品**
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

    document.getElementById("productList").addEventListener("click", function (e) {
        if (e.target.classList.contains("detail-link")) {
            e.preventDefault(); // 阻止默认跳转

            let nomProduit = e.target.dataset.nom; // 获取产品名称
            sessionStorage.setItem("nomProduit", nomProduit); // 存入 sessionStorage

            // 跳转到 article.jsp
            window.location.href = "/ProjetDAI_war/jsp/article.jsp";
        }
    });

});
