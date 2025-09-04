// JwtUtil.java
package app;

import io.jsonwebtoken.*;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET = "my-secret-key";

    //tạo accesstoken/refreshtoken
    public static String generateToken(String username, long expireMs) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireMs))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    //giải mã token lấy username
    public static String validateToken(String token) throws Exception {
        return Jwts.parser().setSigningKey(SECRET)
                .parseClaimsJws(token).getBody().getSubject();
    }
}
