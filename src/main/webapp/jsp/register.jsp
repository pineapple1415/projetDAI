<%--
  Created by IntelliJ IDEA.
  User: rachi
  Date: 12/02/2025
  Time: 08:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Inscription</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css" type="text/css" />
    <style>
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background-color: #f8f9fa;
        }

        .container {
            width: 400px;
            padding: 20px;
            background: white;
            border-radius: 10px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        h2 {
            margin-bottom: 20px;
        }

        .error-message {
            color: red;
            font-size: 14px;
            margin-bottom: 10px;
        }

        label {
            display: block;
            text-align: left;
            margin-top: 10px;
            font-weight: bold;
        }

        input {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        button {
            width: 100%;
            padding: 10px;
            margin-top: 15px;
            border: none;
            background-color: #007BFF;
            color: white;
            font-size: 16px;
            border-radius: 5px;
            cursor: pointer;
        }

        button:hover {
            background-color: #0056b3;
        }

        .login-link {
            margin-top: 10px;
            font-size: 14px;
        }

        .login-link a {
            color: #007BFF;
            text-decoration: none;
        }

        .login-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Inscription</h2>

    <%-- Affichage d'un message d'erreur si l'email est déjà pris --%>
    <% String error = (String) request.getAttribute("error"); %>
    <% if (error != null) { %>
    <p class="error-message"><%= error %></p>
    <% } %>

    <form action="register" method="post">
        <label for="name">Nom:</label>
        <input type="text" id="name" name="name" required>

        <label for="prenom">Prénom:</label>
        <input type="text" id="prenom" name="prenom" required>

        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required>

        <label for="telephone">Téléphone:</label>
        <input type="text" id="telephone" name="telephone" pattern="^(\+?\d{1,3})?\d{9,12}$" title="Numéro de téléphone valide requis">

        <label for="address">Adresse complète:</label>
        <input type="text" id="address" name="address">

        <label for="password">Mot de passe:</label>
        <input type="password" id="password" name="password" required minlength="6">

        <button type="submit">S'inscrire</button>
    </form>

    <p class="login-link">
        <a href="login.jsp">Déjà un compte ? Connectez-vous ici</a>
    </p>
</div>
</body>
</html>