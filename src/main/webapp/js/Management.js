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
