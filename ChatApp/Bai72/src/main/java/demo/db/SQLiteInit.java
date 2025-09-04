package demo.db;

import java.sql.*;

public class SQLiteInit {

    public static void main(String[] args) {
        String url = "jdbc:sqlite:chatapp.db";  // File SQLite sẽ được tạo nếu chưa tồn tại

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("Kết nối thành công đến SQLite");

                Statement stmt = conn.createStatement();

                // Tạo bảng User
                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS User (
                        idUser INTEGER PRIMARY KEY,
                        username TEXT NOT NULL,
                        password TEXT NOT NULL,
                        accessToken TEXT,
                        refreshToken TEXT,
                        active INTEGER
                    );
                """);

                // Tạo bảng Token
                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Token (
                        id INTEGER PRIMARY KEY,
                        userId INTEGER NOT NULL,
                        token TEXT,
                        theloai TEXT NOT NULL,
                        dateStart INTEGER,
                        dateLast INTEGER
                    );
                """);

                // Tạo bảng Relationship
                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Relationship (
                        user1 INTEGER NOT NULL,
                        user2 INTEGER NOT NULL
                    );
                """);

                // Tạo bảng Message
                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS Message (
                        sender INTEGER NOT NULL,
                        receiver INTEGER NOT NULL,
                        time DATETIME NOT NULL,
                        message TEXT NOT NULL,
                        theLoai TEXT NOT NULL,
                        checkRead BOOLEAN NOT NULL
                    );
                """);

                stmt.executeUpdate("DELETE FROM User;");
                stmt.executeUpdate("DELETE FROM Relationship;");

                // Thêm dữ liệu mẫu cho bảng User
                PreparedStatement pstmt = conn.prepareStatement("""
                    INSERT INTO User (idUser, username, password, accessToken, refreshToken, active)
                    VALUES (?, ?, ?, ?, ?, ?)
                """);

                PreparedStatement tokenStmt = conn.prepareStatement("""
                    INSERT INTO Token (userId, token, theloai, dateStart, dateLast)
                    VALUES (?, ?, ?, ?, ?)
                """);

                Object[][] users = {
                        {1, "alice", "password123", null, null,0},
                        {2, "bob", "securepass", null, null,0},
                        {3, "charlie", "charliepwd", null, null,0}
                };

                for (Object[] user : users) {
                    int id = (int) user[0];

                    pstmt.setInt(1, id);
                    pstmt.setString(2, (String) user[1]);
                    pstmt.setString(3, (String) user[2]);

                    if (user[3] == null) pstmt.setNull(4, Types.INTEGER);
                    else pstmt.setInt(4, (int) user[3]);

                    if (user[4] == null) pstmt.setNull(5, Types.INTEGER);
                    else pstmt.setInt(5, (int) user[4]);

                    pstmt.executeUpdate();

                    // Thêm cặp token access & refresh
                    for (String theloai : new String[]{"access", "refresh"}) {
                        tokenStmt.setInt(1, id);                     // userId
                        tokenStmt.setNull(2, Types.VARCHAR);         // token null
                        tokenStmt.setString(3, theloai);             // access or refresh
                        tokenStmt.setNull(4, Types.INTEGER);         // dateStart
                        tokenStmt.setNull(5, Types.INTEGER);         // dateLast
                        tokenStmt.executeUpdate();
                    }
                }

                // Thêm dữ liệu mẫu cho bảng Relationship
                PreparedStatement pstmtRel = conn.prepareStatement("""
                    INSERT INTO Relationship (user1, user2)
                    VALUES (?, ?)
                """);

                Object[][] relationships = {
                        {1, 2},
                        {1, 3}
                };

                for (Object[] rel : relationships) {
                    if (rel[0] == null) pstmtRel.setNull(1, Types.INTEGER);
                    else pstmtRel.setInt(1, (int) rel[0]);

                    if (rel[1] == null) pstmtRel.setNull(2, Types.INTEGER);
                    else pstmtRel.setInt(2, (int) rel[1]);

                    pstmtRel.executeUpdate();
                }

                System.out.println("Đã thêm dữ liệu mẫu cho bảng Relationship.");

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

