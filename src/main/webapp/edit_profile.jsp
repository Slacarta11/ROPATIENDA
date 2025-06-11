<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>

<!DOCTYPE html>
<html>
<head>
  <title>Editar Perfil - Tienda de Ropa</title>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<%
  String username = (String) session.getAttribute("username");

  // Verificar que el usuario esté logueado
  if (username == null) {
    response.sendRedirect("login.jsp");
    return;
  }

  User user = (User) request.getAttribute("user");
%>

<h1>Editar Mi Perfil</h1>

<div class="nav-links">
  <a href="profile">Volver a mi perfil</a>
  <a href="index.jsp">Ir al inicio</a>
</div>

<hr>

<% if (user != null) { %>
<form action="edit_profile" method="post">
  <input type="hidden" name="action" value="updateProfile">

  <table>
    <tr>
      <td>Nombre de Usuario: *</td>
      <td>
        <input type="text" name="username" value="<%= user.getUsername() %>" required maxlength="50">
        <small>(Debe ser único)</small>
      </td>
    </tr>
    <tr>
      <td>Email: *</td>
      <td>
        <input type="email" name="email" value="<%= user.getEmail() %>" required maxlength="100">
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <input type="submit" value="Actualizar Datos">
        <input type="button" value="Cancelar" onclick="window.location.href='profile'">
      </td>
    </tr>
  </table>

  <p><small>* Campos obligatorios</small></p>
</form>

<hr>

<h2>Cambiar Contraseña</h2>
<form action="edit_profile" method="post">
  <input type="hidden" name="action" value="changePassword">

  <table>
    <tr>
      <td>Contraseña Actual: *</td>
      <td><input type="password" name="currentPassword" required></td>
    </tr>
    <tr>
      <td>Nueva Contraseña: *</td>
      <td><input type="password" name="newPassword" required minlength="6"></td>
    </tr>
    <tr>
      <td>Confirmar Nueva Contraseña: *</td>
      <td><input type="password" name="confirmPassword" required minlength="6"></td>
    </tr>
    <tr>
      <td colspan="2">
        <input type="submit" value="Cambiar Contraseña">
      </td>
    </tr>
  </table>

  <p><small>* La contraseña debe tener al menos 6 caracteres</small></p>
</form>

<% } else { %>
<p>Error al cargar los datos del usuario.</p>
<p><a href="profile">Volver a mi perfil</a></p>
<% } %>

</body>
</html>