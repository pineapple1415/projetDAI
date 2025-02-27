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
  <title>confirme votre choix</title>
  <style>
    .out-of-stock {
      background-color: #f0f0f0 !important;
      color: #999 !important;
    }
    .out-of-stock button {
      opacity: 0.5;
      cursor: not-allowed !important;
    }
  </style>
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
    </tr>
    <% for (Map.Entry<Long, Integer> entry : panierMap.entrySet()) {
      Produit p = productInfoMap.get(entry.getKey());
      if (p != null) { %>
    <tr data-product-id="<%= String.valueOf(entry.getKey()) %>"
        data-price="<%= p.getPrixUnit() %>"
        data-pricereduit="<%= p.getPrixApresPromotion() %>">
      <td><%= p.getNomProduit() %></td>
      <td>€<span class="unit-price"><%= p.getPrixUnit() %></span></td>
      <td class="quantity"><%= entry.getValue() %></td>
      <td class="total-price" id="total-price-apres">€<%= p.getPrixApresPromotion() * entry.getValue() %></td>
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

<script>
  // 新增库存检查和总价更新逻辑
  function checkStockStatus() {
    const productIds = Array.from(document.querySelectorAll('tr[data-product-id]'))
            .map(row => parseInt(row.dataset.productId));

    if (productIds.length === 0) return;

    fetch('/ProjetDAI_war/checkStock', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(productIds)
    })
            .then(response => response.json())
            .then(stockStatus => {
              document.querySelectorAll('tr[data-product-id]').forEach(row => {
                const productId = parseInt(row.dataset.productId);
                if (!stockStatus[productId]) {
                  row.classList.add('out-of-stock');
                  row.querySelectorAll('button').forEach(btn => btn.disabled = true);
                }
              });
              updateTotalPrice();
            })
            .catch(error => console.error('库存检查失败:', error));
  }

  function updateTotalPrice() {
    let total = 0;

    document.querySelectorAll('tr[data-product-id]').forEach(row => {
      if (!row.classList.contains('out-of-stock')) {
        const price = parseFloat(row.dataset.pricereduit || row.dataset.price);
        const quantity = parseInt(row.querySelector('.quantity').innerText);
        total += price * quantity;
      }
    });

    // 添加总价显示元素（如果不存在）
    if (!document.getElementById('totalPrice')) {
      const totalDiv = document.createElement('div');
      totalDiv.id = 'totalPrice';
      totalDiv.style.margin = '20px 0';
      document.querySelector('.cart-items').appendChild(totalDiv);
    }

    document.getElementById('totalPrice').textContent =
            'Prix Total : €' + total.toFixed(2);
  }

  // 初始化时执行
  document.addEventListener("DOMContentLoaded", () => {
    updateTotalPrice();
    checkStockStatus();
  });
</script>
</body>
</html>