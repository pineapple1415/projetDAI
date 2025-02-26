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
                        <button class="edit-btn" onclick="editCourse(${course.idCourse}, '${course.texte}')">action texte</button>
                        <button class="delete-btn" onclick="deleteCourse(${course.idCourse})">Supprimer</button>
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
            productList.innerHTML = "";

            if (!Array.isArray(data) || data.length === 0) {
                productList.innerHTML = "<p>Aucun produit dans ce course.</p>";
                return;
            }

            data.forEach(product => {
                let div = document.createElement("div");
                div.className = "product-card";
                div.innerHTML = `
                    <img src="${product.imageUrl}" alt="${product.nom}" class="product-image">
                    <p>${product.nom}</p>
                    <p><strong>Quantité:</strong> ${product.nombre}</p>
                `;
                productList.appendChild(div);
            });

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
