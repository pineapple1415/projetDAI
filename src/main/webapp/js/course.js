document.addEventListener("DOMContentLoaded", function () {
    loadCourses();
});

function loadCourses() {
    fetch("/ProjetDAI_war/courses?action=getCourses")
        .then(response => response.json())
        .then(data => {
            console.log("Courses chargés:", data); // 调试 JSON 结果
            const container = document.getElementById("coursesContainer");
            container.innerHTML = ""; // 清空旧数据

            data.forEach(course => {
                let div = document.createElement("div");
                div.className = "course-card";
                div.innerHTML = `
                    <h3>Course #${course.idCourse}</h3>
                    <p>${course.texte ? course.texte : "Aucune description"}</p>
                    <div class="course-actions">
                        <button class="edit-btn" onclick="editCourse(${course.idCourse}, '${course.texte}')">
                            <i class="fas fa-pencil-alt"></i> <!--  铅笔图标 -->
                        </button>
                        <button class="delete-btn" onclick="deleteCourse(${course.idCourse})">
                            <i class="fas fa-times"></i> <!--  叉号图标 -->
                        </button>
                    </div>

                 
                `;
                div.onclick = () => showProducts(course.idCourse); // 只传 idCourse
                container.appendChild(div);
            });


            // 添加 "Créer Course" 按钮
            let createDiv = document.createElement("div");
            createDiv.className = "course-card create-course";
            createDiv.innerHTML = `
                <h3>+</h3>
                <p>Créer Course</p>
            `;
            createDiv.onclick = () => showCreateCoursePopup();
            container.appendChild(createDiv);

        })
        .catch(error => console.error("Erreur lors du chargement des courses:", error));
}


function showProducts(idCourse) {
    console.log("Fetching produits pour Course ID:", idCourse);

    fetch(`/ProjetDAI_war/courses?action=getProducts&idCourse=${idCourse}`)
        .then(response => response.json())
        .then(data => {
            console.log("Produits chargés:", data); // 调试 JSON 结果
            const productList = document.getElementById("products");
            productList.innerHTML = ""; // 清空旧内容

            // 添加 `Ajouter Produit` 按钮
            let ajouterDiv = document.createElement("div");
            ajouterDiv.className = "product-card ajouter-produit";
            ajouterDiv.innerHTML = `<h3>+ Ajouter Produit</h3>`;
            ajouterDiv.onclick = () => window.location.href = contextPath + "/index";

            let addToCartDiv = document.createElement("div");
            addToCartDiv.className = "add-to-cart";
            addToCartDiv.innerHTML = `<button onclick="addAllToPanier(${idCourse})">🛒 add all to panier</button>`;


            if (!Array.isArray(data) || data.length === 0) {
                // 仅显示 `Ajouter Produit` 按钮
                productList.appendChild(ajouterDiv);
            } else {
                // 显示 `produits`
                data.forEach(product => {
                    console.log("Produit Data:", product); // 确保 `idProduit` 存在

                    let div = document.createElement("div");
                    div.className = "product-card";
                    div.innerHTML = `
                        <img src="${product.imageUrl}" alt="${product.nom}" class="product-image">
                        <p>${product.nom}</p>
                        <div class="quantity-controls">
                            <button onclick="updateQuantity(${idCourse}, ${product.idProduit}, -1)">➖</button>
                            <span id="qty-${product.idProduit}">${product.nombre}</span>
                            <button onclick="updateQuantity(${idCourse}, ${product.idProduit}, 1)">➕</button>
                        </div>
                    `;
                    productList.appendChild(div);
                });

                productList.appendChild(addToCartDiv);

                // 最后再添加 `Ajouter Produit` 按钮
                productList.appendChild(ajouterDiv);
            }

            document.getElementById("product-list").classList.remove("hidden");
        })
        .catch(error => console.error("Erreur lors du chargement des produits:", error));
}


function showCreateCoursePopup() {
    let texte = prompt("Entrez le post-it du nouveau Course :");

    if (texte === null) return; // 如果用户取消输入，直接返回

    texte = texte.trim(); // 移除前后空格
    if (texte === "") texte = "Sans description"; // 默认值

    fetch("/ProjetDAI_war/courses?action=createCourse", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ texte: texte })
    })
        .then(response => response.json())
        .then(data => {
            console.log("Nouveau Course ajouté:", data);
            loadCourses(); // 重新加载 Courses
        })
        .catch(error => console.error("Erreur lors de la création du Course:", error));
}


function editCourse(idCourse, oldTexte) {
    let newTexte = prompt("Modifier le texte du Course :", oldTexte);
    if (newTexte === null) return;

    newTexte = newTexte.trim();
    if (newTexte === "") newTexte = "Sans description"; // 默认值

    fetch("/ProjetDAI_war/courses?action=editCourse", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ idCourse: idCourse, texte: newTexte })
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error("Erreur serveur: " + text); });
            }
            return response.json();
        })
        .then(data => {
            console.log("Course modifié avec succès:", data);
            loadCourses();
        })
        .catch(error => console.error("Erreur lors de la modification du Course:", error));
}

function deleteCourse(idCourse) {
    if (!confirm("Voulez-vous vraiment supprimer ce Course ?")) return;

    fetch(`/ProjetDAI_war/courses?action=deleteCourse&idCourse=${idCourse}`, {
        method: "DELETE"
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Erreur lors de la suppression");
            }
            console.log("Course supprimé:", idCourse);
            loadCourses(); // 重新加载 Courses
        })
        .catch(error => console.error("Erreur lors de la suppression du Course:", error));
}

function updateQuantity(courseId, produitId, change) {
    let qtyElement = document.getElementById(`qty-${produitId}`);
    let currentQty = parseInt(qtyElement.innerText);
    let newQty = currentQty + change;

    if (newQty < 0) return;

    // 调试日志，检查数据是否正确
    console.log("Sending updateQuantity request:", { courseId, produitId, newQty });

    fetch("/ProjetDAI_war/courses?action=updateQuantity", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ courseId: courseId, produitId: produitId, newQty: newQty })
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error("Erreur serveur: " + text); });
            }
            return response.json();
        })
        .then(data => {
            console.log("Response from updateQuantity:", data);
            if (newQty === 0) {
                let productDiv = document.getElementById(`product-${produitId}`);
                console.log("Trying to remove produit:", produitId, document.getElementById(`product-${produitId}`));
                if (productDiv) {
                    productDiv.remove();  // 确保 `null` 时不会调用 `.remove()`
                }
            } else {
                qtyElement.innerText = newQty;
            }
        })
        .catch(error => console.error("Erreur lors de la modification de la quantité:", error));
}


function addAllToPanier(idCourse) {
    console.log("🛒 正在添加 Course ID", idCourse, "的所有产品到购物车");

    fetch(`/ProjetDAI_war/courses?action=getProducts&idCourse=${idCourse}`)
        .then(response => response.json())
        .then(data => {
            console.log("📦 获取到的产品数据:", data);

            if (!Array.isArray(data) || data.length === 0) {
                console.warn("⚠ 该 Course 没有可添加的产品");
                return;
            }

            // **并行发送多个 `addToCart` 请求**
            const promises = data.map(product => {
                console.log(`🛒 添加产品: ID=${product.idProduit}, 数量=${product.nombre}`);
                return addToCartAsync(product.idProduit, product.nombre);
            });

            // **等待所有请求完成**
            return Promise.all(promises);
        })
        .then(() => {
            console.log("✅ 所有商品已成功添加到购物车！");


        })
        .catch(error => console.error("❌ 购物车添加失败:", error));
}

