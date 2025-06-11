package servlet;

import DAO.OrderDao;
import database.Database;
import model.Order;
import exception.OrderNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/order_detail")
public class OrderDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");


        String orderId = request.getParameter("id");

        if (orderId == null || orderId.isEmpty()) {
            response.sendRedirect("orders");
            return;
        }

        try {
            Database db = new Database();
            db.connect();
            OrderDao orderDao = new OrderDao(db.getConnection());

            Order order = orderDao.get(Integer.parseInt(orderId));

            request.setAttribute("order", order);

            request.getRequestDispatcher("order_detail.jsp").forward(request, response);

        } catch (OrderNotFoundException onfe) {
            response.sendRedirect("orders");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}