<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Product, model.Category" %>

<!DOCTYPE html>
<html>
<head>
    <title>Detalle del Producto - Tienda de Ropa</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<div class="container">
    <%
        Product product = (Product) request.getAttribute("product");
        Category category = (Category) request.getAttribute("category");

        if (product != null) {
    %>
    <h1>Detalle del Producto: <%= product.getName() %></h1>

    <div class="nav-links">
        <a href="products">Volver a productos</a>
        <%
            String role = (String) session.getAttribute("role");
            if (role != null && role.equals("admin")) {
        %>
        <a href="edit_product.jsp?id=<%= product.getId() %>">Editar Producto</a>
        <a href="delete_product?product_id=<%= product.getId() %>"
           onclick="return confirm('¿Estás seguro de eliminar este producto?')">Eliminar Producto</a>
        <%
            }
        %>
    </div>

    <hr>

    <table>
        <tr>
            <td><strong>ID:</strong></td>
            <td><%= product.getId() %></td>
        </tr>
        <tr>
            <td><strong>Nombre:</strong></td>
            <td><%= product.getName() %></td>
        </tr>
        <tr>
            <td><strong>Descripción:</strong></td>
            <td><%= product.getDescription() %></td>
        </tr>
        <tr>
            <td><strong>Precio:</strong></td>
            <td><%= product.getPrice() %> €</td>
        </tr>
        <tr>
            <td><strong>Stock:</strong></td>
            <td><%= product.getStockQuantity() %> unidades</td>
        </tr>
        <tr>
            <td><strong>Fecha de lanzamiento:</strong></td>
            <td><%= product.getReleaseDate() %></td>
        </tr>
        <tr>
            <td><strong>Disponible:</strong></td>
            <td><%= product.isAvailable() ? "Sí" : "No" %></td>
        </tr>
        <tr>
            <td><strong>Categoría:</strong></td>
            <td>
                <% if (category != null) { %>
                <a href="category_detail?id=<%= category.getId() %>"><%= category.getName() %></a>
                <% } else { %>
                No disponible
                <% } %>
            </td>
        </tr>
        <tr>
            <td><strong>Marca:</strong></td>
            <td><%= product.getBrand() != null ? product.getBrand() : "No especificada" %></td>
        </tr>
        <tr>
            <td><strong>Talla:</strong></td>
            <td><%= product.getSize() != null ? product.getSize() : "No especificada" %></td>
        </tr>
        <tr>
            <td><strong>Color:</strong></td>
            <td><%= product.getColor() != null ? product.getColor() : "No especificado" %></td>
        </tr>
        <tr>
            <td><strong>Imagen:</strong></td>
            <td>
                <img src="images/<%= product.getImage() != null ? product.getImage() : "default.jpg" %>"
                     alt="<%= product.getName() %>"
                     style="width: 150px; height: 150px; object-fit: cover;">
            </td>
        </tr>
    </table>

    <%
        // Botón para clientes
        if (role != null && role.equals("cliente") && product.isAvailable() && product.getStockQuantity() > 0) {
    %>
    <p><strong>Precio: <%= product.getPrice() %> €</strong></p>
    <p>Disponible (Stock: <%= product.getStockQuantity() %> unidades)</p>
    <p><a href="add_order.jsp">Hacer pedido</a></p>
    <%
    } else if (role != null && role.equals("cliente") && (!product.isAvailable() || product.getStockQuantity() == 0)) {
    %>
    <p><strong>Precio: <%= product.getPrice() %> €</strong></p>
    <p>No disponible actualmente</p>
    <%
        }
    %>

    <%
    } else {
    %>
    <h1>Producto no encontrado</h1>
    <p><a href="products">Volver a productos</a></p>
    <%
        }
    %>
</div>
</body>
</html>