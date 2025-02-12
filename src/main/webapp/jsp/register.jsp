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
</head>
<body>
<div class="container">
    <h2>Inscription</h2>

    <%-- Affichage d'un message d'erreur si l'email est déjà pris --%>
    <% String error = (String) request.getAttribute("error"); %>
    <% if (error != null) { %>
    <p style="color: red;"><%= error %></p>
    <% } %>

    <form action="register" method="post" class="form-style">
        <label for="name">Nom:</label>
        <input type="text" id="name" name="name" required><br>

        <label for="prenom">Prénom:</label>
        <input type="text" id="prenom" name="prenom" required><br>

        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required><br>

        <label for="telephone">Téléphone:</label>
        <input type="text" id="telephone" name="telephone" pattern="^(\+?\d{1,3})?\d{9,12}$" title="Numéro de téléphone valide requis"><br>

        <label for="address">Adresse complète:</label>
        <input type="text" id="address" name="address"><br>

        <label for="password">Mot de passe:</label>
        <input type="password" id="password" name="password" required minlength="6"><br>

        <button type="submit">S'inscrire</button>
    </form>
    <a href="login.jsp">Déjà un compte ? Connectez-vous ici</a>
</div>
</body>
</html>