<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="DAO.CategoryDao, database.Database, model.Category" %>

<!DOCTYPE html>
<html>
<head>
  <title>Editar Categoría - Tienda de Ropa</title>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<h1>Editar Categoría</h1>

<p><a href="categories">Volver a categorías</a></p>

<hr>

<%
  String categoryId = request.getParameter("id");
  Category category = null;

  if (categoryId != null) {
    Database db = new Database();
    try {
      db.connect();
      CategoryDao categoryDao = new CategoryDao(db.getConnection());
      category = categoryDao.get(Integer.parseInt(categoryId));
      db.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  if (category != null) {
%>
<form action="edit_category" method="post" enctype="multipart/form-data">
  <input type="hidden" name="action" value="Modificar">
  <input type="hidden" name="categoryId" value="<%= category.getId() %>">

  <table>
    <tr>
      <td>Nombre:</td>
      <td><input type="text" name="name" value="<%= category.getName() %>" required></td>
    </tr>
    <tr>
      <td>Descripción:</td>
      <td><textarea name="description" rows="3" cols="30"><%= category.getDescription() %></textarea></td>
    </tr>
    <tr>
      <td>Descuento (%):</td>
      <td><input type="number" name="discountPercentage" step="0.01" min="0" max="100" value="<%= category.getDiscountPercentage() %>"></td>
    </tr>
    <tr>
      <td>Activa:</td>
      <td>
        <input type="checkbox" name="isActive" value="true" <%= category.isActive() ? "checked" : "" %>> Sí
      </td>
    </tr>
    <tr>
      <td>Imagen actual:</td>
      <td>
        <img src="images/<%= category.getImage() %>" alt="<%= category.getName() %>"
             style="width: 80px; height: 80px; object-fit: cover; border-radius: 4px;">
        <br><small><%= category.getImage() %></small>
      </td>
    </tr>
    <tr>
      <td>Nueva imagen:</td>
      <td><input type="file" name="image" accept="image/*"></td>
    </tr>
    <tr>
      <td colspan="2">
        <input type="submit" value="Actualizar Categoría">
        <input type="button" value="Cancelar" onclick="window.location.href='category_detail?id=<%= category.getId() %>'">
      </td>
    </tr>
  </table>
</form>
<%
} else {
%>
<p>Categoría no encontrada.</p>
<p><a href="categories">Volver a categorías</a></p>
<%
  }
%>
</body>
</html>