<%--
  Created by IntelliJ IDEA.
  User: rachi
  Date: 26/02/2025
  Time: 23:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Confirmation Sélection des Commandes</title>
    <style>
        /* Style général */
        body {
            font-family: Arial, sans-serif;
            background: linear-gradient(135deg, #74ebd5, #acb6e5);
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        /* Conteneur */
        .container {
            background: #fff;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
            max-width: 600px;
            text-align: center;
        }

        /* Titre */
        h1 {
            color: #333;
            font-size: 1.8em;
            margin-bottom: 20px;
        }

        /* Liste des commandes */
        .order-list {
            text-align: left;
            margin: 20px 0;
            padding: 10px;
            background: #f9f9f9;
            border-radius: 8px;
        }

        .order-list li {
            font-size: 1.1em;
            padding: 5px;
            color: #333;
        }

        /* Boutons */
        .btn-container {
            display: flex;
            justify-content: center;
            gap: 15px;
        }

        button {
            background-color: #2c3e50;
            color: white;
            border: none;
            padding: 12px 20px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 1.1em;
            transition: all 0.3s ease;
        }

        button:hover {
            background-color: #34495e;
        }

        /* Boutons spécifiques */
        button[name="action"][value="non"] {
            background-color: #f39c12;
        }

        button[name="action"][value="non"]:hover {
            background-color: #e67e22;
        }

        button[name="action"][value="annuler"] {
            background-color: #e74c3c;
        }

        button[name="action"][value="annuler"]:hover {
            background-color: #c0392b;
        }
    </style>
</head>
<body>

<div class="container">
    <h1>Confirmer la sélection des commandes suivantes :</h1>

    <div class="order-list">
        <ul>
            <%
                String[] commandesSelectionnees = (String[]) session.getAttribute("commandesSelectionnees");
                if (commandesSelectionnees != null && commandesSelectionnees.length > 0) {
                    for (String id : commandesSelectionnees) {
            %>
            <li>Commande ID: <strong><%= id %></strong></li>
            <%
                }
            } else {
            %>
            <p>Aucune commande sélectionnée.</p>
            <%
                }
            %>
        </ul>
    </div>

    <form action="ServletValiderSelection" method="post">
        <div class="btn-container">
            <!-- ✅ Bouton "Oui" pour confirmer -->
            <button type="submit" name="action" value="Confirmer">✅Confirmer</button>

            <!-- ❌ Bouton "Non" -->
            <button type="submit" name="action" value="Annuler">❌ Annuler</button>

            <!-- 🔙 Bouton "Annuler" -->
            <button type="submit" name="action" value="Retour">🔙Retour</button>
        </div>
    </form>
</div>

</body>
</html>
