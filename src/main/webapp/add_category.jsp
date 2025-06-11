<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
  <title>Añadir Categoría - Tienda de Ropa</title>
  <meta charset="UTF-8">
  <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
<h1>Añadir Nueva Categoría</h1>

<p><a href="categories">Volver a categorías</a></p>

<hr>

<form action="edit_category" method="post" enctype="multipart/form-data">
  <input type="hidden" name="action" value="Registrar">

  <table>
    <tr>
      <td>Nombre:</td>
      <td><input type="text" name="name" required></td>
    </tr>
    <tr>
      <td>Descripción:</td>
      <td><textarea name="description" rows="3" cols="30"></textarea></td>
    </tr>
    <tr>
      <td>Descuento (%):</td>
      <td><input type="number" name="discountPercentage" step="0.01" min="0" max="100" value="0"></td>
    </tr>
    <tr>
      <td>Activa:</td>
      <td>
        <input type="checkbox" name="isActive" value="true" checked> Sí
      </td>
    </tr>
    <tr>
      <td>Imagen:</td>
      <td><input type="file" name="image" accept="image/*"></td>
    </tr>
    <tr>
      <td colspan="2">
        <input type="submit" value="Guardar Categoría">
        <input type="reset" value="Limpiar">
      </td>
    </tr>
  </table>
</form>
</body>
</html>