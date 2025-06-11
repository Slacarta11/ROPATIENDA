package servlet;

import DAO.CategoryDao;
import database.Database;
import model.Category;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/categories")
public class ListCategoriesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String pageParam = request.getParameter("page");
        String search = request.getParameter("search");
        String status = request.getParameter("status");

        int page = 0;
        if (pageParam != null && !pageParam.isEmpty()) {
            page = Integer.parseInt(pageParam);
        }

        Database db = new Database();
        try {
            db.connect();
            CategoryDao categoryDao = new CategoryDao(db.getConnection());

            ArrayList<Category> categories;

            // Si hay criterios de búsqueda, filtrar
            if ((search != null && !search.isEmpty()) || (status != null && !status.isEmpty())) {
                categories = categoryDao.searchAdvanced(page, search, status);
            } else {
                categories = categoryDao.getAll(page);
            }

            request.setAttribute("categories", categories);
            request.setAttribute("currentPage", page);
            request.setAttribute("search", search);
            request.setAttribute("status", status);

            request.getRequestDispatcher("categories.jsp").forward(request, response);

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (db != null) {
                    db.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}