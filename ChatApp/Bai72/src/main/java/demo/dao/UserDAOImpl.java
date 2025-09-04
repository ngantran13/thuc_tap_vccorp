package demo.dao;

import demo.model.User;
import org.springframework.stereotype.Repository;

import java.sql.*;
@Repository
public class UserDAOImpl implements UserDAO{
    private static final String DB_URL = "jdbc:sqlite:chatapp.db";

    @Override
    public User getUser(String username, String password) {
        String sql = "SELECT * FROM User WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("idUser"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getObject("accessToken") != null ? rs.getString("accessToken") : null,
                        rs.getObject("refreshToken") != null ? rs.getString("refreshToken") : null
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Không tìm thấy
    }

    @Override
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM User WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("idUser"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getObject("accessToken") != null ? rs.getString("accessToken") : null,
                        rs.getObject("refreshToken") != null ? rs.getString("refreshToken") : null
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Không tìm thấy
    }

    @Override
    public User getDetailUser(int userId) {
        String sql = "SELECT * FROM User WHERE idUser = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("idUser"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getObject("accessToken") != null ? rs.getString("accessToken") : null,
                        rs.getObject("refreshToken") != null ? rs.getString("refreshToken") : null
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Không tìm thấy
    }

    @Override
    public boolean updateToken(String accessToken, String refreshToken, int userId) {
        String sql = "UPDATE User SET accessToken = ?, refreshToken = ?, active = ? WHERE idUser = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, accessToken);
            pstmt.setString(2, refreshToken);
            pstmt.setInt(3, 1);
            pstmt.setInt(4, userId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateStatus(int userId, int active) {
        String sql = "UPDATE User SET active = ? WHERE idUser = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, active);
            pstmt.setInt(2, userId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
