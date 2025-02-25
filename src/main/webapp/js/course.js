document.addEventListener("DOMContentLoaded", function () {
    loadCourses();
});

function loadCourses() {
    fetch("/ProjetDAI_war/courses?action=getCourses")
        .then(response => response.json())
        .then(data => {
            const container = document.getElementById("coursesContainer");
            container.innerHTML = ""; // 清空旧数据

            data.forEach(course => {
                let div = document.createElement("div");
                div.className = "course-card";
                div.innerHTML = `
                    <h3>Course #${course.idCourse}</h3>
                    <p>${course.texte ? course.texte : "Aucune description"}</p>
                `;
                div.onclick = () => showProducts(course);
                container.appendChild(div);
            });
        })
        .catch(error => console.error("Erreur lors du chargement des courses:", error));
}

function showProducts(course) {
    const productList = document.getElementById("products");
    productList.innerHTML = "";

    if (!course.produits || Object.keys(course.produits).length === 0) {
        productList.innerHTML = "<p>Aucun produit dans ce course.</p>";
    } else {
        Object.entries(course.produits).forEach(([nom, imageUrl]) => {
            let div = document.createElement("div");
            div.className = "product-card";
            div.innerHTML = `
                <img src="${imageUrl}" alt="${nom}" class="product-image">
                <p>${nom}</p>
            `;
            productList.appendChild(div);
        });
    }

    document.getElementById("product-list").classList.remove("hidden");
}
