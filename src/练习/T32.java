package 练习;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class T32 {
    public static void main(String[] args) throws InterruptedException {
        int size = 10;

        CountDownLatch enterLatch = new CountDownLatch(size);//用于计数 入口关闭
         CountDownLatch closeLatch = new CountDownLatch(1);//用于判断是否结束
         CountDownLatch startLatch = new CountDownLatch(1);//判断是否开始

        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < size; i++) {
            exec.execute(new Entrance(enterLatch,closeLatch,startLatch,i));
        }

        //开始任务
        startLatch.countDown();
        TimeUnit.SECONDS.sleep(1);
        exec.shutdown();

        //结束任务
        closeLatch.countDown();

        //等待计数
        enterLatch.await();
        System.out.println("Total: "+ Entrance.getTotalCount());
        System.out.println("Sum of Entrances: "+ Entrance.sumEntrances());
    }
}

class Entrance implements Runnable {
    private static Count count = new Count();
    private static List<Entrance> entrances = new ArrayList<Entrance>();
    private final CountDownLatch enterLatch;//用于计数 入口关闭
    private CountDownLatch closeLatch;//用于判断是否结束
    private CountDownLatch startLatch;//判断是否开始

    private int number = 0;

    //读取无需synchronized同步控制
    private final int id;

    public Entrance(CountDownLatch enterLatch, CountDownLatch closeLatch, CountDownLatch startLatch,int id) {
        this.enterLatch = enterLatch;
        this.closeLatch = closeLatch;
        this.startLatch = startLatch;
        this.id = id;
        entrances.add(this);
    }

    @Override
    public void run() {
        try {
            startLatch.await();
            while (closeLatch.getCount() != 0) {
                synchronized (this) {
                    ++number;
                }
                System.out.println(this + " Total: " + count.increment());

                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch (InterruptedException e) {
            System.out.println("sleep interrupted");
        }
        System.out.println("Stopping " + this);
        enterLatch.countDown();
    }

    public synchronized int getValue() {
        return number;
    }

    @Override
    public String toString() {
        return "Entrance " + id +
                ": " + getValue();
    }

    public static int getTotalCount() {
        return count.value();
    }

    public static int sumEntrances() {
        int sum = 0;
        for (Entrance entrance : entrances) {
            sum += entrance.getValue();
        }
        return sum;
    }
}

class Count {
    private int count = 0;

    public synchronized int increment() {
        int temp = count;
        return (count = ++temp);
    }

    public synchronized int value() {
        return count;
    }
}