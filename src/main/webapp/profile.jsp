<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>

<!DOCTYPE html>
<html>
<head>
  <title>Mi Perfil - Tienda de Ropa</title>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<%
  String username = (String) session.getAttribute("username");
  String role = (String) session.getAttribute("role");

  // Verificar que el usuario esté logueado
  if (username == null) {
    response.sendRedirect("login.jsp");
    return;
  }

  User user = (User) request.getAttribute("user");
%>

<h1>Mi Perfil</h1>

<div class="nav-links">
  <a href="index.jsp">Volver al inicio</a>
  <a href="edit_profile">Editar Perfil</a>
</div>

<hr>

<% if (user != null) { %>
<h2>Información Personal</h2>

<table>
  <tr>
    <td><strong>ID de Usuario:</strong></td>
    <td><%= user.getId() %></td>
  </tr>
  <tr>
    <td><strong>Nombre de Usuario:</strong></td>
    <td><%= user.getUsername() %></td>
  </tr>
  <tr>
    <td><strong>Email:</strong></td>
    <td><%= user.getEmail() %></td>
  </tr>
  <tr>
    <td><strong>Tipo de Usuario:</strong></td>
    <td>
      <% if ("admin".equals(user.getRole())) { %>
      Administrador
      <% } else if ("cliente".equals(user.getRole())) { %>
      Cliente
      <% } else { %>
      <%= user.getRole() %>
      <% } %>
    </td>
  </tr>
</table>

<hr>

<h2>Acciones de Perfil</h2>
<p><a href="edit_profile">Cambiar mis datos personales</a></p>
<p><a href="change_password.jsp">Cambiar mi contraseña</a></p>

<% } else { %>
<p>Error al cargar la información del perfil.</p>
<p><a href="index.jsp">Volver al inicio</a></p>
<% } %>

</body>
</html>