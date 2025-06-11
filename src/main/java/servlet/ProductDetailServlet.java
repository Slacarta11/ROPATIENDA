package servlet;

import DAO.ProductDao;
import DAO.CategoryDao;
import database.Database;
import model.Product;
import model.Category;
import exception.ProductNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/product_detail")
public class ProductDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");  // ← AÑADE ESTA LÍNEA
        response.setContentType("text/html; charset=UTF-8");  // ← Y ESTA


        String productId = request.getParameter("id");

        if (productId == null || productId.isEmpty()) {
            response.sendRedirect("products");
            return;
        }

        try {
            Database db = new Database();
            db.connect();
            ProductDao productDao = new ProductDao(db.getConnection());
            CategoryDao categoryDao = new CategoryDao(db.getConnection());

            Product product = productDao.get(Integer.parseInt(productId));
            Category category = categoryDao.get(product.getCategoryId());

            request.setAttribute("product", product);
            request.setAttribute("category", category);

            request.getRequestDispatcher("product_detail.jsp").forward(request, response);

        } catch (ProductNotFoundException pnfe) {
            response.sendRedirect("products");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}