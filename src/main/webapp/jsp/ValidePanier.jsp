<!DOCTYPE html>
<html>
<head>
  <title>conformation commande</title>
</head>
<body>
<h1>confirme votre choix</h1>

<!-- 购物车内容 -->
<div id="cartItems">
  <table>
    <thead>
    <tr>
      <th>nom produit</th>
      <th>quantity</th>
      <th>prix/unit</th>
      <th>total</th>
    </tr>
    </thead>
    <tbody id="cartBody"></tbody>
  </table>
  <p id="totalPrice">total prix : 0.00 euros</p>
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
</body>
</html>