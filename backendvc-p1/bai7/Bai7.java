package bai7;

import java.util.regex.Pattern;

public class Bai7 {
    public static boolean kiemTra(String s){
        if(!s.contains(":")){
            System.out.println("sai 1");
            return false;
        }
        else{
            String batDau=s.substring(0,s.indexOf(":"));
            if(!Pattern.matches("^http",batDau) && !Pattern.matches("^https",batDau)){
                System.out.println("sai 2");
                return false;
            }

            String url="";
            String domain="";
            int cuoi=0;
            if(s.contains("www")){
                int cham=0;
                for(int i=s.indexOf(".")+1;i<s.length();i++){
                    if((""+s.charAt(i)).equals(".")){
                        cham=i;
                        break;
                    }
                    else url+=s.charAt(i);
                }
                for(int i=cham;i<s.length();i++){
                    if((""+s.charAt(i)).equals("/")){
                        cuoi=i;
                        break;
                    }
                    else domain+=s.charAt(i);
                }
            }else{
                url=s.substring(s.indexOf(":")+3,s.indexOf("."));
                for(int i=s.indexOf(".");i<s.length();i++){
                    if((""+s.charAt(i)).equals("/")){
                        cuoi=i;
                        break;
                    }
                    else domain+=s.charAt(i);
                }
            }
            if(!Pattern.matches("[a-zA-Z0-9]+",url)){
                System.out.println(url);
                System.out.println("sai 3");
                return false;
            }
            if(!Pattern.matches("^\\.[a-z]{2,6}$",domain)){
                System.out.println("sai 4");
                return false;
            }

            String path=s.substring(cuoi);
            if(!Pattern.matches("^[^\\s]*$",path)){
                System.out.println("sai 5");
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args) {
        String s="https://www.tiki999.vnabcv/dien-thoai-may-tinh-bang/c1789?src=mega-menu%29%3A";
        if(kiemTra(s)) System.out.println("url chuẩn");
        else System.out.println("url không chuẩn");
    }
}
