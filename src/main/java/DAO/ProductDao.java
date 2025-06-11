package DAO;

import exception.ProductNotFoundException;
import model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.Constant.PAGE_SIZE;

public class ProductDao {

    private Connection connection;

    public ProductDao(Connection connection) {
        this.connection = connection;
    }

    public boolean add(Product product) throws SQLException {
        String sql = "INSERT INTO products (name, description, price, stock_quantity, release_date, image, " +
                "is_available, category_id, brand, size, color) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = null;

        statement = connection.prepareStatement(sql);
        statement.setString(1, product.getName());
        statement.setString(2, product.getDescription());
        statement.setFloat(3, product.getPrice());
        statement.setInt(4, product.getStockQuantity());
        statement.setDate(5, product.getReleaseDate());
        statement.setString(6, product.getImage());
        statement.setBoolean(7, product.isAvailable());
        statement.setInt(8, product.getCategoryId());
        statement.setString(9, product.getBrand());
        statement.setString(10, product.getSize());
        statement.setString(11, product.getColor());

        int affectedRows = statement.executeUpdate();

        return affectedRows != 0;
    }

    public int getCount(String search) throws SQLException {
        String sql = "SELECT COUNT(*) FROM products WHERE name LIKE ? OR description LIKE ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, "%" + search + "%");
        statement.setString(2, "%" + search + "%");
        ResultSet result = statement.executeQuery();
        result.next();
        return result.getInt(1);
    }

    public ArrayList<Product> getAll(int page) throws SQLException {
        String sql = "SELECT * FROM products ORDER BY name LIMIT ?, ?";
        return launchQuery(sql, page);
    }

    public ArrayList<Product> getAll(int page, String search) throws SQLException {
        if (search == null || search.isEmpty()) {
            return getAll(page);
        }

        String sql = "SELECT * FROM products WHERE name LIKE ? OR description LIKE ? ORDER BY name LIMIT ?, ?";
        return launchQuery(sql, page, search);
    }

    private ArrayList<Product> launchQuery(String query, int page, String ...search) throws SQLException {
        PreparedStatement statement = null;
        ResultSet result = null;

        statement = connection.prepareStatement(query);
        if (search.length > 0) {
            // Listado de búsqueda
            statement.setString(1, "%" + search[0] + "%");
            statement.setString(2, "%" + search[0] + "%");
            statement.setInt(3, page * PAGE_SIZE);
            statement.setInt(4, PAGE_SIZE);
        } else {
            // Listado completo (sin búsqueda)
            statement.setInt(1, page * PAGE_SIZE);
            statement.setInt(2, PAGE_SIZE);
        }
        result = statement.executeQuery();
        ArrayList<Product> productList = new ArrayList<>();
        while (result.next()) {
            Product product = new Product();
            product.setId(result.getInt("id"));
            product.setName(result.getString("name"));
            product.setDescription(result.getString("description"));
            product.setPrice(result.getFloat("price"));
            product.setStockQuantity(result.getInt("stock_quantity"));
            product.setReleaseDate(result.getDate("release_date"));
            product.setImage(result.getString("image"));
            product.setAvailable(result.getBoolean("is_available"));
            product.setCategoryId(result.getInt("category_id"));
            product.setBrand(result.getString("brand"));
            product.setSize(result.getString("size"));
            product.setColor(result.getString("color"));

            productList.add(product);
        }

        statement.close();

        return productList;
    }

    public Product get(int id) throws SQLException, ProductNotFoundException {
        String sql = "SELECT * FROM products WHERE id = ?";
        PreparedStatement statement = null;
        ResultSet result = null;

        statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        result = statement.executeQuery();
        if (!result.next()) {
            throw new ProductNotFoundException();
        }

        Product product = new Product();
        product.setId(result.getInt("id"));
        product.setName(result.getString("name"));
        product.setDescription(result.getString("description"));
        product.setPrice(result.getFloat("price"));
        product.setStockQuantity(result.getInt("stock_quantity"));
        product.setReleaseDate(result.getDate("release_date"));
        product.setImage(result.getString("image"));
        product.setAvailable(result.getBoolean("is_available"));
        product.setCategoryId(result.getInt("category_id"));
        product.setBrand(result.getString("brand"));
        product.setSize(result.getString("size"));
        product.setColor(result.getString("color"));

        statement.close();

        return product;
    }

    public ArrayList<Product> searchAdvanced(int page, String search, String categoryId, String minPrice, String maxPrice) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM products WHERE 1=1");
        ArrayList<Object> parameters = new ArrayList<>();

        if (search != null && !search.isEmpty()) {
            sql.append(" AND (name LIKE ? OR description LIKE ?)");
            parameters.add("%" + search + "%");
            parameters.add("%" + search + "%");
        }

        if (categoryId != null && !categoryId.isEmpty()) {
            sql.append(" AND category_id = ?");
            parameters.add(Integer.parseInt(categoryId));
        }

        if (minPrice != null && !minPrice.isEmpty()) {
            sql.append(" AND price >= ?");
            parameters.add(Float.parseFloat(minPrice));
        }

        if (maxPrice != null && !maxPrice.isEmpty()) {
            sql.append(" AND price <= ?");
            parameters.add(Float.parseFloat(maxPrice));
        }

        sql.append(" ORDER BY name LIMIT ?, ?");
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
        ArrayList<Product> productList = new ArrayList<>();
        while (result.next()) {
            Product product = new Product();
            product.setId(result.getInt("id"));
            product.setName(result.getString("name"));
            product.setDescription(result.getString("description"));
            product.setPrice(result.getFloat("price"));
            product.setStockQuantity(result.getInt("stock_quantity"));
            product.setReleaseDate(result.getDate("release_date"));
            product.setImage(result.getString("image"));
            product.setAvailable(result.getBoolean("is_available"));
            product.setCategoryId(result.getInt("category_id"));
            product.setBrand(result.getString("brand"));
            product.setSize(result.getString("size"));
            product.setColor(result.getString("color"));

            productList.add(product);
        }

        statement.close();
        return productList;
    }
    public boolean modify(Product product) throws SQLException{
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock_quantity = ?, " +
                "release_date = ?, is_available = ?, category_id = ?, brand = ?, size = ?, color = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, product.getName());
        statement.setString(2, product.getDescription());
        statement.setFloat(3, product.getPrice());
        statement.setInt(4, product.getStockQuantity());
        statement.setDate(5, product.getReleaseDate());
        statement.setBoolean(6, product.isAvailable());
        statement.setInt(7, product.getCategoryId());
        statement.setString(8, product.getBrand());
        statement.setString(9, product.getSize());
        statement.setString(10, product.getColor());
        statement.setInt(11, product.getId());
        int affectedRows = statement.executeUpdate();

        return affectedRows != 0;
    }

    public boolean delete(int productId) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, productId);
        int affectedRows = statement.executeUpdate();

        return affectedRows != 0;
    }
}