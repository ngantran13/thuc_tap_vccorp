package app;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.concurrent.TimeUnit;

import static spark.Spark.*;
//heap size max khi dat max thì chuong trinh ngung chạy, tranh gay chiem nhieu/het bo nho gay treo may
public class ApiService {
    static LoadingCache<Integer,Boolean> cache= CacheBuilder.newBuilder()
            .expireAfterWrite(20, TimeUnit.SECONDS)
            .expireAfterAccess(10,TimeUnit.SECONDS)
            .build(new CacheLoader<Integer, Boolean>() {
                @Override
                public Boolean load(Integer key) throws Exception {
                    return nguyenTo(key);
                }
            });
    public static boolean nguyenTo(int k){
        if(k<2) {
            return false;
        }
        for(int i=2;i<Math.sqrt(k);i++){
            if(k%i==0) {
                return false;
            }
        }
        return true;
    }
    /* Chinh sua hop nhat giua server & client*/
    public static void main(String[] args) {
        port(9999);
        get("/prime", ((request, response) -> {
            int n=Integer.parseInt(request.queryParams("n"));
            JsonObject json=new JsonObject();
            JsonArray array=new JsonArray();
            for (int i=1;i<=n;i++){
                array.add(i);
            }
            json.add("array",array);
            return json.toString();
        }));
    }
}
