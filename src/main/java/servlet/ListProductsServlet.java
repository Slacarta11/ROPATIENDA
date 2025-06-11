package servlet;

import DAO.ProductDao;
import database.Database;
import model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/products")
public class ListProductsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String pageParam = request.getParameter("page");
        String search = request.getParameter("search");
        String categoryId = request.getParameter("categoryId");
        String minPrice = request.getParameter("minPrice");
        String maxPrice = request.getParameter("maxPrice");

        int page = 0;
        if (pageParam != null && !pageParam.isEmpty()) {
            page = Integer.parseInt(pageParam);
        }

        try {
            Database db = new Database();
            db.connect();
            ProductDao productDao = new ProductDao(db.getConnection());

            ArrayList<Product> products;

            // Si hay algún criterio de búsqueda, usar búsqueda avanzada
            if ((search != null && !search.isEmpty()) ||
                    (categoryId != null && !categoryId.isEmpty()) ||
                    (minPrice != null && !minPrice.isEmpty()) ||
                    (maxPrice != null && !maxPrice.isEmpty())) {
                products = productDao.searchAdvanced(page, search, categoryId, minPrice, maxPrice);
            } else {
                products = productDao.getAll(page);
            }

            request.setAttribute("products", products);
            request.setAttribute("currentPage", page);
            request.setAttribute("search", search);
            request.setAttribute("categoryId", categoryId);
            request.setAttribute("minPrice", minPrice);
            request.setAttribute("maxPrice", maxPrice);

            request.getRequestDispatcher("products.jsp").forward(request, response);

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}