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
  <title>Accueil Préparateur</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f4f4f4;
      margin: 0;
      padding: 0;
    }
    .container {
      width: 80%;
      margin: 50px auto;
      background-color: white;
      border-radius: 8px;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
      padding: 20px;
    }
    h2 {
      color: #333;
      text-align: center;
    }
    ul {
      list-style-type: none;
      padding: 0;
      display: flex;
      flex-direction: column;
      align-items: center;
    }
    li {
      margin: 15px 0;
      width: 100%;
      text-align: center;
    }
    a {
      text-decoration: none;
      color: white;
      background-color: #007BFF;
      padding: 10px 20px;
      border-radius: 5px;
      font-size: 16px;
      transition: background-color 0.3s;
    }
    a:hover {
      background-color: #0056b3;
    }
    .msg_erreur {
      color: red;
      background-color: #ffcccb;
      padding: 10px;
      border-radius: 5px;
      text-align: center;
      margin-top: 20px;
    }
    .footer {
      text-align: center;
      margin-top: 30px;
      font-size: 14px;
      color: #888;
    }
  </style>
</head>
<body>
<div class="container">
  <h2>Bienvenue sur votre espace Préparateur</h2>

  <ul>
    <!-- US 5.2 : Consulter la liste des commandes à préparer par ordre de priorité -->
    <li>
      <a href="<%= request.getContextPath() %>/commande?action=listePreparations"> Consulter la liste des commandes à préparer </a>
    </li>

    <li>
      <a href="<%= request.getContextPath() %>/commande?action=listePreparationsPrioritaire">Liste des commandes à préparer par prioritée</a>
    </li>

    <li>
      <a href="<%= request.getContextPath() %>/commande?action=listePrioritairePrete">Consulter la Liste des commandes à préparer prioritée Pretes</a>
    </li>




    <!-- US 5.3 et US 5.4 : Ces actions se font via des formulaires sur la page de liste ou de détail de commande -->
    <!-- Pour les actions liées aux formulaires, vous pouvez les retrouver sur les pages appropriées -->
  </ul>

  <%
    // Affichage éventuel d'un message d'erreur
    String msg_erreur = (String) request.getAttribute("msgerreur");
    if (msg_erreur != null) {
  %>
  <div class="msg_erreur">
    <%= msg_erreur %>
  </div>
  <%
    }
  %>
</div>

<div class="footer">
  <p>© 2025 Système de gestion des commandes</p>
</div>
</body>
</html>
