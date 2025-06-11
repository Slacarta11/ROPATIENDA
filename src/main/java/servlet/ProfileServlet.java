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

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession currentSession = request.getSession();
        String username = (String) currentSession.getAttribute("username");

        // Verificar que el usuario esté logueado
        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Database database = new Database();
        try {
            database.connect();
            UserDao userDao = new UserDao(database.getConnection());

            // Obtener los datos del usuario actual
            User user = userDao.getUserByUsername(username);

            request.setAttribute("user", user);
            request.getRequestDispatcher("profile.jsp").forward(request, response);

        } catch (UserNotFoundException unfe) {
            response.getWriter().println("<h2>Error:</h2>");
            response.getWriter().println("<p style='color: red;'>Usuario no encontrado.</p>");
            response.getWriter().println("<p><a href='index.jsp'>Volver al inicio</a></p>");
        } catch (SQLException sqle) {
            response.getWriter().println("<h2>Error de base de datos:</h2>");
            response.getWriter().println("<p style='color: red;'>No se pudo conectar con la base de datos.</p>");
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
}