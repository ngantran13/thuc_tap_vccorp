package demo.dao;

import demo.model.Token;
import org.springframework.stereotype.Repository;

import java.sql.*;
@Repository
public class TokenDAOImpl implements TokenDAO{
    private static final String DB_URL = "jdbc:sqlite:chatapp.db";

    @Override
    public boolean update(Token refresh, Token access, int userId) {
        String sql = """
            UPDATE Token
            SET token = ?, dateStart = ?, dateLast = ?
            WHERE userId = ? AND theloai = ?
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmtRefresh = conn.prepareStatement(sql);
             PreparedStatement pstmtAccess = conn.prepareStatement(sql)) {

            // Cập nhật refresh token
            pstmtRefresh.setString(1, refresh.getToken());
            pstmtRefresh.setLong(2, refresh.getDateStart());
            pstmtRefresh.setLong(3, refresh.getDateLast());
            pstmtRefresh.setInt(4, userId);
            pstmtRefresh.setString(5, "refresh");

            int rows1 = pstmtRefresh.executeUpdate();

            // Cập nhật access token
            pstmtAccess.setString(1, access.getToken());
            pstmtAccess.setLong(2, access.getDateStart());
            pstmtAccess.setLong(3, access.getDateLast());
            pstmtAccess.setInt(4, userId);
            pstmtAccess.setString(5, "access");

            int rows2 = pstmtAccess.executeUpdate();

            return rows1 > 0 && rows2 > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Token getAccess(int userId) {
        return getTokenByType(userId, "access");
    }

    @Override
    public Token getRefresh(int userId) {
        return getTokenByType(userId, "refresh");
    }

    private Token getTokenByType(int userId, String type) {
        String sql = """
            SELECT token FROM Token WHERE userId = ? AND theloai = ?
        """;
        Token token=null;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, type);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                token=new Token();
                token.setId(rs.getInt("id"));
                token.setToken(rs.getString("token"));
                token.setTheloai(rs.getString("theloai"));
                token.setDateLast(rs.getLong("dateLast"));
                token.setUserId(userId);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return token;
    }
}
