<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, DAO.ProductDao, database.Database, model.Product" %>

<!DOCTYPE html>
<html>
<head>
    <title>Tienda de Ropa</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<h1>Bienvenido a la Tienda de Ropa</h1>

<%
    String username = (String) session.getAttribute("username");
    String role = (String) session.getAttribute("role");

    if (username != null) {
        String roleDisplay = "";
        if (role.equals("admin")) {
            roleDisplay = "Administrador";
        } else if (role.equals("cliente")) {
            roleDisplay = "Cliente";
        } else {
            roleDisplay = role;
        }
%>
<p>Has iniciado sesión como: <strong><%= username %></strong> (<%= roleDisplay %>)</p>
<a href="logout">Cerrar sesión</a>
<% if (session.getAttribute("username") != null) { %>
| <a href="profile">Mi Perfil</a>
<% } %>
<%
} else {
%>
<div class="main-buttons">
    <a href="login.jsp" class="main-button">Iniciar sesión</a>
    <a href="signup.jsp" class="main-button">Crear cuenta nueva</a>
</div>
<%
    }
%>

<hr>

<h2>Menú Principal</h2>
<ul>
    <li><a href="products">Ver Productos</a></li>
    <li><a href="categories">Ver Categorías</a></li>
    <li><a href="orders">Ver Pedidos</a></li>
</ul>

<%
    // Solo admin ve el panel de administrador
    if (role != null && role.equals("admin")) {
%>
<h3>Panel de Administrador</h3>
<ul>
    <li><a href="add_product.jsp">Añadir Producto</a></li>
    <li><a href="add_category.jsp">Añadir Categoría</a></li>
    <li><a href="add_order.jsp">Añadir Pedido</a></li>
</ul>
<%
} else if (role != null && role.equals("cliente")) {
%>
<h3>Panel de Cliente</h3>
<p>Como cliente puedes navegar y ver todos nuestros productos y categorías.</p>
<p><a href="add_order.jsp">Hacer nuevo pedido</a></p>
<%
    }
%>
</body>
</html>