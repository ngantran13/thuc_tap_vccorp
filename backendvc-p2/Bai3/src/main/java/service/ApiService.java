package service;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static  spark.Spark.*;
public class ApiService {
    static CacheTTL<Integer,Boolean> cacheTTL=new CacheTTL<>(10,30); //10s truy cập, 30s sống
    public static boolean nguyenTo(int k){
        Boolean cached=cacheTTL.get(k);
        if(cached!=null) return cached;
        if(k<2) {
            cacheTTL.put(k,FALSE);
            return false;
        }
        for(int i=2;i<Math.sqrt(k);i++){
            if(k%i==0) {
                cacheTTL.put(k,FALSE);
                return false;
            }
        }
        cacheTTL.put(k,TRUE);
        return true;
    }
    public static void main(String[] args) {
        port(9999);
        get("/prime", ((request, response) -> {
            int n=Integer.parseInt(request.queryParams("n"));
            JsonObject json=new JsonObject();
            JsonArray array=new JsonArray();
            for (int i=1;i<=n;i++){
                nguyenTo(i);
                array.add(i);
            }
            json.add("array",array);
            return json.toString();
        }));
    }
}
