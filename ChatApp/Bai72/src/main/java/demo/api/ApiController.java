package demo.api;

import demo.dao.MessageDAO;
import demo.dao.RelationshipDAO;
import demo.dao.TokenDAO;
import demo.dao.UserDAO;
import demo.model.Message;
import demo.model.Token;
import demo.model.User;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import demo.security.JwtUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
public class ApiController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RelationshipDAO relationshipDAO;
    @Autowired
    private TokenDAO tokenDAO;

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    // API đăng nhập – kiểm tra user từ CSDL, trả về JWT nếu hợp lệ
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        User user = userDAO.getUser(username, password);
        if (user == null) {
            logger.warn("Đăng nhập thất bại với user: {}", username);
            return ResponseEntity.status(401).body("Sai tên đăng nhập hoặc mật khẩu");
        }
        long now=System.currentTimeMillis();
        String accessToken = jwtUtil.generateToken(user.getIdUser(), now+5*60*1000);
        String refreshToken = jwtUtil.generateToken(user.getIdUser(), now+10*60*1000);
        Token refresh =new Token(user.getIdUser(), refreshToken,"refresh",now+10*60*1000);
        Token access =new Token(user.getIdUser(), accessToken,"access",now+5*60*1000);

        tokenDAO.update(refresh,access,user.getIdUser());
        userDAO.updateToken(accessToken,refreshToken, user.getIdUser());

        System.out.println(accessToken);
        System.out.println(userDAO.getDetailUser(user.getIdUser()).getAccessToken());
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken);

        logger.info("Đăng nhập thành công - ID: {}", user.getIdUser());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/friends")
    public ResponseEntity<?> userInfo(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        String token = (String) request.getAttribute("token");
        int userId = Integer.parseInt(claims.getSubject());
        User u = userDAO.getDetailUser(userId);

        // So sánh có kiểm tra null và trim
        if (u.getAccessToken() != null && token != null && u.getAccessToken().trim().equals(token.trim())) {
            List<User> friends = relationshipDAO.getFriendsOfUser(u);
            List<User> friendsOnl=new ArrayList<>();
            for(User user:friends){
                if(user.getActive()==1) friendsOnl.add(user);
            }

            return ResponseEntity.ok(friends);
        } else {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Access token không đúng");
        }
    }

    @Autowired
    private MessageDAO messageDAO;

    @PostMapping("/send-message")
    public ResponseEntity<?> sendMessage(HttpServletRequest request, @RequestParam("username") String receiverUsername, @RequestParam(value = "text", required = false) String text, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        Claims claims = (Claims) request.getAttribute("claims");
        String token = (String) request.getAttribute("token");
        int senderId = Integer.parseInt(claims.getSubject());
        User sender = userDAO.getDetailUser(senderId);
        User receiver = userDAO.getUserByUsername(receiverUsername);

        logger.info("User {} gửi tin nhắn đến {}", sender.getUsername(), receiverUsername);

        // So sánh có kiểm tra null và trim
        if (sender.getAccessToken() != null && token != null && sender.getAccessToken().trim().equals(token.trim())) {
            if (receiver == null) {
                logger.warn("Người nhận {} không tồn tại", receiverUsername);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Người nhận không tồn tại");
            }

            Message msg = new Message();
            msg.setSender(sender.getIdUser());
            msg.setReceiver(receiver.getIdUser());
            msg.setTime(new Date());
            msg.setCheckRead(false);

            if (text != null) {
                msg.setMessage(text);
                msg.setTheLoai("text");
            } else if (file != null) {
                String originalName = file.getOriginalFilename();
                String savePath = "E:\\Bai72\\src\\main\\java\\demo\\storage\\" + System.currentTimeMillis() + "_" + originalName;
                File dest = new File(savePath);
                file.transferTo(dest);

                msg.setMessage(savePath);
                msg.setTheLoai("file");
            } else {
                return ResponseEntity.badRequest().body("Không có nội dung để gửi");
            }

            if (relationshipDAO.checkRelationship(sender, receiver) && receiver.getActive()==1) {
                // Người nhận đang online
                logger.info("Gửi tin nhắn thành công tới {} đang online", receiverUsername);
                messageDAO.saveMessage(msg);
                return ResponseEntity.ok(1);
            } else if(relationshipDAO.checkRelationship(sender, receiver) && receiver.getActive()==0){
                // Người nhận offline => lưu vào hàng chờ
                logger.info("Gửi tin nhắn thành công tới {} đang offline", receiverUsername);
                messageDAO.saveMessage(msg);
                return ResponseEntity.ok(2);
            }else{
                // Kiểm tra người gửi có nằm trong danh sách bạn bè người nhận
                boolean isFriend = relationshipDAO.checkRelationship(sender, receiver);
                if (!isFriend) {
                    logger.info("Gửi tin nhắn thất bại tới {} vì không phải bạn bè", receiverUsername);
                    return ResponseEntity.ok(3);  // Không gửi tin nhắn và không lưu
                }
            }
        }
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Access token không đúng");

    }
    @GetMapping("/get-messages")
    public ResponseEntity<?> getMessages(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        String token = (String) request.getAttribute("token");
        int userId = Integer.parseInt(claims.getSubject());
        User u = userDAO.getDetailUser(userId);

        // So sánh có kiểm tra null và trim
        if (u.getAccessToken() != null && token != null && u.getAccessToken().trim().equals(token.trim())) {
            long start = System.currentTimeMillis();
            long timeout = 10000; // 10s
            List<Message> messages;

            logger.info("User {} đang chờ tin nhắn...", userId);
            while (System.currentTimeMillis() - start < timeout) {
                messages = messageDAO.getPendingMessages(userId);
                if (!messages.isEmpty()) {
                    List<Map<String, String>> response = new ArrayList<>();
                    for (Message msg : messages) {
                        Map<String, String> item = new HashMap<>();
                        item.put("time", msg.getTime().toString());
                        item.put("sender", userDAO.getDetailUser(msg.getSender()).getUsername());
                        if ("file".equals(msg.getTheLoai())) {
                            String fileName = new File(msg.getMessage()).getName();
                            item.put("message", "/get-file?name=" + fileName);
                        } else {
                            item.put("message", msg.getMessage());
                        }
                        response.add(item);
                    }
                    messageDAO.removePendingMessages(userId);
                    logger.info("Đã gửi {} tin nhắn chờ cho user {}", response.size(), userId);
                    return ResponseEntity.ok(response);
                }
            }

            return ResponseEntity.ok(new ArrayList<>()); // Rỗng nếu không có gì
        } else {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Access token không đúng");
        }


    }
    @GetMapping("/get-file")
    public ResponseEntity<?> getFile(HttpServletRequest request, @RequestParam("name") String fileName) {
        Claims claims = (Claims) request.getAttribute("claims");
        String token = (String) request.getAttribute("token");
        int userId = Integer.parseInt(claims.getSubject());
        User u = userDAO.getDetailUser(userId);
        logger.info("User {} yêu cầu tải file {}", userId, fileName);

        File file = new File("E:\\Bai72\\src\\main\\java\\demo\\storage\\" + fileName);
        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File không tồn tại");
        }
        // So sánh có kiểm tra null và trim
        if (u.getAccessToken() != null && token != null && u.getAccessToken().trim().equals(token.trim())) {
            boolean hasAccess = false;
            Message message=messageDAO.getMessage(userId,"E:\\Bai72\\src\\main\\java\\demo\\storage\\" + fileName);
            if(message!=null) hasAccess=true;
            // Nếu không có quyền truy cập file
            if (!hasAccess) {
                logger.warn("Truy cập bị từ chối với file {} từ user {}", fileName, userId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền truy cập file này");
            }
            logger.info("Truy cập file thành công với file {} từ user {}", fileName, userId);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .contentLength(file.length())
                    .body(new org.springframework.core.io.FileSystemResource(file));
        } else {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body("Access token không đúng");
        }
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refresh(HttpServletRequest request) {
        try {
            Claims claims = (Claims) request.getAttribute("claims");
            logger.info("Yêu cầu refresh token từ user ID: {}", claims.getSubject());
            User user = userDAO.getDetailUser(Integer.parseInt(claims.getSubject()));
            if (user == null) throw new Exception();

            logger.info("Tạo mới access & refresh token cho user ID: {}", user.getIdUser());
            long now=System.currentTimeMillis();
            String accessToken = jwtUtil.generateToken(user.getIdUser(), now+5*60*1000);
            String newRefreshToken = jwtUtil.generateToken(user.getIdUser(), now+10*60*1000);
            Token refresh =new Token(user.getIdUser(), newRefreshToken,"refresh",now+10*60*1000);
            Token access =new Token(user.getIdUser(), accessToken,"access",now+5*60*1000);

            tokenDAO.update(refresh,access,user.getIdUser());
            userDAO.updateToken(accessToken,newRefreshToken, user.getIdUser());

            Map<String, String> response = new HashMap<>();
            response.put("accessToken", accessToken);
            response.put("refreshToken", newRefreshToken);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.warn("Refresh token hết hạn hoặc không hợp lệ");
            return ResponseEntity.status(403).body("Refresh token không hợp lệ hoặc đã hết hạn. Đăng ập lại");
        }
    }
}
