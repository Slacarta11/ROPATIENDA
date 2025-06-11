<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.ArrayList, model.Product, model.Category, DAO.CategoryDao, database.Database" %>

<!DOCTYPE html>
<html>
<head>
    <title>Productos - Tienda de Ropa</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<div class="container">
    <h1>Lista de Productos</h1>

    <p><a href="index.jsp">Volver al inicio</a></p>

    <%
        String role = (String) session.getAttribute("role");
        // Solo admin puede añadir productos
        if (role != null && role.equals("admin")) {
    %>
    <p><a href="add_product.jsp">Añadir nuevo producto</a></p>
    <%
        }
    %>

    <!-- BÚSQUEDA AVANZADA -->
    <h2>Búsqueda Avanzada</h2>
    <form action="products" method="get">
        <table>
            <tr>
                <td>Nombre:</td>
                <td><input type="text" name="search" value="<%= request.getAttribute("search") != null ? request.getAttribute("search") : "" %>"></td>

                <td>Categoría:</td>
                <td>
                    <select name="categoryId">
                        <option value="">Todas</option>
                        <%
                            try {
                                Database dbCat = new Database();
                                dbCat.connect();
                                CategoryDao categoryDao = new CategoryDao(dbCat.getConnection());
                                ArrayList<Category> categories = categoryDao.getAll(0);
                                String selectedCategory = (String) request.getAttribute("categoryId");
                                for (Category category : categories) {
                                    String selected = (selectedCategory != null && selectedCategory.equals(String.valueOf(category.getId()))) ? "selected" : "";
                        %>
                        <option value="<%= category.getId() %>" <%= selected %>><%= category.getName() %></option>
                        <%
                                }
                                dbCat.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        %>
                    </select>
                </td>
            </tr>
            <tr>
                <td>Precio mín:</td>
                <td><input type="number" name="minPrice" step="0.01" value="<%= request.getAttribute("minPrice") != null ? request.getAttribute("minPrice") : "" %>"></td>

                <td>Precio máx:</td>
                <td><input type="number" name="maxPrice" step="0.01" value="<%= request.getAttribute("maxPrice") != null ? request.getAttribute("maxPrice") : "" %>"></td>
            </tr>
            <tr>
                <td colspan="4">
                    <input type="submit" value="Buscar">
                    <input type="button" value="Volver" onclick="window.location.href='products'">
                </td>
            </tr>
        </table>
    </form>

    <hr>

    <%
        ArrayList<Product> products = (ArrayList<Product>) request.getAttribute("products");
        if (products != null && !products.isEmpty()) {
    %>
    <p>Se encontraron <%= products.size() %> productos</p>
    <table border="1">
        <tr>
            <th>Imagen</th>
            <th>ID</th>
            <th>Nombre</th>
            <th>Descripción</th>
            <th>Precio</th>
            <th>Stock</th>
            <th>Marca</th>
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
            for (Product product : products) {
        %>
        <tr>
            <td>
                <img src="images/<%= product.getImage() %>" alt="<%= product.getName() %>"
                     style="width: 80px; height: 80px; object-fit: cover; border-radius: 8px; border: 2px solid #ddd;">
            </td>
            <td><%= product.getId() %></td>
            <td><a href="product_detail?id=<%= product.getId() %>"><%= product.getName() %></a></td>
            <td><%= product.getDescription() %></td>
            <td><%= product.getPrice() %>€</td>
            <td><%= product.getStockQuantity() %></td>
            <td><%= product.getBrand() %></td>
            <%
                // Solo admin ve botones de editar/eliminar
                if (role != null && role.equals("admin")) {
            %>
            <td>
                <a href="edit_product.jsp?id=<%= product.getId() %>">Editar</a> |
                <a href="delete_product?product_id=<%= product.getId() %>"
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
    <p>No hay productos disponibles.</p>
    <%
        }
    %>
</div>
</body>
</html>