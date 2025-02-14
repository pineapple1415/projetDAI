<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Accueil Gérant</title>
    <link rel="stylesheet" type="text/css" href="css/gerant.css">
    <script src="script.js" defer></script>
</head>
<body>

<h1 class="title">Bienvenue, Gérant</h1>

<div class="container">

    <!-- Gestion des Produits -->
    <section class="section">
        <h2 class="section-title">📦 Gestion des Produits</h2>
        <div class="sub-section">
            <h3>Importation</h3>
            <button onclick="importerProduits()">📥 Importer Produits</button>
        </div>
        <div class="sub-section">
            <h3>État du Stock</h3>
            <button onclick="voirStock()">📊 Voir Stock</button>
        </div>
    </section>

    <!-- Analyse de Performance -->
    <section class="section">
        <h2 class="section-title">⏳ Analyse de Performance</h2>
        <div class="sub-section">
            <h3>Vérification</h3>
            <button onclick="verifierEfficacite()">⏳ Vérifier Efficacité</button>
        </div>
        <div class="sub-section">
            <h3>Statistiques</h3>
            <button onclick="editerStatistiques()">📈 Éditer Statistiques</button>
        </div>
    </section>

    <!-- Recommandations et Consommateurs -->
    <section class="section">
        <h2 class="section-title">🔧 Recommandations & Consommateurs</h2>
        <div class="sub-section">
            <h3>Algorithme de Recommandation</h3>
            <button onclick="gererRecommandation()">🔧 Gérer Algorithme</button>
        </div>
        <div class="sub-section">
            <h3>Profils Consommateurs</h3>
            <button onclick="consulterProfils()">👤 Consulter Profils</button>
        </div>
        <div class="sub-section">
            <h3>Détection d'Habitudes</h3>
            <button onclick="detecterHabitudes()">🕵️ Détecter Habitudes</button>
        </div>
    </section>

    <!-- Déconnexion -->
    <section class="section">
        <h2 class="section-title">🚪 Sécurité</h2>
        <div class="sub-section">
            <h3>Déconnexion</h3>
            <button onclick="logout()">🚪 Déconnexion</button>
        </div>
    </section>

    <!-- Zone AJAX pour contenu dynamique -->
    <div id="contenu-dynamique"></div>

</div>

</body>
</html>
