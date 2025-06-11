package servlet;

import DAO.ProductDao;
import database.Database;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/delete_product")
public class DeleteProductServlet extends HttpServlet {

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

        String productId = request.getParameter("product_id");
        // TODO Añadir validación

        try {
            Database db = new Database();
            db.connect();
            ProductDao productDao = new ProductDao(db.getConnection());
            productDao.delete(Integer.parseInt(productId));
            // TODO Borrar también la foto

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