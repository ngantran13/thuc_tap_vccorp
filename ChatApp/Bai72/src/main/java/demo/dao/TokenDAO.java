package demo.dao;

import demo.model.Token;

public interface TokenDAO {
    boolean update(Token refresh, Token access, int userId);
    Token getAccess(int userId);
    Token getRefresh(int userId);
}
