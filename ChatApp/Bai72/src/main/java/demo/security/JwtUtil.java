package demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    //tạo accesstoken/refreshtoken
    public String generateToken(int userId,long dateLast) {
        return Jwts.builder()
                .setSubject(""+userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(dateLast))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
    // Trích xuất thông tin từ token (nếu hợp lệ)
    public Claims extractClaims(String token) throws Exception {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw new Exception("Token không hợp lệ hoặc đã hết hạn");
        }
    }
}
