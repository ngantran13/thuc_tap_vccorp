package bai4;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Bai4 {
    public static void main(String[] args) throws IOException {
        Set<Point> setA=new HashSet<>();
        Set<Point> setB=new HashSet<>();
        Set<Point> setC=new HashSet<>();
        Set<Point> tong=new HashSet<>();

        while(setA.size()<8000){
            int x=(int)(Math.random() * (1000 - 1 + 1)) + 1;
            int y=(int)(Math.random() * (1000 - 1 + 1)) + 1;
            Point p1=new Point(x,y);
            if(p1.tinhKhoangCach(new Point(800,800))<=(400*400)){
                if(!setB.contains(p1)&&!setC.contains(p1)){
                    setA.add(p1);
                    tong.add(p1);
                }
            }
        }

        while(setB.size()<10000){
            int x=(int)(Math.random() * (10000 - 1 + 1)) + 1;
            int y=(int)(Math.random() * (1000 - 1 + 1)) + 1;
            Point p1=new Point(x,y);
            if(p1.tinhKhoangCach(new Point(4000,800))<=(500*500)){
                if(!setC.contains(p1)&&!setA.contains(p1)){
                    setB.add(p1);
                    tong.add(p1);
                }
            }
        }

        while(setC.size()<12000){
            int x=(int)(Math.random() * (10000 - 1 + 1)) + 1;
            int y=(int)(Math.random() * (10000 - 1 + 1)) + 1;
            Point p1=new Point(x,y);
            if(p1.tinhKhoangCach(new Point(2400,2400))<=(600*600)){
                if(!setB.contains(p1)&&!setA.contains(p1)){
                    setC.add(p1);
                    tong.add(p1);
                }
            }
        }
        FileWriter out=new FileWriter("bai4/output.txt");
        for(Point p:tong){
            String s="("+p.getX()+","+p.getY()+")";
            out.write(s);
            out.write("\n");
        }
        out.close();
    }
}
