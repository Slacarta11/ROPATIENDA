<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="DAO.ProductDao, DAO.CategoryDao, database.Database, model.Product, model.Category, java.util.ArrayList" %>

<!DOCTYPE html>
<html>
<head>
    <title>Editar Producto - Tienda de Ropa</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<h1>Editar Producto</h1>

<p><a href="products">Volver a productos</a></p>

<hr>

<%
    String productId = request.getParameter("id");
    Product product = null;

    if (productId != null) {
        try {
            Database db = new Database();
            db.connect();
            ProductDao productDao = new ProductDao(db.getConnection());
            product = productDao.get(Integer.parseInt(productId));
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    if (product != null) {
%>
<form action="edit_product" method="post" enctype="multipart/form-data">
    <input type="hidden" name="action" value="Modificar">
    <input type="hidden" name="productId" value="<%= product.getId() %>">

    <table>
        <tr>
            <td>Nombre:</td>
            <td><input type="text" name="name" value="<%= product.getName() %>" required></td>
        </tr>
        <tr>
            <td>Descripción:</td>
            <td><textarea name="description" rows="3" cols="30"><%= product.getDescription() %></textarea></td>
        </tr>
        <tr>
            <td>Precio:</td>
            <td><input type="number" name="price" step="0.01" value="<%= product.getPrice() %>" required></td>
        </tr>
        <tr>
            <td>Stock:</td>
            <td><input type="number" name="stockQuantity" value="<%= product.getStockQuantity() %>" required></td>
        </tr>
        <tr>
            <td>Categoría:</td>
            <td>
                <select name="categoryId" required>
                    <%
                        try {
                            Database db = new Database();
                            db.connect();
                            CategoryDao categoryDao = new CategoryDao(db.getConnection());
                            ArrayList<Category> categories = categoryDao.getAll(0);
                            for (Category category : categories) {
                                String selected = (category.getId() == product.getCategoryId()) ? "selected" : "";
                    %>
                    <option value="<%= category.getId() %>" <%= selected %>><%= category.getName() %></option>
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
            <td><input type="text" name="brand" value="<%= product.getBrand() %>"></td>
        </tr>
        <tr>
            <td>Talla:</td>
            <td>
                <select name="size">
                    <option value="">Selecciona talla</option>
                    <option value="XS" <%= "XS".equals(product.getSize()) ? "selected" : "" %>>XS</option>
                    <option value="S" <%= "S".equals(product.getSize()) ? "selected" : "" %>>S</option>
                    <option value="M" <%= "M".equals(product.getSize()) ? "selected" : "" %>>M</option>
                    <option value="L" <%= "L".equals(product.getSize()) ? "selected" : "" %>>L</option>
                    <option value="XL" <%= "XL".equals(product.getSize()) ? "selected" : "" %>>XL</option>
                    <option value="XXL" <%= "XXL".equals(product.getSize()) ? "selected" : "" %>>XXL</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>Color:</td>
            <td><input type="text" name="color" value="<%= product.getColor() %>"></td>
        </tr>
        <tr>
            <td>Imagen actual:</td>
            <td><%= product.getImage() %></td>
        </tr>
        <tr>
            <td>Nueva imagen:</td>
            <td><input type="file" name="image" accept="image/*"></td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="Actualizar Producto">
                <input type="button" value="Cancelar" onclick="window.location.href='products'">
            </td>
        </tr>
    </table>
</form>
<%
} else {
%>
<p>Producto no encontrado.</p>
<p><a href="products">Volver a productos</a></p>
<%
    }
%>
</body>
</html>