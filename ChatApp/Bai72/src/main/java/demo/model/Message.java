package demo.model;

import java.util.Date;

public class Message {
    private int sender;
    private int receiver;
    private Date time;
    private String message;
    private String theLoai;
    private boolean checkRead;

    public Message() {
    }

    public Message(int sender, int receiver, Date time, String message, String theLoai) {
        this.sender = sender;
        this.receiver = receiver;
        this.time = time;
        this.message = message;
        this.theLoai = theLoai;
        this.checkRead = false;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isCheckRead() {
        return checkRead;
    }

    public void setCheckRead(boolean checkRead) {
        this.checkRead = checkRead;
    }
}
