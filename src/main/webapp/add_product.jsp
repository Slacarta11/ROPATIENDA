<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList, DAO.CategoryDao, database.Database, model.Category" %>

<!DOCTYPE html>
<html>
<head>
  <title>Añadir Producto - Tienda de Ropa</title>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<div class="container">
  <h1>Añadir Nuevo Producto</h1>

  <p><a href="products">Volver a productos</a></p>

  <hr>

  <form action="edit_product" method="post" enctype="multipart/form-data">
    <input type="hidden" name="action" value="Registrar">

    <table>
      <tr>
        <td>Nombre: *</td>
        <td><input type="text" name="name" required maxlength="50"></td>
      </tr>
      <tr>
        <td>Descripción: *</td>
        <td><textarea name="description" rows="3" cols="30" required></textarea></td>
      </tr>
      <tr>
        <td>Precio: *</td>
        <td><input type="number" name="price" step="0.01" min="0.01" required></td>
      </tr>
      <tr>
        <td>Stock: *</td>
        <td><input type="number" name="stockQuantity" min="0" required></td>
      </tr>
      <tr>
        <td>Categoría: *</td>
        <td>
          <select name="categoryId" required>
            <option value="">Selecciona una categoría</option>
            <%
              try {
                Database db = new Database();
                db.connect();
                CategoryDao categoryDao = new CategoryDao(db.getConnection());
                ArrayList<Category> categories = categoryDao.getAll(0);
                for (Category category : categories) {
            %>
            <option value="<%= category.getId() %>"><%= category.getName() %></option>
            <%
                }
                db.close();
              } catch (Exception e) {
                e.printStackTrace();
              }
            %>
          </select>
        </td>
      </tr>
      <tr>
        <td>Marca:</td>
        <td><input type="text" name="brand" maxlength="50"></td>
      </tr>
      <tr>
        <td>Talla:</td>
        <td>
          <select name="size">
            <option value="">Selecciona talla</option>
            <option value="XS">XS</option>
            <option value="S">S</option>
            <option value="M">M</option>
            <option value="L">L</option>
            <option value="XL">XL</option>
            <option value="XXL">XXL</option>
          </select>
        </td>
      </tr>
      <tr>
        <td>Color:</td>
        <td><input type="text" name="color" maxlength="30"></td>
      </tr>
      <tr>
        <td>Imagen:</td>
        <td><input type="file" name="image" accept="image/*"></td>
      </tr>
      <tr>
        <td colspan="2">
          <input type="submit" value="Guardar Producto">
          <input type="reset" value="Limpiar">
        </td>
      </tr>
    </table>

    <p><small>* Campos obligatorios</small></p>
  </form>
</div>
</body>
</html>