package dao;

import java.sql.*;
import java.util.Optional;
import model.User;
public class UserDAO {
    private Connection conn;

    public UserDAO() throws SQLException {
        conn = DriverManager.getConnection("jdbc:sqlite:users.db");
        createTable();
    }

    private void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS users (username TEXT PRIMARY KEY, passwordHash TEXT, refreshToken TEXT)";
        conn.createStatement().execute(sql);
    }

    public void insertUser(String username, String passwordHash) throws SQLException {
        String sql = "INSERT OR IGNORE INTO users(username, passwordHash) VALUES(?,?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, passwordHash);
        ps.executeUpdate();
    }

    public Optional<User> getUser(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            User user = new User();
            user.username = rs.getString("username");
            user.passwordHash = rs.getString("passwordHash");
            user.refreshToken = rs.getString("refreshToken");
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public void updateRefreshToken(String username, String token) throws SQLException {
        String sql = "UPDATE users SET refreshToken=? WHERE username=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, token);
        ps.setString(2, username);
        ps.executeUpdate();
    }
}