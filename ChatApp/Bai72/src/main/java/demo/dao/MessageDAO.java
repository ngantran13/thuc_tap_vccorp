package demo.dao;

import demo.model.Message;

import java.util.List;

public interface MessageDAO {
    void saveMessage(Message msg);
    List<Message> getPendingMessages(int receiverId);
    void removePendingMessages(int receiverId);

    Message getMessage(int receiverId, String message);
}
