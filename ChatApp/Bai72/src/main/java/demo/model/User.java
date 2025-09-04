package demo.model;

import java.util.Objects;

public class User {
    private int idUser;
    private String username;
    private String password;
    private String accessToken;
    private String refreshToken;
    private int active;

    public User(int idUser, String username, String password, String accessToken, String refreshToken) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.active=0;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return idUser == user.idUser && active == user.active && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(accessToken, user.accessToken) && Objects.equals(refreshToken, user.refreshToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, username, password, accessToken, refreshToken, active);
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", active=" + active +
                '}';
    }
}
