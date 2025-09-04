package demo.model;

public class Token {
    private int id;
    private int userId;
    private String token;
    private String theloai;
    private long dateStart;
    private long dateLast;

    public Token() {
    }

    public Token(int userId, String token, String theloai, long dateLast) {
        this.userId=userId;
        this.token = token;
        this.theloai = theloai;
        this.dateStart = System.currentTimeMillis();
        this.dateLast = dateLast;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTheloai() {
        return theloai;
    }

    public void setTheloai(String theloai) {
        this.theloai = theloai;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getDateStart() {
        return dateStart;
    }

    public void setDateStart(long dateStart) {
        this.dateStart = dateStart;
    }

    public long getDateLast() {
        return dateLast;
    }

    public void setDateLast(long dateLast) {
        this.dateLast = dateLast;
    }
}
