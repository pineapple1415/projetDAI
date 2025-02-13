// 统一事件监听（支持动态生成的按钮）
document.getElementById('productList').addEventListener('click', function(e) {
    if(e.target.closest('.add-btn')) {
        const button = e.target.closest('.add-btn');
        const productId = button.dataset.productId;
        const quantity = parseInt(button.dataset.quantity || 1);
        addToCart(productId, quantity);
    }
});

// 修改后的addToCart函数
// 修改后的addToCart函数
// function addToCart() {
//     const productId = document.getElementById('productId').value;
//
//     fetch('${pageContext.request.contextPath}/addToPanier', {
//         method: 'POST',
//         headers: { 'Content-Type': 'application/json' },
//         body: JSON.stringify({
//             productId: productId,
//             quantity: currentQuantity
//         })
//     })
//         .then(response => {
//             if(response.redirected) {
//                 window.location.href = response.url; // 跳转到成功页面
//             }
//         })
//         .catch(error => console.error('Erreur:', error));
// }


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


    $(document).ready(function () {
        // 监听 Filtrer 按钮点击事件
        $("#filter-container").click(function () {
            loadFilters();
        });

        fetchProduits();

        // 监听产品列表中的 "Add to Cart" 按钮点击事件
        document.getElementById('productList').addEventListener('click', function (e) {
            if (e.target.closest('.add-btn')) {
                const button = e.target.closest('.add-btn');
                const productId = button.dataset.productId;
                const quantity = parseInt(button.dataset.quantity || 1);
                addToCart(productId, quantity);
            }
        });
    });

/**
 * Cette méthode "Ajax" affiche la liste des produits.
 */
function fetchProduits() {
    // Création de l'objet XMLHttpRequest
    var xhr = new XMLHttpRequest();

    // On précise ce que l'on va faire quand on aura reçu la réponse du serveur
    xhr.onload = function () {
        // Si la requête HTTP a réussi (status 200)
        if (xhr.status === 200) {
            // Sélectionne l'élément HTML où afficher les produits
            var elt = document.getElementById("productList");

            // Analyse la réponse XML
            var tableau = xhr.responseXML.getElementsByTagName("produit");
            var ch = "";

            // Boucle à travers chaque élément <produit>
            for (var i = 0; i < tableau.length; i++) {
                var nomProduit = tableau[i].getElementsByTagName("nomProduit")[0].firstChild.nodeValue;
                var prixUnit = tableau[i].getElementsByTagName("prixUnit")[0].firstChild.nodeValue;

                // Ajoute chaque produit sous forme de carte HTML
                ch += `<div class="product-item">
                        <h3>${nomProduit}</h3>
                        <p>Prix: ${prixUnit}€</p>
                    </div>`;
            }

            // Insère les produits dans la page
            elt.innerHTML = ch;
        } else {
            console.error("Erreur lors de la récupération des produits. Statut :", xhr.status);
        }
    };

    // Configuration de la requête (GET vers Servlet)
    xhr.open("GET", "/ProjetDAI_war/produits", true);

    // Envoi de la requête au serveur
    xhr.send();
}

function loadFilters() {
    $.ajax({
        url: "filtrer",
        type: "GET",
        data: { action: "listfiltrer" },
        dataType: "json",
        success: function (data) {
            $("#filterMenu").empty(); // 清空旧内容

            // 处理 Catégories
            $("#filterMenu").append("<h3>Catégories</h3>");
            if (data.categories.length > 0) {
                data.categories.forEach(function (categorie) {
                    let checkbox = `<input type="checkbox" class="filter-checkbox" data-type="categorie" value="${categorie.nom}">
                                    <label>${categorie.nom}</label><br>`;
                    $("#filterMenu").append(checkbox);
                });
            } else {
                $("#filterMenu").append("<label>Aucune catégorie trouvée</label>");
            }

            // 处理 Rayons
            $("#filterMenu").append("<h3>Rayons</h3>");
            if (data.rayons.length > 0) {
                data.rayons.forEach(function (rayon) {
                    let checkbox = `<input type="checkbox" class="filter-checkbox" data-type="rayon" value="${rayon.nom}">
                                    <label>${rayon.nom}</label><br>`;
                    $("#filterMenu").append(checkbox);
                });
            } else {
                $("#filterMenu").append("<label>Aucun rayon trouvé</label>");
            }

            // 添加按钮
            $("#filterMenu").append('<button id="applyFilters">Appliquer le Filtre</button>');

            // 监听复选框事件
            $(".filter-checkbox").on("change", function () {
                updateSessionStorage();
            });

            // 监听按钮点击事件
            $("#applyFilters").on("click", function () {
                applyFilters();
            });
        },
        error: function (xhr, status, error) {
            console.error("Erreur lors du chargement des données:", error);
        }
    });
}

// 存储选中的过滤条件到 sessionStorage
function updateSessionStorage() {
    let selectedFilters = { categories: [], rayons: [] };

    $(".filter-checkbox:checked").each(function () {
        let type = $(this).data("type");
        let value = $(this).val();
        selectedFilters[type + "s"].push(value); // "categories" 或 "rayons"
    });

    sessionStorage.setItem("selectedFilters", JSON.stringify(selectedFilters));
}

function applyFilters() {
    let filters = sessionStorage.getItem("selectedFilters");

    $.ajax({
        url: "/ProjetDAI_war/produits",
        type: "GET",
        data: { filters: filters }, // 发送过滤条件
        dataType: "xml",
        success: function (xml) {
            let elt = $("#productList");
            elt.empty();

            let produits = $(xml).find("produit");
            if (produits.length > 0) {
                produits.each(function () {
                    let nomProduit = $(this).find("nomProduit").text();
                    let prixUnit = $(this).find("prixUnit").text();

                    let productHTML = `<div class="product-item">
                                        <h3>${nomProduit}</h3>
                                        <p>Prix: ${prixUnit}€</p>
                                    </div>`;
                    elt.append(productHTML);
                });
            } else {
                elt.html("<p>Aucun produit trouvé</p>");
            }
        },
        error: function (xhr, status, error) {
            console.error("Erreur lors du filtrage des produits:", error);
        }
    });
}
