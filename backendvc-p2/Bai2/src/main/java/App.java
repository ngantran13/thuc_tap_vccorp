import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.sql.*;
//1 bang url can crawl
public class App {
    public static void main(String[] args) throws Exception {
        String url = "https://www.dienmayxanh.com/quat-dieu-hoa/quat-dieu-hoa-delites-rpd-30";
        Document doc = Jsoup.connect(url).get();
        String title = doc.title();

        Connection conn = DriverManager.getConnection("jdbc:sqlite:news.db");
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS news (title TEXT)");

        PreparedStatement ps = conn.prepareStatement("INSERT INTO news (title) VALUES (?)");
        ps.setString(1, title);
        ps.executeUpdate();

        System.out.println("Saved: " + title);
        conn.close();
    }
}
