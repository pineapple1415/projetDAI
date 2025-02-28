<%--
  Created by IntelliJ IDEA.
  User: rachi
  Date: 27/02/2025
  Time: 23:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Confirmation de réservation</title>
  <style>
    .container {
      width: 50%;
      margin: auto;
      text-align: center;
      margin-top: 50px;
      padding: 20px;
      border: 1px solid #ddd;
      border-radius: 10px;
    }
    h2 {
      color: #4CAF50;
    }
    .button-container {
      display: flex;
      justify-content: space-between;
      margin-top: 20px;
    }
    .button {
      padding: 10px 20px;
      font-size: 16px;
      color: white;
      background-color: #4CAF50;
      border: none;
      border-radius: 5px;
      text-decoration: none;
      cursor: pointer;
      transition: background-color 0.3s;
    }
    .button:hover {
      background-color: #45a049;
    }
  </style>
</head>
<body>
<!-- confirmationCreneau.jsp -->
<div class="container">
  <h2><%= request.getAttribute("message") %></h2>
  <p>Merci, nous vous attendrons au magasin à la date et l'heure réservées.</p>
  <div class="button-container">
    <a href="commande?action=login" class="button">Retour aux site</a>
    <a href="creneau?idCommande=${param.idCommande}" class="button">Modifier le créneau</a>
  </div>
</div>
</body>
</html>

