<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList, model.Category" %>

<!DOCTYPE html>
<html>
<head>
  <title>Categorías - Tienda de Ropa</title>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<h1>Lista de Categorías</h1>

<p><a href="index.jsp">Volver al inicio</a></p>

<%
  String role = (String) session.getAttribute("role");
  // Solo admin puede añadir categorías
  if (role != null && role.equals("admin")) {
%>
<p><a href="add_category.jsp">Añadir nueva categoría</a></p>
<%
  }
%>

<!-- BÚSQUEDA AVANZADA -->
<h2>Búsqueda de Categorías</h2>
<form action="categories" method="get">
  <table>
    <tr>
      <td>Nombre:</td>
      <td><input type="text" name="search" value="<%= request.getAttribute("search") != null ? request.getAttribute("search") : "" %>"></td>

      <td>Estado:</td>
      <td>
        <select name="status">
          <option value="">Todas</option>
          <option value="active" <%= "active".equals(request.getAttribute("status")) ? "selected" : "" %>>Solo activas</option>
          <option value="inactive" <%= "inactive".equals(request.getAttribute("status")) ? "selected" : "" %>>Solo inactivas</option>
        </select>
      </td>
    </tr>
    <tr>
      <td colspan="4">
        <input type="submit" value="Buscar">
        <input type="button" value="Limpiar" onclick="window.location.href='categories'">
      </td>
    </tr>
  </table>
</form>

<hr>

<%
  ArrayList<Category> categories = (ArrayList<Category>) request.getAttribute("categories");
  if (categories != null && !categories.isEmpty()) {
%>
<table border="1">
  <tr>
    <th>ID</th>
    <th>Nombre</th>
    <th>Descripción</th>
    <th>Descuento (%)</th>
    <th>Activa</th>
    <th>Imagen</th>
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
    for (Category category : categories) {
  %>
  <tr>
    <td><%= category.getId() %></td>
    <td><a href="category_detail?id=<%= category.getId() %>"><%= category.getName() %></a></td>
    <td><%= category.getDescription() %></td>
    <td><%= category.getDiscountPercentage() %>%</td>
    <td><%= category.isActive() ? "Sí" : "No" %></td>
    <td>
      <img src="images/<%= category.getImage() %>" alt="<%= category.getName() %>"
           style="width: 50px; height: 50px; object-fit: cover;">
    </td>
    <%
      // Solo admin ve botones de editar/eliminar
      if (role != null && role.equals("admin")) {
    %>
    <td>
      <a href="edit_category.jsp?id=<%= category.getId() %>">Editar</a> |
      <a href="delete_category?category_id=<%= category.getId() %>"
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
<p>No hay categorías disponibles.</p>
<%
  }
%>
</body>
</html>