package servlet;

import DAO.UserDao;
import database.Database;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

    private ArrayList<String> errors;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!validate(request)) {
            // Si hay errores, mostrarlos y volver al formulario
            response.getWriter().println("<h2>Errores en el registro:</h2>");
            for (String error : errors) {
                response.getWriter().println("<p style='color: red;'>• " + error + "</p>");
            }
            response.getWriter().println("<p><a href='signup.jsp'>Volver al formulario</a></p>");
            return;
        }

        Database database = new Database();
        try {
            database.connect();
            UserDao userDao = new UserDao(database.getConnection());

            // Verificar si el usuario ya existe
            if (userDao.userExists(username)) {
                response.getWriter().println("<h2>Error:</h2>");
                response.getWriter().println("<p style='color: red;'>El nombre de usuario '" + username + "' ya está en uso.</p>");
                response.getWriter().println("<p><a href='signup.jsp'>Volver al formulario</a></p>");
                return;
            }

            // Crear nuevo usuario
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setRole("cliente"); // Todos los registros nuevos son clientes

            boolean success = userDao.registerUser(newUser);

            if (success) {
                // Registro exitoso - iniciar sesión automáticamente
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                session.setAttribute("role", "cliente");

                response.getWriter().println("<h2>¡Registro exitoso!</h2>");
                response.getWriter().println("<p style='color: green;'>Tu cuenta se ha creado correctamente como Cliente.</p>");
                response.getWriter().println("<p>Serás redirigido al inicio en 3 segundos...</p>");
                response.getWriter().println("<script>setTimeout(function(){ window.location.href='index.jsp'; }, 3000);</script>");
                response.getWriter().println("<p><a href='index.jsp'>Ir al inicio ahora</a></p>");
            } else {
                response.getWriter().println("<h2>Error:</h2>");
                response.getWriter().println("<p style='color: red;'>No se pudo crear la cuenta. Inténtalo de nuevo.</p>");
                response.getWriter().println("<p><a href='signup.jsp'>Volver al formulario</a></p>");
            }

        } catch (SQLException sqle) {
            response.getWriter().println("<h2>Error de base de datos:</h2>");
            response.getWriter().println("<p style='color: red;'>No se pudo conectar con la base de datos.</p>");
            response.getWriter().println("<p><a href='signup.jsp'>Volver al formulario</a></p>");
            sqle.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            response.getWriter().println("<h2>Error:</h2>");
            response.getWriter().println("<p style='color: red;'>No se pudo cargar el driver de la base de datos.</p>");
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

    private boolean validate(HttpServletRequest request) {
        errors = new ArrayList<>();

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validar campos vacíos
        if (username == null || username.trim().isEmpty()) {
            errors.add("El nombre de usuario es obligatorio");
        }
        if (email == null || email.trim().isEmpty()) {
            errors.add("El email es obligatorio");
        }
        if (password == null || password.trim().isEmpty()) {
            errors.add("La contraseña es obligatoria");
        }
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            errors.add("Debes confirmar la contraseña");
        }

        // Validar longitud mínima
        if (username != null && username.length() < 3) {
            errors.add("El nombre de usuario debe tener al menos 3 caracteres");
        }
        if (password != null && password.length() < 6) {
            errors.add("La contraseña debe tener al menos 6 caracteres");
        }

        // Validar que las contraseñas coincidan
        if (password != null && confirmPassword != null && !password.equals(confirmPassword)) {
            errors.add("Las contraseñas no coinciden");
        }

        // Validar formato de email básico
        if (email != null && !email.contains("@")) {
            errors.add("El email debe tener un formato válido");
        }

        return errors.isEmpty();
    }
}