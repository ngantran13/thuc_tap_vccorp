package demo.dao;

import demo.model.Message;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MessageDAOImpl implements MessageDAO{
    private Map<Integer, List<Message>> pendingMessages = new HashMap<>();
    private static final String DB_URL = "jdbc:sqlite:chatapp.db";
    @Override
    public void saveMessage(Message msg) {
        pendingMessages.computeIfAbsent(msg.getReceiver(), k -> new ArrayList<>()).add(msg);
        String sql = "INSERT INTO Message (sender, receiver, time, message, theLoai, checkRead) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, msg.getSender());
            stmt.setInt(2, msg.getReceiver());
            stmt.setTimestamp(3, new java.sql.Timestamp(msg.getTime().getTime()));
            stmt.setString(4, msg.getMessage());
            stmt.setString(5, msg.getTheLoai());
            stmt.setBoolean(6, msg.isCheckRead());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Bạn nên xử lý/log lỗi thật sự trong hệ thống thật
        }
    }

    @Override
    public List<Message> getPendingMessages(int receiverId) {
        updateCheckRead(receiverId);
        return pendingMessages.getOrDefault(receiverId, new ArrayList<>());
    }

    @Override
    public void removePendingMessages(int receiverId) {
        pendingMessages.remove(receiverId);
    }

    @Override
    public Message getMessage(int receiverId, String messageContent) {
        String sql = "SELECT * " +
                "FROM Message WHERE receiver = ? AND message = ? LIMIT 1";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, receiverId);
            stmt.setString(2, messageContent);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Message msg = new Message();
                msg.setSender(rs.getInt("sender"));
                msg.setReceiver(rs.getInt("receiver"));
                msg.setTime(rs.getTimestamp("time"));
                msg.setMessage(rs.getString("message"));
                msg.setTheLoai(rs.getString("theLoai"));
                msg.setCheckRead(rs.getBoolean("checkRead"));
                return msg;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateCheckRead(int receiverId) {
        String sql = "UPDATE Message SET checkRead = ? WHERE receiver = ? AND checkRead = false";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, true);  // Đặt check_read = true
            stmt.setInt(2, receiverId);

            int rows = stmt.executeUpdate();
            System.out.println("Số dòng đã cập nhật: " + rows);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
