package servlet;

import DAO.CategoryDao;
import database.Database;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/delete_category")
public class DeleteCategoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession currentSession = request.getSession();
        if (currentSession.getAttribute("role") == null) {
            response.sendRedirect("/clothes-shop/login.jsp");
            return;
        }

        String categoryId = request.getParameter("category_id");
        // TODO Añadir validación

        try {
            Database db = new Database();
            db.connect();
            CategoryDao categoryDao = new CategoryDao(db.getConnection());
            categoryDao.delete(Integer.parseInt(categoryId));

            response.sendRedirect("/clothes-shop");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}