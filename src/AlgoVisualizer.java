import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Stack;

public class AlgoVisualizer {

    private static int DELAY = 5;
    private static int blockSide = 10;
    private static final int d[][]={{-1,0},{0,1},{1,0},{0,-1}};

    private MazeData data;
    private AlgoFrame frame;

    public AlgoVisualizer(int N, int M){

        // 初始化数据
        data = new MazeData(N, M);
        int sceneHeight = data.N() * blockSide;
        int sceneWidth = data.M() * blockSide;

        // 初始化视图
        EventQueue.invokeLater(() -> {
            frame = new AlgoFrame("Random Maze Generation Visualization", sceneWidth, sceneHeight);
            frame.addKeyListener(new AlgoKeyListener());

            new Thread(() -> {
                run();
            }).start();
        });
    }

    private void run(){

        setData(-1,-1);
        //　递归算法
        // go(data.getEntranceX(),data.getEntranceY()+1);
        // 深度优先遍历
        // Stack<Position> stack=new Stack<Position>();
        // Position first=new Position(data.getEntranceX(), data.getEntranceY()+1);
        // stack.push(first);
        // data.visited[first.getx()][first.gety()]=true;

        // while(!stack.empty()){
        //     Position curPos=stack.pop();

        //     for(int i=0;i<4;i++){
        //         int newx=curPos.getx()+d[i][0]*2;
        //         int newy=curPos.gety()+d[i][1]*2;
        //         if(data.InArea(newx,newy)&& !data.visited[newx][newy]){
        //             stack.push(new Position(newx, newy));
        //             data.visited[newx][newy]=true;
        //             setData(curPos.getx()+d[i][0],curPos.gety()+d[i][1]);
        //         }
        //     }
        // }
        //　广度优先遍历
        // LinkedList<Position> queue=new LinkedList<Position>();
        // Position first=new Position(data.getEntranceX(), data.getEntranceY()+1);
        // queue.addLast(first);
        // data.visited[first.getx()][first.gety()]=true;

        // while(queue.size()!=0){
        //     Position curPos=queue.pop();

        //     for(int i=0;i<4;i++){
        //         int newx=curPos.getx()+d[i][0]*2;
        //         int newy=curPos.gety()+d[i][1]*2;
        //         if(data.InArea(newx,newy)&& !data.visited[newx][newy]){
        //             queue.addLast(new Position(newx, newy));
        //             data.visited[newx][newy]=true;
        //             setData(curPos.getx()+d[i][0],curPos.gety()+d[i][1]);
        //         }
        //     }
        // }
        // 更强随机性的随机队列
        RandomQueue<Position> queue=new RandomQueue<Position>();
        Position first=new Position(data.getEntranceX(), data.getEntranceY()+1);
        queue.add(first);
        data.visited[first.getx()][first.gety()]=true;
        data.openMist(first.getx(), first.gety());

        while(queue.size()!=0){
            Position curPos=queue.remove();

            for(int i=0;i<4;i++){
                int newx=curPos.getx()+d[i][0]*2;
                int newy=curPos.gety()+d[i][1]*2;
                if(data.InArea(newx,newy)&& !data.visited[newx][newy]){
                    queue.add(new Position(newx, newy));
                    data.visited[newx][newy]=true;
                    data.openMist(newx,newy);
                    setData(curPos.getx()+d[i][0],curPos.gety()+d[i][1]);
                }
            }
        }

        setData(-1,-1);

    }
    //递归算法
    private void go(int x,int y){
        if(!data.InArea(x, y)){
            throw new IllegalArgumentException("不在迷宫范围内!");
        }

        data.visited[x][y] = true;
        for(int i=0;i<4;i++){
            int newX=x+d[i][0]*2;
            int newY=y+d[i][1]*2;
            if(data.InArea(newX,newY)&& !data.visited[newX][newY]){
                setData(x+d[i][0],y+d[i][1]);
                go(newX,newY);
            }
        }
    }

    //从x，y开始走迷宫如果成功返回true
    private boolean runner(int x,int y){
        if(!data.InArea(x, y))
            throw new IllegalArgumentException("(x,y) not in this maze!");
        
        data.visited[x][y]=true;
        setData1(x,y,true);

        if(x==data.getExitX() && y==data.getExitY())
            return true;

        for(int i=0;i<4;i++){
            int newX=x+d[i][0];
            int newy=y+d[i][1];
            if(data.InArea(newX, newy)&&
                    data.getMaze(newX, newy)==MazeData.ROAD&&
                    !data.visited[newX][newy])
                if(runner(newX,newy))
                    return true;    
        }

        setData1(x, y,false);
        return false;
    }

    private void setData1(int x,int y,boolean isPath){
        if(data.InArea(x, y)){
            data.path[x][y]=isPath;
        }
        frame.render(data);
        AlgoVisHelper.pause(DELAY);
    }

    private void setData(int x,int y){
        if(data.InArea(x, y)){
            data.maze[x][y]=MazeData.ROAD;
        }
        frame.render(data);
        AlgoVisHelper.pause(DELAY);
    }

    private class AlgoKeyListener extends KeyAdapter{

        @Override
        public void keyReleased(KeyEvent event){
            if(event.getKeyChar()==' '){
                for(int i=0;i<data.N();i++)
                    for(int j=0;j<data.M();j++)
                        data.visited[i][j]=false;

                new Thread(()->{
                    runner(data.getEntranceX(),data.getEntranceY());
                }).start();
            }
        }
    }

    public static void main(String[] args) {

        int N = 101;
        int M = 101;

        AlgoVisualizer vis = new AlgoVisualizer(N, M);

    }
}