import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Lock {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> result = new ArrayList<Integer>();
        System.out.println("Thread Main: start.");
        for (int k = 0; k < 100; k++) {
            int threadNum = 2;
            MyThread[] threadArray = new MyThread[threadNum];
            for (int i = 0; i < threadNum; i++) {
                threadArray[i] = new MyThread(i);
                threadArray[i].start();
            }
            for (int i = 0; i < threadNum; i++) {
                threadArray[i].join();
            }
            result.add(MyThread.i);
            MyThread.i = 0;
        }
        Collections.sort(result);
        Map<Integer, Integer> map = new HashMap<>();

        for (Integer i : result) {
            if (map.containsKey(i)) {
                map.put(i, map.get(i) + 1);
            } else {
                map.put(i, 1);
            }
        }
        for(Integer k : map.keySet()){
            System.out.println(k + " is repeated " + map.get(k) + " times.");
        }
        System.out.println("Thread Main: end.");
    }
}
public class MyThread extends Thread{
    public static int i = 0;
    private int id;
    public MyThread(int id){
        this.id = id;
    }
    @Override
    public void run(){
        for (int j = 0; j < 100; j++) {
            try {
                i++;
                Thread.sleep(20);
            } catch (InterruptedException ex) {
            }
        }
    }
}
