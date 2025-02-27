<%--
  Created by IntelliJ IDEA.
  User: Gu HJ
  Date: 2025/2/24
  Time: 13:47
  To change this template use File | Settings | File Templates.
--%>
<%-- paymentSuccess.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Payment Successful</title>
</head>
<body>
<h1>Payment Completed Successfully!</h1>
<p>Order ID: <%= request.getParameter("commandeId") %></p>
<a href="index">Return to Home</a>
</body>
</html>
