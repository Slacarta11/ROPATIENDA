<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
  <title>Añadir Pedido - Tienda de Ropa</title>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<h1>Añadir Nuevo Pedido</h1>

<p><a href="orders">Volver a pedidos</a></p>

<hr>

<form action="edit_order" method="post">
  <input type="hidden" name="action" value="Registrar">

  <table>
    <tr>
      <td>Nombre del Cliente:</td>
      <td><input type="text" name="customerName" required></td>
    </tr>
    <tr>
      <td>Email del Cliente:</td>
      <td><input type="email" name="customerEmail" required></td>
    </tr>
    <tr>
      <td>Total del Pedido:</td>
      <td><input type="number" name="totalAmount" step="0.01" required></td>
    </tr>
    <tr>
      <td>Dirección de Envío:</td>
      <td><textarea name="shippingAddress" rows="3" cols="30" required></textarea></td>
    </tr>
    <tr>
      <td>Teléfono:</td>
      <td><input type="tel" name="phoneNumber"></td>
    </tr>
    <tr>
      <td>Completado:</td>
      <td>
        <input type="checkbox" name="isCompleted" value="true"> Sí
      </td>
    </tr>
    <tr>
      <td colspan="2">
        <input type="submit" value="Guardar Pedido">
        <input type="reset" value="Limpiar">
        <input type="button" value="Cancelar" onclick="window.location.href='orders'">
      </td>
    </tr>
  </table>
</form>
</body>
</html>