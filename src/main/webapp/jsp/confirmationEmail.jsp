<%--
  Created by IntelliJ IDEA.
  User: rachi
  Date: 28/02/2025
  Time: 02:53
  To change this template use File | Settings | File Templates.
--%>
<%--
  Created by IntelliJ IDEA.
  User: rachi
  Date: <%= new java.util.Date() %>
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Confirmation d'envoi</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        .confirmation-container {
            margin-top: 100px;
            text-align: center;
            padding: 40px;
            border: 2px solid #28a745;
            border-radius: 10px;
            max-width: 600px;
            margin-left: auto;
            margin-right: auto;
        }
        .success-icon {
            font-size: 4rem;
            color: #28a745;
            margin-bottom: 20px;
        }
    </style>
</head>
<body class="container">

<div class="confirmation-container">
    <div class="success-icon">✓</div>
    <h2 class="text-success">E-mail envoyé avec succès !</h2>
    <p class="lead mt-3">
        La notification pour le retrait de la commande a bien été envoyée au client.
    </p>

    <div class="mt-4">
        <a href="commande?action=listePreparations" class="btn btn-primary btn-lg">
            ← Retour à l'accueil
        </a>
    </div>
</div>

</body>
</html>
