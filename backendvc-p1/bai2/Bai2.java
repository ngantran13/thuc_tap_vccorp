package bai2;

import java.io.*;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class Bai2 {
    public static void main(String[] args) throws IOException {
        Scanner in=new Scanner(new FileReader("E:\\backend\\01.txt"));
        TreeMap<String, Integer> dem=new TreeMap<>();
        while(in.hasNextLine()){
            String s=in.nextLine();
            String[] str=s.split("[\\s.,!=+\\-—()\"']+");
            for(String x:str){
                if(x.length()>=3){
                    if(!dem.containsKey(x)) {
                        dem.put(x,1);
                    }else{
                        dem.put(x,dem.get(x)+1);
                    }
                }
            }
        }
        FileWriter out=new FileWriter("bai2/output.txt");
        Set<String> ghi=dem.keySet();
        for(String x:ghi){
            String s=x+ " " + dem.get(x);
            out.write(s);
            out.write("\n");
        }
        in.close();
        out.close();
    }
}
