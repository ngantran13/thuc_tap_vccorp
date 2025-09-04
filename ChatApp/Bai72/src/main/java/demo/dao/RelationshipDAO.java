package demo.dao;

import demo.model.User;

import java.util.List;

public interface RelationshipDAO {
    List<User> getFriendsOfUser(User u);
    boolean checkRelationship(User sender, User receiver);
}
