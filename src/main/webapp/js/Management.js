function envoyerRequete(action) {
    fetch(`/gerant?action=${action}`)
        .then(response => response.text())
        .then(data => document.getElementById('resultat').innerHTML = data)
        .catch(error => console.error('Erreur:', error));
}

function importerProduits() {
    envoyerRequete('importer');
}

function voirStock() {
    envoyerRequete('etatStock');
}

function verifierEfficacite() {
    envoyerRequete('efficacite');
}

function editerStatistiques() {
    envoyerRequete('statistiques');
}

function gererRecommandation() {
    envoyerRequete('recommandation');
}

function consulterProfils() {
    envoyerRequete('profils');
}

function detecterHabitudes() {
    envoyerRequete('habitudes');
}

function logout() {
    fetch('/logout', { method: 'POST' })
        .then(() => window.location.href = 'login.jsp')
        .catch(error => console.error('Erreur de déconnexion:', error));
}

// 显示隐藏导入产品的表单
function afficherFormImporterProduit() {
    const form = document.getElementById("formProduit");
    if (form) {
        form.style.display = "block";
    }
}

// 监听 DOM 加载
window.addEventListener("DOMContentLoaded", function () {
    const fournisseurSelect = document.getElementById('fournisseurSelect');
    const nouveauFournisseurRow = document.getElementById('nouveauFournisseurRow');
    const adresseFournisseurRow = document.getElementById('adresseFournisseurRow');
    const contactFournisseurRow = document.getElementById('contactFournisseurRow');

    const categorieSelect = document.getElementById('categorieSelect');
    const nouvelleCategorieRow = document.getElementById('nouvelleCategorieRow');
    const rayonRow = document.getElementById('rayonRow');

    const rayonSelect = document.getElementById('rayonSelect');
    const nouveauRayonRow = document.getElementById('nouveauRayonRow');

    const produitForm = document.getElementById('produitForm');
    const confirmerBtn = document.getElementById('confirmerBtn');
    const resultat = document.getElementById('resultat');

    fournisseurSelect.onchange = function () {
        const isNouveau = this.value === 'nouveau';
        nouveauFournisseurRow.style.display = isNouveau ? 'table-row' : 'none';
        adresseFournisseurRow.style.display = isNouveau ? 'table-row' : 'none';
        contactFournisseurRow.style.display = isNouveau ? 'table-row' : 'none';
    };

    categorieSelect.onchange = function () {
        const isNouveau = this.value === 'nouveau';
        nouvelleCategorieRow.style.display = isNouveau ? 'table-row' : 'none';
        rayonRow.style.display = isNouveau ? 'table-row' : 'none';
    };

    rayonSelect.onchange = function () {
        nouveauRayonRow.style.display = this.value === 'nouveau' ? 'table-row' : 'none';
    };

    confirmerBtn.onclick = (event) => {
        event.preventDefault();
        const formData = new FormData(produitForm);
        fetch("ajouterProduit", {
            method: "POST",
            body: formData
        })
            .then(res => res.text())
            .then(msg => {
                resultat.innerHTML = `<p style="color: green;">${msg}</p>`;
                produitForm.reset();
                nouvelleCategorieRow.style.display = 'none';
                rayonRow.style.display = 'none';
                nouveauRayonRow.style.display = 'none';
            })
            .catch(err => {
                resultat.innerHTML = `<p style="color: red;">Erreur: ${err}</p>`;
            });
    };
});

// ✅ 确保 DOM 加载后执行
document.addEventListener("DOMContentLoaded", function () {
    const chartSelector = document.getElementById("chartSelector");
    const afficherChartBtn = document.getElementById("afficherChartBtn");
    const chartContainer = document.getElementById("chartContainer");
    let chartInstance = null;

    afficherChartBtn.addEventListener("click", function () {
        let chartType = chartSelector.value;

        if (!chartType) {
            alert("Veuillez sélectionner un graphique !");
            return;
        }

        chartContainer.style.display = "block";
        fetchDataAndRenderChart(chartType);
    });
});

// ✅ 发送请求并渲染图表
function fetchDataAndRenderChart(chartType) {
    const url = `${window.location.origin}/ProjetDAI_war/performance?chartType=${chartType}`;

    fetch(url)
        .then(response => response.text()) // 服务器返回 CSV 格式数据
        .then(data => {
            let lines = data.trim().split("\n"); // 按行分割

            if (lines.length < 2) {
                console.error("Erreur: Données invalides");
                return;
            }

            let labels = lines[0].split(","); // 第一行是月份
            let values = lines[1].split(",").map(Number); // 第二行是数值

            renderChart(chartType, labels, values);
        })
        .catch(error => console.error("Erreur:", error));
}

// ✅ 渲染图表
function renderChart(chartType, labels, values) {
    const ctx = document.getElementById("performanceChart").getContext("2d");

    // **🚀 确保 Chart.js 已经加载**
    if (typeof Chart === "undefined") {
        console.error("Chart.js 未加载，请检查引入！");
        return;
    }

    // **✅ 删除旧图表，避免重叠**
    if (window.chartInstance) {
        window.chartInstance.destroy();
    }

    window.chartInstance = new Chart(ctx, {
        type: "bar",
        data: {
            labels: labels,
            datasets: [{
                label: chartType === "shoppingTime" ? "Temps Moyen de Shopping (minutes)" : "Temps Moyen de Préparation (minutes)",
                data: values,
                backgroundColor: chartType === "shoppingTime" ? "rgba(54, 162, 235, 0.6)" : "rgba(255, 99, 132, 0.6)",
                borderColor: chartType === "shoppingTime" ? "rgba(54, 162, 235, 1)" : "rgba(255, 99, 132, 1)",
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: { beginAtZero: true }
            }
        }
    });
}


