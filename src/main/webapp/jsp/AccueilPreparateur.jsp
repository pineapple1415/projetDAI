<%--
  Created by IntelliJ IDEA.
  User: rachi
  Date: 25/02/2025
  Time: 00:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Tableau de Bord Préparateur</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f4f4f4;
      margin: 0;
      padding: 0;
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
    }
    .container {
      width: 80%;
      background-color: white;
      border-radius: 8px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
      padding: 30px;
      text-align: center;
    }
    h2 {
      color: #333;
      margin-bottom: 20px;
    }
    .grid {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 20px;
      justify-items: center;
    }
    .button {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 10px;
      text-decoration: none;
      color: white;
      background-color: #007BFF;
      padding: 15px;
      border-radius: 8px;
      font-size: 16px;
      font-weight: bold;
      transition: background-color 0.3s;
      width: 200px;
    }
    .button:hover {
      background-color: #0056b3;
    }
    .icon {
      font-size: 20px;
    }
    .back-link {
      display: block;
      margin-top: 20px;
      text-align: center;
      font-size: 18px;
    }

    .back-link a {
      text-decoration: none;
      color: #007BFF;
    }

    .back-link a:hover {
      text-decoration: underline;
    }
  </style>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
</head>
<body>
<div class="container">
  <h2>Tableau de Bord Préparateur</h2>
  <div class="grid">
    <a href="<%= request.getContextPath() %>/commande?action=listePreparations" class="button">
      <i class="fa-solid fa-eye icon"></i> Consulter la liste des commandes prioritaire
    </a>
    <a href="<%= request.getContextPath() %>/commande?action=listePreparationsPrioritaire" class="button">
      <i class="fa-solid fa-list icon"></i> Consulter la liste des commandes en cours de preparation
    </a>
    <a href="<%= request.getContextPath() %>/commande?action=listePrioritairePrete" class="button">
      <i class="fa-solid fa-check-double icon"></i> Consulter les commandes pretes
    </a>
  </div>
  <div class="back-link">
    <a href="commande?action=login">Deconnexion</a>
  </div>

</div>
</body>
</html>
