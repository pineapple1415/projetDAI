
<%@ page import="java.util.List" %>
<%@ page import="model.*" %>
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
<%
    Gerant gerant = (Gerant) session.getAttribute("user");
    if (gerant == null) {
        response.sendRedirect("jsp/login.jsp");
        return;
    }
%>
<div class="header">
    <h1>Bienvenue, <%= gerant.getNom() %></h1>
    <button class="logout-button" onclick="window.location.href='${pageContext.request.contextPath}/logout'">Déconnexion</button>
</div>

<div class="container">

    <!-- Gestion des Produits -->
    <section class="section">
        <h2 class="section-title">Gestion des Produits</h2>
        <div class="sub-section">
            <h3>Importation</h3>
            <button onclick="afficherFormImporterProduit()">Importer Produits</button>
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


        <!-- Ajout de Stock -->
        <div class="sub-section">
            <h3>Ajout de Stock</h3>
            <button id="ajouterStockBtn">Ajouter au Stock</button>

            <!-- Stock Form -->
            <div id="stockFormContainer" style="display: none;">
                <table>
                    <!-- Magasin -->
                    <tr>
                        <td>Magasin:</td>
                        <td>
                            <select name="magasin" id="magasinSelect" required>
                                <option value="">Sélectionner un magasin</option>
                                <%-- 直接从 request 读取 magasins 数据 --%>
                                <%
                                    List<Magasin> magasins = (List<Magasin>) request.getAttribute("magasins");
                                    if (magasins != null) {
                                        for (Magasin magasin : magasins) { %>
                                <option value="<%= magasin.getIdMagasin() %>"><%= magasin.getNomMagasin() %></option>
                                <%      }
                                }
                                %>
                            </select>
                        </td>
                    </tr>

                    <!-- Rayon -->
                    <tr id="rayonRow2" style="display: none;">
                        <td>Rayon:</td>
                        <td>
                            <select name="rayon" id="rayonStockSelect" required>
                                <option value="">Sélectionner un rayon</option>
                            </select>
                        </td>
                    </tr>

                    <!-- Catégorie -->
                    <tr id="categorieRow" style="display: none;">
                        <td>Catégorie:</td>
                        <td>
                            <select name="categorie" id="categorieStockSelect" required>
                                <option value="">Sélectionner une catégorie</option>
                            </select>
                        </td>
                    </tr>

                    <!-- Produit -->
                    <tr id="produitRow" style="display: none;">
                        <td>Produit:</td>
                        <td>
                            <select name="produit" id="produitStockSelect" required>
                                <option value="">Sélectionner un produit</option>
                            </select>
                        </td>
                    </tr>

                    <!-- Quantité -->
                    <tr id="quantiteRow" style="display: none;">
                        <td>Quantité à Ajouter:</td>
                        <td><input type="number" id="quantiteInput" min="1" required></td>
                    </tr>

                    <!-- Soumission -->
                    <tr>
                        <td colspan="2">
                            <button id="confirmerStockBtn">Ajouter</button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>

        <div class="sub-section">
            <h3>État du Stock - 7 Prochains Jours</h3>
            <button id="voirStockBtn">Voir Stock</button>

            <div id="stockContainer" style="display: none;">
                <h3>État du Stock Prévisionnel</h3>

                <table border="1">
                    <thead>
                    <tr>
                        <th>Nom Produit</th>
                        <th>Stock Actuel</th>
                        <th>Ventes Estimées (7 Jours)</th>
                        <th>Stock Prévu</th>
                    </tr>
                    </thead>
                    <tbody id="stockTableBody"></tbody>
                </table>

                <canvas id="stockChart"></canvas>
            </div>
        </div>
    </section>

    <!-- Analyse de Performance -->
    <section class="section">
        <h2 class="section-title">Analyse de Performance</h2>

        <select id="chartSelector">
            <option value="">-- Sélectionnez un graphique --</option>
            <option value="shoppingTime">Temps Moyen de Shopping</option>
            <option value="preparationTime">Temps Moyen de Préparation</option>
        </select>
        <button id="afficherChartBtn">Afficher</button>

        <div id="chartContainer" style="display:none;">
            <h3>Visualisation des Données</h3>
            <canvas id="performanceChart"></canvas>
        </div>

    </section>

    <!-- 🔹 **新的统计数据部分** -->
    <section class="section">
        <h2 class="section-title">Statistiques des Ventes</h2>

        <!-- 选择统计类型 -->
        <label for="statSelector">Sélectionnez un type de statistique :</label>
        <select id="statSelector">
            <option value="">-- Sélectionnez un graphique --</option>
            <option value="customerPurchaseDistribution">Répartition des Achats Clients</option>
            <option value="salesPerProduct">Ventes par Produit</option>
            <option value="stockPerProduct">Stock des Produits</option>
            <option value="salesPerCategory">Répartition des Ventes par Catégorie</option>
            <option value="salesPerRayon">Répartition des Ventes par Rayon</option>
        </select>

        <!-- 选择 Rayon (默认隐藏，选中 salesPerCategory 时才显示) -->
        <div id="rayonSelectorContainer" style="display: none;">
            <label for="rayonSelector">Sélectionnez un Rayon :</label>
            <%
                List<Rayon> rayon2 = (List<Rayon>) request.getAttribute("rayons"); // 更改变量名称
            %>
            <select id="rayonSelector">
                <option value="">-- Tous les Rayons --</option>
                <%
                    if (rayon2 != null) {
                        for (Rayon ray : rayon2) {
                %>
                <option value="<%= ray.getIdRayon() %>"><%= ray.getNomRayon() %></option>
                <%
                        }
                    }
                %>
            </select>
        </div>
        <button id="showStatChartBtn">Afficher</button>

        <!-- **统计数据展示区** -->
        <div id="statChartContainer" style="display:none;">
            <h3>Visualisation des Données</h3>

            <!-- 购买次数分布 -->
            <!-- 修正 `canvas` 的 ID，确保和 `chartType` 匹配 -->
            <div id="customerPurchaseDistribution" style="display:none;">
                <h3>Répartition des Achats Clients</h3>
                <canvas id="chartCustomerPurchaseDistribution"></canvas>
            </div>

            <div id="salesPerProduct" style="display:none;">
                <h3>Ventes par Produit</h3>
                <canvas id="chartSalesPerProduct"></canvas>
            </div>

            <div id="stockPerProduct" style="display:none;">
                <h3>Stock des Produits</h3>
                <canvas id="chartStockPerProduct"></canvas>
            </div>

            <div id="salesPerCategory" style="display:none;">
                <h3>Répartition des Ventes par Catégorie</h3>
                <canvas id="chartSalesPerCategory"></canvas>
            </div>


            <div id="salesPerRayon" style="display:none;">
                <h3>Répartition des Ventes par Rayon</h3>
                <canvas id="chartSalesPerRayon"></canvas>
            </div>

        </div>
    </section>


    <!-- Recommandations et Consommateurs -->
    <section class="section">
        <h2 class="section-title">Recommandations & Consommateurs</h2>
        <div class="sub-section">
            <h3>Algorithme de Recommandation</h3>
            <button onclick="gererRecommandation()">Gérer Algorithme</button>
        </div>
        <div class="sub-section">
            <h3>Profils Consommateurs</h3>
            <button onclick="consulterProfils()">Consulter Profils</button>
        </div>
        <div class="sub-section">
            <h3>Détection d'Habitudes</h3>
            <button onclick="detecterHabitudes()">Détecter Habitudes</button>
        </div>
    </section>

    <!-- Déconnexion -->
<%--    <section class="section">--%>
<%--        <h2 class="section-title">🚪 Sécurité</h2>--%>
<%--        <div class="sub-section">--%>
<%--            <h3>Déconnexion</h3>--%>
<%--            <button onclick="logout()">🚪 Déconnexion</button>--%>
<%--        </div>--%>
<%--    </section>--%>

    <!-- Zone AJAX pour contenu dynamique -->
    <div id="contenu-dynamique"></div>

</div>

</body>
</html>