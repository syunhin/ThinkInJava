package 线程的基本定义与操作.休眠;

import java.util.concurrent.TimeUnit;

/**
 * sleep()不会释放锁
 */
public class SleepForLock implements Runnable{
    public static synchronized void count(){
        try {
            TimeUnit.MINUTES.sleep(2);
            System.out.println(Thread.currentThread().getName()+" awake up");
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println(Thread.currentThread().getName()+" Interrupted");
        }
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+" running");
        count();
    }

    public static void main(String[] args) {
        Thread a = new Thread(new SleepForLock(),"a");
        Thread b = new Thread(new SleepForLock(),"b");
        a.start();
        b.start();
        a.interrupt();
    }
}
