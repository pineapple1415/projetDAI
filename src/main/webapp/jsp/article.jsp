<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Détails du produit</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css" />
    <style>
        /* 添加数量选择器样式 */
        .quantity-selector {
            margin: 15px 0;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .quantity-btn {
            width: 30px;
            height: 30px;
            border-radius: 50%;
            border: 1px solid #ccc;
            background: #f8f8f8;
            cursor: pointer;
        }
        #quantity {
            font-size: 18px;
            min-width: 40px;
            text-align: center;
        }
    </style>
</head>
<body>
<header>
    <button id="loginButton">Espace client</button>
</header>
<main>
    <h2>Détails du produit</h2>
    <div id="productDetail">
        <img id="productImage" src="" alt="Image du produit" style="width: 200px; height: auto;">
        <p>Nom du produit : <span id="produitNom"></span></p>
        <p>Description : <span id="productDesc"></span></p>
        <p>Origine : <span id="productOrigin"></span></p>
        <p>Taille : <span id="productSize"></span></p>
        <p>Prix : <span id="productPrice"></span> €</p>

        <!-- 隐藏的 input 存 productId -->
        <input type="hidden" id="productId">

        <!-- 数量选择器 -->
        <div class="quantity-selector">
            <button class="quantity-btn" onclick="updateQuantity(-1)">-</button>
            <span id="quantity">1</span>
            <button class="quantity-btn" onclick="updateQuantity(1)">+</button>
        </div>
    </div>

    <button type="button" id="ajoutePanier">Ajouter au panier</button>
    <button type="button" id="ajouteCourse">Ajouter au course</button>
</main>

<script>
    document.getElementById("loginButton").addEventListener("click", function () {
        window.location.href = "${pageContext.request.contextPath}/client";
    });

    document.addEventListener("DOMContentLoaded", function () {
        let nomProduit = sessionStorage.getItem("nomProduit"); // 获取产品名称

        if (!nomProduit) {
            document.getElementById("produitNom").innerText = "Produit non trouvé";
            return;
        }

        // 发送请求获取产品信息
        fetch(`${window.location.origin}/ProjetDAI_war/produitDetail?nomProduit=` + encodeURIComponent(nomProduit), {
            method: "GET",
            headers: { "Accept": "application/json" }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Erreur lors de la récupération des détails du produit");
                }
                return response.json();
            })
            .then(produit => {
                if (!produit || produit.error) {
                    document.getElementById("produitNom").innerText = "Produit non trouvé";
                    return;
                }

                // 填充页面数据
                document.getElementById("produitNom").innerText = produit.nomProduit;
                document.getElementById("productPrice").innerText = produit.prixUnit;
                document.getElementById("productDesc").innerText = produit.descriptionProduit;
                document.getElementById("productOrigin").innerText = produit.origineProduit;
                document.getElementById("productSize").innerText = produit.tailleProduit;
                document.getElementById("productImage").src = produit.image;

                // 设置 productId
                document.getElementById("productId").value = produit.idProduit;

                // 根据库存状态更新按钮
                const addButton = document.getElementById('ajoutePanier');
                if (!produit.available) {
                    addButton.innerText = "produit deja vendu";
                    addButton.disabled = true;
                    addButton.style.backgroundColor = "#ccc"; // 灰色背景
                    addButton.style.cursor = "not-allowed"; // 禁用光标
                } else {
                    addButton.disabled = false;
                }
            })
            .catch(error => console.error("Erreur:", error));
    });

    // 数量控制逻辑
    let currentQuantity = 1;

    function updateQuantity(change) {
        currentQuantity = Math.max(1, currentQuantity + change);
        document.getElementById('quantity').textContent = currentQuantity;
    }

    // 修改后的加入购物车函数
    // 修改后的addToCart函数
    function addToCart() {
        const productId = document.getElementById('productId').value;

        fetch('${pageContext.request.contextPath}/addToPanier', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                productId: productId,
                quantity: currentQuantity
            })
        })
            .then(response => {
                if(response.redirected) {
                    window.location.href = response.url; // 跳转到成功页面
                }
            })
            .catch(error => console.error('Erreur:', error));

        // 在addToCart函数中添加验证
        console.log('正在添加商品:', {
            productId: productId,
            quantity: currentQuantity
        });

    }


    // 绑定点击事件（替代onclick属性）
    document.getElementById('ajoutePanier').addEventListener('click', addToCart);

    // 绑定点击事件（替代onclick属性）
    document.getElementById('ajouteCourse').addEventListener('click', addToCourse);

    function addToCourse() {
        const productId = document.getElementById('productId').value;
        const quantity = currentQuantity; // 获取当前选择的数量

        // 弹出 `prompt` 让用户输入 `idCourse`
        let idCourse = prompt("Veuillez entrer l'ID du course où vous voulez ajouter ce produit:");

        // 验证用户输入
        if (!idCourse || isNaN(idCourse) || parseInt(idCourse) <= 0) {
            alert("ID de course invalide. Veuillez entrer un nombre valide.");
            return;
        }

        console.log("Ajout au course:", { productId, quantity, idCourse });

        fetch(`${window.location.origin}/ProjetDAI_war/courses?action=addToCourse`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                idCourse: idCourse,
                productId: productId,
                quantity: quantity
            })
        })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => { throw new Error("Erreur serveur: " + text); });
                }
                return response.json();
            })
            .then(data => {
                console.log("Réponse de l'ajout au course:", data);
                if (data.success) {
                    alert("Produit ajouté au course avec succès !");
                } else {
                    alert("Échec de l'ajout au course: " + data.message);
                }
            })
            .catch(error => {
                console.error('Erreur:', error);
                alert("Une erreur s'est produite lors de l'ajout au course.");
            });

    }

</script>

</body>
</html>