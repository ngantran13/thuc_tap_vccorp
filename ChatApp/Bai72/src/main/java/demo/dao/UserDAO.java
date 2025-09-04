package demo.dao;

import demo.model.User;

public interface UserDAO {
    User getUser(String username, String password);
    User getUserByUsername(String username);
    User getDetailUser(int userId);
    boolean updateToken(String accessToken, String refreshToken, int userId);
    boolean updateStatus(int userId, int active);
}
