<%--
  Created by IntelliJ IDEA.
  User: Gu HJ
  Date: 2025/2/12
  Time: 14:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.net.URLDecoder, com.fasterxml.jackson.databind.ObjectMapper, java.util.*, DAO.ProductDAO, model.Produit" %>
<%@ page import="jakarta.servlet.http.Cookie" %>
<%@ page import="com.fasterxml.jackson.core.type.TypeReference" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
  // 读取购物车Cookie
  Cookie[] cookies = request.getCookies();
  String panierValue = "";
  for (Cookie cookie : cookies) {
    if ("panier".equals(cookie.getName())) {
      panierValue = URLDecoder.decode(cookie.getValue(), "UTF-8");
      break;
    }
  }

  // 解析JSON数据
  ObjectMapper mapper = new ObjectMapper();
  Map<Long, Integer> panierMap = new HashMap<>();
  if (!panierValue.isEmpty()) {
    panierMap = mapper.readValue(panierValue, new TypeReference<HashMap<Long, Integer>>(){});
  }

  // 获取商品详细信息
  ProductDAO productDAO = new ProductDAO();
  List<Produit> products = productDAO.getAllProducts();
  Map<Long, Produit> productInfoMap = new HashMap<>();
  for (Produit p : products) {
    productInfoMap.put(Long.valueOf(p.getIdProduit()), p);
  }
%>


<html>
<head>
  <title>Mon Panier</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<%-- 调试输出（临时可见） --%>
<div style="border: 1px solid red; padding: 10px; margin: 20px;">
  <h3>Debug信息</h3>
  <p>Cookie原始值: <%= panierValue %></p>
  <p>解析后的Map: <%= panierMap %></p>
  <p>商品总数: <%= productInfoMap.size() %></p>
  <p>匹配的商品ID:
    <% for (Long key : panierMap.keySet()) { %>
    <%= key %> -> <%= productInfoMap.containsKey(key) ? "存在" : "不存在" %> <br>
    <% } %>
  </p>
</div>


<h1>🛒 Mon panier</h1>
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
      <td>
        <button class="btn-minus" data-action="decrease">-</button>
        <button class="btn-plus" data-action="increase">+</button>
      </td>
    </tr>
    <% }
    } %>
  </table>
  <% } %>
</div>

<div class="total-section">
  <h3 id="total-price">Prix Total : €0.00</h3>
</div>


<div class="button-group" style="margin-top: 20px;">
  <button onclick="clearCart()" style="background-color: #ff4444; color: white;">
    🗑️ vider panier
  </button>
  <button onclick="location.href='index'" style="margin-left: 10px;">
    ← continue achate
  </button>

  <button id="validateCart" onclick="window.location.href='ValidePanier'">passer la commande</button>


</div>

<script src="/ProjetDAI_war/js/script.js"></script>

<script>

  updateTotalPrice();

  document.querySelector('.cart-items').addEventListener('click', function(e) {
    const target = e.target;

    // 仅处理加减按钮的点击
    if (target.classList.contains('btn-minus') || target.classList.contains('btn-plus')) {
      // 获取商品行元素
      const row = target.closest('tr[data-product-id]');
      if (!row) return; // 安全校验

      // 从行元素获取商品ID和单价
      const productId = row.dataset.productId;
      const delta = target.classList.contains('btn-minus') ? -1 : 1; // 操作类型

      // 调用更新函数
      updateQuantity(productId, delta);
    }
  });
</script>

</body>
</html>
