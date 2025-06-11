<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="DAO.OrderDao, database.Database, model.Order" %>

<!DOCTYPE html>
<html>
<head>
  <title>Editar Pedido - Tienda de Ropa</title>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<h1>Editar Pedido</h1>

<p><a href="orders">Volver a pedidos</a></p>

<hr>

<%
  String orderId = request.getParameter("id");
  Order order = null;

  if (orderId != null) {
    Database db = new Database();
    try {
      db.connect();
      OrderDao orderDao = new OrderDao(db.getConnection());
      order = orderDao.get(Integer.parseInt(orderId));
      db.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  if (order != null) {
%>
<form action="edit_order" method="post">
  <input type="hidden" name="action" value="Modificar">
  <input type="hidden" name="orderId" value="<%= order.getId() %>">

  <table>
    <tr>
      <td>Nombre del Cliente:</td>
      <td><input type="text" name="customerName" value="<%= order.getCustomerName() %>" required></td>
    </tr>
    <tr>
      <td>Email del Cliente:</td>
      <td><input type="email" name="customerEmail" value="<%= order.getCustomerEmail() %>" required></td>
    </tr>
    <tr>
      <td>Total del Pedido:</td>
      <td><input type="number" name="totalAmount" step="0.01" value="<%= order.getTotalAmount() %>" required></td>
    </tr>
    <tr>
      <td>Dirección de Envío:</td>
      <td><textarea name="shippingAddress" rows="3" cols="30" required><%= order.getShippingAddress() %></textarea></td>
    </tr>
    <tr>
      <td>Teléfono:</td>
      <td><input type="tel" name="phoneNumber" value="<%= order.getPhoneNumber() %>"></td>
    </tr>
    <tr>
      <td>Completado:</td>
      <td>
        <input type="checkbox" name="isCompleted" value="true" <%= order.isCompleted() ? "checked" : "" %>> Sí
      </td>
    </tr>
    <tr>
      <td>Fecha del Pedido:</td>
      <td><%= order.getOrderDate() %> (no modificable)</td>
    </tr>
    <tr>
      <td colspan="2">
        <input type="submit" value="Actualizar Pedido">
        <input type="button" value="Cancelar" onclick="window.location.href='order_detail?id=<%= order.getId() %>'">
      </td>
    </tr>
  </table>
</form>
<%
} else {
%>
<p>Pedido no encontrado.</p>
<p><a href="orders">Volver a pedidos</a></p>
<%
  }
%>
</body>
</html>