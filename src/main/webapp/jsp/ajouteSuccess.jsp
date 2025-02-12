<%--
  Created by IntelliJ IDEA.
  User: Gu HJ
  Date: 2025/2/12
  Time: 14:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Ajoute Sucess !</title>
  <style>
    .success-box {
      text-align: center;
      margin-top: 100px;
      padding: 20px;
      border: 2px solid #4CAF50;
      border-radius: 10px;
      max-width: 500px;
      margin-left: auto;
      margin-right: auto;
    }
    .button-group {
      margin-top: 20px;
    }
    .btn {
      padding: 10px 20px;
      margin: 0 10px;
      border: none;
      border-radius: 5px;
      cursor: pointer;
    }
    .view-cart {
      background-color: #4CAF50;
      color: white;
    }
    .back-home {
      background-color: #2196F3;
      color: white;
    }
  </style>
</head>
<body>
<div class="success-box">
  <h2> ajoute reussi ！</h2>
  <div class="button-group">
    <button class="btn view-cart" onclick="location.href='${pageContext.request.contextPath}/affichePanier'">
      查看购物车
    </button>
    <button class="btn back-home" onclick="location.href='${pageContext.request.contextPath}/index'">
      返回首页
    </button>
  </div>
</div>
</body>
</html>

