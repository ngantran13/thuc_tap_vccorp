package client;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LogClient {
    static Logger logger = LoggerFactory.getLogger(LogClient.class); //loggeer de dung chung cho ca package
    static int oldUser=-1; //user ra cuoi cung
    static long lastLogTime = 0; //thoi gian user ra cuoi
    public static String getJson() throws IOException {
        URL url=new URL("http://localhost:9999/data");
        String user="";
        try{
            HttpURLConnection con=(HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5000); //co 5s de ket noi
            con.setReadTimeout(10); //co 10ms de tra data
            con.setRequestMethod("GET");

            // Dữ liệu từ server được truyền theo dòng byte
            BufferedReader reader=new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line= reader.readLine();
            while(line!=null){
                user+=line;
                line=reader.readLine();
            }
        }catch(Exception e){
            logger.error("timeout");
        }
        return user.substring(user.indexOf(":")+1,user.length()-1);
    }
    public static void main(String[] args){
        ScheduledExecutorService executorService= Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(()->{
            int getUser;
            try {
                getUser=Integer.parseInt(getJson());
                System.out.println(getUser);
                long now=System.currentTimeMillis();
                if(oldUser!=-1){
                    double d=Math.abs(getUser-oldUser)/oldUser; //tính su thay doi
                    if(d>0.02){
                        logger.info(""+getUser);
                        oldUser=getUser;
                        lastLogTime=now;
                    } else if ((now-lastLogTime)>=12000) {
                        logger.debug(""+getUser);
                        oldUser=getUser;
                        lastLogTime=now;
                    }
                }else{
                    logger.info(""+getUser);
                    oldUser=getUser;
                    lastLogTime=System.currentTimeMillis();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        },0,2, TimeUnit.SECONDS);
    }
}
