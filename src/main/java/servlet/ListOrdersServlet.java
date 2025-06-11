package servlet;

import DAO.OrderDao;
import database.Database;
import model.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/orders")
public class ListOrdersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String pageParam = request.getParameter("page");
        String search = request.getParameter("search");
        String status = request.getParameter("status");
        String minAmount = request.getParameter("minAmount");
        String maxAmount = request.getParameter("maxAmount");

        int page = 0;
        if (pageParam != null && !pageParam.isEmpty()) {
            page = Integer.parseInt(pageParam);
        }

        Database db = new Database();
        try {
            db.connect();
            OrderDao orderDao = new OrderDao(db.getConnection());

            ArrayList<Order> orders;

            // Si hay criterios de búsqueda, usar búsqueda avanzada
            if ((search != null && !search.isEmpty()) ||
                    (status != null && !status.isEmpty()) ||
                    (minAmount != null && !minAmount.isEmpty()) ||
                    (maxAmount != null && !maxAmount.isEmpty())) {
                orders = orderDao.searchAdvanced(page, search, status, minAmount, maxAmount);
            } else {
                orders = orderDao.getAll(page);
            }

            request.setAttribute("orders", orders);
            request.setAttribute("currentPage", page);
            request.setAttribute("search", search);
            request.setAttribute("status", status);
            request.setAttribute("minAmount", minAmount);
            request.setAttribute("maxAmount", maxAmount);

            request.getRequestDispatcher("orders.jsp").forward(request, response);

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