package servlet;

import DAO.ProductDao;
import database.Database;
import model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

@WebServlet("/edit_product")
@MultipartConfig
public class EditProductServlet extends HttpServlet {

    private ArrayList<String> errors;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession currentSession = request.getSession();
        if ((currentSession.getAttribute("role") == null) || (!currentSession.getAttribute("role").equals("admin"))) {

            response.sendRedirect("/clothes-shop/login.jsp");
        }

        if (!validate(request)) {
            response.getWriter().println(errors.toString());
            return;
        }

        String action = request.getParameter("action");

        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String price = request.getParameter("price");
        String stockQuantity = request.getParameter("stockQuantity");
        String categoryId = request.getParameter("categoryId");
        Part image = request.getPart("image");
        String brand = request.getParameter("brand");
        String size = request.getParameter("size");
        String color = request.getParameter("color");

        try {
            Database database = new Database();
            database.connect();
            ProductDao productDao = new ProductDao(database.getConnection());
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(Float.parseFloat(price));
            product.setStockQuantity(Integer.parseInt(stockQuantity));
            product.setCategoryId(Integer.parseInt(categoryId));

            product.setReleaseDate(new Date(System.currentTimeMillis()));

            // Procesa la imagen del producto
            if (action.equals("Registrar")) {
                String filename = "default.jpg";
                if (image.getSize() != 0) {
                    filename = UUID.randomUUID() + ".jpg";

                    // GUARDAR EN AMBOS SITIOS:

                    // 1. Guardar en carpeta del servidor (para que se vea en web)
                    String serverImagePath = getServletContext().getRealPath("/") + "images";
                    File serverImageDir = new File(serverImagePath);
                    if (!serverImageDir.exists()) {
                        serverImageDir.mkdirs();
                    }

                    // 2. Guardar en carpeta del proyecto (para que persista)
                    String projectPath = System.getProperty("user.dir");
                    String projectImagePath = projectPath + File.separator + "src" + File.separator + "main" + File.separator + "webapp" + File.separator + "images";
                    File projectImageDir = new File(projectImagePath);
                    if (!projectImageDir.exists()) {
                        projectImageDir.mkdirs();
                    }

                    // Guardar en servidor (para web)
                    InputStream inputStream1 = image.getInputStream();
                    Files.copy(inputStream1, Path.of(serverImagePath + File.separator + filename));

                    // Guardar en proyecto (para persistencia)
                    InputStream inputStream2 = image.getInputStream();
                    Files.copy(inputStream2, Path.of(projectImagePath + File.separator + filename));
                }

                product.setImage(filename);
            } else {
                // MODIFICAR
                product.setId(Integer.parseInt(request.getParameter("productId")));

                if (image.getSize() != 0) {
                    // Si se sube nueva imagen
                    String filename = UUID.randomUUID() + ".jpg";

                    // GUARDAR EN AMBOS SITIOS:

                    // 1. Guardar en carpeta del servidor (para que se vea en web)
                    String serverImagePath = getServletContext().getRealPath("/") + "images";
                    File serverImageDir = new File(serverImagePath);
                    if (!serverImageDir.exists()) {
                        serverImageDir.mkdirs();
                    }

                    // 2. Guardar en carpeta del proyecto (para que persista)
                    String projectPath = System.getProperty("user.dir");
                    String projectImagePath = projectPath + File.separator + "src" + File.separator + "main" + File.separator + "webapp" + File.separator + "images";
                    File projectImageDir = new File(projectImagePath);
                    if (!projectImageDir.exists()) {
                        projectImageDir.mkdirs();
                    }

                    // Guardar en servidor (para web)
                    InputStream inputStream1 = image.getInputStream();
                    Files.copy(inputStream1, Path.of(serverImagePath + File.separator + filename));

                    // Guardar en proyecto (para persistencia)
                    InputStream inputStream2 = image.getInputStream();
                    Files.copy(inputStream2, Path.of(projectImagePath + File.separator + filename));

                    product.setImage(filename);
                } else {
                    // Si no se sube imagen, mantener la actual
                    Product currentProduct = productDao.get(Integer.parseInt(request.getParameter("productId")));
                    product.setImage(currentProduct.getImage());
                }
            }

            product.setAvailable(true);
            product.setBrand(brand);
            product.setSize(size);
            product.setColor(color);

            boolean done = false;
            if (action.equals("Registrar")) {
                done = productDao.add(product);
            } else {
                done = productDao.modify(product);
            }

            if (done) {
                response.sendRedirect("products");  // ESTO LLEVA A LA LISTA
            } else {
                response.getWriter().print("No se ha podido guardar el producto");
            }
        } catch (SQLException sqle) {
            response.getWriter().println("No se ha podido conectar con la base de datos");
            sqle.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            response.getWriter().println("No se ha podido cargar el driver de la base de datos");
            cnfe.printStackTrace();
        } catch (IOException ioe) {
            response.getWriter().println("Error no esperado: " + ioe.getMessage());
            ioe.printStackTrace();
        } catch (Exception e) {
            response.getWriter().println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validate(HttpServletRequest request) {
        errors = new ArrayList<>();
        if (request.getParameter("name").isEmpty()) {
            errors.add("El nombre es un campo obligatorio");
        }
        if ((request.getParameter("price").isEmpty()) || (!request.getParameter("price").matches("[0-9]+(\\.[0-9]{1,2})?"))) {
            errors.add("El precio es un campo numérico");

        }
        if (request.getParameter("stockQuantity").isEmpty()) {
            errors.add("El stock es un campo obligatorio");
        }
        if (request.getParameter("categoryId").isEmpty()) {
            errors.add("La categoría es un campo obligatorio");
        }


        return errors.isEmpty();
    }
}