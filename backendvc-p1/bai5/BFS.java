package bai5;

import java.util.*;

public class BFS {
    private int[][] maze;
    private Point start;
    private Point end;
    private int sizeh,sizew;
    public static List<Point> danhDau =new ArrayList<>();
    public static Map<Point,Point> truoc=new HashMap<>();
    public static Queue<Point> queue=new LinkedList<>();

    public BFS(int[][] maze, Point start, int sizeh,int sizew) {
        this.maze = maze;
        this.start = start;
        this.sizeh=sizeh;
        this.sizew=sizew;
        queue.add(start);
        danhDau.add(start);
    }
    public void timDuongDi(){
        while(!queue.isEmpty()){
            Point p=queue.poll();
            int i=p.getX();
            int j=p.getY();
            if(maze[i][j]==2) this.end=new Point(i,j);
            if(i>0){
                if((maze[i-1][j]==0 || maze[i-1][j]==2) && !danhDau.contains(new Point(i-1,j))){
                    capNhatMap(new Point(i-1,j), p);
                }
            }
            if(j>0){
                if((maze[i][j-1]==0 || maze[i][j-1]==2) && !danhDau.contains(new Point(i,j-1))){
                    capNhatMap(new Point(i,j-1), p);
                }
            }
            if(i<sizeh-1){
                if((maze[i+1][j]==0 || maze[i+1][j]==2) && !danhDau.contains(new Point(i+1,j))){
                    capNhatMap(new Point(i+1,j), p);
                }
            }
            if(j<sizew-1){
                if((maze[i][j+1]==0 || maze[i][j+1]==2) && !danhDau.contains(new Point(i,j+1))){
                    capNhatMap(new Point(i,j+1), p);
                }
            }
        }
    }
    public void capNhatMap(Point sau, Point trc){
        danhDau.add(sau);
        queue.add(sau);
        truoc.put(sau, trc);
    }
    public Map<Point, Point> getTruoc() {
        return truoc;
    }

    public void setTruoc(Map<Point, Point> truoc) {
        this.truoc = truoc;
    }

    public Point getEnd() {
        return end;
    }

    public void setEnd(Point end) {
        this.end = end;
    }
}
