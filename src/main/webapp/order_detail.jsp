<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Order" %>

<!DOCTYPE html>
<html>
<head>
  <title>Detalle del Pedido - Tienda de Ropa</title>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<div class="container">
  <%
    Order order = (Order) request.getAttribute("order");

    if (order != null) {
  %>
  <h1>Detalle del Pedido #<%= order.getId() %></h1>

  <div class="nav-links">
    <a href="orders">Volver a pedidos</a>
    <%
      String role = (String) session.getAttribute("role");
      if (role != null && role.equals("admin")) {
    %>
    <a href="edit_order.jsp?id=<%= order.getId() %>">Editar Pedido</a>
    <a href="delete_order?order_id=<%= order.getId() %>"
       onclick="return confirm('¿Estás seguro de eliminar este pedido?')">Eliminar</a>
    <%
      }
    %>
  </div>

  <hr>

  <table>
    <tr>
      <td><strong>ID del Pedido:</strong></td>
      <td><%= order.getId() %></td>
    </tr>
    <tr>
      <td><strong>Cliente:</strong></td>
      <td><%= order.getCustomerName() %></td>
    </tr>
    <tr>
      <td><strong>Email:</strong></td>
      <td><%= order.getCustomerEmail() %></td>
    </tr>
    <tr>
      <td><strong>Total:</strong></td>
      <td><%= order.getTotalAmount() %>€</td>
    </tr>
    <tr>
      <td><strong>Fecha del pedido:</strong></td>
      <td><%= order.getOrderDate() %></td>
    </tr>
    <tr>
      <td><strong>Estado:</strong></td>
      <td><%= order.isCompleted() ? "Completado" : "Pendiente" %></td>
    </tr>
    <tr>
      <td><strong>Dirección de envío:</strong></td>
      <td><%= order.getShippingAddress() %></td>
    </tr>
    <tr>
      <td><strong>Teléfono:</strong></td>
      <td><%= order.getPhoneNumber() != null ? order.getPhoneNumber() : "No especificado" %></td>
    </tr>
  </table>

  <%
  } else {
  %>
  <h1>Pedido no encontrado</h1>
  <p><a href="orders">Volver a pedidos</a></p>
  <%
    }
  %>
</div>
</body>
</html>