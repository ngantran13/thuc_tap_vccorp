package bai1;

import java.util.*;

public class Bai1 {
    public static void main(String[] args) {
        Set<Integer> setA=new HashSet<>();
        Set<Integer> setB=new HashSet<>();
        Set<Integer> setC=new HashSet<>();
        while(setA.size()<5000000){
            int x=(int)(Math.random() * (10000000 - 1 + 1)) + 1;
            setA.add(x);
        }
        while(setB.size()<7000000){
            int x=(int)(Math.random() * (15000000 - 5000000 + 1)) + 5000000;
            setB.add(x);
        }
        while(setC.size()<6000000){
            int x=(int)(Math.random() * (18000000 - 8000000 + 1)) + 8000000;
            setC.add(x);
        }
        long dem1 = setA.parallelStream().filter(setB::contains).count();
        System.out.println(dem1);

        long dem2 = setB.parallelStream().filter(setC::contains).count();
        System.out.println(dem2);

        Set<Integer> setD=new HashSet<>();
        setD.addAll(setA);
        setD.addAll(setB);
        setD.addAll(setC);

        setA.retainAll(setB);
        setA.retainAll(setC);

        System.out.println(setD.size()-setA.size());

    }


}
