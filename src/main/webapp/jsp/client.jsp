<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, model.Commande, model.User" %>
<html>
<head>
  <title>Espace Client</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/client.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

</head>
<body>
<div class="container">
  <%
    // 从 session 中获取用户对象
    User user = (User) session.getAttribute("user");
    // 从 request 中获取订单列表
    List<Commande> commandes = (List<Commande>) request.getAttribute("commandes");
  %>

  <h1>Bienvenue, <%= user.getPrenom() %> <%= user.getNom() %></h1>

  <div class="profile-section">
    <h2>Informations personnelles</h2>
    <p>Email: <%= user.getEmail() %></p>
  </div>

  <div class="orders-section">
    <h2>Historique des commandes</h2>
    <table>
      <thead>
      <tr>
        <th>N° Commande</th>
        <th>Montant</th>
        <th>Date</th>
        <th>Statut</th>
      </tr>
      </thead>
      <tbody>
      <%
        if (commandes != null) {
          for (Commande commande : commandes) {
      %>
      <tr>
        <td>#<%= commande.getIdCommande() %></td>
        <td><%= commande.getPrixTotal() %> €</td>
        <td><%= commande.getDateCommande() %></td>
        <td><%= commande.getStatut() %></td>
      </tr>
      <%
          }
        }
      %>
      </tbody>
    </table>
  </div>

  <div class="courses-section">
    <h2>Mes Courses</h2>

    <!-- 心愿单容器 -->
    <div class="courses-container" id="coursesContainer"></div>

    <!-- 选中的 Course 的 Produit 列表 -->
    <div id="product-list" class="hidden">
      <h3>Produits dans ce Course</h3>
      <div id="products"></div>
    </div>
  </div>




</div>

<div class="button-group">
  <button class="btn view-cart" onclick="location.href='${pageContext.request.contextPath}/affichePanier'">
    voir le panier
  </button>
  <button class="btn back-home" onclick="location.href='${pageContext.request.contextPath}/index'">
    à l'acceuil
  </button>
  <button class="btn course" onclick="location.href='${pageContext.request.contextPath}/course'">
    mes courses
  </button>
</div>


<script>var contextPath = "${pageContext.request.contextPath}";</script>
<script src="${pageContext.request.contextPath}/static/JS/panier.js"></script>
<script src="${pageContext.request.contextPath}/js/course.js"></script>
</body>
</html>