<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
  <title>Login - Tienda de Ropa</title>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<h1>Iniciar Sesión</h1>

<form action="login" method="post">
  <table>
    <tr>
      <td>Usuario:</td>
      <td><input type="text" name="username" required></td>
    </tr>
    <tr>
      <td>Contraseña:</td>
      <td><input type="password" name="password" required></td>
    </tr>
    <tr>
      <td colspan="2">
        <input type="submit" value="Entrar">
      </td>
    </tr>
  </table>
</form>

<hr>

<h3>¿No tienes cuenta?</h3>
<p><a href="signup.jsp" style="background-color: #4CAF50; color: white; padding: 10px 20px; text-decoration: none; border-radius: 4px;">Crear cuenta nueva</a></p>

<p><a href="index.jsp">Volver al inicio</a></p>

<hr>
<h3>Usuarios de prueba:</h3>
<table border="1">
  <tr>
    <th>Usuario</th>
    <th>Contraseña</th>
    <th>Rol</th>
    <th>Permisos</th>
  </tr>
  <tr>
    <td><strong>admin</strong></td>
    <td>admin123</td>
    <td>Administrador</td>
    <td>Puede crear, editar y eliminar todo</td>
  </tr>
  <tr>
    <td colspan="4" style="text-align: center; color: #666;">
      <em>O crea tu propia cuenta como Cliente usando el botón de arriba</em>
    </td>
  </tr>
</table>
</body>
</html>