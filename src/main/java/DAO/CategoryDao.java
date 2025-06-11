package DAO;

import exception.CategoryNotFoundException;
import model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.Constant.PAGE_SIZE;

public class CategoryDao {

    private Connection connection;

    public CategoryDao(Connection connection) {
        this.connection = connection;
    }

    public boolean add(Category category) throws SQLException {
        String sql = "INSERT INTO categories (name, description, discount_percentage, creation_date, is_active, image) " +
                " VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = null;

        statement = connection.prepareStatement(sql);
        statement.setString(1, category.getName());
        statement.setString(2, category.getDescription());
        statement.setFloat(3, category.getDiscountPercentage());
        statement.setDate(4, category.getCreationDate());
        statement.setBoolean(5, category.isActive());
        statement.setString(6, category.getImage());

        int affectedRows = statement.executeUpdate();

        return affectedRows != 0;
    }

    public int getCount(String search) throws SQLException {
        String sql = "SELECT COUNT(*) FROM categories WHERE name LIKE ? OR description LIKE ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, "%" + search + "%");
        statement.setString(2, "%" + search + "%");
        ResultSet result = statement.executeQuery();
        result.next();
        return result.getInt(1);
    }

    public ArrayList<Category> getAll(int page) throws SQLException {
        String sql = "SELECT * FROM categories ORDER BY name LIMIT ?, ?";
        return launchQuery(sql, page);
    }

    public ArrayList<Category> getAll(int page, String search) throws SQLException {
        if (search == null || search.isEmpty()) {
            return getAll(page);
        }

        String sql = "SELECT * FROM categories WHERE name LIKE ? OR description LIKE ? ORDER BY name LIMIT ?, ?";
        return launchQuery(sql, page, search);
    }

    public ArrayList<Category> searchAdvanced(int page, String search, String status) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM categories WHERE 1=1");
        ArrayList<Object> parameters = new ArrayList<>();

        if (search != null && !search.isEmpty()) {
            sql.append(" AND (name LIKE ? OR description LIKE ?)");
            parameters.add("%" + search + "%");
            parameters.add("%" + search + "%");
        }

        if (status != null && !status.isEmpty()) {
            if (status.equals("active")) {
                sql.append(" AND is_active = true");
            } else if (status.equals("inactive")) {
                sql.append(" AND is_active = false");
            }
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
            }
        }

        ResultSet result = statement.executeQuery();
        ArrayList<Category> categoryList = new ArrayList<>();
        while (result.next()) {
            Category category = new Category();
            category.setId(result.getInt("id"));
            category.setName(result.getString("name"));
            category.setDescription(result.getString("description"));
            category.setDiscountPercentage(result.getFloat("discount_percentage"));
            category.setCreationDate(result.getDate("creation_date"));
            category.setActive(result.getBoolean("is_active"));
            category.setImage(result.getString("image"));
            categoryList.add(category);
        }

        statement.close();
        return categoryList;
    }

    private ArrayList<Category> launchQuery(String query, int page, String ...search) throws SQLException {
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
        ArrayList<Category> categoryList = new ArrayList<>();
        while (result.next()) {
            Category category = new Category();
            category.setId(result.getInt("id"));
            category.setName(result.getString("name"));
            category.setDescription(result.getString("description"));
            category.setDiscountPercentage(result.getFloat("discount_percentage"));
            category.setCreationDate(result.getDate("creation_date"));
            category.setActive(result.getBoolean("is_active"));
            category.setImage(result.getString("image"));
            categoryList.add(category);
        }

        statement.close();

        return categoryList;
    }

    public Category get(int id) throws SQLException, CategoryNotFoundException {
        String sql = "SELECT * FROM categories WHERE id = ?";
        PreparedStatement statement = null;
        ResultSet result = null;

        statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        result = statement.executeQuery();
        if (!result.next()) {
            throw new CategoryNotFoundException();
        }

        Category category = new Category();
        category.setId(result.getInt("id"));
        category.setName(result.getString("name"));
        category.setDescription(result.getString("description"));
        category.setDiscountPercentage(result.getFloat("discount_percentage"));
        category.setCreationDate(result.getDate("creation_date"));
        category.setActive(result.getBoolean("is_active"));
        category.setImage(result.getString("image"));

        statement.close();

        return category;
    }

    public ArrayList<Category> search(String searchTerm) {
        // TODO
        return null;
    }

    public boolean modify(Category category) throws SQLException{
        String sql = "UPDATE categories SET name = ?, description = ?, discount_percentage = ?, " +
                "creation_date = ?, is_active = ?, image = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, category.getName());
        statement.setString(2, category.getDescription());
        statement.setFloat(3, category.getDiscountPercentage());
        statement.setDate(4, category.getCreationDate());
        statement.setBoolean(5, category.isActive());
        statement.setString(6, category.getImage());
        statement.setInt(7, category.getId());
        int affectedRows = statement.executeUpdate();

        return affectedRows != 0;
    }

    public boolean delete(int categoryId) throws SQLException {
        String sql = "DELETE FROM categories WHERE id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, categoryId);
        int affectedRows = statement.executeUpdate();

        return affectedRows != 0;
    }
}