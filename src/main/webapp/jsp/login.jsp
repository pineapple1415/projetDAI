<%--
  Created by IntelliJ IDEA.
  User: rachi
  Date: 12/02/2025
  Time: 08:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Connexion</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css" type="text/css" />
</head>
<body>
<div class="container">
    <h2>Connexion</h2>

    <%-- Affichage d'un message d'erreur si les identifiants sont incorrects --%>
    <% if (request.getParameter("error") != null) { %>
    <p style="color: red;">Email ou mot de passe incorrect</p>
    <% } %>

    <form action="login" method="post">
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required><br>

        <label for="password">Mot de passe:</label>
        <input type="password" id="password" name="password" required><br>

        <button type="submit">Se connecter</button>
    </form>

    <a href="register.jsp">Pas encore inscrit ? Créez un compte</a>
</div>
</body>
</html>