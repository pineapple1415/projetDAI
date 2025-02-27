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


// Cette partie pour importer des produits
// 显示隐藏导入产品的表单
function afficherFormImporterProduit() {
    const form = document.getElementById("formProduit");
    if(form) {
        form.style.display = "block";
    }
}

// DOMContentLoaded事件处理表单动态逻辑
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

// Ce partie pour ajouter des stocks
document.addEventListener("DOMContentLoaded", function () {
    const ajouterStockBtn = document.getElementById("ajouterStockBtn");
    const stockFormContainer = document.getElementById("stockFormContainer");

    // 获取所有的 DOM 元素
    const magasinSelect = document.getElementById("magasinSelect");
    const rayonSelect = document.getElementById("rayonStockSelect");
    const categorieSelect = document.getElementById("categorieStockSelect");
    const produitSelect = document.getElementById("produitStockSelect");
    const quantiteInput = document.getElementById("quantiteInput");
    const confirmerStockBtn = document.getElementById("confirmerStockBtn");

    const rayonRow = document.getElementById("rayonRow2");
    const categorieRow = document.getElementById("categorieRow");
    const produitRow = document.getElementById("produitRow");
    const quantiteRow = document.getElementById("quantiteRow");

    /** ✅ 点击 "Ajouter au Stock" 按钮时，加载 magasins 和 rayons */
    ajouterStockBtn.addEventListener("click", function () {
        stockFormContainer.style.display = stockFormContainer.style.display === "none" ? "block" : "none";

        // **1️⃣ 加载 magasins**
        fetch(`${window.location.origin}/ProjetDAI_war/ajouterStock?type=magasins`)
            .then(response => response.text())
            .then(data => {
                console.log("Magasins reçus:", data);  // 调试信息
                magasinSelect.innerHTML = '<option value="">Sélectionner un magasin</option>';
                data.trim().split("\n").forEach(line => {
                    const [id, nom] = line.split(",");
                    magasinSelect.innerHTML += `<option value="${id}">${nom}</option>`;
                });
            })
            .catch(error => console.error("Erreur lors du chargement des magasins:", error));

        // **2️⃣ 加载 rayons（rayon 与 magasin 无关，独立加载）**
        fetch(`${window.location.origin}/ProjetDAI_war/ajouterStock?type=rayons`)
            .then(response => response.text())
            .then(data => {
                console.log("Rayons reçus:", data);  // 调试信息
                rayonSelect.innerHTML = '<option value="">Sélectionner un rayon</option>';
                data.trim().split("\n").forEach(line => {
                    const [id, nom] = line.split(",");
                    rayonSelect.innerHTML += `<option value="${id}">${nom}</option>`;
                });
                rayonRow.style.display = "table-row"; // 显示 Rayon 选择框
            })
            .catch(error => console.error("Erreur lors du chargement des rayons:", error));
    });

    /** ✅ 监听 Rayon 选择，加载对应的 Catégories */
    rayonSelect.addEventListener("change", function () {
        const rayonId = rayonSelect.value;
        if (!rayonId) return;

        fetch(`${window.location.origin}/ProjetDAI_war/ajouterStock?type=categories&rayonId=${rayonId}`)
            .then(response => response.text())
            .then(data => {
                console.log("Catégories reçues:", data);  // 调试信息
                categorieSelect.innerHTML = '<option value="">Sélectionner une catégorie</option>';
                data.trim().split("\n").forEach(line => {
                    const [id, nom] = line.split(",");
                    categorieSelect.innerHTML += `<option value="${id}">${nom}</option>`;
                });
                categorieRow.style.display = "table-row"; // 显示 Catégorie 选择框
            })
            .catch(error => console.error("Erreur lors du chargement des catégories:", error));
    });

    /** ✅ 监听 Catégorie 选择，加载对应的 Produits */
    categorieSelect.addEventListener("change", function () {
        const categorieId = categorieSelect.value;
        if (!categorieId) return;

        fetch(`${window.location.origin}/ProjetDAI_war/ajouterStock?type=produits&categorieId=${categorieId}`)
            .then(response => response.text())
            .then(data => {
                console.log("Produits reçus:", data);  // 调试信息
                produitSelect.innerHTML = '<option value="">Sélectionner un produit</option>';
                data.trim().split("\n").forEach(line => {
                    const [id, nom] = line.split(",");
                    produitSelect.innerHTML += `<option value="${id}">${nom}</option>`;
                });
                produitRow.style.display = "table-row"; // 显示 Produit 选择框
            })
            .catch(error => console.error("Erreur lors du chargement des produits:", error));
    });

    /** ✅ 监听 Produit 选择，显示 Quantité 输入框 */
    produitSelect.addEventListener("change", function () {
        quantiteRow.style.display = "table-row";
    });

    /** ✅ 发送 Stock 添加请求 */
    confirmerStockBtn.addEventListener("click", function () {
        const magasinId = magasinSelect.value;
        const produitId = produitSelect.value;
        const quantite = quantiteInput.value;

        if (!magasinId || !produitId || !quantite) {
            alert("Veuillez remplir tous les champs !");
            return;
        }

        fetch(`${window.location.origin}/ProjetDAI_war/ajouterStock`, {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: `magasin=${magasinId}&produit=${produitId}&quantite=${quantite}`
        })
            .then(response => response.text())
            .then(message => {
                alert(message);
                stockFormContainer.style.display = "none";  // ✅ 关闭表单
                magasinSelect.value = "";
                rayonSelect.innerHTML = '<option value="">Sélectionner un rayon</option>';
                categorieSelect.innerHTML = '<option value="">Sélectionner une catégorie</option>';
                produitSelect.innerHTML = '<option value="">Sélectionner un produit</option>';
                quantiteInput.value = "";
            })
            .catch(error => console.error("Erreur lors de l'ajout de stock:", error));
    });
});



// Cette partie pour visualiser des performances
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


// Cette partie pour editer des statistiques

document.addEventListener("DOMContentLoaded", function () {
    const statSelector = document.getElementById("statSelector");
    const rayonSelectorContainer = document.getElementById("rayonSelectorContainer");
    const rayonSelector = document.getElementById("rayonSelector");
    const showStatChartBtn = document.getElementById("showStatChartBtn");
    const statChartContainer = document.getElementById("statChartContainer");

    // 存储 Chart.js 图表实例
    window.statChartInstance = {};

    // 监听统计类型选择
    statSelector.addEventListener("change", function () {
        if (statSelector.value === "salesPerCategory") {
            rayonSelectorContainer.style.display = "block";  // 显示 Rayon 选择框
        } else {
            rayonSelectorContainer.style.display = "none";
            statChartContainer.style.display = "none"; // 选择其他图表时，隐藏统计图
        }
    });

    // 监听 Rayon 选择变化（自动加载）
    rayonSelector.addEventListener("change", function () {
        if (statSelector.value === "salesPerCategory") {
            fetchDataAndRenderStatChart("salesPerCategory");
        }
    });

    // 监听 "Afficher" 按钮点击
    showStatChartBtn.addEventListener("click", function () {
        const selectedStat = statSelector.value;
        if (!selectedStat) {
            alert("Veuillez sélectionner un graphique !");
            return;
        }
        fetchDataAndRenderStatChart(selectedStat);
    });

    // 发送请求并渲染图表
    function fetchDataAndRenderStatChart(chartType) {
        let url = `${window.location.origin}/ProjetDAI_war/statistiques?type=${chartType}`;

        // 若是 salesPerCategory，需要传 rayonId
        if (chartType === "salesPerCategory") {
            const rayonId = rayonSelector.value;
            if (!rayonId) {
                alert("Veuillez sélectionner un rayon !");
                return;
            }
            url += `&rayonId=${rayonId}`;
        }

        fetch(url)
            .then(response => response.text())
            .then(data => {
                let lines = data.trim().split("\n");
                if (lines.length < 2) {
                    alert("Pas de données disponibles !");
                    return;
                }

                let labels = lines[0].split(",");
                let values = lines[1].split(",").map(Number);

                // 定义 chartId
                let canvasId = getCanvasId(chartType);
                let canvas = document.getElementById(canvasId);
                if (!canvas) {
                    console.error("Erreur: Canvas introuvable pour " + chartType);
                    return;
                }

                let ctx = canvas.getContext("2d");

                // 销毁旧图表
                if (window.statChartInstance[chartType]) {
                    window.statChartInstance[chartType].destroy();
                }

                // 创建新图表
                window.statChartInstance[chartType] = new Chart(ctx, {
                    type: "bar",
                    data: {
                        labels: labels,
                        datasets: [{
                            label: getChartLabel(chartType),
                            data: values,
                            backgroundColor: ["#FF6384", "#36A2EB", "#FFCE56", "#4BC0C0"],
                            borderColor: ["#FF6384", "#36A2EB", "#FFCE56", "#4BC0C0"],
                            borderWidth: 1
                        }]
                    },
                    options: {
                        responsive: true,
                        plugins: {
                            legend: { display: true },
                            tooltip: { enabled: true }
                        },
                        scales: { y: { beginAtZero: true } }
                    }
                });

                // 显示图表容器
                statChartContainer.style.display = "block";
                document.getElementById(chartType).style.display = "block";
            })
            .catch(error => console.error("Erreur AJAX:", error));
    }

    // 获取 canvas ID
    function getCanvasId(chartType) {
        const chartMap = {
            "customerPurchaseDistribution": "chartCustomerPurchaseDistribution",
            "salesPerProduct": "chartSalesPerProduct",
            "stockPerProduct": "chartStockPerProduct",
            "salesPerCategory": "chartSalesPerCategory",
            "salesPerRayon": "chartSalesPerRayon"
        };
        return chartMap[chartType] || `chart${chartType.charAt(0).toUpperCase() + chartType.slice(1)}`;
    }

    // 获取图表名称
    function getChartLabel(chartType) {
        const chartLabels = {
            "customerPurchaseDistribution": "Répartition des Achats Clients",
            "salesPerProduct": "Ventes par Produit (€)",
            "stockPerProduct": "Stock des Produits",
            "salesPerCategory": "Ventes par Catégorie (€)",
            "salesPerRayon": "Ventes par Rayon (€)"
        };
        return chartLabels[chartType] || chartType;
    }
});


// Cette partie pour visualiser des stocks dans 7 jours

document.addEventListener("DOMContentLoaded", function () {
    const voirStockBtn = document.getElementById("voirStockBtn");
    const stockContainer = document.getElementById("stockContainer");

    if (voirStockBtn) {
        voirStockBtn.addEventListener("click", function () {
            stockContainer.style.display = (stockContainer.style.display === "none") ? "block" : "none";
            if (stockContainer.style.display === "block") {
                chargerStockPrevuData();
            }
        });
    }
});

function chargerStockPrevuData() {
    let url = `${window.location.origin}/ProjetDAI_war/stockPrevu`; // 确保路径正确

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.text();
        })
        .then(text => {
            const lines = text.trim().split("\n");
            if (lines.length < 4) {
                console.error("❌ 数据格式错误");
                return;
            }

            const labels = lines[0].split(",");
            const stockActuelData = lines[1].split(",").map(Number);
            const ventesEstimeesData = lines[2].split(",").map(Number);
            const stockPrevuData = lines[3].split(",").map(Number);

            remplirTableauStock(labels, stockActuelData, ventesEstimeesData, stockPrevuData);
            afficherStockChart(labels, stockActuelData, stockPrevuData);
        })
        .catch(error => console.error("❌ 加载库存预测数据失败:", error));
}

function remplirTableauStock(labels, stockActuelData, ventesEstimeesData, stockPrevuData) {
    const table = document.getElementById("stockTableBody");
    table.innerHTML = ""; // 清空旧数据

    for (let i = 0; i < labels.length; i++) {
        let row = document.createElement("tr");
        row.innerHTML = `
            <td>${labels[i]}</td>
            <td>${stockActuelData[i]}</td>
            <td>${ventesEstimeesData[i]}</td>
            <td>${stockPrevuData[i]}</td>
        `;
        table.appendChild(row);
    }
}

function afficherStockChart(labels, stockActuelData, stockPrevuData) {
    const ctx = document.getElementById('stockChart').getContext('2d');

    if (window.stockChartInstance) {
        window.stockChartInstance.destroy();
    }

    window.stockChartInstance = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [
                {
                    label: "Stock Actuel",
                    data: stockActuelData,
                    backgroundColor: "blue"
                },
                {
                    label: "Stock Prévu",
                    data: stockPrevuData,
                    backgroundColor: "green"
                }
            ]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { display: true },
                tooltip: { enabled: true }
            },
            scales: {
                y: { beginAtZero: true }
            }
        }
    });
}






