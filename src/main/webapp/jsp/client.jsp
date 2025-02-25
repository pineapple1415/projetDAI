<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, model.Commande, model.User" %>
<html>
<head>
  <title>Espace Client</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/client.css">
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
</div>

<div class="button-group">
  <button class="btn view-cart" onclick="location.href='${pageContext.request.contextPath}/affichePanier'">
    查看购物车
  </button>
  <button class="btn back-home" onclick="location.href='${pageContext.request.contextPath}/index'">
    返回首页
  </button>
</div>

</body>
</html>