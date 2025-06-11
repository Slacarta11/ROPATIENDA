<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Category" %>

<!DOCTYPE html>
<html>
<head>
  <title>Detalle de la Categoría - Tienda de Ropa</title>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<div class="container">
  <%
    Category category = (Category) request.getAttribute("category");

    if (category != null) {
  %>
  <h1>Detalle de la Categoría: <%= category.getName() %></h1>

  <div class="nav-links">
    <a href="categories">Volver a categorías</a>
    <%
      String role = (String) session.getAttribute("role");
      if (role != null && role.equals("admin")) {
    %>
    <a href="edit_category.jsp?id=<%= category.getId() %>">Editar Categoría</a>
    <a href="delete_category?category_id=<%= category.getId() %>"
       onclick="return confirm('¿Estás seguro de eliminar esta categoría?')">Eliminar</a>
    <%
      }
    %>
  </div>

  <hr>

  <table>
    <tr>
      <td><strong>ID:</strong></td>
      <td><%= category.getId() %></td>
    </tr>
    <tr>
      <td><strong>Nombre:</strong></td>
      <td><%= category.getName() %></td>
    </tr>
    <tr>
      <td><strong>Descripción:</strong></td>
      <td><%= category.getDescription() %></td>
    </tr>
    <tr>
      <td><strong>Descuento:</strong></td>
      <td><%= category.getDiscountPercentage() %>%</td>
    </tr>
    <tr>
      <td><strong>Fecha de creación:</strong></td>
      <td><%= category.getCreationDate() %></td>
    </tr>
    <tr>
      <td><strong>Estado:</strong></td>
      <td><%= category.isActive() ? "Activa" : "Inactiva" %></td>
    </tr>
    <tr>
      <td><strong>Imagen:</strong></td>
      <td>
        <img src="images/<%= category.getImage() != null ? category.getImage() : "default.jpg" %>"
             alt="<%= category.getName() %>"
             style="width: 100px; height: 100px; object-fit: cover; border-radius: 8px;">
      </td>
    </tr>
  </table>

  <%
  } else {
  %>
  <h1>Categoría no encontrada</h1>
  <p><a href="categories">Volver a categorías</a></p>
  <%
    }
  %>
</div>
</body>
</html>