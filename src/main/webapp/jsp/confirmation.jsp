<%@ page import="model.Commande" %>
<%@ page import="DAO.CommandeDAO" %>
<%
    String commandeId = request.getParameter("commandeId");
    Commande commande = new CommandeDAO().getCommandeById(Integer.parseInt(commandeId));
%>

<!DOCTYPE html>
<html>
<head>
    <title>cmd confirm</title>
</head>
<body>
<h1>votre commande est creer</h1>

<div class="order-info">
    <p>commande id : <%= commande.getIdCommande() %></p>
    <p>total : <%= String.format("%.2f", commande.getPrixTotal()) %></p>
    <p>magasin reserve :<%= commande.getMagasin().getNomMagasin() %></p>
</div>

<a href="index.jsp">back to index</a>
</body>
</html>