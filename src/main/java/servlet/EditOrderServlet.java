package servlet;

import DAO.OrderDao;
import database.Database;
import model.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/edit_order")
public class EditOrderServlet extends HttpServlet {

    private ArrayList<String> errors;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession currentSession = request.getSession();
        String userRole = (String) currentSession.getAttribute("role");
        String action = request.getParameter("action");

        // Verificar permisos
        if (userRole == null) {
            response.sendRedirect("/clothes-shop/login.jsp");
            return;
        }

        // Para CREAR pedidos: admin y cliente pueden
        // Para MODIFICAR pedidos: solo admin puede
        if (action.equals("Modificar") && !userRole.equals("admin")) {
            response.sendRedirect("/clothes-shop/login.jsp");
            return;
        }

        if (!validate(request)) {
            response.getWriter().println(errors.toString());
            return;
        }

        String customerName = request.getParameter("customerName");
        String customerEmail = request.getParameter("customerEmail");
        String totalAmount = request.getParameter("totalAmount");
        String shippingAddress = request.getParameter("shippingAddress");
        String phoneNumber = request.getParameter("phoneNumber");
        String isCompleted = request.getParameter("isCompleted");

        Database database = new Database();
        try {
            database.connect();
            OrderDao orderDao = new OrderDao(database.getConnection());
            Order order = new Order();
            order.setCustomerName(customerName);
            order.setCustomerEmail(customerEmail);
            order.setTotalAmount(Float.parseFloat(totalAmount));
            order.setOrderDate(new Date(System.currentTimeMillis()));

            if (!action.equals("Registrar")) {
                order.setId(Integer.parseInt(request.getParameter("orderId")));
            }

            order.setShippingAddress(shippingAddress);
            order.setPhoneNumber(phoneNumber);
            order.setCompleted(isCompleted != null && isCompleted.equals("true"));

            boolean done = false;
            if (action.equals("Registrar")) {
                done = orderDao.add(order);
            } else {
                done = orderDao.modify(order);
            }

            if (done) {
                response.sendRedirect("orders");
            } else {
                response.getWriter().print("No se ha podido guardar el pedido");
            }
        } catch (SQLException sqle) {
            response.getWriter().println("No se ha podido conectar con la base de datos");
            sqle.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            response.getWriter().println("No se ha podido cargar el driver de la base de datos");
            cnfe.printStackTrace();
        } catch (IOException ioe) {
            response.getWriter().println("Error no esperado: " + ioe.getMessage());
            ioe.printStackTrace();
        } catch (Exception e) {
            response.getWriter().println("Error: " + e.getMessage());
            e.printStackTrace();
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

    private boolean validate(HttpServletRequest request) {
        errors = new ArrayList<>();
        if (request.getParameter("customerName").isEmpty()) {
            errors.add("El nombre del cliente es un campo obligatorio");
        }
        if (request.getParameter("customerEmail").isEmpty()) {
            errors.add("El email es un campo obligatorio");
        }
        if ((request.getParameter("totalAmount").isEmpty()) || (!request.getParameter("totalAmount").matches("[0-9]+(\\.[0-9]{1,2})?"))) {
            errors.add("El total es un campo numérico");
        }

        return errors.isEmpty();
    }
}