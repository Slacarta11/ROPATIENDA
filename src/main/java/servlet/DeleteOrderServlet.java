package servlet;

import DAO.OrderDao;
import database.Database;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/delete_order")
public class DeleteOrderServlet extends HttpServlet {

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

        String orderId = request.getParameter("order_id");
        // TODO Añadir validación

        Database db = new Database();
        try {
            db.connect();
            OrderDao orderDao = new OrderDao(db.getConnection());
            orderDao.delete(Integer.parseInt(orderId));

            response.sendRedirect("orders");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // IMPORTANTE: Siempre cerrar la conexión
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