
<%@ page import="model.Categorie" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Rayon" %>
<%@ page import="model.Fournisseur" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Accueil Gérant</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/gerant.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="${pageContext.request.contextPath}/js/Management.js" defer></script>

</head>
<body>

<h1 class="title">Bienvenue, Gérant</h1>

<div class="container">

    <!-- Gestion des Produits -->
    <section class="section">
        <h2 class="section-title">📦 Gestion des Produits</h2>
        <div class="sub-section">
            <h3>Importation</h3>
            <button onclick="afficherFormImporterProduit()">📥 Importer Produits</button>
            <div id="formProduit" style="display:none;">
                <form id="produitForm" action="${pageContext.request.contextPath}/ajouterProduit" method="post" enctype="multipart/form-data">
                    <table>
                        <tr><td>Nom Produit:</td><td><input name="nomProduit" required></td></tr>
                        <tr><td>Prix:</td><td><input type="number" step="0.01" name="prix" required></td></tr>
                        <tr><td>Origine:</td><td><input name="origine" required></td></tr>
                        <tr><td>Taille:</td><td><input name="taille" required></td></tr>
                        <tr><td>Description:</td><td><textarea name="description" required></textarea></td></tr>
                        <tr><td>Promotion (%):</td><td><input type="number" step="0.01" name="promotion" placeholder="0.0"></td></tr>
                        <tr><td>Image:</td><td><input type="file" name="imageProduit" required></td></tr>

                        <!-- Fournisseur -->
                        <tr>
                            <td>Fournisseur:</td>
                            <td>
                                <select name="fournisseur" id="fournisseurSelect" required>
                                    <option value="">Choisir un fournisseur existant</option>
                                    <%
                                        List<Fournisseur> fournisseurs = (List<Fournisseur>) request.getAttribute("fournisseurs");
                                        if (fournisseurs != null) {
                                            for (Fournisseur f : fournisseurs) {
                                    %>
                                    <option value="<%= f.getIdFournisseur() %>"><%= f.getNomFournisseur() %></option>
                                    <%
                                            }
                                        }
                                    %>
                                    <option value="nouveau">Ajouter nouveau fournisseur</option>
                                </select>
                            </td>
                        </tr>
                        <tr id="nouveauFournisseurRow" style="display:none;">
                            <td>Nom Fournisseur:</td>
                            <td><input name="nouveauNomFournisseur"></td>
                        </tr>
                        <tr id="adresseFournisseurRow" style="display:none;">
                            <td>Adresse Fournisseur:</td>
                            <td><input name="adresseFournisseur"></td>
                        </tr>
                        <tr id="contactFournisseurRow" style="display:none;">
                            <td>Contact Fournisseur:</td>
                            <td><input name="contactFournisseur"></td>
                        </tr>

                        <!-- Catégorie -->
                        <tr>
                            <td>Catégorie:</td>
                            <td>
                                <select name="categorie" id="categorieSelect" required>
                                    <option value="">Choisir une catégorie existante</option>
                                    <%
                                        List<Categorie> categories = (List<Categorie>) request.getAttribute("categories");
                                        if (categories != null) {
                                            for (Categorie cat : categories) {
                                    %>
                                    <option value="<%= cat.getIdCategorie() %>"><%= cat.getNomCategorie() %></option>
                                    <%
                                            }
                                        }
                                    %>
                                    <option value="nouveau">Ajouter nouvelle catégorie</option>
                                </select>
                            </td>
                        </tr>
                        <tr id="nouvelleCategorieRow" style="display:none;">
                            <td>Nom Nouvelle Catégorie:</td>
                            <td><input name="nouvelleCategorie"></td>
                        </tr>

                        <!-- Rayon -->
                        <tr id="rayonRow" style="display:none;">
                            <td>Rayon:</td>
                            <td>
                                <select name="rayon" id="rayonSelect">
                                    <option value="">Choisir un rayon existant</option>
                                    <%
                                        List<Rayon> rayons = (List<Rayon>) request.getAttribute("rayons");
                                        if (rayons != null) {
                                            for (Rayon ray : rayons) {
                                    %>
                                    <option value="<%= ray.getIdRayon() %>"><%= ray.getNomRayon() %></option>
                                    <%
                                            }
                                        }
                                    %>
                                    <option value="nouveau">Ajouter nouveau rayon</option>
                                </select>
                            </td>
                        </tr>
                        <tr id="nouveauRayonRow" style="display:none;">
                            <td>Nom Nouveau Rayon:</td>
                            <td><input name="nouveauRayon"></td>
                        </tr>

                        <tr>
                            <td colspan="2"><button id="confirmerBtn">✅ Confirmer</button></td>
                        </tr>
                    </table>
                </form>
            </div>

            <div id="resultat"></div>
        </div>
        <div class="sub-section">
            <h3>État du Stock</h3>
            <button onclick="voirStock()">📊 Voir Stock</button>
        </div>
    </section>

    <!-- Analyse de Performance -->
    <section class="section">
        <h2 class="section-title">⏳ Analyse de Performance</h2>

        <select id="chartSelector">
            <option value="">-- Sélectionnez un graphique --</option>
            <option value="shoppingTime">🛒 Temps Moyen de Shopping</option>
            <option value="preparationTime">⏳ Temps Moyen de Préparation</option>
        </select>
        <button id="afficherChartBtn">Afficher</button>

        <div id="chartContainer" style="display:none;">
            <h3>Visualisation des Données</h3>
            <canvas id="performanceChart"></canvas>
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
