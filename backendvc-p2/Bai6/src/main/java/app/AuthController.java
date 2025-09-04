package app;

import static spark.Spark.*;
import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.gson.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import dao.UserDAO;
import io.jsonwebtoken.ExpiredJwtException;
import model.User;

public class AuthController {
    public static void init(UserDAO dao) {
        Gson gson = new Gson();

        // Đăng nhập
        post("/login", (req, res) -> {
            Map<String, String> data = gson.fromJson(req.body(), Map.class);
            String username = data.get("username");
            String password = data.get("password");

            Optional<User> userOpt = dao.getUser(username);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                var result = BCrypt.verifyer().verify(password.toCharArray(), user.passwordHash);
                if (result.verified) {
                    String accessToken = JwtUtil.generateToken(username, 5000);   // 5 giây
                    String refreshToken = JwtUtil.generateToken(username, 60000); // 1 phút
                    dao.updateRefreshToken(username, refreshToken);

                    Map<String, String> resp = new HashMap<>();
                    resp.put("accessToken", accessToken);
                    resp.put("refreshToken", refreshToken);
                    return gson.toJson(resp);
                }
            }

            res.status(401);
            return "Invalid username or password";
        });

        // Làm mới access token bằng refresh token
        post("/refresh", (req, res) -> {
            Map<String, String> data = gson.fromJson(req.body(), Map.class);
            String refreshToken = data.get("refreshToken");
            try {
                String username = JwtUtil.validateToken(refreshToken);
                Optional<User> userOpt = dao.getUser(username);
                if (userOpt.isPresent() && refreshToken.equals(userOpt.get().refreshToken)) {
                    String newAccess = JwtUtil.generateToken(username, 5000); // 5 giây
                    Map<String, String> resp = new HashMap<>();
                    resp.put("accessToken", newAccess);
                    return gson.toJson(resp);
                }
            } catch (Exception e) {
                res.status(403);
                return "Invalid refresh token";
            }

            res.status(403);
            return "Invalid refresh token or user not found";
        });

        // Gọi /prime, tự xử lý accessToken hết hạn
        get("/prime", (req, res) -> {
            String accessHeader = req.headers("Authorization");
            String refreshHeader = req.headers("X-Refresh-Token");

            if (accessHeader != null && accessHeader.startsWith("Bearer ")) {
                String token = accessHeader.substring(7);
                try {
                    JwtUtil.validateToken(token);
                    int n=Integer.parseInt(req.queryParams("n"));
                    JsonObject json=new JsonObject();
                    JsonArray array=new JsonArray();
                    for (int i=1;i<=n;i++){
                        array.add(i);
                    }
                    json.add("array",array);
                    return json.toString();
                } catch (ExpiredJwtException e) {
                    String expiredUser = e.getClaims().getSubject();

                    // Có refresh token?
                    if (refreshHeader != null) {
                        try {
                            String refreshUser = JwtUtil.validateToken(refreshHeader);

                            if (!expiredUser.equals(refreshUser)) {
                                res.status(403);
                                return "Refresh token không khớp user";
                            }

                            Optional<User> userOpt = dao.getUser(expiredUser);
                            if (userOpt.isPresent() && refreshHeader.equals(userOpt.get().refreshToken)) {
                                // ✅ Tạo access token mới
                                String newAccess = JwtUtil.generateToken(expiredUser, 5000);
                                res.header("New-Access-Token", newAccess);
                                int n=Integer.parseInt(req.queryParams("n"));
                                JsonObject json=new JsonObject();
                                JsonArray array=new JsonArray();
                                for (int i=1;i<=n;i++){
                                    array.add(i);
                                }
                                json.add("array",array);
                                return json.toString();
                            }
                        } catch (Exception ex) {
                            res.status(403);
                            return "Refresh token không hợp lệ";
                        }
                    }

                    res.status(401);
                    return "Access token hết hạn và không có refresh token";
                } catch (Exception e) {
                    res.status(401);
                    return "Access token không hợp lệ";
                }
            }

            res.status(401);
            return "Thiếu access token";
        });
    }
}
