<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
  <title>Cambiar Contraseña - Tienda de Ropa</title>
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
%>

<h1>Cambiar Mi Contraseña</h1>

<div class="nav-links">
  <a href="profile">Volver a mi perfil</a>
  <a href="index.jsp">Ir al inicio</a>
</div>

<hr>

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
        <input type="button" value="Cancelar" onclick="window.location.href='profile'">
      </td>
    </tr>
  </table>

  <p><small>* La contraseña debe tener al menos 6 caracteres</small></p>
  <p><small>Por seguridad, necesitas introducir tu contraseña actual para confirmar el cambio.</small></p>
</form>

</body>
</html>