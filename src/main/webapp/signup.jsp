<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>Registro - Tienda de Ropa</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<h1>Crear Cuenta Nueva</h1>

<form action="signup" method="post">
    <table>
        <tr>
            <td>Nombre de usuario: *</td>
            <td><input type="text" name="username" required maxlength="50"
                       placeholder="Ej: juan123"></td>
        </tr>
        <tr>
            <td>Email: *</td>
            <td><input type="email" name="email" required maxlength="100"
                       placeholder="Ej: juan@email.com"></td>
        </tr>
        <tr>
            <td>Contraseña: *</td>
            <td><input type="password" name="password" required minlength="6"
                       placeholder="Mínimo 6 caracteres"></td>
        </tr>
        <tr>
            <td>Confirmar contraseña: *</td>
            <td><input type="password" name="confirmPassword" required minlength="6"
                       placeholder="Repite la contraseña"></td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="Crear Cuenta">
                <input type="reset" value="Limpiar">
            </td>
        </tr>
    </table>

    <p><small>* Campos obligatorios</small></p>
    <p><small>Tu cuenta se creará como <strong>Cliente</strong> automáticamente.</small></p>
</form>

<hr>

<p><a href="login.jsp">¿Ya tienes cuenta? Inicia sesión aquí</a></p>
<p><a href="index.jsp">Volver al inicio</a></p>

<h3>Información:</h3>
<ul>
    <li>Al registrarte tendrás acceso como <strong>Cliente</strong></li>
    <li>Podrás ver productos, categorías y pedidos</li>
    <li>Para permisos de administrador, contacta al administrador</li>
</ul>
</body>
</html>