<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Accueil Gérant</title>
    <link rel="stylesheet" type="text/css" href="gerant.css">
    <script src="script.js" defer></script>
</head>
<body>
<h1>Bienvenue, Gérant</h1>

<div class="buttons">
    <button onclick="importerProduits()">📥 Importer Produits</button>
    <button onclick="voirStock()">📊 État du Stock</button>
    <button onclick="verifierEfficacite()">⏳ Vérifier Efficacité</button>
    <button onclick="editerStatistiques()">📈 Éditer Statistiques</button>
    <button onclick="gererRecommandation()">🔧 Gérer Algorithme de Recommandation</button>
    <button onclick="consulterProfils()">👤 Consulter Profils Consommateurs</button>
    <button onclick="detecterHabitudes()">🕵️ Détecter Habitudes</button>
    <button onclick="logout()">🚪 Déconnexion</button>
</div>

<div id="resultat"></div>
</body>
</html>
