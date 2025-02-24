<%-- confirmation.jsp --%>
<%@ page import="model.Commande, DAO.CommandeDAO" %>
<%
    String commandeId = request.getParameter("commandeId");
    Commande commande = new CommandeDAO().getCommandeById(Integer.parseInt(commandeId));
%>

<!DOCTYPE html>
<html>
<head>
    <title>Payment Confirmation</title>
    <style>
        .payment-form {
            max-width: 400px;
            margin: 20px auto;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
        }
        input {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .expiry-wrapper {
            display: flex;
            gap: 10px;
        }
        button {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
    </style>
</head>
<body>
<h1>Order #<%= commande.getIdCommande() %> Payment</h1>
<div class="payment-form">
    <form action="ProcessPayment" method="POST">
        <input type="hidden" name="commandeId" value="<%= commande.getIdCommande() %>">

        <div class="form-group">
            <label>Card Number:</label>
            <input type="text" name="cardNumber" pattern="[0-9]{13,16}" required>
        </div>

        <div class="form-group">
            <label>Expiration Date:</label>
            <div class="expiry-wrapper">
                <input type="text" name="expiryMonth" placeholder="MM" pattern="(0[1-9]|1[0-2])" required>
                <input type="text" name="expiryYear" placeholder="YYYY" pattern="\d{4}" required>
            </div>
        </div>

        <div class="form-group">
            <label>CVV:</label>
            <input type="text" name="cvv" pattern="\d{3}" required>
        </div>

        <button type="submit">Confirm Payment</button>
    </form>
</div>
</body>
</html>