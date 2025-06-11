package servlet;

import DAO.UserDao;
import database.Database;
import model.User;
import exception.UserNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/edit_profile")
public class EditProfileServlet extends HttpServlet {

    private ArrayList<String> errors;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Mostrar formulario de edición - reutilizar código de ProfileServlet
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession currentSession = request.getSession();
        String username = (String) currentSession.getAttribute("username");

        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Database database = new Database();
        try {
            database.connect();
            UserDao userDao = new UserDao(database.getConnection());
            User user = userDao.getUserByUsername(username);

            request.setAttribute("user", user);
            request.getRequestDispatcher("edit_profile.jsp").forward(request, response);

        } catch (UserNotFoundException unfe) {
            response.getWriter().println("<h2>Error:</h2>");
            response.getWriter().println("<p style='color: red;'>Usuario no encontrado.</p>");
            response.getWriter().println("<p><a href='profile'>Volver al perfil</a></p>");
        } catch (SQLException sqle) {
            response.getWriter().println("<h2>Error de base de datos:</h2>");
            response.getWriter().println("<p style='color: red;'>No se pudo conectar con la base de datos: " + sqle.getMessage() + "</p>");
            sqle.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            response.getWriter().println("<h2>Error:</h2>");
            response.getWriter().println("<p style='color: red;'>No se pudo cargar el driver de la base de datos.</p>");
            cnfe.printStackTrace();
        } catch (Exception e) {
            response.getWriter().println("<h2>Error inesperado:</h2>");
            response.getWriter().println("<p style='color: red;'>Error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        } finally {
            try {
                if (database != null) {
                    database.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession currentSession = request.getSession();
        String username = (String) currentSession.getAttribute("username");

        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("updateProfile".equals(action)) {
            updateProfile(request, response, username);
        } else if ("changePassword".equals(action)) {
            changePassword(request, response, username);
        } else {
            response.sendRedirect("profile");
        }
    }

    private void updateProfile(HttpServletRequest request, HttpServletResponse response, String currentUsername) throws IOException {
        String newUsername = request.getParameter("username");
        String email = request.getParameter("email");

        if (!validateProfileData(newUsername, email)) {
            response.getWriter().println("<h2>Errores en el formulario:</h2>");
            for (String error : errors) {
                response.getWriter().println("<p style='color: red;'>• " + error + "</p>");
            }
            response.getWriter().println("<p><a href='edit_profile'>Volver al formulario</a></p>");
            return;
        }

        Database database = new Database();
        try {
            database.connect();
            UserDao userDao = new UserDao(database.getConnection());

            // Verificar si el nuevo username ya existe (solo si es diferente del actual)
            if (!newUsername.equals(currentUsername) && userDao.userExists(newUsername)) {
                response.getWriter().println("<h2>Error:</h2>");
                response.getWriter().println("<p style='color: red;'>El nombre de usuario '" + newUsername + "' ya está en uso.</p>");
                response.getWriter().println("<p><a href='edit_profile'>Volver al formulario</a></p>");
                return;
            }

            // Actualizar perfil
            boolean success = userDao.updateProfile(currentUsername, newUsername, email);

            if (success) {
                // Actualizar la sesión si cambió el username
                if (!newUsername.equals(currentUsername)) {
                    request.getSession().setAttribute("username", newUsername);
                }

                response.getWriter().println("<h2>Perfil actualizado correctamente</h2>");
                response.getWriter().println("<p style='color: green;'>Tus datos se han actualizado con éxito.</p>");
                response.getWriter().println("<p>Serás redirigido a tu perfil en 3 segundos...</p>");
                response.getWriter().println("<script>setTimeout(function(){ window.location.href='profile'; }, 3000);</script>");
                response.getWriter().println("<p><a href='profile'>Ver mi perfil</a></p>");
            } else {
                response.getWriter().println("<h2>Error:</h2>");
                response.getWriter().println("<p style='color: red;'>No se pudo actualizar el perfil.</p>");
                response.getWriter().println("<p><a href='edit_profile'>Volver al formulario</a></p>");
            }

        } catch (SQLException sqle) {
            response.getWriter().println("<h2>Error de base de datos</h2>");
            sqle.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            response.getWriter().println("<h2>Error del sistema</h2>");
            cnfe.printStackTrace();
        } finally {
            try {
                if (database != null) {
                    database.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void changePassword(HttpServletRequest request, HttpServletResponse response, String username) throws IOException {
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!validatePasswordData(currentPassword, newPassword, confirmPassword)) {
            response.getWriter().println("<h2>Errores en el cambio de contraseña:</h2>");
            for (String error : errors) {
                response.getWriter().println("<p style='color: red;'>• " + error + "</p>");
            }
            response.getWriter().println("<p><a href='edit_profile'>Volver al formulario</a></p>");
            return;
        }

        Database database = new Database();
        try {
            database.connect();
            UserDao userDao = new UserDao(database.getConnection());

            // Verificar contraseña actual
            try {
                userDao.loginUser(username, currentPassword);
            } catch (UserNotFoundException e) {
                response.getWriter().println("<h2>Error:</h2>");
                response.getWriter().println("<p style='color: red;'>La contraseña actual es incorrecta.</p>");
                response.getWriter().println("<p><a href='edit_profile'>Volver al formulario</a></p>");
                return;
            }

            // Cambiar contraseña
            boolean success = userDao.changePassword(username, newPassword);

            if (success) {
                response.getWriter().println("<h2>Contraseña cambiada correctamente</h2>");
                response.getWriter().println("<p style='color: green;'>Tu contraseña se ha actualizado con éxito.</p>");
                response.getWriter().println("<p>Serás redirigido a tu perfil en 3 segundos...</p>");
                response.getWriter().println("<script>setTimeout(function(){ window.location.href='profile'; }, 3000);</script>");
                response.getWriter().println("<p><a href='profile'>Ver mi perfil</a></p>");
            } else {
                response.getWriter().println("<h2>Error:</h2>");
                response.getWriter().println("<p style='color: red;'>No se pudo cambiar la contraseña.</p>");
                response.getWriter().println("<p><a href='edit_profile'>Volver al formulario</a></p>");
            }

        } catch (SQLException sqle) {
            response.getWriter().println("<h2>Error de base de datos</h2>");
            sqle.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            response.getWriter().println("<h2>Error del sistema</h2>");
            cnfe.printStackTrace();
        } finally {
            try {
                if (database != null) {
                    database.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validateProfileData(String username, String email) {
        errors = new ArrayList<>();

        if (username == null || username.trim().isEmpty()) {
            errors.add("El nombre de usuario es obligatorio");
        } else if (username.length() < 3) {
            errors.add("El nombre de usuario debe tener al menos 3 caracteres");
        }

        if (email == null || email.trim().isEmpty()) {
            errors.add("El email es obligatorio");
        } else if (!email.contains("@")) {
            errors.add("El email debe tener un formato válido");
        }

        return errors.isEmpty();
    }

    private boolean validatePasswordData(String currentPassword, String newPassword, String confirmPassword) {
        errors = new ArrayList<>();

        if (currentPassword == null || currentPassword.trim().isEmpty()) {
            errors.add("Debes introducir tu contraseña actual");
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            errors.add("La nueva contraseña es obligatoria");
        } else if (newPassword.length() < 6) {
            errors.add("La nueva contraseña debe tener al menos 6 caracteres");
        }

        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            errors.add("Debes confirmar la nueva contraseña");
        } else if (newPassword != null && !newPassword.equals(confirmPassword)) {
            errors.add("Las contraseñas no coinciden");
        }

        return errors.isEmpty();
    }
}