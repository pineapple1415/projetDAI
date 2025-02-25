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

