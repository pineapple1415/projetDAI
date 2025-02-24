<!DOCTYPE html>
<%@ page import="java.net.URLDecoder, com.fasterxml.jackson.databind.ObjectMapper, java.util.*, DAO.ProductDAO, model.Produit" %>
<%@ page import="jakarta.servlet.http.Cookie" %>
<%@ page import="com.fasterxml.jackson.core.type.TypeReference" %>
<%@ page import="model.User" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
  // 新增：获取用户登录状态
  User currentUser = (User) session.getAttribute("user");
  String cookieName = "panier"; // 默认Cookie名称

  // 如果已登录，生成用户专属Cookie名称
  if(currentUser != null){
    cookieName = "userId_" + currentUser.getIdUser() + "_panier";
  }

  // 修改：根据cookieName读取对应Cookie
  Cookie[] cookies = request.getCookies();
  String panierValue = "";
  for (Cookie cookie : cookies) {
    if (cookieName.equals(cookie.getName())) { // 这里改为动态名称
      panierValue = URLDecoder.decode(cookie.getValue(), "UTF-8");
      break;
    }
  }

  // 原有解析逻辑保持不变...
  ObjectMapper mapper = new ObjectMapper();
  Map<Long, Integer> panierMap = new HashMap<>();
  if (!panierValue.isEmpty()) {
    panierMap = mapper.readValue(panierValue, new TypeReference<HashMap<Long, Integer>>(){});
  }

  ProductDAO productDAO = new ProductDAO();
  List<Produit> products = productDAO.getAllProducts();
  Map<Long, Produit> productInfoMap = new HashMap<>();
  for (Produit p : products) {
    productInfoMap.put(Long.valueOf(p.getIdProduit()), p);
  }
%>


<html>
<head>
  <title>conformation commande</title>
</head>
<body>
<h1>confirme votre choix</h1>

<!-- 购物车内容 -->
<div class="cart-items">
  <% if (panierMap.isEmpty()) { %>
  <p>votre panier est vide</p>
  <% } else { %>
  <table>
    <tr>
      <th>nom de produit</th>
      <th>prix/unit</th>
      <th>quantity</th>
      <th>prix ensemble</th>
      <th>操作</th>
    </tr>
    <% for (Map.Entry<Long, Integer> entry : panierMap.entrySet()) {
      Produit p = productInfoMap.get(entry.getKey());
      if (p != null) { %>
    <tr data-product-id="<%= String.valueOf(entry.getKey()) %>"
        data-price="<%= p.getPrixUnit() %>">
      <td><%= p.getNomProduit() %></td>
      <td>€<span class="unit-price"><%= p.getPrixUnit() %></span></td>
      <td class="quantity"><%= entry.getValue() %></td>
      <td class="total-price">€<%= p.getPrixUnit() * entry.getValue() %></td>
    </tr>
    <% }
    } %>
  </table>
  <% } %>
</div>

<!-- 商店选择表单 -->
<form id="storeForm">
  <label for="magasin">choisir le magasin : </label>
  <select id="magasin" name="magasin" required>
    <option value="">choisir magasin</option>
  </select>
  <button type="submit">submit commande</button>
</form>

<script src="/ProjetDAI_war/static/JS/validerPanier.js"></script>
<script src="/ProjetDAI_war/js/script.js"></script>
</body>
</html>