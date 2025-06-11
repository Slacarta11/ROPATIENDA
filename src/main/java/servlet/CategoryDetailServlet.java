package servlet;

import DAO.CategoryDao;
import database.Database;
import model.Category;
import exception.CategoryNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/category_detail")
public class CategoryDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String categoryId = request.getParameter("id");

        if (categoryId == null || categoryId.isEmpty()) {
            response.sendRedirect("categories");
            return;
        }

        try {
            Database db = new Database();
            db.connect();
            CategoryDao categoryDao = new CategoryDao(db.getConnection());

            Category category = categoryDao.get(Integer.parseInt(categoryId));

            request.setAttribute("category", category);

            request.getRequestDispatcher("category_detail.jsp").forward(request, response);

        } catch (CategoryNotFoundException cnfe) {
            response.sendRedirect("categories");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (ClassNotFoundException cnf) {
            cnf.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}