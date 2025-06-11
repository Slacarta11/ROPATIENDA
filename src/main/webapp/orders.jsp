<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList, model.Order" %>

<!DOCTYPE html>
<html>
<head>
  <title>Pedidos - Tienda de Ropa</title>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<h1>Lista de Pedidos</h1>

<p><a href="index.jsp">Volver al inicio</a></p>

<%
  String role = (String) session.getAttribute("role");
  // Admin y clientes pueden añadir pedidos
  if (role != null && (role.equals("admin") || role.equals("cliente"))) {
%>
<p><a href="add_order.jsp">Añadir nuevo pedido</a></p>
<%
  }
%>

<!-- BÚSQUEDA AVANZADA -->
<h2>Búsqueda de Pedidos</h2>
<form action="orders" method="get">
  <table>
    <tr>
      <td>Cliente:</td>
      <td><input type="text" name="search" value="<%= request.getAttribute("search") != null ? request.getAttribute("search") : "" %>"></td>

      <td>Estado:</td>
      <td>
        <select name="status">
          <option value="">Todos</option>
          <option value="completed" <%= "completed".equals(request.getAttribute("status")) ? "selected" : "" %>>Solo completados</option>
          <option value="pending" <%= "pending".equals(request.getAttribute("status")) ? "selected" : "" %>>Solo pendientes</option>
        </select>
      </td>
    </tr>
    <tr>
      <td>Monto mínimo:</td>
      <td><input type="number" name="minAmount" step="0.01" value="<%= request.getAttribute("minAmount") != null ? request.getAttribute("minAmount") : "" %>"></td>

      <td>Monto máximo:</td>
      <td><input type="number" name="maxAmount" step="0.01" value="<%= request.getAttribute("maxAmount") != null ? request.getAttribute("maxAmount") : "" %>"></td>
    </tr>
    <tr>
      <td colspan="4">
        <input type="submit" value="Buscar">
        <input type="button" value="Limpiar" onclick="window.location.href='orders'">
      </td>
    </tr>
  </table>
</form>

<hr>

<%
  ArrayList<Order> orders = (ArrayList<Order>) request.getAttribute("orders");
  if (orders != null && !orders.isEmpty()) {
%>
<table border="1">
  <tr>
    <th>ID</th>
    <th>Cliente</th>
    <th>Email</th>
    <th>Total</th>
    <th>Fecha</th>
    <th>Completado</th>
    <th>Teléfono</th>
    <%
      // Solo admin ve columna de acciones
      if (role != null && role.equals("admin")) {
    %>
    <th>Acciones</th>
    <%
      }
    %>
  </tr>
  <%
    for (Order order : orders) {
  %>
  <tr>
    <td><%= order.getId() %></td>
    <td><a href="order_detail?id=<%= order.getId() %>"><%= order.getCustomerName() %></a></td>
    <td><%= order.getCustomerEmail() %></td>
    <td><%= order.getTotalAmount() %>€</td>
    <td><%= order.getOrderDate() %></td>
    <td><%= order.isCompleted() ? "Sí" : "No" %></td>
    <td><%= order.getPhoneNumber() %></td>
    <%
      // Solo admin ve botones de editar/eliminar
      if (role != null && role.equals("admin")) {
    %>
    <td>
      <a href="edit_order.jsp?id=<%= order.getId() %>">Editar</a> |
      <a href="delete_order?order_id=<%= order.getId() %>"
         onclick="return confirm('¿Estás seguro?')">Eliminar</a>
    </td>
    <%
      }
    %>
  </tr>
  <%
    }
  %>
</table>
<%
} else {
%>
<p>No hay pedidos disponibles.</p>
<%
  }
%>
</body>
</html>