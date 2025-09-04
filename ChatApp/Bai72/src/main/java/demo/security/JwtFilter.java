package demo.security;

import demo.dao.TokenDAO;
import demo.dao.UserDAO;
import demo.model.Token;
import demo.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private TokenDAO tokenDAO;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        System.out.println("🔍 JWT Filter intercepting: " + path);

        // Bỏ qua kiểm tra JWT cho /login
        if (path.contains("/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        //Kiểm tra với api /refresh-token
        if(path.contains("/refresh-token")){
            String token=request.getHeader("refreshToken");
            try {
                Claims claims = jwtUtil.extractClaims(token);
                request.setAttribute("token", token);
                request.setAttribute("claims", claims);

                int userId = Integer.parseInt(claims.getSubject());
                User user = userDAO.getDetailUser(userId);

                if (user != null && user.getRefreshToken().equals(token)) {
                    filterChain.doFilter(request, response);
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Refresh token không đúng");
                }

            } catch (ExpiredJwtException e) {
                e.printStackTrace();
                try {
                    // 🟡 Lấy userId từ token đã hết hạn
                    Claims claims = e.getClaims();
                    int userId = Integer.parseInt(claims.getSubject());

                    // 🟢 Cập nhật trạng thái user về offline
                    userDAO.updateStatus(userId,0);

                } catch (Exception ex) {

                }
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Refresh Token đã hết hạn");
            }catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Refresh Token không hợp lệ");
            }
        }

        //Kiểm tra với api có access token
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Claims claims = jwtUtil.extractClaims(token);
                request.setAttribute("token", token);
                request.setAttribute("claims", claims);

                int userId = Integer.parseInt(claims.getSubject());
                User user = userDAO.getDetailUser(userId);

                //multipart/form-data có thể kích hoạt chuỗi filter khác, hoặc bị các SecurityConfigurer xử lý riêng → nếu không có Authentication, có thể bị từ chối
                if (user != null && user.getAccessToken().equals(token)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    filterChain.doFilter(request, response);
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Access token không đúng");
                }

            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Access Token không hợp lệ hoặc đã hết hạn");
            }
            return;
        }

    }
}
