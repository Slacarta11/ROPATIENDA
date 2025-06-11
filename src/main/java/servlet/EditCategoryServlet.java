package servlet;

import DAO.CategoryDao;
import database.Database;
import model.Category;

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

@WebServlet("/edit_category")
@MultipartConfig
public class EditCategoryServlet extends HttpServlet {

    private ArrayList<String> errors;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession currentSession = request.getSession();
        if ((currentSession.getAttribute("role") == null) || (!currentSession.getAttribute("role").equals("admin"))) {
            response.sendRedirect("/clothes-shop/login.jsp");
            return;
        }

        if (!validate(request)) {
            response.getWriter().println(errors.toString());
            return;
        }

        String action = request.getParameter("action");

        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String discountPercentage = request.getParameter("discountPercentage");
        Part image = request.getPart("image");
        String isActive = request.getParameter("isActive");

        Database database = new Database();
        try {
            database.connect();
            CategoryDao categoryDao = new CategoryDao(database.getConnection());
            Category category = new Category();
            category.setName(name);
            category.setDescription(description);
            category.setDiscountPercentage(Float.parseFloat(discountPercentage));
            category.setCreationDate(new Date(System.currentTimeMillis()));

            // Procesa la imagen de la categoría
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
                category.setImage(filename);
            } else {
                // MODIFICAR - manejar imagen correctamente
                category.setId(Integer.parseInt(request.getParameter("categoryId")));

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

                    category.setImage(filename);
                } else {
                    // Si no se sube imagen, mantener la actual
                    Category currentCategory = categoryDao.get(Integer.parseInt(request.getParameter("categoryId")));
                    category.setImage(currentCategory.getImage());
                }
            }

            category.setActive(isActive != null && isActive.equals("true"));

            boolean done = false;
            if (action.equals("Registrar")) {
                done = categoryDao.add(category);
            } else {
                done = categoryDao.modify(category);
            }

            if (done) {
                response.sendRedirect("categories");
            } else {
                response.getWriter().print("No se ha podido guardar la categoría");
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
        } finally {
            // IMPORTANTE: Siempre cerrar la conexión
            try {
                if (database != null) {
                    database.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validate(HttpServletRequest request) {
        errors = new ArrayList<>();
        if (request.getParameter("name").isEmpty()) {
            errors.add("El nombre es un campo obligatorio");
        }
        if ((request.getParameter("discountPercentage").isEmpty()) || (!request.getParameter("discountPercentage").matches("[0-9]+(\\.[0-9]{1,2})?"))) {
            errors.add("El descuento es un campo numérico");
        }

        return errors.isEmpty();
    }
}