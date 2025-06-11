package DAO;

import exception.UserNotFoundException;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private Connection connection;

    public UserDao(Connection connection) {
        this.connection = connection;
    }

    public String loginUser(String username, String password) throws SQLException, UserNotFoundException {
        String sql = "SELECT role FROM users WHERE username = ? AND password = SHA1(?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, username);
        statement.setString(2, password);
        ResultSet result = statement.executeQuery();
        if (!result.next()) {
            throw new UserNotFoundException();
        }

        return result.getString("role");
    }

    public boolean userExists(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, username);
        ResultSet result = statement.executeQuery();
        result.next();

        int count = result.getInt(1);
        statement.close();

        return count > 0;
    }

    public boolean registerUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, email, password, role) VALUES (?, ?, SHA1(?), ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getEmail());
        statement.setString(3, user.getPassword());
        statement.setString(4, user.getRole());

        int affectedRows = statement.executeUpdate();
        statement.close();

        return affectedRows > 0;
    }

    public User getUserByUsername(String username) throws SQLException, UserNotFoundException {
        String sql = "SELECT id, username, email, role FROM users WHERE username = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, username);
        ResultSet result = statement.executeQuery();

        if (!result.next()) {
            throw new UserNotFoundException();
        }

        User user = new User();
        user.setId(result.getInt("id"));
        user.setUsername(result.getString("username"));
        user.setEmail(result.getString("email"));
        user.setRole(result.getString("role"));

        statement.close();
        return user;
    }

    public boolean updateProfile(String currentUsername, String newUsername, String email) throws SQLException {
        String sql = "UPDATE users SET username = ?, email = ? WHERE username = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, newUsername);
        statement.setString(2, email);
        statement.setString(3, currentUsername);

        int affectedRows = statement.executeUpdate();
        statement.close();

        return affectedRows > 0;
    }

    public boolean changePassword(String username, String newPassword) throws SQLException {
        String sql = "UPDATE users SET password = SHA1(?) WHERE username = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, newPassword);
        statement.setString(2, username);

        int affectedRows = statement.executeUpdate();
        statement.close();

        return affectedRows > 0;
    }
}

