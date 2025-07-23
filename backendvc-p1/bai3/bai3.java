package bai3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class bai3 {
    static ConcurrentSkipListMap<String,Integer> dem=new ConcurrentSkipListMap<>();
    static long dai=0;
    static String tuDai;
    static String tuNgan;
    static long ngan=Long.MAX_VALUE;
    static long tong=0;
    static final ReentrantLock lock = new ReentrantLock();

    public static void xuly(String s){
        String[] str=s.split("[\\s.,!=+\\-—()\"']+");
        for(String x:str){
            if(x.length()>=3){
                dem.compute(x,(k,v) -> (v==null) ? 1:(v+1));
                lock.lock();
                try {
                    if(dem.get(x)==1) {
                        if(x.length()>dai){
                            dai=x.length();
                            tuDai=x;
                        }
                        if(x.length()<ngan){
                            ngan=x.length();
                            tuNgan=x;
                        }
                        tong+=x.length();
                    }
                }finally {
                    lock.unlock();
                }
            }
        }
    }
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        ExecutorService executorService= Executors.newFixedThreadPool(6);
        File folder =new File("E:\\backendvc\\bai3\\input_3");
        File[] listFile=folder.listFiles();
        List<CompletableFuture<Void>> futures=new ArrayList<>();
        AtomicInteger check1= new AtomicInteger();
        if(listFile!=null){
            for(File file:listFile){
                Callable<Integer> callable=new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        Scanner in=new Scanner(file);
                        while(in.hasNextLine()){
                            String s=in.nextLine();
                            xuly(s);
                        }
                        return check1.get();
                    }
                };
                CompletableFuture<Void> future=CompletableFuture.supplyAsync(()->{
                    try {
                        callable.call();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return check1.getAndIncrement();
                },executorService).thenAccept((ch) -> {
                    System.out.println("Đã hoàn thành: "+(double)(ch+1)/ listFile.length*100.0+ "%");
                });
                futures.add(future);
            }
        }
        CompletableFuture<Void> all = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );
        all.join();
        //completablefuture join chỉ chạy đến supplyAsync là tính hoàn thành, accept
        while(check1.get()!=listFile.length){

        }
        Set<String> stringSet=dem.keySet();
        List<String> l=new ArrayList<>();
        l.addAll(stringSet);
        l.sort((o1, o2) -> Integer.compare(dem.get(o1), dem.get(o2)));
        int k=0;
        for(int i= stringSet.size()-1;i>=0;i--){
            System.out.println(l.get(i)+ " "+ dem.get(l.get(i)));
            k++;
            if(k==10) break;
        }
        for(int i=0;i<l.size();i++){
            System.out.println(l.get(i)+ " "+ dem.get(l.get(i)));
            k++;
            if(k==20) break;
        }
        System.out.println("Tu dai: "+tuDai);
        System.out.println("Tu ngan: "+tuNgan);

        if(stringSet.size()!=0) System.out.println("Trung bình: "+ tong/stringSet.size());

        executorService.shutdown();
    }
}
