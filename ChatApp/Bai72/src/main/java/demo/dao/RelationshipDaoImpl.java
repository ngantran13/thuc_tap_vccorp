package demo.dao;

import demo.model.User;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Repository
public class RelationshipDaoImpl implements RelationshipDAO{
    private static final String DB_URL = "jdbc:sqlite:chatapp.db";
    @Override
    public List<User> getFriendsOfUser(User u) {
        List<User> friends = new ArrayList<>();

        String sql = """
            SELECT u.* FROM User u
            JOIN Relationship r
            ON (u.idUser = r.user1 AND r.user2 = ?)
            OR  (u.idUser = r.user2 AND r.user1 = ?)
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, u.getIdUser());
            pstmt.setInt(2, u.getIdUser());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                if (rs.getInt("idUser") != u.getIdUser()) { // tránh chính nó
                    User friend = new User(
                            rs.getInt("idUser"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getObject("accessToken") != null ? rs.getString("accessToken") : null,
                            rs.getObject("refreshToken") != null ? rs.getString("refreshToken") : null
                    );
                    friend.setActive(rs.getInt("active"));
                    friends.add(friend);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return friends;
    }

    @Override
    public boolean checkRelationship(User sender, User receiver) {
        String sql = """
            SELECT 1 FROM Relationship
            WHERE (user1 = ? AND user2 = ?)
               OR (user1 = ? AND user2 = ?)
            LIMIT 1
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, sender.getIdUser());
            pstmt.setInt(2, receiver.getIdUser());
            pstmt.setInt(3, receiver.getIdUser());
            pstmt.setInt(4, sender.getIdUser());

            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // tồn tại mối quan hệ thì trả về true

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
