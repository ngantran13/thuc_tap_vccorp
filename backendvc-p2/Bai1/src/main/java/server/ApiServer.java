package server;
import com.google.gson.JsonObject;
import java.util.Random;
import static spark.Spark.*;
public class ApiServer {
    public static void main(String[] args) {
        port(9999);
        get("/data",(req,res) ->{
            res.type("application/json");
            int user=new Random().nextInt(501)+4000;
            JsonObject json=new JsonObject();
            json.addProperty("user",user);
            return json.toString();
        });
    }
}
