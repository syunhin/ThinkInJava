package 终结任务.入门花园场景;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class OrnamentalGarden {
    public static void main(String[] args) throws Exception{
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            exec.execute(new Entrance(i));
        }
        TimeUnit.SECONDS.sleep(3);
        Entrance.cancel();
        exec.shutdown();
        if(!exec.awaitTermination(250,TimeUnit.MILLISECONDS)){
            System.out.println("Some tasks were not terminated");
        }
        System.out.println("Total: "+Entrance.getTotalCount());
        System.out.println("Sum of Entrances: "+Entrance.sumEntrances());
    }
}

class Entrance implements Runnable {
    private static Count count = new Count();
    private static List<Entrance> entrances = new ArrayList<Entrance>();

    private int number = 0;

    //读取无需synchronized同步控制
    private final int id;
    private static volatile boolean canceled = false;

    //对一个volatile变量原子操作
    public static void cancel() {
        canceled = true;
    }

    public Entrance(int id) {
        this.id = id;
        //将此清单保留在列表中。还可以防止垃圾收集已完成的任务；
        entrances.add(this);
    }

    @Override
    public void run() {
        while (!canceled) {
            synchronized (this) {
                ++number;
            }
            System.out.println(this + " Total: " + count.increment());
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("sleep interrupted");
            }
        }
        System.out.println("Stopping " + this);
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
    private Random rand = new Random(47);

    //去掉synchronized模拟失败的场景
    public synchronized int increment() {
        //此处使用temp也是增加synchronized移除后失败几率。
        int temp = count;
        if (rand.nextBoolean()) {
            //有50%几率让出，但是有synchronized时不会释放锁，因此并无效果
            //此处实际上是为了将synchronized移除后，可以模拟失败的情况
            Thread.yield();
        }
        return (count = ++temp);
    }

    public synchronized int value() {
        return count;
    }
}
