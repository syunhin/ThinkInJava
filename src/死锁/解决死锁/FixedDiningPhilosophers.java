package 死锁.解决死锁;

import 死锁.哲学家就餐问题.Chopstick;
import 死锁.哲学家就餐问题.Philosopher;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * 要修正死锁，你必须明白，当以下4个条件同时满足时，就会发生死锁：
 *
 * 互斥条件。任务使用的资源中至少有一个是不能共享的。
 *      此处的例子为筷子，一个Chopstick对象只能给一个Philosopher使用
 * 至少有一个任务它必须持有一个资源且正在等待获取一个当前被别的任务持有的资源；
 *      也就是说，要发生死锁，一个Philosopher必须拿着一个Chopstick并且等待另一个。
 * 资源不能被任务抢占，任务必须把资源释放当作普通事件；
 *      Pholosopher很有礼貌，不会从其他的ph那里抢Chopstick。
 * 必须有循环等待。
 *
 * 所以防止死锁，只需破坏其中一个即可。
 */
public class FixedDiningPhilosophers {
    public static void main(String[] args) throws IOException {
        int ponder = 0;
        int size = 5;

        ExecutorService exec = Executors.newCachedThreadPool();
        Chopstick[] sticks = new Chopstick[size];
        for (int i = 0; i < size; i++) {
            sticks[i] = new Chopstick();
        }

        for (int i = 0; i < size; i++) {
            if(i< (size-1)){
                exec.execute(new Philosopher(sticks[i],sticks[i+1],i,ponder));
            }else{
                exec.execute(new Philosopher(sticks[0],sticks[1],i,ponder));
            }
        }

        System.out.println("Press 'Enter' to quit");
        System.in.read();

        exec.shutdownNow();
    }
}
