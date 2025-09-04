// Main.java
package app;

import static spark.Spark.*;

import java.sql.SQLException;
import at.favre.lib.crypto.bcrypt.BCrypt;
import dao.UserDAO;

public class Main {
    public static void main(String[] args) throws SQLException {
        port(8080);
        secure("src/main/resources/keystore.jks", "123456", null, null);

        UserDAO dao = new UserDAO();

        // tạo user ban đầu
        dao.insertUser("user1", BCrypt.withDefaults().hashToString(12, "123456".toCharArray()));
        dao.insertUser("user2", BCrypt.withDefaults().hashToString(12, "654321".toCharArray()));

        AuthController.init(dao);
    }
}
