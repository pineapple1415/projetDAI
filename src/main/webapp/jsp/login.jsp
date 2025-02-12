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
    <title>login</title>
</head>
<body>
<h2>Connexion</h2>
<form action="login" method="post">
    Email: <input type="email" name="email" required><br>
    Mot de passe: <input type="password" name="password" required><br>
    <button type="submit">Se connecter</button>
</form>
<% if (request.getParameter("error") != null) { %>
<p style="color: red;">Identifiants incorrects</p>
<% } %>
<a href="register.jsp">Pas encore inscrit ? Créez un compte</a>
</body>
</html>
