package DAO;

import exception.OrderNotFoundException;
import model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.Constant.PAGE_SIZE;

public class OrderDao {

    private Connection connection;

    public OrderDao(Connection connection) {
        this.connection = connection;
    }

    public boolean add(Order order) throws SQLException {
        String sql = "INSERT INTO orders (customer_name, customer_email, total_amount, order_date, " +
                "is_completed, shipping_address, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = null;

        statement = connection.prepareStatement(sql);
        statement.setString(1, order.getCustomerName());
        statement.setString(2, order.getCustomerEmail());
        statement.setFloat(3, order.getTotalAmount());
        statement.setDate(4, order.getOrderDate());
        statement.setBoolean(5, order.isCompleted());
        statement.setString(6, order.getShippingAddress());
        statement.setString(7, order.getPhoneNumber());

        int affectedRows = statement.executeUpdate();

        return affectedRows != 0;
    }

    public int getCount(String search) throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders WHERE customer_name LIKE ? OR customer_email LIKE ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, "%" + search + "%");
        statement.setString(2, "%" + search + "%");
        ResultSet result = statement.executeQuery();
        result.next();
        return result.getInt(1);
    }

    public ArrayList<Order> getAll(int page) throws SQLException {
        String sql = "SELECT * FROM orders ORDER BY order_date DESC LIMIT ?, ?";
        return launchQuery(sql, page);
    }

    public ArrayList<Order> getAll(int page, String search) throws SQLException {
        if (search == null || search.isEmpty()) {
            return getAll(page);
        }

        String sql = "SELECT * FROM orders WHERE customer_name LIKE ? OR customer_email LIKE ? ORDER BY order_date DESC LIMIT ?, ?";
        return launchQuery(sql, page, search);
    }

    public ArrayList<Order> searchAdvanced(int page, String search, String status, String minAmount, String maxAmount) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM orders WHERE 1=1");
        ArrayList<Object> parameters = new ArrayList<>();

        if (search != null && !search.isEmpty()) {
            sql.append(" AND (customer_name LIKE ? OR customer_email LIKE ?)");
            parameters.add("%" + search + "%");
            parameters.add("%" + search + "%");
        }

        if (status != null && !status.isEmpty()) {
            if (status.equals("completed")) {
                sql.append(" AND is_completed = true");
            } else if (status.equals("pending")) {
                sql.append(" AND is_completed = false");
            }
        }

        if (minAmount != null && !minAmount.isEmpty()) {
            sql.append(" AND total_amount >= ?");
            parameters.add(Float.parseFloat(minAmount));
        }

        if (maxAmount != null && !maxAmount.isEmpty()) {
            sql.append(" AND total_amount <= ?");
            parameters.add(Float.parseFloat(maxAmount));
        }

        sql.append(" ORDER BY order_date DESC LIMIT ?, ?");
        parameters.add(page * PAGE_SIZE);
        parameters.add(PAGE_SIZE);

        PreparedStatement statement = connection.prepareStatement(sql.toString());

        for (int i = 0; i < parameters.size(); i++) {
            Object param = parameters.get(i);
            if (param instanceof String) {
                statement.setString(i + 1, (String) param);
            } else if (param instanceof Integer) {
                statement.setInt(i + 1, (Integer) param);
            } else if (param instanceof Float) {
                statement.setFloat(i + 1, (Float) param);
            }
        }

        ResultSet result = statement.executeQuery();
        ArrayList<Order> orderList = new ArrayList<>();
        while (result.next()) {
            Order order = new Order();
            order.setId(result.getInt("id"));
            order.setCustomerName(result.getString("customer_name"));
            order.setCustomerEmail(result.getString("customer_email"));
            order.setTotalAmount(result.getFloat("total_amount"));
            order.setOrderDate(result.getDate("order_date"));
            order.setCompleted(result.getBoolean("is_completed"));
            order.setShippingAddress(result.getString("shipping_address"));
            order.setPhoneNumber(result.getString("phone_number"));
            orderList.add(order);
        }

        statement.close();
        return orderList;
    }

    private ArrayList<Order> launchQuery(String query, int page, String ...search) throws SQLException {
        PreparedStatement statement = null;
        ResultSet result = null;

        statement = connection.prepareStatement(query);
        if (search.length > 0) {
            statement.setString(1, "%" + search[0] + "%");
            statement.setString(2, "%" + search[0] + "%");
            statement.setInt(3, page * PAGE_SIZE);
            statement.setInt(4, PAGE_SIZE);
        } else {
            statement.setInt(1, page * PAGE_SIZE);
            statement.setInt(2, PAGE_SIZE);
        }
        result = statement.executeQuery();
        ArrayList<Order> orderList = new ArrayList<>();
        while (result.next()) {
            Order order = new Order();
            order.setId(result.getInt("id"));
            order.setCustomerName(result.getString("customer_name"));
            order.setCustomerEmail(result.getString("customer_email"));
            order.setTotalAmount(result.getFloat("total_amount"));
            order.setOrderDate(result.getDate("order_date"));
            order.setCompleted(result.getBoolean("is_completed"));
            order.setShippingAddress(result.getString("shipping_address"));
            order.setPhoneNumber(result.getString("phone_number"));
            orderList.add(order);
        }

        statement.close();

        return orderList;
    }

    public Order get(int id) throws SQLException, OrderNotFoundException {
        String sql = "SELECT * FROM orders WHERE id = ?";
        PreparedStatement statement = null;
        ResultSet result = null;

        statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        result = statement.executeQuery();
        if (!result.next()) {
            throw new OrderNotFoundException();
        }

        Order order = new Order();
        order.setId(result.getInt("id"));
        order.setCustomerName(result.getString("customer_name"));
        order.setCustomerEmail(result.getString("customer_email"));
        order.setTotalAmount(result.getFloat("total_amount"));
        order.setOrderDate(result.getDate("order_date"));
        order.setCompleted(result.getBoolean("is_completed"));
        order.setShippingAddress(result.getString("shipping_address"));
        order.setPhoneNumber(result.getString("phone_number"));

        statement.close();

        return order;
    }

    public ArrayList<Order> search(String searchTerm) {

        return null;
    }

    public boolean modify(Order order) throws SQLException{
        String sql = "UPDATE orders SET customer_name = ?, customer_email = ?, total_amount = ?, " +
                "order_date = ?, is_completed = ?, shipping_address = ?, phone_number = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, order.getCustomerName());
        statement.setString(2, order.getCustomerEmail());
        statement.setFloat(3, order.getTotalAmount());
        statement.setDate(4, order.getOrderDate());
        statement.setBoolean(5, order.isCompleted());
        statement.setString(6, order.getShippingAddress());
        statement.setString(7, order.getPhoneNumber());
        statement.setInt(8, order.getId());
        int affectedRows = statement.executeUpdate();

        return affectedRows != 0;
    }

    public boolean delete(int orderId) throws SQLException {
        String sql = "DELETE FROM orders WHERE id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, orderId);
        int affectedRows = statement.executeUpdate();

        return affectedRows != 0;
    }
}